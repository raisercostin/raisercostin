package ro.raisercostin.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

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
				return HttpServletUtils.toHtmlString(servletContext, (HttpServletRequest) request, true, true);
			}

			public boolean isRequested() {
				String debug = request.getParameter("debug");
				if (debug != null) {
					if ("false".equals(debug)) {
						return false;
					}
					return true;
				}
				return false;
			}
		});
		if (loggerParameters.isDebugEnabled()) {
			loggerParameters.debug(HttpServletUtils.toString(servletContext, (HttpServletRequest) request, loggerAll
					.isDebugEnabled(), loggerRequest.isDebugEnabled()));
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		servletContext = filterConfig.getServletContext();
	}

	public static interface DebugInfo {
		String getJspInfo();

		boolean isRequested();
	}
}