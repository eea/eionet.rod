package eionet.rod.services.modules.db.dao.mysql.test;

import java.util.Hashtable;
import java.util.Random;

import eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao;

public class RoleMySqlDaoTest extends BaseMySqlDaoTest {

	RoleMySqlDao roleMySqlDao = new RoleMySqlDao(){};
	
	String role_name = "EPER Data Reporter Test";                        
	String role_email = "eper-dat-test@roles.eea.eionet.eu.int";                          
	String role_url = "http://ldap.eionet.europa.eu:389/Public/irc/eionet-circle/Home/central_dir_admin?fn=roles&v=eper-dat-se";                                                                                                            
	String role_id  = "eper-dat-test";
	String role_members_url ="http://ldap.eionet.europa.eu:389/Members/irc/eionet-circle/Home/central_dir_admin?fn=roles&v=eper-dat-se";                                                                                                     
	String person = "Test user"; 
	String institute = "Test institute";

	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(RoleMySqlDaoTest.class);
	}

	public RoleMySqlDaoTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.saveRole(Hashtable, String, String)'
	 */
	public void testSaveRole() throws Exception{
		Hashtable role = new Hashtable();
		role.put("ID",role_id);
		role.put("URL",role_url);
		role.put("URL_MEMBERS",role_members_url);
		role.put("DESCRIPTION",role_id);
		role.put("MAIL",role_email);
		roleMySqlDao.saveRole(role);
		// FIXME: Must add assert statement here

	}
	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.savePerson(String, String, String)'
	 */
	public void testSavePerson() throws Exception{
		// FIXME: Must add assert statement here
		roleMySqlDao.savePerson(role_id,person +( new Random()).nextInt(), institute +( new Random()).nextInt());
		// FIXME: Must add assert statement here that tests addiotional record
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.getRoleIds()'
	 */
	public void testGetRoleIds() throws Exception{
//		printMatrixResult(roleMySqlDao.getRoleIds());
		String [][] m = roleMySqlDao.getRoleIds();
		assertEquals(1, m.length);
	}


	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.backUpRoles()'
	 */
	public void testBackUpRoles() throws Exception{
		roleMySqlDao.backUpRoles();
		// FIXME: Must add assert statement here
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.commitRoles()'
	 */
	public void testCommitRoles() throws Exception{
		roleMySqlDao.commitRoles();
		// FIXME: Must add assert statement here
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.getRoleDesc(String)'
	 */
	public void testGetRoleDesc() throws Exception{
		Hashtable  ht = roleMySqlDao.getRoleDesc(role_id);
		// FIXME: Must add assert statement here
//		System.out.println(ht);
	}

}
