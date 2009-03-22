package org.joe_e.servlet;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.http.HttpSession;

/**
 * Abstract class whose subclass represent session instances for 
 * servlets. The SessionView is in fact the policy regarding what
 * subset of the session the servlet has access to. Members specified
 * in the SessionView will be visible from within the servlet's 
 * standard methods. Anything not explicitly declared in the SessionView
 * will be hidden from the servlet's methods. All JoeEServlets must
 * have an inner class called SessionView that extends this class.
 * @author akshay
 */
public abstract class AbstractSessionView {

	/**
	 * default constructor
	 */
	public AbstractSessionView() {
	}
	
	/**
	 * Initialize this SessionView by filling it with values from the 
	 * HttpSession. Uses Reflection API to determine what mappings
	 * from the session should be placed into the SessionView. If
	 * f is marked as @readonly, tries to perform a deep copy using
	 * the serialization technique. If this fails for any reason, then
	 * just set the field in the SessionView, to be a pointer to the
	 * object in the HttpSession (i.e. not a copy).
	 * @param ses - the HttpSession
	 * @throws IllegalAccessException - if Reflection stuff goes wrong
	 */
	public void fillSessionView(HttpSession ses) throws IllegalAccessException {
		for (Field f : this.getClass().getDeclaredFields()) {
			if (ses.getAttribute(f.getName()) != null) {
				// TODO: cloneable? serializable? copyable?
				if (f.isAnnotationPresent(readonly.class)) {
					// try to deep-copy
					Object o = ses.getAttribute(f.getName());
					try {
						f.set(this, Cloner.deepCopy(o));
					} catch (Exception e) {
						// deep copy failed
						f.set(this, o);
					} 
				} else {
					// not marked as @readonly.
					f.set(this, ses.getAttribute(f.getName()));
				}
			}
		}
	}
	
	/**
	 * Updates the HttpSession with modifications that may have been
	 * made to objects in the SessionView. This method allows the servlet
	 * to write to the SessionView, and for those writes to persist across
	 * requests. Uses Reflection API to determine which mappings should
	 * get written.
	 * @param ses - the HttpSession
	 * @throws IllegalAccessException - if Reflection stuff goes wrong.
	 */
	public void fillHttpSession(HttpSession ses) throws IllegalAccessException {
		for (Field f : this.getClass().getDeclaredFields()) {
			// TODO: HACK! ask adrian what's going on here. Fix.
			// shouldn't have to setAccessible each attribute
			f.setAccessible(true);
			if (f.isAnnotationPresent(readonly.class)) {
				Dispatcher.logger.fine(f.getName() + " marked as readonly");
			}
			if (!f.isSynthetic() && f.isAccessible() && !f.isAnnotationPresent(readonly.class)) {
				ses.setAttribute(f.getName(), f.get(this));
			}
		}
	}
	
	/**
	 * Check if the argument object is cloneable. 
	 * @deprecated
	 * @param o
	 */
	private static boolean isCloneable(Object o) {
		Class<?>[] classes = o.getClass().getInterfaces();
		for (Class<?> cl : classes) {
			if (cl.getName().equals("java.lang.cloneable")){
				return true;
			}
		}
		return false;
	}
	
}