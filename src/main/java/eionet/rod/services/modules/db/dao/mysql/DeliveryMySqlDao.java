package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;

import eionet.rod.RODUtil;
import eionet.rod.dto.CountryDeliveryDTO;
import eionet.rod.dto.CountryDeliveryDataDTO;
import eionet.rod.dto.readers.CountryDeliveryDTOReader;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IDeliveryDao;
import eionet.rod.util.sql.SQLUtil;

public class DeliveryMySqlDao extends MySqlBaseDao implements IDeliveryDao {

    private static String obligationsPrefix = rodDomain + "/obligations/";
    private static String spatialsPrefix = rodDomain + "/spatial/";

    private static DateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static final String qSaveDeliveries = "INSERT INTO T_DELIVERY (TITLE,RA_URL,TYPE,FORMAT,COVERAGE,"
            + "STATUS,UPLOAD_DATE,DELIVERY_URL,FK_SPATIAL_ID,FK_RA_ID) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?)";

    public DeliveryMySqlDao() {
    };

    /** */
    private static final String qMarkCountries = "" + "UPDATE T_OBLIGATION "
            + "SET LAST_HARVESTED = {fn now()}, FK_DELIVERY_COUNTRY_IDS = ? " + "WHERE PK_RA_ID = ?;";

    /**
     *
     * @param raId
     * @param cIds
     * @param connection
     * @throws Exception
     */
    protected void markCountries(Integer raId, String cIds, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(qMarkCountries);
        preparedStatement.setString(1, cIds);
        preparedStatement.setInt(2, raId.intValue());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IDeliveryDao#saveDeliveries(TupleQueryResult, HashMap<String,HashSet<Integer>>)
     */
    public int saveDeliveries(TupleQueryResult bindings, HashMap<String, HashSet<Integer>> savedCountriesByObligationId)
            throws ServiceException {

        int batchCounter = 0;

        if (bindings == null) {
            return batchCounter;
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(qSaveDeliveries);

            for (int row = 0; bindings.hasNext(); row++) {
                BindingSet pairs = bindings.next();

                String link = pairs.getValue("link").stringValue();
                if (link == null || link.trim().length() == 0) {
                    link = "No URL";
                }
                String title = (pairs.getValue("title") != null) ? pairs.getValue("title").stringValue() : "";
                String locality = (pairs.getValue("locality") != null) ? pairs.getValue("locality").stringValue() : "";
                String obligation = (pairs.getValue("obligation") != null) ? pairs.getValue("obligation").stringValue() : "";
                String period = (pairs.getValue("period") != null) ? pairs.getValue("period").stringValue() : "";
                String sdate = (pairs.getValue("date") != null) ? pairs.getValue("date").stringValue() : "";
                Date date = null;
                try {
                    date = isoDateFormat.parse(sdate);
                } catch (ParseException pe) {
                }

                String countryId = null;
                if (!StringUtils.isBlank(locality) && locality.startsWith(spatialsPrefix)) {
                    int index = locality.lastIndexOf("/");
                    if (index != -1 && locality.length() > (index + 1)) {
                        countryId = locality.substring(index + 1);
                    }
                }

                if (countryId == null) {
                    logger.info("!!! Delivery not saved, failed to find id for country: " + locality + ", " + "Identifier: " + link
                            + ", " + "Title: " + title + ", " + "Date: " + sdate + ", " + "Coverage: " + period);
                } else if (!StringUtils.isBlank(obligation) && obligation.startsWith(obligationsPrefix)) {

                    int index = obligation.lastIndexOf("/");
                    if (index != -1 && obligation.length() > (index + 1)) {
                        String obligationId = obligation.substring(index + 1);

                        if (!StringUtils.isBlank(obligationId)) {
                            preparedStatement.setString(1, (title == null) ? "" : title);
                            preparedStatement.setString(2, obligation);
                            preparedStatement.setString(3, "");
                            preparedStatement.setString(4, "");
                            preparedStatement.setString(5, ((period == null) ? "" : period));
                            preparedStatement.setInt(6, 1);
                            preparedStatement.setTimestamp(7, date != null ? new Timestamp(date.getTime()) : null);
                            preparedStatement.setString(8, link);
                            preparedStatement.setInt(9, Integer.parseInt(countryId));
                            preparedStatement.setInt(10, Integer.parseInt(obligationId));

                            preparedStatement.addBatch();
                            batchCounter++;

                            HashSet<Integer> savedCountries = (HashSet<Integer>) savedCountriesByObligationId.get(obligationId);
                            if (savedCountries == null) {
                                savedCountries = new HashSet<Integer>();
                                savedCountriesByObligationId.put(obligationId, savedCountries);
                            }
                            savedCountries.add(Integer.parseInt(countryId));
                        }
                    }
                }
            }
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("Saving deliveries failed with reason " + e.toString());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return batchCounter;
    }

    private static final String qRollBackDeliveriesDelete = "DELETE " + "FROM T_DELIVERY " + "WHERE STATUS=1 AND FK_RA_ID=?";

    private static final String qRollBackDeliveriesUpdate = "UPDATE T_DELIVERY " + "SET STATUS=1 "
            + "WHERE STATUS=0 AND FK_RA_ID=?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IDeliveryDao#rollBackDeliveries(java.lang.Integer)
     */
    public void rollBackDeliveries(Integer raId) throws ServiceException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(qRollBackDeliveriesDelete);
            preparedStatement.setInt(1, raId.intValue());
            if (isDebugMode)
                logQuery(qRollBackDeliveriesDelete);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(qRollBackDeliveriesUpdate);
            preparedStatement.setInt(1, raId.intValue());
            if (isDebugMode)
                logQuery(qRollBackDeliveriesUpdate);
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IDeliveryDao#rollBackDeliveries()
     */
    public void rollBackDeliveries() throws ServiceException {

        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate("DELETE from T_DELIVERY WHERE STATUS=1");
            statement.close();
            statement = connection.createStatement();
            statement.executeUpdate("UPDATE T_DELIVERY SET STATUS=1 WHERE STATUS=0");
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, statement, connection);
        }
    }

