package de.sten.dg.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.sten.dg.client.ValidationService;
import de.sten.dg.shared.SMSSendException;

public class ValidationServiceImpl extends RemoteServiceServlet implements
ValidationService
{

	public void startValidation(String number) throws SMSSendException {
		System.out.println("staring validation request. number is "+number);
		
		
	}

	public String validate(String secretkey) throws SMSSendException {
		// TODO Auto-generated method stub
		return null;
	}

}
