package eionet.rod.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.yale.its.tp.cas.client.filter.CASFilter;
import eionet.rod.Attrs;
import eionet.rod.Constants;
import eionet.rod.ROCASUser;
import eionet.rod.RODUtil;
import eionet.rod.ROUser;

public class SecurityUtil {

    private static String casLoginUrl;
    private static String casServerName;

    /**
     * 
     * @param request
     * @return String
     */
    public static String getLoginURL(HttpServletRequest request) {

        String result = "javascript:login()";

        String casLoginUrl = getCasLoginUrl(request);
        if (casLoginUrl != null) {

            String casServerName = getCasServerName(request);
            if (casServerName == null)
                throw new RuntimeException("If " + CASFilter.LOGIN_INIT_PARAM
                        + " context parameter has been specified, so must be " + CASFilter.SERVERNAME_INIT_PARAM);

            // set the after-login-url
            String action = (String) request.getSession(true).getAttribute(Constants.LAST_ACTION_URL_SESSION_ATTR);
            String afterUrl = request.getScheme() + "://" + casServerName + request.getContextPath();
            if (RODUtil.isNullOrEmpty(action))
                afterUrl = request.getRequestURL().toString()
                        + (request.getQueryString() != null ? ("?" + request.getQueryString()) : "");
            else
                afterUrl = request.getScheme() + "://" + casServerName + request.getContextPath() + action;

            request.getSession().setAttribute("afterLogin", afterUrl);

            try {
                result = casLoginUrl
                        + "?service="
                        + URLEncoder.encode(request.getScheme() + "://" + casServerName + request.getContextPath() + "/login",
                                "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.toString(), e);
            }
        }

        return result;
    }

    /**
     * @param request
     * @return String
     */
    public static String getCasLoginUrl(HttpServletRequest request) {

        if (SecurityUtil.casLoginUrl == null)
            SecurityUtil.casLoginUrl = request.getSession().getServletContext().getInitParameter(CASFilter.LOGIN_INIT_PARAM);

        return SecurityUtil.casLoginUrl;
    }

    /**
     * @param request
     * @return the casServerName
     */
    public static String getCasServerName(HttpServletRequest request) {

        if (SecurityUtil.casServerName == null)
            SecurityUtil.casServerName = request.getSession().getServletContext().getInitParameter(CASFilter.SERVERNAME_INIT_PARAM);

        return SecurityUtil.casServerName;
    }

    /**
     * 
     * @param request
     * @return String
     */
    public static String getLogoutURL(HttpServletRequest request) {

        String result = "index.jsp";

        String casLoginUrl = request.getSession().getServletContext().getInitParameter(CASFilter.LOGIN_INIT_PARAM);
        if (casLoginUrl != null) {

            String casServerName = getCasServerName(request);
            if (casServerName == null)
                throw new RuntimeException("If " + CASFilter.LOGIN_INIT_PARAM
                        + " context parameter has been specified, so must be " + CASFilter.SERVERNAME_INIT_PARAM);

            try {
                result = casLoginUrl.replaceFirst("/login", "/logout") + "?url="
                        + URLEncoder.encode(request.getScheme() + "://" + casServerName + request.getContextPath(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.toString(), e);
            }
        }

        return result;
    }

    /**
     * Returns current user, or null, if the current session does not have user attached to it.
     * 
     * @param request
     * @return ROUser
     */
    public static final ROUser getUser(HttpServletRequest request) {

        HttpSession session = request.getSession();
        String appName = session.getServletContext().getInitParameter(Attrs.APPPARAM);
        ROUser user = (ROUser) session.getAttribute(Attrs.USERPREFIX + appName);

        if (user == null) {
            String casUserName = (String) session.getAttribute(CASFilter.CAS_FILTER_USER);
            if (casUserName != null) {
                user = ROCASUser.create(casUserName);
                session.setAttribute(Attrs.USERPREFIX + appName, user);
            }
        } else if (user instanceof ROCASUser) {
            String casUserName = (String) session.getAttribute(CASFilter.CAS_FILTER_USER);
            if (casUserName == null) {
                user.invalidate();
                user = null;
                session.removeAttribute(Attrs.USERPREFIX + appName);
            } else if (!casUserName.equals(user.getUserName())) {
                user.invalidate();
                user = ROCASUser.create(casUserName);
                session.setAttribute(Attrs.USERPREFIX + appName, user);
            }
        }

        if (user != null)
            return user.isAuthentic() ? user : null;
        else
            return null;
    }

}
