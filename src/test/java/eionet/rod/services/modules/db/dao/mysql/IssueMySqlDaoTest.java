package eionet.rod.services.modules.db.dao.mysql;

import java.util.Map;
import java.util.Vector;


public class IssueMySqlDaoTest extends BaseMySqlDaoTest {

    private IssueMySqlDao issueMySqlDao = new IssueMySqlDao();
    
    public IssueMySqlDaoTest(String arg0) throws Exception {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.IssueMySqlDao.getIssues()'
     */
    public void testGetIssues() throws Exception{
        Vector<Map<String,String>> v = issueMySqlDao.getIssues();
        assertEquals(12, v.size());
        //printVectorResult(v);
    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.IssueMySqlDao.getObligationIssues(Integer)'
     */
    public void testGetObligationIssues()throws Exception {
        Vector<Map<String,String>> v = issueMySqlDao.getObligationIssues(new Integer(514));
        assertEquals(2, v.size());
        //printVectorResult(v);
    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.IssueMySqlDao.getIssueIdPairs()'
     */
    public void testGetIssueIdPairs() throws Exception{
        String[][] m = issueMySqlDao.getIssueIdPairs();
        assertEquals(12, m.length);
        //printMatrixResult(m);

    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.IssueMySqlDao.getIssues(Integer)'
     */
    public void testGetIssuesInteger() throws Exception{
        String[][] m = issueMySqlDao.getIssues(new Integer(514));
        assertEquals(2, m.length);
        //printMatrixResult(m);

    }

}
