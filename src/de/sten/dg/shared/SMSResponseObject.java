package de.sten.dg.shared;

import java.io.Serializable;

public class SMSResponseObject implements Serializable{
	
	int statuscode =-1;
	String statusmessage ="not set";
	
	public int getStatuscode() {
		return statuscode;
	}
	public String getStatusmessage() {
		return statusmessage;
	}
	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}
	public void setStatusmessage(String statusmessage) {
		this.statusmessage = statusmessage;
	}
	
	

}
