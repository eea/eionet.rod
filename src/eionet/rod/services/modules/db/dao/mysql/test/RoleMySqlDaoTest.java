package eionet.rod.services.modules.db.dao.mysql.test;

import java.util.Hashtable;
import java.util.Random;

import eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao;

public class RoleMySqlDaoTest extends BaseMySqlDaoTest {

	RoleMySqlDao roleMySqlDao = new RoleMySqlDao(){};
	
	String role_name = "EPER Data Reporter Test";                        
	String role_email = "eper-dat-test@roles.eea.eionet.eu.int";                          
	String role_url = "http://eea.eionet.eu.int:8980/Public/irc/eionet-circle/Home/central_dir_admin?fn=roles&v=eper-dat-se";                                                                                                            
	String role_id  = "eper-dat-test";
	String role_members_url ="http://eea.eionet.eu.int:8980/Members/irc/eionet-circle/Home/central_dir_admin?fn=roles&v=eper-dat-se";                                                                                                     
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
		statement = connection.createStatement();
		statement.executeUpdate("delete from T_ROLE where PERSON like 'Test user%';");
		closeAllResources();
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
		roleMySqlDao.saveRole(role,person,institute);

	}
	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.savePerson(String, String, String)'
	 */
	public void testSavePerson() throws Exception{
		
		roleMySqlDao.savePerson(role_id,person +( new Random()).nextInt(), institute +( new Random()).nextInt());
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.getRoleIds()'
	 */
	public void testGetRoleIds() throws Exception{
		printMatrixResult(roleMySqlDao.getRoleIds());
	}


	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.backUpRoles()'
	 */
	public void testBackUpRoles() throws Exception{
		roleMySqlDao.backUpRoles();
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.commitRoles()'
	 */
	public void testCommitRoles() throws Exception{
		roleMySqlDao.commitRoles();
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.getRoleDesc(String)'
	 */
	public void testGetRoleDesc() throws Exception{
		Hashtable  ht = roleMySqlDao.getRoleDesc(role_id);
		System.out.println(ht);
	}

}
