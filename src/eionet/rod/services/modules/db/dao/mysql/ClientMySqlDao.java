package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import eionet.rod.dto.CountryDeliveryDTO;
import eionet.rod.dto.ClientDTO;
import eionet.rod.dto.IssueDTO;
import eionet.rod.dto.readers.CountryDeliveryDTOReader;
import eionet.rod.dto.readers.ClientDTOReader;
import eionet.rod.dto.readers.IssueDTOReader;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IClientDao;
import eionet.rod.util.sql.SQLUtil;

public class ClientMySqlDao extends MySqlBaseDao implements IClientDao {

	
	private static final String qOrganisations = 
		"SELECT " +
			"CONCAT(CLIENT_ACRONYM,'-',CLIENT_NAME) AS name," + 
			"PK_CLIENT_ID AS id," + 
			" {fn concat(?,PK_CLIENT_ID) }  AS uri " + 
		"FROM T_CLIENT " + 
		"ORDER BY name ";
    
    private static final String qGetOrganisationNameByID = 
        "SELECT " +
            "CONCAT(CLIENT_ACRONYM,'-',CLIENT_NAME) AS name " + 
        "FROM T_CLIENT " +
        "WHERE PK_CLIENT_ID = ? ";

	
	private static final String getObligationOrg = 
		"SELECT c.CLIENT_NAME AS name " + 
		"FROM T_CLIENT c, T_OBLIGATION o " + 
		"WHERE o.PK_RA_ID = ? " + 
			"AND c.PK_CLIENT_ID = o.FK_CLIENT_ID " + 
		"ORDER BY c.CLIENT_NAME";

	public ClientMySqlDao() {
	}
	
    /*
     * (non-Javadoc)
     * 
     * @see eionet.rod.services.modules.db.dao.IClientDao#getOrganisationNameByID()
     */
    public String getOrganisationNameByID(String clientId) throws ServiceException {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        String[][] result = null;
        String res = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(qGetOrganisationNameByID);
            preparedStatement.setString(1, clientId);
            if (isDebugMode) logQuery(qGetOrganisationNameByID);
            resultSet = preparedStatement.executeQuery();
            result = getResults(resultSet);
            resultSet.close();
            preparedStatement.close();
            if(result.length > 0){
                res = result[0][0];
            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(resultSet, preparedStatement, connection);
        }

        return res != null ? res : "";
    }
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IClientDao#getOrganisations()
	 */
	public Vector getOrganisations() throws ServiceException {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(qOrganisations);
			preparedStatement.setString(1, (String) properties.get(FileServiceIF.ORGANISATION_NAMESPACE));
			if (isDebugMode) logQuery(qOrganisations);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(resultSet, preparedStatement, connection);
		}

		return result != null ? result : new Vector();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IClientDao#getObligationOrg(java.lang.Integer)
	 */
	public Vector getObligationOrg(Integer obligationId) throws ServiceException {

		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(getObligationOrg);
			preparedStatement.setInt(1, obligationId.intValue());
			if (isDebugMode) logQuery(getObligationOrg);
			result = _getVectorOfHashes(preparedStatement);
		} catch (Exception exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(resultSet, preparedStatement, connection);
		}

