package de.sten.dg.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.sten.dg.shared.SMSRequestObject;
import de.sten.dg.shared.SMSResponseObject;
import de.sten.dg.shared.SMSSendException;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("sendsms")
public interface SMSService extends RemoteService {
	
	public SMSResponseObject sendSMS(SMSRequestObject smsro) throws SMSSendException;

}
