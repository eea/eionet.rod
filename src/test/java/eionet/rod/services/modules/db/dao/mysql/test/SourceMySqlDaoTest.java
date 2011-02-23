package eionet.rod.services.modules.db.dao.mysql.test;

import eionet.rod.services.modules.db.dao.mysql.SourceMySqlDao;

public class SourceMySqlDaoTest extends BaseMySqlDaoTest {

	private SourceMySqlDao sourceMySqlDao = new SourceMySqlDao();
	public static void main(String[] args) {
		junit.textui.TestRunner.run(SourceMySqlDaoTest.class);
	}

	public SourceMySqlDaoTest(String arg0) {
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
		printVectorResult(sourceMySqlDao.getInstruments());
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.SourceMySqlDao.getInstrumentsRSS()'
	 */
	public void testGetInstrumentsRSS() throws Exception{
		String [][] m = sourceMySqlDao.getInstrumentsRSS();
		assertEquals(2, m.length);
//		printMatrixResult(sourceMySqlDao.getInstrumentsRSS());

	}

}
