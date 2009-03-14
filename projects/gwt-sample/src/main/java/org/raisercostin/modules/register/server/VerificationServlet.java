package org.raisercostin.modules.register.server;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class VerificationServlet extends HttpServlet {
	private static final long serialVersionUID = -1994750544064797735L;

	private HttpSession session;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession(true);
		response.setHeader("Pragma", "public");
		response.setHeader("cache-control",
				"max-age=0,no-cache, no-store,must-revalidate, proxy-revalidate, s-maxage=0");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/png");
		ImageVerification verification = new ImageVerification(response.getOutputStream());
		String code = verification.getVerificationValue();
		session.setAttribute("verification.code", code);
		response.flushBuffer();
	}
}
