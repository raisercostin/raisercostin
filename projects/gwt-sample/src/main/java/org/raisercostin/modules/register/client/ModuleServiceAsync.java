package org.raisercostin.modules.register.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ModuleServiceAsync {
	public void isValidCode(String entercode, AsyncCallback<Boolean> callback);
}
