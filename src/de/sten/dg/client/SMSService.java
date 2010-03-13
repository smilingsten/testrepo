package de.sten.dg.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("sendsms")
public interface SMSService extends RemoteService {
	String sendSMS(String smsobj);

}
