package eionet.rod.services.modules;

import eionet.rod.services.EmailServiceIF;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import org.apache.commons.lang3.BooleanUtils;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

/**
 * Created by kaido on 16.04.2015.
 */
public class EmailServiceImpl implements EmailServiceIF {

    /**
     * Sends the email to system administrators in To: field.
     *
     * @param subject Subject of email
     * @param body Message
     * @throws javax.mail.MessagingException if sending fails
     */
    public  void sendToSysAdmin(String subject, String body) throws MessagingException, ServiceException, IOException {
        send(getSysAdmins(), subject, body, false);
    }

    /**
     * Sends the email - if there is a mail host in the configuration file.
     *
     * @param to Email recipients
     * @param subject Subject of email
     * @param body Message
     * @param ccSysAdmin whether to CC system administrators
     * @throws MessagingException if sending fails
     */
    public void send(String[] to, String subject, String body, boolean ccSysAdmin)
            throws MessagingException, ServiceException, IOException {


        FileServiceIF fService = RODServices.getFileService();
        
        // if no mail.host specified in the properties, go no further
        String mailHost = fService.getStringProperty(fService.MAIL_HOST);
        if (mailHost == null || mailHost.trim().length() == 0) {
            return;
        }

        Authenticator authenticator = null;

        Session session;
        if (BooleanUtils.isTrue(fService.getBooleanProperty(fService.MAIL_SMTP_AUTH))) {
            String user = fService.getStringProperty(fService.MAIL_USER);
            String password = fService.getStringProperty(fService.MAIL_PASSWORD);
            authenticator = new EMailAuthenticator(user, password);
            session = Session.getDefaultInstance(fService.getProps(), authenticator);
        } else {
            session = Session.getDefaultInstance(fService.getProps());
        }
        MimeMessage message = new MimeMessage(session);
        for (int i = 0; to != null && i < to.length; i++) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
        }

        if (ccSysAdmin) {
            String[] sysAdmins = getSysAdmins();
            if (sysAdmins != null) {
                for (int i = 0; i < sysAdmins.length; i++) {
                    String sysAdmin = sysAdmins[i].trim();
                    if (sysAdmin.length() > 0) {
                        message.addRecipient(Message.RecipientType.CC, new InternetAddress(sysAdmin));
                    }
                }
            }
        }

        message.setSubject("[ROD] " + subject);
        message.setText(body);
        Transport.send(message);
    }

    /**
     * Returns syadmins email addresses.
     *
     * @return String[] list of sysadmin email addresses
     */
    private String[] getSysAdmins() throws ServiceException {

        FileServiceIF fService = RODServices.getFileService();
        String s = fService.getStringProperty(FileServiceIF.MAIL_SYSADMINS);
        if (s != null) {
            s = s.trim();
            if (s.length() > 0) {
                return s.split(",");
            }
        }

        return new String[0];
    }
}
