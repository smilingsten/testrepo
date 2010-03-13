package de.sten.dg.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SMSServiceAsync {

	void sendSMS(String smsobj, AsyncCallback<String> callback);

}
