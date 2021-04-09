package com.backend.utils;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.FileAppender;

import com.backend.model.MessageThread;

import jdk.internal.org.jline.utils.Log;

public class MessageThreadImpl 
{
	HashMap<String,MessageThread> allMessageThreads;
	public MessageThreadImpl()
	{
		allMessageThreads = new HashMap<String,MessageThread>();

	}
	
	public MessageThread getMessageThread(String participant)
	{
		MessageThread thread = allMessageThreads.get(participant);
		if(thread == null)
			return createMessageThread(participant);
		return thread;
	}
	
	public void deleteMessageThread(String participant)
	{
		allMessageThreads.remove(participant);
		Log.debug("Message Thread Removed "+participant);
	}
	
	public MessageThread createMessageThread(String participant)
	{
		MessageThread thread = new MessageThread(participant);
		allMessageThreads.put(participant, thread);
		return thread;
	}
	public void print()
	{
		System.out.println("All Message Threads");
		for(String sender : allMessageThreads.keySet() )
		{
			System.out.println("======>   USER : "+sender);
			allMessageThreads.get(sender).print();
		}
		System.out.println("Message Threads DONE");
	}
}
