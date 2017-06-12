package eionet.rod;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import eionet.rod.services.RODServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Undo extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(Undo.class);

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String appName = getServletContext().getInitParameter(Attrs.APPPARAM);
        ROUser rouser = (ROUser) session.getAttribute(Attrs.USERPREFIX + appName);

        if (rouser == null) {
            res.sendRedirect("versions.jsp?id=-1");
        } else {
            String values = req.getParameter("group");
            String[] va = new String[4];
            StringTokenizer st = new StringTokenizer(values, ",");
            int i = 0;
            while (st.hasMoreTokens()) {
                va[i] = st.nextToken();
                i++;
            }

            String location = null;

            try {
                location = RODServices.getDbService().getUndoDao().undo(Long.valueOf(va[0]).longValue(), va[1], va[2], va[3]);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new ServletException(e.getMessage(), e.getCause());
            }
            res.sendRedirect(location);
        }
    }
}
