package eionet.rod;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlrpc.XmlRpcClient;

import com.tee.uit.server.ServiceException;

import eionet.rod.Attrs;
import eionet.rod.ROUser;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;

public class EditPeriod extends HttpServlet {
    
	    
    /*
     *  (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void service(HttpServletRequest req, HttpServletResponse res)
                                            throws ServletException, IOException {
        
        String start_date = req.getParameter("from");
        String end_date = req.getParameter("to");
        String spatialHistoryID = req.getParameter("spatialHistoryID");
        String ra_id = req.getParameter("ra_id");
        
        try{
            
        RODServices.getDbService().getSpatialHistoryDao().editPeriod(start_date,end_date,spatialHistoryID,ra_id);
        
        } catch (Exception e){
            e.printStackTrace();
        }
        res.sendRedirect("spatialhistory.jsv?ID="+ra_id);
        
    }

}