    private static final String qCommitDeliveries = "DELETE " + "FROM T_DELIVERY " + "WHERE STATUS=0";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IDeliveryDao#commitDeliveriesNew(java.util.HashMap)
     */
    public void commitDeliveries(HashMap<String, HashSet<Integer>> deliveredCountriesByObligations) throws ServiceException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();

            // commit deliveries first

            preparedStatement = connection.prepareStatement(qCommitDeliveries);
            if (isDebugMode) {
                logQuery(qCommitDeliveries);
            }
            preparedStatement.executeUpdate();

            // now mark the countries

            if (deliveredCountriesByObligations != null && !deliveredCountriesByObligations.isEmpty()) {

                Iterator<Entry<String, HashSet<Integer>>> entries = deliveredCountriesByObligations.entrySet().iterator();
                while (entries.hasNext()) {

                    Entry<String, HashSet<Integer>> entry = entries.next();
                    String obligId = (String) entry.getKey();
                    HashSet<Integer> countryIdsSet = (HashSet<Integer>) entry.getValue();
                    if (countryIdsSet != null && !countryIdsSet.isEmpty()) {
                        String countryIds = "," + cnvHashSet(countryIdsSet, ",") + ",";
                        markCountries(Integer.valueOf(obligId), countryIds, connection);
                    }
                }
            }

        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }
    }

    private static final String qBackUpDeliveries = "UPDATE T_DELIVERY " + "SET STATUS=0";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IDeliveryDao#backUpDeliveries()
     */
    public void backUpDeliveries() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(qBackUpDeliveries);
            if (isDebugMode)
                logQuery(qBackUpDeliveries);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }
    }

    private String getDeliveriesListSql(String actDetailsId, String spatialId) throws ServiceException {

        StringBuilder q_obligations_list = new StringBuilder(
                "SELECT T_DELIVERY.FK_RA_ID, T_DELIVERY.FK_SPATIAL_ID, T_DELIVERY.TITLE, ");
        q_obligations_list
                .append("T_DELIVERY.DELIVERY_URL, T_DELIVERY.UPLOAD_DATE, T_DELIVERY.TYPE AS DELIVERY_TYPE, T_DELIVERY.FORMAT, T_DELIVERY.COVERAGE, "
                        + "T_OBLIGATION.PK_RA_ID, T_OBLIGATION.FK_SOURCE_ID, T_OBLIGATION.TITLE AS OBLIGATION_TITLE, T_OBLIGATION.REPORT_FREQ_MONTHS, "
                        + "T_OBLIGATION.TERMINATE, T_OBLIGATION.NEXT_DEADLINE, T_OBLIGATION.REPORT_FORMAT_URL, T_OBLIGATION.RESPONSIBLE_ROLE, "
                        + "T_OBLIGATION.FORMAT_NAME, T_OBLIGATION.FK_DELIVERY_COUNTRY_IDS, T_OBLIGATION.PARAMETERS, "
                        + "T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_SPATIAL.SPATIAL_TWOLETTER, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY, "
                        + "T_ROLE.ROLE_NAME, T_ROLE.ROLE_URL, T_ROLE.ROLE_MEMBERS_URL, "
                        + "T_CLIENT_LNK.FK_CLIENT_ID, T_CLIENT_LNK.FK_OBJECT_ID, T_CLIENT_LNK.STATUS, T_CLIENT_LNK.TYPE, "
                        + "T_CLIENT.PK_CLIENT_ID, T_CLIENT.CLIENT_NAME "
                        + "FROM T_DELIVERY JOIN T_OBLIGATION ON T_DELIVERY.FK_RA_ID=T_OBLIGATION.PK_RA_ID "
                        + "JOIN T_SPATIAL ON T_SPATIAL.PK_SPATIAL_ID=T_DELIVERY.FK_SPATIAL_ID "
                        + "LEFT JOIN T_ROLE ON CONCAT(T_OBLIGATION.RESPONSIBLE_ROLE,'-',IF(T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY='Y','mc','cc'),'-',LCASE(T_SPATIAL.SPATIAL_TWOLETTER))=T_ROLE.ROLE_ID "
                        + "LEFT JOIN T_CLIENT_LNK ON T_CLIENT_LNK.TYPE='A' AND T_CLIENT_LNK.STATUS='M' AND T_CLIENT_LNK.FK_OBJECT_ID=T_OBLIGATION.PK_RA_ID "
                        + "LEFT JOIN T_CLIENT ON T_CLIENT_LNK.FK_CLIENT_ID=T_CLIENT.PK_CLIENT_ID "
                        + "WHERE T_DELIVERY.FK_RA_ID="
                        + actDetailsId);

        if (!RODUtil.isNullOrEmpty(spatialId))
            q_obligations_list.append(" AND T_DELIVERY.FK_SPATIAL_ID = " + spatialId);

        q_obligations_list.append(" ORDER BY T_SPATIAL.SPATIAL_NAME, T_DELIVERY.UPLOAD_DATE DESC");

        return q_obligations_list.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.IDeliveryDao#getCountryDeliveriesList()
     */
    public List<CountryDeliveryDTO> getCountyDeliveriesList(String actDetailsId, String spatialId) throws ServiceException {

        String query = getDeliveriesListSql(actDetailsId, spatialId);

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        CountryDeliveryDTOReader rsReader = new CountryDeliveryDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<CountryDeliveryDTO> list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
            }
        }
    }

    private static final String q_delivery_data = "SELECT T_OBLIGATION.PK_RA_ID, T_OBLIGATION.TITLE, T_OBLIGATION.LAST_HARVESTED, T_OBLIGATION.TERMINATE, "
            + "T_OBLIGATION.REPORT_FREQ_MONTHS, T_OBLIGATION.NEXT_DEADLINE, T_OBLIGATION.FORMAT_NAME, T_OBLIGATION.REPORT_FORMAT_URL, "
            + "T_CLIENT_LNK.FK_CLIENT_ID, T_CLIENT_LNK.FK_OBJECT_ID, T_CLIENT_LNK.TYPE, T_CLIENT_LNK.STATUS, "
            + "T_CLIENT.PK_CLIENT_ID, T_CLIENT.CLIENT_NAME "
            + "FROM T_OBLIGATION JOIN T_CLIENT_LNK ON T_CLIENT_LNK.TYPE='A' AND T_CLIENT_LNK.STATUS='M' AND T_CLIENT_LNK.FK_OBJECT_ID=T_OBLIGATION.PK_RA_ID "
            + "LEFT JOIN T_CLIENT ON T_CLIENT_LNK.FK_CLIENT_ID=T_CLIENT.PK_CLIENT_ID " + "WHERE T_OBLIGATION.PK_RA_ID=?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.IDeliveryDao#getDeliveryData()
     */
    public CountryDeliveryDataDTO getDeliveryData(String actDetailsId) throws ServiceException {

        CountryDeliveryDataDTO ret = new CountryDeliveryDataDTO();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            if (isDebugMode)
                logQuery(q_delivery_data);
            preparedStatement = connection.prepareStatement(q_delivery_data);
            preparedStatement.setString(1, actDetailsId);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ret.setObligationId(new Integer(rs.getInt("PK_RA_ID")));
                ret.setObligationTitle(rs.getString("TITLE"));
                ret.setObligationLastHarvested(rs.getString("LAST_HARVESTED"));
                ret.setObligationTerminate(rs.getString("TERMINATE"));
                ret.setObligationReportFreqMonths(rs.getString("REPORT_FREQ_MONTHS"));
                ret.setObligationNextDeadline(rs.getString("NEXT_DEADLINE"));
                ret.setObligationFormatName(rs.getString("FORMAT_NAME"));
                ret.setObligationReportFormatUrl(rs.getString("REPORT_FORMAT_URL"));

                ret.setClientLnkFKClientId(new Integer(rs.getInt("FK_CLIENT_ID")));
                ret.setClientLnkFKObjectId(new Integer(rs.getInt("FK_OBJECT_ID")));
                ret.setClientLnkType(rs.getString("TYPE"));
                ret.setClientLnkStatus(rs.getString("STATUS"));

                ret.setClientId(new Integer(rs.getInt("PK_CLIENT_ID")));
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
}
