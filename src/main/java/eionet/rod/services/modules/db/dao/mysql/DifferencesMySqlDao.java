package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import eionet.rod.dto.DifferenceDTO;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IDifferencesDao;

public class DifferencesMySqlDao extends MySqlBaseDao implements IDifferencesDao {

    private static final String q_current_sql_differences_in_countries =
        "SELECT FK_SPATIAL_ID "
        + "FROM T_RASPATIAL_LNK "
        + "WHERE " + "FK_RA_ID=?  AND VOLUNTARY =?";

    private static final String q_undo_sql_differences =
        "SELECT value, sub_trans_nr "
        + "FROM T_UNDO WHERE "
        + "undo_time =? AND col=? AND operation = ?";

    public DifferencesMySqlDao() {
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IDifferencesDao#getDifferencesInCountries(long,
     *      int, java.lang.String, java.lang.String)
     */
    public DifferenceDTO getDifferencesInCountries(long ts, int id, String voluntary, String op) throws ServiceException {

        Vector current = new Vector();
        Vector undo = new Vector();

        StringBuffer currentCountries = new StringBuffer();
        StringBuffer undoCountries = new StringBuffer();
        StringBuffer added = new StringBuffer();
        StringBuffer removed = new StringBuffer();

        DifferenceDTO ret = new DifferenceDTO();
        Connection connection = null;
        PreparedStatement currentPreparedStatement = null;
        PreparedStatement undoPreparedStatement = null;

        try {
            connection = getConnection();
            currentPreparedStatement = connection.prepareStatement(q_current_sql_differences_in_countries);
            currentPreparedStatement.setInt(1, id);
            currentPreparedStatement.setString(2, voluntary);
            if (isDebugMode) logQuery(q_current_sql_differences_in_countries);
            undoPreparedStatement = connection.prepareStatement(q_undo_sql_differences);
            undoPreparedStatement.setLong(1, ts);
            undoPreparedStatement.setString(2, "FK_SPATIAL_ID");
            undoPreparedStatement.setString(3, op);
            if (isDebugMode) logQuery(q_undo_sql_differences);
            current = _getVectorOfNames(currentPreparedStatement, ts, null, null, null,
                    "SELECT SPATIAL_NAME FROM T_SPATIAL WHERE PK_SPATIAL_ID = ", null);
            undo = _getVectorOfNames(undoPreparedStatement, ts, "T_RASPATIAL_LNK", "VOLUNTARY", voluntary,
                    "SELECT SPATIAL_NAME FROM T_SPATIAL WHERE PK_SPATIAL_ID = ", null);
            currentPreparedStatement.close();
            undoPreparedStatement.close();
        } catch (SQLException sqle) {
            LOGGER.error(sqle.getMessage(),sqle);
            throw new ServiceException(sqle.getMessage());
        } finally {
            closeAllResources(null, null, connection);
        }

        int cnt1 = 0;
        int i = 0;
        for (Enumeration en = current.elements(); en.hasMoreElements();) {
            if (cnt1 != 0) {
                currentCountries.append(", ");
            }
            String match = (String) en.nextElement();
            currentCountries.append(match);
            if (!undo.contains(match)) {
                if (i != 0) added.append(", ");
                added.append(match);
                i++;
            }
            cnt1++;
        }

        int cnt2 = 0;
        int j = 0;
        for (Enumeration en = undo.elements(); en.hasMoreElements();) {
            if (cnt2 != 0) {
                undoCountries.append(", ");
            }
            String match = (String) en.nextElement();
            undoCountries.append(match);
            if (!current.contains(match)) {
                if (j != 0) removed.append(", ");
                removed.append(match);
                j++;
            }
            cnt2++;
        }

        ret.setUndo(undoCountries.toString());
        ret.setCurrent(currentCountries.toString());
        ret.setAdded(added.toString());
        ret.setRemoved(removed.toString());

        return ret;
    }

