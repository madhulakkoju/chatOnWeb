package com.backend.model;

public class User 
{
	final String email;
	String password;
	
	public User(String email,String pwd)
	{
		this.email = email;
		this.password = pwd;
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
