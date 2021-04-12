package com.backend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class MessageThread implements Serializable
{
	private static Logger log = Logger.getLogger(MessageThread.class);
	private final String participant;
	private List<Message> messagesList;
	boolean replied;
	
	public MessageThread(String participant)
	{
		this.participant = participant;
		messagesList = new ArrayList<>(10);
		this.addMessage(new Message("SYSTEM","Hello "+participant));
		replied = false;
		log.debug("Message Thread Object Created for User : " + participant);
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

	public List<Message> getMessages()
	{
		return this.messagesList;
	}
	
	public boolean isReplied() {
		return replied;
	}

	public void setReplied(boolean replied) {
		this.replied = replied;
	}
	
}
