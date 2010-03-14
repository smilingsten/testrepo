package de.sten.dg.shared;

import java.io.Serializable;

public class SMSMessageObject implements Serializable{
	
	public String getNumber() {
		return number;
	}
	public String getMessage() {
		return message;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	String number;
	String message;

	
	

}
