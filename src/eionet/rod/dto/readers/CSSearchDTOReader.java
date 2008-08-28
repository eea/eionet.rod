package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.CSSearchDTO;
import eionet.rod.dto.ClientDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class CSSearchDTOReader extends ResultSetBaseReader {
	
	/** */
	List<CSSearchDTO> resultList = new ArrayList<CSSearchDTO>();
	
	/*
	 * (non-Javadoc)
	 * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
	 */
	public void readRow(ResultSet rs) throws SQLException {
		
		CSSearchDTO searchDTO = new CSSearchDTO();
		searchDTO.setObligationId(new Integer(rs.getInt("PK_RA_ID")));
		searchDTO.setObligationTitle(rs.getString("TITLE"));
		searchDTO.setObligationRespRole(rs.getString("RESPONSIBLE_ROLE"));
		searchDTO.setObligationNextReporting(rs.getString("NEXT_REPORTING"));
		searchDTO.setObligationNextDeadline(rs.getString("NEXT_DEADLINE"));
		searchDTO.setObligationFKSourceId(new Integer(rs.getInt("FK_SOURCE_ID")));
		searchDTO.setObligationTerminate(rs.getString("TERMINATE"));
		searchDTO.setObligationFKClientId(new Integer(rs.getInt("CLIENTID")));
		searchDTO.setObligationFKDeliveryCountryIds(rs.getString("FK_DELIVERY_COUNTRY_IDS"));
		searchDTO.setObligationDeadline(rs.getString("DEADLINE"));
		searchDTO.setObligationDeadline2(rs.getString("DEADLINE2"));
		searchDTO.setObligationHasDelivery(new Integer(rs.getInt("HAS_DELIVERY")));
		
		searchDTO.setRoleDescr(rs.getString("ROLE_DESCR"));
		searchDTO.setRoleUrl(rs.getString("ROLE_URL"));
		searchDTO.setRoleMembersUrl(rs.getString("ROLE_MEMBERS_URL"));
		
		searchDTO.setClientLnkFKClientId(new Integer(rs.getInt("FK_CLIENT_ID")));
		searchDTO.setClientLnkFKObjectId(new Integer(rs.getInt("FK_OBJECT_ID")));
		searchDTO.setClientLnkStatus(rs.getString("STATUS"));
		searchDTO.setClientLnkType(rs.getString("TYPE"));
		
		searchDTO.setClientId(new Integer(rs.getInt("PK_CLIENT_ID")));
		searchDTO.setClientName(rs.getString("CLIENT_NAME"));
		searchDTO.setClientDescr(rs.getString("CLIENT_DESCR"));
		
		searchDTO.setSpatialLnkFKSpatialId(new Integer(rs.getInt("FK_SPATIAL_ID")));
		searchDTO.setSpatialLnkFKObligationId(new Integer(rs.getInt("FK_RA_ID")));
		
		searchDTO.setSpatialId(new Integer(rs.getInt("PK_SPATIAL_ID")));
		searchDTO.setSpatialName(rs.getString("SPATIAL_NAME"));
		searchDTO.setSpatialTwoLetter(rs.getString("SPATIAL_TWOLETTER"));
		searchDTO.setSpatialIsMember(rs.getString("SPATIAL_ISMEMBERCOUNTRY"));
		
		searchDTO.setSourceId(new Integer(rs.getInt("PK_SOURCE_ID")));
		searchDTO.setSourceCode(rs.getString("SOURCE_CODE"));
		
		resultList.add(searchDTO);
	}
	
	/**
	 * @return the resultList
	 */
	public List<CSSearchDTO> getResultList() {
		return resultList;
	}

}
