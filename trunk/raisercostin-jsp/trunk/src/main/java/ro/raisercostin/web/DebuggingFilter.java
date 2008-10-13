package ro.raisercostin.web;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;

import org.springframework.util.StringUtils;

/**
 * see http://www.gulland.com/courses/JavaServerPages/jsp_objects.jsp
 */
public class DebuggingFilter implements Filter {
	/**
	 * Logger for this class
	 */
	private static final Logger loggerParameters = Logger.getLogger(DebuggingFilter.class.getName() + ".PARAMETERS");
	private static final Logger loggerRequest = Logger.getLogger(DebuggingFilter.class.getName() + ".REQUEST");
	private static final Logger loggerAll = Logger.getLogger(DebuggingFilter.class.getName() + ".ALL");

	private ServletContext servletContext;

	public void destroy() {
	}

	public void doFilter(final ServletRequest request, final ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setAttribute("debug", new DebugInfo() {
			public String getJspInfo() {
				return debug(servletContext, (HttpServletRequest) request, (HttpServletResponse) response,
						new HtmlPrinter(), true, true);
			}
		});
		if (loggerParameters.isDebugEnabled()) {
			loggerParameters.debug(debug(servletContext, (HttpServletRequest) request, (HttpServletResponse) response,
					new PlainTextPrinter(), loggerAll.isDebugEnabled(), loggerRequest.isDebugEnabled()));
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		servletContext = filterConfig.getServletContext();
	}

	public static interface DebugInfo {
		String getJspInfo();
	}

	public String debug(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response,
			DebuggingPrinter debuggingPrinter, boolean debugAll, boolean debugRequest) {
		final JspFactory jspFactory = JspFactory.getDefaultFactory();
		HttpSession session = request.getSession();
		debuggingPrinter.addHeader();
		debuggingPrinter.addSection("Request Parameters");
		for (Iterator iterator = request.getParameterMap().entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, Object> parameter = (Map.Entry<String, Object>) iterator.next();
			addRow(debuggingPrinter, parameter.getKey(), StringUtils.arrayToCommaDelimitedString((Object[]) parameter
					.getValue()));
		}
		debuggingPrinter.endSection();

		if (debugRequest) {
			debuggingPrinter.addSection("Request Header");
			for (Enumeration e = request.getHeaderNames(); e.hasMoreElements();) {
				String parameterName = (String) e.nextElement();
				addRow(debuggingPrinter, parameterName, debuggingPrinter.transform(request.getHeader(parameterName)));
			}
			debuggingPrinter.endSection();

			debuggingPrinter.addSection("Request Attributes");
			java.util.Enumeration en = request.getAttributeNames();
			while (en.hasMoreElements()) {
				String attrName = (String) en.nextElement();
				try {
					addRow(debuggingPrinter, attrName, request.getAttribute(attrName).toString());
				} catch (Exception e) {
					addRow(debuggingPrinter, attrName, toString(e));
				}

			}
			debuggingPrinter.endSection();

			debuggingPrinter.addSection("Session Attributes");
			en = request.getSession().getAttributeNames();
			while (en.hasMoreElements()) {
				String attrName = (String) en.nextElement();
				try {
					addRow(debuggingPrinter, attrName, request.getAttribute(attrName).toString());
				} catch (Exception e) {
					addRow(debuggingPrinter, attrName, toString(e));
				}
			}
			debuggingPrinter.endSection();

			debuggingPrinter.addSection("Request Info");
			addRow(debuggingPrinter, "AuthType", request.getAuthType());
			addRow(debuggingPrinter, "ContextPath", request.getContextPath());
			addRow(debuggingPrinter, "Method", request.getMethod());
			addRow(debuggingPrinter, "PathInfo", request.getPathInfo());
			addRow(debuggingPrinter, "PathTranslated", request.getPathTranslated());
			addRow(debuggingPrinter, "Protocol", request.getProtocol());
			addRow(debuggingPrinter, "QueryString", request.getQueryString());
			addRow(debuggingPrinter, "RemoteAddr", request.getRemoteAddr());
			addRow(debuggingPrinter, "RemoteUser", request.getRemoteUser());
			addRow(debuggingPrinter, "RequestedSessionId", request.getRequestedSessionId());
			addRow(debuggingPrinter, "RequestURI", request.getRequestURI());
			addRow(debuggingPrinter, "RequestURL", request.getRequestURL().toString());
			addRow(debuggingPrinter, "ServletPath", request.getServletPath());
			addRow(debuggingPrinter, "Scheme", request.getScheme());
			addRow(debuggingPrinter, "ServletPath", request.getServletPath());
		}
		if (debugAll) {
			debuggingPrinter.addSection("Server");
			addRow(debuggingPrinter, "Server Info", servletContext.getServerInfo());
			addRow(debuggingPrinter, "Servlet Engine Version", servletContext.getMajorVersion() + "."
					+ servletContext.getMinorVersion());
			addRow(debuggingPrinter, "JSP Version", jspFactory.getEngineInfo().getSpecificationVersion());
			debuggingPrinter.endSection();

			debuggingPrinter.addSection("JVM Properties");
			for (Enumeration e = System.getProperties().propertyNames(); e.hasMoreElements();) {
				String parameterName = (String) e.nextElement();
				addRow(debuggingPrinter, parameterName, debuggingPrinter.transform(System.getProperty(parameterName)));
			}
			debuggingPrinter.endSection();

			debuggingPrinter.addSection("Environment");
			for (Map.Entry<String, String> property : System.getenv().entrySet()) {
				addRow(debuggingPrinter, property.getKey(), debuggingPrinter.transform(property.getValue()));
			}
			debuggingPrinter.endSection();

			debuggingPrinter.addSection("Debugger Provided by");
			addRow(debuggingPrinter, "provided by", "raisercostin");
			debuggingPrinter
					.addRow(
							"source",
							"<a target='_blank' href='http://code.google.com/p/raisercostin/wiki/DebuggingFilter'>http://code.google.com/p/raisercostin/wiki/DebuggingFilter</a>");
			addRow(debuggingPrinter, "version", "1.0");
			addRow(debuggingPrinter, "timestamp", "2008.June.14");
			addRow(debuggingPrinter, "license",
					"<a target='_blank' href='http://www.apache.org/licenses/LICENSE-2.0.html'>Apache License 2.0</a>");
			debuggingPrinter.endSection();
		}
		debuggingPrinter.addFooter();
		return debuggingPrinter.getString();
	}

	private String toString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter pw = new PrintWriter(stringWriter);
		e.printStackTrace(pw);
		pw.close();
		return stringWriter.toString();
	}

