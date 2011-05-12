package eionet.rod;

import java.util.Vector;

import javax.servlet.ServletException;

import org.apache.xmlrpc.XmlRpcClient;

import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;

/**
 * @author jaanus
 * 
 *         To change the template for this generated type comment go to Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UNSEventSender {

    public UNSEventSender() {
    }

    /*
     *
     */
    public static void makeCall(Object notifications) throws Exception {
        try {
            FileServiceIF fileSrv = RODServices.getFileService();
            String server_url = fileSrv.getStringProperty(FileServiceIF.UNS_XMLRPC_SERVER_URL);
            String channel_name = fileSrv.getStringProperty(FileServiceIF.UNS_CHANNEL_NAME);
            if (notifications == null)
                throw new Exception("Cannot send a null object via XML-RPC");

            XmlRpcClient server = new XmlRpcClient(server_url);
            server.setBasicAuthentication(fileSrv.getStringProperty(FileServiceIF.UNS_USERNAME),
                    fileSrv.getStringProperty(FileServiceIF.UNS_PWD));

            Vector<Object> params = new Vector<Object>();
            params.add(channel_name);
            params.add(notifications);

            server.execute(fileSrv.getStringProperty(FileServiceIF.UNS_SEND_NOTIFICATION), params);

        } catch (Throwable t) {
            t.printStackTrace(System.out);
            throw new ServletException(t);
        }
    }
}
