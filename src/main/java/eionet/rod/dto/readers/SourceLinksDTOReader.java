package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.SourceLinksDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class SourceLinksDTOReader extends ResultSetBaseReader {
	
	/** */
	List<SourceLinksDTO> resultList = new ArrayList<SourceLinksDTO>();
	
	/*
	 * (non-Javadoc)
	 * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
	 */
	public void readRow(ResultSet rs) throws SQLException {

		SourceLinksDTO scDTO = new SourceLinksDTO();
		scDTO.setChildId(new Integer(rs.getInt("FK_SOURCE_CHILD_ID")));
		scDTO.setParentId(new Integer(rs.getInt("FK_SOURCE_PARENT_ID")));
		
		resultList.add(scDTO);
	}
	
	/**
	 * @return the resultList
	 */
	public List<SourceLinksDTO> getResultList() {
		return resultList;
	}

}
