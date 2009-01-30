package eionet.rod;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tee.xmlserver.AppUserIF;
import com.tee.xmlserver.XDBApplication;

import edu.yale.its.tp.cas.client.filter.CASFilter;
import eionet.rod.services.LogServiceIF;
import eionet.rod.services.RODServices;

public class EionetCASFilter extends CASFilter {

	public static final String EIONET_LOGIN_COOKIE_NAME = "eionetCasLogin";

	public static String getEionetLoginCookieName(){
		return EIONET_LOGIN_COOKIE_NAME;
	}
	
	private static LogServiceIF logger = RODServices.getLogService();

	private static final String EIONET_COOKIE_LOGIN_PATH = "eionetCookieLogin";

	private static String CAS_LOGIN_URL = null;

	private static String SERVER_NAME = null;

	private static String EIONET_LOGIN_COOKIE_DOMAIN = null;

	private static final String REMOTEUSER = "com.tee.xmlserver.user";

	private static String ROD_USER_ATTRIBUTE;
	
	public void init(FilterConfig config) throws ServletException {
		CAS_LOGIN_URL = config.getInitParameter(LOGIN_INIT_PARAM);
		SERVER_NAME = config.getInitParameter(SERVERNAME_INIT_PARAM);
		EIONET_LOGIN_COOKIE_DOMAIN = config.getInitParameter("eionetLoginCookieDomain");
		XDBApplication.getInstance(config.getServletContext());
		ROD_USER_ATTRIBUTE = REMOTEUSER + "/" + XDBApplication.getAppName();
		super.init(config);

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws ServletException, IOException {
		CASFilterChain chain = new CASFilterChain();
		super.doFilter(request, response, chain);

		if (chain.isDoNext()) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpSession session = httpRequest.getSession();
			if (session != null && session.getAttribute(ROD_USER_ATTRIBUTE) == null) {
				String rd = httpRequest.getParameter("rd");
				AppUserIF user = XDBApplication.getAuthenticator();
				String userName = (String) session.getAttribute(CAS_FILTER_USER);
				user.authenticate(userName, null);
				session.setAttribute(ROD_USER_ATTRIBUTE, user);
				logger.debug("Logged in user " + session.getAttribute(CAS_FILTER_USER));
				String requestURI = httpRequest.getRequestURI();
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				if (requestURI.indexOf(EIONET_COOKIE_LOGIN_PATH) > -1) {
					redirectAfterEionetCookieLogin(httpRequest, httpResponse);
					return;
				} else if (requestURI.endsWith("/login")) {
					attachEionetLoginCookie(httpResponse,true);
					String location = (String) session.getAttribute("afterLogin");
					
					if (rd != null && rd.equals("subscribe")) {
						location = "subscribe.jsp";
					}
					if (location != null)
						httpResponse.sendRedirect(location);
					else
						httpResponse.sendRedirect(httpRequest.getContextPath());
					return;
				}
			}
			fc.doFilter(request, response);
			return;
		}
	}

	
	public static void attachEionetLoginCookie(HttpServletResponse response, boolean isLoggedIn){
		Cookie tgc = new Cookie(EIONET_LOGIN_COOKIE_NAME, isLoggedIn?"loggedIn":"loggedOut");
		tgc.setMaxAge(-1);
		if (!EIONET_LOGIN_COOKIE_DOMAIN.equalsIgnoreCase("localhost"))
			tgc.setDomain(EIONET_LOGIN_COOKIE_DOMAIN);
		tgc.setPath("/");			
		response.addCookie(tgc);		
	}
	
	
	public static String getCASLoginURL(HttpServletRequest request) {
		
		String action = (String) request.getSession(true).getAttribute(Constants.LAST_ACTION_URL_SESSION_ATTR);
		String afterUrl = request.getScheme() + "://" + SERVER_NAME + request.getContextPath();
		if(RODUtil.isNullOrEmpty(action))
			afterUrl = request.getRequestURL().toString() + (request.getQueryString() != null ? ("?" +request.getQueryString()):"");
		else
			afterUrl = request.getScheme() + "://" + SERVER_NAME + request.getContextPath() + action;
		
		request.getSession(true).setAttribute("afterLogin",afterUrl);
		return CAS_LOGIN_URL + "?service=" + request.getScheme() + "://" + SERVER_NAME + request.getContextPath() + "/login";
	}
	
	public static String getCASLoginURLTemp(HttpServletRequest req) {
		
		StringBuffer sb = new StringBuffer(CAS_LOGIN_URL);
		sb.append("?service=");
		sb.append(req.getScheme());
		sb.append("://");
		sb.append(SERVER_NAME);
		if (!req.getContextPath().equals("")) {
			sb.append(req.getContextPath());
		}
		sb.append("/login");

		return sb.toString();
	}
						  	
