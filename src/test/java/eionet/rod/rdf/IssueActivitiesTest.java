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
public class IssueActivitiesTest extends BaseMySqlDaoTest {

    public IssueActivitiesTest(String arg0) throws Exception {
        super(arg0);
    }

    private void assertContains(String result, String expected) {
        Assert.assertTrue(expected, result.contains(expected));
    }

    private void assertNotContains(String result, String notExpected) {
        Assert.assertFalse(notExpected, result.contains(notExpected));
    }

    private void checkRSSContentType(WebResponse response) {
        Assert.assertNotNull("No response received", response);
        Assert.assertEquals("content type", "application/rss+xml", response.getContentType());
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        FlatXmlDataSet loadedDataSet;
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        loadedDataSet = builder.build(this.getClass().getClassLoader().getResourceAsStream("seed-events.xml"));
        return loadedDataSet;

    }

    @Test
    public void testNoParameters() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("instruments.rss", IssueActivities.class.getName());
        ServletUnitClient sc = sr.newClient();
        WebRequest request   = new GetMethodWebRequest("http://test.meterware.com/instruments.rss");
        WebResponse response = sc.getResponse(request);
        //Assert.assertNotNull("No response received", response);
        //Assert.assertEquals("content type", "application/rss+xml", response.getContentType());
        checkRSSContentType(response);
        String resp = response.getText();
        //System.out.println(resp);

        assertNotContains(resp, "xmlns:ev=\" xmlns:ev=");
        // Obl. 15
        assertContains(resp, "<ev:startdate>2007-01-01</ev:startdate>");
        assertContains(resp, "<title>&lt;&amp;&gt;</title>");
        assertContains(resp, "<description>&quot;&amp;amp;&quot;</description>");
        // Obl. 514
        assertContains(resp, "<title>Obl. 514—&gt;&amp;&lt;</title>");
        assertContains(resp, "<ev:startdate>2008-08-31</ev:startdate>");
        assertContains(resp, "<description>&amp;&amp;&quot;—</description>");
    }

} 
