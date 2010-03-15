package de.sten.dg.client;

import java.util.ArrayList;
import java.util.Iterator;


public class FormChecker {
	public boolean isFormDataOK(SMSFormObject formdata) throws SMSFormDataException{
		//System.out.println("checking form data");
		String errorMSg="";

		String uname = formdata.getUsername();
		String pw = formdata.getPassword();
		String origin = formdata.getOrginator();
		String msg = formdata.getMessage();

		ArrayList<String> nums = formdata.getNumbers();
		
		if (uname==null || uname.equals("")) errorMSg+="Benutzername fehlt<br />";
		if (pw==null || pw.equals("")) errorMSg+="Passwort fehlt<br />";
		if (msg==null || msg.equals("")) errorMSg+="Nachricht fehlt<br />";
		boolean origContainschars = true;
		if ( (origin.replaceAll("[\\+0-9-/]", "").length())>0) {
			origContainschars = true;
			System.out.println("stripped origin string is "+origin.replaceAll("[\\+0-9-/]", ""));
			System.out.println("origin contains chars true");

		}
		if ( (origin.replaceAll("[\\+0-9-/]", "").length())==0) origContainschars = false;
		if ((origContainschars) && origin.length()>11  ) errorMSg+="Absender zu lang (max. 11 Zeichen) <br />";
		String forgotwhatiwanted = origin.replaceAll("[a-zA-Z0-9]", "");
		if ( (origContainschars) && (forgotwhatiwanted.length()>0) )  errorMSg+="Absender enthält ungültige Zeichen<br />";
		int okmsgcnd =0;
		for (Iterator iterator = nums.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();

			if (   !(string == null || string.equals("")) ){
				okmsgcnd++;
			}	
		}
	//	System.out.println("found recipients "+okmsgcnd);
		if (okmsgcnd==0) errorMSg+="Empfänger fehlt<br />";
		
		if (!errorMSg.equals("")) {
			throw new SMSFormDataException(errorMSg);
			
		}
	//	System.out.println("formdata is ok...");

		return true;
		
	}


}
