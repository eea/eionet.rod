package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.ClientDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class ClientAllDTOReader extends ResultSetBaseReader {
	
	/** */
	List<ClientDTO> resultList = new ArrayList<ClientDTO>();
	
	/*
	 * (non-Javadoc)
	 * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
	 */
	public void readRow(ResultSet rs) throws SQLException {

		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setClientId(new Integer(rs.getInt("PK_CLIENT_ID")));
		clientDTO.setName(rs.getString("CLIENT_NAME"));
		clientDTO.setAcronym(rs.getString("CLIENT_ACRONYM"));
		clientDTO.setUrl(rs.getString("CLIENT_URL"));
		clientDTO.setAddress(rs.getString("CLIENT_ADDRESS"));
		clientDTO.setEmail(rs.getString("CLIENT_EMAIL"));
		clientDTO.setDescription(rs.getString("DESCRIPTION"));
		clientDTO.setPostalCode(rs.getString("POSTAL_CODE"));
		clientDTO.setCity(rs.getString("CITY"));
		clientDTO.setCountry(rs.getString("COUNTRY"));
		clientDTO.setShortName(rs.getString("CLIENT_SHORT_NAME"));
		
		resultList.add(clientDTO);
	}
	
	/**
	 * @return the resultList
	 */
	public List<ClientDTO> getResultList() {
		return resultList;
	}

}
