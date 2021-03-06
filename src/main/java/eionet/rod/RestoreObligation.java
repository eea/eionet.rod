package eionet.rod;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import eionet.rod.services.RODServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestoreObligation extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestoreObligation.class);
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        // req.setCharacterEncoding("UTF-8");
        int update = 0;
        String id = req.getParameter("id");
        String pid = req.getParameter("pid");
        String latestversion = req.getParameter("latestversion");
        if (latestversion != null && !latestversion.equalsIgnoreCase("") && id != null && !id.equalsIgnoreCase("")) {
            int latestVer = Integer.parseInt(latestversion);
            try {

                update = RODServices.getDbService().getObligationDao()
                        .getRestoreObligation(Integer.valueOf(id), Integer.valueOf(pid), latestVer);

            } catch (Exception e) {
                LOGGER.error("RestoreObligation.service() error when updating ");
                LOGGER.error(e.getMessage(), e);
            }
        }

        LOGGER.debug("RestoreObligation.service() update status: " + update);
        if (update != 0) {
            res.sendRedirect("versions.jsp?id=" + pid);
        } else {
            LOGGER.error("RestoreObligation.service() update status is not correct.");
        }
    }
}
