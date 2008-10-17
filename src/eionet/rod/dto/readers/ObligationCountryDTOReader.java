package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.ObligationCountryDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class ObligationCountryDTOReader extends ResultSetBaseReader {
	
	/** */
	List<ObligationCountryDTO> resultList = new ArrayList<ObligationCountryDTO>();
	
	/*
	 * (non-Javadoc)
	 * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
	 */
	public void readRow(ResultSet rs) throws SQLException {

		ObligationCountryDTO countryDTO = new ObligationCountryDTO();
		countryDTO.setCountryId(new Integer(rs.getInt("PK_SPATIAL_ID")));
		countryDTO.setName(rs.getString("SPATIAL_NAME"));
		countryDTO.setVoluntary(rs.getString("VOLUNTARY"));
		countryDTO.setIsMemberCountry(rs.getString("SPATIAL_ISMEMBERCOUNTRY"));
		
		resultList.add(countryDTO);
	}
	
	/**
	 * @return the resultListAAA
	 */
	public List<ObligationCountryDTO> getResultList() {
		return resultList;
	}

}