    private static final String q_current_sql_differences_in_clients =
        "SELECT FK_CLIENT_ID "
        + "FROM T_CLIENT_LNK WHERE "
        + "FK_OBJECT_ID =? AND STATUS =? AND TYPE =?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IDifferencesDao#getDifferencesInClients(long,
     *      int, java.lang.String, java.lang.String, java.lang.String)
     */
    public DifferenceDTO getDifferencesInClients(long ts, int id, String status, String op, String type) throws ServiceException {

        Vector current = new Vector();
        Vector undo = new Vector();

        StringBuffer currentClients = new StringBuffer();
        StringBuffer undoClients = new StringBuffer();
        StringBuffer added = new StringBuffer();
        StringBuffer removed = new StringBuffer();

        DifferenceDTO ret = new DifferenceDTO();

        Connection connection = null;
        PreparedStatement currentPreparedStatement = null;
        PreparedStatement undoPreparedStatement = null;

        try {
            connection = getConnection();
            currentPreparedStatement = connection.prepareStatement(q_current_sql_differences_in_clients);
            currentPreparedStatement.setInt(1, id);
            currentPreparedStatement.setString(2, status);
            currentPreparedStatement.setString(3, type);
            if (isDebugMode) logQuery(q_current_sql_differences_in_clients);
            undoPreparedStatement = connection.prepareStatement(q_undo_sql_differences);
            undoPreparedStatement.setLong(1, ts);
            undoPreparedStatement.setString(2, "FK_CLIENT_ID");
            undoPreparedStatement.setString(3, op);
            if (isDebugMode) logQuery(q_undo_sql_differences);
            current = _getVectorOfNames(currentPreparedStatement, ts, null, null, null,
                    "SELECT CLIENT_NAME FROM T_CLIENT WHERE PK_CLIENT_ID = ", null);
            undo = _getVectorOfNames(undoPreparedStatement, ts, "T_CLIENT_LNK", "TYPE", type,
                    "SELECT CLIENT_NAME FROM T_CLIENT WHERE PK_CLIENT_ID = ", status);
            currentPreparedStatement.close();
            undoPreparedStatement.close();
        } catch (SQLException sqle) {
            LOGGER.error(sqle.getMessage(), sqle);
            throw new ServiceException(sqle.getMessage());
        } finally {
            closeAllResources(null, null, connection);
        }

        int cnt1 = 0;
        int i = 0;
        for (Enumeration en = current.elements(); en.hasMoreElements();) {
            if (cnt1 != 0) {
                currentClients.append(",<br/>");
            }
            String match = (String) en.nextElement();
            currentClients.append(match);
            if (!undo.contains(match)) {
                if (i != 0) added.append(",<br/>");
                added.append(match);
                i++;
            }
            cnt1++;
        }

        int cnt2 = 0;
        int j = 0;
        for (Enumeration en = undo.elements(); en.hasMoreElements();) {
            if (cnt2 != 0) {
                undoClients.append(",<br/>");
            }
            String match = (String) en.nextElement();
            undoClients.append(match);
            if (!current.contains(match)) {
                if (j != 0) removed.append(",<br/>");
                removed.append(match);
                j++;
            }
            cnt2++;
        }

        ret.setUndo(undoClients.toString());
        ret.setCurrent(currentClients.toString());
        ret.setAdded(added.toString());
        ret.setRemoved(removed.toString());

        return ret;
    }

    private static final String q_current_sql_differences_in_eurlex_categories =
        "SELECT FK_SOURCE_PARENT_ID "
        + "FROM T_SOURCE_LNK WHERE "
        + "FK_SOURCE_CHILD_ID =? AND PARENT_TYPE='C' AND CHILD_TYPE='S'";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IDifferencesDao#getDifferencesInClients(long,
     *      int, java.lang.String, java.lang.String, java.lang.String)
     */
    public DifferenceDTO getDifferencesInEurlexCategories(long ts, int id, String op) throws ServiceException {

        Vector current = new Vector();
        Vector undo = new Vector();

        StringBuffer currentClients = new StringBuffer();
        StringBuffer undoClients = new StringBuffer();
        StringBuffer added = new StringBuffer();
        StringBuffer removed = new StringBuffer();

        DifferenceDTO ret = new DifferenceDTO();

        Connection connection = null;
        PreparedStatement currentPreparedStatement = null;
        PreparedStatement undoPreparedStatement = null;

        try {
            connection = getConnection();
            currentPreparedStatement = connection.prepareStatement(q_current_sql_differences_in_eurlex_categories);
            currentPreparedStatement.setInt(1, id);
            if (isDebugMode) logQuery(q_current_sql_differences_in_eurlex_categories);
            undoPreparedStatement = connection.prepareStatement(q_undo_sql_differences);
            undoPreparedStatement.setLong(1, ts);
            undoPreparedStatement.setString(2, "FK_SOURCE_PARENT_ID");
            undoPreparedStatement.setString(3, op);
            if (isDebugMode) logQuery(q_undo_sql_differences);
            current = _getVectorOfNames(currentPreparedStatement, ts, null, null, null,
                    "SELECT CLASS_NAME FROM T_SOURCE_CLASS WHERE PK_CLASS_ID = ", null);
            undo = _getVectorOfNames(undoPreparedStatement, ts, "T_SOURCE_LNK", "CHILD_TYPE", "S",
                    "SELECT CLASS_NAME FROM T_SOURCE_CLASS WHERE PK_CLASS_ID = ", null);
            currentPreparedStatement.close();
            undoPreparedStatement.close();
        } catch (SQLException sqle) {
            LOGGER.error(sqle.getMessage(), sqle);
            throw new ServiceException(sqle.getMessage());
        } finally {
            closeAllResources(null, null, connection);
        }

        int cnt1 = 0;
        int i = 0;
        for (Enumeration en = current.elements(); en.hasMoreElements();) {
            if (cnt1 != 0) {
                currentClients.append(", ");
            }
            String match = (String) en.nextElement();
            currentClients.append(match);
            if (!undo.contains(match)) {
                if (i != 0) added.append(", ");
                added.append(match);
                i++;
            }
            cnt1++;
        }

        int cnt2 = 0;
        int j = 0;
        for (Enumeration en = undo.elements(); en.hasMoreElements();) {
            if (cnt2 != 0) {
                undoClients.append(", ");
            }
            String match = (String) en.nextElement();
            undoClients.append(match);
            if (!current.contains(match)) {
                if (j != 0) removed.append(", ");
                removed.append(match);
                j++;
            }
            cnt2++;
        }

        ret.setUndo(undoClients.toString());
        ret.setCurrent(currentClients.toString());
        ret.setAdded(added.toString());
        ret.setRemoved(removed.toString());

        return ret;
    }

    private static final String q_current_sql_differences_in_issues =
        "SELECT FK_ISSUE_ID "
        + "FROM T_RAISSUE_LNK "
        + "WHERE FK_RA_ID =? ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IDifferencesDao#getDifferencesInIssues(long,
     *      int, java.lang.String)
     */
    public DifferenceDTO getDifferencesInIssues(long ts, int id, String op) throws ServiceException {

        Vector current = new Vector();
        Vector undo = new Vector();

        StringBuffer currentIssues = new StringBuffer();
        StringBuffer undoIssues = new StringBuffer();
        StringBuffer added = new StringBuffer();
        StringBuffer removed = new StringBuffer();

        DifferenceDTO ret = new DifferenceDTO();

        Connection connection = null;
        PreparedStatement currentPreparedStatement = null;
        PreparedStatement undoPreparedStatement = null;

        try {
            connection = getConnection();
            currentPreparedStatement = connection.prepareStatement(q_current_sql_differences_in_issues);
            currentPreparedStatement.setInt(1, id);
            if (isDebugMode) logQuery(q_current_sql_differences_in_issues);
            undoPreparedStatement = connection.prepareStatement(q_undo_sql_differences);
            undoPreparedStatement.setLong(1, ts);
            undoPreparedStatement.setString(2, "FK_ISSUE_ID");
            undoPreparedStatement.setString(3, op);
            if (isDebugMode) logQuery(q_undo_sql_differences);
            current = _getVectorOfNames(currentPreparedStatement, ts, null, null, null,
                    "SELECT ISSUE_NAME FROM T_ISSUE WHERE PK_ISSUE_ID = ", null);
            undo = _getVectorOfNames(undoPreparedStatement, ts, null, null, null,
                    "SELECT ISSUE_NAME FROM T_ISSUE WHERE PK_ISSUE_ID = ", null);
            currentPreparedStatement.close();
            undoPreparedStatement.close();
        } catch (SQLException sqle) {
            LOGGER.error(sqle.getMessage(), sqle);
            throw new ServiceException(sqle.getMessage());
        } finally {
            closeAllResources(null, null, connection);
        }

        int cnt1 = 0;
        int i = 0;
        for (Enumeration en = current.elements(); en.hasMoreElements();) {
            if (cnt1 != 0) {
                currentIssues.append(", ");
            }
            String match = (String) en.nextElement();
            currentIssues.append(match);
            if (!undo.contains(match)) {
                if (i != 0) added.append(", ");
                added.append(match);
                i++;
            }
            cnt1++;
        }

        int cnt2 = 0;
        int j = 0;
        for (Enumeration en = undo.elements(); en.hasMoreElements();) {
            if (cnt2 != 0) {
                undoIssues.append(", ");
            }
            String match = (String) en.nextElement();
            undoIssues.append(match);
            if (!current.contains(match)) {
                if (j != 0) removed.append(", ");
                removed.append(match);
                j++;
            }
            cnt2++;
        }

        ret.setUndo(undoIssues.toString());
        ret.setCurrent(currentIssues.toString());
        ret.setAdded(added.toString());
        ret.setRemoved(removed.toString());

        return ret;
    }

    private static final String q_current_sql_differences_in_info =
        "SELECT FK_INFO_ID "
        + "FROM T_INFO_LNK "
        + "WHERE FK_RA_ID =? ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IDifferencesDao#getDifferencesInInfo(long,
     *      int, java.lang.String, java.lang.String)
     */
    public DifferenceDTO getDifferencesInInfo(long ts, int id, String op, String cat) throws ServiceException {

        Vector current = new Vector();
        Vector undo = new Vector();

        StringBuffer currentInfo = new StringBuffer();
        StringBuffer undoInfo = new StringBuffer();
        StringBuffer added = new StringBuffer();
        StringBuffer removed = new StringBuffer();

        DifferenceDTO ret = new DifferenceDTO();

        Connection connection = null;
        PreparedStatement currentPreparedStatement = null;
        PreparedStatement undoPreparedStatement = null;

        try {
            connection = getConnection();
            currentPreparedStatement = connection.prepareStatement(q_current_sql_differences_in_info);
            currentPreparedStatement.setInt(1, id);
            if (isDebugMode) logQuery(q_current_sql_differences_in_info);
            undoPreparedStatement = connection.prepareStatement(q_undo_sql_differences);
            undoPreparedStatement.setLong(1, ts);
            undoPreparedStatement.setString(2, "FK_INFO_ID");
            undoPreparedStatement.setString(3, op);
            if (isDebugMode) logQuery(q_undo_sql_differences);
            current = _getVectorOfNames(currentPreparedStatement, ts, null, null, null,
                    "SELECT C_TERM FROM T_LOOKUP WHERE CATEGORY = '" + cat + "' AND C_VALUE = ", null);
            undo = _getVectorOfNames(undoPreparedStatement, ts, null, null, null,
                    "SELECT C_TERM FROM T_LOOKUP WHERE CATEGORY = '" + cat + "' AND C_VALUE = ", null);
            currentPreparedStatement.close();
            undoPreparedStatement.close();
        } catch (SQLException sqle) {
            LOGGER.error(sqle.getMessage(), sqle);
            throw new ServiceException(sqle.getMessage());
        } finally {
            closeAllResources(null, null, connection);
        }

        int cnt1 = 0;
        int i = 0;
        for (Enumeration en = current.elements(); en.hasMoreElements();) {
            if (cnt1 != 0) {
                currentInfo.append(", ");
            }
            String match = (String) en.nextElement();
            currentInfo.append(match);
            if (!undo.contains(match)) {
                if (i != 0) added.append(", ");
                added.append(match);
                i++;
            }
            cnt1++;
        }

        int cnt2 = 0;
        int j = 0;
        for (Enumeration en = undo.elements(); en.hasMoreElements();) {
            if (cnt2 != 0) {
                undoInfo.append(", ");
            }
            String match = (String) en.nextElement();
            undoInfo.append(match);
            if (!current.contains(match)) {
                if (j != 0) removed.append(", ");
                removed.append(match);
                j++;
            }
            cnt2++;
        }

        ret.setUndo(undoInfo.toString());
        ret.setCurrent(currentInfo.toString());
        ret.setAdded(added.toString());
        ret.setRemoved(removed.toString());

        return ret;
    }

    private static final String q_where_sql =
        "SELECT value "
                + "FROM T_UNDO "
                + "WHERE undo_time=? AND tab=? AND operation='A'";

    public String getDifferences(long ts, String tab, String col) throws ServiceException {

        String whereClause = null;
        String ret = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String[][] wa = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_where_sql);
            preparedStatement.setLong(1, ts);
            preparedStatement.setString(2, tab);
            if (isDebugMode) logQuery(q_where_sql);
            wa = _executeStringQuery(preparedStatement);
            if (wa.length > 0) whereClause = wa[0][0];

            if (whereClause != null) {
                String value_sql = "SELECT " + col + " FROM " + tab + " WHERE " + whereClause;
                String[][] va = _executeStringQuery(value_sql);
                if (va.length > 0) ret = va[0][0];
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
