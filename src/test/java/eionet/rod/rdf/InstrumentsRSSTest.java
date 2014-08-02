package eionet.rod.rdf;

import eionet.rod.services.modules.db.dao.mysql.BaseMySqlDaoTest;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.GetMethodWebRequest;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 */
public class InstrumentsRSSTest extends BaseMySqlDaoTest {

    public InstrumentsRSSTest(String arg0) throws Exception {
        super(arg0);
    }

    private void assertContains(String result, String expected) {
        Assert.assertTrue(expected, result.contains(expected));
    }

    private void assertNotContains(String result, String notExpected) {
        Assert.assertFalse(notExpected, result.contains(notExpected));
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        FlatXmlDataSet loadedDataSet;
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        loadedDataSet = builder.build(this.getClass().getClassLoader().getResourceAsStream("seed-rod.xml"));
        return loadedDataSet;

    }

    @Test
    public void testNoParameters() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("instruments.rss", InstrumentsRSS.class.getName());
        ServletUnitClient sc = sr.newClient();
        WebRequest request   = new GetMethodWebRequest("http://test.meterware.com/instruments.rss");
        WebResponse response = sc.getResponse(request);
        Assert.assertNotNull("No response received", response);
        Assert.assertEquals("content type", "application/rss+xml", response.getContentType());
        String resp = response.getText();
        //System.out.println(resp);

        assertNotContains(resp, "xmlns:ev=\" xmlns:ev=");
        assertContains(resp, "<channel rdf:about=\"http://rod.eionet.europa.eu/instruments.rss\">");
        assertContains(resp, "<rdf:li rdf:resource=\"http://rod.eionet.europa.eu/instruments/170\"/>");
        // Instrument 170
        assertContains(resp, "<title>Convention for the protection of the marine environment of the north-east Atlantic</title>");
        assertContains(resp, "<description>The Convention for the Protection of the Marine Environment o");
        // Instrument 499
        assertContains(resp, "<title>EEA Annual Management Plan</title>");
        assertContains(resp, "<link>http://rod.eionet.europa.eu/instruments/499</link>");
        assertContains(resp, "<description>Former name was Annual Work Programme</description>");
    }

} 
