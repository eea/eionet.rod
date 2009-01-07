package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import eionet.rod.dto.IssueDTO;
import eionet.rod.dto.readers.IssueDTOReader;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IIssueDao;
import eionet.rod.util.sql.SQLUtil;

public class IssueMySqlDao extends MySqlBaseDao implements IIssueDao {

	public IssueMySqlDao() {
	}

	private static final String qIssues = 
		"SELECT ISSUE_NAME AS name, PK_ISSUE_ID AS id, {fn concat(?,PK_ISSUE_ID) }  AS uri " + 
		"FROM T_ISSUE " + 
		"ORDER BY ISSUE_NAME ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IIssueDao#getIssues()
	 */
	public Vector getIssues() throws ServiceException {

		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(qIssues);
			preparedStatement.setString(1, (String) properties.get(FileServiceIF.ISSUE_NAMESPACE));
			if (isDebugMode) logQuery(qIssues);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(resultSet, preparedStatement, connection);
		}

		return result != null ? result : new Vector();

	}

	private final static String qObligationIssues = 
		"SELECT i.ISSUE_NAME AS name " +
		"FROM T_ISSUE i, T_RAISSUE_LNK r " + 
		"WHERE  r.FK_RA_ID = ? AND i.PK_ISSUE_ID = r.FK_ISSUE_ID " + 
		"ORDER BY i.ISSUE_NAME";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IIssueDao#getObligationIssues(java.lang.Integer)
	 */
	public Vector getObligationIssues(Integer id) throws ServiceException {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(qObligationIssues);
			preparedStatement.setInt(1, id.intValue());
			if (isDebugMode) logQuery(qObligationIssues);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(resultSet, preparedStatement, connection);
		}

		return result != null ? result : new Vector();
	}

	private static final String qIssueIdPairs = 
		"SELECT PK_ISSUE_ID, REPLACE(ISSUE_NAME, '&', '&#038;') AS ISSUE_NAME " + 
		"FROM T_ISSUE " + 
		"ORDER BY PK_ISSUE_ID";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IIssueDao#getIssueIdPairs()
	 */
	public String[][] getIssueIdPairs() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String[][] result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(qIssueIdPairs);
			if (isDebugMode) logQuery(qIssueIdPairs);
			result = _executeStringQuery(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new String[][] {};
	}

	private static final String gIssuesByRaId = 
		"SELECT FK_ISSUE_ID " + 
		"FROM T_OBLIGATION o, T_RAISSUE_LNK il " + 
		"WHERE il.FK_RA_ID=o.PK_RA_ID AND il.FK_RA_ID=? " + 
		"ORDER BY FK_ISSUE_ID";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IIssueDao#getIssues(java.lang.Integer)
	 */
	public String[][] getIssues(Integer raId) throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String[][] result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(gIssuesByRaId);
			preparedStatement.setInt(1, raId.intValue());
			if (isDebugMode) logQuery(gIssuesByRaId);
			result = _executeStringQuery(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new String[][] {};
	}
	
	private static final String qIssuesList = 
		"SELECT ISSUE_NAME, PK_ISSUE_ID " + 
		"FROM T_ISSUE " + 
		"ORDER BY ISSUE_NAME ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IIssueDao#getIssuesList()
	 */
	public List<IssueDTO> getIssuesList() throws ServiceException {

		List<Object> values = new ArrayList<Object>();
		
		Connection conn = null;
		IssueDTOReader rsReader = new IssueDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(qIssuesList, values, rsReader, conn);
			List<IssueDTO>  list = rsReader.getResultList();
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
	 * @see eionet.rod.services.modules.db.dao.IIssueDao#getObligationIssuesList(java.lang.String)
	 */
	public List<IssueDTO> getObligationIssuesList(String obligationId) throws ServiceException {

		List<Object> values = new ArrayList<Object>();
		
		String qObligationIssuesList = 
			"SELECT T_ISSUE.ISSUE_NAME, T_ISSUE.PK_ISSUE_ID " + 
			"FROM T_ISSUE, T_RAISSUE_LNK " + 
			"WHERE T_RAISSUE_LNK.FK_ISSUE_ID=T_ISSUE.PK_ISSUE_ID AND T_RAISSUE_LNK.FK_RA_ID=" + obligationId + " " +
			"ORDER BY T_ISSUE.ISSUE_NAME ";
		
		Connection conn = null;
		IssueDTOReader rsReader = new IssueDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(qObligationIssuesList, values, rsReader, conn);
			List<IssueDTO>  list = rsReader.getResultList();
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
	 * @see eionet.rod.services.modules.db.dao.IIssueDao#getObligationIssuesList(List<String> issueIds)
	 */
	public List<IssueDTO> getObligationIssuesList(List<String> issueIds) throws ServiceException {
		
		StringBuilder ids = new StringBuilder();
    	if(issueIds != null){
	    	for(Iterator<String> it = issueIds.iterator(); it.hasNext(); ){
	    		String id = it.next();
	    		ids.append(id);
	    		if(it.hasNext())
	    			ids.append(",");
	    	}
    	} else {
    		return null;
    	}

		List<Object> values = new ArrayList<Object>();
		
		String qObligationIssuesList = 
			"SELECT ISSUE_NAME, PK_ISSUE_ID " + 
			"FROM T_ISSUE " + 
			"WHERE PK_ISSUE_ID IN ("+ids.toString()+") " +
			"ORDER BY ISSUE_NAME ";
		
		Connection conn = null;
		IssueDTOReader rsReader = new IssueDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(qObligationIssuesList, values, rsReader, conn);
			List<IssueDTO>  list = rsReader.getResultList();
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
	
	private static final String q_issue_name_by_id = 
		"SELECT ISSUE_NAME AS name " + 
		"FROM T_ISSUE " + 
		"WHERE PK_ISSUE_ID =?";

	/* (non-Javadoc)
	 * @see eionet.rod.services.modules.db.dao.IIssueDao#getCountryById(String id)
	 */
	public String getIssueNameById(String id) throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String result = null;
		String[][] m = null;
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_issue_name_by_id);
			preparedStatement.setString(1, id);
			if (isDebugMode) logQuery(q_issue_name_by_id);
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
	
	private static final String q_insert_obligation_issue = 
		"INSERT INTO T_RAISSUE_LNK (FK_ISSUE_ID, FK_RA_ID) VALUES (?,?)";

	/* (non-Javadoc)
	 * @see eionet.rod.services.modules.db.dao.IIssueDao#insertObligationIssues(String obligationId, List<String> selectedIssues)
	 */
	public void insertObligationIssues(String obligationId, List<String> selectedIssues) throws ServiceException {
		List<Object> values = null;
		Connection conn = null;
		try{
			conn = getConnection();
			for(Iterator<String> it = selectedIssues.iterator(); it.hasNext();){
				String issueId = it.next();
				values = new ArrayList<Object>();
				values.add(issueId);
				values.add(obligationId);
				SQLUtil.executeUpdate(q_insert_obligation_issue, values, conn);
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
