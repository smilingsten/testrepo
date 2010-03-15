package de.sten.dg.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.sten.dg.shared.SMSRequestObject;
import de.sten.dg.shared.SMSResponseObject;

public interface SMSServiceAsync {

	void sendSMS(SMSRequestObject smsro,
			AsyncCallback<SMSResponseObject> callback);

}
