package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.HelpDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class HelpDTOReader extends ResultSetBaseReader {
	
	/** */
	List<HelpDTO> resultList = new ArrayList<HelpDTO>();
	
	/*
	 * (non-Javadoc)
	 * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
	 */
	public void readRow(ResultSet rs) throws SQLException {

		HelpDTO helpDTO = new HelpDTO();
		helpDTO.setHelpId(rs.getString("PK_HELP_ID"));
		helpDTO.setHelpTitle(rs.getString("HELP_TITLE"));
		helpDTO.setHelpText(rs.getString("HELP_TEXT"));
		
		resultList.add(helpDTO);
	}
	
	/**
	 * @return the resultList
	 */
	public List<HelpDTO> getResultList() {
		return resultList;
	}

}
