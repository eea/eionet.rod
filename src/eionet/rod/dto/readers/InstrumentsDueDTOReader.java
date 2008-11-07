package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.InstrumentsDueDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class InstrumentsDueDTOReader extends ResultSetBaseReader {
	
	/** */
	List<InstrumentsDueDTO> resultList = new ArrayList<InstrumentsDueDTO>();
	
	/*
	 * (non-Javadoc)
	 * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
	 */
	public void readRow(ResultSet rs) throws SQLException {

		InstrumentsDueDTO instrumentDTO = new InstrumentsDueDTO();
		instrumentDTO.setInstrumentId(new Integer(rs.getInt("PK_SOURCE_ID")));
		instrumentDTO.setTitle(rs.getString("TITLE"));
		instrumentDTO.setNextUpdate(rs.getString("RM_NEXT_UPDATE"));
		instrumentDTO.setVerified(rs.getString("RM_VERIFIED"));
		instrumentDTO.setVerifiedBy(rs.getString("RM_VERIFIED_BY"));
		
		resultList.add(instrumentDTO);
	}
	
	/**
	 * @return the resultList
	 */
	public List<InstrumentsDueDTO> getResultList() {
		return resultList;
	}

}
