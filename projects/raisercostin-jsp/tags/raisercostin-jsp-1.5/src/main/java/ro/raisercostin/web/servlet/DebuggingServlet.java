package ro.raisercostin.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ro.raisercostin.web.HttpServletUtils;

public class DebuggingServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2191140311391265236L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		String message = "It works [" + request.getPathInfo() + "].";
		writer.print(message + "<hr/>" + HttpServletUtils.toHtmlString(request));
		System.out.println(message + "\n" + HttpServletUtils.toString(request));
	}
}