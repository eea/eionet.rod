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
    public void testSimpleTest() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("allevents.rss", AllEvents.class.getName());

        ServletUnitClient sc = sr.newClient();
        WebRequest request   = new GetMethodWebRequest( "http://test.meterware.com/allevents.rss" );
        //request.setParameter( "color", "red" );
        WebResponse response = sc.getResponse( request );
        Assert.assertNotNull( "No response received", response );
        Assert.assertEquals( "content type", "application/rss+xml", response.getContentType() );
        String resp = response.getText();
        //System.out.println(resp);
        Assert.assertTrue(resp.contains("<title>Deadline for Reporting Obligation: &lt;&#038;&gt;</title>"));
        Assert.assertTrue(resp.contains("<ev:startdate>2008-08-31</ev:startdate>"));
        Assert.assertTrue(resp.contains("<ev:startdate>2008-01-01</ev:startdate>"));
        Assert.assertTrue(resp.contains("<description>&quot;&#038;amp;&quot;</description>"));
    }

}
