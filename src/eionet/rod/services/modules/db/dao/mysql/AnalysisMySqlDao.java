package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IAnalysisDao;

public class AnalysisMySqlDao extends MySqlBaseDao implements IAnalysisDao {
	
	private int getIntValue(String query) throws ServiceException {

		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		String[][] resultArray = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(query);
			logQuery(query);
			resultSet = preparedStatement.executeQuery();
			resultArray = getResults(resultSet);

		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(resultSet, preparedStatement, connection);
		}

		return (resultArray != null && resultArray.length > 0) ? new Integer(resultArray[0][0]).intValue() : null;
	}
	
	private String getStringValue(String query) throws ServiceException {

		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		String[][] resultArray = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(query);
			logQuery(query);
			resultSet = preparedStatement.executeQuery();
			resultArray = getResults(resultSet);

		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(resultSet, preparedStatement, connection);
		}

		return (resultArray != null && resultArray.length > 0) ? resultArray[0][0] : null;
	}
	
	private static final String getTotalRa = 
		"SELECT COUNT(PK_RA_ID) AS TOTAL_RA FROM T_OBLIGATION ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IAnalysisDao#getTotalRa()
	 */
	public int getTotalRa() throws ServiceException {
		return getIntValue(getTotalRa);
	}
	
	private static final String getLastUpdateRa = 
		"SELECT DATE_FORMAT(MAX(LAST_UPDATE), '%d/%m/%y') AS RA_UPDATE FROM T_OBLIGATION ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IAnalysisDao#getLastUpdateRa()
	 */
	public String getLastUpdateRa() throws ServiceException {
		return getStringValue(getLastUpdateRa);
	}
	
	private static final String getTotalLi = 
		"SELECT COUNT(PK_SOURCE_ID) AS TOTAL_LI FROM T_SOURCE ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IAnalysisDao#getTotalLi()
	 */
	public int getTotalLi() throws ServiceException {
		return getIntValue(getTotalLi);
	}
	
	private static final String getLastUpdateLi = 
		"SELECT DATE_FORMAT(MAX(LAST_UPDATE),  '%d/%m/%y') AS LI_UPDATE FROM T_SOURCE ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IAnalysisDao#getLastUpdateLi()
	 */
	public String getLastUpdateLi() throws ServiceException {
		return getStringValue(getLastUpdateLi);
	}
	
	private static final String getEeaCore = 
		"SELECT COUNT(PK_RA_ID) AS TOTAL_RA FROM T_OBLIGATION WHERE EEA_CORE=1 ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IAnalysisDao#getEeaCore()
	 */
	public int getEeaCore() throws ServiceException {
		return getIntValue(getEeaCore);
	}
	
	private static final String getEeaPriority = 
		"SELECT COUNT(PK_RA_ID) AS TOTAL_RA FROM T_OBLIGATION WHERE EEA_PRIMARY=1 ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IAnalysisDao#getEeaPriority()
	 */
	public int getEeaPriority() throws ServiceException {
		return getIntValue(getEeaPriority);
	}
	
	private static final String getOverlapRa = 
		"SELECT COUNT(PK_RA_ID) AS TOTAL_RA FROM T_OBLIGATION WHERE OVERLAP_URL IS NOT NULL AND OVERLAP_URL != '' ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IAnalysisDao#getOverlapRa()
	 */
	public int getOverlapRa() throws ServiceException {
		return getIntValue(getOverlapRa);
	}
	
	private static final String getFlaggedRa = 
		"SELECT COUNT(PK_RA_ID) AS TOTAL_RA FROM T_OBLIGATION WHERE FLAGGED=1 ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IAnalysisDao#getFlaggedRa()
	 */
	public int getFlaggedRa() throws ServiceException {
		return getIntValue(getFlaggedRa);
	}
}
