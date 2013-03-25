package eionet.rod.services.modules.db.dao.mysql;

import java.util.Map;
import java.util.Vector;

public class SourceMySqlDaoTest extends BaseMySqlDaoTest {

    private SourceMySqlDao sourceMySqlDao = new SourceMySqlDao();

    public SourceMySqlDaoTest(String arg0) throws Exception {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.SourceMySqlDao.getInstruments()'
     */
    public void testGetInstruments() throws Exception{
        Vector<Map<String, String>> vector = sourceMySqlDao.getInstruments();
        assertEquals(2, vector.size());
    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.SourceMySqlDao.getInstrumentsRSS()'
     */
    public void testGetInstrumentsRSS() throws Exception {
        String [][] m = sourceMySqlDao.getInstrumentsRSS();
        assertEquals(2, m.length);
//      printMatrixResult(sourceMySqlDao.getInstrumentsRSS());

    }

}
