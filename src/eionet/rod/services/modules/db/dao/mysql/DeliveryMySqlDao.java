package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import eionet.rod.services.FileServiceIF;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IDeliveryDao;

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
		"INSERT INTO T_DELIVERY (TITLE,TYPE,FORMAT,COVERAGE,STATUS,UPLOAD_DATE,DELIVERY_URL,FK_SPATIAL_ID,FK_RA_ID) " + 
		"VALUES (?,?,?,?,?,?,?,?,?)";

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
						preparedStatement.setString(2, ((type == null) ? "" : type)); // TYPE
						preparedStatement.setString(3, ((format == null) ? "" : format)); // FORMAT
						preparedStatement.setString(4, ((coverage == null) ? "" : coverage));// COVERAGE
						preparedStatement.setInt(5, 1); // STATUS
						preparedStatement.setTimestamp(6, date != null ? new Timestamp(date.getTime()):null);// UPLOAD_DATE
						preparedStatement.setString(7, identifier); // DELIVERY_URL
						preparedStatement.setInt(8, countryId.intValue()); // FK_SPATIAL_ID
						preparedStatement.setInt(9, raId.intValue()); // FK_RA_ID
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

}
