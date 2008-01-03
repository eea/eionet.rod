package eionet.rod.services.modules.db.dao.mysql.test;

import java.util.Vector;

import eionet.rod.services.modules.db.dao.mysql.ClientMySqlDao;


public class ClientMySqlDaoTest extends BaseMySqlDaoTest {

	public ClientMySqlDaoTest(String arg0) {
		super(arg0);
	}

	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ClientMySqlDaoTest.class);
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.ClientMySqlDao.getOrganisations()'
	 */
	public void testGetOrganisations() throws Exception {

                Vector v = new ClientMySqlDao().getOrganisations();
                assertEquals(4, v.size());
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.ClientMySqlDao.getObligationOrg(Integer)'
	 */
	public void testGetObligationOrg() throws Exception{
                Vector v = new ClientMySqlDao().getObligationOrg(new Integer(514));
                assertEquals(1, v.size());

	}

}
