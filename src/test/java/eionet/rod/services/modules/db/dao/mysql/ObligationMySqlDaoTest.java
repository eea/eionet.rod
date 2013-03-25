package eionet.rod.services.modules.db.dao.mysql;

import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public class ObligationMySqlDaoTest extends BaseMySqlDaoTest {

    ObligationMySqlDao obligationMySqlDao;

    public ObligationMySqlDaoTest(String arg0) throws Exception {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        obligationMySqlDao = new ObligationMySqlDao();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getDeadlines()'
     */
    public void testGetDeadlines() throws Exception{
        String [][] m = obligationMySqlDao.getDeadlines();
        assertEquals(2, m.length);
//      printMatrixResult(obligationMySqlDao.getDeadlines());

    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.saveDeadline(Integer, Date, Date, Date)'
     */
    public void testSaveDeadline() throws Exception{
        obligationMySqlDao.saveDeadline(new Integer(15),"2007-01-01","2008-01-01","2005-01-01");

    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.saveTerminate(Integer, String)'
     */
    public void testSaveTerminate() throws Exception{
        obligationMySqlDao.saveTerminate(new Integer(15),"N");
    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getRaData()'
     */
    public void testGetRaData() throws Exception{
        String [][] m = obligationMySqlDao.getRaData();
        assertEquals(2, m.length);
//      printMatrixResult(m);

    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getRespRoles()'
     */
    public void testGetRespRoles() throws Exception{
        String[] roles = obligationMySqlDao.getRespRoles();
        assertEquals(3, roles.length);
        /*
        System.out.println("Roles " + "\n");
        for (int i = 0; i < roles.length; i++) {
            System.out.println(roles[i]);
        }
        */

    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getUpcomingDeadlines(double)'
     */
    public void testGetUpcomingDeadlines() throws Exception{
        Vector<Map<String,String>> v = obligationMySqlDao.getUpcomingDeadlines((double)2);
        assertEquals(0, v.size());
        statement = getConnection().getConnection().createStatement();
        statement.executeUpdate("update T_OBLIGATION SET next_deadline = (CURDATE() + INTERVAL 10 DAY) where PK_RA_ID=15");
        v = obligationMySqlDao.getUpcomingDeadlines((double)2);
        assertEquals(1, v.size());
//      printVectorResult(v);


    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getActivities()'
     */
    public void testGetActivities() throws Exception{
        Vector<Map<String,String>> v = obligationMySqlDao.getActivities();
        assertEquals(2, v.size());
//      printVectorResult(v);


    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getObligationsForRDF()'
     */
    /*public void testGetObligations() throws Exception{
        List<ObligationRdfDTO> list = obligationMySqlDao.getObligationsForRDF();
        assertEquals(2, list.size());
//      printVectorResult(v);
    }*/

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getActivityDeadlines(StringTokenizer, StringTokenizer)'
     */
    public void testGetActivityDeadlines() throws Exception{
        // TODO Auto-generated method stub


    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getAllActivityDeadlines(StringTokenizer, StringTokenizer)'
     */
    public void testGetAllActivityDeadlines() throws Exception{
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getIssueActivities(StringTokenizer, StringTokenizer)'
     */
    public void testGetIssueActivities() throws Exception{
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getObligationById(Integer)'
     */
    public void testGetObligationById() throws Exception{
        Hashtable<String,String> hash = obligationMySqlDao.getObligationById(new Integer(15));
        assertEquals(15, Integer.parseInt(hash.get("obligationID")));
//      printVectorResult(v);


    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getObligationDetail(Integer)'
     */
    public void testGetObligationDetail() throws Exception{
        Vector<Map<String,String>> v = obligationMySqlDao.getObligationDetail(new Integer(15));
        assertEquals(1, v.size());
//      printVectorResult(v);
    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getObligationIds()'
     */
    public void testGetObligationIds() throws Exception{
        String[][] m = obligationMySqlDao.getObligationIds();
        assertEquals(2, m.length);
//      printMatrixResult(m);

    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getROComplete()'
     */
    public void testGetROComplete() throws Exception{
        Vector<Map<String,String>> v = obligationMySqlDao.getROComplete();
        assertEquals(2, v.size());
//      printVectorResult(v);

    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getROSummary()'
     */
    public void testGetROSummary() throws Exception{
        Vector<Map<String,String>> v = obligationMySqlDao.getROSummary();
        assertEquals(2, v.size());
//      printVectorResult(v);

    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.getRODeadlines()'
     */
    public void testGetRODeadlines() throws Exception{
        Vector<Map<String,String>> v = obligationMySqlDao.getRODeadlines();
        assertEquals(2, v.size());
//      printVectorResult(v);
    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.dpsirValuesFromExcelToDB(int, String)'
     */
    public void testDpsirValuesFromExcelToDB() throws Exception{
        obligationMySqlDao.dpsirValuesFromExcelToDB(15,"P");
        statement = getConnection().getConnection().createStatement();
        resultSet = statement.executeQuery("select DPSIR_P from T_OBLIGATION where PK_RA_ID=15");
        resultSet.next();
        assertEquals("yes", resultSet.getString("DPSIR_P"));
    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao.harvestParams(Integer)'
     */
    /*public void testHarvestParams() throws Exception{
        //T_PARAMETER, T_PARAMETER_LNK - there is no such tables in ROD db anymore
        //obligationMySqlDao.harvestParams(new Integer(15));

    }*/

        
}
