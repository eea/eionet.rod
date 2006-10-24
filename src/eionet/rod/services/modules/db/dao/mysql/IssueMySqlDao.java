package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import eionet.rod.services.FileServiceIF;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IIssueDao;

public class IssueMySqlDao extends MySqlBaseDao implements IIssueDao {

	public IssueMySqlDao() {
	}

	private static final String qIssues = 
		"SELECT ISSUE_NAME AS name, PK_ISSUE_ID AS id, {fn concat(?,PK_ISSUE_ID) }  AS uri " + 
		"FROM T_ISSUE " + 
		"ORDER BY ISSUE_NAME ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IIssueDao#getIssues()
	 */
	public Vector getIssues() throws ServiceException {

		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(qIssues);
			preparedStatement.setString(1, (String) properties.get(FileServiceIF.ISSUE_NAMESPACE));
			if (isDebugMode) logQuery(qIssues);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(resultSet, preparedStatement, connection);
		}

		return result != null ? result : new Vector();

	}

	private final static String qObligationIssues = 
		"SELECT i.ISSUE_NAME AS name " +
		"FROM T_ISSUE i, T_RAISSUE_LNK r " + 
		"WHERE  r.FK_RA_ID = ? AND i.PK_ISSUE_ID = r.FK_ISSUE_ID " + 
		"ORDER BY i.ISSUE_NAME";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IIssueDao#getObligationIssues(java.lang.Integer)
	 */
	public Vector getObligationIssues(Integer id) throws ServiceException {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(qObligationIssues);
			preparedStatement.setInt(1, id.intValue());
			if (isDebugMode) logQuery(qObligationIssues);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(resultSet, preparedStatement, connection);
		}

		return result != null ? result : new Vector();
	}

	private static final String qIssueIdPairs = 
		"SELECT PK_ISSUE_ID, REPLACE(ISSUE_NAME, '&', '&#038;') AS ISSUE_NAME " + 
		"FROM T_ISSUE " + 
		"ORDER BY PK_ISSUE_ID";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IIssueDao#getIssueIdPairs()
	 */
	public String[][] getIssueIdPairs() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String[][] result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(qIssueIdPairs);
			if (isDebugMode) logQuery(qIssueIdPairs);
			result = _executeStringQuery(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new String[][] {};
	}

	private static final String gIssuesByRaId = 
		"SELECT FK_ISSUE_ID " + 
		"FROM T_OBLIGATION o, T_RAISSUE_LNK il " + 
		"WHERE il.FK_RA_ID=o.PK_RA_ID AND il.FK_RA_ID=? " + 
		"ORDER BY FK_ISSUE_ID";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IIssueDao#getIssues(java.lang.Integer)
	 */
	public String[][] getIssues(Integer raId) throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String[][] result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(gIssuesByRaId);
			preparedStatement.setInt(1, raId.intValue());
			if (isDebugMode) logQuery(gIssuesByRaId);
			result = _executeStringQuery(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new String[][] {};
	}

}
