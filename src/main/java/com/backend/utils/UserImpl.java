package com.backend.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.backend.applications.ChatServerEndPoint;
import com.backend.model.User;

public class UserImpl 
{
	protected static final Map<String, User> allUsers = Collections.synchronizedMap(new HashMap<String,User>());
	private static Logger log = Logger.getLogger(UserImpl.class);
	
	static {
		allUsers.put("ADMIN_USER",new User("ADMIN_USER", "admin"));
		allUsers.put("madhu.ml193@gmail.com", new User("madhu.ml193@gmail.com","abc") );
		allUsers.put("kch@gmail.com",new User("kch@gmail.com", "kch"));
		allUsers.put("madhu@gmail.com", new User("madhu@gmail.com","abc") );
		allUsers.put("kch1@gmail.com",new User("kch1@gmail.com", "kch"));
		allUsers.put("madhu3@gmail.com", new User("madhu3@gmail.com","abc") );
		allUsers.put("kch2@gmail.com",new User("kch2@gmail.com", "kch"));	
		ChatServerEndPoint.generateMessageThreadsToAllUsers(allUsers.keySet().iterator());
		log.debug("user objects created");
	}
	
	private UserImpl()
	{
		super();
	}
	
	public static boolean authenticate(String email,String password)
	{
		return allUsers.get(email).getPassword().equals(password);
	}
	

	
}
