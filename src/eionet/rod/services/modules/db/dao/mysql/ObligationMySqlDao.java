package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import com.tee.util.Util;

import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IObligationDao;

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
		"SELECT DISTINCT CONCAT(a.RESPONSIBLE_ROLE, '-' , LCASE(s.SPATIAL_TWOLETTER)) AS ohoo " + 
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
		"SELECT DISTINCT CONCAT(a.COORDINATOR_ROLE, '-' , LCASE(s.SPATIAL_TWOLETTER)) " + 
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
				preparedStatement = connection.prepareStatement(qCountryResponsibleRole);
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
			"CONCAT('" + rodDomain + "/show.jsv?id=', PK_RA_ID, '&aid=', FK_SOURCE_ID, '&mode=A') AS details_url, " + 
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
			"REPLACE(a.TITLE, '&', '&#038;') as TITLE, " + 
			"IF( s.ALIAS IS NULL OR TRIM(s.ALIAS) = '', s.TITLE, s.ALIAS) AS SOURCE_TITLE, " + 
			"a.LAST_UPDATE, " + 
			"CONCAT('" + rodDomain + "/show.jsv?id=', PK_RA_ID, '&#038;aid=', FK_SOURCE_ID, '&#038;mode=A') AS details_url, " + 
			"CONCAT('" + roNs + "', '/',  a.PK_RA_ID) AS uri," + 
			"IF (TERMINATE='Y', 1, 0) AS 'terminated', " + 
			"a.VALID_SINCE, " + 
			"a.EEA_PRIMARY, " + "REPLACE(a.RESPONSIBLE_ROLE, '&', '&#038;') AS  RESPONSIBLE_ROLE, " + 
			"REPLACE(a.DESCRIPTION, '&', '&#038;') AS  DESCRIPTION, " + 
			"a.NEXT_DEADLINE, " + "a.NEXT_DEADLINE2, " + 
			"REPLACE(a.COMMENT, '&', '&#038;') AS  COMMENT, " + 
			"REPLACE(a.REPORTING_FORMAT, '&', '&#038;') AS  REPORTING_FORMAT, " + 
			"REPLACE(a.FORMAT_NAME, '&', '&#038;') AS  FORMAT_NAME, " + 
			"REPLACE(a.REPORT_FORMAT_URL, '&', '&#038;') AS  REPORT_FORMAT_URL " + 
		"FROM T_OBLIGATION a , T_SOURCE s "+ 
		"WHERE a.FK_SOURCE_ID = s.PK_SOURCE_ID " + 
		"ORDER BY SOURCE_TITLE, TITLE";

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
			"CONCAT('" + rodDomain + "/show.jsv?id=', " + "PK_RA_ID,'&aid=', FK_SOURCE_ID, '&mode=A') AS details_url " + 
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
			"CONCAT('" + rodDomain + "/show.jsv?id=', PK_RA_ID,'&aid=', FK_SOURCE_ID, '&mode=A') AS details_url " + 
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
}
