package org.joe_e.servlet.mail;

import java.io.PrintWriter;

public class HtmlWriter {

	public static void printHeader(PrintWriter p) {
	        p.println("<!doctype html>");
		p.println("<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\"><title>Joe-E Mail</title></head>");
	}
	
	public static String getHeader() {
		return "<html><head><title>Joe-E Mail</title></head>";
	}
	
	public static void printFooter(PrintWriter p) {
		p.println("</html>");
	}
	
	public static String getFooter() {
		return "</html>";
	}

}