	public static String getCASLoginURL(HttpServletRequest req, boolean forSubscription) {
		req.getSession(true).setAttribute("afterLogin",req.getRequestURL().toString() + (req.getQueryString() != null ? ("?" +req.getQueryString()):"" ));
		StringBuffer sb = new StringBuffer(CAS_LOGIN_URL);
		sb.append("?service=");
		sb.append(req.getScheme());
		sb.append("://");
		sb.append(SERVER_NAME);
		if (!req.getContextPath().equals("")) {
			sb.append(req.getContextPath());
		}
		sb.append("/login");
		if (forSubscription) {
			sb.append("?rd=subscribe");
		}
		return sb.toString();
	}

	public static String getCASLogoutURL(HttpServletRequest request) {
		return CAS_LOGIN_URL.replaceFirst("/login", "/logout") + "?url=" + request.getScheme() + "://" + SERVER_NAME + request.getContextPath();
	}

	public static String getEionetCookieCASLoginURL(HttpServletRequest request) {

		String contextPath = request.getContextPath();
		String serviceURL =  request.getRequestURL().toString(); 
		if (request.getQueryString() != null && request.getQueryString().length() > 0){
			serviceURL = serviceURL + "?" + request.getQueryString();
		}

		String serviceURI = serviceURL.substring(serviceURL.indexOf("/", serviceURL.indexOf("://") + 3));

		if (contextPath.equals("")) {
			if (serviceURI.equals("/"))
				serviceURL = serviceURL + EIONET_COOKIE_LOGIN_PATH + "/";
			else
				serviceURL = serviceURL.replaceFirst(forRegex(serviceURI), "/" + EIONET_COOKIE_LOGIN_PATH + serviceURI);
		} else {
			String servletPath = serviceURI.substring(contextPath.length(), serviceURI.length());
			if (serviceURI.equals("/"))
				serviceURL = serviceURL + EIONET_COOKIE_LOGIN_PATH + "/";
			else
				serviceURL = serviceURL.replaceFirst(forRegex(serviceURI), contextPath + "/" + EIONET_COOKIE_LOGIN_PATH + servletPath);
		}

		try {
			serviceURL = URLEncoder.encode(serviceURL,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
		return CAS_LOGIN_URL + "?service=" +   serviceURL ;
		
	}

	
	public static boolean isCasLoggedUser(HttpServletRequest request){
		return (request.getSession() != null && request.getSession().getAttribute(CAS_FILTER_USER) != null);		
	}
	
	private void redirectAfterEionetCookieLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String requestUri = request.getRequestURI() + (request.getQueryString() != null ? ("?" +request.getQueryString()):"" );
		String realURI = null;
		if (requestUri.endsWith(EIONET_COOKIE_LOGIN_PATH + "/"))
			realURI = requestUri.replaceFirst(EIONET_COOKIE_LOGIN_PATH + "/", "");
		else
			realURI = requestUri.replaceFirst("/" + EIONET_COOKIE_LOGIN_PATH, "");
		response.sendRedirect(realURI);
	}

	  public static String forRegex(String aRegexFragment){
		    final StringBuffer result = new StringBuffer();

		    final StringCharacterIterator iterator = new StringCharacterIterator(aRegexFragment);
		    char character =  iterator.current();
		    while (character != CharacterIterator.DONE ){
		      /*
		      * All literals need to have backslashes doubled.
		      */
		      if (character == '.') {
		        result.append("\\.");
		      }
		      else if (character == '\\') {
		        result.append("\\\\");
		      }
		      else if (character == '?') {
		        result.append("\\?");
		      }
		      else if (character == '*') {
		        result.append("\\*");
		      }
		      else if (character == '+') {
		        result.append("\\+");
		      }
		      else if (character == '&') {
		        result.append("\\&");
		      }
		      else if (character == ':') {
		        result.append("\\:");
		      }
		      else if (character == '{') {
		        result.append("\\{");
		      }
		      else if (character == '}') {
		        result.append("\\}");
		      }
		      else if (character == '[') {
		        result.append("\\[");
		      }
		      else if (character == ']') {
		        result.append("\\]");
		      }
		      else if (character == '(') {
		        result.append("\\(");
		      }
		      else if (character == ')') {
		        result.append("\\)");
		      }
		      else if (character == '^') {
		        result.append("\\^");
		      }
		      else if (character == '$') {
		        result.append("\\$");
		      }
		      else {
		        //the char is not a special one
		        //add it to the result as is
		        result.append(character);
		      }
		      character = iterator.next();
		    }
		    return result.toString();
		  }	  
}

class CASFilterChain implements FilterChain {

	private boolean doNext = false;

	public void doFilter(ServletRequest request, ServletResponse response) {
		doNext = true;
	}

	public boolean isDoNext() {
		return doNext;
	}
}


