package eionet.rod.services.modules;

import javax.mail.PasswordAuthentication;

/**
 * Authenticator for email sending.
 */
public class EMailAuthenticator extends javax.mail.Authenticator {

    /** */
    private String user;
    /** */
    private String password;

    /**
     *
     * @param username username
     * @param password password
     */
    public EMailAuthenticator(String username, String password) {
        super();
        this.user = username;
        this.password = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PasswordAuthentication getPasswordAuthentication() {

        return new PasswordAuthentication(user, password);
    }
}
