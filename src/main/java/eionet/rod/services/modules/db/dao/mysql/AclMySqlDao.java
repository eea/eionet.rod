package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;

import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IAclDao;

public class AclMySqlDao extends MySqlBaseDao implements IAclDao {

    public AclMySqlDao() {
    }

    private static final String q_insert_acls = "INSERT INTO ACLS (ACL_NAME, PARENT_NAME, OWNER, DESCRIPTION) "
            + "VALUES (?,?,?,?)";

    private static final String q_max_acl_id = "SELECT MAX(ACL_ID) FROM ACLS ";

    private static final String q_insert_acl_rows = "INSERT INTO ACL_ROWS (ACL_ID, ENTRY_TYPE, TYPE, PRINCIPAL, PERMISSIONS, STATUS) "
            + "VALUES(?,?,?,?,?,?)";

    private static final String q_acl_by_name_and_type = "SELECT ACL_ID AS acl_id " + "FROM ACLS "
            + "WHERE ACL_NAME = ? AND PARENT_NAME = ?";

    public void addAcl(String aclPath, String owner, String description) throws ServiceException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int lastSlash = aclPath.lastIndexOf("/");
        String parentName = (lastSlash == 0 ? "/" : aclPath.substring(0, lastSlash));
        String aclName = aclPath.substring(lastSlash + 1);

        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            if (isDebugMode)
                logQuery(q_insert_acls);
            preparedStatement = connection.prepareStatement(q_insert_acls);
            preparedStatement.setString(1, aclName);
            preparedStatement.setString(2, parentName);
            preparedStatement.setString(3, owner);
            preparedStatement.setString(4, description);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            preparedStatement = connection.prepareStatement(q_max_acl_id);
            String ret[][] = _executeStringQuery(preparedStatement);
            preparedStatement.close();
            String acl_id = null;

            if (ret.length > 0)
                acl_id = ret[0][0];

            preparedStatement = connection.prepareStatement(q_insert_acl_rows);
            preparedStatement.setInt(1, Integer.valueOf(acl_id).intValue());
            preparedStatement.setString(2, "localgroup");
            preparedStatement.setString(3, "object");
            preparedStatement.setString(4, "rod_user");
            preparedStatement.setString(5, "v,u,d");
            preparedStatement.setInt(6, 1);
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, Integer.valueOf(acl_id).intValue());
            preparedStatement.setString(2, "localgroup");
            preparedStatement.setString(3, "object");
            preparedStatement.setString(4, "rod_admin");
            preparedStatement.setString(5, "v,u,d,c");
            preparedStatement.setInt(6, 1);
            preparedStatement.executeUpdate();

            connection.commit();
        } catch (SQLException sqle) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
            LOGGER.error(sqle.getMessage(), sqle);
            throw new ServiceException(sqle.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

    }

    public String getAclId(String acl_name, String type) throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String ret = "";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_acl_by_name_and_type);
            if (isDebugMode)
                logQuery(q_acl_by_name_and_type);
            preparedStatement.setString(1, acl_name);
            preparedStatement.setString(2, type);
            Hashtable<String, String> hash = _getHashtable(preparedStatement);
            if (hash != null && hash.size() > 0) {
                ret = (String) hash.get("acl_id");
            }
        } catch (SQLException sqle) {
            LOGGER.error(sqle.getMessage(), sqle);
            throw new ServiceException(sqle.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }
        return ret;
    }

}
