package de.sten.dg.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.sten.dg.shared.SMSSendException;



/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("validatesms")
public interface ValidationService extends RemoteService {
	
	public void startValidation(String number) throws SMSSendException;
	public String validate(String secretkey) throws SMSSendException;

}
