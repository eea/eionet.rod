package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import eionet.rod.dto.HarvestHistoryDTO;
import eionet.rod.dto.readers.HarvestHistoryDTOReader;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IHistoryDao;
import eionet.rod.util.sql.SQLUtil;

public class HistoryMySqlDao extends MySqlBaseDao implements IHistoryDao {

    private static final String qGetItemHistory =
        "SELECT LOG_TIME, ACTION_TYPE, USER, DESCRIPTION " +
        "FROM T_HISTORY " +
        "WHERE ITEM_TYPE = ? AND ITEM_ID = ? " +
        "ORDER BY LOG_TIME ";

    public HistoryMySqlDao() {
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IHistoryDao#getItemHistory(java.lang.String,
     *      int)
     */
    @Override
    public String[][] getItemHistory(String itemType, int itemId) throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String[][] result = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(qGetItemHistory);
            preparedStatement.setString(1, itemType);
            preparedStatement.setInt(2, itemId);

            if (isDebugMode) logQuery(qGetItemHistory);
            result = _executeStringQuery(preparedStatement);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result != null ? result : new String[][] {};
    }

    private static final String qDeletedItems =
        "SELECT ITEM_ID, LOG_TIME, USER " +
        "FROM T_HISTORY " +
        "WHERE ACTION_TYPE = ? AND ITEM_TYPE = ?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.mysql.IHistoryDao#getDeletedItems(java.lang.String)
     */
    @Override
    public String[][] getDeletedItems(String itemType) throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String[][] result = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(qDeletedItems);
            preparedStatement.setString(1, DELETE_ACTION_TYPE);
            preparedStatement.setString(2, itemType);
            if (isDebugMode) logQuery(qDeletedItems);
            result = _executeStringQuery(preparedStatement);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result != null ? result : new String[][] {};
    }

    private static final String countDeletedSql =
        "SELECT COUNT(*) AS count " +
        "FROM T_HISTORY " +
        "WHERE ACTION_TYPE =? AND ITEM_TYPE IN (?,?)";

    private static final String selectDeletedSql =
        "SELECT ITEM_ID, ACTION_TYPE, ITEM_TYPE, LOG_TIME, USER " +
        "FROM T_HISTORY " +
        "WHERE ACTION_TYPE =? AND ITEM_TYPE IN (?,?) " +
        "LIMIT ?,? ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.mysql.IHistoryDao#getDeletedItemsVector(java.lang.String)
     */
    @Override
    public Hashtable<String,Object> getDeletedItemsVector(String itemType) throws ServiceException {

        String sql = null;
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rset = null;

        Hashtable<String,Object> ret = new Hashtable<String,Object>();

        FileServiceIF fileSrv = RODServices.getFileService();
        int step = fileSrv.getIntProperty(FileServiceIF.HISTORY_UNDO_STEP);

        try {
            con = getConnection();
            preparedStatement = con.prepareStatement(countDeletedSql);
            preparedStatement.setString(1, DELETE_ACTION_TYPE);
            preparedStatement.setString(2, itemType);
            preparedStatement.setString(3, itemType.equals("O")?"A":itemType);
            if (isDebugMode) logQuery(countDeletedSql);
            rset = preparedStatement.executeQuery();
            int count = 0;
            while (rset.next()) {
                count = rset.getInt("count");
            }
            preparedStatement.close();
            int pages = 0;
            if (count % step == 0)
                pages = (count / step);
            else
                pages = (count / step) + 1;

            ret.put("pages", new Integer(pages));

            int start = 0;
            preparedStatement = con.prepareStatement(selectDeletedSql);
            for (int i = 1; i <= pages; i++) {
                preparedStatement.setString(1, DELETE_ACTION_TYPE);
                preparedStatement.setString(2, itemType);
                preparedStatement.setString(3, itemType.equals("O")?"A":itemType);
                preparedStatement.setInt(4, start);
                preparedStatement.setInt(5, step);
                if (isDebugMode) logQuery(selectDeletedSql);
                Vector<Map<String,String>> vec = _getVectorOfHashes(preparedStatement);
                ret.put(new Integer(i).toString(), vec);
                start = start + step;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error occurred when processing result set: " + sql, e);
            throw new ServiceException("Error occurred when processing result set: " + sql);
        } catch (NullPointerException nue) {
            logger.error("getDeletedItemsVector() NullPointerException " + nue);
        } finally {
            closeAllResources(null, preparedStatement, con);
        }
        return ret;
    }

    private static final String qHistory =
        "SELECT LOG_TIME AS time, ACTION_TYPE AS action, USER AS user, DESCRIPTION AS description " +
        "FROM T_HISTORY " +
        "WHERE ITEM_ID = ?  AND ITEM_TYPE = ? " +
        "ORDER BY LOG_TIME desc";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.mysql.IHistoryDao#getHistory(int,
     *      java.lang.String)
     */
    @Override
    public Vector<Map<String,String>> getHistory(int id, String tab) throws ServiceException {

        String type = null;
        if (tab.equals("T_OBLIGATION"))
            type = "A";
        else if (tab.equals("T_SOURCE")) type = "L";
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Vector<Map<String,String>> result = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(qHistory);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, type);
            if (isDebugMode) logQuery(qHistory);
            result = _getVectorOfHashes(preparedStatement);
        } catch (Exception exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(resultSet, preparedStatement, connection);
        }

        return result != null ? result : new Vector<Map<String,String>>();
    }

    private static final String qLogHistory =
        "INSERT INTO T_HISTORY ( ITEM_ID,ITEM_TYPE,ACTION_TYPE,LOG_TIME,USER,DESCRIPTION ) " +
        "VALUES (?,?,?,?,?,?) ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.mysql.IHistoryDao#logHistory(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     */

    @Override
    public void logHistory(String itemType, String itemId, String userName, String actionType, String description) throws ServiceException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(qLogHistory);
            preparedStatement.setInt(1, Integer.valueOf(itemId).intValue());
            preparedStatement.setString(2, itemType);
            preparedStatement.setString(3, actionType);
            preparedStatement.setTimestamp(4, new Timestamp(new Date().getTime()));
            preparedStatement.setString(5, userName);
            preparedStatement.setString(6, description);
            if (isDebugMode) logQuery(qLogHistory);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.mysql.IHistoryDao#getHarvestHistory()
     */
    @Override
    public List<HarvestHistoryDTO> getHarvestHistory() throws ServiceException {

        String query =
            "SELECT PK_HISTORY_ID, ITEM_ID, ITEM_TYPE, ACTION_TYPE, USER, DESCRIPTION, DATE_FORMAT(LOG_TIME, '%d/%m/%Y %H:%i') AS TIME_STAMP " +
            "FROM T_HISTORY WHERE ITEM_TYPE = 'H' AND ITEM_ID = 0 ORDER BY LOG_TIME DESC LIMIT 100";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        HarvestHistoryDTOReader rsReader = new HarvestHistoryDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<HarvestHistoryDTO>  list = rsReader.getResultList();
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

}
