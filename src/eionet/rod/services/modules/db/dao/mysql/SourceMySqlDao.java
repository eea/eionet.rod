package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import eionet.rod.RODUtil;
import eionet.rod.dto.HierarchyInstrumentDTO;
import eionet.rod.dto.InstrumentDTO;
import eionet.rod.dto.InstrumentFactsheetDTO;
import eionet.rod.dto.InstrumentObligationDTO;
import eionet.rod.dto.InstrumentParentDTO;
import eionet.rod.dto.InstrumentsDueDTO;
import eionet.rod.dto.InstrumentsListDTO;
import eionet.rod.dto.ObligationFactsheetDTO;
import eionet.rod.dto.SiblingObligationDTO;
import eionet.rod.dto.readers.HierarchyInstrumentDTOReader;
import eionet.rod.dto.readers.InstrumentsDueDTOReader;
import eionet.rod.dto.readers.SiblingObligationDTOReader;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.ISourceDao;
import eionet.rod.util.sql.SQLUtil;

public class SourceMySqlDao extends MySqlBaseDao implements ISourceDao {

	public SourceMySqlDao() {
	}

	private static final String q_instruments = 
		"SELECT " + 
			"s.PK_SOURCE_ID, " + 
			"REPLACE(s.TITLE, '&', '&#038;') AS TITLE, " + 
			"REPLACE(s.ALIAS, '&', '&#038;') AS ALIAS, " + 
			"REPLACE(s.URL, '&', '&#038;') AS URL, " + 
			"REPLACE(s.ABSTRACT, '&', '&#038;') AS ABSTRACT, " + 
			"REPLACE(c.CLIENT_NAME, '&', '&#038;') AS ISSUED_BY, " + 
			"REPLACE(s.LEGAL_NAME, '&', '&#038;') AS LEGAL_NAME, " + 
			"REPLACE(s.CELEX_REF, '&', '&#038;') AS CELEX_REF, " + 
			"REPLACE(s.TITLE, '&', '&#038;') AS TITLE, " + 
			"REPLACE(s.TITLE, '&', '&#038;') AS TITLE, " + 
			"CONCAT('" + rodDomain + "/instruments/', PK_SOURCE_ID) AS details_url, " + 
			"DATE_FORMAT(s.LAST_UPDATE, '%Y-%m-%d') AS LAST_UPDATE " + 
		"FROM T_SOURCE s LEFT OUTER JOIN T_CLIENT c ON s.FK_CLIENT_ID=c.PK_CLIENT_ID " + 
		"ORDER BY TITLE ";

