package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.HistDeadlineDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class HistDeadlineDTOReader extends ResultSetBaseReader {
	
	/** */
	List<HistDeadlineDTO> resultList = new ArrayList<HistDeadlineDTO>();
	
	/*
	 * (non-Javadoc)
	 * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
	 */
	public void readRow(ResultSet rs) throws SQLException {

		HistDeadlineDTO deadlineDTO = new HistDeadlineDTO();
		deadlineDTO.setObligationId(new Integer(rs.getInt("id")));
		deadlineDTO.setSourceId(new Integer(rs.getInt("source")));
		deadlineDTO.setObligationTitle(rs.getString("title"));
		deadlineDTO.setDeadline(rs.getString("deadline"));
		
		resultList.add(deadlineDTO);
	}
	
	/**
	 * @return the resultList
	 */
	public List<HistDeadlineDTO> getResultList() {
		return resultList;
	}

}
