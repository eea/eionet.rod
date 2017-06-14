package eionet.rod;

import java.util.Vector;

import org.apache.xmlrpc.XmlRpcClient;

import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple utility class for sending UNS notifications.
 *
 * @author altnyris
 * @author heinlja
 */
public class UNSEventSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(UNSEventSender.class);

    /**
     * Hide utility class constructor.
     */
    private UNSEventSender() {
        // Nothing to do here.
    }

    /**
     * Utility class for sending the given notifications.
     *
     * @param notifications The notifications to be sent.
     */
    @SuppressWarnings("deprecation")
    public static void makeCall(Vector<Vector<String>> notifications) {
        try {
            FileServiceIF fileSrv = RODServices.getFileService();
            String server_url = fileSrv.getStringProperty(FileServiceIF.UNS_XMLRPC_SERVER_URL);
            String channel_name = fileSrv.getStringProperty(FileServiceIF.UNS_CHANNEL_NAME);
            if (notifications == null) {
                throw new Exception("Cannot send a null object via XML-RPC");
            }

            XmlRpcClient server = new XmlRpcClient(server_url);
            server.setBasicAuthentication(fileSrv.getStringProperty(FileServiceIF.UNS_USERNAME),
                    fileSrv.getStringProperty(FileServiceIF.UNS_PWD));

            Vector<Vector<String>> sanitizedNotifications = sanitizeNotifications(notifications);
            Vector<Object> params = new Vector<Object>();
            params.add(channel_name);
            params.add(sanitizedNotifications);

            String remoteMethodName = fileSrv.getStringProperty(FileServiceIF.UNS_SEND_NOTIFICATION);
            server.execute(remoteMethodName, params);

        } catch (Exception e) {
            LOGGER.error("Failed to send notification", e);
        }
    }

    /**
     * Sanitizes the given notifications before they should be sent.
     *
     * @param notifications The given notifications.
     * @return Sanitized notifications.
     */
    private static Vector<Vector<String>> sanitizeNotifications(Vector<Vector<String>> notifications) {

        if (notifications == null) {
            return new Vector<Vector<String>>();
        }

        // Now vector for the sanitized notifications.
        Vector<Vector<String>> result = new Vector<Vector<String>>();

        // Loop through the vector of notifications.
        // Each notification is a vector too, containing 3 elements, representing an RDF triple (i.e. subject, predicate, obejct).
        for (Vector<String> notification : notifications) {

            if (notification == null) {
                // Lets replace a null notification with an empty vector, to avoid null-pointer exceptions.
                result.add(new Vector<String>());
            } else {
                Vector<String> sanitizedNotification = new Vector<String>();
                for (String subjectOrPredicateOrObject : notification) {
                    // If a subject/predicate/object is null, replace it with empty string.
                    sanitizedNotification.add(subjectOrPredicateOrObject == null ? "" : subjectOrPredicateOrObject);
                }
                result.add(sanitizedNotification);
            }
        }

        return result;
    }
}
