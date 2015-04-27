package eionet.rod.services;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * Email sending service.
 *  
 */
public interface EmailServiceIF {
    void sendToSysAdmin(String subject, String body) throws MessagingException, ServiceException, IOException;

}
