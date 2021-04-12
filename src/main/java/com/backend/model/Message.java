package com.backend.model;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class Message implements Serializable
{
	@JsonProperty("Sender")
	String sender;
	
	@JsonProperty("MessageBody")
	String text;
	
	public Message() {
		super();
	}

	@JsonIgnore
	Date dt;
		
	public Message(String sender, String text, Date dt) {
		super();
		this.sender = sender;
		this.text = text;
		this.dt = dt;

	}

	public Message(String sender)
	{
		this.sender = sender;
	}
	
	public Message(String sender, String text)
	{
		this.sender= sender;
		this.text = text;
		this.dt = new Date();
	}


	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public Date getDt() {
		return dt;
	}


	public void setDt(Date dt) {
		this.dt = dt;
	}
	
	
	
}
