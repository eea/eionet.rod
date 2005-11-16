package eionet.rod;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eionet.rod.services.RODServices;

public class RestoreObligation extends HttpServlet {
    
    /*
     *  (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void service(HttpServletRequest req, HttpServletResponse res)
                                            throws ServletException, IOException {
                                            	
        // req.setCharacterEncoding("UTF-8");
        int update = 0;
		String id = req.getParameter("id");
        String pid = req.getParameter("pid");
        String latestversion = req.getParameter("latestversion");
        if (latestversion != null && !latestversion.equalsIgnoreCase("") &&
                id != null && !id.equalsIgnoreCase("")){
            int latestVer = Integer.parseInt(latestversion);
            try{
                
                update = RODServices.getDbService().getRestoreObligation(id, pid, latestVer);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
		System.out.println("+++++++++++++UPDATE: " +update);
        if (update != 0){
             res.sendRedirect("versions.jsp?id=" +pid);
        } else 
            System.out.println("Error");
    }
}