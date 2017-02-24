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
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import eionet.rod.RODUtil;
import eionet.rod.dto.LookupDTO;
import eionet.rod.dto.ObligationFactsheetDTO;
import eionet.rod.dto.ObligationRdfDTO;
import eionet.rod.dto.ObligationsDueDTO;
import eionet.rod.dto.ObligationsListDTO;
import eionet.rod.dto.SearchDTO;
import eionet.rod.dto.SiblingObligationDTO;
import eionet.rod.dto.UrlDTO;
import eionet.rod.dto.readers.LookupDTOReader;
import eionet.rod.dto.readers.ObligationsDueDTOReader;
import eionet.rod.dto.readers.ObligationsListDTOReader;
import eionet.rod.dto.readers.SearchDTOReader;
import eionet.rod.dto.readers.SiblingObligationDTOReader;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IObligationDao;
import eionet.rod.util.sql.SQLUtil;

/**
 * Queries for obligations in MySQL.
 */
public class ObligationMySqlDao extends MySqlBaseDao implements IObligationDao {

    public ObligationMySqlDao() {
    }

    private static final String Q_DEADLINES =
        "SELECT PK_RA_ID, FIRST_REPORTING, REPORT_FREQ_MONTHS, VALID_TO, TERMINATE "
        + "FROM T_OBLIGATION "
        + "WHERE FIRST_REPORTING > 0 "
        + "AND VALID_TO > 0";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IObligationDao#getDeadlines()
     */
    @Override
    public String[][] getDeadlines() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String[][] result = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Q_DEADLINES);
            if (isDebugMode) logQuery(Q_DEADLINES);
            result = _executeStringQuery(preparedStatement);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result != null ? result : new String[][] {};
    }

    private static final String qUpdateDeadlines =
        "UPDATE T_OBLIGATION "
        + "SET NEXT_DEADLINE = ?, NEXT_DEADLINE2 = NULL " + "WHERE PK_RA_ID = ?";

    private static final String qUpdateDeadlines2 =
        "UPDATE T_OBLIGATION "
        + "SET NEXT_DEADLINE =?, NEXT_DEADLINE2=? "
        + "WHERE PK_RA_ID=? ";

    private static final String qInsertToHistoricDeadlines =
        "INSERT IGNORE INTO T_HISTORIC_DEADLINES "
        + "SET FK_RA_ID=?, DEADLINE=?";

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
            String query = ((next2 == null || next2.equals("")) ? qUpdateDeadlines : qUpdateDeadlines2);
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

    private static final String qTerminate =
        "UPDATE T_OBLIGATION "
        + "SET TERMINATE=? "
        + "WHERE PK_RA_ID=?";

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

    private static final String qResponsibleRole =
        "SELECT DISTINCT RESPONSIBLE_ROLE "
        + "FROM T_OBLIGATION "
        + "WHERE RESPONSIBLE_ROLE IS NOT NULL AND RESPONSIBLE_ROLE <> '' ";

    private static final String qCountryResponsibleRole =
        "SELECT DISTINCT CONCAT(a.RESPONSIBLE_ROLE, '-' , "
        + "(CASE s.SPATIAL_ISMEMBERCOUNTRY WHEN 'Y' THEN 'mc' WHEN 'N' THEN 'cc' END), '-' , LCASE(s.SPATIAL_TWOLETTER)) AS ohoo "
        + "FROM T_OBLIGATION a, T_SPATIAL s,  T_RASPATIAL_LNK sl "
        + "WHERE  a.RESPONSIBLE_ROLE_SUF=1 "
        + "AND sl.FK_RA_ID=a.PK_RA_ID "
        + "AND sl.FK_SPATIAL_ID = s.PK_SPATIAL_ID "
        + "AND a.RESPONSIBLE_ROLE IS NOT NULL "
        + "AND a.RESPONSIBLE_ROLE <> '' "
        + "AND s.SPATIAL_TYPE = 'C' "
        + "AND s.SPATIAL_TWOLETTER IS NOT NULL "
        + "AND TRIM(s.SPATIAL_TWOLETTER) <> '' ";

    private static final String qCoordinatorRole =
        "SELECT DISTINCT COORDINATOR_ROLE "
        + "FROM T_OBLIGATION "
        + "WHERE COORDINATOR_ROLE IS NOT NULL "
        + "AND COORDINATOR_ROLE <> '' ";

    private static final String qCountryCoordinatorRole =
        "SELECT DISTINCT CONCAT(a.COORDINATOR_ROLE, '-' , "
        + "(CASE s.SPATIAL_ISMEMBERCOUNTRY WHEN 'Y' THEN 'mc' WHEN 'N' THEN 'cc' END), '-' , LCASE(s.SPATIAL_TWOLETTER)) "
        + "FROM T_OBLIGATION a, T_SPATIAL s,  T_RASPATIAL_LNK sl "
        + "WHERE  a.COORDINATOR_ROLE_SUF=1 "
        + "AND sl.FK_RA_ID=a.PK_RA_ID "
        + "AND sl.FK_SPATIAL_ID = s.PK_SPATIAL_ID "
        + "AND a.COORDINATOR_ROLE IS NOT NULL "
        + "AND a.COORDINATOR_ROLE <> '' "
        + "AND s.SPATIAL_TYPE = 'C' "
        + "AND s.SPATIAL_TWOLETTER IS NOT NULL AND "
        + "TRIM(s.SPATIAL_TWOLETTER) <> '' ";

    private static String[] respRolesQueries = { qResponsibleRole, qCountryResponsibleRole, qCoordinatorRole, qCountryCoordinatorRole };

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IObligationDao#getRespRoles()
     */
    public String[] getRespRoles() throws ServiceException {
        Set<String> roles = new HashSet<String>();
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
        return roles.toArray(new String[0]);
    }

    private static final String qUpcomingDeadlines =
        "SELECT "
        + "o.TITLE AS title, "
        + "o.PK_RA_ID AS id, "
        + "o.FK_SOURCE_ID AS src_id, "
        + "o.REPORT_FREQ_MONTHS AS freq, "
        + "c.CLIENT_NAME AS client, "
        + "o.NEXT_DEADLINE AS next_deadline, "
        + "o.NEXT_DEADLINE2 AS next_deadline2, "
        + "o.RESPONSIBLE_ROLE AS responsible_role "
        + "FROM T_OBLIGATION o, T_CLIENT c "
        + "WHERE CURDATE() < o.NEXT_DEADLINE "
        + "AND (CURDATE() + INTERVAL (o.REPORT_FREQ_MONTHS * ? ) DAY) > o.NEXT_DEADLINE "
        + "AND c.PK_CLIENT_ID = o.FK_CLIENT_ID ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IObligationDao#getUpcomingDeadlines(double)
     */
    public Vector<Map<String, String>> getUpcomingDeadlines(double days) throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Vector<Map<String, String>> result = null;

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

        return result != null ? result : new Vector<Map<String, String>>();

    }

    private static final String qActivities =
        "SELECT "
        + "a.PK_RA_ID, "
        + "s.PK_SOURCE_ID, "
        + "a.TITLE, "
        + "IF( s.ALIAS IS NULL OR TRIM(s.ALIAS) = '', s.TITLE, s.ALIAS) AS SOURCE_TITLE, "
        + "a.LAST_UPDATE, "
        + "CONCAT('" + rodDomain + "/obligations/', PK_RA_ID) AS details_url, "
        + "CONCAT('" + roNs + "', '/',  a.PK_RA_ID) AS uri, "
        + "IF (TERMINATE='Y', 1, 0) AS 'terminated' "
        + "FROM T_OBLIGATION a , T_SOURCE s "
        + "WHERE a.FK_SOURCE_ID = s.PK_SOURCE_ID "
        + "ORDER BY a.PK_RA_ID";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IObligationDao#getActivities()
     */
    public Vector<Map<String, String>> getActivities() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Vector<Map<String, String>> result = null;
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

        return result != null ? result : new Vector<Map<String, String>>();

    }

    private static final String Q_OBLIGATIONS =
        "SELECT "
        + "a.PK_RA_ID, "
        + "s.PK_SOURCE_ID, "
        + "a.TITLE AS TITLE, "
        + "IF( s.ALIAS IS NULL OR TRIM(s.ALIAS) = '', s.TITLE, s.ALIAS) AS SOURCE_TITLE, "
        + "CONCAT('" + rodDomain + "/obligations/', PK_RA_ID) AS details_url, "
        + "CONCAT('" + roNs + "', '/',  a.PK_RA_ID) AS uri,"
        + "IF(a.TERMINATE='Y','true','false') AS 'terminated', "
        + "a.VALID_SINCE, "
        + "a.EEA_PRIMARY, a.RESPONSIBLE_ROLE AS RESPONSIBLE_ROLE, "
        + "a.DESCRIPTION AS DESCRIPTION, "
        + "a.NEXT_DEADLINE, a.NEXT_DEADLINE2, a.LAST_UPDATE, a.RM_NEXT_UPDATE, "
        + "a.COMMENT AS COMMENT, "
        + "a.REPORTING_FORMAT AS REPORTING_FORMAT, "
        + "a.FORMAT_NAME AS FORMAT_NAME, "
        + "a.REPORT_FORMAT_URL AS REPORT_FORMAT_URL, "
        + "a.DATA_USED_FOR_URL, "
        + "a.RM_VERIFIED, a.RM_VERIFIED_BY, "
        + "a.LAST_HARVESTED, a.FK_CLIENT_ID, a.VALIDATED_BY, a.COORDINATOR_URL, "
        + "IF(a.EEA_PRIMARY=1,'true','false') AS 'isEEAPrimary', "
        + "IF(a.EEA_CORE=1,'true','false') AS 'isEEACore', "
        + "IF(a.FLAGGED=1,'true','false') AS 'isFlagged', "
        + "IF(a.DPSIR_D='yes','http://rdfdata.eionet.europa.eu/eea/dpsir/D',NULL) AS 'dpsirD', "
        + "IF(a.DPSIR_P='yes','http://rdfdata.eionet.europa.eu/eea/dpsir/P',NULL) AS 'dpsirP', "
        + "IF(a.DPSIR_S='yes','http://rdfdata.eionet.europa.eu/eea/dpsir/S',NULL) AS 'dpsirS', "
        + "IF(a.DPSIR_I='yes','http://rdfdata.eionet.europa.eu/eea/dpsir/I',NULL) AS 'dpsirI', "
        + "IF(a.DPSIR_R='yes','http://rdfdata.eionet.europa.eu/eea/dpsir/R',NULL) AS 'dpsirR', "
        + "clk.FK_CLIENT_ID "
        + "FROM T_OBLIGATION a , T_SOURCE s, T_CLIENT_LNK clk "
        + "WHERE a.FK_SOURCE_ID = s.PK_SOURCE_ID AND clk.TYPE='A' "
        + "AND clk.STATUS='M' AND clk.FK_OBJECT_ID=a.PK_RA_ID "
        + "ORDER BY TITLE";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.IObligationDao#getObligationsForRDF()
     */
    public List<ObligationRdfDTO> getObligationsForRDF() throws ServiceException {
        List<ObligationRdfDTO> ret = new ArrayList<ObligationRdfDTO>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            if (isDebugMode) logQuery(Q_OBLIGATIONS);
            preparedStatement = connection.prepareStatement(Q_OBLIGATIONS);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ObligationRdfDTO obligation = new ObligationRdfDTO();
                obligation.setObligationId(rs.getInt("a.PK_RA_ID"));
                obligation.setSourceId(rs.getInt("s.PK_SOURCE_ID"));
                obligation.setTitle(rs.getString("TITLE"));
                obligation.setSourceTitle(rs.getString("SOURCE_TITLE"));
                obligation.setDetailsUrl(rs.getString("details_url"));
                obligation.setUri(rs.getString("uri"));
                obligation.setTerminated(rs.getBoolean("terminated"));
                obligation.setValidSince(rs.getString("a.VALID_SINCE"));
                obligation.setEeaPrimary(rs.getBoolean("isEEAPrimary"));
                obligation.setEeaCore(rs.getBoolean("isEEACore"));
                obligation.setFlagged(rs.getBoolean("isFlagged"));
                obligation.setResponsibleRole(rs.getString("RESPONSIBLE_ROLE"));
                obligation.setDescription(rs.getString("DESCRIPTION"));
                obligation.setNextDeadline(rs.getString("a.NEXT_DEADLINE"));
                obligation.setNextDeadline2(rs.getString("a.NEXT_DEADLINE2"));
                obligation.setComment(rs.getString("COMMENT"));
                obligation.setReportingFormat(rs.getString("REPORTING_FORMAT"));
                obligation.setFormatName(rs.getString("FORMAT_NAME"));
                obligation.setReportFormatUrl(rs.getString("REPORT_FORMAT_URL"));
                obligation.setDataUsedForUrl(rs.getString("a.DATA_USED_FOR_URL"));
                obligation.setClientId(rs.getInt("clk.FK_CLIENT_ID"));
                obligation.setLastUpdate(rs.getString("a.LAST_UPDATE"));
                obligation.setNextUpdate(rs.getString("a.RM_NEXT_UPDATE"));
                obligation.setVerified(rs.getString("a.RM_VERIFIED"));
                obligation.setVerifiedBy(rs.getString("a.RM_VERIFIED_BY"));
                obligation.setLastHarvested(rs.getString("a.LAST_HARVESTED"));
                obligation.setValidatedBy(rs.getString("a.VALIDATED_BY"));
                obligation.setCoordinatorUrl(rs.getString("a.COORDINATOR_URL"));
                obligation.setRequester(rs.getString("a.FK_CLIENT_ID"));
                obligation.setDpsirD(rs.getString("dpsirD"));
                obligation.setDpsirP(rs.getString("dpsirP"));
                obligation.setDpsirS(rs.getString("dpsirS"));
                obligation.setDpsirI(rs.getString("dpsirI"));
                obligation.setDpsirR(rs.getString("dpsirR"));

                ret.add(obligation);
            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return ret;
    }

    private static final String Q_OBLIGATION_FOR_RDF =
        "SELECT "
        + "a.PK_RA_ID, "
        + "s.PK_SOURCE_ID, "
        + "a.TITLE AS TITLE, "
        + "IF( s.ALIAS IS NULL OR TRIM(s.ALIAS) = '', s.TITLE, s.ALIAS) AS SOURCE_TITLE, "
        + "CONCAT('" + rodDomain + "/obligations/', PK_RA_ID) AS details_url, "
        + "CONCAT('" + roNs + "', '/',  a.PK_RA_ID) AS uri,"
        + "IF(a.TERMINATE='Y','true','false') AS 'terminated', "
        + "a.VALID_SINCE, "
        + "a.RESPONSIBLE_ROLE AS RESPONSIBLE_ROLE, "
        + "a.DESCRIPTION AS DESCRIPTION, "
        + "a.NEXT_DEADLINE, a.NEXT_DEADLINE2, a.LAST_UPDATE, a.RM_NEXT_UPDATE,"
        + "a.COMMENT AS COMMENT, "
        + "a.REPORTING_FORMAT AS REPORTING_FORMAT, "
        + "a.FORMAT_NAME AS FORMAT_NAME, "
        + "a.REPORT_FORMAT_URL AS REPORT_FORMAT_URL, "
        + "a.DATA_USED_FOR_URL, "
        + "a.RM_VERIFIED, a.RM_VERIFIED_BY, "
        + "a.LAST_HARVESTED, a.FK_CLIENT_ID, a.VALIDATED_BY, a.COORDINATOR_URL, "
        + "IF(a.EEA_PRIMARY=1,'true','false') AS 'isEEAPrimary', "
        + "IF(a.EEA_CORE=1,'true','false') AS 'isEEACore', "
        + "IF(a.FLAGGED=1,'true','false') AS 'isFlagged', "
        + "IF(a.DPSIR_D='yes','http://rdfdata.eionet.europa.eu/eea/dpsir/D',NULL) AS 'dpsirD', "
        + "IF(a.DPSIR_P='yes','http://rdfdata.eionet.europa.eu/eea/dpsir/P',NULL) AS 'dpsirP', "
        + "IF(a.DPSIR_S='yes','http://rdfdata.eionet.europa.eu/eea/dpsir/S',NULL) AS 'dpsirS', "
        + "IF(a.DPSIR_I='yes','http://rdfdata.eionet.europa.eu/eea/dpsir/I',NULL) AS 'dpsirI', "
        + "IF(a.DPSIR_R='yes','http://rdfdata.eionet.europa.eu/eea/dpsir/R',NULL) AS 'dpsirR', "
        + "clk.FK_CLIENT_ID "
        + "FROM T_OBLIGATION a , T_SOURCE s, T_CLIENT_LNK clk "
        + "WHERE a.PK_RA_ID = ? AND a.FK_SOURCE_ID = s.PK_SOURCE_ID AND clk.TYPE='A' "
        + "AND clk.STATUS='M' AND clk.FK_OBJECT_ID=a.PK_RA_ID";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.IObligationDao#getObligationForRDF()
     */
    public ObligationRdfDTO getObligationForRDF(String obligationId) throws ServiceException {
        ObligationRdfDTO obligation = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            if (isDebugMode) logQuery(Q_OBLIGATION_FOR_RDF);
            preparedStatement = connection.prepareStatement(Q_OBLIGATION_FOR_RDF);
            preparedStatement.setString(1, obligationId);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                obligation = new ObligationRdfDTO();
                obligation.setObligationId(rs.getInt("a.PK_RA_ID"));
                obligation.setSourceId(rs.getInt("s.PK_SOURCE_ID"));
                obligation.setTitle(rs.getString("TITLE"));
                obligation.setSourceTitle(rs.getString("SOURCE_TITLE"));
                obligation.setDetailsUrl(rs.getString("details_url"));
                obligation.setUri(rs.getString("uri"));
                obligation.setTerminated(rs.getBoolean("terminated"));
                obligation.setValidSince(rs.getString("a.VALID_SINCE"));
                obligation.setEeaPrimary(rs.getBoolean("isEEAPrimary"));
                obligation.setEeaCore(rs.getBoolean("isEEACore"));
                obligation.setFlagged(rs.getBoolean("isFlagged"));
                obligation.setResponsibleRole(rs.getString("RESPONSIBLE_ROLE"));
                obligation.setDescription(rs.getString("DESCRIPTION"));
                obligation.setNextDeadline(rs.getString("a.NEXT_DEADLINE"));
                obligation.setNextDeadline2(rs.getString("a.NEXT_DEADLINE2"));
                obligation.setComment(rs.getString("COMMENT"));
                obligation.setReportingFormat(rs.getString("REPORTING_FORMAT"));
                obligation.setFormatName(rs.getString("FORMAT_NAME"));
                obligation.setReportFormatUrl(rs.getString("REPORT_FORMAT_URL"));
                obligation.setDataUsedForUrl(rs.getString("a.DATA_USED_FOR_URL"));
                obligation.setClientId(rs.getInt("clk.FK_CLIENT_ID"));
                obligation.setLastUpdate(rs.getString("a.LAST_UPDATE"));
                obligation.setNextUpdate(rs.getString("a.RM_NEXT_UPDATE"));
                obligation.setVerified(rs.getString("a.RM_VERIFIED"));
                obligation.setVerifiedBy(rs.getString("a.RM_VERIFIED_BY"));
                obligation.setLastHarvested(rs.getString("a.LAST_HARVESTED"));
                obligation.setValidatedBy(rs.getString("a.VALIDATED_BY"));
                obligation.setCoordinatorUrl(rs.getString("a.COORDINATOR_URL"));
                obligation.setRequester(rs.getString("a.FK_CLIENT_ID"));
                obligation.setDpsirD(rs.getString("dpsirD"));
                obligation.setDpsirP(rs.getString("dpsirP"));
                obligation.setDpsirS(rs.getString("dpsirS"));
                obligation.setDpsirI(rs.getString("dpsirI"));
                obligation.setDpsirR(rs.getString("dpsirR"));

            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return obligation;
    }

    private static final String qSubscribeObligations =
        "SELECT TITLE "
        + "FROM T_OBLIGATION "
        + "ORDER BY TITLE";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IObligationDao#getObligations()
     */
    public List<String> getSubscribeObligations() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<String> result = new ArrayList<String>();
        try {
            connection = getConnection();
            if (isDebugMode) logQuery(qSubscribeObligations);
            preparedStatement = connection.prepareStatement(qSubscribeObligations);
            ResultSet rs = null;
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String title = rs.getString("TITLE");
                result.add(title);
            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result;

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
            sql = "SELECT DISTINCT a.PK_RA_ID, a.TITLE, a.NEXT_DEADLINE, a.REPORT_FREQ_MONTHS, a.FK_SOURCE_ID, a.DESCRIPTION"
                + " FROM T_OBLIGATION a, T_RAISSUE_LNK il, T_RASPATIAL_LNK r WHERE "
                + "  a.PK_RA_ID = il.FK_RA_ID AND a.PK_RA_ID = r.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND "
                + "a.NEXT_DEADLINE > '0000-00-00'";

            sql = sql + " AND " + getWhereClause("il.FK_ISSUE_ID", issues);
            sql = sql + " AND " + getWhereClause("r.FK_SPATIAL_ID", countries);
        } else if (issues != null && countries == null) {
            sql = "SELECT DISTINCT a.PK_RA_ID, a.TITLE, a.NEXT_DEADLINE, a.REPORT_FREQ_MONTHS, a.FK_SOURCE_ID, a.DESCRIPTION"
                + " FROM T_OBLIGATION a, T_RAISSUE_LNK il WHERE "
                + "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND "
                + "a.NEXT_DEADLINE > '0000-00-00'";

            sql = sql + " AND " + getWhereClause("il.FK_ISSUE_ID", issues);
        } else if (issues == null && countries != null) {
            sql = "SELECT DISTINCT a.PK_RA_ID, a.TITLE, a.NEXT_DEADLINE, a.REPORT_FREQ_MONTHS, a.FK_SOURCE_ID, a.DESCRIPTION"
                + " FROM T_OBLIGATION a, T_RASPATIAL_LNK il WHERE "
                + "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND "
                + "a.NEXT_DEADLINE > '0000-00-00'";

            sql = sql + " AND " + getWhereClause("il.FK_SPATIAL_ID", countries);
        } else {
            sql = "SELECT PK_RA_ID, TITLE, NEXT_DEADLINE, REPORT_FREQ_MONTHS, FK_SOURCE_ID, DESCRIPTION"
                + " FROM T_OBLIGATION WHERE NEXT_DEADLINE IS NOT NULL AND "
                + "NEXT_DEADLINE > '0000-00-00'";
        }

        return _executeStringQuery(sql);
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IObligationDao#getAllActivityDeadlines(java.util.StringTokenizer,
     *      java.util.StringTokenizer)
     */
    @Override
    public String[][] getAllActivityDeadlines(StringTokenizer issues, StringTokenizer countries) throws ServiceException {

        StringBuffer buf_sql = new StringBuffer();
        String whereClause1 = null;
        String whereClause2 = null;

        if (issues != null && countries != null) {
            whereClause1 = getWhereClause("il.FK_ISSUE_ID", issues);
            whereClause2 = getWhereClause("r.FK_SPATIAL_ID", countries);

            buf_sql.append("SELECT DISTINCT a.PK_RA_ID, a.TITLE, a.NEXT_DEADLINE AS DEADLINE, a.FK_SOURCE_ID, a.DESCRIPTION");
            buf_sql.append(" FROM T_OBLIGATION a, T_RAISSUE_LNK il, T_RASPATIAL_LNK r WHERE "
                + "  a.PK_RA_ID = il.FK_RA_ID AND a.PK_RA_ID = r.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND "
                + "a.NEXT_DEADLINE > '0000-00-00'");
            buf_sql.append(" AND ");
            buf_sql.append(whereClause1);
            buf_sql.append(" AND ");
            buf_sql.append(whereClause2);

            // EK 010306
            // Create UNION SELECT
            buf_sql.append(" UNION SELECT DISTINCT a.PK_RA_ID, a.TITLE, a.NEXT_DEADLINE2 AS DEADLINE, a.FK_SOURCE_ID, a.DESCRIPTION");
            buf_sql.append(" FROM T_OBLIGATION a, T_RAISSUE_LNK il, T_RASPATIAL_LNK r WHERE "
                + "  a.PK_RA_ID = il.FK_RA_ID AND a.PK_RA_ID = r.FK_RA_ID AND a.NEXT_DEADLINE2 IS NOT NULL AND "
                + "a.NEXT_DEADLINE2 > '0000-00-00'");
            buf_sql.append(" AND ");
            buf_sql.append(whereClause1);
            buf_sql.append(" AND ");
            buf_sql.append(whereClause2);

        } else if (issues != null && countries == null) {
            whereClause1 = getWhereClause("il.FK_ISSUE_ID", issues);
            buf_sql.append("SELECT DISTINCT a.PK_RA_ID, a.TITLE AS TITLE, a.NEXT_DEADLINE AS DEADLINE, a.FK_SOURCE_ID, a.DESCRIPTION");
            buf_sql.append(" FROM T_OBLIGATION a, T_RAISSUE_LNK il WHERE "
                + "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND "
                + "a.NEXT_DEADLINE > '0000-00-00'");

            buf_sql.append(" AND ");
            buf_sql.append(whereClause1);
            // Create UNION SELECT
            buf_sql.append(" UNION SELECT DISTINCT a.PK_RA_ID, a.TITLE, a.NEXT_DEADLINE2 AS DEADLINE, a.FK_SOURCE_ID, a.DESCRIPTION");
            buf_sql.append(" FROM T_OBLIGATION a, T_RAISSUE_LNK il WHERE "
                + "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE2 IS NOT NULL AND "
                + "a.NEXT_DEADLINE2 > '0000-00-00'");

            buf_sql.append(" AND ");
            buf_sql.append(whereClause1);

        } else if (issues == null && countries != null) {
            whereClause1 = getWhereClause("il.FK_SPATIAL_ID", countries);

            buf_sql.append("SELECT DISTINCT a.PK_RA_ID, a.TITLE, a.NEXT_DEADLINE AS DEADLINE, a.FK_SOURCE_ID, a.DESCRIPTION");
            buf_sql.append(" FROM T_OBLIGATION a, T_RASPATIAL_LNK il WHERE "
                + "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND "
                + "a.NEXT_DEADLINE > '0000-00-00'");

            buf_sql.append(" AND ");
            buf_sql.append(whereClause1);
            // Create UNION SELECT
            buf_sql.append(" UNION SELECT DISTINCT a.PK_RA_ID, a.TITLE, a.NEXT_DEADLINE2 AS DEADLINE, a.FK_SOURCE_ID, a.DESCRIPTION");
            buf_sql.append(" FROM T_OBLIGATION a, T_RASPATIAL_LNK il WHERE "
                + "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE2 IS NOT NULL AND "
                + "a.NEXT_DEADLINE2 > '0000-00-00'");

            buf_sql.append(" AND ");
            buf_sql.append(whereClause1);
        } else {
            buf_sql.append("SELECT PK_RA_ID, TITLE, NEXT_DEADLINE AS DEADLINE, FK_SOURCE_ID, DESCRIPTION");
            buf_sql.append(" FROM T_OBLIGATION WHERE NEXT_DEADLINE IS NOT NULL AND "
                + "NEXT_DEADLINE > '0000-00-00'");
            // Create UNION SELECT
            buf_sql.append(" UNION SELECT PK_RA_ID, TITLE, NEXT_DEADLINE2 AS DEADLINE, FK_SOURCE_ID, DESCRIPTION");
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
    @Override
    public String[][] getIssueActivities(StringTokenizer issues, StringTokenizer countries) throws ServiceException {
        String sql = "";

        if (issues != null && countries != null) {
            sql = "SELECT DISTINCT a.PK_RA_ID, a.TITLE, a.NEXT_DEADLINE, a.FK_SOURCE_ID, a.DESCRIPTION"
                + " FROM T_OBLIGATION a, T_RAISSUE_LNK il, T_RASPATIAL_LNK r WHERE "
                + " a.PK_RA_ID = il.FK_RA_ID AND a.PK_RA_ID = r.FK_RA_ID";

            sql = sql + " AND " + getWhereClause("il.FK_ISSUE_ID", issues);
            sql = sql + " AND " + getWhereClause("r.FK_SPATIAL_ID", countries);
        } else if (issues != null && countries == null) {
            sql = "SELECT DISTINCT a.PK_RA_ID, a.TITLE, a.NEXT_DEADLINE, a.FK_SOURCE_ID, a.DESCRIPTION"
                + " FROM T_OBLIGATION a, T_RAISSUE_LNK il WHERE " + "  a.PK_RA_ID = il.FK_RA_ID";

            sql = sql + " AND " + getWhereClause("il.FK_ISSUE_ID", issues);
        } else if (issues == null && countries != null) {
            sql = "SELECT DISTINCT a.PK_RA_ID, a.TITLE, a.NEXT_DEADLINE, a.FK_SOURCE_ID, a.DESCRIPTION"
                + " FROM T_OBLIGATION a, T_RASPATIAL_LNK il WHERE " + "  a.PK_RA_ID = il.FK_RA_ID";

            sql = sql + " AND " + getWhereClause("il.FK_SPATIAL_ID", countries);
        } else {
            sql = "SELECT PK_RA_ID, TITLE, NEXT_DEADLINE, FK_SOURCE_ID, DESCRIPTION FROM T_OBLIGATION";
        }

        sql += " ORDER BY PK_RA_ID";

        return _executeStringQuery(sql);

    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IObligationDao#getObligationById(java.lang.Integer)
     */
    @Override
    public Hashtable<String, String> getObligationById(Integer id) throws ServiceException {
        String sql = "SELECT o.TITLE as title, c.CLIENT_NAME AS client FROM T_OBLIGATION o, T_CLIENT c " + 
                "WHERE c.PK_CLIENT_ID = o.FK_CLIENT_ID AND o.PK_RA_ID=?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Hashtable<String, String> result = null;
        try {
            connection = getConnection();
            if (isDebugMode) {
                logQuery(sql);
            }
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            result = _getHashtable(preparedStatement);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result != null ? result : new Hashtable<String, String>();
    }

    private static final String qObligationDetail =
        "SELECT "
        + "TITLE AS title, "
        + "DESCRIPTION as description, "
        + "NEXT_DEADLINE AS next_deadline, "
        + "NEXT_DEADLINE2 AS next_deadline2, "
        + "COMMENT as comment, "
        + "DATE_COMMENTS as date_comments, "
        + "REPORT_FREQ as report_freq, "
        + "CONCAT('" + rodDomain + "/obligations/', " + "PK_RA_ID) AS details_url "
        + "FROM T_OBLIGATION "
        + "WHERE PK_RA_ID=?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IObligationDao#getObligationDetail(java.lang.Integer)
     */
    public Vector<Map<String, String>> getObligationDetail(Integer id) throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Vector<Map<String, String>> result = null;
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

        return result != null ? result : new Vector<Map<String, String>>();
    }

    private static final String qParentObligationId =
        "SELECT PARENT_OBLIGATION "
        + "FROM T_OBLIGATION "
        + "WHERE PK_RA_ID= ?";

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

    private static final String qLatestVersionId =
        "select PK_RA_ID "
        + "from T_OBLIGATION "
        + "where (PARENT_OBLIGATION=? OR PK_RA_ID=?)";

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

    private static final String qRestoreObligation1 =
        "UPDATE T_OBLIGATION "
        + "SET VERSION=?, HAS_NEWER_VERSION=? "
        + "WHERE PK_RA_ID=?";

    private static final String qRestoreObligation2 =
        "UPDATE T_OBLIGATION "
        + "SET HAS_NEWER_VERSION=? "
        + "WHERE (PK_RA_ID=? OR PARENT_OBLIGATION=?) AND VERSION=?";

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

    private static final String qObligationIds =
        "SELECT PK_RA_ID "
        + "FROM T_OBLIGATION "
        + "ORDER BY PK_RA_ID";

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

    private static final String qROComplete = "SELECT * FROM T_OBLIGATION";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IObligationDao#getROComplete()
     */
    public Vector<Map<String, String>> getROComplete() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Vector<Map<String, String>> result = null;
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
        return result != null ? result : new Vector<Map<String, String>>();
    }

    private static final String qROSummary =
        "SELECT "
        + "TITLE, "
        + "LAST_UPDATE, "
        + "DESCRIPTION, "
        + "CONCAT('" + rodDomain + "/obligations/', PK_RA_ID) AS details_url "
        + "FROM T_OBLIGATION ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IObligationDao#getROSummary()
     */
    public Vector<Map<String, String>> getROSummary() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Vector<Map<String, String>> result = null;
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
        return result != null ? result : new Vector<Map<String, String>>();
    }

    private static final String qRODeadlines =
        "SELECT "
        + "o.TITLE, "
        + "c.CLIENT_NAME, "
        + "IF (o.NEXT_DEADLINE IS NULL, o.NEXT_REPORTING, o.NEXT_DEADLINE) AS NEXT_DEADLINE, "
        + "o.NEXT_DEADLINE2, "
        + "o.DATE_COMMENTS "
        + "FROM T_OBLIGATION o LEFT OUTER JOIN T_CLIENT c ON o.FK_CLIENT_ID=c.PK_CLIENT_ID";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IObligationDao#getRODeadlines()
     */
    public Vector<Map<String, String>> getRODeadlines() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Vector<Map<String, String>> result = null;
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
        return result != null ? result : new Vector<Map<String, String>>();

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

    private static final String qParameters =
        "SELECT p.PARAMETER_NAME, u.UNIT_NAME "
        + "FROM T_PARAMETER p, T_PARAMETER_LNK pl LEFT OUTER JOIN T_UNIT u ON pl.FK_UNIT_ID=u.PK_UNIT_ID "
        + "WHERE pl.FK_PARAMETER_ID=p.PK_PARAMETER_ID AND pl.FK_RA_ID=? "
        + "ORDER BY PARAMETER_NAME";

    private static final String qUpdateParameters =
        "UPDATE T_OBLIGATION "
        + "SET PARAMETERS=? "
        + "WHERE (PARAMETERS IS NULL OR PARAMETERS='') AND PK_RA_ID=?";

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

                    if (!RODUtil.nullString(uName)) s.append("(").append(uName).append(")");
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
        "DELETE FROM T_RAISSUE_LNK "
        + "WHERE FK_RA_ID=?";

    public void deleteIssueLink(Integer raId) throws ServiceException {
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

    public void deleteSpatialLink(Integer raId) throws ServiceException {
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
        "DELETE FROM T_RASPATIAL_LNK "
        + "WHERE FK_RA_ID=? AND FK_SPATIAL_ID=?";
    public void deleteSpatialLink(Integer raId, Integer spatialId) throws ServiceException {
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

    public void deleteInfoLink(Integer raId) throws ServiceException {
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

    public void deleteObligation(Integer raId) throws ServiceException {
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
        "INSERT INTO T_INFO_LNK (FK_RA_ID, FK_INFO_ID) "
        + "VALUES (?,?)";


    public void insertInfoLink(Integer raId, String infoId) throws ServiceException {
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

    public List<String> getObligationsBySource(Integer sourceId) throws ServiceException {
        List<String> obligations = new ArrayList<String>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            if (isDebugMode) logQuery(q_obligations_by_source);
            preparedStatement = connection.prepareStatement(q_obligations_by_source);
            preparedStatement.setInt(1, sourceId.intValue());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
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

    private String getSearchSql(String spatialId, String clientId, String issueId, String date1,
                String date2, String dlCase, String order) throws ServiceException {

        StringBuilder q_obligations_list = new StringBuilder(
            "SELECT DISTINCT T_OBLIGATION.PK_RA_ID, T_OBLIGATION.TITLE, T_OBLIGATION.RESPONSIBLE_ROLE, "
            + "T_OBLIGATION.NEXT_REPORTING, T_OBLIGATION.NEXT_DEADLINE, ");
        q_obligations_list.append("IF(T_OBLIGATION.NEXT_DEADLINE IS NULL, T_OBLIGATION.NEXT_REPORTING, "
            + "T_OBLIGATION.NEXT_DEADLINE) AS DEADLINE, T_OBLIGATION.NEXT_DEADLINE2 AS DEADLINE2, ");

        q_obligations_list.append("T_OBLIGATION.TERMINATE, T_OBLIGATION.FK_SOURCE_ID, T_OBLIGATION.FK_CLIENT_ID AS CLIENTID, T_OBLIGATION.FK_DELIVERY_COUNTRY_IDS, "
        + "T_OBLIGATION.FK_DELIVERY_COUNTRY_IDS REGEXP CONCAT(',',T_SPATIAL.PK_SPATIAL_ID,',') AS HAS_DELIVERY, T_ROLE.ROLE_NAME AS ROLE_DESCR, T_ROLE.ROLE_URL, T_ROLE.ROLE_MEMBERS_URL, "
        + "T_CLIENT_LNK.FK_CLIENT_ID, T_CLIENT_LNK.FK_OBJECT_ID, T_CLIENT_LNK.TYPE, T_CLIENT_LNK.STATUS, "
        + "T_CLIENT.PK_CLIENT_ID, T_CLIENT.CLIENT_NAME, IF(T_CLIENT.CLIENT_ACRONYM='', T_CLIENT.CLIENT_NAME, T_CLIENT.CLIENT_ACRONYM) AS CLIENT_DESCR, "
        + "T_RASPATIAL_LNK.FK_RA_ID, T_RASPATIAL_LNK.FK_SPATIAL_ID, T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_SPATIAL.SPATIAL_TWOLETTER, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY, "
        + "T_SOURCE.PK_SOURCE_ID, T_SOURCE.SOURCE_CODE ");
        if (!RODUtil.nullString(issueId) && !issueId.equals("0")) {
            q_obligations_list.append(", T_RAISSUE_LNK.FK_RA_ID, T_RAISSUE_LNK.FK_ISSUE_ID ");
        }
        q_obligations_list.append("FROM ");
        if (!RODUtil.nullString(issueId) && !issueId.equals("0")) {
            q_obligations_list.append("(");
        }
        q_obligations_list.append("(T_RASPATIAL_LNK LEFT JOIN T_SPATIAL ON T_RASPATIAL_LNK.FK_SPATIAL_ID=T_SPATIAL.PK_SPATIAL_ID) "
        + "JOIN T_OBLIGATION ON T_RASPATIAL_LNK.FK_RA_ID=T_OBLIGATION.PK_RA_ID "
        + "LEFT JOIN T_SOURCE ON T_SOURCE.PK_SOURCE_ID = T_OBLIGATION.FK_SOURCE_ID "
        + "LEFT JOIN T_ROLE ON CONCAT(T_OBLIGATION.RESPONSIBLE_ROLE,'-',IF(T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY='Y','mc','cc'),'-',LCASE(T_SPATIAL.SPATIAL_TWOLETTER))=T_ROLE.ROLE_ID "
        + "LEFT JOIN T_CLIENT_LNK ON T_CLIENT_LNK.TYPE='A' AND T_CLIENT_LNK.STATUS='M' AND T_CLIENT_LNK.FK_OBJECT_ID=T_OBLIGATION.PK_RA_ID "
        + "LEFT JOIN T_CLIENT ON T_CLIENT.PK_CLIENT_ID = T_CLIENT_LNK.FK_CLIENT_ID ");
        if (!RODUtil.nullString(issueId) && !issueId.equals("0")) {
            q_obligations_list.append(") JOIN T_RAISSUE_LNK ON T_OBLIGATION.PK_RA_ID=T_RAISSUE_LNK.FK_RA_ID ");
        }

        q_obligations_list.append("WHERE TERMINATE='N' ");

        if (!RODUtil.nullString(spatialId))
            q_obligations_list.append("AND PK_SPATIAL_ID=").append(RODUtil.strLiteral(spatialId)).append(" ");

        if (!RODUtil.nullString(clientId) && !clientId.equals("0") )
            q_obligations_list.append("AND PK_CLIENT_ID=").append(RODUtil.strLiteral(clientId)).append(" ");

        if (!RODUtil.nullString(issueId) && !issueId.equals("0"))
            q_obligations_list.append("AND FK_ISSUE_ID=").append(RODUtil.strLiteral(issueId)).append(" ");

        if ((date1 != null && !date1.equals("dd/mm/yyyy")) || (date2 != null && !date2.equals("dd/mm/yyyy")) || dlCase != null) {
            q_obligations_list.append(handleDeadlines(dlCase, date1, date2));
        }

        if (!RODUtil.isNullOrEmpty(order))
            q_obligations_list.append("ORDER BY ").append(order);
        else
            q_obligations_list.append("ORDER BY TITLE");

        return q_obligations_list.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.IObligationDao#getSearchObligationsList()
     */
    public List<SearchDTO> getSearchObligationsList(String spatialId, String clientId, String issueId, String date1, String date2, String dlCase, String order) throws ServiceException {

        String sql = getSearchSql(spatialId, clientId, issueId, date1, date2, dlCase, order);

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        SearchDTOReader rsReader = new SearchDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(sql, values, rsReader, conn);
            List<SearchDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    private String handleDeadlines(String dlCase, String date1, String date2) {
        String ret = "";
        if ( dlCase != null ) { //selected in combo
            Calendar today = Calendar.getInstance();
            //next month
            if (dlCase.equals("1")) {
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

        if (dlCase == null || !dlCase.equals("0")) {
            date1=cnvDate(date1);
            date2=cnvDate(date2);
            ret = "AND ((NEXT_DEADLINE >= '" + date1 + "' AND NEXT_DEADLINE <= '" + date2 + "') OR (NEXT_DEADLINE2 >= '" + date1 + "' AND NEXT_DEADLINE2 <= '" + date2 + "')) ";
        }

        return ret;
    }

    // dd/mm/yyyy -> yyyy-mm-dd
    private String cnvDate(String date ) {
        date = date.substring(6) + "-" + date.substring(3,5) + "-" + date.substring(0,2);
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
        "SELECT OB.PK_RA_ID, OB.FK_SOURCE_ID, DATE_FORMAT(OB.VALID_SINCE, '%d/%m/%Y') AS VALID_SINCE, DATE_FORMAT(OB.VALID_TO, '%d/%m/%Y') AS VALID_TO, "
        + "OB.TITLE, OB.LAST_HARVESTED, OB.TERMINATE, "
        + "OB.REPORT_FREQ_MONTHS, IF(OB.NEXT_DEADLINE, DATE_FORMAT(OB.NEXT_DEADLINE, '%d/%m/%Y'), '') AS NEXT_DEADLINE, "
        + "IF(OB.NEXT_DEADLINE2, DATE_FORMAT(OB.NEXT_DEADLINE2, '%d/%m/%Y'), '') AS NEXT_DEADLINE2, "
        + "OB.FORMAT_NAME, OB.REPORT_FORMAT_URL, OB.RESPONSIBLE_ROLE, OB.REPORT_FORMAT_URL, OB.REPORT_FREQ, OB.REPORT_FREQ_DETAIL, "
        + "REPLACE(REPLACE(OB.REPORTING_FORMAT, '\r\n', '\n'), '\r', '\n') AS REPORTING_FORMAT, "
        + "IF(OB.FIRST_REPORTING, DATE_FORMAT(OB.FIRST_REPORTING, '%d/%m/%Y'), '') AS FIRST_REPORTING, "
        + "OB.NEXT_REPORTING, OB.DATE_COMMENTS, DATE_FORMAT(OB.LAST_UPDATE, '%d/%m/%Y') AS LAST_UPDATE, "
        + "REPLACE(REPLACE(OB.COMMENT, '\r\n', '\n'), '\r', '\n') AS COMMENT, OB.FK_DELIVERY_COUNTRY_IDS, "
        + "DATE_FORMAT(OB.RM_NEXT_UPDATE, '%d/%m/%Y') AS RM_NEXT_UPDATE, "
        + "DATE_FORMAT(OB.RM_VERIFIED, '%d/%m/%Y') AS RM_VERIFIED, "
        + "OB.RM_VERIFIED_BY, OB.LOCATION_PTR, OB.LOCATION_INFO, OB.DATA_USED_FOR, OB.DATA_USED_FOR_URL, "
        + "OB.FK_CLIENT_ID, OB.RESPONSIBLE_ROLE_SUF, OB.NATIONAL_CONTACT, OB.NATIONAL_CONTACT_URL, "
        + "REPLACE(REPLACE(OB.DESCRIPTION, '\r\n', '\n'), '\r', '\n') AS DESCRIPTION, "
        + "OB.COORDINATOR_ROLE, OB.COORDINATOR_ROLE_SUF, OB.COORDINATOR, OB.COORDINATOR_URL, OB.AUTHORITY, OB.EEA_PRIMARY, "
        + "OB.PARAMETERS, OB.VALIDATED_BY, OB.LEGAL_MORAL, OB.OVERLAP_URL, OB.EEA_CORE, OB.FLAGGED, OB.DPSIR_D, OB.DPSIR_P, OB.DPSIR_S, OB.DPSIR_I, OB.DPSIR_R, OB.CONTINOUS_REPORTING, "
        + "SO.PK_SOURCE_ID, SO.TITLE AS SOURCE_TITLE, SO.ALIAS, SO.CELEX_REF, SO.SOURCE_CODE, "
        + "RRO.ROLE_ID AS R_ROLE_ID, RRO.ROLE_NAME AS R_ROLE_NAME, RRO.ROLE_URL AS R_ROLE_URL, RRO.ROLE_MEMBERS_URL AS R_ROLE_MEMBERS_URL, "
        + "CRO.ROLE_ID AS C_ROLE_ID, CRO.ROLE_NAME AS C_ROLE_NAME, CRO.ROLE_URL AS C_ROLE_URL, CRO.ROLE_MEMBERS_URL AS C_ROLE_MEMBERS_URL, "
        + "LU.C_VALUE, LU.C_TERM, "
        + "CLK.FK_CLIENT_ID AS CLK_FK_CLIENT_ID, CLK.FK_OBJECT_ID, CLK.TYPE, CLK.STATUS, "
        + "CL.PK_CLIENT_ID, CL.CLIENT_NAME "
        + "FROM T_OBLIGATION OB "
        + "LEFT JOIN T_SOURCE SO ON SO.PK_SOURCE_ID = OB.FK_SOURCE_ID "
        + "LEFT JOIN T_ROLE RRO ON RRO.ROLE_ID=OB.RESPONSIBLE_ROLE "
        + "LEFT JOIN T_ROLE CRO ON CRO.ROLE_ID=OB.COORDINATOR_ROLE "
        + "LEFT JOIN T_LOOKUP LU ON LU.C_VALUE=OB.LEGAL_MORAL AND LU.CATEGORY='2' "
        + "LEFT JOIN T_CLIENT_LNK CLK ON CLK.TYPE='A' AND CLK.STATUS='M' AND CLK.FK_OBJECT_ID=OB.PK_RA_ID "
        + "LEFT JOIN T_CLIENT CL ON CLK.FK_CLIENT_ID=CL.PK_CLIENT_ID "
        + "WHERE OB.PK_RA_ID=?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.IObligationDao#getObligationFactsheet()
     */
    public ObligationFactsheetDTO getObligationFactsheet(String obligationId) throws ServiceException {

        ObligationFactsheetDTO ret = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            if (isDebugMode) logQuery(q_obligation_factsheet);
            preparedStatement = connection.prepareStatement(q_obligation_factsheet);
            preparedStatement.setString(1, obligationId);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {

                ret = new ObligationFactsheetDTO();
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
                ret.setNextDeadline2(rs.getString("NEXT_DEADLINE2"));
                ret.setFirstReporting(rs.getString("FIRST_REPORTING"));
                ret.setReportFreqMonths(rs.getString("REPORT_FREQ_MONTHS"));
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
                ret.setValidatedBy(rs.getString("VALIDATED_BY"));
                ret.setLegalMoral(rs.getString("LEGAL_MORAL"));
                ret.setOverlapUrl(rs.getString("OVERLAP_URL"));
                ret.setEeaCore(new Integer(rs.getInt("EEA_CORE")));
                ret.setFlagged(new Integer(rs.getInt("FLAGGED")));
                ret.setDpsirD(rs.getString("DPSIR_D"));
                ret.setDpsirP(rs.getString("DPSIR_P"));
                ret.setDpsirS(rs.getString("DPSIR_S"));
                ret.setDpsirI(rs.getString("DPSIR_I"));
                ret.setDpsirR(rs.getString("DPSIR_R"));
                ret.setContinousReporting(rs.getString("CONTINOUS_REPORTING"));

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

        String query = "SELECT T_LOOKUP.C_TERM, T_LOOKUP.C_VALUE "
        + "FROM T_LOOKUP, T_INFO_LNK "
        + "WHERE T_INFO_LNK.FK_RA_ID = " + obligationId + " AND T_LOOKUP.C_VALUE=T_INFO_LNK.FK_INFO_ID AND CATEGORY='I'";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        LookupDTOReader rsReader = new LookupDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<LookupDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.IObligationDao#getLookupListByCategory(String cat)
     */
    public List<LookupDTO> getLookupListByCategory(String cat) throws ServiceException {

        String query = "SELECT C_TERM, C_VALUE "
        + "FROM T_LOOKUP "
        + "WHERE CATEGORY='" + cat + "' ORDER BY C_TERM";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        LookupDTOReader rsReader = new LookupDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<LookupDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.IObligationDao#getSiblingObligations()
     */
    public List<SiblingObligationDTO> getSiblingObligations(String obligationId) throws ServiceException {

        String query = "SELECT o2.PK_RA_ID, o2.FK_SOURCE_ID, o2.TITLE, o2.AUTHORITY, o2.TERMINATE "
        + "FROM T_OBLIGATION o1, T_OBLIGATION o2, T_SOURCE "
        + "WHERE T_SOURCE.PK_SOURCE_ID=o1.FK_SOURCE_ID AND o1.PK_RA_ID = " + obligationId + " AND o2.PK_RA_ID != " + obligationId + " AND o2.FK_SOURCE_ID = T_SOURCE.PK_SOURCE_ID "
        + "ORDER BY o2.TITLE";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        SiblingObligationDTOReader rsReader = new SiblingObligationDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<SiblingObligationDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.IObligationDao#getObligationsList(String anmode, String country, String issue, String client, String terminated, boolean ccClients)
     */
    public List<ObligationsListDTO> getObligationsList(String anmode, String country, String issue, String client, String terminated, boolean ccClients) throws ServiceException {

        String query = getObligationsListQuery(anmode, country, issue, client, terminated, ccClients);

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        ObligationsListDTOReader rsReader = new ObligationsListDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<ObligationsListDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    public Vector<Hashtable<String, String>> getObligationsVector(String anmode, String country, String issue, String client, String terminated, boolean ccClients) throws ServiceException {

        Vector<Hashtable<String, String>> ret = null;
        String query = getObligationsListQuery(anmode, country, issue, client, terminated, ccClients);

        try {
            ret = _getVectorOfHashes(query);
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
        return ret != null ? ret : new Vector<Hashtable<String, String>>();
    }

    private String getObligationsListQuery(String anmode, String country, String issue, String client, String terminated, boolean ccClients) {

        StringBuilder query = new StringBuilder();

        query.append("SELECT DISTINCT o.PK_RA_ID, o.TITLE, o.NEXT_DEADLINE, o.NEXT_REPORTING, o.FK_DELIVERY_COUNTRY_IDS, o.TERMINATE, "
        + "s.PK_SOURCE_ID, s.TITLE AS SOURCE_TITLE, "
        + "c.PK_CLIENT_ID, c.CLIENT_NAME, IF(c.CLIENT_ACRONYM='', c.CLIENT_NAME, c.CLIENT_ACRONYM) AS CLIENT_DESCR "
        + "FROM T_OBLIGATION o, T_SOURCE s, T_CLIENT c ");
        if (!RODUtil.isNullOrEmpty(country) && !country.equals("-1"))
            query.append(", T_RASPATIAL_LNK r ");
        if (!RODUtil.isNullOrEmpty(issue) && !issue.equals("-1"))
            query.append(", T_RAISSUE_LNK i ");
        if (!RODUtil.isNullOrEmpty(client) && !client.equals("-1"))
            query.append(", T_CLIENT_LNK cl ");

        query.append("WHERE s.PK_SOURCE_ID=o.FK_SOURCE_ID AND c.PK_CLIENT_ID = o.FK_CLIENT_ID ");

        if (!RODUtil.isNullOrEmpty(country) && !country.equals("-1"))
            query.append("AND o.PK_RA_ID = r.FK_RA_ID AND r.FK_SPATIAL_ID = ").append(country).append(" ");
        if (!RODUtil.isNullOrEmpty(issue) && !issue.equals("-1") && !issue.equals("NI"))
            query.append("AND o.PK_RA_ID = i.FK_RA_ID AND i.FK_ISSUE_ID = ").append(issue).append(" ");
        else if (!RODUtil.isNullOrEmpty(issue) && issue.equals("NI"))
            query.append("AND o.PK_RA_ID NOT IN (SELECT DISTINCT FK_RA_ID FROM T_RAISSUE_LNK) ");
        if (!RODUtil.isNullOrEmpty(client) && !client.equals("-1")) {
            if (ccClients)
                query.append("AND cl.STATUS='C' ");
            else
                query.append("AND cl.STATUS='M' ");
            query.append("AND o.PK_RA_ID = cl.FK_OBJECT_ID AND cl.TYPE='A' AND cl.FK_CLIENT_ID = ").append(client).append(" ");
        }
        if (RODUtil.isNullOrEmpty(terminated) || !terminated.equals("Y"))
            query.append("AND o.TERMINATE = 'N' ");

        if (!RODUtil.isNullOrEmpty(anmode)) {
            if (anmode.equals("C"))
                query.append("AND o.EEA_CORE='1' ");
            else if (anmode.equals("P"))
                query.append("AND o.EEA_PRIMARY='1' ");
            else if (anmode.equals("O"))
                query.append("AND LENGTH(o.OVERLAP_URL)>0 ");
            else if (anmode.equals("F"))
                query.append("AND o.FLAGGED='1' ");
            else if (anmode.equals("NI"))
                query.append("AND o.PK_RA_ID NOT IN (SELECT DISTINCT FK_RA_ID FROM T_RAISSUE_LNK) ");
        }
        query.append("ORDER BY o.TITLE");

        return query.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.IObligationDao#getObligationsDue()
     */
    public List<ObligationsDueDTO> getObligationsDue() throws ServiceException {

        String query = "SELECT PK_RA_ID, TITLE, LAST_UPDATE, VALIDATED_BY, RM_NEXT_UPDATE, RM_VERIFIED, RM_VERIFIED_BY "
        + "FROM T_OBLIGATION ORDER BY RM_NEXT_UPDATE";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        ObligationsDueDTOReader rsReader = new ObligationsDueDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<ObligationsDueDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    /** */
    private static final String editObligationSQL = "UPDATE T_OBLIGATION SET "
    + "TITLE=?, DESCRIPTION=?, FIRST_REPORTING=?, VALID_TO=?, REPORT_FREQ_MONTHS=?, "
    + "NEXT_DEADLINE=?, NEXT_DEADLINE2=?, TERMINATE=?, NEXT_REPORTING=?, DATE_COMMENTS=?, "
    + "FORMAT_NAME=?, REPORT_FORMAT_URL=?, VALID_SINCE=?, REPORTING_FORMAT=?, LOCATION_INFO=?, "
    + "LOCATION_PTR=?, DATA_USED_FOR=?, DATA_USED_FOR_URL=?, COORDINATOR_ROLE=?, "
    + "COORDINATOR_ROLE_SUF=?, COORDINATOR=?, COORDINATOR_URL=?, RESPONSIBLE_ROLE=?, "
    + "RESPONSIBLE_ROLE_SUF=?, NATIONAL_CONTACT=?, NATIONAL_CONTACT_URL=?, LEGAL_MORAL=?, "
    + "PARAMETERS=?, EEA_PRIMARY=?, EEA_CORE=?, FLAGGED=?, DPSIR_D=?, DPSIR_P=?, DPSIR_S=?, "
    + "DPSIR_I=?, DPSIR_R=?, OVERLAP_URL=?, COMMENT=?, AUTHORITY=?, RM_VERIFIED=?, "
    + "RM_VERIFIED_BY=?, RM_NEXT_UPDATE=?, VALIDATED_BY=?, LAST_UPDATE=CURDATE(), FK_CLIENT_ID=?, CONTINOUS_REPORTING=? "
    + "WHERE PK_RA_ID=?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.IObligationDao#editObligation(ObligationFactsheetDTO obligation)
     */
    public void editObligation(ObligationFactsheetDTO obligation) throws ServiceException {

        List<Object> values = new ArrayList<Object>();
        values.add(obligation.getTitle());
        values.add(obligation.getDescription());
        values.add(RODUtil.str2Date(obligation.getFirstReporting()));
        values.add(RODUtil.str2Date(obligation.getValidTo()));
        values.add(obligation.getReportFreqMonths());
        values.add(RODUtil.str2Date(obligation.getNextDeadline()));
        values.add(RODUtil.str2Date(obligation.getNextDeadline2()));
        values.add(obligation.getTerminate());
        values.add(obligation.getNextReporting());
        values.add(obligation.getDateComments());
        values.add(obligation.getFormatName());
        values.add(obligation.getReportFormatUrl());
        values.add(RODUtil.str2Date(obligation.getValidSince()));
        values.add(obligation.getReportingFormat());
        values.add(obligation.getLocationInfo());
        values.add(obligation.getLocationPtr());
        values.add(obligation.getDataUsedFor());
        values.add(obligation.getDataUsedForUrl());
        values.add(obligation.getCoordinatorRole());
        values.add(obligation.getCoordinatorRoleSuf());
        values.add(obligation.getCoordinator());
        values.add(obligation.getCoordinatorUrl());
        values.add(obligation.getResponsibleRole());
        values.add(obligation.getResponsibleRoleSuf());
        values.add(obligation.getNationalContact());
        values.add(obligation.getNationalContactUrl());
        values.add(obligation.getLegalMoral());
        values.add(obligation.getParameters());
        values.add(obligation.getEeaPrimary());
        values.add(obligation.getEeaCore());
        values.add(obligation.getFlagged());
        values.add(obligation.getDpsirD());
        values.add(obligation.getDpsirP());
        values.add(obligation.getDpsirS());
        values.add(obligation.getDpsirI());
        values.add(obligation.getDpsirR());
        values.add(obligation.getOverlapUrl());
        values.add(obligation.getComment());
        values.add(obligation.getAuthority());
        values.add(RODUtil.str2Date(obligation.getRmVerified()));
        values.add(obligation.getRmVerifiedBy());
        values.add(RODUtil.str2Date(obligation.getRmNextUpdate()));
        values.add(obligation.getValidatedBy());
        values.add(obligation.getFkClientId());
        values.add(obligation.getContinousReporting());

        values.add(obligation.getObligationId());

        Connection conn = null;
        try {
            conn = getConnection();
            SQLUtil.executeUpdate(editObligationSQL, values, conn);

        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    /** */
    private static final String insertObligationSQL = "INSERT INTO T_OBLIGATION SET "
    + "TITLE=?, DESCRIPTION=?, FIRST_REPORTING=?, VALID_TO=?, REPORT_FREQ_MONTHS=?, "
    + "NEXT_DEADLINE=?, NEXT_DEADLINE2=?, TERMINATE=?, NEXT_REPORTING=?, DATE_COMMENTS=?, "
    + "FORMAT_NAME=?, REPORT_FORMAT_URL=?, VALID_SINCE=?, REPORTING_FORMAT=?, LOCATION_INFO=?, "
    + "LOCATION_PTR=?, DATA_USED_FOR=?, DATA_USED_FOR_URL=?, COORDINATOR_ROLE=?, "
    + "COORDINATOR_ROLE_SUF=?, COORDINATOR=?, COORDINATOR_URL=?, RESPONSIBLE_ROLE=?, "
    + "RESPONSIBLE_ROLE_SUF=?, NATIONAL_CONTACT=?, NATIONAL_CONTACT_URL=?, LEGAL_MORAL=?, "
    + "PARAMETERS=?, EEA_PRIMARY=?, EEA_CORE=?, FLAGGED=?, DPSIR_D=?, DPSIR_P=?, DPSIR_S=?, "
    + "DPSIR_I=?, DPSIR_R=?, OVERLAP_URL=?, COMMENT=?, AUTHORITY=?, RM_VERIFIED=?, "
    + "RM_VERIFIED_BY=?, RM_NEXT_UPDATE=?, VALIDATED_BY=?, LAST_UPDATE=CURDATE(), FK_CLIENT_ID=?, FK_SOURCE_ID=?, "
    + "CONTINOUS_REPORTING=?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.IObligationDao#insertObligation(ObligationFactsheetDTO obligation)
     */
    public Integer insertObligation(ObligationFactsheetDTO obligation) throws ServiceException {

        List<Object> values = new ArrayList<Object>();
        values.add(obligation.getTitle());
        values.add(obligation.getDescription());
        values.add(RODUtil.str2Date(obligation.getFirstReporting()));
        values.add(RODUtil.str2Date(obligation.getValidTo()));
        values.add(obligation.getReportFreqMonths());
        values.add(RODUtil.str2Date(obligation.getNextDeadline()));
        values.add(RODUtil.str2Date(obligation.getNextDeadline2()));
        values.add(obligation.getTerminate());
        values.add(obligation.getNextReporting());
        values.add(obligation.getDateComments());
        values.add(obligation.getFormatName());
        values.add(obligation.getReportFormatUrl());
        values.add(RODUtil.str2Date(obligation.getValidSince()));
        values.add(obligation.getReportingFormat());
        values.add(obligation.getLocationInfo());
        values.add(obligation.getLocationPtr());
        values.add(obligation.getDataUsedFor());
        values.add(obligation.getDataUsedForUrl());
        values.add(obligation.getCoordinatorRole());
        values.add(obligation.getCoordinatorRoleSuf());
        values.add(obligation.getCoordinator());
        values.add(obligation.getCoordinatorUrl());
        values.add(obligation.getResponsibleRole());
        values.add(obligation.getResponsibleRoleSuf());
        values.add(obligation.getNationalContact());
        values.add(obligation.getNationalContactUrl());
        values.add(obligation.getLegalMoral());
        values.add(obligation.getParameters());
        values.add(obligation.getEeaPrimary());
        values.add(obligation.getEeaCore());
        values.add(obligation.getFlagged());
        values.add(obligation.getDpsirD());
        values.add(obligation.getDpsirP());
        values.add(obligation.getDpsirS());
        values.add(obligation.getDpsirI());
        values.add(obligation.getDpsirR());
        values.add(obligation.getOverlapUrl());
        values.add(obligation.getComment());
        values.add(obligation.getAuthority());
        values.add(RODUtil.str2Date(obligation.getRmVerified()));
        values.add(obligation.getRmVerifiedBy());
        values.add(RODUtil.str2Date(obligation.getRmNextUpdate()));
        values.add(obligation.getValidatedBy());
        values.add(obligation.getFkClientId());
        values.add(obligation.getFkSourceId());
        values.add(obligation.getContinousReporting());

        Integer obligationId = null;

        Connection conn = null;
        try {
            conn = getConnection();
            SQLUtil.executeUpdate(insertObligationSQL, values, conn);
            obligationId = SQLUtil.getLastInsertID(conn);

        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
        return obligationId;
    }

    private static final String q_obligation_urls =
        "SELECT FORMAT_NAME, REPORT_FORMAT_URL, LOCATION_INFO, LOCATION_PTR, NATIONAL_CONTACT, NATIONAL_CONTACT_URL, "
        + "COORDINATOR, COORDINATOR_URL, OVERLAP_URL, DATA_USED_FOR, DATA_USED_FOR_URL "
        + "FROM T_OBLIGATION";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.IObligationDao#getObligationsUrls()
     */
    public List<UrlDTO> getObligationsUrls() throws ServiceException {

        List<UrlDTO> ret = new ArrayList<UrlDTO>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            if (isDebugMode) logQuery(q_obligation_urls);
            preparedStatement = connection.prepareStatement(q_obligation_urls);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                UrlDTO url = new UrlDTO();

                url.setTitle(rs.getString("FORMAT_NAME"));
                url.setUrl(rs.getString("REPORT_FORMAT_URL"));
                ret.add(url);

                url = new UrlDTO();
                url.setTitle(rs.getString("LOCATION_INFO"));
                url.setUrl(rs.getString("LOCATION_PTR"));
                ret.add(url);

                url = new UrlDTO();
                url.setTitle(rs.getString("NATIONAL_CONTACT"));
                url.setUrl(rs.getString("NATIONAL_CONTACT_URL"));
                ret.add(url);

                url = new UrlDTO();
                url.setTitle(rs.getString("COORDINATOR"));
                url.setUrl(rs.getString("COORDINATOR_URL"));
                ret.add(url);

                url = new UrlDTO();
                url.setTitle(null);
                url.setUrl(rs.getString("OVERLAP_URL"));
                ret.add(url);

                url = new UrlDTO();
                url.setTitle(rs.getString("DATA_USED_FOR"));
                url.setUrl(rs.getString("DATA_USED_FOR_URL"));
                ret.add(url);
            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return ret;
    }

}
