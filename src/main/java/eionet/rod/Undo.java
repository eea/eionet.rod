package eionet.rod;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import eionet.rod.services.RODServices;

public class Undo extends HttpServlet {

    /*
     *  (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void service(HttpServletRequest req, HttpServletResponse res)
                                            throws ServletException, IOException {

        HttpSession session = req.getSession();
        String appName = getServletContext().getInitParameter(Attrs.APPPARAM);
        ROUser rouser = (ROUser) session.getAttribute(Attrs.USERPREFIX + appName);

        if (rouser == null) {
            res.sendRedirect("versions.jsp?id=-1");
        } else {
            String values = req.getParameter("group");
            String[] va = new String[4];
            StringTokenizer st = new StringTokenizer(values,",");
            int i = 0;
            while (st.hasMoreTokens()) {
                va[i] = st.nextToken();
                i++;
            }

            String location = null;

            try {
                location = RODServices.getDbService().getUndoDao().undo(Long.valueOf(va[0]).longValue(), va[1], va[2], va[3]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            res.sendRedirect(location);
        }
    }
}
