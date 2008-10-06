package eionet.rod.web.util;

import eionet.rod.RODUtil;
import eionet.rod.ROUser;

/**
 * 
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
public class JstlFunctions {

	/**
	 * 
	 * @param userName
	 * @param aclName
	 * @param permission
	 * @return
	 */
	public static boolean hasPermission(java.lang.String userName, java.lang.String aclName, java.lang.String permission){
		return ROUser.hasPermission(userName, aclName, permission);
	}
	
	/**
	 * 
	 * @param in
	 * @return
	 */
	public static String replaceTags(String in) {
		return RODUtil.replaceTags(in);
	}
	
	/**
	 * 
	 * @param in
	 * @param length
	 * @return
	 */
	public static String threeDots(String in, Integer length) {
		return RODUtil.threeDots(in, length.intValue());
	}


	
}
