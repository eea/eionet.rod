package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import eionet.rod.util.sql.SQLUtil;
import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.readers.CountryDTOReader;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.ISpatialDao;

public class SpatialMySqlDao extends MySqlBaseDao implements ISpatialDao {

	public SpatialMySqlDao() {
	}

	private static final String q_countries = 
		"SELECT " + 
			"{fn concat(?, PK_SPATIAL_ID)}  AS uri, " + 
			"SPATIAL_NAME AS name, " + 
			"UPPER(SPATIAL_TWOLETTER) AS iso " + 
		"FROM T_SPATIAL " +
		"WHERE SPATIAL_TYPE='C' " + 
		"ORDER BY SPATIAL_NAME ";

	/* (non-Javadoc)
	 * @see eionet.rod.services.modules.db.dao.ISpatialDao#getCountries()
	 */
	public Vector getCountries() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_countries);
			preparedStatement.setString(1, (String) properties.get(FileServiceIF.SPATIAL_NAMESPACE));
			if (isDebugMode) logQuery(q_countries);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new Vector();
	}

	private static final String q_country_by_id = 
		"SELECT SPATIAL_NAME AS name " + 
		"FROM T_SPATIAL " + 
		"WHERE PK_SPATIAL_ID =?";

	/* (non-Javadoc)
	 * @see eionet.rod.services.modules.db.dao.ISpatialDao#getCountryById(int)
	 */
	public String getCountryById(int id) throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String result = null;
		String[][] m = null;
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_country_by_id);
			preparedStatement.setInt(1, id);
			if (isDebugMode) logQuery(q_country_by_id);
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

	private static final String q_counties_by_raid = 
		"SELECT sl.FK_SPATIAL_ID " + 
		"FROM T_RASPATIAL_LNK sl, T_SPATIAL s " + 
		"WHERE s.SPATIAL_TYPE='C' " + 
			"AND sl.FK_SPATIAL_ID=s.PK_SPATIAL_ID " + 
			"AND sl.FK_RA_ID=? " + 
		"ORDER BY FK_SPATIAL_ID";

	/* (non-Javadoc)
	 * @see eionet.rod.services.modules.db.dao.ISpatialDao#getCountries(int)
	 */
	public String[][] getCountries(int raId) throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String[][] result = null;
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_counties_by_raid);
			preparedStatement.setInt(1, raId);
			if (isDebugMode) logQuery(q_counties_by_raid);
			result = _executeStringQuery(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new String[][] {};

	}

	private static final String q_obligation_counties = 
		"SELECT s.SPATIAL_NAME AS name " + 
		"FROM T_SPATIAL s, T_RASPATIAL_LNK r " + 
		"WHERE r.FK_RA_ID = ? AND s.PK_SPATIAL_ID = r.FK_SPATIAL_ID " + 
		"ORDER BY s.SPATIAL_NAME";

	/* (non-Javadoc)
	 * @see eionet.rod.services.modules.db.dao.ISpatialDao#getObligationCountries(int)
	 */
	public Vector getObligationCountries(int id) throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_obligation_counties);
			preparedStatement.setInt(1, id);
			if (isDebugMode) logQuery(q_obligation_counties);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new Vector();

	}
	
	private static final String q_country_id_pairs = 
		"SELECT PK_SPATIAL_ID, SPATIAL_NAME " + 
		"FROM T_SPATIAL " + 
		"WHERE SPATIAL_TYPE ='C' " + 
		"ORDER BY SPATIAL_NAME";

	/* (non-Javadoc)
	 * @see eionet.rod.services.modules.db.dao.ISpatialDao#getCountryIdPairs()
	 */
	public String[][] getCountryIdPairs() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String[][] result = null;
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_country_id_pairs);
			if (isDebugMode) logQuery(q_country_id_pairs);
			result = _executeStringQuery(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new String[][] {};
	}


	private static final String q_obligationInfo = 
		"SELECT TITLE AS title, RESPONSIBLE_ROLE AS role " +
		"FROM T_OBLIGATION " +
		"WHERE PK_RA_ID=? " ;
	
	private static final String q_spatialinfo = 
		"SELECT SPATIAL_NAME AS name, SPATIAL_TWOLETTER AS two " +
		"FROM T_SPATIAL " +
		"WHERE PK_SPATIAL_ID=?";
	
	
	private static final String q_period =	
		"SELECT START_DATE AS start, END_DATE AS end " +
		"FROM T_SPATIAL_HISTORY " +
		"WHERE FK_SPATIAL_ID =? AND FK_RA_ID =?" ;
	
	
	private static final String q_deliveries =
		"SELECT PK_DELIVERY_ID AS id, TITLE AS title, DELIVERY_URL AS url " +
		"FROM T_DELIVERY " +
		"WHERE FK_SPATIAL_ID=? AND FK_RA_ID=?";
	

	public Hashtable getCountryInfo(int ra_id, int spatial_id) throws ServiceException {
		Hashtable ret = new Hashtable();
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_obligationInfo);
			preparedStatement.setInt(1, ra_id);
			if (isDebugMode) logQuery(q_obligationInfo);
			ret.put("obligationinfo", _getHashtable(preparedStatement));
			preparedStatement.close();

			preparedStatement = connection.prepareStatement(q_spatialinfo);
			preparedStatement.setInt(1, spatial_id);
			if (isDebugMode) logQuery(q_spatialinfo);
			ret.put("spatialinfo", _getHashtable(preparedStatement));
			preparedStatement.close();
			
			preparedStatement = connection.prepareStatement(q_period);
			preparedStatement.setInt(1, spatial_id);
			preparedStatement.setInt(2, ra_id);
			if (isDebugMode) logQuery(q_period);
			ret.put("period", _getHashtable(preparedStatement));
			preparedStatement.close();
			
			preparedStatement = connection.prepareStatement(q_deliveries);
			preparedStatement.setInt(1, spatial_id);
			preparedStatement.setInt(2, ra_id);
			if (isDebugMode) logQuery(q_deliveries);
			ret.put("deliveries", _getVectorOfHashes(preparedStatement));
			
			
			
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}
		return ret;
		
	}
	
	private static final String q_check_twoletter = 
		"SELECT PK_SPATIAL_ID AS id " + 
		"FROM T_SPATIAL " + 
		"WHERE SPATIAL_TWOLETTER =?";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.ISpatialDao#checkCountry(java.lang.String)
	 */
	public boolean checkCountry(String twoletter) throws ServiceException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Hashtable result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_check_twoletter);
			preparedStatement.setString(1, twoletter);
			logQuery(q_check_twoletter);
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
	
	private static final String q_check_countryid = 
		"SELECT PK_SPATIAL_ID AS id " + 
		"FROM T_SPATIAL " + 
		"WHERE PK_SPATIAL_ID =?";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.ISpatialDao#checkCountryById(java.lang.String)
	 */
	public boolean checkCountryById(String id) throws ServiceException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Hashtable result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_check_countryid);
			preparedStatement.setString(1, id);
			logQuery(q_check_countryid);
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
	
	/** */
	private static final String q_member_countries = 
		"SELECT T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_RASPATIAL_LNK.VOLUNTARY, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY " +
		"FROM T_SPATIAL, T_RASPATIAL_LNK " +
		"WHERE T_SPATIAL.SPATIAL_TYPE='C' AND T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY='Y' AND T_SPATIAL.SPATIAL_TWOLETTER <> 'EU' " +
		"AND T_RASPATIAL_LNK.FK_SPATIAL_ID=T_SPATIAL.PK_SPATIAL_ID " + 
		"ORDER BY T_SPATIAL.SPATIAL_NAME ";
		
	/*
     * (non-Javadoc)
     * 
     * @see eionet.rod.dao.ISpatialDao#getMemberCountries()
     */
    public List<CountryDTO> getMemberCountries() throws ServiceException {
    	List<Object> values = new ArrayList<Object>();
				
		Connection conn = null;
		CountryDTOReader rsReader = new CountryDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(q_member_countries, values, rsReader, conn);
			List<CountryDTO>  list = rsReader.getResultList();
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
    
    /** */
	private static final String q_non_member_countries = 
		"SELECT T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_RASPATIAL_LNK.VOLUNTARY, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY " +
		"FROM T_SPATIAL, T_RASPATIAL_LNK " +
		"WHERE T_SPATIAL.SPATIAL_TYPE='C' AND T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY='N' AND T_SPATIAL.SPATIAL_TWOLETTER <> 'EU' " +
		"AND T_RASPATIAL_LNK.FK_SPATIAL_ID=T_SPATIAL.PK_SPATIAL_ID " + 
		"ORDER BY T_SPATIAL.SPATIAL_NAME ";
		
	/*
     * (non-Javadoc)
     * 
     * @see eionet.rod.dao.ISpatialDao#getNonMemberCountries()
     */
    public List<CountryDTO> getNonMemberCountries() throws ServiceException {
    	List<Object> values = new ArrayList<Object>();
				
		Connection conn = null;
		CountryDTOReader rsReader = new CountryDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(q_non_member_countries, values, rsReader, conn);
			List<CountryDTO>  list = rsReader.getResultList();
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
    
    /** */
	private static final String q_countries_list = 
		"SELECT T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_RASPATIAL_LNK.VOLUNTARY, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY " +
		"FROM T_SPATIAL, T_RASPATIAL_LNK " +
		"WHERE T_SPATIAL.SPATIAL_TYPE='C' AND T_RASPATIAL_LNK.FK_SPATIAL_ID=T_SPATIAL.PK_SPATIAL_ID " + 
		"ORDER BY T_SPATIAL.SPATIAL_NAME ";
		
	/*
     * (non-Javadoc)
     * 
     * @see eionet.rod.dao.ISpatialDao#getCountriesList()
     */
    public List<CountryDTO> getCountriesList() throws ServiceException {
    	List<Object> values = new ArrayList<Object>();
				
		Connection conn = null;
		CountryDTOReader rsReader = new CountryDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(q_countries_list, values, rsReader, conn);
			List<CountryDTO>  list = rsReader.getResultList();
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
     * @see eionet.rod.dao.ISpatialDao#getObligationCountriesList()
     */
    public List<CountryDTO> getObligationCountriesList(String id) throws ServiceException {
    	List<Object> values = new ArrayList<Object>();
    	
    	String q_obligation_countries_list = 
    		"SELECT T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_RASPATIAL_LNK.VOLUNTARY, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY " +
    		"FROM T_SPATIAL, T_RASPATIAL_LNK " +
    		"WHERE T_RASPATIAL_LNK.FK_RA_ID=" + id + " AND T_RASPATIAL_LNK.FK_SPATIAL_ID=T_SPATIAL.PK_SPATIAL_ID " + 
    		"ORDER BY T_SPATIAL.SPATIAL_NAME ";
				
		Connection conn = null;
		CountryDTOReader rsReader = new CountryDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(q_obligation_countries_list, values, rsReader, conn);
			List<CountryDTO>  list = rsReader.getResultList();
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
