package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.SiblingObligationDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class SiblingObligationDTOReader extends ResultSetBaseReader {
	
	/** */
	List<SiblingObligationDTO> resultList = new ArrayList<SiblingObligationDTO>();
	
	/*
	 * (non-Javadoc)
	 * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
	 */
	public void readRow(ResultSet rs) throws SQLException {

		SiblingObligationDTO obligationDTO = new SiblingObligationDTO();
		obligationDTO.setObligationId(rs.getString("PK_RA_ID"));
		obligationDTO.setFkSourceId(rs.getString("FK_SOURCE_ID"));
		obligationDTO.setTitle(rs.getString("TITLE"));
		obligationDTO.setAuthority(rs.getString("AUTHORITY"));
		obligationDTO.setTerminate(rs.getString("TERMINATE"));
		
		resultList.add(obligationDTO);
	}
	
	/**
	 * @return the resultList
	 */
	public List<SiblingObligationDTO> getResultList() {
		return resultList;
	}

}
