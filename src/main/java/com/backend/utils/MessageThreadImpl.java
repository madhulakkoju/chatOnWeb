package com.backend.utils;

import java.util.HashMap;
import com.backend.model.MessageThread;


public class MessageThreadImpl 
{
	
	HashMap<String,MessageThread> allMessageThreads;
	public MessageThreadImpl()
	{
		allMessageThreads = new HashMap<>();

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

}
