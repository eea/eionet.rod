package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import eionet.rod.RODUtil;
import eionet.rod.util.sql.SQLUtil;
import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.CountryInfoDTO;
import eionet.rod.dto.DeliveryDTO;
import eionet.rod.dto.ObligationCountryDTO;
import eionet.rod.dto.ObligationDTO;
import eionet.rod.dto.readers.CountryDTOReader;
import eionet.rod.dto.readers.DeliveryDTOReader;
import eionet.rod.dto.readers.ObligationCountryDTOReader;
import eionet.rod.dto.readers.ObligationDTOReader;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.ISpatialDao;

public class SpatialMySqlDao extends MySqlBaseDao implements ISpatialDao {

    public SpatialMySqlDao() {
    }

    private static final String Q_COUNTRIES =
        "SELECT {fn concat(?, PK_SPATIAL_ID)}  AS uri, "
            + "SPATIAL_NAME AS name, "
            + "UPPER(SPATIAL_TWOLETTER) AS iso "
            + "FROM T_SPATIAL "
            + "WHERE SPATIAL_TYPE='C' "
            + "ORDER BY SPATIAL_NAME ";

    /* (non-Javadoc)
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getCountries()
     */
    @Override
    public Vector<Map<String, String>> getCountries() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Vector<Map<String, String>> result = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Q_COUNTRIES);
            preparedStatement.setString(1, (String) properties.get(FileServiceIF.SPATIAL_NAMESPACE));
            if (isDebugMode) logQuery(Q_COUNTRIES);
            result = _getVectorOfHashes(preparedStatement);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result != null ? result : new Vector<Map<String, String>>();
    }

    private static final String Q_COUNTRY_BY_ID =
        "SELECT SPATIAL_NAME AS name "
            + "FROM T_SPATIAL "
            + "WHERE PK_SPATIAL_ID =?";

    /* (non-Javadoc)
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getCountryById(int)
     */
    @Override
    public String getCountryById(int id) throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String result = null;
        String[][] m = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Q_COUNTRY_BY_ID);
            preparedStatement.setInt(1, id);
            if (isDebugMode) logQuery(Q_COUNTRY_BY_ID);
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

    private static final String Q_OBLIGATION_VOLUNTARY_COUNTRIES =
        "SELECT sl.FK_SPATIAL_ID FROM T_RASPATIAL_LNK sl, T_SPATIAL s "
            + "WHERE s.SPATIAL_TYPE='C' AND sl.VOLUNTARY=? AND sl.FK_SPATIAL_ID=s.PK_SPATIAL_ID AND sl.FK_RA_ID=? "
            + "ORDER BY FK_SPATIAL_ID";

    /* (non-Javadoc)
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getObligationCountries(int, boolean)
     */
    @Override
    public List<Integer> getObligationCountries(final int raId, final boolean voluntary) throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Integer> result = new ArrayList<Integer>();
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Q_OBLIGATION_VOLUNTARY_COUNTRIES);
            if (voluntary)
                preparedStatement.setString(1, "Y");
            else
                preparedStatement.setString(1, "N");
            preparedStatement.setInt(2, raId);

            if (isDebugMode) logQuery(Q_OBLIGATION_VOLUNTARY_COUNTRIES);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Integer spatialId = rs.getInt("sl.FK_SPATIAL_ID");
                result.add(spatialId);
            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }
        return result;
    }

    private static final String Q_OBLIGATION_COUNTRIES =
        "SELECT s.SPATIAL_NAME AS name "
            + "FROM T_SPATIAL s, T_RASPATIAL_LNK r "
            + "WHERE r.FK_RA_ID = ? AND s.PK_SPATIAL_ID = r.FK_SPATIAL_ID "
            + "ORDER BY s.SPATIAL_NAME";

    /* (non-Javadoc)
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getObligationCountries(int)
     */
    @Override
    public Vector<Map<String, String>> getObligationCountries(final int id) throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Vector<Map<String, String>> result = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Q_OBLIGATION_COUNTRIES);
            preparedStatement.setInt(1, id);
            if (isDebugMode) logQuery(Q_OBLIGATION_COUNTRIES);
            result = _getVectorOfHashes(preparedStatement);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result != null ? result : new Vector<Map<String, String>>();

    }

    private static final String Q_COUNTRY_ID_PAIRS =
        "SELECT PK_SPATIAL_ID, SPATIAL_NAME "
        + "FROM T_SPATIAL "
        + "WHERE SPATIAL_TYPE ='C' "
        + "ORDER BY SPATIAL_NAME";

    /* (non-Javadoc)
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getCountryIdPairs()
     */
    @Override
    public String[][] getCountryIdPairs() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String[][] result = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Q_COUNTRY_ID_PAIRS);
            if (isDebugMode) logQuery(Q_COUNTRY_ID_PAIRS);
            result = _executeStringQuery(preparedStatement);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result != null ? result : new String[][] {};
    }


    private static final String Q_OBLIGATION_INFO =
        "SELECT TITLE AS title, RESPONSIBLE_ROLE AS role "
        + "FROM T_OBLIGATION "
        + "WHERE PK_RA_ID=? ";

    private static final String Q_SPATIAL_INFO =
        "SELECT SPATIAL_NAME AS name, SPATIAL_TWOLETTER AS two "
        + "FROM T_SPATIAL "
        + "WHERE PK_SPATIAL_ID=?";


    private static final String Q_PERIOD =
        "SELECT START_DATE AS start, END_DATE AS end "
        + "FROM T_SPATIAL_HISTORY "
        + "WHERE FK_SPATIAL_ID =? AND FK_RA_ID =?";


    private static final String Q_DELIVERIES =
        "SELECT TITLE, DELIVERY_URL "
        + "FROM T_DELIVERY "
        + "WHERE FK_SPATIAL_ID=? AND FK_RA_ID=?";


    public CountryInfoDTO getCountryInfo(String oid, String sid) throws ServiceException {
        CountryInfoDTO ret = new CountryInfoDTO();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Hashtable<String, String> hash = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Q_OBLIGATION_INFO);
            preparedStatement.setString(1, oid);
            if (isDebugMode) logQuery(Q_OBLIGATION_INFO);
            hash =  _getHashtable(preparedStatement);
            if (hash != null) {
                ret.setObligationTitle((String) hash.get("title"));
                ret.setRole((String) hash.get("role"));
            }
            preparedStatement.close();

            preparedStatement = connection.prepareStatement(Q_SPATIAL_INFO);
            preparedStatement.setString(1, sid);
            if (isDebugMode) logQuery(Q_SPATIAL_INFO);
            hash =  _getHashtable(preparedStatement);
            if (hash != null) {
                ret.setCountry((String) hash.get("name"));
                ret.setTwoLetter(((String) hash.get("two")).toLowerCase());
            }
            preparedStatement.close();

            preparedStatement = connection.prepareStatement(Q_PERIOD);
            preparedStatement.setString(1, sid);
            preparedStatement.setString(2, oid);
            if (isDebugMode) logQuery(Q_PERIOD);
            hash =  _getHashtable(preparedStatement);
            if (hash != null) {
                String start = (String) hash.get("start");
                if (start == null || start.equals("") || start.equals("00/00/0000") || start.equals("0000-00-00")) {
                    start = "Prior to start of ROD (2003)";
                } else {
                    start = "From " + start;
                }
                String end = (String) hash.get("end");
                if (end == null || end.equals("") || end.equals("00/00/0000") || start.equals("0000-00-00")) {
                    end = "present";
                }
                ret.setStart(start);
                ret.setEnd(end);
            }
            preparedStatement.close();

            List<Object> values = new ArrayList<Object>();
            values.add(sid);
            values.add(oid);
            DeliveryDTOReader rsReader = new DeliveryDTOReader();
            SQLUtil.executeQuery(Q_DELIVERIES, values, rsReader, connection);
            List<DeliveryDTO>  list = rsReader.getResultList();
            ret.setDeliveries(list);


        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }
        return ret;

    }

    private static final String Q_CHECK_TWOLETTER =
        "SELECT PK_SPATIAL_ID AS id "
            + "FROM T_SPATIAL "
            + "WHERE SPATIAL_TWOLETTER =?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#checkCountry(java.lang.String)
     */
    @Override
    public boolean checkCountry(String twoletter) throws ServiceException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Hashtable<String, String> result = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Q_CHECK_TWOLETTER);
            preparedStatement.setString(1, twoletter);
            logQuery(Q_CHECK_TWOLETTER);
            result = _getHashtable(preparedStatement);
            if (result != null && result.size() > 0) {
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

    private static final String Q_CHECK_COUNTRY_ID =
        "SELECT PK_SPATIAL_ID AS id "
        + "FROM T_SPATIAL "
        + "WHERE PK_SPATIAL_ID =?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#checkCountryById(java.lang.String)
     */
    @Override
    public boolean checkCountryById(String id) throws ServiceException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Hashtable<String, String> result = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Q_CHECK_COUNTRY_ID);
            preparedStatement.setString(1, id);
            logQuery(Q_CHECK_COUNTRY_ID);
            result = _getHashtable(preparedStatement);
            if (result != null && result.size() > 0) {
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

    /** */
    private static final String Q_MEMBER_COUNTRIES =
        "SELECT T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_SPATIAL.SPATIAL_TYPE, "
            + "T_SPATIAL.SPATIAL_TWOLETTER, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY "
            + "FROM T_SPATIAL "
            + "WHERE T_SPATIAL.SPATIAL_TYPE='C' AND T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY='Y' AND T_SPATIAL.SPATIAL_TWOLETTER <> 'EU' "
            + "ORDER BY T_SPATIAL.SPATIAL_NAME ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getMemberCountries()
     */
    @Override
    public List<CountryDTO> getMemberCountries() throws ServiceException {
        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        CountryDTOReader rsReader = new CountryDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(Q_MEMBER_COUNTRIES, values, rsReader, conn);
            List<CountryDTO>  list = rsReader.getResultList();
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
    private static final String Q_COUNTRY =
        "SELECT PK_SPATIAL_ID, SPATIAL_NAME, SPATIAL_TYPE, SPATIAL_TWOLETTER, SPATIAL_ISMEMBERCOUNTRY "
            + "FROM T_SPATIAL "
            + "WHERE PK_SPATIAL_ID = ?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getCountry(String id)
     */
    @Override
    public CountryDTO getCountry(String spatialId) throws ServiceException {

        CountryDTO ret = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            if (isDebugMode) logQuery(Q_COUNTRY);
            preparedStatement = connection.prepareStatement(Q_COUNTRY);
            preparedStatement.setString(1, spatialId);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ret = new CountryDTO();
                ret.setCountryId(rs.getInt("PK_SPATIAL_ID"));
                ret.setName(rs.getString("SPATIAL_NAME"));
                ret.setType(rs.getString("SPATIAL_TYPE"));
                ret.setTwoletter(rs.getString("SPATIAL_TWOLETTER"));
                ret.setIsMember(rs.getString("SPATIAL_ISMEMBERCOUNTRY"));
            }
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            closeAllResources(rs, preparedStatement, connection);
        }

        return ret;
    }

    private static final String Q_COUNTRY_OBLIGATIONS_LIST =
        "SELECT O.TITLE, O.PK_RA_ID, O.FK_SOURCE_ID "
            + "FROM T_OBLIGATION AS O, T_RASPATIAL_LNK AS OS "
            + "WHERE O.PK_RA_ID = OS.FK_RA_ID AND OS.FK_SPATIAL_ID = ?"
            + "ORDER BY O.TITLE ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IIssueDao#getCountryObligationsList(String spatialId)
     */
    @Override
    public List<ObligationDTO> getCountryObligationsList(String spatialId) throws ServiceException {

        List<Object> values = new ArrayList<Object>();
        values.add(spatialId);

        Connection conn = null;
        ObligationDTOReader rsReader = new ObligationDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(Q_COUNTRY_OBLIGATIONS_LIST, values, rsReader, conn);
            List<ObligationDTO>  list = rsReader.getResultList();
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
    private static final String Q_NON_MEMBER_COUNTRIES =
        "SELECT T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_SPATIAL.SPATIAL_TYPE, "
            + "T_SPATIAL.SPATIAL_TWOLETTER, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY "
            + "FROM T_SPATIAL "
            + "WHERE T_SPATIAL.SPATIAL_TYPE='C' AND T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY='N' AND T_SPATIAL.SPATIAL_TWOLETTER <> 'EU' "
            + "ORDER BY T_SPATIAL.SPATIAL_NAME ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getNonMemberCountries()
     */
    @Override
    public List<CountryDTO> getNonMemberCountries() throws ServiceException {
        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        CountryDTOReader rsReader = new CountryDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(Q_NON_MEMBER_COUNTRIES, values, rsReader, conn);
            List<CountryDTO>  list = rsReader.getResultList();
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
    private static final String Q_COUNTRIES_LIST =
        "SELECT PK_SPATIAL_ID, SPATIAL_NAME, SPATIAL_TYPE, SPATIAL_TWOLETTER, SPATIAL_ISMEMBERCOUNTRY "
            + "FROM T_SPATIAL "
            + "WHERE SPATIAL_TYPE='C' "
            + "ORDER BY SPATIAL_NAME ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getCountriesList()
     */
    @Override
    public List<CountryDTO> getCountriesList() throws ServiceException {
        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        CountryDTOReader rsReader = new CountryDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(Q_COUNTRIES_LIST, values, rsReader, conn);
            List<CountryDTO>  list = rsReader.getResultList();
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
    private static final String Q_SPATIALS_LIST =
        "SELECT PK_SPATIAL_ID, SPATIAL_NAME, SPATIAL_TYPE, SPATIAL_TWOLETTER, SPATIAL_ISMEMBERCOUNTRY "
            + "FROM T_SPATIAL "
            + "ORDER BY SPATIAL_NAME ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getSpatialsList()
     */
    @Override
    public List<CountryDTO> getSpatialsList() throws ServiceException {
        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        CountryDTOReader rsReader = new CountryDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(Q_SPATIALS_LIST, values, rsReader, conn);
            List<CountryDTO>  list = rsReader.getResultList();
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
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getCountriesList(List<String> countryIds)
     */
    @Override
    public List<CountryDTO> getObligationCountriesList(List<String> countryIds) throws ServiceException {

        StringBuilder ids = new StringBuilder();
        if (countryIds != null) {
            for (Iterator<String> it = countryIds.iterator(); it.hasNext();) {
                String id = it.next();
                ids.append(id);
                if (it.hasNext())
                    ids.append(",");
            }
        } else {
            return null;
        }

        String query = "SELECT PK_SPATIAL_ID, SPATIAL_NAME, SPATIAL_TYPE, SPATIAL_TWOLETTER, SPATIAL_ISMEMBERCOUNTRY "
            + "FROM T_SPATIAL "
            + "WHERE T_SPATIAL.PK_SPATIAL_ID IN (" + ids.toString() + ") "
            + "ORDER BY SPATIAL_NAME";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        CountryDTOReader rsReader = new CountryDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<CountryDTO>  list = rsReader.getResultList();
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
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getObligationCountriesList()
     */
    @Override
    public List<CountryDTO> getEditObligationCountriesList(String id, String voluntary) throws ServiceException {
        List<Object> values = new ArrayList<Object>();

        String qObligationCountriesList =
            "SELECT T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_SPATIAL.SPATIAL_TYPE, "
                + "T_SPATIAL.SPATIAL_TWOLETTER, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY "
                + "FROM T_SPATIAL, T_RASPATIAL_LNK "
                + "WHERE T_RASPATIAL_LNK.FK_RA_ID=" + id + " AND T_RASPATIAL_LNK.FK_SPATIAL_ID=T_SPATIAL.PK_SPATIAL_ID ";

        if (!RODUtil.isNullOrEmpty(voluntary) && voluntary.equals("N"))
            qObligationCountriesList = qObligationCountriesList + "AND T_RASPATIAL_LNK.VOLUNTARY!='Y' ";
        else if (!RODUtil.isNullOrEmpty(voluntary) && voluntary.equals("Y"))
            qObligationCountriesList = qObligationCountriesList + "AND T_RASPATIAL_LNK.VOLUNTARY='Y' AND T_SPATIAL.SPATIAL_TYPE='C' ";

        qObligationCountriesList = qObligationCountriesList + "ORDER BY T_SPATIAL.SPATIAL_NAME ";

        Connection conn = null;
        CountryDTOReader rsReader = new CountryDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(qObligationCountriesList, values, rsReader, conn);
            List<CountryDTO>  list = rsReader.getResultList();
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
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getObligationCountriesList(String id)
     */
    @Override
    public List<ObligationCountryDTO> getObligationCountriesList(String id) throws ServiceException {
        List<Object> values = new ArrayList<Object>();

        String qObligationCountriesList =
            "SELECT T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_RASPATIAL_LNK.VOLUNTARY, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY "
                + "FROM T_SPATIAL, T_RASPATIAL_LNK "
                + "WHERE T_RASPATIAL_LNK.FK_RA_ID=" + id + " AND T_RASPATIAL_LNK.FK_SPATIAL_ID=T_SPATIAL.PK_SPATIAL_ID ";

        qObligationCountriesList = qObligationCountriesList + "ORDER BY T_SPATIAL.SPATIAL_NAME ";

        Connection conn = null;
        ObligationCountryDTOReader rsReader = new ObligationCountryDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(qObligationCountriesList, values, rsReader, conn);
            List<ObligationCountryDTO>  list = rsReader.getResultList();
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

    private static final String Q_INSERT_OBLIGATION_COUNTRIES =
        "INSERT INTO T_RASPATIAL_LNK (FK_SPATIAL_ID, FK_RA_ID, VOLUNTARY) VALUES (?,?,?)";

    /* (non-Javadoc)
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#insertObligationCountries(String obligationId, List<String> selectedCountries, String voluntary)
     */
    @Override
    public void insertObligationCountries(String obligationId, List<String> selectedCountries, String voluntary) throws ServiceException {
        List<Object> values = null;
        Connection conn = null;
        try {
            conn = getConnection();
            for (Iterator<String> it = selectedCountries.iterator(); it.hasNext();) {
                String countryId = it.next();
                values = new ArrayList<Object>();
                values.add(countryId);
                values.add(obligationId);
                values.add(voluntary);
                SQLUtil.executeUpdate(Q_INSERT_OBLIGATION_COUNTRIES, values, conn);
            }
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
