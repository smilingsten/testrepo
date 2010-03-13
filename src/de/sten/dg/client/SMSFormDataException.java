package de.sten.dg.client;

public class SMSFormDataException extends Exception{
	
	private String errorMsg;
	
	public SMSFormDataException(String errorMsg) {
		this.errorMsg = errorMsg;	
		System.out.println("SMSFORMEX thrown. msg was: \n"+this.errorMsg);
		}
	public String getErrorMsg(){
		return errorMsg;
	}

}
