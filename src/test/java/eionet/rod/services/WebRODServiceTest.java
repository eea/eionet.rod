package eionet.rod.services;

import eionet.rod.services.modules.db.dao.mysql.BaseMySqlDaoTest;
import junit.framework.Assert;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by kaido on 19.02.2015.
 */
public class WebRODServiceTest extends BaseMySqlDaoTest {


    public WebRODServiceTest(String arg0) throws Exception {
        super(arg0);
    }


    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
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
    public void testGetObligation() throws Exception {
        WebRODService ws = new WebRODService();
        Vector<Hashtable<String, String>> obligations = ws.getObligations("2", "", "", "");
        Assert.assertTrue(obligations.size() == 1);
    }
}