	private void addRow(DebuggingPrinter debuggingPrinter, String key, String value) {
		if (key.toLowerCase().contains("password")) {
			debuggingPrinter.addRow(key, "****");
		} else {
			debuggingPrinter.addRow(key, value);
		}
	}

	public static interface DebuggingPrinter {

		void addHeader();

		String transform(String value);

		void addFooter();

		void endSection();

		void addRow(String key, String value);

		void addSection(String name);

		String getString();
	}

	public static class HtmlPrinter implements DebuggingPrinter {
		StringBuilder result = new StringBuilder();

		public void addHeader() {
			result.append("<div class='debugDiv'>\n");
			result.append("<style>\n");
			result.append(".debugDiv{");
			result.append("}");
			result.append(".debug{");
			result.append("    margin-bottom: 20px;");
			result.append("    margin-left:auto;");
			result.append("    margin-right:auto;");
			result.append("    color: black;");
			result.append("    text-align: left;");
			result.append("    border-collapse: collapse;");
			result.append("    font-family: Arial, sans-serif;");
			result.append("    font-size: 12px;");
			result.append("}");
			result.append(".debug th{");
			result.append("    border: solid 1px black;");
			result.append("    background-color:#CCCCFF;");
			result.append("}");
			result.append(".debug td{");
			result.append("    border: solid 1px black;");
			result.append("    background-color:#CCCCCC;");
			result.append("}");
			result.append(".debug thead th{");
			result.append("    font-size: large;");
			result.append("    background-color:#9999CC;");
			result.append("    text-align: center;");
			result.append("}");
			result.append("</style>\n");
			result.append("<hr>\n");
		}

		public void addSection(String value) {
			result.append("<table class='debug'>\n");
			result.append("<thead><tr><th colspan='2'>").append(value).append("</th></tr></thead>");
		}

		public void addRow(String key, String value) {
			result.append("<tr>");
			result.append("<th>");
			result.append(key);
			result.append("</th>");
			result.append("<td>[");
			result.append(value);
			result.append("]</td>");
			result.append("</tr>\n");
		}

		public void endSection() {
			result.append("</table>\n");
		}

		public void addFooter() {
			result.append("</div>\n");
		}

		public String getString() {
			return result.toString();
		}

		public String transform(String value) {
			if (value == null) {
				value = "";
			}
			if (value.indexOf(";") > 0) {
				value = value.replaceAll("[;]", ";<br>\n");
			}
			if (value.indexOf(",") > 0) {
				value = value.replaceAll("[,]", ",<br>\n");
			}
			return value;
		}
	}

	public static class PlainTextPrinter implements DebuggingPrinter {
		StringBuilder result = new StringBuilder();
		String prefix = "    ";

		public void addHeader() {
			result.append("----------------\n");
		}

		public void addFooter() {
			result.append("----------------\n");
		}

		public void addRow(String key, String value) {
			result.append(prefix).append(key).append("=[").append(value).append("]\n");
		}

		public void addSection(String name) {
			result.append(name).append("\n");
		}

		public void endSection() {
		}

		public String getString() {
			return result.toString();
		}

		public String transform(String value) {
			if (value == null) {
				value = "";
			}
			if (value.indexOf(";") > 0) {
				value = value.replaceAll("[;]", ";\n" + prefix + prefix);
			}
			if (value.indexOf(",") > 0) {
				value = value.replaceAll("[,]", ",\n" + prefix + prefix);
			}
			return value;
		}

	}
}