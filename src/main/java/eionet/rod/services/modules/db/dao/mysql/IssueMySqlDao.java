package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import eionet.rod.dto.IssueDTO;
import eionet.rod.dto.ObligationDTO;
import eionet.rod.dto.readers.IssueDTOReader;
import eionet.rod.dto.readers.ObligationDTOReader;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IIssueDao;
import eionet.rod.util.sql.SQLUtil;

public class IssueMySqlDao extends MySqlBaseDao implements IIssueDao {

    public IssueMySqlDao() {
    }

    private static final String qObligationIssues =
        "SELECT i.ISSUE_NAME AS name " +
        "FROM T_ISSUE i, T_RAISSUE_LNK r " +
        "WHERE  r.FK_RA_ID = ? AND i.PK_ISSUE_ID = r.FK_ISSUE_ID " +
        "ORDER BY i.ISSUE_NAME";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IIssueDao#getObligationIssues(java.lang.Integer)
     */
    public Vector<Map<String, String>> getObligationIssues(Integer id) throws ServiceException {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Vector<Map<String, String>> result = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(qObligationIssues);
            preparedStatement.setInt(1, id.intValue());
            if (isDebugMode) logQuery(qObligationIssues);
            result = _getVectorOfHashes(preparedStatement);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } finally {
            closeAllResources(resultSet, preparedStatement, connection);
        }

        return result != null ? result : new Vector<Map<String, String>>();
    }

    private static final String qIssuesList =
        "SELECT ISSUE_NAME, PK_ISSUE_ID " +
        "FROM T_ISSUE " +
        "ORDER BY ISSUE_NAME ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IIssueDao#getIssuesList()
     */
    public List<IssueDTO> getIssuesList() throws ServiceException {

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        IssueDTOReader rsReader = new IssueDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(qIssuesList, values, rsReader, conn);
            List<IssueDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }

    }

    private static final String qIssueObligationsList =
        "SELECT O.TITLE, O.PK_RA_ID, O.FK_SOURCE_ID " +
        "FROM T_OBLIGATION AS O, T_RAISSUE_LNK AS OI " +
        "WHERE O.PK_RA_ID = OI.FK_RA_ID AND OI.FK_ISSUE_ID = ?" +
        "ORDER BY O.TITLE ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IIssueDao#getIssueObligationsList()
     */
    public List<ObligationDTO> getIssueObligationsList(String issueId) throws ServiceException {

        List<Object> values = new ArrayList<Object>();
        values.add(issueId);

        Connection conn = null;
        ObligationDTOReader rsReader = new ObligationDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(qIssueObligationsList, values, rsReader, conn);
            List<ObligationDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IIssueDao#getObligationIssuesList(java.lang.String)
     */
    public List<IssueDTO> getObligationIssuesList(String obligationId) throws ServiceException {

        List<Object> values = new ArrayList<Object>();

        String qObligationIssuesList =
            "SELECT T_ISSUE.ISSUE_NAME, T_ISSUE.PK_ISSUE_ID " +
            "FROM T_ISSUE, T_RAISSUE_LNK " +
            "WHERE T_RAISSUE_LNK.FK_ISSUE_ID=T_ISSUE.PK_ISSUE_ID AND T_RAISSUE_LNK.FK_RA_ID=" + obligationId + " " +
            "ORDER BY T_ISSUE.ISSUE_NAME ";

        Connection conn = null;
        IssueDTOReader rsReader = new IssueDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(qObligationIssuesList, values, rsReader, conn);
            List<IssueDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IIssueDao#getObligationIssuesList(List<String> issueIds)
     */
    public List<IssueDTO> getObligationIssuesList(List<String> issueIds) throws ServiceException {

        StringBuilder ids = new StringBuilder();
        if (issueIds != null) {
            for (Iterator<String> it = issueIds.iterator(); it.hasNext(); ) {
                String id = it.next();
                ids.append(id);
                if (it.hasNext())
                    ids.append(",");
            }
        } else {
            return null;
        }

        List<Object> values = new ArrayList<Object>();

        String qObligationIssuesList =
            "SELECT ISSUE_NAME, PK_ISSUE_ID " +
            "FROM T_ISSUE " +
            "WHERE PK_ISSUE_ID IN ("+ids.toString()+") " +
            "ORDER BY ISSUE_NAME ";

        Connection conn = null;
        IssueDTOReader rsReader = new IssueDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(qObligationIssuesList, values, rsReader, conn);
            List<IssueDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

    }

    private static final String q_issue_name_by_id =
        "SELECT ISSUE_NAME AS name " +
        "FROM T_ISSUE " +
        "WHERE PK_ISSUE_ID =?";

    /* (non-Javadoc)
     * @see eionet.rod.services.modules.db.dao.IIssueDao#getCountryById(String id)
     */
    public String getIssueNameById(String id) throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String result = null;
        String[][] m = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_issue_name_by_id);
            preparedStatement.setString(1, id);
            if (isDebugMode) logQuery(q_issue_name_by_id);
            m = _executeStringQuery(preparedStatement);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }
        if (m.length > 0) result = m[0][0];

        return result;
    }

    private static final String q_insert_obligation_issue =
        "INSERT INTO T_RAISSUE_LNK (FK_ISSUE_ID, FK_RA_ID) VALUES (?,?)";

    /* (non-Javadoc)
     * @see eionet.rod.services.modules.db.dao.IIssueDao#insertObligationIssues(String obligationId, List<String> selectedIssues)
     */
    public void insertObligationIssues(String obligationId, List<String> selectedIssues) throws ServiceException {
        List<Object> values = null;
        Connection conn = null;
        try {
            conn = getConnection();
            for (Iterator<String> it = selectedIssues.iterator(); it.hasNext();) {
                String issueId = it.next();
                values = new ArrayList<Object>();
                values.add(issueId);
                values.add(obligationId);
                SQLUtil.executeUpdate(q_insert_obligation_issue, values, conn);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

}
