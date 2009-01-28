package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.HarvestHistoryDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class HarvestHistoryDTOReader extends ResultSetBaseReader {
	
	/** */
	List<HarvestHistoryDTO> resultList = new ArrayList<HarvestHistoryDTO>();
	
	/*
	 * (non-Javadoc)
	 * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
	 */
	public void readRow(ResultSet rs) throws SQLException {

		HarvestHistoryDTO harvestDTO = new HarvestHistoryDTO();
		harvestDTO.setHistoryId(rs.getString("PK_HISTORY_ID"));
		harvestDTO.setItemId(rs.getString("ITEM_ID"));
		harvestDTO.setItemType(rs.getString("ITEM_TYPE"));
		harvestDTO.setActionType(rs.getString("ACTION_TYPE"));
		harvestDTO.setUser(rs.getString("USER"));
		harvestDTO.setDescription(rs.getString("DESCRIPTION"));
		harvestDTO.setTimestamp(rs.getString("TIME_STAMP"));
		
		resultList.add(harvestDTO);
	}
	
	/**
	 * @return the resultList
	 */
	public List<HarvestHistoryDTO> getResultList() {
		return resultList;
	}

}
