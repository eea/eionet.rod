package eionet.rod;

import eionet.rod.services.ServiceException;
import eionet.rod.services.WebRODService;

public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            WebRODService ws = new WebRODService();
            ws.getObligations("110", "", "", "");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

}
