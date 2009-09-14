package org.joe_e.servlet;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class represents a view into some of the cookies associated with the
 * HTTP request. To limit information leakage, and to fully understand the 
 * communication channels between servlets, we require that each servlet 
 * programmatically specify which cookies it plans to access, and we then only
 * give the servlet access to these specific cookies. This is done by
 * subclassing an <code>AbstractCookieView</code>, similarly to what we do with
 * <code>AbstractSessionView</code>s. All JoeEServlets must have an inner class 
 * called <code>CookieView</code> that extends this class. 
 * @author akshay
 *
 */
public abstract class AbstractCookieView {

	/**
	 * default constructor
	 */
	public AbstractCookieView() {
		
	}
	
	/**
	 * Initialize this CookieView by filling it with values from the HttpRequest
	 * We use the reflection API to determine what mappings from the session
	 * should be placed into the SessionView. 
	 * TODO: for now we aren't doing anything about readonly. 
	 * @param HttpRequest
	 * @throws IllegalAccessException - if Reflection stuff goes wrong
	 */
	public final void fillCookieView(HttpServletRequest req) throws IllegalAccessException {
		for (Field f : this.getClass().getDeclaredFields()) {
			boolean done = false;
			for (Cookie c : req.getCookies()) {
				if (c.getName().equals("__joe-e__"+f.getName())) {
					f.set(this, c.getValue());
					Dispatcher.logger.fine("request contains cookie: " + c.getName() + " " + c.getValue());
					done = true;
				}
			}
			if (!done && !Modifier.isFinal(f.getModifiers())) {
				Cookie c = new Cookie("__joe-e__"+f.getName(), "");
				f.set(this, c.getValue());
			}
		}
	}
	
	/**
	 * Updated the cookies in the HttpServletRequest with any values that may
	 * have changed as a result of executing the current servlet. This allows
	 * changes that the servlet makes to persist across requests. 
	 * @param req - The HttpServletRequest 
	 * @throws IllegalAccessException
	 */
	public final void fillHttpResponse(HttpServletResponse res) throws IllegalAccessException {
		for (Field f : this.getClass().getDeclaredFields()) {
			if (!Modifier.isFinal(f.getModifiers())) {
				Cookie c = new Cookie("__joe-e__"+f.getName(), (String) f.get(this));
				res.addCookie(c);
			}
		}
	}
}
