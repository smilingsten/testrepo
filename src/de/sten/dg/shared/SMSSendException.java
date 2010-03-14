package de.sten.dg.shared;

import java.io.Serializable;

public class SMSSendException extends Exception implements Serializable{
	
	String errorMsg;
	int responseCode;

	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public int getResponseCode() {
		return responseCode;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	
}
