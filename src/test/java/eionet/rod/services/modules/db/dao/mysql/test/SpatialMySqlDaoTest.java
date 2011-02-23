package eionet.rod.services.modules.db.dao.mysql.test;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import eionet.rod.dto.CountryDTO;
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
		Vector<Map<String,String>> v = spatialMySqlDao.getCountries();
		assertEquals(3, v.size());
		
		for(Map<String,String> hash : v){
			if(hash != null){
				String val = (String) hash.get("name");
				assertNotNull(val);
			}
		}
//		printVectorResult(spatialMySqlDao.getCountries());
	}
	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.SpatialMySqlDao.getCountryById(int)'
	 */
	public void testGetCountryById() throws Exception{
		assertEquals("Austria",spatialMySqlDao.getCountryById(3));
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.SpatialMySqlDao.getObligationCountries(int)'
	 */
	/*public void testGetObligationCountries() throws Exception{
//		printVectorResult(spatialMySqlDao.getObligationCountries(15)) ;
		List<Integer> list = spatialMySqlDao.getObligationCountries(15, true);
		assertEquals(1, list.size());
	}*/
	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.SpatialMySqlDao.getMemberCountries()'
	 */
	public void testGetMemberCountries() throws Exception{
		List<CountryDTO> v = spatialMySqlDao.getMemberCountries();
		assertEquals(1, v.size());
	}
	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.SpatialMySqlDao.getNonMemberCountries()'
	 */
	public void testGetNonMemberCountries() throws Exception{
		List<CountryDTO> v = spatialMySqlDao.getNonMemberCountries();
		assertEquals(2, v.size());
	}

}
