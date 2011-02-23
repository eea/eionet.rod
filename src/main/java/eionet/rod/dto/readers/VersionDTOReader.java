package eionet.rod.dto.readers;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.VersionDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class VersionDTOReader extends ResultSetBaseReader {
	
	/** */
	List<VersionDTO> resultList = new ArrayList<VersionDTO>();
	
	/*
	 * (non-Javadoc)
	 * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
	 */
	public void readRow(ResultSet rs) throws SQLException {

		VersionDTO versionDTO = new VersionDTO();
		versionDTO.setUndoTime(rs.getString("UNDO_TIME"));
		versionDTO.setCol(rs.getString("COL"));
		versionDTO.setTab(rs.getString("TAB"));
		versionDTO.setOperation(rs.getString("OPERATION"));
		
		Blob blob = rs.getBlob("VALUE");
		byte[] bdata = blob.getBytes(1, (int) blob.length());
		String value = new String(bdata);
		versionDTO.setValue(value);
		
		versionDTO.setShowObject(rs.getString("SHOW_OBJECT"));
		
		resultList.add(versionDTO);
	}
	
	/**
	 * @return the resultList
	 */
	public List<VersionDTO> getResultList() {
		return resultList;
	}

}
