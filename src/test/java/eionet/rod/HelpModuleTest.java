package eionet.rod;

import eionet.help.Helps;
import eionet.rod.services.modules.db.dao.mysql.BaseMySqlDaoTest;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


/**
 * Integration test to verify that the eionet.help module works correctly.
 * As long as ROD doesn't configure where to load the help module's configuration
 * this test shows that the fallback to properties file works.
 */
public class HelpModuleTest extends BaseMySqlDaoTest {

    /**
     * Constructor. BaseMySqlDaoTest needs to be cleaned up.
     */
    public HelpModuleTest(String arg0) throws Exception {
        super(arg0);
    }

    @Override
    protected String getSeedFilename() {
        return "seed-hlp.xml";
    }

    @Test
    public void testSimpleValues() throws Exception {
        String html = Helps.get("nr1", "announcements", "en");
        assertEquals("We now have unit tests", html);
    }

}

