package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.HierarchyInstrumentDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class HierarchyInstrumentDTOReader extends ResultSetBaseReader {

    /** */
    List<HierarchyInstrumentDTO> resultList = new ArrayList<HierarchyInstrumentDTO>();

    /*
     * (non-Javadoc)
     * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
     */
    public void readRow(ResultSet rs) throws SQLException {

        HierarchyInstrumentDTO instrumentDTO = new HierarchyInstrumentDTO();
        instrumentDTO.setSourceId(new Integer(rs.getInt("PK_SOURCE_ID")));
        instrumentDTO.setSourceTitle(rs.getString("TITLE"));
        instrumentDTO.setSourceAlias(rs.getString("ALIAS"));
        instrumentDTO.setSourceUrl(rs.getString("URL"));

        instrumentDTO.setParentSourceId(new Integer(rs.getInt("PARENT_ID")));
        instrumentDTO.setParentSourceTitle(rs.getString("PARENT_TITLE"));
        instrumentDTO.setParentSourceAlias(rs.getString("PARENT_ALIAS"));
        instrumentDTO.setParentSourceUrl(rs.getString("PARENT_URL"));

        resultList.add(instrumentDTO);
    }

    /**
     * @return the resultList
     */
    public List<HierarchyInstrumentDTO> getResultList() {
        return resultList;
    }

}
