package eionet.rod.services.modules.db.dao.mysql;

import java.util.Hashtable;

import eionet.rod.dto.ResponsibleRoleDTO;

public class RoleMySqlDaoTest extends BaseMySqlDaoTest {

    RoleMySqlDao roleMySqlDao;
    
    String role_name = "EPER Data Reporter Test";                        
    String role_email = "eper-dat-test@roles.eea.eionet.europa.eu";                          
    String role_url = "http://ldap.eionet.europa.eu:389/Public/irc/eionet-circle/Home/central_dir_admin?fn=roles&v=eper-dat-se";                                                                                                            
    String role_id  = "eper-dat-test";
    String role_members_url ="http://ldap.eionet.europa.eu:389/Members/irc/eionet-circle/Home/central_dir_admin?fn=roles&v=eper-dat-se";                                                                                                     
    String person = "Test user"; 
    String institute = "Test institute";

    
    public RoleMySqlDaoTest(String arg0) throws Exception {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        roleMySqlDao = new RoleMySqlDao();
    }
    
    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.saveRole(Hashtable, String, String)'
     */
    public void testSaveRole() throws Exception {
        Hashtable<String,Object> role = new Hashtable<String,Object>();
        role.put("ID", role_id);
        role.put("URL", role_url);
        role.put("URL_MEMBERS", role_members_url);
        role.put("DESCRIPTION", role_id);
        role.put("MAIL", role_email);
        roleMySqlDao.saveRole(role);

        statement = getConnection().getConnection().createStatement();
        resultSet = statement.executeQuery("select ROLE_ID from T_ROLE where ROLE_EMAIL='" + role_email + "'");
        resultSet.next();
        assertEquals(role_id, resultSet.getString("ROLE_ID"));

        ResponsibleRoleDTO  ht = roleMySqlDao.getRoleDesc(role_id, role_name);
        assertEquals(role_email, ht.getEmail());
    }
    
    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.savePerson(String, String, String)'
     */
    /*public void testSavePerson() throws Exception {
        roleMySqlDao.savePerson(role_id, person + ( new Random()).nextInt(), institute + ( new Random()).nextInt());
          
        METHOD savePerson is no longer in use - occupants are saved in method saveRole 
    }*/

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.getRoleIds()'
     */
    public void testGetRoleIds() throws Exception {
//      printMatrixResult(roleMySqlDao.getRoleIds());
        String [][] m = roleMySqlDao.getRoleIds();
        assertEquals(1, m.length);
    }
    
    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.getRoleDesc(String)'
     */
    /*
    public void testGetRoleDesc() throws Exception {
        ResponsibleRoleDTO  ht = roleMySqlDao.getRoleDesc(role_id, role_name);
        assertEquals(role_email, ht.getEmail());
    }
    */

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.backUpRoles()'
     */
    public void testBackUpRoles() throws Exception {
        roleMySqlDao.backUpRoles();
        statement = getConnection().getConnection().createStatement();
        resultSet = statement.executeQuery("select count(ROLE_ID) from T_ROLE where STATUS=0");
        resultSet.next();
        assertEquals(1, resultSet.getInt(1));
    }

    /*
     * Test method for 'eionet.rod.services.modules.db.dao.mysql.RoleMySqlDao.commitRoles()'
     */
    public void testCommitRoles() throws Exception {
        roleMySqlDao.commitRoles();
        statement = getConnection().getConnection().createStatement();
        resultSet = statement.executeQuery("select count(ROLE_ID) from T_ROLE where STATUS=0");
        resultSet.next();
        assertEquals(0, resultSet.getInt(1));   
    }

}
