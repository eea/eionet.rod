package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import eionet.directory.DirServiceException;
import eionet.directory.DirectoryService;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IRoleDao;

public class RoleMySqlDao extends MySqlBaseDao implements IRoleDao {

    public RoleMySqlDao() {}

    private static final String q_roleIds = "SELECT ROLE_ID FROM T_ROLE";

    @Override
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
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
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

    private static final String q_update_role_rollback =
        "UPDATE T_ROLE "
        + "SET STATUS=1 "
        + "WHERE STATUS=0 AND ROLE_ID=?";

    @Override
    public void saveRole(Hashtable<String, Object> role) throws ServiceException {
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
                if (isDebugMode) logQuery(q_insert_role);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
            } catch (SQLException sqle) {
                LOGGER.error(sqle.getMessage(), sqle);
                throw new ServiceException(sqle.getMessage());
            }
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

    }


    private static final String q_update_role_status = "UPDATE T_ROLE " + "SET STATUS=?";

    @Override
    public void backUpRoles() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_update_role_status);
            preparedStatement.setInt(1, 0);
            if (isDebugMode) logQuery(q_update_role_status);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

    }

    private static final String q_commit_roles = "DELETE FROM T_ROLE WHERE STATUS=0";

    @Override
    public void commitRoles() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_commit_roles);
            if (isDebugMode) logQuery(q_commit_roles);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

    }

    private static final String q_check_role =
        "SELECT ROLE_ID FROM T_ROLE "
            + "WHERE ROLE_ID =? ";

    @Override
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

        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return false;
    }

}
