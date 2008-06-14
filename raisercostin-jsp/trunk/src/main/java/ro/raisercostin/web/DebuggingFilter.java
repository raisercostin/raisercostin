package ro.raisercostin.web;

import java.io.IOException;
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

	private ServletContext servletContext;

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(final ServletRequest request, final ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setAttribute("debug", new DebugInfo() {
			public String getJspInfo() {
				return debug(servletContext, (HttpServletRequest) request, (HttpServletResponse) response);
			}
		});
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		servletContext = filterConfig.getServletContext();
	}

	public static interface DebugInfo {
		String getJspInfo();
	}

	public String debug(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) {
		final JspFactory jspFactory = JspFactory.getDefaultFactory();
		HttpSession session = request.getSession();
		StringBuilder result = new StringBuilder();
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

		addSection(result, "Request Parameters");
		for (Iterator iterator = request.getParameterMap().entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, Object> parameter = (Map.Entry<String, Object>) iterator.next();
			addRow(result, parameter.getKey(), StringUtils.arrayToCommaDelimitedString((Object[]) parameter.getValue()));
		}
		endSection(result);

		addSection(result, "Request Header");
		for (Enumeration e = request.getHeaderNames(); e.hasMoreElements();) {
			String parameterName = (String) e.nextElement();
			addRow(result, parameterName, transform(request.getHeader(parameterName)));
		}
		endSection(result);

		addSection(result, "Request Info");
		addRow(result, "AuthType", request.getAuthType());
		addRow(result, "ContextPath", request.getContextPath());
		addRow(result, "Method", request.getMethod());
		addRow(result, "PathInfo", request.getPathInfo());
		addRow(result, "PathTranslated", request.getPathTranslated());
		addRow(result, "Protocol", request.getProtocol());
		addRow(result, "QueryString", request.getQueryString());
		addRow(result, "RemoteAddr", request.getRemoteAddr());
		addRow(result, "RemoteUser", request.getRemoteUser());
		addRow(result, "RequestedSessionId", request.getRequestedSessionId());
		addRow(result, "RequestURI", request.getRequestURI());
		addRow(result, "RequestURL", request.getRequestURL().toString());
		addRow(result, "ServletPath", request.getServletPath());
		addRow(result, "Scheme", request.getScheme());
		addRow(result, "ServletPath", request.getServletPath());
		addSection(result, "Server");
		addRow(result, "Server Info", servletContext.getServerInfo());
		addRow(result, "Servlet Engine Version", servletContext.getMajorVersion() + "."
				+ servletContext.getMinorVersion());
		addRow(result, "JSP Version", jspFactory.getEngineInfo().getSpecificationVersion());
		endSection(result);

		addSection(result, "JVM Properties");
		for (Enumeration e = System.getProperties().propertyNames(); e.hasMoreElements();) {
			String parameterName = (String) e.nextElement();
			addRow(result, parameterName, transform(System.getProperty(parameterName)));
		}
		endSection(result);

		addSection(result, "Environment");
		for (Map.Entry<String, String> property : System.getenv().entrySet()) {
			addRow(result, property.getKey(), transform(property.getValue()));
		}
		endSection(result);

		addSection(result, "Debugger Provided by");
		addRow(result, "provided by", "raisercostin");
		addRow(
				result,
				"source",
				"<a target='_blank' href='http://code.google.com/p/raisercostin/wiki/DebuggingFilter'>http://code.google.com/p/raisercostin/wiki/DebuggingFilter</a>");
		addRow(result, "version", "1.0");
		addRow(result, "timestamp", "2008.June.14");
		addRow(result, "license",
				"<a target='_blank' href='http://www.apache.org/licenses/LICENSE-2.0.html'>Apache License 2.0</a>");
		endSection(result);

		result.append("</div>\n");
		return result.toString();
	}

	private void endSection(StringBuilder result) {
		result.append("</table>\n");
	}

	private String transform(String value) {
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

	private void addSection(StringBuilder result, String value) {
		result.append("<table class='debug'>\n");
		result.append("<thead><tr><th colspan='2'>").append(value).append("</th></tr></thead>");
	}

	private void addRow(StringBuilder result, String key, String value) {
		result.append("<tr>");
		result.append("<th>");
		result.append(key);
		result.append("</th>");
		result.append("<td>[");
		result.append(value);
		result.append("]</td>");
		result.append("</tr>\n");
	}
}