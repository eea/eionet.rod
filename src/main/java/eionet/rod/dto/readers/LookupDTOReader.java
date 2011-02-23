package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.LookupDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class LookupDTOReader extends ResultSetBaseReader {
	
	/** */
	List<LookupDTO> resultList = new ArrayList<LookupDTO>();
	
	/*
	 * (non-Javadoc)
	 * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
	 */
	public void readRow(ResultSet rs) throws SQLException {

		LookupDTO lookupDTO = new LookupDTO();
		lookupDTO.setCterm(rs.getString("C_TERM"));
		lookupDTO.setCvalue(rs.getString("C_VALUE"));
		
		resultList.add(lookupDTO);
	}
	
	/**
	 * @return the resultList
	 */
	public List<LookupDTO> getResultList() {
		return resultList;
	}

}
