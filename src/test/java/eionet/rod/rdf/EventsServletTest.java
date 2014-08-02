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
public class EventsServletTest extends BaseMySqlDaoTest {

    public EventsServletTest(String arg0) throws Exception {
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
        loadedDataSet = builder.build(this.getClass().getClassLoader().getResourceAsStream("seed-events.xml"));
        return loadedDataSet;

    }

    @Test
    public void testNoParameters() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("events.rss", FakeEvents.class.getName());
        ServletUnitClient sc = sr.newClient();
        WebRequest request   = new GetMethodWebRequest("http://test.meterware.com/events.rss");
        WebResponse response = sc.getResponse(request);
        Assert.assertNotNull("No response received", response);
        Assert.assertEquals("content type", "application/rss+xml", response.getContentType());
        String resp = response.getText();
        //System.out.println(resp);

        assertNotContains(resp, "xmlns:ev=\" xmlns:ev=");
        // Obl. 15
        assertContains(resp, "<ev:startdate>2007-01-01</ev:startdate>");
        assertContains(resp, "<title>Deadline for Reporting Obligation: &lt;&#038;&gt;</title>");
        assertContains(resp, "<description>&quot;&#038;amp;&quot;</description>");
        // Obl. 15 a year later
        assertNotContains(resp, "<ev:startdate>2008-01-01</ev:startdate>");
        // Obl. 514
        assertNotContains(resp, "<title>Deadline for Reporting Obligation: Obl. 514—&gt;&#038;&lt;</title>");
        assertNotContains(resp, "<ev:startdate>2008-08-31</ev:startdate>");
        assertNotContains(resp, "<description>&#038;&#038;&quot;—</description>");
    }

    @Test
    public void testWithIssuesParameter() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("events.rss", FakeEvents.class.getName());
        ServletUnitClient sc = sr.newClient();
        WebRequest request   = new GetMethodWebRequest("http://test.meterware.com/events.rss");
        // issue 1 OR 4. No obligation in test data has #4
        request.setParameter("issues", "1,4");
        WebResponse response = sc.getResponse(request);
        Assert.assertNotNull("No response received", response);
        Assert.assertEquals("content type", "application/rss+xml", response.getContentType());
        String resp = response.getText();
        //System.out.println(resp);

        // Obl. 15
        assertContains(resp, "<ev:startdate>2007-01-01</ev:startdate>");
        assertContains(resp, "<title>Deadline for Reporting Obligation: &lt;&#038;&gt;</title>");
        assertContains(resp, "<description>&quot;&#038;amp;&quot;</description>");
        // Obl. 514 is not there.
        assertNotContains(resp, "<ev:startdate>2008-08-31</ev:startdate>");
    }

    @Test
    public void testWithCountriesParameter() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("events.rss", FakeEvents.class.getName());
        ServletUnitClient sc = sr.newClient();
        WebRequest request   = new GetMethodWebRequest("http://test.meterware.com/events.rss");
        request.setParameter("countries", "2");
        WebResponse response = sc.getResponse(request);
        Assert.assertNotNull("No response received", response);
        Assert.assertEquals("content type", "application/rss+xml", response.getContentType());
        String resp = response.getText();
        //System.out.println(resp);

        // Obl. 15
        assertContains(resp, "<ev:startdate>2007-01-01</ev:startdate>");
        assertContains(resp, "<title>Deadline for Reporting Obligation: &lt;&#038;&gt;</title>");
        assertContains(resp, "<description>&quot;&#038;amp;&quot;</description>");
        // Obl. 514 is not there.
        assertNotContains(resp, "<ev:startdate>2008-08-31</ev:startdate>");
    }

    @Test
    public void testWithBothParameters() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("events.rss", FakeEvents.class.getName());
        ServletUnitClient sc = sr.newClient();
        WebRequest request   = new GetMethodWebRequest("http://test.meterware.com/events.rss");
        request.setParameter("issues", "6");
        request.setParameter("countries", "1");
        WebResponse response = sc.getResponse(request);
        Assert.assertNotNull("No response received", response);
        Assert.assertEquals("content type", "application/rss+xml", response.getContentType());
        String resp = response.getText();
        //System.out.println(resp);

        // Obl. 15
        assertNotContains(resp, "<ev:startdate>2007-01-01</ev:startdate>");
        assertNotContains(resp, "<title>Deadline for Reporting Obligation: &lt;&#038;&gt;</title>");
        assertNotContains(resp, "<description>&quot;&#038;amp;&quot;</description>");
        // Obl. 15 a year later
        assertNotContains(resp, "<ev:startdate>2008-01-01</ev:startdate>");
        // Obl. 514
        assertNotContains(resp, "<title>Deadline for Reporting Obligation: Obl. 514—&gt;&#038;&lt;</title>");
        assertNotContains(resp, "<ev:startdate>2008-08-31</ev:startdate>");
        assertNotContains(resp, "<description>&#038;&#038;&quot;—</description>");
    }

} 
