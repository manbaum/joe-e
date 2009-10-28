package org.joe_e.servlet.mail;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joe_e.servlet.AbstractCookieView;
import org.joe_e.servlet.AbstractSessionView;
import org.joe_e.servlet.Dispatcher;
import org.joe_e.servlet.JoeEServlet;
import org.joe_e.servlet.readonly;

//TODO: how do you invalidate the session? on logout really
//      you should just invalidate so that you can get back
//      things like the AuthenticationAgent
public class Logout extends JoeEServlet {

	public class SessionView extends AbstractSessionView {
		public boolean invalidate;
	}
	
	public class CookieView extends AbstractCookieView {
	}
	
	//TODO: need to make it invalidate the session.
	public void doGet(HttpServletRequest req, HttpServletResponse res, AbstractSessionView ses, AbstractCookieView cookies)
		throws IOException, ServletException {
	    Dispatcher.logger.fine("in doGet of Logout");
		SessionView session = (SessionView) ses;
		session.invalidate = true;
		res.sendRedirect("/servlet/");
		return;
	}
}