		return result != null ? result : new Vector();
	}

	private static final String q_delete_obligation_link =
		"DELETE FROM T_CLIENT_LNK " +
		"WHERE TYPE='A' AND FK_OBJECT_ID=?";
		
	public void deleteObligationLink(Integer raId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_obligation_link);
			preparedStatement = connection.prepareStatement(q_delete_obligation_link);
			preparedStatement.setInt(1, raId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	}
	
	private static final String q_insert_client_link = 
		"INSERT INTO T_CLIENT_LNK (FK_CLIENT_ID, FK_OBJECT_ID, STATUS, TYPE) " +
		"VALUES (?,?,?,?)";

	public void insertClientLink(Integer clientId, Integer objectId, String status, String type) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_insert_client_link);
			preparedStatement = connection.prepareStatement(q_insert_client_link);
			preparedStatement.setInt(1, clientId.intValue());
			preparedStatement.setInt(2, objectId.intValue());			
			preparedStatement.setString(3, status);			
			preparedStatement.setString(4, type);			
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	}
	
	

	
	private static final String q_delete_parameter_link =
		"DELETE FROM T_CLIENT_LNK " +
		"WHERE TYPE='S' AND STATUS = 'M' AND FK_OBJECT_ID=?";
	
	public void deleteParameterLink(Integer objectId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_parameter_link);
			preparedStatement = connection.prepareStatement(q_delete_parameter_link);
			preparedStatement.setInt(1, objectId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	}
	

	
	private static final String q_delete_source_link =
		"DELETE FROM T_CLIENT_LNK " +
		"WHERE TYPE='S' AND FK_OBJECT_ID=?";		

	
	public void deleteSourceLink(Integer srcId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_source_link);
			preparedStatement = connection.prepareStatement(q_delete_source_link);
			preparedStatement.setInt(1, srcId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	}
	
	private static final String qClientsList = 
		"SELECT DISTINCT c.PK_CLIENT_ID, c.CLIENT_ACRONYM, " + 
		"CONCAT( IF(c.CLIENT_ACRONYM != '', CONCAT(c.CLIENT_ACRONYM, ' - '), ''), LEFT(c.CLIENT_NAME, 50), IF( LENGTH(c.CLIENT_NAME) > 50 ,'...', '')   ) AS CLIENT_NAME " + 
		"FROM T_CLIENT c, T_CLIENT_LNK cl, T_OBLIGATION o " + 
		"WHERE cl.TYPE='A' AND cl.STATUS='M' AND cl.FK_CLIENT_ID=c.PK_CLIENT_ID AND o.PK_RA_ID=cl.FK_OBJECT_ID " +
		"ORDER BY CLIENT_NAME ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IClientDao#getClientsList()
	 */
	public List<ClientDTO> getClientsList() throws ServiceException {

		List<Object> values = new ArrayList<Object>();
		
		Connection conn = null;
		ClientDTOReader rsReader = new ClientDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(qClientsList, values, rsReader, conn);
			List<ClientDTO>  list = rsReader.getResultList();
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
     * @see eionet.rod.services.modules.db.dao.IClientDao#getClients()
     */
    public List<ClientDTO> getClients(String objectId) throws ServiceException {
    	
    	String query = "SELECT T_CLIENT.PK_CLIENT_ID, T_CLIENT.CLIENT_NAME, T_CLIENT.CLIENT_ACRONYM " +
    		"FROM T_CLIENT, T_CLIENT_LNK " +
    		"WHERE T_CLIENT_LNK.FK_OBJECT_ID = " + objectId + " AND T_CLIENT.PK_CLIENT_ID=T_CLIENT_LNK.FK_CLIENT_ID " +
    		"AND T_CLIENT_LNK.STATUS='C' AND T_CLIENT_LNK.TYPE='A'";
    	
    	List<Object> values = new ArrayList<Object>();
				
		Connection conn = null;
		ClientDTOReader rsReader = new ClientDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(query, values, rsReader, conn);
			List<ClientDTO>  list = rsReader.getResultList();
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
     * @see eionet.rod.services.modules.db.dao.IClientDao#getAllClients()
     */
    public List<ClientDTO> getAllClients() throws ServiceException {
    	
    	String query = "SELECT PK_CLIENT_ID, CLIENT_NAME, CLIENT_ACRONYM " +
    		"FROM T_CLIENT " +
    		"ORDER BY CLIENT_NAME";
    	
    	List<Object> values = new ArrayList<Object>();
				
		Connection conn = null;
		ClientDTOReader rsReader = new ClientDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(query, values, rsReader, conn);
			List<ClientDTO>  list = rsReader.getResultList();
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
