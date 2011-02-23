package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.ObligationDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class ObligationDTOReader extends ResultSetBaseReader {
	
	/** */
	List<ObligationDTO> resultList = new ArrayList<ObligationDTO>();
	
	/*
	 * (non-Javadoc)
	 * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
	 */
	public void readRow(ResultSet rs) throws SQLException {

		ObligationDTO obligationDTO = new ObligationDTO();
		obligationDTO.setObligationId(new Integer(rs.getInt("PK_RA_ID")));
		obligationDTO.setSourceId(new Integer(rs.getInt("FK_SOURCE_ID")));
		obligationDTO.setTitle(rs.getString("TITLE"));
		
		resultList.add(obligationDTO);
	}
	
	/**
	 * @return the resultList
	 */
	public List<ObligationDTO> getResultList() {
		return resultList;
	}

}
