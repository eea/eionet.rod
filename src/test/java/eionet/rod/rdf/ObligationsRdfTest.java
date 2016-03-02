package eionet.rod.rdf;

import eionet.rod.services.modules.db.dao.mysql.BaseMySqlDaoTest;
import eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import junit.framework.Assert;
import org.junit.Test;

public class ObligationsRdfTest extends BaseMySqlDaoTest {

    ObligationMySqlDao obligationMySqlDao;

    public ObligationsRdfTest(String arg0) throws Exception {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        obligationMySqlDao = new ObligationMySqlDao();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private void assertContains(String result, String expected) {
        Assert.assertTrue(expected, result.contains(expected));
    }

    private void assertNotContains(String result, String notExpected) {
        Assert.assertFalse(notExpected, result.contains(notExpected));
    }
    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    @Test
    public void testObligations514() throws Exception {

        obligationMySqlDao.saveDeadline(514, "2008-08-31", "2009-08-31", "2005-01-01");
        RdfExporter.execute("obligations", "514");
        ResourceBundle props = ResourceBundle.getBundle("rdfexport");
        String dir = props.getString("files.dest.dir");
        File file = new File(dir, "obligations.rdf");
        String obligationsRdf = readFile(file.getAbsolutePath(), StandardCharsets.UTF_8);
        assertContains(obligationsRdf, "<Obligation rdf:about=\"obligations/514\">");
        assertContains(obligationsRdf, "<nextdeadline rdf:datatype=\"http://www.w3.org/2001/XMLSchema#date\">2008-08-31</nextdeadline>");
        assertContains(obligationsRdf, "<nextdeadline2 rdf:datatype=\"http://www.w3.org/2001/XMLSchema#date\">2009-08-31</nextdeadline2>");

    }

    @Test
    public void testObligations15() throws Exception {

        String obligationsRdf;
        obligationMySqlDao.saveDeadline(15, "2016-02-29", "", "2015-02-29");
        RdfExporter.execute("obligations", "15");
        ResourceBundle props = ResourceBundle.getBundle("rdfexport");
        String dir = props.getString("files.dest.dir");
        File file = new File(dir, "obligations.rdf");
        obligationsRdf = readFile(file.getAbsolutePath(), StandardCharsets.UTF_8);
        assertContains(obligationsRdf, "<Obligation rdf:about=\"obligations/15\">");
        assertContains(obligationsRdf, "<nextdeadline rdf:datatype=\"http://www.w3.org/2001/XMLSchema#date\">2016-02-29</nextdeadline>");

    }

    @Test
    public void testObligationsMissing() throws Exception {

        String obligationsRdf;
        RdfExporter.execute("obligations", "55555555");
        ResourceBundle props = ResourceBundle.getBundle("rdfexport");
        String dir = props.getString("files.dest.dir");
        File file = new File(dir, "obligations.rdf");
        obligationsRdf = readFile(file.getAbsolutePath(), StandardCharsets.UTF_8);
        assertNotContains(obligationsRdf, "<Obligation rdf:about=\"obligations/55555555\">");

    }

}
