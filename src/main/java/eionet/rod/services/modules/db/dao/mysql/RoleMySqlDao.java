package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import eionet.directory.DirServiceException;
import eionet.directory.DirectoryService;
import eionet.rod.dto.ObligationDTO;
import eionet.rod.dto.ResponsibleRoleDTO;
import eionet.rod.dto.RoleOccupantDTO;
import eionet.rod.dto.readers.ObligationDTOReader;
import eionet.rod.dto.readers.RoleOccupantDTOReader;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IRoleDao;
import eionet.rod.util.sql.SQLUtil;

public class RoleMySqlDao extends MySqlBaseDao implements IRoleDao {

    public RoleMySqlDao() {
    }

    private static final String q_roleIds =
        "SELECT ROLE_ID "
        + "FROM T_ROLE";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IRoleDao#getRoleIds()
     */
    public String[][] getRoleIds() throws ServiceException {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        String[][] result = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_roleIds);
            logQuery(q_roleIds);
            resultSet = preparedStatement.executeQuery();
            result = getResults(resultSet);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(resultSet, preparedStatement, connection);
        }

        return result != null ? result : new String[][] {};

    }

    private static final String q_delete_role_data =
        "DELETE FROM T_ROLE "
        + "WHERE ROLE_ID=? AND STATUS=1";

    private static final String q_insert_role =
        "INSERT INTO T_ROLE "
        + "SET STATUS=?, LAST_HARVESTED={fn now()}, ROLE_NAME=?, ROLE_EMAIL=?, ROLE_ID=?, ROLE_URL=?, ROLE_MEMBERS_URL=?";

    private static final String q_insert_role_occupants =
        "INSERT INTO T_ROLE_OCCUPANTS "
        + "SET ROLE_ID=?, PERSON=?, INSTITUTE=?";

    private static final String q_delete_role_occupants =
        "DELETE FROM T_ROLE_OCCUPANTS "
        + "WHERE ROLE_ID=?";

    private static final String q_update_role_rollback =
        "UPDATE T_ROLE "
        + "SET STATUS=1 "
        + "WHERE STATUS=0 AND ROLE_ID=?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IRoleDao#saveRole(java.util.Hashtable)
     */
    public void saveRole(Hashtable<String, Object> role) throws ServiceException {
        // backup ->
        String roleId = (String) role.get("ID");
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_delete_role_data);
            preparedStatement.setString(1, roleId);
            if (isDebugMode) logQuery(q_delete_role_data);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            preparedStatement = connection.prepareStatement(q_delete_role_occupants);
            preparedStatement.setString(1, roleId);
            if (isDebugMode) logQuery(q_delete_role_occupants);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            String uid = "";
            String orgId = "";
            String orgName = orgId;
            String fullName = uid;
            Hashtable<String, String> person = null;
            Hashtable<String, Object> org = null;


            Vector<String> occupants = (Vector<String>) role.get("OCCUPANTS");
            if (occupants != null && occupants.size() > 0) {
                for (int i = 0; i < occupants.size(); i++) {
                    uid = (String) occupants.elementAt(i);

                    if (uid != null && uid.trim().length() > 0) {
                        try {
                            person = DirectoryService.getPerson(uid);
                        } catch (DirServiceException dire) {
                            logger.error("Error getting person " + uid + ": " + dire.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (person != null) {
                        fullName = (String) person.get("FULLNAME");
                        orgId = (String) person.get("ORG_ID");
                    }

                    if (orgId != null && orgId.trim().length() > 0) {
                        try {
                            org =  DirectoryService.getOrganisation(orgId);
                            if (org != null)
                                orgName = (String) org.get("NAME");
                        } catch (DirServiceException dire) {
                            logger.error("Error getting organisation " + orgId + ": " + dire.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    preparedStatement = connection.prepareStatement(q_insert_role_occupants);
                    preparedStatement.setString(1, roleId);
                    preparedStatement.setString(2, fullName);
                    preparedStatement.setString(3, orgName);
                    if (isDebugMode) logQuery(q_insert_role_occupants);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                }
            }

            String circaUrl = (String) role.get("URL");
            String circaMembersUrl = (String) role.get("URL_MEMBERS");
            String desc = (String) role.get("DESCRIPTION");
            String mail = (String) role.get("MAIL");

            if (roleId != null) {
                preparedStatement = connection.prepareStatement(q_insert_role);
                preparedStatement.setInt(1, 1);
                preparedStatement.setString(2, desc);
                preparedStatement.setString(3, mail);
                preparedStatement.setString(4, roleId);
                preparedStatement.setString(5, circaUrl);
                preparedStatement.setString(6, circaMembersUrl);
                if (isDebugMode) logQuery(q_insert_role);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        } catch (Exception e) {
            logger.error(e);
            try {
                preparedStatement = connection.prepareStatement(q_delete_role_data);
                preparedStatement.setString(1, roleId);
                if (isDebugMode) logQuery(q_delete_role_data);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                preparedStatement = connection.prepareStatement(q_update_role_rollback);
                preparedStatement.setString(1, roleId);
                if (isDebugMode) logQuery(q_update_role_rollback);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e1) {
                logger.error(e1);
                throw new ServiceException(e1.getMessage());
            }
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

    }

    /*public void saveRole(Hashtable role, String person, String org) throws ServiceException {
        // backup ->
        String roleId = (String) role.get("ID");
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_delete_role_data);
            preparedStatement.setString(1, roleId);
            if (isDebugMode) logQuery(q_delete_role_data);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            String circaUrl = (String) role.get("URL");
            String circaMembersUrl = (String) role.get("URL_MEMBERS");
            String desc = (String) role.get("DESCRIPTION");
            String mail = (String) role.get("MAIL");

            if (roleId != null) {
                preparedStatement = connection.prepareStatement(q_insert_role);
                preparedStatement.setInt(1, 1);
                preparedStatement.setString(2, desc);
                preparedStatement.setString(3, mail);
                preparedStatement.setString(4, roleId);
                preparedStatement.setString(5, circaUrl);
                preparedStatement.setString(6, circaMembersUrl);
                preparedStatement.setString(7, person);
                preparedStatement.setString(8, org);
                if (isDebugMode) logQuery(q_insert_role);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        } catch (Exception e) {
            logger.error(e);
            try {
                preparedStatement = connection.prepareStatement(q_delete_role_data);
                preparedStatement.setString(1, roleId);
                if (isDebugMode) logQuery(q_delete_role_data);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                preparedStatement = connection.prepareStatement(q_update_role_rollback);
                preparedStatement.setString(1, roleId);
                if (isDebugMode) logQuery(q_update_role_rollback);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e1) {
                logger.error(e1);
                throw new ServiceException(e1.getMessage());
            }
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

    }*/

    private static final String q_update_role_status = "UPDATE T_ROLE " + "SET STATUS=?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IRoleDao#backUpRoles()
     */
    public void backUpRoles() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_update_role_status);
            preparedStatement.setInt(1, 0);
            if (isDebugMode) logQuery(q_update_role_status);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

    }

    private static final String q_commit_roles = "DELETE FROM T_ROLE " + "WHERE STATUS=0";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IRoleDao#commitRoles()
     */
    public void commitRoles() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_commit_roles);
            if (isDebugMode) logQuery(q_commit_roles);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

    }

    private static final String q_role_description =
        "SELECT "
        + "ROLE_NAME, "
        + "ROLE_ID, "
        + "ROLE_EMAIL, "
        + "ROLE_URL, "
        + "ROLE_MEMBERS_URL, "
        + "LAST_HARVESTED "
        + "FROM T_ROLE "
        + "WHERE ROLE_ID =? ";

    private static final String q_role_occupants =
        "SELECT "
        + "PERSON, "
        + "INSTITUTE "
        + "FROM T_ROLE_OCCUPANTS "
        + "WHERE ROLE_ID =? ";

    private static final String q_role_obligations =
        "SELECT "
        + "PK_RA_ID, "
        + "FK_SOURCE_ID, "
        + "TITLE "
        + "FROM T_OBLIGATION "
        + "WHERE RESPONSIBLE_ROLE =? ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IRoleDao#getRoleDesc(java.lang.String)
     */
    public ResponsibleRoleDTO getRoleDesc(String role_id, String role_name) throws ServiceException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResponsibleRoleDTO ret = new ResponsibleRoleDTO();
        ResultSet rs = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_role_description);
            preparedStatement.setString(1, role_id);
            logQuery(q_role_description);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ret.setName(rs.getString("ROLE_NAME"));
                ret.setEmail(rs.getString("ROLE_EMAIL"));
                ret.setRoleUrl(rs.getString("ROLE_URL"));
                ret.setMembersUrl(rs.getString("ROLE_MEMBERS_URL"));
                ret.setLastHarvested(rs.getString("LAST_HARVESTED"));
            }

            List<Object> values = new ArrayList<Object>();
            values.add(role_id);
            RoleOccupantDTOReader rsReader = new RoleOccupantDTOReader();
            SQLUtil.executeQuery(q_role_occupants, values, rsReader, connection);
            List<RoleOccupantDTO>  occupants = rsReader.getResultList();
            if (occupants != null && occupants.size() > 0)
                ret.setOccupants(occupants);

            values = new ArrayList<Object>();
            values.add(role_name);
            ObligationDTOReader rsReader2 = new ObligationDTOReader();
            SQLUtil.executeQuery(q_role_obligations, values, rsReader2, connection);
            List<ObligationDTO>  obligations = rsReader2.getResultList();
            if (obligations != null && obligations.size() > 0)
                ret.setObligations(obligations);


        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return ret;
    }

    private static final String q_check_role =
        "SELECT ROLE_ID FROM T_ROLE "
            + "WHERE ROLE_ID =? ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IRoleDao#checkRole(java.lang.String)
     */
    public boolean checkRole(String role_id) throws ServiceException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Hashtable<String, String> result = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_check_role);
            preparedStatement.setString(1, role_id);
            logQuery(q_check_role);
            result = _getHashtable(preparedStatement);
            if (result != null && result.size() > 0) {
                return true;
            }

        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return false;
    }

}
