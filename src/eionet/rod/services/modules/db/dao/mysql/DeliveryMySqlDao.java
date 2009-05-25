package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import eionet.rod.RODUtil;
import eionet.rod.dto.CountryDeliveryDTO;
import eionet.rod.dto.CountryDeliveryDataDTO;
import eionet.rod.dto.readers.CountryDeliveryDTOReader;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IDeliveryDao;
import eionet.rod.util.sql.SQLUtil;

public class DeliveryMySqlDao extends MySqlBaseDao implements IDeliveryDao {

	private static String titlePred = (String) properties.get(FileServiceIF.CONTREG_TITLE_PREDICATE);

	private static String typePred = (String) properties.get(FileServiceIF.CONTREG_TYPE_PREDICATE);

	private static String datePred = (String) properties.get(FileServiceIF.CONTREG_DATE_PREDICATE);

	private static String identifierPred = (String) properties.get(FileServiceIF.CONTREG_IDENTIFIER_PREDICATE);

	private static String formatPred = (String) properties.get(FileServiceIF.CONTREG_FORMAT_PREDICATE);

	private static String coveragePred = (String) properties.get(FileServiceIF.CONTREG_COVERAGE_PREDICATE);

	private static String countryPred = (String) properties.get(FileServiceIF.COUNTRY_NAMESPACE);

	private static DateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	private static final String qSaveDeliveries = 
		"INSERT INTO T_DELIVERY (TITLE,RA_URL,TYPE,FORMAT,COVERAGE,STATUS,UPLOAD_DATE,DELIVERY_URL,FK_SPATIAL_ID,FK_RA_ID) " + 
		"VALUES (?,?,?,?,?,?,?,?,?,?)";

	public DeliveryMySqlDao() {
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IDeliveryDao#saveDeliveries(java.lang.Integer,
	 *      java.util.Vector, java.util.HashMap)
	 */
	public void saveDeliveries(Integer raId, Vector deliveries, HashMap cMap) throws ServiceException {

		Vector cIds = new Vector();
		String countryIds = "";
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(qSaveDeliveries);

			for (int ii = 0; ii < deliveries.size(); ii++) {
				// deliveryKEY:STRING(url), VALUE:HASH (metadata)
				Hashtable delivery = (Hashtable) deliveries.elementAt(ii);
				if (delivery != null && delivery.size() > 0) {
					// Hash contains only one member that's delivery data
					String crId = (String) (delivery.keys().nextElement());
					Hashtable metaData = (Hashtable) delivery.get(crId);

					String identifier = cnvVector((Vector) metaData.get(identifierPred), ",");
					String title = cnvVector((Vector) metaData.get(titlePred), ",");
					String sdate = cnvVector((Vector) metaData.get(datePred), ",");

					String type = cnvVector((Vector) metaData.get(typePred), ",");
					String format = cnvVector((Vector) metaData.get(formatPred), ",");
					String coverage = cnvVector((Vector) metaData.get(coveragePred), " ");
					String country = cnvVector((Vector) metaData.get(countryPred), " ");

					// Each hash contains Strings as Keys and Vectors as values
					Integer countryId = getCountryId(country, cMap, connection);

					if (countryId == null)
						logger.info("!!! Delivery not saved, Country is not in T_SPATIAL " + country +", "
						+ "Identifier: " + identifier + ", "
						+ "Title: " + title + ", "
						+ "Date: " + sdate + ", "
						+ "Type: " + type + ", "
						+ "Format: " + format + ", "
						+ "Coverage: " + coverage + ", "
						+ "raID: " + raId);
					else {
						
						Date date = null;
						try{
							date = isoDateFormat.parse(sdate);
						}catch(ParseException pe){}
						
						// java.sql.Date jdbcDate = null;

						preparedStatement.setString(1, (title == null) ? "" : title); // TITLE
						preparedStatement.setString(2, "http://rod.eionet.europa.eu/"+raId); // TITLE
						preparedStatement.setString(3, ((type == null) ? "" : type)); // TYPE
						preparedStatement.setString(4, ((format == null) ? "" : format)); // FORMAT
						preparedStatement.setString(5, ((coverage == null) ? "" : coverage));// COVERAGE
						preparedStatement.setInt(6, 1); // STATUS
						preparedStatement.setTimestamp(7, date != null ? new Timestamp(date.getTime()):null);// UPLOAD_DATE
						preparedStatement.setString(8, identifier); // DELIVERY_URL
						preparedStatement.setInt(9, countryId.intValue()); // FK_SPATIAL_ID
						preparedStatement.setInt(10, raId.intValue()); // FK_RA_ID
						if (isDebugMode) logQuery(qSaveDeliveries);
						preparedStatement.executeUpdate();
						// (TITLE,TYPE,FORMAT,COVERAGE,STATUS,UPLOAD_DATE,DELIVERY_URL,FK_SPATIAL_ID,FK_RA_ID)
						// " + "VALUES = (?,?,?,?,?,?,?,?,?)";
						if (!cIds.contains(countryId)) cIds.add(countryId.toString());
					}
				}
			}

			if (cIds.size() > 0)
				countryIds = "," + cnvVector(cIds, ",") + ",";
			else
				countryIds = "";

			markCountries(raId, countryIds, connection);

		} catch (Exception e) {
			e.printStackTrace();
			rollBackDeliveries(raId);
			throw new ServiceException("Harvesting deliveries for RA: " + raId + " failed with reason " + e.toString());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

	}

	private static final String qRollBackDeliveriesDelete = 
		"DELETE " + 
		"FROM T_DELIVERY " + 
		"WHERE STATUS=1 AND FK_RA_ID=?";