	private static final String q_instruments_ids = "SELECT PK_SOURCE_ID FROM T_SOURCE"; 
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.ISourceDao#getInstruments()
	 */
	public Vector getInstruments() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;

		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_instruments);
			if (isDebugMode) logQuery(q_instruments);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new Vector();

	}
    
    private final static String qInstrumentById = 
        "SELECT " + 
            "PK_SOURCE_ID AS instrumentID, " + 
            "REPLACE(TITLE, '&', '&#038;') AS TITLE " + 
        "FROM T_SOURCE WHERE PK_SOURCE_ID=? ";

    /*
     * (non-Javadoc)
     * 
     * @see eionet.rod.services.modules.db.dao.ISourceDao#getInstrumentById(java.lang.Integer)
     */
    public Vector getInstrumentById(Integer id) throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Vector result = null;
        try {
            connection = getConnection();
            if (isDebugMode) logQuery(qInstrumentById);
            preparedStatement = connection.prepareStatement(qInstrumentById);
            preparedStatement.setInt(1, id.intValue());
            result = _getVectorOfHashes(preparedStatement);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result != null ? result : new Vector();
    }

	private static final String q_instruments_rss = 
		"SELECT " + 
			"PK_SOURCE_ID, " + 
			"REPLACE(TITLE, '&', '&#038;') AS TITLE, " + 
			"CONCAT('" + rodDomain + "/instruments/', PK_SOURCE_ID) AS LINK, " + 
			"REPLACE(COMMENT, '&', '&#038;') AS COMMENT " + 
		"FROM T_SOURCE " + 
		"ORDER BY PK_SOURCE_ID ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.ISourceDao#getInstrumentsRSS()
	 */
	public String[][] getInstrumentsRSS() throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String[][] result = null;
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(q_instruments_rss);
			if (isDebugMode) logQuery(q_instruments_rss);
			result = _executeStringQuery(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

		return result != null ? result : new String[][] {};

	}
	
	private static final String q_delete_child_link =
		"DELETE FROM T_SOURCE_LNK " +
		"WHERE CHILD_TYPE='S' AND FK_SOURCE_CHILD_ID=?";	
	
	public void deleteChildLink(Integer childId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_child_link);
			preparedStatement = connection.prepareStatement(q_delete_child_link);
			preparedStatement.setInt(1, childId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	}

	
	private static final String q_delete_parent_link =
		"DELETE FROM T_SOURCE_LNK " +
		"WHERE PARENT_TYPE='S' AND FK_SOURCE_PARENT_ID=?";	
	
	
	public void deleteParentLink(Integer parentId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_parent_link);
			preparedStatement = connection.prepareStatement(q_delete_parent_link);
			preparedStatement.setInt(1, parentId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	}
	
	
	private static final String q_delete_source =
		"DELETE FROM T_SOURCE WHERE PK_SOURCE_ID=?";
	
	public void deleteSource(Integer sourceId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_source);
			preparedStatement = connection.prepareStatement(q_delete_source);
			preparedStatement.setInt(1, sourceId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
		
	}
	
	 public String[][] getInstrumentIds() throws ServiceException {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			String[][] result = null;
			try {
				connection = getConnection();
				preparedStatement = connection.prepareStatement(q_instruments_ids);
				if (isDebugMode) logQuery(q_instruments_ids);
				result = _executeStringQuery(preparedStatement);
			} catch (SQLException exception) {
				logger.error(exception);
				throw new ServiceException(exception.getMessage());
			} finally {
				closeAllResources(null, preparedStatement, connection);
			}

			return result != null ? result : new String[][] {};
	  }
     
      private final static String qDGEnv = 
            "SELECT " + 
                "C_TERM AS name " + 
            "FROM T_LOOKUP WHERE C_VALUE=? AND CATEGORY = 'DGS' ";

        /*
         * (non-Javadoc)
         * 
         * @see eionet.rod.services.modules.db.dao.ISourceDao#getDGEnvName(java.lang.String)
         */
      public String getDGEnvName(String value) throws ServiceException {
          Connection connection = null;
          ResultSet resultSet = null;
          PreparedStatement preparedStatement = null;
          String[][] result = null;
          String res = null;

          try {
              connection = getConnection();
              preparedStatement = connection.prepareStatement(qDGEnv);
              preparedStatement.setString(1, value);
              if (isDebugMode) logQuery(qDGEnv);
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
       * @see eionet.rod.dao.ISourceDao#getInstrumentsDue()
       */
      public List<InstrumentsDueDTO> getInstrumentsDue() throws ServiceException {
      	
      	String query = "SELECT PK_SOURCE_ID, TITLE, RM_NEXT_UPDATE, RM_VERIFIED, RM_VERIFIED_BY " +
  		"FROM T_SOURCE ORDER BY RM_NEXT_UPDATE";
      	
      	List<Object> values = new ArrayList<Object>();
  				
  		Connection conn = null;
  		InstrumentsDueDTOReader rsReader = new InstrumentsDueDTOReader();
  		try{
  			conn = getConnection();
  			SQLUtil.executeQuery(query, values, rsReader, conn);
  			List<InstrumentsDueDTO>  list = rsReader.getResultList();
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
      
      private static final String q_instrument_factsheet =
  		"SELECT SO.PK_SOURCE_ID, SO.SOURCE_CODE, SO.CELEX_REF, SO.TITLE, SO.ALIAS, SO.URL, " +
  		"SO.ISSUED_BY_URL, CONCAT(LEFT(SO.ISSUED_BY_URL, 40), IF( LENGTH(SO.ISSUED_BY_URL) > 40 ,'...', '')) AS ISSUED_BY_URL_LABEL, " +
  		"SO.SECRETARIAT, SO.SECRETARIAT_URL, SO.ABSTRACT, IF(SO.VALID_FROM, DATE_FORMAT(SO.VALID_FROM,'%d/%m/%Y'), '') AS VALID_FROM, " +
  		"IF(SO.EC_ACCESSION, DATE_FORMAT(SO.EC_ACCESSION,'%d/%m/%Y'), '') AS EC_ACCESSION, " +
  		"IF(SO.EC_ENTRY_INTO_FORCE, DATE_FORMAT(SO.EC_ENTRY_INTO_FORCE,'%d/%m/%Y'), '') AS EC_ENTRY_INTO_FORCE, SO.COMMENT, " +
  		"DATE_FORMAT(SO.LAST_UPDATE, '%d/%m/%Y') AS LAST_UPDATE, DATE_FORMAT(SO.RM_NEXT_UPDATE, '%d/%m/%Y') AS RM_NEXT_UPDATE, " +
  		"DATE_FORMAT(SO.RM_VERIFIED, '%d/%m/%Y') AS RM_VERIFIED, SO.RM_VERIFIED_BY, SO.RM_VALIDATED_BY, SO.GEOGRAPHIC_SCOPE, SO.DGENV_REVIEW, " +
  		"CL.FK_CLIENT_ID, CL.FK_OBJECT_ID, CL.STATUS, CL.TYPE, " +
  		"C.PK_CLIENT_ID, C.CLIENT_NAME " +
  		"FROM T_SOURCE SO, T_CLIENT_LNK CL, T_CLIENT C " +
  		"WHERE SO.PK_SOURCE_ID=? AND CL.TYPE='S' AND CL.STATUS='M' AND CL.FK_OBJECT_ID=SO.PK_SOURCE_ID AND C.PK_CLIENT_ID = CL.FK_CLIENT_ID";
      
      private static final String q_parent_instruments =
    	"SELECT SC.PK_CLASS_ID, SC.CLASSIFICATOR, SC.CLASS_NAME " +
    	"FROM T_SOURCE_CLASS SC, T_SOURCE_LNK SL, T_SOURCE SO " +
    	"WHERE SO.PK_SOURCE_ID=? AND SO.PK_SOURCE_ID = SL.FK_SOURCE_CHILD_ID AND SL.FK_SOURCE_PARENT_ID = SC.PK_CLASS_ID AND SL.CHILD_TYPE='S' AND SL.PARENT_TYPE='C'";
      
      private static final String q_related_instruments =
  		"SELECT LSOURCE.PK_SOURCE_ID, LSOURCE.TITLE, LSOURCE.ALIAS " +
  		"FROM T_SOURCE_LNK SL, T_SOURCE LSOURCE, T_SOURCE SO " +
  		"WHERE SO.PK_SOURCE_ID=? AND SO.PK_SOURCE_ID=SL.FK_SOURCE_PARENT_ID AND SL.FK_SOURCE_CHILD_ID=LSOURCE.PK_SOURCE_ID AND SL.CHILD_TYPE='S' AND SL.PARENT_TYPE='S'";
      
      private static final String q_origin_instruments =
    		"SELECT LSOURCE.PK_SOURCE_ID, LSOURCE.TITLE, LSOURCE.ALIAS " +
    		"FROM T_SOURCE_LNK SL, T_SOURCE LSOURCE, T_SOURCE SO " +
    		"WHERE SO.PK_SOURCE_ID=? AND SO.PK_SOURCE_ID=SL.FK_SOURCE_CHILD_ID AND SL.FK_SOURCE_PARENT_ID=LSOURCE.PK_SOURCE_ID AND SL.CHILD_TYPE='S' AND SL.PARENT_TYPE='S'";
      
      private static final String q_instrument_obligations =
  		"SELECT OB.PK_RA_ID, OB.TITLE, OB.AUTHORITY " +
  		"FROM T_OBLIGATION OB, T_SOURCE SO " +
  		"WHERE SO.PK_SOURCE_ID=? AND SO.PK_SOURCE_ID=OB.FK_SOURCE_ID";
      
      /*
       * (non-Javadoc)
       * 
       * @see eionet.rod.dao.ISourceDao#getInstrumentFactsheet(String id)
       */
      public InstrumentFactsheetDTO getInstrumentFactsheet(String id) throws ServiceException {
      	
    	InstrumentFactsheetDTO ret = new InstrumentFactsheetDTO();
  		Connection connection = null;
  		PreparedStatement preparedStatement = null;
  		ResultSet rs = null;
  		ResultSet sub_rs = null;
  		
  		List<InstrumentParentDTO> parents = new ArrayList<InstrumentParentDTO>();
  		List<InstrumentDTO> relatedInstruments = new ArrayList<InstrumentDTO>();
  		List<InstrumentObligationDTO> obligations = new ArrayList<InstrumentObligationDTO>();
  		
  		try {
  			connection = getConnection();
  			if (isDebugMode) logQuery(q_instrument_factsheet);
  			preparedStatement = connection.prepareStatement(q_instrument_factsheet);
  			preparedStatement.setString(1,id);
  			rs = preparedStatement.executeQuery();
  			while(rs.next()){
  				ret.setSourceId(new Integer(rs.getInt("PK_SOURCE_ID")));
  				ret.setSourceTitle(rs.getString("TITLE"));
				ret.setSourceAlias(rs.getString("ALIAS"));
				ret.setSourceCelexRef(rs.getString("CELEX_REF"));
				ret.setSourceCode(rs.getString("SOURCE_CODE"));
				ret.setSourceUrl(rs.getString("URL"));
				ret.setSourceIssuedByUrl(rs.getString("ISSUED_BY_URL"));
				ret.setSourceIssuedByUrlLabel(rs.getString("ISSUED_BY_URL_LABEL"));
				ret.setSourceSecretariat(rs.getString("SECRETARIAT"));
				ret.setSourceSecretariatUrl(rs.getString("SECRETARIAT_URL"));
				ret.setSourceAbstract(rs.getString("ABSTRACT"));
				ret.setSourceValidFrom(rs.getString("VALID_FROM"));
				ret.setSourceEcAccession(rs.getString("EC_ACCESSION"));
				ret.setSourceEcEntryIntoForce(rs.getString("EC_ENTRY_INTO_FORCE"));
				ret.setSourceComment(rs.getString("COMMENT"));
				ret.setSourceLastUpdate(rs.getString("LAST_UPDATE"));
				ret.setSourceNextUpdate(rs.getString("RM_NEXT_UPDATE"));
				ret.setSourceVerified(rs.getString("RM_VERIFIED"));
				ret.setSourceVerifiedBy(rs.getString("RM_VERIFIED_BY"));
				ret.setSourceGeographicScope(rs.getString("GEOGRAPHIC_SCOPE"));
				ret.setSourceDgenvReview(rs.getString("DGENV_REVIEW"));
				
				ret.setClientLnkFKClientId(new Integer(rs.getInt("FK_CLIENT_ID")));
				ret.setClientLnkFKObjectId(new Integer(rs.getInt("FK_OBJECT_ID")));
				ret.setClientLnkStatus(rs.getString("STATUS"));
				ret.setClientLnkType(rs.getString("TYPE"));
				
				ret.setClientId(new Integer(rs.getInt("PK_CLIENT_ID")));
				ret.setClientName(rs.getString("CLIENT_NAME"));
				
				preparedStatement = connection.prepareStatement(q_parent_instruments);
	  			preparedStatement.setString(1,id);
	  			sub_rs = preparedStatement.executeQuery();
	  			while(sub_rs.next()){
	  				InstrumentParentDTO parent = new InstrumentParentDTO();
	  				parent.setClassId(new Integer(sub_rs.getInt("PK_CLASS_ID")));
	  				parent.setClassificator(sub_rs.getString("CLASSIFICATOR"));
	  				parent.setClassName(sub_rs.getString("CLASS_NAME"));
	  				parents.add(parent);
	  			}
	  			ret.setParents(parents);
	  			
				preparedStatement = connection.prepareStatement(q_origin_instruments);
	  			preparedStatement.setString(1,id);
	  			sub_rs = preparedStatement.executeQuery();
	  			while(sub_rs.next()){
	  				InstrumentDTO origin = new InstrumentDTO();
	  				origin.setSourceId(new Integer(sub_rs.getInt("PK_SOURCE_ID")));
	  				origin.setSourceTitle(sub_rs.getString("TITLE"));
	  				origin.setSourceAlias(sub_rs.getString("ALIAS"));
	  				ret.setOrigin(origin);
	  			}
	  			
	  			preparedStatement = connection.prepareStatement(q_related_instruments);
	  			preparedStatement.setString(1,id);
	  			sub_rs = preparedStatement.executeQuery();
	  			while(sub_rs.next()){
	  				InstrumentDTO instrument = new InstrumentDTO();
	  				instrument.setSourceId(new Integer(sub_rs.getInt("PK_SOURCE_ID")));
	  				instrument.setSourceTitle(sub_rs.getString("TITLE"));
	  				instrument.setSourceAlias(sub_rs.getString("ALIAS"));
	  				relatedInstruments.add(instrument);
	  			}
	  			ret.setRelatedInstruments(relatedInstruments);
	  			
	  			preparedStatement = connection.prepareStatement(q_instrument_obligations);
	  			preparedStatement.setString(1,id);
	  			sub_rs = preparedStatement.executeQuery();
	  			while(sub_rs.next()){
	  				InstrumentObligationDTO obligation = new InstrumentObligationDTO();
	  				obligation.setObligationId(new Integer(sub_rs.getInt("PK_RA_ID")));
	  				obligation.setTitle(sub_rs.getString("TITLE"));
	  				obligation.setAuthority(sub_rs.getString("AUTHORITY"));
	  				obligations.add(obligation);
	  			}
	  			ret.setObligations(obligations);

  			}	
  		} catch (SQLException exception) {
  			logger.error(exception);
  			throw new ServiceException(exception.getMessage());
  		} finally {
  			closeAllResources(null, preparedStatement, connection);
  		}
  		
  		return ret;
      }
      
      private final static String q_instrument_DGEnv = 
          "SELECT " + 
              "L.C_TERM AS name " + 
          "FROM T_LOOKUP L, T_SOURCE S " +
          "WHERE S.PK_SOURCE_ID=? AND S.DGENV_REVIEW=L.C_VALUE AND L.CATEGORY='DGS' ";

      /*
       * (non-Javadoc)
       * 
       * @see eionet.rod.services.modules.db.dao.ISourceDao#getDGEnvNameByInstrumentId(java.lang.String)
       */
	    public String getDGEnvNameByInstrumentId(String id) throws ServiceException {
	        Connection connection = null;
	        ResultSet resultSet = null;
	        PreparedStatement preparedStatement = null;
	        String[][] result = null;
	        String res = null;
	
	        try {
	            connection = getConnection();
	            preparedStatement = connection.prepareStatement(q_instrument_DGEnv);
	            preparedStatement.setString(1, id);
	            if (isDebugMode) logQuery(q_instrument_DGEnv);
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
	    
	  private static final String q_hierarchy_instrument =
		  "SELECT SC.PK_CLASS_ID, SC.CLASSIFICATOR, SC.CLASS_NAME, SL.FK_SOURCE_PARENT_ID " +
		  "FROM T_SOURCE_CLASS SC, T_SOURCE_LNK SL " +
		  "WHERE SC.PK_CLASS_ID=? AND SC.PK_CLASS_ID=SL.FK_SOURCE_CHILD_ID AND SL.CHILD_TYPE='C' AND SL.PARENT_TYPE='C' " +
		  "ORDER BY SC.CLASSIFICATOR";
	    
	  /*
       * (non-Javadoc)
       * 
       * @see eionet.rod.dao.ISourceDao#getHierarchyInstrument(String id)
       */
      public InstrumentsListDTO getHierarchyInstrument(String id) throws ServiceException {
      	
    	InstrumentsListDTO ret = new InstrumentsListDTO();
  		Connection connection = null;
  		PreparedStatement preparedStatement = null;
  		ResultSet rs = null;
  		
  		try {
  			connection = getConnection();
  			if (isDebugMode) logQuery(q_hierarchy_instrument);
  			preparedStatement = connection.prepareStatement(q_hierarchy_instrument);
  			preparedStatement.setString(1,id);
  			rs = preparedStatement.executeQuery();
  			while(rs.next()){
  				ret.setClassId(new Integer(rs.getInt("PK_CLASS_ID")));
  				ret.setClassificator(rs.getString("CLASSIFICATOR"));
  				ret.setClassName(rs.getString("CLASS_NAME"));
  				ret.setParentId(rs.getString("FK_SOURCE_PARENT_ID"));
		
  			}	
  		} catch (SQLException exception) {
  			logger.error(exception);
  			throw new ServiceException(exception.getMessage());
  		} finally {
  			closeAllResources(null, preparedStatement, connection);
  		}
  		
  		return ret;
      }
      
      private static final String q_hierarchy =
		  "SELECT SC.PK_CLASS_ID, SC.CLASSIFICATOR, SC.CLASS_NAME, SL.FK_SOURCE_PARENT_ID " +
		  "FROM T_SOURCE_CLASS SC_PARENT, T_SOURCE_CLASS SC, T_SOURCE_LNK SL " +
		  "WHERE SC_PARENT.PK_CLASS_ID=? AND SC_PARENT.PK_CLASS_ID=SL.FK_SOURCE_PARENT_ID AND SL.FK_SOURCE_CHILD_ID=SC.PK_CLASS_ID AND SL.CHILD_TYPE='C' AND SL.PARENT_TYPE='C' " +
		  "ORDER BY SC.CLASSIFICATOR";
      
      private static final String q_hierarchy_cnt =
		  "SELECT COUNT(*) " +
		  "FROM T_SOURCE_CLASS SC_PARENT, T_SOURCE_CLASS SC, T_SOURCE_LNK SL " +
		  "WHERE SC_PARENT.PK_CLASS_ID=? AND SC_PARENT.PK_CLASS_ID=SL.FK_SOURCE_PARENT_ID AND SL.FK_SOURCE_CHILD_ID=SC.PK_CLASS_ID AND SL.CHILD_TYPE='C' AND SL.PARENT_TYPE='C' " +
		  "ORDER BY SC.CLASSIFICATOR";
      
      /*
       * (non-Javadoc)
       * 
       * @see eionet.rod.dao.ISourceDao#getHierarchy(String id, boolean hasParent)
       */
      public String getHierarchy(String id, boolean hasParent) throws ServiceException {
    	  
    	String newLine = "\n";
      	
    	StringBuilder ret = new StringBuilder();
  		Connection connection = null;
  		PreparedStatement preparedStatement = null;
  		ResultSet rs = null;
  		
  		try {
  			connection = getConnection();
  			
  			preparedStatement = connection.prepareStatement(q_hierarchy_cnt);
  			preparedStatement.setString(1,id);
  			rs = preparedStatement.executeQuery();
  			int cnt = 0;
  			while(rs.next())
  				cnt = rs.getInt(1);
  			
  			if (isDebugMode) logQuery(q_hierarchy);
  			preparedStatement = connection.prepareStatement(q_hierarchy);
  			preparedStatement.setString(1,id);
  			rs = preparedStatement.executeQuery();
  			String style = "category";
  			if(!hasParent)
  				style = "topcategory";
  			
  			if(cnt > 0){
  				ret.append("<ul class='").append(style).append("'>").append(newLine);
	  			while(rs.next()){
	  				String child_id = rs.getString("PK_CLASS_ID");
	  				String classificator = rs.getString("CLASSIFICATOR");
	  				String className = rs.getString("CLASS_NAME");
	  				ret.append("<li>").append(newLine);
	  				if(hasParent){
	  					if(!RODUtil.isNullOrEmpty(classificator))
	  						ret.append(classificator).append("&#160;").append(newLine);
	  				}
	  				ret.append("<a href='instruments?id=").append(child_id).append("'>").append(className).append("</a>").append(newLine);
	  				ret.append(getHierarchy(child_id, true));
	  				ret.append("</li>").append(newLine);
	  			}	
	  			ret.append("</ul>").append(newLine);
  			}
  		} catch (SQLException exception) {
  			logger.error(exception);
  			throw new ServiceException(exception.getMessage());
  		} finally {
  			closeAllResources(null, preparedStatement, connection);
  		}
  		
  		return ret.toString();
      }
	    
      /*
       * (non-Javadoc)
       * 
       * @see eionet.rod.dao.ISourceDao#getHierarchyInstruments(String id)
       */
      public List<HierarchyInstrumentDTO> getHierarchyInstruments(String id) throws ServiceException {
      	
    	String query =
    		"SELECT S.PK_SOURCE_ID, S.TITLE, S.ALIAS, S.URL, " +
    		"PS.PK_SOURCE_ID AS PARENT_ID, PS.TITLE AS PARENT_TITLE, PS.ALIAS AS PARENT_ALIAS, PS.URL AS PARENT_URL " +
    		"FROM T_SOURCE_CLASS SC JOIN T_SOURCE_LNK SL ON SC.PK_CLASS_ID=SL.FK_SOURCE_PARENT_ID " +
    		"JOIN T_SOURCE S ON SL.FK_SOURCE_CHILD_ID=S.PK_SOURCE_ID " +
    		"LEFT OUTER JOIN T_SOURCE_LNK PSL ON PSL.FK_SOURCE_CHILD_ID=S.PK_SOURCE_ID AND PSL.CHILD_TYPE='S' AND PSL.PARENT_TYPE='S' " +
    		"LEFT OUTER JOIN T_SOURCE PS ON PS.PK_SOURCE_ID=PSL.FK_SOURCE_PARENT_ID " +
    		"WHERE SL.CHILD_TYPE='S' AND SL.PARENT_TYPE='C' AND SC.PK_CLASS_ID=" +id+ " ORDER BY S.TITLE";
      	
      	List<Object> values = new ArrayList<Object>();
  				
  		Connection conn = null;
  		HierarchyInstrumentDTOReader rsReader = new HierarchyInstrumentDTOReader();
  		try{
  			conn = getConnection();
  			SQLUtil.executeQuery(query, values, rsReader, conn);
  			List<HierarchyInstrumentDTO>  list = rsReader.getResultList();
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
