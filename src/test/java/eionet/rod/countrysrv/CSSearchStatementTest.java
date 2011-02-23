package eionet.rod.countrysrv;

import eionet.rod.services.modules.db.dao.mysql.test.BaseMySqlDaoTest;

public class CSSearchStatementTest extends BaseMySqlDaoTest {
	
	public CSSearchStatementTest(String arg0) {
		super(arg0);
	}
	
	public void testDummyTest() { }

    //  check for Unknown column 'T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY' in 'on clause'
    /*public void test_query() throws Exception
    {
                // Create the mock objects
                HttpServletRequest request = createMock(HttpServletRequest.class);
                String reqString = "COUNTRY_ID=3&ORD=NEXT_REPORTING,%20DEADLINE";
                Parameters params = new Parameters(request, reqString);
                CSSearchStatement stmt = new CSSearchStatement(params, false, false);
                Connection conn = getConnection().getConnection();
                PreparedStatement ps = stmt.prepareStatement(conn);
                stmt.executeQuery();
    }*/

}
