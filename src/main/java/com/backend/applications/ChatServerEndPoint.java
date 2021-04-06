package com.backend.applications;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.backend.model.Message;
import com.backend.model.MessageThread;

import com.backend.utils.MessageEncoder;
import com.backend.utils.MessageDecoder;
import com.backend.utils.MessageThreadEncoder;
import com.backend.utils.MessageThreadDecoder;



@ServerEndpoint(value="/helpChat/{userEmail}", encoders = {MessageEncoder.class,MessageThreadEncoder.class}, decoders = {MessageDecoder.class,MessageThreadDecoder.class})
public class ChatServerEndPoint 
{
	public static final Map<String, MessageThread> messageThreads = Collections.synchronizedMap(new HashMap<String,MessageThread>());
	
	public static final Map<String, Session> users = Collections.synchronizedMap(new HashMap<String,Session>());
	
	public static Session adminSession = null;
	
	public static Queue<Message> messageQueue = new LinkedList<Message>();
	
	public static MessageThread getThread(String email)
	{
		MessageThread thread = messageThreads.get(email);
		if(thread != null)
			return thread;
		thread = new MessageThread(email);
		messageThreads.put(email,thread);
		return thread;
	}
	
	public static void sendMessageThread(Session session, String email)
	{
		MessageThread thread = getThread(email);
		Basic remote = session.getBasicRemote();
		for(Message m : thread.getMessages())
		{
			try {
				remote.sendText(m.getSender()+" ::: "+m.getText());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	@OnOpen
	public void onOpen(final Session session, @PathParam("userEmail") String email) {
		System.out.println("Opening new Session/Connection");
		Message m;
		//Checking if the user is ADMIN
		if(email.equals("ADMIN_USER"))
		{
			adminSession = session;
			session.setMaxIdleTimeout(1*60*1000);
			session.getUserProperties().putIfAbsent("userEmail", email);
			// send the messages that were on the queue
			while(! messageQueue.isEmpty())
			{
				try {
					m = messageQueue.poll();
					session.getBasicRemote().sendObject( m);
				} catch (IOException | EncodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Admin Logged in   at    "+adminSession);
			return ;
		}
		//Non - Admin User
		session.setMaxIdleTimeout(5*60*1000);
		session.getUserProperties().putIfAbsent("userEmail",email);
		/*
		MessageThread thread = getThread(email);
		
		try {
			// sending previous Chat / Help Data 
			session.getBasicRemote().sendObject(thread);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		try {
			session.getBasicRemote().sendObject( new Message("System","Welcome"));
			users.put(email, session);
			//send all the previous messages to the User/Client
			sendMessageThread(session,email);
		} catch (IOException | EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OnMessage
	public void onMessage(Message message,Session session)
	{
		System.out.println(message.getSender()+"  ;;  "+message.getText());
		// message format being receiver ::: message
		String sender = (String) session.getUserProperties().get("userEmail");
		
		//String[] msgArgs = message.split(" ::: ");
		//[0] is receiver
		//[1] is message
		
		String[] msgArgs = {message.getSender(),message.getText()};
		
		//Sender is Admin
		if(sender.equals("ADMIN_USER"))
		{
			message.setSender("ADMIN_USER");
			//Admin sends message in format of Receiver ::: message
			// so string sender is receiver here
			if(users.get(msgArgs[0])==null)
			{
				System.out.println(msgArgs[0] + " Offline");
			}
			else
			{
				try {
					
				users.get(msgArgs[0]).getBasicRemote().sendObject(message);
				} catch (EncodeException |IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// save this message to messageThread
			
			getThread(msgArgs[0]).addMessage(message);
			
			return;
		}
		
		else
		{
			// Admin ONLINE
			if(adminSession != null)
			{
				//Send to Admin
				try {
					adminSession.getBasicRemote().sendObject(message);
				} catch (IOException | EncodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// Admin Not ONLINE
			else
			{
				//Add to message Queue and also to Message Thread
				messageQueue.add(message);
				
			}
			getThread(sender).addMessage(message);
		}
	} 
	
	@OnClose
	public void onClose(Session session, CloseReason reason)
	{
		if(session == adminSession)
		{
			System.out.println("Session removed "+ session.getUserProperties().get("userEmail"));
			adminSession = null;
		}
		else
		{
			System.out.println("Session removed "+ session.getUserProperties().get("userEmail"));
			users.remove(session.getUserProperties().get("userEmail"));
			
		}	
		return;
	}
	
    @OnError
    public void onError(Session session, @PathParam("userEmail") String email, Throwable throwable) {
        System.out.println(throwable.getMessage());
    }
	
}
