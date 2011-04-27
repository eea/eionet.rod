package eionet.rod;

import com.tee.uit.security.AuthMechanism;
import com.tee.uit.security.SignOnException;

public class ROCASUser extends ROUser {

    public ROCASUser() {
        super();
    }

    public boolean authenticate(String userName, String userPws) {
        invalidate();

        try {
            fullName = AuthMechanism.getFullName(userName);
        } catch (SignOnException e) {
            logger.error("Fatal error: can not get full name for authaticated user", e);
        }
        //
        authented = true;
        user = userName;
        password = userPws;

        return authented;
    }

    /**
     *
     * @return
     */
    public static ROCASUser create(String userName) {
        ROCASUser user = new ROCASUser();
        user.authenticate(userName, null);
        return user;
    }

}
