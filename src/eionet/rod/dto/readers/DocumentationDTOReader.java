package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.DocumentationDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class DocumentationDTOReader extends ResultSetBaseReader {
	
	/** */
	List<DocumentationDTO> resultList = new ArrayList<DocumentationDTO>();
	
	/*
	 * (non-Javadoc)
	 * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
	 */
	public void readRow(ResultSet rs) throws SQLException {

		DocumentationDTO docDTO = new DocumentationDTO();
		docDTO.setAreaId(rs.getString("AREA_ID"));
		docDTO.setScreenId(rs.getString("SCREEN_ID"));
		docDTO.setDescription(rs.getString("DESCRIPTION"));
		docDTO.setHtml(rs.getString("HTML"));
		
		resultList.add(docDTO);
	}
	
	/**
	 * @return the resultListAAA
	 */
	public List<DocumentationDTO> getResultList() {
		return resultList;
	}

}
