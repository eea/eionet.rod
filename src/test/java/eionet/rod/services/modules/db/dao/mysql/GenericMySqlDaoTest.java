package eionet.rod.services.modules.db.dao.mysql;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public class GenericMySqlDaoTest extends BaseMySqlDaoTest {

    private GenericMySqlDao genericMySqlDao = new GenericMySqlDao();

    public GenericMySqlDaoTest(String arg0) throws Exception {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
     * Test method for
     * 'eionet.rod.services.modules.db.dao.mysql.GenericMySqlDao.getTable(String)'
     */
    public void testGetTable() throws Exception {
        Vector<Map<String,String>> v = genericMySqlDao.getTable("T_ISSUE");
        assertEquals(12, v.size());
//      printVectorResult(genericMySqlDao.getTable("T_ISSUE"));
    }

    /*
     * Test method for
     * 'eionet.rod.services.modules.db.dao.mysql.GenericMySqlDao.getTableDesc(String)'
     */
    public void testGetTableDesc() throws Exception {
        Vector<Hashtable<String,String>> v = genericMySqlDao.getTableDesc("T_ISSUE");
        assertEquals(4, v.size());
//      printVectorResult(genericMySqlDao.getTable("T_ISSUE"));
    }

}
