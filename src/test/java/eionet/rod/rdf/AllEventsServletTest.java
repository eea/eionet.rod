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
public class AllEventsServletTest extends BaseMySqlDaoTest {

    public AllEventsServletTest(String arg0) throws Exception {
        super(arg0);
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        FlatXmlDataSet loadedDataSet;
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        loadedDataSet = builder.build(this.getClass().getClassLoader().getResourceAsStream("seed-events.xml"));
        return loadedDataSet;

    }

    /**
     * If the frequency is every 12 months, then the event will not appear
     * until 36 days before.
     */
    @Test
    public void testNoParameters() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("allevents.rss", AllEvents.class.getName());
        ServletUnitClient sc = sr.newClient();
        WebRequest request   = new GetMethodWebRequest("http://test.meterware.com/allevents.rss");
        WebResponse response = sc.getResponse(request);
        Assert.assertNotNull("No response received", response);
        Assert.assertEquals("content type", "application/rss+xml", response.getContentType());
        String resp = response.getText();
        //System.out.println(resp);

        // Obl. 15
        Assert.assertTrue(resp.contains("<ev:startdate>2007-01-01</ev:startdate>"));
        Assert.assertTrue(resp.contains("<title>Deadline for Reporting Obligation: &lt;&#038;&gt;</title>"));
        Assert.assertTrue(resp.contains("<description>&quot;&#038;amp;&quot;</description>"));
        // Obl. 15 a year later
        Assert.assertTrue(resp.contains("<ev:startdate>2008-01-01</ev:startdate>"));
        // Obl. 514
        Assert.assertTrue(resp.contains("<title>Deadline for Reporting Obligation: Obl. 514—&gt;&#038;&lt;</title>"));
        Assert.assertTrue(resp.contains("<ev:startdate>2008-08-31</ev:startdate>"));
        Assert.assertTrue(resp.contains("<description>&#038;&#038;&quot;—</description>"));
    }

    @Test
    public void testWithIssuesParameter() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("allevents.rss", AllEvents.class.getName());
        ServletUnitClient sc = sr.newClient();
        WebRequest request   = new GetMethodWebRequest("http://test.meterware.com/allevents.rss");
        // issue 1 OR 4. No obligation in test data has #4
        request.setParameter("issues", "1,4");
        WebResponse response = sc.getResponse(request);
        Assert.assertNotNull("No response received", response);
        Assert.assertEquals("content type", "application/rss+xml", response.getContentType());
        String resp = response.getText();
        //System.out.println(resp);

        // Obl. 15
        Assert.assertTrue(resp.contains("<ev:startdate>2008-01-01</ev:startdate>"));
        Assert.assertTrue(resp.contains("<title>Deadline for Reporting Obligation: &lt;&#038;&gt;</title>"));
        Assert.assertTrue(resp.contains("<description>&quot;&#038;amp;&quot;</description>"));
    }

    @Test
    public void testWithCountriesParameter() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("allevents.rss", AllEvents.class.getName());
        ServletUnitClient sc = sr.newClient();
        WebRequest request   = new GetMethodWebRequest("http://test.meterware.com/allevents.rss");
        request.setParameter("countries", "1,2,3");
        WebResponse response = sc.getResponse(request);
        Assert.assertNotNull("No response received", response);
        Assert.assertEquals("content type", "application/rss+xml", response.getContentType());
        String resp = response.getText();
        //System.out.println(resp);

        // Obl. 15
        Assert.assertTrue(resp.contains("<ev:startdate>2007-01-01</ev:startdate>"));
        Assert.assertTrue(resp.contains("<title>Deadline for Reporting Obligation: &lt;&#038;&gt;</title>"));
        Assert.assertTrue(resp.contains("<description>&quot;&#038;amp;&quot;</description>"));
    }
} 
