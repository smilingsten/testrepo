package de.sten.dg.client;

import java.util.ArrayList;

//same as sms form object
public class SMSFormObject {
	
	
	String username, password, orginator, message, environment;
	ArrayList<String> numbers;
	
	boolean flash;
	
	public SMSFormObject(String username, String password, String originator, String message, 
			ArrayList<String> numbers, boolean flash, String envorinment){
		this.username = username;
		this.password = password;
		this.orginator = originator;
		this.message = message;
		this.numbers = numbers;
		this.flash = flash;
		this.environment = envorinment;
		
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

	public String getEnvironment() {
		return environment;
	}




}
