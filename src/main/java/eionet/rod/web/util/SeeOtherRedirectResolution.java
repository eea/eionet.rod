package eionet.rod.web.util;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.RedirectResolution;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SeeOtherRedirectResolution extends RedirectResolution {
        public SeeOtherRedirectResolution(String url) {
            super(url);
        }

        public SeeOtherRedirectResolution(Class<? extends ActionBean> beanType) {
            super(beanType);
        }

        public SeeOtherRedirectResolution(Class<? extends ActionBean> beanType, String event) {
            super(beanType, event);
        }

        @Override
        public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String url = getUrl(request.getLocale());
            String contextPath = request.getContextPath();

            if (contextPath.length() > 1 && !url.startsWith(contextPath + "/")) {
                url = contextPath + url;
            }

            url = response.encodeRedirectURL(url);

            response.setStatus(HttpServletResponse.SC_SEE_OTHER);
            response.setHeader("Location", url);
        }
}
