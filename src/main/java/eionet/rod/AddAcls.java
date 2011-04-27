package eionet.rod;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tee.uit.security.AccessController;
import com.tee.uit.security.SignOnException;

import eionet.rod.services.RODServices;


public class AddAcls extends HttpServlet {
    /*
     *  (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void service(HttpServletRequest req, HttpServletResponse res)
                                            throws ServletException, IOException {

        try {
            String[][] obligations = RODServices.getDbService().getObligationDao().getObligationIds();
            HashMap acls = AccessController.getAcls();
            for (int i=0; i<obligations.length; i++) {
                String id = obligations[i][0];
                String aclPath = "/obligations/"+id;
                if (!acls.containsKey(aclPath)) {
                    RODServices.getDbService().getAclDao().addAcl(aclPath, "", "");
                }
            }
            String[][] instruments = RODServices.getDbService().getSourceDao().getInstrumentIds();
            for (int i=0; i<instruments.length; i++) {
                String id = instruments[i][0];
                String aclPath = "/instruments/"+id;
                if (!acls.containsKey(aclPath)) {
                    RODServices.getDbService().getAclDao().addAcl(aclPath, "", "");
                }
            }
            res.sendRedirect("index.html");
        } catch (Throwable t) {
            t.printStackTrace(System.out);
        }
    }
}
