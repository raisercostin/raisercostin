package org.raisercostin.modules.register.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ModuleService extends RemoteService {
	public Boolean isValidCode(String code);
}
