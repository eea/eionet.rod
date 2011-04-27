package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.RoleOccupantDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class RoleOccupantDTOReader extends ResultSetBaseReader {

    /** */
    List<RoleOccupantDTO> resultList = new ArrayList<RoleOccupantDTO>();

    /*
     * (non-Javadoc)
     * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
     */
    public void readRow(ResultSet rs) throws SQLException {

        RoleOccupantDTO roDTO = new RoleOccupantDTO();
        roDTO.setPerson(rs.getString("PERSON"));
        roDTO.setInstitute(rs.getString("INSTITUTE"));

        resultList.add(roDTO);
    }

    /**
     * @return the resultList
     */
    public List<RoleOccupantDTO> getResultList() {
        return resultList;
    }

}
