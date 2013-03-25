package eionet.rod.services.modules.db.dao.mysql;

import java.util.Map;
import java.util.Vector;

public class ClientMySqlDaoTest extends BaseMySqlDaoTest {

    public ClientMySqlDaoTest(String arg0) throws Exception {
        super(arg0);
    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ClientMySqlDao.getOrganisations()'
     */
    public void testGetOrganisations() throws Exception {

        Vector<Map<String,String>> v = new ClientMySqlDao().getOrganisations();
        assertEquals(4, v.size());
    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ClientMySqlDao.getObligationOrg(Integer)'
     */
    public void testGetObligationOrg() throws Exception{
        Vector<Map<String,String>> v = new ClientMySqlDao().getObligationOrg(new Integer(514));
        assertEquals(1, v.size());

    }

}
