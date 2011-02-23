package eionet.rod;

import java.util.Vector;

import eionet.rod.services.ServiceException;
import eionet.rod.services.WebRODService;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			WebRODService ws = new WebRODService();
			Vector obligations = ws.getObligations("110", "", "", "");
			String a = "a";
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

}
