package org.raisercostin.modules.register.server;

import javax.servlet.http.HttpSession;

import org.raisercostin.modules.register.client.ModuleService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ModuleServiceImpl extends RemoteServiceServlet implements ModuleService {
	private static final long serialVersionUID = -8318545812344758022L;
	private HttpSession session = null;

	public Boolean isValidCode(String entercode) {
		session = getThreadLocalRequest().getSession();
		String code = (String) session.getAttribute("verification.code");
		return (code != null && code.equals(entercode));
	}
}
