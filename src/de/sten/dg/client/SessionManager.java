package de.sten.dg.client;

import java.util.Date;

import com.google.gwt.user.client.Cookies;
	
public class SessionManager {
	String dgcookie;
	
	public SessionManager() {
		String dgcookie = Cookies.getCookie("DG_SMS_SessionCookie");
		if ( !(dgcookie==null)) {
			System.out.println("found dgcookie. value is "+dgcookie);
		}
		else System.out.println("no dgcookie found");
		
	}
	
	public void setSessionCookie(String username, String password){
		Date date = new Date();
		Long time = date.getTime();
		String timestr = time.toString();
		String cookiestring = timestr+"-->!sten!<--"+username+"-->!sten!<--"+password;
		System.out.println("setting cookie "+cookiestring);
		//Date expires = new Date();
		//expires.setTime(time+ (3600*1000*5));
		Cookies.setCookie("DG_SMS_SessionCookie", cookiestring);	
		String dgcookie = Cookies.getCookie("DG_SMS_SessionCookie");
		if (!(dgcookie==null)) System.out.println("cookie exists after setting");;
		
	}
	
	public void deleteSessionCookie(){
		Cookies.removeCookie("DG_SMS_SessionCookie");
		
	}
	
	public boolean validCookieExists(){
		boolean validcookiethere = false;
		String dgcookie = Cookies.getCookie("DG_SMS_SessionCookie");
		System.out.println("");
		if ((dgcookie==null)) return false;
		Date date = new Date();
		Long time = date.getTime();
		Long cookietime = Long.parseLong(dgcookie.split("-->!sten!<--")[0]);
		System.out.println("time -cookietime is "+(time-cookietime));
		if ( (time-cookietime) < (3600*1000*5)) return true;
		return true	;
		
		
	}
	
	


}