	private static final String qRollBackDeliveriesUpdate = 
		"UPDATE T_DELIVERY " + 
		"SET STATUS=1 " + 
		"WHERE STATUS=0 AND FK_RA_ID=?";

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
			if (isDebugMode) logQuery(qRollBackDeliveriesDelete);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			preparedStatement = connection.prepareStatement(qRollBackDeliveriesUpdate);
			preparedStatement.setInt(1, raId.intValue());
			if (isDebugMode) logQuery(qRollBackDeliveriesUpdate);
			preparedStatement.executeUpdate();

		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

	}

	private static final String qCommitDeliveries = 
		"DELETE " + 
		"FROM T_DELIVERY " + 
		"WHERE STATUS=0";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IDeliveryDao#commitDeliveries()
	 */
	public void commitDeliveries() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(qCommitDeliveries);
			if (isDebugMode) logQuery(qCommitDeliveries);
			preparedStatement.executeUpdate();

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
			if (isDebugMode) logQuery(qBackUpDeliveries);
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
    		"SELECT T_DELIVERY.PK_DELIVERY_ID, T_DELIVERY.FK_RA_ID, T_DELIVERY.FK_SPATIAL_ID, T_DELIVERY.TITLE, ");
		q_obligations_list.append("T_DELIVERY.DELIVERY_URL, T_DELIVERY.UPLOAD_DATE, T_DELIVERY.TYPE AS DELIVERY_TYPE, T_DELIVERY.FORMAT, T_DELIVERY.COVERAGE, " +
			"T_OBLIGATION.PK_RA_ID, T_OBLIGATION.FK_SOURCE_ID, T_OBLIGATION.TITLE AS OBLIGATION_TITLE, T_OBLIGATION.REPORT_FREQ_MONTHS, " +
			"T_OBLIGATION.TERMINATE, T_OBLIGATION.NEXT_DEADLINE, T_OBLIGATION.REPORT_FORMAT_URL, T_OBLIGATION.RESPONSIBLE_ROLE, " +
			"T_OBLIGATION.FORMAT_NAME, T_OBLIGATION.FK_DELIVERY_COUNTRY_IDS, T_OBLIGATION.PARAMETERS, " +
			"T_SPATIAL.PK_SPATIAL_ID, T_SPATIAL.SPATIAL_NAME, T_SPATIAL.SPATIAL_TWOLETTER, T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY, " +
			"T_ROLE.ROLE_NAME, T_ROLE.ROLE_URL, T_ROLE.ROLE_MEMBERS_URL, " +
			"T_CLIENT_LNK.FK_CLIENT_ID, T_CLIENT_LNK.FK_OBJECT_ID, T_CLIENT_LNK.STATUS, T_CLIENT_LNK.TYPE, " +
			"T_CLIENT.PK_CLIENT_ID, T_CLIENT.CLIENT_NAME " +
			"FROM T_DELIVERY JOIN T_OBLIGATION ON T_DELIVERY.FK_RA_ID=T_OBLIGATION.PK_RA_ID " +
			"JOIN T_SPATIAL ON T_SPATIAL.PK_SPATIAL_ID=T_DELIVERY.FK_SPATIAL_ID " +
			"LEFT JOIN T_ROLE ON CONCAT(T_OBLIGATION.RESPONSIBLE_ROLE,'-',IF(T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY='Y','mc','cc'),'-',LCASE(T_SPATIAL.SPATIAL_TWOLETTER))=T_ROLE.ROLE_ID " +
			"LEFT JOIN T_CLIENT_LNK ON T_CLIENT_LNK.TYPE='A' AND T_CLIENT_LNK.STATUS='M' AND T_CLIENT_LNK.FK_OBJECT_ID=T_OBLIGATION.PK_RA_ID " +
			"LEFT JOIN T_CLIENT ON T_CLIENT_LNK.FK_CLIENT_ID=T_CLIENT.PK_CLIENT_ID "+
			"WHERE T_DELIVERY.FK_RA_ID=" + actDetailsId);
		
		if(!RODUtil.isNullOrEmpty(spatialId))
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
		try{
			conn = getConnection();
			SQLUtil.executeQuery(query, values, rsReader, conn);
			List<CountryDeliveryDTO>  list = rsReader.getResultList();
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
    
    private static final String q_delivery_data =
		"SELECT T_OBLIGATION.PK_RA_ID, T_OBLIGATION.TITLE, T_OBLIGATION.LAST_HARVESTED, T_OBLIGATION.TERMINATE, " +
		"T_OBLIGATION.REPORT_FREQ_MONTHS, T_OBLIGATION.NEXT_DEADLINE, T_OBLIGATION.FORMAT_NAME, T_OBLIGATION.REPORT_FORMAT_URL, " +
		"T_CLIENT_LNK.FK_CLIENT_ID, T_CLIENT_LNK.FK_OBJECT_ID, T_CLIENT_LNK.TYPE, T_CLIENT_LNK.STATUS, " +			
		"T_CLIENT.PK_CLIENT_ID, T_CLIENT.CLIENT_NAME " +
		"FROM T_OBLIGATION JOIN T_CLIENT_LNK ON T_CLIENT_LNK.TYPE='A' AND T_CLIENT_LNK.STATUS='M' AND T_CLIENT_LNK.FK_OBJECT_ID=T_OBLIGATION.PK_RA_ID " +
		"LEFT JOIN T_CLIENT ON T_CLIENT_LNK.FK_CLIENT_ID=T_CLIENT.PK_CLIENT_ID " +
		"WHERE T_OBLIGATION.PK_RA_ID=?";
    
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
			if (isDebugMode) logQuery(q_delivery_data);
			preparedStatement = connection.prepareStatement(q_delivery_data);
			preparedStatement.setString(1,actDetailsId);
			rs = preparedStatement.executeQuery();
			while(rs.next()){
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
