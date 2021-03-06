package eionet.rod.services.modules.db.dao.mysql;

import java.util.Hashtable;
import java.util.Vector;


public class HistoryMySqlDaoTest extends BaseMySqlDaoTest {

    
    private HistoryMySqlDao historyMySqlDao = new HistoryMySqlDao();
    
    public HistoryMySqlDaoTest(String arg0) throws Exception {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.HistoryMySqlDao.HistoryMySqlDao()'
     */
    public void testHistoryMySqlDao() throws Exception {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.HistoryMySqlDao.getItemHistory(String, int)'
     */
    public void testGetItemHistory() throws Exception {
//      printMatrixResult(historyMySqlDao.getItemHistory("A",514));
                String[][] matrix = historyMySqlDao.getItemHistory("A",514);
        assertEquals(7, matrix.length);

    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.HistoryMySqlDao.getDeletedItems(String)'
     */
    public void testGetDeletedItems() throws Exception {
                String[][] matrix = historyMySqlDao.getDeletedItems("A");
        assertEquals(2, matrix.length);
//      printMatrixResult(historyMySqlDao.getDeletedItems("A"));

    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.HistoryMySqlDao.getDeletedItemsVector(String)'
     */
    public void testGetDeletedItemsVector() throws Exception {
        Vector<Hashtable<String,Object>> v = new Vector<Hashtable<String,Object>>(1);
        v.add(historyMySqlDao.getDeletedItemsVector("A"));
                assertEquals(1, v.size());
//      printVectorResult(v);


    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.HistoryMySqlDao.getHistory(Integer, String)'
     */
    public void testGetHistory() throws Exception {
        assertEquals(7, historyMySqlDao.getHistory(514,"T_OBLIGATION").size());
//      printVectorResult(historyMySqlDao.getHistory(514,"T_OBLIGATION"));
    }

}
