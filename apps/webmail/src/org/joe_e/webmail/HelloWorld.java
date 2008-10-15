package org.joe_e.webmail;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.catalina.connector.RequestFacade;

public class HelloWorld extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws IOException, ServletException {
		// RequestFacade req = (RequestFacade) request;
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		String user = (String) request.getSession().getAttribute("user");
		
		HtmlWriter.printHeader(out);
		if (user != null) {
			response.sendRedirect("/webmail/inbox");
		}
		out.println("<body><h1>Welcome to WebMail</h1>");
		out.println("<p>"+request.getClass()+"</p>");
		out.println("<a href=\"/webmail/create\">Create Account</a>");
		out.println("<a href=\"/webmail/login\">Login</a>");
		out.println("<a href=\"/webmail/webform\">Click Here</a>");
		
		out.println("</body>");
		HtmlWriter.printFooter(out);
	}
}
