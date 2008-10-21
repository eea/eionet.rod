package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import com.tee.util.Util;
import com.tee.xmlserver.FieldInfo;

import eionet.rod.RODUtil;
import eionet.rod.dto.CountryDeliveryDTO;
import eionet.rod.dto.CountryDeliveryDataDTO;
import eionet.rod.dto.LookupDTO;
import eionet.rod.dto.ObligationFactsheetDTO;
import eionet.rod.dto.SearchDTO;
import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.SiblingObligationDTO;
import eionet.rod.dto.readers.CountryDeliveryDTOReader;
import eionet.rod.dto.readers.LookupDTOReader;
import eionet.rod.dto.readers.SearchDTOReader;
import eionet.rod.dto.readers.CountryDTOReader;
import eionet.rod.dto.readers.SiblingObligationDTOReader;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IObligationDao;
import eionet.rod.util.sql.SQLUtil;

public class ObligationMySqlDao extends MySqlBaseDao implements IObligationDao {

	public ObligationMySqlDao() {
	}

	private final static String qDeadlines = 
		"SELECT PK_RA_ID, FIRST_REPORTING, REPORT_FREQ_MONTHS, VALID_TO, TERMINATE " + 
		"FROM T_OBLIGATION " + 
		"WHERE FIRST_REPORTING > 0 " + 
			"AND VALID_TO > 0";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getDeadlines()
	 */
	public String[][] getDeadlines() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String[][] result = null;
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(qDeadlines);
			if (isDebugMode) logQuery(qDeadlines);
			result = _executeStringQuery(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new String[][] {};
	}

	private final static String qUpdateDeadlines = 
		"UPDATE T_OBLIGATION " + 
		"SET NEXT_DEADLINE = ?, NEXT_DEADLINE2 = NULL " + "WHERE PK_RA_ID = ?";

	private final static String qUpdateDeadlines2 = 
		"UPDATE T_OBLIGATION " + 
		"SET NEXT_DEADLINE =?, NEXT_DEADLINE2=? " + 
		"WHERE PK_RA_ID=? ";

