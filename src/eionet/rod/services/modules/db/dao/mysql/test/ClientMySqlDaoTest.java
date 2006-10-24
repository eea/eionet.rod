package eionet.rod.services.modules.db.dao.mysql.test;


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
			printVectorResult(new ClientMySqlDao().getOrganisations());
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.ClientMySqlDao.getObligationOrg(Integer)'
	 */
	public void testGetObligationOrg() throws Exception{
		printVectorResult(new ClientMySqlDao().getObligationOrg(new Integer(15)));

	}

}
