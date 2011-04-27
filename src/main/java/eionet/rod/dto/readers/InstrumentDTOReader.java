package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.InstrumentDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class InstrumentDTOReader extends ResultSetBaseReader {

    /** */
    List<InstrumentDTO> resultList = new ArrayList<InstrumentDTO>();

    /*
     * (non-Javadoc)
     * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
     */
    public void readRow(ResultSet rs) throws SQLException {

        InstrumentDTO instrumentDTO = new InstrumentDTO();
        instrumentDTO.setSourceId(new Integer(rs.getInt("PK_SOURCE_ID")));
        instrumentDTO.setSourceTitle(rs.getString("TITLE"));

        resultList.add(instrumentDTO);
    }

    /**
     * @return the resultList
     */
    public List<InstrumentDTO> getResultList() {
        return resultList;
    }

}
