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
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.codehaus.jackson.map.ObjectMapper;

import com.backend.model.Message;
import com.backend.model.MessageThread;

import com.backend.utils.MessageEncoder;
import com.backend.utils.MessageDecoder;
import com.backend.utils.MessageThreadEncoder;
import com.backend.utils.UserImpl;
import com.backend.utils.MessageThreadDecoder;



@ServerEndpoint(value="/helpChat/{userEmail}", encoders = {MessageEncoder.class,MessageThreadEncoder.class}, decoders = {MessageDecoder.class,MessageThreadDecoder.class})
public class ChatServerEndPoint 
{
	public static final Map<String, MessageThread> messageThreads = Collections.synchronizedMap(new HashMap<String,MessageThread>());
	
	public static final Map<String, Session> users = Collections.synchronizedMap(new HashMap<String,Session>());
	
	public static Session adminSession = null;
	
	public static Queue<String> messageQueue = new LinkedList<String>();
	
	public static MessageThread getThread(String email)
	{
		MessageThread thread = messageThreads.get(email);
		if(thread != null)
			return thread;
		thread = new MessageThread(email);
		messageThreads.put(email,thread);
		return thread;
	}
	
	@OnOpen
	public void onOpen(final Session session, @PathParam("userEmail") String email) {
		System.out.println("Open new");
		if(email.equals("ADMIN_USER"))
		{
			adminSession = session;
			session.setMaxIdleTimeout(3*60*1000);
			session.getUserProperties().putIfAbsent("userEmail", email);
			/*
			Map<String,MessageThread> threads = messageThreads;
			try {
				session.getBasicRemote().sendObject(threads);
			} catch (IOException | EncodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			while(! messageQueue.isEmpty())
			{
				try {
					session.getBasicRemote().sendText(messageQueue.poll());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.out.println("Admin Logged in   at    "+adminSession);
			return ;
		}
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
			session.getBasicRemote().sendObject(new Message("System","Hello Welcome"));
			
			users.put(email, session);
			
		} catch (IOException | EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@OnMessage
	public void onMessage(String message,Session session)
	{
		//System.out.println("Message " + message.getSender());
		System.out.println(message);
		
		Session receiver = users.get("Madhu@gmail.com");
		if(session == adminSession)
		{
			
			if(receiver == null)
			{
				System.out.println("User Not Online");
			}
			try {
				receiver.getBasicRemote().sendText( message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		
		
		
		if(adminSession != null)
		{
			try {
				
				adminSession.getBasicRemote().sendText(receiver.getUserProperties().get("userEmail") +" : "+message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			
			messageQueue.add(receiver.getUserProperties().get("userEmail") +" : "+message);
			try {
				session.getBasicRemote().sendText("Admin is not available as of now. Please wait until we get back to you");
				System.out.println("Admin is not available as of now. Please wait until we get back to you");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
