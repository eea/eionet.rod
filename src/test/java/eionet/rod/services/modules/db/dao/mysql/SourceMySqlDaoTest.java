package eionet.rod.services.modules.db.dao.mysql;

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
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.SourceMySqlDao.getInstrumentsRSS()'
     */
    public void testGetInstrumentsRSS() throws Exception {
        String [][] m = sourceMySqlDao.getInstrumentsRSS();
        assertEquals(2, m.length);
//      printMatrixResult(sourceMySqlDao.getInstrumentsRSS());

    }

}
