package eionet.rod.services.modules.db.dao.mysql.test;

import java.util.Vector;

import eionet.rod.services.modules.db.dao.mysql.SpatialMySqlDao;

public class SpatialMySqlDaoTest extends BaseMySqlDaoTest {

	private SpatialMySqlDao spatialMySqlDao = new SpatialMySqlDao();
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(SpatialMySqlDaoTest.class);
	}

	public SpatialMySqlDaoTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.SpatialMySqlDao.getCountryIdPairs()'
	 */
	public void testGetCountryIdPairs() throws Exception{
		String [][] m = spatialMySqlDao.getCountryIdPairs();
		assertEquals(3, m.length);
//		printMatrixResult(spatialMySqlDao.getCountryIdPairs());

	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.SpatialMySqlDao.getCountries()'
	 */
	public void testGetCountries() throws Exception{		
		Vector v = spatialMySqlDao.getCountries();
		assertEquals(3, v.size());
//		printVectorResult(spatialMySqlDao.getCountries());
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.SpatialMySqlDao.getCountryById(int)'
	 */
	public void testGetCountryById() throws Exception{
		assertEquals("Austria",spatialMySqlDao.getCountryById(3));
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.SpatialMySqlDao.getCountries(int)'
	 */
	public void testGetCountriesInt() throws Exception{
		String [][] m = spatialMySqlDao.getCountries(15);
		assertEquals(2, m.length);
//		printMatrixResult(spatialMySqlDao.getCountries(15));

	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.SpatialMySqlDao.getObligationCountries(int)'
	 */
	public void testGetObligationCountries() throws Exception{
//		printVectorResult(spatialMySqlDao.getObligationCountries(15)) ;
		Vector v = spatialMySqlDao.getObligationCountries(15);
		assertEquals(2, v.size());
	}

}
