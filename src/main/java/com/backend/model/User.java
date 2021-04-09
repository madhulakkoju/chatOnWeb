package com.backend.model;

import org.apache.log4j.Logger;

public class User 
{
	private static Logger log = Logger.getLogger(User.class);
	final String email;
	String password;
	
	public User(String email,String pwd)
	{
		this.email = email;
		this.password = pwd;
		log.debug("New User Registered : " + email);
	}
	
	public String getEmail()
	{
		return this.email;
	}
	public String getPassword()
	{
		return this.password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
}
