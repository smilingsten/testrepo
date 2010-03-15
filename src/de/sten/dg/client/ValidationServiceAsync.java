package de.sten.dg.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ValidationServiceAsync {

	void startValidation(String number, AsyncCallback<Void> callback);

	void validate(String secretkey, AsyncCallback<String> callback);

}
