package com.backend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageThread implements Serializable
{
	String participant;
	String admin;
	List<Message> messagesList;
	boolean replied;
	
	public MessageThread(String participant)
	{
		this.participant = participant;
		messagesList = new ArrayList<Message>(10);
		replied = false;
	}
	
	public void addMessage(Message message)
	{
		messagesList.add(message);
		if(message.getSender().equalsIgnoreCase(participant))
			replied = false;
		else if(message.getSender().equalsIgnoreCase(participant))
			replied = true;
	}
	
	public List<Message> getMessages()
	{
		return this.messagesList;
	}
	
	public void print()
	{
		System.out.println("----------------CHAT-----------------");
		for(Message m: messagesList)
		{
			System.out.println(m.getSender()+" : "+m.getText());
		}
	}
	
}
