package com.backend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageThread implements Serializable
{
	String participant;
	List<Message> messagesList;
	boolean replied;
	
	public MessageThread(String participant)
	{
		this.participant = participant;
		messagesList = new ArrayList<Message>(10);
		this.addMessage(new Message("SYSTEM","Hello "+participant));
		replied = false;
	}
	
	public void addMessage(Message message)
	{
		messagesList.add(message);
		if(message.getSender().equalsIgnoreCase(participant))
			replied = false;
		else if(message.getSender().equalsIgnoreCase("ADMIN_USER"))
			replied = true;
	}
	
	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
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

	public boolean isReplied() {
		return replied;
	}

	public void setReplied(boolean replied) {
		this.replied = replied;
	}
	
}
