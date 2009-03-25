package eionet.rod.services.modules.db.dao.mysql.test;

import java.util.Hashtable;
import java.util.Random;

import eionet.rod.dto.ResponsibleRoleDTO;
import eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao;

public class RoleMySqlDaoTest extends BaseMySqlDaoTest {

	RoleMySqlDao roleMySqlDao;
	
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
		roleMySqlDao = new RoleMySqlDao();
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

		statement = connection.createStatement();
		resultSet = statement.executeQuery("select ROLE_ID from T_ROLE where ROLE_EMAIL='"+role_email+"'");
		resultSet.next();
		assertEquals(role_id,resultSet.getString("ROLE_ID"));

	}
	
	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.savePerson(String, String, String)'
	 */
	/*public void testSavePerson() throws Exception{
		roleMySqlDao.savePerson(role_id,person +( new Random()).nextInt(), institute +( new Random()).nextInt());
		  
		METHOD savePerson is no longer in use - occupants are saved in method saveRole 
	}*/

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.getRoleIds()'
	 */
	public void testGetRoleIds() throws Exception{
//		printMatrixResult(roleMySqlDao.getRoleIds());
		String [][] m = roleMySqlDao.getRoleIds();
		assertEquals(2, m.length);
	}


	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.backUpRoles()'
	 */
	public void testBackUpRoles() throws Exception{
		roleMySqlDao.backUpRoles();
		statement = connection.createStatement();
		resultSet = statement.executeQuery("select count(ROLE_ID) from T_ROLE where STATUS=0");
		resultSet.next();
		assertEquals(0, resultSet.getInt(1));
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.commitRoles()'
	 */
	public void testCommitRoles() throws Exception{
		roleMySqlDao.commitRoles();
		statement = connection.createStatement();
		resultSet = statement.executeQuery("select count(ROLE_ID) from T_ROLE where STATUS=0");
		resultSet.next();
		assertEquals(0, resultSet.getInt(1));	
	}

	/*
	 * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.getRoleDesc(String)'
	 */
	public void testGetRoleDesc() throws Exception{
		ResponsibleRoleDTO  ht = roleMySqlDao.getRoleDesc(role_id, role_name);
		assertEquals("eper-dat-test@roles.eea.eionet.eu.int",ht.getEmail());
//		System.out.println(ht);
	}

}
