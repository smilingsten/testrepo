package de.sten.dg.client;

import java.util.ArrayList;
import java.util.Iterator;


public class FormChecker {
	public boolean isFormDataOK(SMSFormObject formdata) throws SMSFormDataException{
		System.out.println("checking form data");
		String errorMSg="";

		String uname = formdata.getUsername();
		String pw = formdata.getPassword();
		String origin = formdata.getOrginator();
		String msg = formdata.getMessage();

		ArrayList<String> nums = formdata.getNumbers();
		
		if (uname==null || uname.equals("")) errorMSg+="Benutzername fehlt\n";
		if (pw==null || pw.equals("")) errorMSg+="Passwort fehlt\n";
		if (msg==null || msg.equals("")) errorMSg+="Nachricht fehlt\n";
		int okmsgcnd =0;
		for (Iterator iterator = nums.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();

			if (   !(string == null || string.equals("")) ){
				okmsgcnd++;
			}	
		}
		System.out.println("found recipients "+okmsgcnd);
		if (okmsgcnd==0) errorMSg+="Empfänger fehlt\n";
		
		if (!errorMSg.equals("")) {
			throw new SMSFormDataException(errorMSg);
			
		}
		System.out.println("formdata is ok...");

		return true;
		
	}


}
