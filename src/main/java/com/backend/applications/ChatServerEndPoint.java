package com.backend.applications;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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

import org.apache.log4j.Logger;

import com.backend.model.Message;
import com.backend.model.MessageThread;

import com.backend.utils.MessageEncoder;
import com.backend.utils.MessageDecoder;
import com.backend.utils.MessageThreadEncoder;
import com.backend.utils.MessageThreadDecoder;



@ServerEndpoint(value="/helpChat/{userEmail}", encoders = {MessageEncoder.class,MessageThreadEncoder.class}, decoders = {MessageDecoder.class,MessageThreadDecoder.class})
public class ChatServerEndPoint 
{
	static String userEmail = "userEmail";
	
	static String adminUserName = "ADMIN_USER";
	
	private static Logger log = Logger.getLogger(ChatServerEndPoint.class);
	
	protected static final Map<String, MessageThread> messageThreads = Collections.synchronizedMap(new HashMap<String,MessageThread>());
	
	protected static final Map<String, Session> users = Collections.synchronizedMap(new HashMap<String,Session>());
	
	private static  Session adminSession = null;
	
	private static Queue<Message> messageQueue = new LinkedList<>();

	public static void generateMessageThreadsToAllUsers(Iterator<String> iterator)
	{
		String email;
		while(iterator.hasNext()) {
			email = iterator.next();
			messageThreads.putIfAbsent(email, new MessageThread(email));
		}
		log.debug("Generated Empty Message Threads to users if needed");
	}

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
		
		try {
			remote.sendObject(thread);
		} catch (IOException | EncodeException e) {
			log.error("Cannot send Message Thread to "+ email);
			e.printStackTrace();
		}
		log.debug("Sent Message Thread user");
	}
	
	@OnOpen
	public void onOpen(final Session session, @PathParam("userEmail") String email) {
		log.debug("Opening new Session/Connection");
		
		//Checking if the user is ADMIN
		if(email.equals(adminUserName))
		{
			log.debug("ADMIN logged in");
			adminSession = session;
			session.setMaxIdleTimeout((long)5*60*1000);
			session.getUserProperties().putIfAbsent(userEmail, email);
			// send the messages that were on the queue
			
			for(MessageThread thread : messageThreads.values() )
			{
				try {
					session.getBasicRemote().sendObject(thread);
				} catch (IOException | EncodeException e) {
					e.printStackTrace();
					log.error("Cannot send MessageThread");
				}
			}
			
			log.debug("Admin Logged in   at    "+adminSession);
			return ;
		}
		//Non - Admin User
		session.setMaxIdleTimeout( (long)5 * 60 * 1000);
		session.getUserProperties().putIfAbsent(userEmail,email);
		log.debug("User Logged in");
		MessageThread thread = getThread(email);
		try {
			session.getBasicRemote().sendObject(thread);
			session.getBasicRemote().sendObject( new Message("System","Welcome"));
			users.put(email, session);
			//send all the previous messages to the User/Client
			sendMessageThread(session,email);
		} catch (IOException | EncodeException e) {
			e.printStackTrace();
		}
	}
	
	@OnMessage
	public void onMessage(Message message,Session session)
	{
		log.debug(message.getSender()+"  ;;  "+message.getText());
		// message format being receiver ::: message
		String sender = (String) session.getUserProperties().get(userEmail);
		
		//[0] is receiver
		//[1] is message
		log.debug("Message Received at serrver");

		String[] msgArgs = {message.getSender(),message.getText()};
		
		//Sender is Admin
		if(sender.equals(adminUserName))
		{
			message.setSender(adminUserName);
			//Admin sends message in format of Receiver ::: message
			// so string sender is receiver here
			if(users.get(msgArgs[0])==null)
			{
				log.info(msgArgs[0] + " Offline");
			}
			else
			{
				try {
					
				users.get(msgArgs[0]).getBasicRemote().sendObject(message);
				} catch (EncodeException |IOException e) {
					e.printStackTrace();
				}
			}
			// save this message to messageThread
			
			getThread(msgArgs[0]).addMessage(message);
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
		log.debug(session.getUserProperties().get(userEmail) + "about to close");
		if(session == adminSession)
		{
			log.debug("Session removed "+ session.getUserProperties().get(userEmail));
			adminSession = null;
		}
		else
		{
			log.debug("Session removed "+ session.getUserProperties().get(userEmail));
			users.remove(session.getUserProperties().get(userEmail));
			
		}	
		log.debug("Session closed");

	}
	
    @OnError
    public void onError(Session session, @PathParam("userEmail") String email, Throwable throwable) {
        log.error(throwable.getMessage());
    }
	
}
