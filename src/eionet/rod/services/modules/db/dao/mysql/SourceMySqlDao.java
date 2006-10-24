package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.ISourceDao;

public class SourceMySqlDao extends MySqlBaseDao implements ISourceDao {

	public SourceMySqlDao() {
	}

	private static final String q_instruments = 
		"SELECT " + 
			"s.PK_SOURCE_ID, " + 
			"REPLACE(s.TITLE, '&', '&#038;') AS TITLE, " + 
			"REPLACE(s.ALIAS, '&', '&#038;') AS ALIAS, " + 
			"REPLACE(s.URL, '&', '&#038;') AS URL, " + 
			"REPLACE(s.ABSTRACT, '&', '&#038;') AS ABSTRACT, " + 
			"REPLACE(c.CLIENT_NAME, '&', '&#038;') AS ISSUED_BY, " + 
			"REPLACE(s.LEGAL_NAME, '&', '&#038;') AS LEGAL_NAME, " + 
			"REPLACE(s.CELEX_REF, '&', '&#038;') AS CELEX_REF, " + 
			"REPLACE(s.TITLE, '&', '&#038;') AS TITLE, " + 
			"REPLACE(s.TITLE, '&', '&#038;') AS TITLE, " + 
			"CONCAT('" + rodDomain + "/show.jsv?id=', PK_SOURCE_ID, '&#038;mode=S') AS details_url, " + 
			"DATE_FORMAT(s.LAST_UPDATE, '%Y-%m-%d') AS LAST_UPDATE " + 
		"FROM T_SOURCE s LEFT OUTER JOIN T_CLIENT c ON s.FK_CLIENT_ID=c.PK_CLIENT_ID " + 
		"ORDER BY s.ALIAS ";

	private static final String q_instruments_ids = "SELECT PK_SOURCE_ID FROM T_SOURCE"; 
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.ISourceDao#getInstruments()
	 */
	public Vector getInstruments() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_instruments);
			if (isDebugMode) logQuery(q_instruments);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new Vector();

	}

	private static final String q_instruments_rss = 
		"SELECT " + 
			"PK_SOURCE_ID, " + 
			"REPLACE(TITLE, '&', '&#038;') AS TITLE, " + 
			"CONCAT('" + rodDomain + "/show.jsv?id=', PK_SOURCE_ID, '&amp;mode=S') AS LINK, " + 
			"REPLACE(COMMENT, '&', '&#038;') AS COMMENT " + 
		"FROM T_SOURCE " + 
		"ORDER BY PK_SOURCE_ID ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.ISourceDao#getInstrumentsRSS()
	 */
	public String[][] getInstrumentsRSS() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String[][] result = null;
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_instruments_rss);
			if (isDebugMode) logQuery(q_instruments_rss);
			result = _executeStringQuery(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new String[][] {};

	}
	
	private static final String q_delete_child_link =
		"DELETE FROM T_SOURCE_LNK " +
		"WHERE CHILD_TYPE='S' AND FK_SOURCE_CHILD_ID=?";	
	
	public void deleteChildLink(Integer childId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_child_link);
			preparedStatement = connection.prepareStatement(q_delete_child_link);
			preparedStatement.setInt(1, childId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	}

	
	private static final String q_delete_parent_link =
		"DELETE FROM T_SOURCE_LNK " +
		"WHERE PARENT_TYPE='S' AND FK_SOURCE_PARENT_ID=?";	
	
	
	public void deleteParentLink(Integer parentId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_parent_link);
			preparedStatement = connection.prepareStatement(q_delete_parent_link);
			preparedStatement.setInt(1, parentId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	}
	
	
	private static final String q_delete_source =
		"DELETE FROM T_SOURCE WHERE PK_SOURCE_ID=?";
	
	public void deleteSource(Integer sourceId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_source);
			preparedStatement = connection.prepareStatement(q_delete_source);
			preparedStatement.setInt(1, sourceId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
		
	}
	
	 public String[][] getInstrumentIds() throws ServiceException {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			String[][] result = null;
			try {
				connection = getConnection();
				preparedStatement = connection.prepareStatement(q_instruments_ids);
				if (isDebugMode) logQuery(q_instruments_ids);
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
