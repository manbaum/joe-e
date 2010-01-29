package org.joe_e.servlet.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joe_e.charset.ASCII;
import org.joe_e.file.Filesystem;
import org.joe_e.servlet.AbstractCookieView;
import org.joe_e.servlet.AbstractSessionView;
import org.joe_e.servlet.JoeEServlet;
import org.joe_e.servlet.Dispatcher;
import org.joe_e.servlet.response.ResponseDocument;
import org.joe_e.servlet.response.ResponseElement;
import org.joe_e.servlet.response.ResponseUrl;
import org.joe_e.servlet.response.ServletResponseWrapper;

public class Inbox extends JoeEServlet {


	public class SessionView extends AbstractSessionView {
		public SessionView(HttpSession ses) {
			super(ses);
		}
		public String getUsername() {
			return (String) session.getAttribute("__joe-e__username");
		}
		public File getMailbox() {
			return (File) session.getAttribute("__joe-e__mailbox");
		}
	}
	
	public AbstractSessionView getSessionView(HttpSession ses) {
		return new SessionView(ses);
	}
	
	public class CookieView extends AbstractCookieView {
		public CookieView(Cookie[] c) {
			super(c);
		}
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res, AbstractSessionView ses, AbstractCookieView c)
		throws IOException, ServletException {
		SessionView session = (SessionView) ses;
		if (session.getUsername() == null) {
		    Dispatcher.logMsg("redirecting to /servlet/login");
		    res.sendRedirect("/servlet/login");
		    return;
		}
		res.addHeader("Content-type", "text/html");
		ResponseDocument doc = ((ServletResponseWrapper) res).getDocument();
		ResponseElement body = HtmlWriter.printHeader(doc);
		ResponseElement tmp = doc.createElement("h2");
		tmp.appendChild(doc.createTextNode("Joe-E Mail"));
		body.appendChild(tmp);
		tmp = doc.createElement("h4");
		tmp.appendChild(doc.createTextNode("inbox of " + session.getUsername()));
		body.appendChild(tmp);
		
		tmp = doc.createElement("a");
		tmp.addLinkAttribute("href", new ResponseUrl("/servlet/compose", null));
		tmp.appendChild(doc.createTextNode("Write an email"));
		body.appendChild(tmp);
		body.appendChild(doc.createElement("br"));
		

		File maildir = Filesystem.file(session.getMailbox(), "Maildir");
		File newFolder = Filesystem.file(maildir, "new");
		for (File f : Filesystem.list(newFolder)) {
			Reader reader = ASCII.input(Filesystem.read(f));
			BufferedReader in = new BufferedReader(reader);
			String line = "";
			String id = f.getName();
			String subject = "";
			while ((line = in.readLine()) != null) {
				if (line.length() > 7 && line.substring(0, 7).equals("Subject")) {
					subject = line.substring(8);
				}
			}
			if (!"".equals(id) && !"".equals(subject)) {
				tmp = doc.createElement("a");
				tmp.addLinkAttribute("href", new ResponseUrl("/servlet/read", "id="+id));
				tmp.appendChild(doc.createTextNode(subject));
				body.appendChild(tmp);
				body.appendChild(doc.createElement("br"));
			}
		}
		
		tmp = doc.createElement("a");
		tmp.addLinkAttribute("href", new ResponseUrl("/servlet/logout", null));
		tmp.appendChild(doc.createTextNode("logout"));
		body.appendChild(tmp);
		body.appendChild(doc.createElement("br"));
	}
}
