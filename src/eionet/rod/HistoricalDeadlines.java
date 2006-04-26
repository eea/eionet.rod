package eionet.rod;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class HistoricalDeadlines extends HttpServlet {
    
    /*
     *  (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void service(HttpServletRequest req, HttpServletResponse res)
                                            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        String appName = getServletContext().getInitParameter(Attrs.APPPARAM);
        ROUser rouser = (ROUser) session.getAttribute(Attrs.USERPREFIX + appName);
        
        if(rouser == null){
            res.sendRedirect("index.html");
        } else {        
            String start_date = req.getParameter("start_date");
            String end_date = req.getParameter("end_date");
            
            res.sendRedirect("histdeadlines.jsp?start_date="+start_date+"&end_date="+end_date);
        }
    }
}