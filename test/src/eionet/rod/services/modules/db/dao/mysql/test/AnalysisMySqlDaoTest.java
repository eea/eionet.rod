package eionet.rod.services.modules.db.dao.mysql.test;

import eionet.rod.services.modules.db.dao.mysql.AnalysisMySqlDao;

public class AnalysisMySqlDaoTest extends BaseMySqlDaoTest {
	
	public AnalysisMySqlDaoTest(String arg0) {
		super(arg0);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(AnalysisMySqlDaoTest.class);
	}
	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.AnalysisMySqlDao.getTotalRa()'
	 */
	public void testGetTotalRa() throws Exception {
		int total = new AnalysisMySqlDao().getTotalRa();
		assertEquals(2, total);
	}
	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.AnalysisMySqlDao.getTotalLi()'
	 */
	public void testGetTotalLi() throws Exception {
		int total = new AnalysisMySqlDao().getTotalLi();
		assertEquals(2, total);
	}
	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.AnalysisMySqlDao.getLastUpdateRa()'
	 */
	public void testGetLastUpdateRa() throws Exception {
		String date = new AnalysisMySqlDao().getLastUpdateRa();
		assertEquals("22/08/07", date);
	}
	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.AnalysisMySqlDao.getLastUpdateLi()'
	 */
	public void testGetLastUpdateLi() throws Exception {
		String date = new AnalysisMySqlDao().getLastUpdateLi();
		assertEquals("27/11/07", date);
	}
	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.AnalysisMySqlDao.getEeaCore()'
	 */
	public void testGetEeaCore() throws Exception {
		int cnt = new AnalysisMySqlDao().getEeaCore();
		assertEquals(1, cnt);
	}
	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.AnalysisMySqlDao.getEeaPriority()'
	 */
	public void testGetEeaPriority() throws Exception {
		int cnt = new AnalysisMySqlDao().getEeaPriority();
		assertEquals(1, cnt);
	}
	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.AnalysisMySqlDao.getOverlapRa()'
	 */
	public void testGetOverlapRa() throws Exception {
		int cnt = new AnalysisMySqlDao().getOverlapRa();
		assertEquals(1, cnt);
	}
	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.AnalysisMySqlDao.getFlaggedRa()'
	 */
	public void testGetFlaggedRa() throws Exception {
		int cnt = new AnalysisMySqlDao().getFlaggedRa();
		assertEquals(1, cnt);
	}

}
