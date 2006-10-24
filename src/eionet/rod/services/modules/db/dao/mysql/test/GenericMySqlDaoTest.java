package eionet.rod.services.modules.db.dao.mysql.test;

import eionet.rod.services.modules.db.dao.mysql.GenericMySqlDao;

public class GenericMySqlDaoTest extends BaseMySqlDaoTest {

	private GenericMySqlDao genericMySqlDao = new GenericMySqlDao();

	public static void main(String[] args) {
		junit.textui.TestRunner.run(GenericMySqlDaoTest.class);
	}

	public GenericMySqlDaoTest(String arg0) {
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
		printVectorResult(genericMySqlDao.getTable("T_ISSUE"));
	}

	/*
	 * Test method for
	 * 'eionet.rod.services.modules.db.dao.mysql.GenericMySqlDao.getTableDesc(String)'
	 */
	public void testGetTableDesc() throws Exception {
		printVectorResult(genericMySqlDao.getTable("T_ISSUE"));
	}

}
