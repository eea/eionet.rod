package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IRoleDao;

public class RoleMySqlDao extends MySqlBaseDao implements IRoleDao {

	public RoleMySqlDao() {
	}

	private static final String q_update_role = 
		"UPDATE T_ROLE " + 
		"SET PERSON=?, INSTITUTE =? " + 
		"WHERE ROLE_ID=? ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IRoleDao#savePerson(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void savePerson(String roleId, String fullName, String orgName) throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_update_role);
			preparedStatement.setString(1, fullName);
			preparedStatement.setString(2, orgName);
			preparedStatement.setString(3, roleId);
			if (isDebugMode) logQuery(q_update_role);
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

	}

	private static final String q_roleIds = 
		"SELECT ROLE_ID, PERSON " + 
		"FROM T_ROLE";

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
		"DELETE FROM T_ROLE " + 
		"WHERE ROLE_ID=? AND STATUS=1";

	private static final String q_insert_role = 
		"INSERT INTO T_ROLE " + 
		"SET STATUS=?, LAST_HARVESTED={fn now()}, ROLE_NAME=?, ROLE_EMAIL=?, ROLE_ID=?, ROLE_URL=?, ROLE_MEMBERS_URL=?, PERSON=?, INSTITUTE=?";

	private static final String q_update_role_rollback = 
		"UPDATE T_ROLE " + 
		"SET STATUS=1 " + 
		"WHERE STATUS=0 AND ROLE_ID=?";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IRoleDao#saveRole(java.util.Hashtable,
	 *      java.lang.String, java.lang.String)
	 */
	public void saveRole(Hashtable role, String person, String org) throws ServiceException {
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

	}

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
			preparedStatement.setInt(1, 1);
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
		"SELECT " + 
			"PERSON AS person, " + 
			"INSTITUTE AS institute, " + 
			"ROLE_URL AS role_url " + 
		"FROM T_ROLE " + 
		"WHERE ROLE_ID =? ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IRoleDao#getRoleDesc(java.lang.String)
	 */
	public Hashtable getRoleDesc(String role_id) throws ServiceException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Hashtable result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_role_description);
			preparedStatement.setString(1, role_id);
			logQuery(q_role_description);
			result = _getHashtable(preparedStatement);

		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new Hashtable();
	}

}
