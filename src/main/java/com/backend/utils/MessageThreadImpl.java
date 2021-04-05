package com.backend.utils;

import java.util.HashMap;
import java.util.List;

import com.backend.model.MessageThread;

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
