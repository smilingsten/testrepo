package de.sten.dg.client;

import java.util.ArrayList;

public class SMSFormObject {
	
	String username, password, orginator, message;
	ArrayList<String> numbers;
	boolean flash;
	
	public SMSFormObject(String username, String password, String originator, String message, 
			ArrayList<String> numbers, boolean flash){
		this.username = username;
		this.password = password;
		this.orginator = originator;
		this.message = message;
		this.numbers = numbers;
		this.flash = flash;
		
	}
	
	

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getOrginator() {
		return orginator;
	}

	public String getMessage() {
		return message;
	}

	public ArrayList<String> getNumbers() {
		return numbers;
	}

	public boolean isFlash() {
		return flash;
	}

}
