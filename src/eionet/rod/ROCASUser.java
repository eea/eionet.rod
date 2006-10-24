package eionet.rod;

import com.tee.xmlserver.*;
import com.tee.uit.security.AuthMechanism;
import com.tee.uit.security.SignOnException;

public class ROCASUser extends ROUser {

	public ROCASUser() {
		super();
	}

	public boolean authenticate(String userName, String userPws) {
		invalidate();

		// LOG
		if (Logger.enable(5))
			Logger.log("Create ROD user '" + userName + "'");
		try {
			fullName = AuthMechanism.getFullName(userName);
		} catch (SignOnException e) {
			Logger.log("Fatal error: can not get full name for authaticated user", e);
		}
		//
		authented = true;
		user = userName;
		password = userPws;

		return authented;
	}

}