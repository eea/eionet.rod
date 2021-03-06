package eionet.rod;

import eionet.acl.AuthMechanism;
import eionet.acl.SignOnException;

public class ROCASUser extends ROUser {

    public ROCASUser() {
        super();
    }

    public boolean authenticate(String userName, String userPws) {
        invalidate();

        try {
            fullName = AuthMechanism.getFullName(userName);
        } catch (SignOnException e) {
            LOGGER.error("Fatal error: can not get full name for authaticated user", e);
        }
        //
        authented = true;
        user = userName;
        password = userPws;

        return authented;
    }

    /**
     * 
     * @param userName 
     * @return ROCASUser
     */
    public static ROCASUser create(String userName) {
        ROCASUser user = new ROCASUser();
        user.authenticate(userName, null);
        return user;
    }

}
