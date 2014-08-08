package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.DocumentationDTO;
import eionet.rod.dto.HelpDTO;
import eionet.rod.dto.readers.DocumentationDTOReader;
import eionet.rod.dto.readers.HelpDTOReader;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IHelpDao;
import eionet.rod.util.sql.SQLUtil;

public class HelpMySqlDao extends MySqlBaseDao implements IHelpDao {

    public HelpMySqlDao() {
    }

    private static final String q_html_by_area_id =
        "SELECT HTML AS name " +
        "FROM HLP_AREA " +
        "WHERE AREA_ID =?";

    /* (non-Javadoc)
     * @see eionet.rod.services.modules.db.dao.IGenericDao#getHelpAreaText(String area_id)
     */
    public String getHelpAreaHtml(String area_id) throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String result = null;
        String[][] m = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_html_by_area_id);
            preparedStatement.setString(1, area_id);
            if (isDebugMode) logQuery(q_html_by_area_id);
            m = _executeStringQuery(preparedStatement);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }
        if (m.length > 0) result = m[0][0];

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IGenericDao#getDocList()
     */
    public List<DocumentationDTO> getDocList() throws ServiceException {

        String query =
            "SELECT AREA_ID, SCREEN_ID, DESCRIPTION, HTML " +
            "FROM HLP_AREA " +
            "WHERE SCREEN_ID='documentation'";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        DocumentationDTOReader rsReader = new DocumentationDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<DocumentationDTO>  list = rsReader.getResultList();
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

    private static final String q_get_doc =
        "SELECT AREA_ID, SCREEN_ID, DESCRIPTION, HTML " +
        "FROM HLP_AREA " +
        "WHERE AREA_ID=?";

    /* (non-Javadoc)
     * @see eionet.rod.services.modules.db.dao.IGenericDao#getDoc(String area_id)
     */
    public DocumentationDTO getDoc(String area_id) throws ServiceException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        DocumentationDTO ret = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_get_doc);
            preparedStatement.setString(1, area_id);
            if (isDebugMode) logQuery(q_get_doc);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ret = new DocumentationDTO();
                ret.setAreaId(rs.getString("AREA_ID"));
                ret.setScreenId(rs.getString("SCREEN_ID"));
                ret.setDescription(rs.getString("DESCRIPTION"));
                ret.setHtml(rs.getString("HTML"));
            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return ret;
    }

    private static final String q_get_help =
        "SELECT PK_HELP_ID, HELP_TITLE, HELP_TEXT " +
        "FROM T_HELP WHERE PK_HELP_ID=?";


    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IGenericDao#getHelp(String id)
     */
    public HelpDTO getHelp(String id) throws ServiceException {

        HelpDTO ret = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            if (isDebugMode) logQuery(q_get_help);
            preparedStatement = connection.prepareStatement(q_get_help);
            preparedStatement.setString(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ret = new HelpDTO();
                ret.setHelpId(rs.getString("PK_HELP_ID"));
                ret.setHelpTitle(rs.getString("HELP_TITLE"));
                ret.setHelpText(rs.getString("HELP_TEXT"));
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
     * @see eionet.rod.services.modules.db.dao.IGenericDao#getHelpList(String id)
     */
    public List<HelpDTO> getHelpList(String id) throws ServiceException {

        String query =
            "SELECT PK_HELP_ID, HELP_TITLE, HELP_TEXT " +
            "FROM T_HELP WHERE ";

        if (id != null && id.equals("RO"))
            query = query + "LOCATE('HELP_RO_', PK_HELP_ID) > 0 OR LOCATE('HELP_RA_', PK_HELP_ID)";
        else
            query = query + "LOCATE('HELP_LI_', PK_HELP_ID) > 0";

        query = query + " ORDER BY HELP_TITLE";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        HelpDTOReader rsReader = new HelpDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<HelpDTO>  list = rsReader.getResultList();
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
    private static final String editHelpSQL = "UPDATE T_HELP SET HELP_TITLE=?, HELP_TEXT=? " +
            "WHERE PK_HELP_ID=?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IGenericDao#editHelp(HelpDTO help)
     */
    public void editHelp(HelpDTO help) throws ServiceException {

        List<Object> values = new ArrayList<Object>();
        values.add(help.getHelpTitle());
        values.add(help.getHelpText());
        values.add(help.getHelpId());

        Connection conn = null;
        try {
            conn = getConnection();
            SQLUtil.executeUpdate(editHelpSQL, values, conn);

        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

}
