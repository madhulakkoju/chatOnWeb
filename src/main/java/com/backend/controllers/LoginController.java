package com.backend.controllers;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.backend.utils.UserImpl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;


@WebServlet("/login")
public class LoginController extends HttpServlet {
	
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if(UserImpl.authenticate((String)request.getParameter("email"),(String)request.getParameter("password")))
		{
			HttpSession session = request.getSession();
			session.setAttribute("loggedIn", true);
			session.setAttribute("userEmail",(String)request.getParameter("email") );
			System.out.println("User Authenticated");
			
			if(session.getAttribute("userEmail").equals("ADMIN_USER"))
			{
				RequestDispatcher dispatcher = request.getRequestDispatcher("/adminChatPage.html");
				dispatcher.forward(request,response);
			}
			else
			{
				RequestDispatcher dispatcher = request.getRequestDispatcher("/chat.html");
				dispatcher.forward(request,response);
			}
		}
	}
}
