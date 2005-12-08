package eionet.rod.countrysrv.servlets;

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

import eionet.rod.Attrs;
import eionet.rod.ROUser;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;

public class Subscribe extends HttpServlet {
    
    /** */
	public static String CHANNEL_NAME = null;
	public static String server_url = null;
	
	public static String predEventType = null;
    
    private static FileServiceIF fileSrv = null;

    /** */    
    private Hashtable preferencesMap;
	private String appName = "webrod";

	/*
	 *  (non-Javadoc)
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	public void init() throws ServletException{
		try{
            fileSrv=RODServices.getFileService();
            
            predEventType = fileSrv.getStringProperty(FileServiceIF.UNS_EVENTTYPE_PREDICATE);
            server_url = fileSrv.getStringProperty(FileServiceIF.UNS_XMLRPC_SERVER_URL);
            CHANNEL_NAME = fileSrv.getStringProperty(FileServiceIF.UNS_CHANNEL_NAME);
            
    		preferencesMap = new Hashtable();
    		preferencesMap.put(
    		"country", fileSrv.getStringProperty(FileServiceIF.UNS_COUNTRY_PREDICATE));
    		preferencesMap.put(
    		"issue", fileSrv.getStringProperty(FileServiceIF.UNS_ISSUE_PREDICATE));
    		preferencesMap.put(
    		"obligation", fileSrv.getStringProperty(FileServiceIF.UNS_OBLIGATION_PREDICATE));
    		preferencesMap.put(
    		"organisation", fileSrv.getStringProperty(FileServiceIF.UNS_ORGANISATION_PREDICATE));
        } catch (Throwable t) {
            t.printStackTrace(System.out);
            throw new ServletException(t);
        }
	}
	    
    /*
     *  (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void service(HttpServletRequest req, HttpServletResponse res)
                                            throws ServletException, IOException {
        
        try {
            
        req.getSession().removeAttribute("SUCCESS");
		
		String userName = getUserName(req);;
		
		// set up the filters
		
		String[] eventTypes = req.getParameterValues("event_type");
		if (eventTypes==null || eventTypes.length==0){
			eventTypes = new String[1];
			eventTypes[0] = "";
		}
		
		Vector filters = new Vector();	
        Hashtable filter = new Hashtable();

		for (int i=0; i<eventTypes.length; i++){
			
			filter = new Hashtable();
			
			String eventType = eventTypes[i];
			if (eventType!=null && eventType.length()>0)
				filter.put(predEventType, eventType);
					
			
			Enumeration parNames = req.getParameterNames();
			while (parNames!=null && parNames.hasMoreElements()){
				String parName = (String)parNames.nextElement();
				String prefPredicate = (String)preferencesMap.get(parName);
				if (prefPredicate!=null && prefPredicate.length()>0){
					String parValue = req.getParameter(parName);
					if (parValue!=null && parValue.length()>0){
						filter.put(prefPredicate, parValue);
					}
				}
			}
			
			if (filter.size()>0)
				filters.add(filter);
		}
		
		// call RPC method
        if (filters.size()>0){
	        	
				XmlRpcClient server = new XmlRpcClient(server_url);
				server.setBasicAuthentication(FileServiceIF.UNS_USERNAME, FileServiceIF.UNS_PWD);
	        	
	            // make subscription
				Vector params = new Vector();
				params = new Vector();
	            params.add(CHANNEL_NAME);
	            params.add(userName);
	            params.add(filters);            
	            String makeSubscription =
	            (String) server.execute("makeSubscription", params);

        }
        
        req.getSession().setAttribute("SUCCESS", "");
        res.sendRedirect("subscribe.jsp");
        
        } catch (Throwable t) {
            t.printStackTrace(System.out);
            throw new ServletException(t);
        }
    }

	/*
	 * 
	 */
	private String getUserName(HttpServletRequest req) throws ServletException{
		
		String username = null;
		
		ServletContext ctx = getServletContext();		
		if (ctx!=null){
			String appName = ctx.getInitParameter(Attrs.APPPARAM);
			ROUser o =
			(ROUser) req.getSession().getAttribute(Attrs.USERPREFIX + appName);
			
			username = o!=null ? o.getUserName() : null;
		}
		
		if (username==null)
			throw new ServletException("Failed to get the username");
		
		return username;
	}
	
}