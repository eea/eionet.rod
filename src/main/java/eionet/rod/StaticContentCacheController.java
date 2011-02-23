package eionet.rod;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class StaticContentCacheController implements Filter {
	private static String cacheControl;
	private static String expires;

	public void init(FilterConfig config) throws ServletException {

		cacheControl = config.getInitParameter("cacheControl");
		expires = config.getInitParameter("expires");

	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws ServletException, IOException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		Date exp = RODUtil.getDate(expires);
		if (cacheControl != null) {
			httpResponse.setHeader("Cache-Control", cacheControl);
		}
		if (exp != null) {
			httpResponse.setDateHeader("Expires", exp.getTime());
		}
		fc.doFilter(request, response);
	}
}
