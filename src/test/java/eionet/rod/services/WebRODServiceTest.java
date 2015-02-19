package eionet.rod.services;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by kaido on 19.02.2015.
 */
public class WebRODServiceTest {

    @Test
    public void getObligationTest() throws ServiceException {
        WebRODService ws = new WebRODService();
        Vector<Hashtable<String, String>> obligations = ws.getObligations("2", "", "", "");
        Assert.assertTrue(obligations.size() == 1);
    }
}
