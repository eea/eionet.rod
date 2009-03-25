package eionet.rod.services.modules.db.dao.mysql.test;

import java.util.List;

import eionet.rod.dto.HistDeadlineDTO;
import eionet.rod.services.modules.db.dao.mysql.HistoricDeadlineMySqlDao;

public class HistoricDeadlineMySqlDaoTest extends BaseMySqlDaoTest {

	private HistoricDeadlineMySqlDao historicDeadlineMySqlDao = new HistoricDeadlineMySqlDao();
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(HistoricDeadlineMySqlDaoTest.class);
	}

	public HistoricDeadlineMySqlDaoTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.HistoricDeadlineMySqlDao.getHistoricDeadlines(String, String)'
	 */
	public void testGetHistoricDeadlines() throws Exception {
		List<HistDeadlineDTO> list = historicDeadlineMySqlDao.getHistoricDeadlines("01/01/2000","01/01/2007");
		assertEquals(3, list.size());
//		printVectorResult(historicDeadlineMySqlDao.getHistoricDeadlines("01/01/2000","01/01/2007"));

	}
	
}
