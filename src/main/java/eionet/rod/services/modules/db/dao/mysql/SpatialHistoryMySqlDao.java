package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.ISpatialHistoryDao;

public class SpatialHistoryMySqlDao extends MySqlBaseDao implements ISpatialHistoryDao {

    public SpatialHistoryMySqlDao() {
    }

    private static final String q_select_sh_by_voluntary_raid_and_spatialId =
        "SELECT * " +
        "FROM T_SPATIAL_HISTORY " +
        "WHERE VOLUNTARY=? AND END_DATE=NOW() AND FK_RA_ID=? AND FK_SPATIAL_ID=? ";

    private static final String q_insert_into_sh =
        "INSERT INTO T_SPATIAL_HISTORY (FK_RA_ID, FK_SPATIAL_ID, VOLUNTARY, START_DATE) " +
        "VALUES(?,?,?,{fn now()})";

    private static final String q_update_sh =
        "UPDATE T_SPATIAL_HISTORY " +
        "SET END_DATE=NULL " +
        "WHERE END_DATE=NOW() AND VOLUNTARY=? AND FK_RA_ID=? AND FK_SPATIAL_ID=?";

    private static final String q_clear_sh =
        "DELETE FROM T_SPATIAL_HISTORY " +
        "WHERE START_DATE=END_DATE " +
        "AND END_DATE=NOW()";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISpatialHistoryDao#logSpatialHistory(int,
     *      int, java.lang.String)
     */
    public void logSpatialHistory(int raId, int spatialId, String voluntary) throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String[][] result = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_select_sh_by_voluntary_raid_and_spatialId);
            preparedStatement.setString(1, voluntary);
            preparedStatement.setInt(2, raId);
            preparedStatement.setInt(3, spatialId);
            if (isDebugMode) logQuery(q_select_sh_by_voluntary_raid_and_spatialId);
            result = _executeStringQuery(preparedStatement);
            preparedStatement.close();
            if (result.length == 0) {
                preparedStatement = connection.prepareStatement(q_insert_into_sh);
                preparedStatement.setInt(1, raId);
                preparedStatement.setInt(2, spatialId);
                preparedStatement.setString(3, voluntary);
                if (isDebugMode) logQuery(q_insert_into_sh);
                preparedStatement.executeUpdate();

            } else {
                preparedStatement = connection.prepareStatement(q_update_sh);
                preparedStatement.setString(1, voluntary);
                preparedStatement.setInt(2, raId);
                preparedStatement.setInt(3, spatialId);
                if (isDebugMode) logQuery(q_update_sh);
                preparedStatement.executeUpdate();

            }
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(q_clear_sh);
            if (isDebugMode) logQuery(q_clear_sh);
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

    }

    private static final String q_update_period_by_spatialHistoryId =
        "UPDATE T_SPATIAL_HISTORY " +
        "SET START_DATE=?, END_DATE=? " +
        "WHERE PK_SPATIAL_HISTORY_ID=?";

    private static final String q_update_period_by_raId =
        "UPDATE T_SPATIAL_HISTORY " +
        "SET START_DATE=?, END_DATE=? " +
        "WHERE FK_RA_ID=?";

    private static SimpleDateFormat simpFormater = new SimpleDateFormat("dd/MM/yyyy");

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISpatialHistoryDao#editPeriod(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public void editPeriod(String start, String end, String spatialHistoryID, String ra_id) throws ServiceException {

        String sql = null;

        if (spatialHistoryID != null && !spatialHistoryID.equals(""))
            sql = q_update_period_by_spatialHistoryId;
        else {
            sql = q_update_period_by_raId;
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, new java.sql.Date(simpFormater.parse(start).getTime()));
            preparedStatement.setDate(2, new java.sql.Date(simpFormater.parse(end).getTime()));
            if (isDebugMode) logQuery(sql);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

    }

    private static final String q_update_end_date_for_obligation =
        "UPDATE T_SPATIAL_HISTORY " +
        "SET END_DATE= {fn now()} " +
        "WHERE END_DATE IS NULL AND FK_RA_ID=?";

    public void updateEndDateForObligation(Integer raId) throws ServiceException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            if (isDebugMode) logQuery(q_update_end_date_for_obligation);
            preparedStatement = connection.prepareStatement(q_update_end_date_for_obligation);
            preparedStatement.setInt(1, raId.intValue());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }
    }

}