	private final static String qInsertToHistoricDeadlines = 
		"INSERT IGNORE INTO T_HISTORIC_DEADLINES " + 
		"SET FK_RA_ID=?, DEADLINE=?";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#saveDeadline(java.lang.Integer,
	 *      java.util.Date, java.util.Date, java.util.Date)
	 */
	public void saveDeadline(Integer raId, String next, String next2, String current) throws ServiceException {
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			String query = ((next2 == null || next2.equals(""))? qUpdateDeadlines : qUpdateDeadlines2);
			connection = getConnection();
			if (isDebugMode) logQuery(query);
			preparedStatement = connection.prepareStatement(query);
			int index = 1;
			preparedStatement.setDate(index++, sqlDate(dFormat.parse(next)));
			if (next2 != null && !next2.equals("")) preparedStatement.setDate(index++, sqlDate(dFormat.parse(next2)));
			preparedStatement.setInt(index, raId.intValue());

			preparedStatement.executeUpdate();
			preparedStatement.close();
			if (isDebugMode) logQuery(qInsertToHistoricDeadlines);
			preparedStatement = connection.prepareStatement(qInsertToHistoricDeadlines);
			preparedStatement.setInt(1, raId.intValue());
			preparedStatement.setDate(2, sqlDate(dFormat.parse(current)));
			preparedStatement.executeUpdate();
		} catch (Exception exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

	}

	private final static String qTerminate = 
		"UPDATE T_OBLIGATION " + 
		"SET TERMINATE=? " + 
		"WHERE PK_RA_ID=?";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#saveTerminate(java.lang.Integer,
	 *      java.lang.String)
	 */
	public void saveTerminate(Integer raId, String terminated) throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qTerminate);
			preparedStatement = connection.prepareStatement(qTerminate);
			preparedStatement.setString(1, terminated);
			preparedStatement.setInt(2, raId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

	}

	private static final String qRaData = 
		"SELECT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') as TITLE " + 
		"FROM T_OBLIGATION a " + 
		"ORDER BY a.PK_RA_ID";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getRaData()
	 */
	public String[][] getRaData() throws ServiceException {

		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		String[][] result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(qRaData);
			logQuery(qRaData);
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

	private final static String qResponsibleRole = 
		"SELECT DISTINCT RESPONSIBLE_ROLE " + 
		"FROM T_OBLIGATION " + 
		"WHERE RESPONSIBLE_ROLE IS NOT NULL AND RESPONSIBLE_ROLE <> '' ";

	private final static String qCountryResponsibleRole = 
		"SELECT DISTINCT CONCAT(a.RESPONSIBLE_ROLE, '-' , (CASE s.SPATIAL_ISMEMBERCOUNTRY WHEN 'Y' THEN 'mc' WHEN 'N' THEN 'cc' END), '-' , LCASE(s.SPATIAL_TWOLETTER)) AS ohoo " + 
		"FROM T_OBLIGATION a, T_SPATIAL s,  T_RASPATIAL_LNK sl " + 
		"WHERE  a.RESPONSIBLE_ROLE_SUF=1 " + 
			"AND sl.FK_RA_ID=a.PK_RA_ID " + 
			"AND sl.FK_SPATIAL_ID = s.PK_SPATIAL_ID " + 
			"AND a.RESPONSIBLE_ROLE IS NOT NULL " + 
			"AND a.RESPONSIBLE_ROLE <> '' " + 
			"AND s.SPATIAL_TYPE = 'C' " + 
			"AND s.SPATIAL_TWOLETTER IS NOT NULL " + 
			"AND TRIM(s.SPATIAL_TWOLETTER) <> '' ";

	private final static String qCoordinatorRole = 
		"SELECT DISTINCT COORDINATOR_ROLE " + 
		"FROM T_OBLIGATION " + 
		"WHERE COORDINATOR_ROLE IS NOT NULL " + 
			"AND COORDINATOR_ROLE <> '' ";

	private final static String qCountryCoordinatorRole = 
		"SELECT DISTINCT CONCAT(a.COORDINATOR_ROLE, '-' , (CASE s.SPATIAL_ISMEMBERCOUNTRY WHEN 'Y' THEN 'mc' WHEN 'N' THEN 'cc' END), '-' , LCASE(s.SPATIAL_TWOLETTER)) " + 
		"FROM T_OBLIGATION a, T_SPATIAL s,  T_RASPATIAL_LNK sl  " + 
		"WHERE  a.COORDINATOR_ROLE_SUF=1 " + 
			"AND sl.FK_RA_ID=a.PK_RA_ID " + 
			"AND sl.FK_SPATIAL_ID = s.PK_SPATIAL_ID " + 
			"AND a.COORDINATOR_ROLE IS NOT NULL " + 
			"AND a.COORDINATOR_ROLE <> '' " + 
			"AND s.SPATIAL_TYPE = 'C' " + 
			"AND s.SPATIAL_TWOLETTER IS NOT NULL AND " + 
			"TRIM(s.SPATIAL_TWOLETTER) <> '' ";

	private static String[] respRolesQueries = { qResponsibleRole, qCountryResponsibleRole, qCoordinatorRole, qCountryCoordinatorRole };

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getRespRoles()
	 */
	public String[] getRespRoles() throws ServiceException {
		Set roles = new HashSet();
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			for (int i = 0; i < respRolesQueries.length; i++) {
				if (isDebugMode) logQuery(respRolesQueries[i]);
				preparedStatement = connection.prepareStatement(respRolesQueries[i]);
				resultSet = preparedStatement.executeQuery();
				String[][] result = getResults(resultSet);
				resultSet.close();
				preparedStatement.close();
				addStringsToSet(roles, result);
			}
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, null, connection);
		}
		return (String[]) roles.toArray(new String[0]);
	}

	private final static String qUpcomingDeadlines = 
		"SELECT " + 
			"o.TITLE AS title, " + 
			"o.PK_RA_ID AS id, " + 
			"o.FK_SOURCE_ID AS src_id, " + 
			"o.REPORT_FREQ_MONTHS AS freq, " + 
			"c.CLIENT_NAME AS client, " + 
			"o.NEXT_DEADLINE AS next_deadline, " + 
			"o.NEXT_DEADLINE2 AS next_deadline2, " + 
			"o.RESPONSIBLE_ROLE AS responsible_role " + 
		"FROM T_OBLIGATION o, T_CLIENT c " + 
		"WHERE CURDATE() < o.NEXT_DEADLINE " + 
			"AND (CURDATE() + INTERVAL (o.REPORT_FREQ_MONTHS * ? ) DAY) > o.NEXT_DEADLINE " + 
			"AND c.PK_CLIENT_ID = o.FK_CLIENT_ID ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getUpcomingDeadlines(double)
	 */
	public Vector getUpcomingDeadlines(double days) throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;

		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qUpcomingDeadlines);
			preparedStatement = connection.prepareStatement(qUpcomingDeadlines);
			preparedStatement.setDouble(1, days);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new Vector();

	}

	private final static String qActivities = 
		"SELECT " + 
			"a.PK_RA_ID, " + 
			"s.PK_SOURCE_ID, " + 
			"a.TITLE, " + 
			"IF( s.ALIAS IS NULL OR TRIM(s.ALIAS) = '', s.TITLE, s.ALIAS) AS SOURCE_TITLE, " + 
			"a.LAST_UPDATE, " + 
			"CONCAT('" + rodDomain + "/show.jsv?id=', PK_RA_ID, '&mode=A') AS details_url, " + 
			"CONCAT('" + roNs + "', '/',  a.PK_RA_ID) AS uri, " + 
			"IF (TERMINATE='Y', 1, 0) AS 'terminated' " + 
		"FROM T_OBLIGATION a , T_SOURCE s " + 
		"WHERE a.FK_SOURCE_ID = s.PK_SOURCE_ID " + 
		"ORDER BY a.PK_RA_ID";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getActivities()
	 */
	public Vector getActivities() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qActivities);
			preparedStatement = connection.prepareStatement(qActivities);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new Vector();

	}

	private final static String qObligations = 
		"SELECT " + 
			"a.PK_RA_ID, " + 
			"s.PK_SOURCE_ID, " + 
			"a.TITLE AS TITLE, " + 
			"IF( s.ALIAS IS NULL OR TRIM(s.ALIAS) = '', s.TITLE, s.ALIAS) AS SOURCE_TITLE, " + 
			"a.LAST_UPDATE, " + 
			"CONCAT('" + rodDomain + "/obligations/', PK_RA_ID) AS details_url, " + 
			"CONCAT('" + roNs + "', '/',  a.PK_RA_ID) AS uri," + 
			"IF (TERMINATE='Y', 1, 0) AS 'terminated', " + 
			"a.VALID_SINCE, " + 
			"a.EEA_PRIMARY, a.RESPONSIBLE_ROLE AS RESPONSIBLE_ROLE, " + 
			"a.DESCRIPTION AS DESCRIPTION, " +
			"a.NEXT_DEADLINE, a.NEXT_DEADLINE2, " + 
	        "a.COMMENT AS COMMENT, " + 
	        "a.REPORTING_FORMAT AS REPORTING_FORMAT, " +	          
	        "a.FORMAT_NAME AS FORMAT_NAME, " + 
	        "a.REPORT_FORMAT_URL AS REPORT_FORMAT_URL " + 
		"FROM T_OBLIGATION a , T_SOURCE s "+ 
		"WHERE a.FK_SOURCE_ID = s.PK_SOURCE_ID " + 
		"ORDER BY TITLE";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getObligations()
	 */
	public Vector getObligations() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qObligations);
			preparedStatement = connection.prepareStatement(qObligations);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new Vector();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getActivityDeadlines(java.util.StringTokenizer,
	 *      java.util.StringTokenizer)
	 */
	public String[][] getActivityDeadlines(StringTokenizer issues, StringTokenizer countries) throws ServiceException {

		String sql = "";

		if (issues != null && countries != null) {
			sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.REPORT_FREQ_MONTHS, a.FK_SOURCE_ID," + rplAmp("a.DESCRIPTION", "DESCRIPTION") + " FROM T_OBLIGATION a, T_RAISSUE_LNK il, T_RASPATIAL_LNK r WHERE " + "  a.PK_RA_ID = il.FK_RA_ID AND a.PK_RA_ID = r.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " + "a.NEXT_DEADLINE > '0000-00-00'";

			sql = sql + " AND " + getWhereClause("il.FK_ISSUE_ID", issues);
			sql = sql + " AND " + getWhereClause("r.FK_SPATIAL_ID", countries);
		} else if (issues != null && countries == null) {
			sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.REPORT_FREQ_MONTHS, a.FK_SOURCE_ID, " + rplAmp("a.DESCRIPTION", "DESCRIPTION") + " FROM T_OBLIGATION a, T_RAISSUE_LNK il WHERE " + "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " + "a.NEXT_DEADLINE > '0000-00-00'";

			sql = sql + " AND " + getWhereClause("il.FK_ISSUE_ID", issues);
		} else if (issues == null && countries != null) {
			sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.REPORT_FREQ_MONTHS, a.FK_SOURCE_ID, " + rplAmp("a.DESCRIPTION", "DESCRIPTION") + " FROM T_OBLIGATION a, T_RASPATIAL_LNK il WHERE " + "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " + "a.NEXT_DEADLINE > '0000-00-00'";

			sql = sql + " AND " + getWhereClause("il.FK_SPATIAL_ID", countries);
		} else {
			sql = "SELECT PK_RA_ID, REPLACE(TITLE, '&', '&#038;') AS TITLE , NEXT_DEADLINE, REPORT_FREQ_MONTHS, FK_SOURCE_ID, " + rplAmp("DESCRIPTION", "DESRCIPTION") + " FROM T_OBLIGATION WHERE NEXT_DEADLINE IS NOT NULL AND " + "NEXT_DEADLINE > '0000-00-00'";
		}

		return _executeStringQuery(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getAllActivityDeadlines(java.util.StringTokenizer,
	 *      java.util.StringTokenizer)
	 */
	public String[][] getAllActivityDeadlines(StringTokenizer issues, StringTokenizer countries) throws ServiceException {

		StringBuffer buf_sql = new StringBuffer();
		String whereClause1 = null;
		String whereClause2 = null;

		if (issues != null && countries != null) {
			whereClause1 = getWhereClause("il.FK_ISSUE_ID", issues);
			whereClause2 = getWhereClause("r.FK_SPATIAL_ID", countries);

			buf_sql.append("SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE AS DEADLINE, a.FK_SOURCE_ID,");
			buf_sql.append(rplAmp("a.DESCRIPTION", "DESCRIPTION"));
			buf_sql.append(" FROM T_OBLIGATION a, T_RAISSUE_LNK il, T_RASPATIAL_LNK r WHERE " + "  a.PK_RA_ID = il.FK_RA_ID AND a.PK_RA_ID = r.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " + "a.NEXT_DEADLINE > '0000-00-00'");
			buf_sql.append(" AND ");
			buf_sql.append(whereClause1);
			buf_sql.append(" AND ");
			buf_sql.append(whereClause2);

			// EK 010306
			// Create UNION SELECT
			buf_sql.append(" UNION SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE2 AS DEADLINE, a.FK_SOURCE_ID,");
			buf_sql.append(rplAmp("a.DESCRIPTION", "DESCRIPTION"));
			buf_sql.append(" FROM T_OBLIGATION a, T_RAISSUE_LNK il, T_RASPATIAL_LNK r WHERE " + "  a.PK_RA_ID = il.FK_RA_ID AND a.PK_RA_ID = r.FK_RA_ID AND a.NEXT_DEADLINE2 IS NOT NULL AND " + "a.NEXT_DEADLINE2 > '0000-00-00'");
			buf_sql.append(" AND ");
			buf_sql.append(whereClause1);
			buf_sql.append(" AND ");
			buf_sql.append(whereClause2);

		} else if (issues != null && countries == null) {
			whereClause1 = getWhereClause("il.FK_ISSUE_ID", issues);
			buf_sql.append("SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE AS DEADLINE, a.FK_SOURCE_ID, ");
			buf_sql.append(rplAmp("a.DESCRIPTION", "DESCRIPTION"));
			buf_sql.append(" FROM T_OBLIGATION a, T_RAISSUE_LNK il WHERE " + "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " + "a.NEXT_DEADLINE > '0000-00-00'");

			buf_sql.append(" AND ");
			buf_sql.append(whereClause1);
			// EK 010306
			// Create UNION SELECT
			buf_sql.append(" UNION SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE2 AS DEADLINE, a.FK_SOURCE_ID, ");
			buf_sql.append(rplAmp("a.DESCRIPTION", "DESCRIPTION"));
			buf_sql.append(" FROM T_OBLIGATION a, T_RAISSUE_LNK il WHERE " + "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE2 IS NOT NULL AND " + "a.NEXT_DEADLINE2 > '0000-00-00'");

			buf_sql.append(" AND ");
			buf_sql.append(whereClause1);

		} else if (issues == null && countries != null) {
			whereClause1 = getWhereClause("il.FK_SPATIAL_ID", countries);

			buf_sql.append("SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE AS DEADLINE, a.FK_SOURCE_ID, ");
			buf_sql.append(rplAmp("a.DESCRIPTION", "DESCRIPTION"));
			buf_sql.append(" FROM T_OBLIGATION a, T_RASPATIAL_LNK il WHERE " + "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " + "a.NEXT_DEADLINE > '0000-00-00'");

			buf_sql.append(" AND ");
			buf_sql.append(whereClause1);
			// EK 010306
			// Create UNION SELECT
			buf_sql.append(" UNION SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE2 AS DEADLINE, a.FK_SOURCE_ID, ");
			buf_sql.append(rplAmp("a.DESCRIPTION", "DESCRIPTION"));
			buf_sql.append(" FROM T_OBLIGATION a, T_RASPATIAL_LNK il WHERE " + "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE2 IS NOT NULL AND " + "a.NEXT_DEADLINE2 > '0000-00-00'");

			buf_sql.append(" AND ");
			buf_sql.append(whereClause1);
		} else {
			buf_sql.append("SELECT PK_RA_ID, REPLACE(TITLE, '&', '&#038;') AS TITLE , NEXT_DEADLINE AS DEADLINE, FK_SOURCE_ID, ");
			buf_sql.append(rplAmp("DESCRIPTION", "DESRCIPTION"));
			buf_sql.append(" FROM T_OBLIGATION WHERE NEXT_DEADLINE IS NOT NULL AND " + "NEXT_DEADLINE > '0000-00-00'");
			// EK 010306
			// Create UNION SELECT
			buf_sql.append(" UNION SELECT PK_RA_ID, REPLACE(TITLE, '&', '&#038;') AS TITLE , NEXT_DEADLINE2 AS DEADLINE, FK_SOURCE_ID, ");
			buf_sql.append(rplAmp("DESCRIPTION", "DESRCIPTION"));
			buf_sql.append(" FROM T_OBLIGATION WHERE NEXT_DEADLINE2 IS NOT NULL AND " + "NEXT_DEADLINE2 > '0000-00-00'");
		}
		return _executeStringQuery(buf_sql.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getIssueActivities(java.util.StringTokenizer,
	 *      java.util.StringTokenizer)
	 */
	public String[][] getIssueActivities(StringTokenizer issues, StringTokenizer countries) throws ServiceException {
		String sql = "";

		if (issues != null && countries != null) {
			sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.FK_SOURCE_ID," + rplAmp("a.DESCRIPTION", "DESCRIPTION") + " FROM T_OBLIGATION a, T_RAISSUE_LNK il, T_RASPATIAL_LNK r WHERE " + "  a.PK_RA_ID = il.FK_RA_ID AND a.PK_RA_ID = r.FK_RA_ID";

			sql = sql + " AND " + getWhereClause("il.FK_ISSUE_ID", issues);
			sql = sql + " AND " + getWhereClause("r.FK_SPATIAL_ID", countries);
		} else if (issues != null && countries == null) {
			sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.FK_SOURCE_ID, " + rplAmp("a.DESCRIPTION", "DESCRIPTION") + " FROM T_OBLIGATION a, T_RAISSUE_LNK il WHERE " + "  a.PK_RA_ID = il.FK_RA_ID";

			sql = sql + " AND " + getWhereClause("il.FK_ISSUE_ID", issues);
		} else if (issues == null && countries != null) {
			sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.FK_SOURCE_ID, " + rplAmp("a.DESCRIPTION", "DESCRIPTION") + " FROM T_OBLIGATION a, T_RASPATIAL_LNK il WHERE " + "  a.PK_RA_ID = il.FK_RA_ID";

			sql = sql + " AND " + getWhereClause("il.FK_SPATIAL_ID", countries);
		} else {
			sql = "SELECT PK_RA_ID, REPLACE(TITLE, '&', '&#038;') AS TITLE, NEXT_DEADLINE, " + " FK_SOURCE_ID, " + rplAmp("DESCRIPTION", "DESCRIPTION") + " FROM T_OBLIGATION";
		}

		sql += " ORDER BY PK_RA_ID";

		return _executeStringQuery(sql);

	}

	private final static String qObligationById = 
		"SELECT " + 
			"REPLACE(o.TITLE, '&', '&#038;') as title, " + 
			"c.CLIENT_NAME AS client, " + 
			"o.PK_RA_ID AS obligationID, " + 
			"c.PK_CLIENT_ID AS clientID " + 
		"FROM T_OBLIGATION o, T_CLIENT c " + 
		"WHERE c.PK_CLIENT_ID = o.FK_CLIENT_ID AND o.PK_RA_ID=?";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getObligationById(java.lang.Integer)
	 */
	public Vector getObligationById(Integer id) throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qObligationById);
			preparedStatement = connection.prepareStatement(qObligationById);
			preparedStatement.setInt(1, id.intValue());
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new Vector();
	}

	private final static String qObligationDetail = 
		"SELECT " + 
			"TITLE AS title, " + 
			"DESCRIPTION as description, " + 
			"NEXT_DEADLINE AS next_deadline, " + 
			"NEXT_DEADLINE2 AS next_deadline2, " + 
			"COMMENT as comment, " + 
			"DATE_COMMENTS as date_comments, " + 
			"REPORT_FREQ as report_freq, " + 
			"CONCAT('" + rodDomain + "/show.jsv?id=', " + "PK_RA_ID,'&mode=A') AS details_url " + 
		"FROM T_OBLIGATION " + 
		"WHERE PK_RA_ID=?";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getObligationDetail(java.lang.Integer)
	 */
	public Vector getObligationDetail(Integer id) throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qObligationDetail);
			preparedStatement = connection.prepareStatement(qObligationDetail);
			preparedStatement.setInt(1, id.intValue());
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new Vector();
	}

	private final static String qParentObligationId = 
		"SELECT PARENT_OBLIGATION " + 
		"FROM T_OBLIGATION " + 
		"WHERE PK_RA_ID= ?";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getParentObligationId(java.lang.Integer)
	 */
	public String[][] getParentObligationId(Integer id) throws ServiceException {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		String[][] result = null;

		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qParentObligationId);
			preparedStatement = connection.prepareStatement(qParentObligationId);
			preparedStatement.setInt(1, id.intValue());
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

	private final static String qLatestVersionId = 
		"select PK_RA_ID " + 
		"from T_OBLIGATION " + 
		"where (PARENT_OBLIGATION=? OR PK_RA_ID=?)";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getLatestVersionId(java.lang.Integer)
	 */
	public String[][] getLatestVersionId(Integer id) throws ServiceException {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		String[][] result = null;

		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qLatestVersionId);
			preparedStatement = connection.prepareStatement(qLatestVersionId);
			preparedStatement.setInt(1, id.intValue());
			preparedStatement.setInt(2, id.intValue());
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

	private final static String qRestoreObligation1 = 
		"UPDATE T_OBLIGATION " + 
		"SET VERSION=?, HAS_NEWER_VERSION=? " + 
		"WHERE PK_RA_ID=?";

	private final static String qRestoreObligation2 = 
		"UPDATE T_OBLIGATION " + 
		"SET HAS_NEWER_VERSION=? " + 
		"WHERE (PK_RA_ID=? OR PARENT_OBLIGATION=?) AND VERSION=?";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getRestoreObligation(java.lang.Integer,
	 *      java.lang.Integer, int)
	 */
	public int getRestoreObligation(Integer id, Integer pid, int latestVersion) throws ServiceException {
		//
		int success = 0;
		int newVer = latestVersion + 1;
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = getConnection();
			connection.setAutoCommit(false);

			if (isDebugMode) logQuery(qRestoreObligation1);
			preparedStatement = connection.prepareStatement(qRestoreObligation1);
			preparedStatement.setInt(1, newVer);
			preparedStatement.setInt(2, -1);
			preparedStatement.setInt(3, id.intValue());
			preparedStatement.executeUpdate();
			preparedStatement.close();

			if (pid != null) {
				if (isDebugMode) logQuery(qRestoreObligation2);
				preparedStatement = connection.prepareStatement(qRestoreObligation2);
				preparedStatement.setInt(1, 1);
				preparedStatement.setInt(2, pid.intValue());
				preparedStatement.setInt(3, pid.intValue());
				preparedStatement.setInt(4, latestVersion);
				preparedStatement.executeUpdate();
			}
			commit(connection);
			success = 1;

		} catch (SQLException exception) {
			rollback(connection);
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return success;
	}

	private final static String qObligationIds = 
		"SELECT PK_RA_ID " + 
		"FROM T_OBLIGATION " + 
		"ORDER BY PK_RA_ID";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getObligationIds()
	 */
	public String[][] getObligationIds() throws ServiceException {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		String[][] result = null;

		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qObligationIds);
			preparedStatement = connection.prepareStatement(qObligationIds);
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

	private final static String qROComplete = "SELECT * FROM T_OBLIGATION";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getROComplete()
	 */
	public Vector getROComplete() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qROComplete);
			preparedStatement = connection.prepareStatement(qROComplete);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}
		return result != null ? result : new Vector();
	}

	private final static String qROSummary = 
		"SELECT " + 
			"TITLE, " + 
			"LAST_UPDATE, " + 
			"DESCRIPTION, " + 
			"CONCAT('" + rodDomain + "/show.jsv?id=', PK_RA_ID,'&mode=A') AS details_url " + 
		"FROM T_OBLIGATION ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getROSummary()
	 */
	public Vector getROSummary() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qROSummary);
			preparedStatement = connection.prepareStatement(qROSummary);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}
		return result != null ? result : new Vector();
	}

	private final static String qRODeadlines = 
		"SELECT " + 
			"o.TITLE, " + 
			"c.CLIENT_NAME, " + 
			"IF (o.NEXT_DEADLINE IS NULL, o.NEXT_REPORTING, o.NEXT_DEADLINE) AS NEXT_DEADLINE, " + 
			"o.NEXT_DEADLINE2, " + 
			"o.DATE_COMMENTS " + 
		"FROM T_OBLIGATION o LEFT OUTER JOIN T_CLIENT c ON o.FK_CLIENT_ID=c.PK_CLIENT_ID";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#getRODeadlines()
	 */
	public Vector getRODeadlines() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qRODeadlines);
			preparedStatement = connection.prepareStatement(qRODeadlines);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}
		return result != null ? result : new Vector();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#dpsirValuesFromExcelToDB(int,
	 *      java.lang.String)
	 */
	public void dpsirValuesFromExcelToDB(int id, String value) throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			String query = "UPDATE T_OBLIGATION SET DPSIR_" + value + "=? WHERE PK_RA_ID=?";
			if (isDebugMode) logQuery(query);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, "yes");
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}
	}

	private final static String qParameters = 
		"SELECT p.PARAMETER_NAME, u.UNIT_NAME " + 
		"FROM T_PARAMETER p, T_PARAMETER_LNK pl LEFT OUTER JOIN T_UNIT u ON pl.FK_UNIT_ID=u.PK_UNIT_ID " + 
		"WHERE pl.FK_PARAMETER_ID=p.PK_PARAMETER_ID AND pl.FK_RA_ID=? " + 
		"ORDER BY PARAMETER_NAME";

	private final static String qUpdateParameters = 
		"UPDATE T_OBLIGATION " + 
		"SET PARAMETERS=? " + 
		"WHERE (PARAMETERS IS NULL OR PARAMETERS='') AND PK_RA_ID=?";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#harvestParams(java.lang.Integer)
	 */
	public void harvestParams(Integer raId) throws ServiceException {
		String p[][] = null;
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qParameters);
			preparedStatement = connection.prepareStatement(qParameters);
			preparedStatement.setInt(1, raId.intValue());
			resultSet = preparedStatement.executeQuery();
			p = getResults(resultSet);
			preparedStatement.close();
			if (p != null) {
				String prmName = "";
				String uName = ""; // unit name
				StringBuffer s = new StringBuffer();

				for (int i = 0; i < p.length; i++) {
					prmName = p[i][0];
					uName = p[i][1];
					s.append(prmName);

					if (!Util.nullString(uName)) s.append("(").append(uName).append(")");
					s.append("\n");
				}
				if (s.length() > 0) {
					if (isDebugMode) logQuery(qUpdateParameters);
					preparedStatement = connection.prepareStatement(qUpdateParameters);
					preparedStatement.setString(1, strLit(s.toString()));
					preparedStatement.setInt(2, raId.intValue());
				}
			}

		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(resultSet, preparedStatement, connection);
		}

	}
	
	private static final String q_delete_issue_link = 
		"DELETE FROM T_RAISSUE_LNK " +
		"WHERE FK_RA_ID=?";
		
	public void deleteIssueLink(Integer raId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_issue_link);
			preparedStatement = connection.prepareStatement(q_delete_issue_link);
			preparedStatement.setInt(1, raId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	}
	
	private static final String q_delete_spatial_link = "DELETE FROM T_RASPATIAL_LNK WHERE FK_RA_ID=?";

	public void deleteSpatialLink(Integer raId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_spatial_link);
			preparedStatement = connection.prepareStatement(q_delete_spatial_link);
			preparedStatement.setInt(1, raId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	}

	
	private static final String q_delete_spatial_link_ext = 
		"DELETE FROM T_RASPATIAL_LNK " +
		"WHERE FK_RA_ID=? AND FK_SPATIAL_ID=?";
	public void deleteSpatialLink(Integer raId, Integer spatialId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_spatial_link_ext);
			preparedStatement = connection.prepareStatement(q_delete_spatial_link_ext);
			preparedStatement.setInt(1, raId.intValue());
			preparedStatement.setInt(2, spatialId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	}
	
	
	private static final String q_delete_info_link ="DELETE FROM T_INFO_LNK WHERE FK_RA_ID=?";

	public void deleteInfoLink(Integer raId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_info_link);
			preparedStatement = connection.prepareStatement(q_delete_info_link);
			preparedStatement.setInt(1, raId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	}
	
	

	
	
	private static final String q_delete_obligation = "DELETE FROM T_OBLIGATION WHERE PK_RA_ID=?";
	
	public void deleteObligation(Integer raId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_obligation);
			preparedStatement = connection.prepareStatement(q_delete_obligation);
			preparedStatement.setInt(1, raId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	}

	
	private static final String q_insert_info_link =  
		"INSERT INTO T_INFO_LNK (FK_RA_ID, FK_INFO_ID) " +
		"VALUES (?,?)";

	
	public void insertInfoLink(Integer raId, String infoId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_insert_info_link);
			preparedStatement = connection.prepareStatement(q_insert_info_link);
			preparedStatement.setInt(1, raId.intValue());
			preparedStatement.setString(2, infoId);			
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	}
	

	private static final String q_obligations_by_source =
		"SELECT PK_RA_ID FROM T_OBLIGATION WHERE FK_SOURCE_ID=?";
	
	public List getObligationsBySource(Integer sourceId) throws ServiceException{
		List obligations = new ArrayList();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_obligations_by_source);
			preparedStatement = connection.prepareStatement(q_obligations_by_source);
			preparedStatement.setInt(1,sourceId.intValue());
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				obligations.add(resultSet.getString(1));
			}			
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}
		
		return obligations;
	}
	
	private static final String q_check_obligationid = 
		"SELECT PK_RA_ID AS id " + 
		"FROM T_OBLIGATION " + 
		"WHERE PK_RA_ID =?";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IObligationDao#checkObligationById(java.lang.String)
	 */
	public boolean checkObligationById(String id) throws ServiceException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Hashtable result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_check_obligationid);
			preparedStatement.setString(1, id);
			logQuery(q_check_obligationid);
			result = _getHashtable(preparedStatement);
			if(result != null && result.size() > 0){
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
	
	private String getSearchSql(String spatialId, String clientId, String issueId, String date1, String date2, String dlCase, boolean union, String order) throws ServiceException {
		
		StringBuilder q_obligations_list = new StringBuilder( 
    		"SELECT DISTINCT T_OBLIGATION.PK_RA_ID, T_OBLIGATION.TITLE, T_OBLIGATION.RESPONSIBLE_ROLE, T_OBLIGATION.NEXT_REPORTING, T_OBLIGATION.NEXT_DEADLINE, ");
		if(union)
			q_obligations_list.append("T_OBLIGATION.NEXT_DEADLINE2 AS DEADLINE, T_OBLIGATION.NEXT_DEADLINE AS DEADLINE2, ");
		else
			q_obligations_list.append("IF(T_OBLIGATION.NEXT_DEADLINE IS NULL, T_OBLIGATION.NEXT_REPORTING, T_OBLIGATION.NEXT_DEADLINE) AS DEADLINE, T_OBLIGATION.NEXT_DEADLINE2 AS DEADLINE2, ");
			
		q_obligations_list.append("T_OBLIGATION.TERMINATE, T_OBLIGATION.FK_SOURCE_ID, T_OBLIGATION.FK_CLIENT_ID AS CLIENTID, T_OBLIGATION.FK_DELIVERY_COUNTRY_IDS, " +
    		"T_OBLIGATION.FK_DELIVERY_COUNTRY_IDS REGEXP CONCAT(',',T_SPATIAL.PK_SPATIAL_ID,',') AS HAS_DELIVERY, T_ROLE.ROLE_NAME AS ROLE_DESCR, T_ROLE.ROLE_URL, T_ROLE.ROLE_MEMBERS_URL, " +
    		"T_CLIENT_LNK.FK_CLIENT_ID, T_CLIENT_LNK.FK_OBJECT_ID, T_CLIENT_LNK.TYPE, T_CLIENT_LNK.STATUS, " +
    		"T_CLIENT.PK_CLIENT_ID, T_CLIENT.CLIENT_NAME, IF(T_CLIENT.CLIENT_ACRONYM='', T_CLIENT.CLIENT_NAME, T_CLIENT.CLIENT_ACRONYM) AS CLIENT_DESCR, " +
    		"T_RASPATIAL_LNK.FK_RA_ID, T_RASPATIAL_LNK.FK_SPATIAL_ID, T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_SPATIAL.SPATIAL_TWOLETTER, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY, " +
    		"T_SOURCE.PK_SOURCE_ID, T_SOURCE.SOURCE_CODE ");
    	if (!Util.nullString(issueId) && !issueId.equals("0")) {
    		q_obligations_list.append(", T_RAISSUE_LNK.FK_RA_ID, T_RAISSUE_LNK.FK_ISSUE_ID ");
    	}
    	q_obligations_list.append("FROM ");
    	if (!Util.nullString(issueId) && !issueId.equals("0")) {
        	q_obligations_list.append("(");
        }
    	q_obligations_list.append("(T_RASPATIAL_LNK LEFT JOIN T_SPATIAL ON T_RASPATIAL_LNK.FK_SPATIAL_ID=T_SPATIAL.PK_SPATIAL_ID) " +
    		"JOIN T_OBLIGATION ON T_RASPATIAL_LNK.FK_RA_ID=T_OBLIGATION.PK_RA_ID " +
    		"LEFT JOIN T_SOURCE ON T_SOURCE.PK_SOURCE_ID = T_OBLIGATION.FK_SOURCE_ID " +
    		"LEFT JOIN T_ROLE ON CONCAT(T_OBLIGATION.RESPONSIBLE_ROLE,'-',IF(T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY='Y','mc','cc'),'-',LCASE(T_SPATIAL.SPATIAL_TWOLETTER))=T_ROLE.ROLE_ID " +
    		"LEFT JOIN T_CLIENT_LNK ON T_CLIENT_LNK.TYPE='A' AND T_CLIENT_LNK.STATUS='M' AND T_CLIENT_LNK.FK_OBJECT_ID=T_OBLIGATION.PK_RA_ID " +
    		"LEFT JOIN T_CLIENT ON T_CLIENT.PK_CLIENT_ID = T_CLIENT_LNK.FK_CLIENT_ID ");
    	if (!Util.nullString(issueId) && !issueId.equals("0")) {
        	q_obligations_list.append(") JOIN T_RAISSUE_LNK ON T_OBLIGATION.PK_RA_ID=T_RAISSUE_LNK.FK_RA_ID ");
        }
    	
    	q_obligations_list.append("WHERE TERMINATE='N' ");
    	
    	if (union){
    		q_obligations_list.append("AND NEXT_DEADLINE2 IS NOT NULL ");
    		q_obligations_list.append("AND NEXT_DEADLINE2 != NEXT_DEADLINE ");
        }
    	
    	if (!Util.nullString(spatialId))
    		q_obligations_list.append("AND PK_SPATIAL_ID=").append(Util.strLiteral(spatialId)).append(" ");
    	
    	if (!Util.nullString(clientId) && !clientId.equals("0") )
    		q_obligations_list.append("AND PK_CLIENT_ID=").append(Util.strLiteral(clientId)).append(" ");
    	
    	if (!Util.nullString(issueId) && !issueId.equals("0")) 
    		q_obligations_list.append("AND FK_ISSUE_ID=").append(Util.strLiteral(issueId)).append(" ");
    	
    	if ((date1 != null && !date1.equals("dd/mm/yyyy")) || (date2 != null && !date2.equals("dd/mm/yyyy")) || dlCase != null){
    		q_obligations_list.append(handleDeadlines(dlCase, date1, date2, union));
    	}
    	
    	if(union){
    		if(!RODUtil.isNullOrEmpty(order))
    			q_obligations_list.append("ORDER BY ").append(order);
    		else
    			q_obligations_list.append("ORDER BY TITLE");
    	}
    	
    	return q_obligations_list.toString();
	}
	
	/*
     * (non-Javadoc)
     * 
     * @see eionet.rod.dao.IObligationDao#getSearchObligationsList()
     */
    public List<SearchDTO> getSearchObligationsList(String spatialId, String clientId, String issueId, String date1, String date2, String dlCase, String order) throws ServiceException {
    	
    	String sql = getSearchSql(spatialId, clientId, issueId, date1, date2, dlCase, false, order);
    	String sql_union = getSearchSql(spatialId, clientId, issueId, date1, date2, dlCase, true, order);
    	
    	String query = sql + " UNION " + sql_union;
    	
    	List<Object> values = new ArrayList<Object>();
				
		Connection conn = null;
		SearchDTOReader rsReader = new SearchDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(query, values, rsReader, conn);
			List<SearchDTO>  list = rsReader.getResultList();
			return list;
		}
		catch (Exception e){
			logger.error(e);
			throw new ServiceException(e.getMessage());
		}
		finally{
			try{
				if (conn!=null) conn.close();
			}
			catch (SQLException e){}
		}
    }
    
    private String handleDeadlines(String dlCase, String date1, String date2, boolean union) {
    	String ret = "";
    	if ( dlCase != null ) { //selected in combo
           Calendar today = Calendar.getInstance();
           //all Deadlines
           if (dlCase.equals("0")) {
        	   date1 ="dd/mm/yyyy";
        	   date2 ="dd/mm/yyyy";
           }
           //next month
           else if (dlCase.equals("1")) {
        	   date1=getDate(today);
        	   today.add(Calendar.MONTH, 1);
        	   date2=getDate(today);
           }
           //next 3 months
           else if (dlCase.equals("2")) {
        	   date1=getDate(today);
             	today.add(Calendar.MONTH, 3);
             	date2=getDate(today);
           }
           //next 6 months
           else if (dlCase.equals("3")) {
        	   date1=getDate(today);
        	   today.add(Calendar.MONTH, 6);
        	   date2=getDate(today);
           }
           //passed
           else if (dlCase.equals("4")) {
        	   date2=getDate(today);
        	   today.add(Calendar.MONTH, -3);
        	   date1=getDate(today);
           }
        }

        if (date1.equals("dd/mm/yyyy"))
        	date1 ="00/00/0000";

        date1=cnvDate(date1);

        if (date2.equals("dd/mm/yyyy"))
        	date2="31/12/9999";

        date2=cnvDate(date2);
        if (union)
        	ret = "AND ((NEXT_DEADLINE2 >= '" + date1 + "' AND NEXT_DEADLINE2 <= '" + date2 + "')) ";
        else
        	ret = "AND ((NEXT_DEADLINE >= '" + date1 + "' AND NEXT_DEADLINE <= '" + date2 + "')) ";
        return ret;
    }

    // dd/mm/yyyy -> yyyy-mm-dd
	private String cnvDate(String date ){
		date = date.substring(6) +"-"+  date.substring(3,5) +"-"+  date.substring(0,2);
		return date;
	}
	
	//formats Calendar object date to dd/mm/yyyy
	private String getDate(Calendar cal) {
		String day = Integer.toString( cal.get( Calendar.DATE) );
	    if (day.length() == 1)
	    	day  ="0" + day;
	    String month = Integer.toString( cal.get( Calendar.MONTH) +1 );
	    if (month.length() == 1)
	    	month  ="0" + month;

	    String year = Integer.toString( cal.get( Calendar.YEAR) );

	    return day + "/" + month + "/" + year;
	}
	
	private static final String q_obligation_factsheet =
		"SELECT OB.PK_RA_ID, OB.FK_SOURCE_ID, DATE_FORMAT(OB.VALID_SINCE, '%d/%m/%Y') AS VALID_SINCE, DATE_FORMAT(OB.VALID_TO, '%d/%m/%Y') AS VALID_TO, " +
		"OB.TITLE, OB.LAST_HARVESTED, OB.TERMINATE, " +
		"OB.REPORT_FREQ_MONTHS, IF(OB.NEXT_DEADLINE, DATE_FORMAT(OB.NEXT_DEADLINE, '%d/%m/%Y'), '') AS NEXT_DEADLINE, " +
		"OB.FORMAT_NAME, OB.REPORT_FORMAT_URL, OB.RESPONSIBLE_ROLE, OB.REPORT_FORMAT_URL, OB.REPORT_FREQ, OB.REPORT_FREQ_DETAIL, " +
		"REPLACE(REPLACE(OB.REPORTING_FORMAT, '\r\n', '\n'), '\r', '\n') AS REPORTING_FORMAT, " +
		"IF(OB.FIRST_REPORTING, DATE_FORMAT(OB.FIRST_REPORTING, '%d/%m/%Y'), '') AS FIRST_REPORTING, " +
		"OB.NEXT_REPORTING, OB.DATE_COMMENTS, DATE_FORMAT(OB.LAST_UPDATE, '%d/%m/%Y') AS LAST_UPDATE, " +
		"REPLACE(REPLACE(OB.COMMENT, '\r\n', '\n'), '\r', '\n') AS COMMENT, OB.FK_DELIVERY_COUNTRY_IDS, " +
		"DATE_FORMAT(OB.RM_NEXT_UPDATE, '%d/%m/%Y') AS RM_NEXT_UPDATE, " +
		"DATE_FORMAT(OB.RM_VERIFIED, '%d/%m/%Y') AS RM_VERIFIED, " +
		"OB.RM_VERIFIED_BY, OB.LOCATION_PTR, OB.LOCATION_INFO, OB.DATA_USED_FOR, OB.DATA_USED_FOR_URL, " +
		"OB.FK_CLIENT_ID, OB.RESPONSIBLE_ROLE_SUF, OB.NATIONAL_CONTACT, OB.NATIONAL_CONTACT_URL, " +
		"REPLACE(REPLACE(OB.DESCRIPTION, '\r\n', '\n'), '\r', '\n') AS DESCRIPTION, " +
		"OB.COORDINATOR_ROLE, OB.COORDINATOR_ROLE_SUF, OB.COORDINATOR, OB.COORDINATOR_URL, OB.AUTHORITY, OB.EEA_PRIMARY, " +
		"OB.PARAMETERS, OB.OVERLAP_URL, OB.EEA_CORE, OB.FLAGGED, OB.DPSIR_D, OB.DPSIR_P, OB.DPSIR_S, OB.DPSIR_I, OB.DPSIR_R, " +
		"SO.PK_SOURCE_ID, SO.TITLE AS SOURCE_TITLE, SO.ALIAS, SO.CELEX_REF, SO.SOURCE_CODE, " +
		"RRO.ROLE_ID AS R_ROLE_ID, RRO.ROLE_NAME AS R_ROLE_NAME, RRO.ROLE_URL AS R_ROLE_URL, RRO.ROLE_MEMBERS_URL AS R_ROLE_MEMBERS_URL, " +
		"CRO.ROLE_ID AS C_ROLE_ID, CRO.ROLE_NAME AS C_ROLE_NAME, CRO.ROLE_URL AS C_ROLE_URL, CRO.ROLE_MEMBERS_URL AS C_ROLE_MEMBERS_URL, " +
		"LU.C_VALUE, LU.C_TERM, " +
		"CLK.FK_CLIENT_ID AS CLK_FK_CLIENT_ID, CLK.FK_OBJECT_ID, CLK.TYPE, CLK.STATUS, " +			
		"CL.PK_CLIENT_ID, CL.CLIENT_NAME " +
		"FROM T_OBLIGATION OB " +
		"LEFT JOIN T_SOURCE SO ON SO.PK_SOURCE_ID = OB.FK_SOURCE_ID " +
		"LEFT JOIN T_ROLE RRO ON RRO.ROLE_ID=OB.RESPONSIBLE_ROLE " +
		"LEFT JOIN T_ROLE CRO ON CRO.ROLE_ID=OB.COORDINATOR_ROLE " +
		"LEFT JOIN T_LOOKUP LU ON LU.C_VALUE=OB.LEGAL_MORAL AND LU.CATEGORY='2' " +
		"LEFT JOIN T_CLIENT_LNK CLK ON CLK.TYPE='A' AND CLK.STATUS='M' AND CLK.FK_OBJECT_ID=OB.PK_RA_ID " +
		"LEFT JOIN T_CLIENT CL ON CLK.FK_CLIENT_ID=CL.PK_CLIENT_ID " +
		"WHERE OB.PK_RA_ID=?";
	
	/*
     * (non-Javadoc)
     * 
     * @see eionet.rod.dao.IObligationDao#getObligationFactsheet()
     */
	public ObligationFactsheetDTO getObligationFactsheet(String obligationId) throws ServiceException {
		
		ObligationFactsheetDTO ret = new ObligationFactsheetDTO();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_obligation_factsheet);
			preparedStatement = connection.prepareStatement(q_obligation_factsheet);
			preparedStatement.setString(1,obligationId);
			rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				ret.setObligationId(rs.getString("PK_RA_ID"));
				ret.setFkSourceId(rs.getString("FK_SOURCE_ID"));
				ret.setValidSince(rs.getString("VALID_SINCE"));
				ret.setValidTo(rs.getString("VALID_TO"));
				ret.setTitle(rs.getString("TITLE"));
				ret.setFormatName(rs.getString("FORMAT_NAME"));
				ret.setReportFormatUrl(rs.getString("REPORT_FORMAT_URL"));
				ret.setReportFreq(rs.getString("REPORT_FREQ"));
				ret.setReportFreqDetail(rs.getString("REPORT_FREQ_DETAIL"));
				ret.setReportingFormat(rs.getString("REPORTING_FORMAT"));
				ret.setNextReporting(rs.getString("NEXT_REPORTING"));
				ret.setDateComments(rs.getString("DATE_COMMENTS"));
				ret.setTerminate(rs.getString("TERMINATE"));
				ret.setLastUpdate(rs.getString("LAST_UPDATE"));
				ret.setComment(rs.getString("COMMENT"));
				ret.setResponsibleRole(rs.getString("RESPONSIBLE_ROLE"));
				ret.setNextDeadline(rs.getString("NEXT_DEADLINE"));
				ret.setFirstReporting(rs.getString("FIRST_REPORTING"));
				ret.setReportFreqMonths(new Integer(rs.getInt("REPORT_FREQ_MONTHS")));
				ret.setFkDeliveryCountryIds(rs.getString("FK_DELIVERY_COUNTRY_IDS"));
				ret.setRmNextUpdate(rs.getString("RM_NEXT_UPDATE"));
				ret.setRmVerified(rs.getString("RM_VERIFIED"));
				ret.setRmVerifiedBy(rs.getString("RM_VERIFIED_BY"));
				ret.setLocationPtr(rs.getString("LOCATION_PTR"));
				ret.setLocationInfo(rs.getString("LOCATION_INFO"));
				ret.setDataUsedFor(rs.getString("DATA_USED_FOR"));
				ret.setDataUsedForUrl(rs.getString("DATA_USED_FOR_URL"));
				ret.setFkClientId(rs.getString("FK_CLIENT_ID"));
				ret.setDescription(rs.getString("DESCRIPTION"));
				ret.setResponsibleRoleSuf(rs.getString("RESPONSIBLE_ROLE_SUF"));
				ret.setNationalContact(rs.getString("NATIONAL_CONTACT"));
				ret.setNationalContactUrl(rs.getString("NATIONAL_CONTACT_URL"));
				ret.setCoordinatorRole(rs.getString("COORDINATOR_ROLE"));
				ret.setCoordinatorRoleSuf(rs.getString("COORDINATOR_ROLE_SUF"));
				ret.setCoordinator(rs.getString("COORDINATOR"));
				ret.setCoordinatorUrl(rs.getString("COORDINATOR_URL"));
				ret.setAuthority(rs.getString("AUTHORITY"));
				ret.setEeaPrimary(new Integer(rs.getInt("EEA_PRIMARY")));
				ret.setParameters(rs.getString("PARAMETERS"));
				ret.setOverlapUrl(rs.getString("OVERLAP_URL"));
				ret.setEeaCore(new Integer(rs.getInt("EEA_CORE")));
				ret.setFlagged(new Integer(rs.getInt("FLAGGED")));
				ret.setDpsirD(rs.getString("DPSIR_D"));
				ret.setDpsirP(rs.getString("DPSIR_P"));
				ret.setDpsirS(rs.getString("DPSIR_S"));
				ret.setDpsirI(rs.getString("DPSIR_I"));
				ret.setDpsirR(rs.getString("DPSIR_R"));
				
				ret.setSourceId(rs.getString("PK_SOURCE_ID"));
				ret.setSourceTitle(rs.getString("SOURCE_TITLE"));
				ret.setSourceAlias(rs.getString("ALIAS"));
				ret.setSourceCelexRef(rs.getString("CELEX_REF"));
				ret.setSourceCode(rs.getString("SOURCE_CODE"));
				
				ret.setRespRoleId(rs.getString("R_ROLE_ID"));
				ret.setRespRoleName(rs.getString("R_ROLE_NAME"));
				ret.setRespRoleUrl(rs.getString("R_ROLE_URL"));
				ret.setRespRoleMembersUrl(rs.getString("R_ROLE_MEMBERS_URL"));
				
				ret.setCoordRoleId(rs.getString("C_ROLE_ID"));
				ret.setCoordRoleName(rs.getString("C_ROLE_NAME"));
				ret.setCoordRoleUrl(rs.getString("C_ROLE_URL"));
				ret.setCoordRoleMembersUrl(rs.getString("C_ROLE_MEMBERS_URL"));
				
				ret.setLookupCValue(rs.getString("C_VALUE"));
				ret.setLookupCTerm(rs.getString("C_TERM"));
				
				ret.setClientLnkFKClientId(rs.getString("CLK_FK_CLIENT_ID"));
				ret.setClientLnkFKObjectId(rs.getString("FK_OBJECT_ID"));
				ret.setClientLnkType(rs.getString("TYPE"));
				ret.setClientLnkStatus(rs.getString("STATUS"));
				
				ret.setClientId(rs.getString("PK_CLIENT_ID"));
				ret.setClientName(rs.getString("CLIENT_NAME"));
				
			}	
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}
		
		return ret;
	}
	
	/*
     * (non-Javadoc)
     * 
     * @see eionet.rod.dao.IObligationDao#getLookupList()
     */
    public List<LookupDTO> getLookupList(String obligationId) throws ServiceException {
    	
    	String query = "SELECT T_LOOKUP.C_TERM, T_LOOKUP.C_VALUE " +
		"FROM T_LOOKUP, T_INFO_LNK " +
		"WHERE T_INFO_LNK.FK_RA_ID = " + obligationId + " AND T_LOOKUP.C_VALUE=T_INFO_LNK.FK_INFO_ID AND CATEGORY='I'";
    	
    	List<Object> values = new ArrayList<Object>();
				
		Connection conn = null;
		LookupDTOReader rsReader = new LookupDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(query, values, rsReader, conn);
			List<LookupDTO>  list = rsReader.getResultList();
			return list;
		}
		catch (Exception e){
			logger.error(e);
			throw new ServiceException(e.getMessage());
		}
		finally{
			try{
				if (conn!=null) conn.close();
			}
			catch (SQLException e){}
		}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see eionet.rod.dao.IObligationDao#getSiblingObligations()
     */
    public List<SiblingObligationDTO> getSiblingObligations(String obligationId) throws ServiceException {
    	
    	String query = "SELECT o2.PK_RA_ID, o2.FK_SOURCE_ID, o2.TITLE, o2.AUTHORITY " +
		"FROM T_OBLIGATION o1, T_OBLIGATION o2, T_SOURCE " +
		"WHERE T_SOURCE.PK_SOURCE_ID=o1.FK_SOURCE_ID AND o1.PK_RA_ID = "+obligationId+" AND o2.PK_RA_ID != "+obligationId+" AND o2.FK_SOURCE_ID = T_SOURCE.PK_SOURCE_ID " +
		"ORDER BY o2.TITLE";
    	
    	List<Object> values = new ArrayList<Object>();
				
		Connection conn = null;
		SiblingObligationDTOReader rsReader = new SiblingObligationDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(query, values, rsReader, conn);
			List<SiblingObligationDTO>  list = rsReader.getResultList();
			return list;
		}
		catch (Exception e){
			logger.error(e);
			throw new ServiceException(e.getMessage());
		}
		finally{
			try{
				if (conn!=null) conn.close();
			}
			catch (SQLException e){}
		}
    }

}
