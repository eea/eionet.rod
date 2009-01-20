package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.SourceClassDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class SourceClassDTOReader extends ResultSetBaseReader {
	
	/** */
	List<SourceClassDTO> resultList = new ArrayList<SourceClassDTO>();
	
	/*
	 * (non-Javadoc)
	 * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
	 */
	public void readRow(ResultSet rs) throws SQLException {

		SourceClassDTO scDTO = new SourceClassDTO();
		scDTO.setClassId(new Integer(rs.getInt("PK_CLASS_ID")));
		scDTO.setClassificator(rs.getString("CLASSIFICATOR"));
		scDTO.setClassName(rs.getString("CLASS_NAME"));
		
		resultList.add(scDTO);
	}
	
	/**
	 * @return the resultList
	 */
	public List<SourceClassDTO> getResultList() {
		return resultList;
	}

}
