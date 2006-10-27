package eionet.rod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;

import org.apache.xmlrpc.XmlRpcClient;

import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * @author risto alt
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DeadlinesDaemon {
    
    public DeadlinesDaemon(){
    }
    
	/*
	 * 
	 */
	public static void main(String[] args) throws ServiceException {
        
        try{
            
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
            String d = formatter.format(new Date());
            Date today = formatter.parse(d);
            long time = today.getTime();
            
            String fileName = RODServices.getFileService().getStringProperty(FileServiceIF.DEADLINES_DAEMON_DATEFILE);
            File file = new File(fileName);
            file.createNewFile();
            FileReader in = new FileReader(file);
            BufferedReader br = new BufferedReader(in);
            FileWriter out = null;
            String result = br.readLine();
            if(result != null){
                Date date = formatter.parse(result);
                if(date.before(today)){
                    out = new FileWriter(file);
                    out.write(d);
                    makeStructure(date);
                }
            } else {
                out = new FileWriter(file);
                out.write(d);
                makeStructure(today);
            }
            if (out != null) {
                out.flush();
                out.close();
            }
            in.close();
            
        } catch (Throwable t){
            t.printStackTrace(System.out);
        }
	}
    
    private static void makeStructure(Date date) throws Exception{
        
        Vector lists = new Vector();
        int percent = RODServices.getFileService().getIntProperty( FileServiceIF.PERCENT_OF_FREQ);
        double days = (percent / 100.0) * 30.0;
        
            Vector vec = RODServices.getDbService().getObligationDao().getUpcomingDeadlines(days);
            long timestamp = System.currentTimeMillis();
            
            
            for (Enumeration en = vec.elements(); en.hasMoreElements(); ){
                Hashtable h = (Hashtable) en.nextElement();
                
                String nd = (String) h.get("next_deadline");
                int year = Integer.parseInt(nd.substring(0,4)) - 1900;
                int month = Integer.parseInt(nd.substring(5,7)) - 1;
                int day = Integer.parseInt(nd.substring(8,10));
                Date nextDeadline = new Date(year, month, day);
                
                long nextDeadlineMillis = nextDeadline.getTime();
                
                String freq = (String) h.get("freq");
                int f = Integer.parseInt(freq);
                int period = new Double(days * f).intValue();
                long periodMillis = (new Long(period).longValue() * new Long(24).longValue() * new Long(3600).longValue() * new Long(1000).longValue());
                Date periodStartDate = new Date(nextDeadlineMillis - periodMillis);
                
                if(!periodStartDate.before(date)){
                
                    Vector list = new Vector();
                    String events = "http://rod.eionet.europa.eu/events/" + timestamp;
                    
                    list.add(events);
                    list.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                    list.add(Attrs.SCHEMA_RDF + "Deadlineevent");
                    lists.add(list);
                    
                    list = new Vector();
                    list.add(events);
                    list.add(Attrs.SCHEMA_RDF + "event_type");
                    list.add("Deadlineevent");
                    lists.add(list);
                    
                    list = new Vector();
                    list.add(events);
                    list.add(Attrs.SCHEMA_RDF + "label");
                    list.add("Approaching deadline");
                    lists.add(list);
                    
                    list = new Vector();
                    list.add(events);
                    list.add(Attrs.SCHEMA_RDF + "nextdeadline");
                    list.add(h.get("next_deadline"));
                    lists.add(list);
                    
                    list = new Vector();
                    list.add(events);
                    list.add(Attrs.SCHEMA_RDF + "nextdeadline2");
                    list.add(h.get("next_deadline2"));
                    lists.add(list);
                    
                    list = new Vector();
                    list.add(events);
                    list.add(Attrs.SCHEMA_RDF + "obligation");
                    list.add(h.get("title"));
                    lists.add(list);
                    
                    String id = (String) h.get("id");
                    Vector countries = RODServices.getDbService().getSpatialDao().getObligationCountries(Integer.valueOf(id).intValue());
                    for(Enumeration cen = countries.elements(); cen.hasMoreElements(); ){
                        Hashtable hash = (Hashtable) cen.nextElement();
                            String country = (String) hash.get("name");
                            if (country != null && !country.equals("")) {
                                list = new Vector();
                                list.add(events);
                                list.add(Attrs.SCHEMA_RDF + "locality");
                                list.add(country);
                                lists.add(list);
                            }
                    }
                    
                    list = new Vector();
                    list.add(events);
                    list.add(Attrs.SCHEMA_RDF + "responsiblerole");
                    list.add(h.get("responsible_role"));
                    lists.add(list);
                    
                    list = new Vector();
                    list.add(events);
                    list.add("http://purl.org/dc/elements/1.1/identifier");
                    String url = "http://rod.eionet.europa.eu/show.jsv?id="+h.get("id")+"&aid="+h.get("src_id")+"&mode=A";
                    list.add(url);
                    lists.add(list);
                    
                    if (lists.size() > 0){
                        makeCall(lists);
                    }
                }
            }
    }
    
    public static void makeCall(Object notifications) throws Exception{
        try{
            FileServiceIF fileSrv = RODServices.getFileService();
            String server_url = fileSrv.getStringProperty(FileServiceIF.UNS_XMLRPC_SERVER_URL);
            String channel_name = fileSrv.getStringProperty(FileServiceIF.UNS_CHANNEL_NAME);
            if (notifications==null)
                throw new Exception("Cannot send a null object via XML-RPC");
            
            XmlRpcClient server = new XmlRpcClient(server_url);
            server.setBasicAuthentication(fileSrv.getStringProperty(FileServiceIF.UNS_USERNAME), fileSrv.getStringProperty(FileServiceIF.UNS_PWD));
            
            Vector params = new Vector();
            params.add(channel_name);
            params.add(notifications);
    
            String result = null;
            result = (String) server.execute(fileSrv.getStringProperty(FileServiceIF.UNS_SEND_NOTIFICATION), params);
    
        } catch (Throwable t) {
            t.printStackTrace(System.out);
            throw new ServletException(t);
        }
    }
}
