package de.sten.dg.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class SMSRequestObject implements Serializable{
	
	String username, password, orginator, environment;
	ArrayList<SMSMessageObject> messages;
	boolean flash;
	
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
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
	public ArrayList<SMSMessageObject> getMessages() {
		return messages;
	}
	public boolean isFlash() {
		return flash;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setOrginator(String orginator) {
		this.orginator = orginator;
	}
	public void setMessages(ArrayList<SMSMessageObject> messages) {
		this.messages = messages;
	}
	public void setFlash(boolean flash) {
		this.flash = flash;
	}
	
	


	 

}
