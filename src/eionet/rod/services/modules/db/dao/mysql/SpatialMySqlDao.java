package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import eionet.rod.RODUtil;
import eionet.rod.util.sql.SQLUtil;
import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.CountryInfoDTO;
import eionet.rod.dto.DeliveryDTO;
import eionet.rod.dto.ObligationCountryDTO;
import eionet.rod.dto.ObligationDTO;
import eionet.rod.dto.ObligationFactsheetDTO;
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
		"SELECT PK_DELIVERY_ID, TITLE, DELIVERY_URL " +
		"FROM T_DELIVERY " +
		"WHERE FK_SPATIAL_ID=? AND FK_RA_ID=?";
	

	public CountryInfoDTO getCountryInfo(String oid, String sid) throws ServiceException {
		CountryInfoDTO ret = new CountryInfoDTO();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Hashtable hash = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_obligationInfo);
			preparedStatement.setString(1, oid);
			if (isDebugMode) logQuery(q_obligationInfo);
			hash =  _getHashtable(preparedStatement);
			if(hash != null){
				ret.setObligationTitle((String)hash.get("title"));
				ret.setRole((String)hash.get("role"));
			}
			preparedStatement.close();

			preparedStatement = connection.prepareStatement(q_spatialinfo);
			preparedStatement.setString(1, sid);
			if (isDebugMode) logQuery(q_spatialinfo);
			hash =  _getHashtable(preparedStatement);
			if(hash != null){
				ret.setCountry((String)hash.get("name"));
				ret.setTwoLetter(((String)hash.get("two")).toLowerCase());
			}
			preparedStatement.close();
			
			preparedStatement = connection.prepareStatement(q_period);
			preparedStatement.setString(1, sid);
			preparedStatement.setString(2, oid);
			if (isDebugMode) logQuery(q_period);
			hash =  _getHashtable(preparedStatement);
			if(hash != null){
				String start = (String)hash.get("start");
				if(start == null || start.equals("") || start.equals("00/00/0000") || start.equals("0000-00-00")){
					start = "Prior to start of ROD (2003)";
				} else {
					start = "From " + start;	
				}
				String end = (String)hash.get("end");
				if(end == null || end.equals("") || end.equals("00/00/0000") || start.equals("0000-00-00")){
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
			SQLUtil.executeQuery(q_deliveries, values, rsReader, connection);
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
		"SELECT T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_SPATIAL.SPATIAL_TYPE, T_SPATIAL.SPATIAL_TWOLETTER, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY " +
		"FROM T_SPATIAL " +
		"WHERE T_SPATIAL.SPATIAL_TYPE='C' AND T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY='Y' AND T_SPATIAL.SPATIAL_TWOLETTER <> 'EU' " +
		"ORDER BY T_SPATIAL.SPATIAL_NAME ";
		
	/*
     * (non-Javadoc)
     * 
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getMemberCountries()
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
	private static final String q_country = 
		"SELECT PK_SPATIAL_ID, SPATIAL_NAME, SPATIAL_TYPE, SPATIAL_TWOLETTER, SPATIAL_ISMEMBERCOUNTRY " +
		"FROM T_SPATIAL " +
		"WHERE PK_SPATIAL_ID = ?";
		
	/*
     * (non-Javadoc)
     * 
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getCountry(String id)
     */
    public CountryDTO getCountry(String spatialId) throws ServiceException {
    	
    	CountryDTO ret = null;
    	
    	Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_country);
			preparedStatement = connection.prepareStatement(q_country);
			preparedStatement.setString(1, spatialId);
			rs = preparedStatement.executeQuery();
			while(rs.next()){
				ret = new CountryDTO();
				ret.setCountryId(rs.getInt("PK_SPATIAL_ID"));
				ret.setName(rs.getString("SPATIAL_NAME"));
				ret.setType(rs.getString("SPATIAL_TYPE"));
				ret.setTwoletter(rs.getString("SPATIAL_TWOLETTER"));
				ret.setIsMember(rs.getString("SPATIAL_ISMEMBERCOUNTRY"));
			}
		}
		catch (Exception e){
			logger.error(e);
			throw new ServiceException(e.getMessage());
		}
		finally{
			closeAllResources(rs, preparedStatement, connection);
		}
		
		return ret;
    }
    
    private static final String qCountryObligationsList = 
		"SELECT O.TITLE, O.PK_RA_ID, O.FK_SOURCE_ID " + 
		"FROM T_OBLIGATION AS O, T_RASPATIAL_LNK AS OS " +
		"WHERE O.PK_RA_ID = OS.FK_RA_ID AND OS.FK_SPATIAL_ID = ?" + 
		"ORDER BY O.TITLE ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IIssueDao#getCountryObligationsList(String spatialId)
	 */
	public List<ObligationDTO> getCountryObligationsList(String spatialId) throws ServiceException {

		List<Object> values = new ArrayList<Object>();
		values.add(spatialId);
		
		Connection conn = null;
		ObligationDTOReader rsReader = new ObligationDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(qCountryObligationsList, values, rsReader, conn);
			List<ObligationDTO>  list = rsReader.getResultList();
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
		"SELECT T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_SPATIAL.SPATIAL_TYPE, T_SPATIAL.SPATIAL_TWOLETTER, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY " +
		"FROM T_SPATIAL " +
		"WHERE T_SPATIAL.SPATIAL_TYPE='C' AND T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY='N' AND T_SPATIAL.SPATIAL_TWOLETTER <> 'EU' " +
		"ORDER BY T_SPATIAL.SPATIAL_NAME ";
		
	/*
     * (non-Javadoc)
     * 
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getNonMemberCountries()
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
		"SELECT PK_SPATIAL_ID, SPATIAL_NAME, SPATIAL_TYPE, SPATIAL_TWOLETTER, SPATIAL_ISMEMBERCOUNTRY " +
		"FROM T_SPATIAL " +
		"WHERE SPATIAL_TYPE='C' " + 
		"ORDER BY SPATIAL_NAME ";
		
	/*
     * (non-Javadoc)
     * 
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getCountriesList()
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
    
    /** */
	private static final String q_spatials_list = 
		"SELECT PK_SPATIAL_ID, SPATIAL_NAME, SPATIAL_TYPE, SPATIAL_TWOLETTER, SPATIAL_ISMEMBERCOUNTRY " +
		"FROM T_SPATIAL " +
		"ORDER BY SPATIAL_NAME ";
		
	/*
     * (non-Javadoc)
     * 
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getSpatialsList()
     */
    public List<CountryDTO> getSpatialsList() throws ServiceException {
    	List<Object> values = new ArrayList<Object>();
				
		Connection conn = null;
		CountryDTOReader rsReader = new CountryDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(q_spatials_list, values, rsReader, conn);
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
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getCountriesList(List<String> countryIds)
     */
    public List<CountryDTO> getObligationCountriesList(List<String> countryIds) throws ServiceException {
    	
    	StringBuilder ids = new StringBuilder();
    	if(countryIds != null){
	    	for(Iterator<String> it = countryIds.iterator(); it.hasNext(); ){
	    		String id = it.next();
	    		ids.append(id);
	    		if(it.hasNext())
	    			ids.append(",");
	    	}
    	} else {
    		return null;
    	}
    	
    	String query = "SELECT PK_SPATIAL_ID, SPATIAL_NAME, SPATIAL_TYPE, SPATIAL_TWOLETTER, SPATIAL_ISMEMBERCOUNTRY " +
    		"FROM T_SPATIAL " +
    		"WHERE T_SPATIAL.PK_SPATIAL_ID IN ("+ids.toString()+") " +
    		"ORDER BY SPATIAL_NAME";
    	
    	List<Object> values = new ArrayList<Object>();
    	    			
		Connection conn = null;
		CountryDTOReader rsReader = new CountryDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(query, values, rsReader, conn);
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
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getObligationCountriesList()
     */
    public List<CountryDTO> getEditObligationCountriesList(String id, String voluntary) throws ServiceException {
    	List<Object> values = new ArrayList<Object>();
    	
    	String q_obligation_countries_list = 
    		"SELECT T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_SPATIAL.SPATIAL_TYPE, T_SPATIAL.SPATIAL_TWOLETTER, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY " +
    		"FROM T_SPATIAL, T_RASPATIAL_LNK " +
    		"WHERE T_RASPATIAL_LNK.FK_RA_ID=" + id + " AND T_RASPATIAL_LNK.FK_SPATIAL_ID=T_SPATIAL.PK_SPATIAL_ID ";
    	
    	if(!RODUtil.isNullOrEmpty(voluntary) && voluntary.equals("N"))
    		q_obligation_countries_list = q_obligation_countries_list + "AND T_RASPATIAL_LNK.VOLUNTARY!='Y' ";
    	else if(!RODUtil.isNullOrEmpty(voluntary) && voluntary.equals("Y"))
    		q_obligation_countries_list = q_obligation_countries_list + "AND T_RASPATIAL_LNK.VOLUNTARY='Y' AND T_SPATIAL.SPATIAL_TYPE='C' ";
    		
    	q_obligation_countries_list = q_obligation_countries_list + "ORDER BY T_SPATIAL.SPATIAL_NAME ";
				
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
    
    /*
     * (non-Javadoc)
     * 
     * @see eionet.rod.services.modules.db.dao.ISpatialDao#getObligationCountriesList(String id)
     */
    public List<ObligationCountryDTO> getObligationCountriesList(String id) throws ServiceException {
    	List<Object> values = new ArrayList<Object>();
    	
    	String q_obligation_countries_list = 
    		"SELECT T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_RASPATIAL_LNK.VOLUNTARY, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY " +
    		"FROM T_SPATIAL, T_RASPATIAL_LNK " +
    		"WHERE T_RASPATIAL_LNK.FK_RA_ID=" + id + " AND T_RASPATIAL_LNK.FK_SPATIAL_ID=T_SPATIAL.PK_SPATIAL_ID ";
    	
    	q_obligation_countries_list = q_obligation_countries_list + "ORDER BY T_SPATIAL.SPATIAL_NAME ";
				
		Connection conn = null;
		ObligationCountryDTOReader rsReader = new ObligationCountryDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(q_obligation_countries_list, values, rsReader, conn);
			List<ObligationCountryDTO>  list = rsReader.getResultList();
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
    
    private static final String q_insert_obligation_countries = 
		"INSERT INTO T_RASPATIAL_LNK (FK_SPATIAL_ID, FK_RA_ID, VOLUNTARY) VALUES (?,?,?)";

	/* (non-Javadoc)
	 * @see eionet.rod.services.modules.db.dao.ISpatialDao#insertObligationCountries(String obligationId, List<String> selectedCountries, String voluntary)
	 */
	public void insertObligationCountries(String obligationId, List<String> selectedCountries, String voluntary) throws ServiceException {
		List<Object> values = null;
		Connection conn = null;
		try{
			conn = getConnection();
			for(Iterator<String> it = selectedCountries.iterator(); it.hasNext();){
				String countryId = it.next();
				values = new ArrayList<Object>();
				values.add(countryId);
				values.add(obligationId);
				values.add(voluntary);
				SQLUtil.executeUpdate(q_insert_obligation_countries, values, conn);
			}
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
