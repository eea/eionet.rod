package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.ObligationsDueDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class ObligationsDueDTOReader extends ResultSetBaseReader {

    /** */
    List<ObligationsDueDTO> resultList = new ArrayList<ObligationsDueDTO>();

    /*
     * (non-Javadoc)
     * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
     */
    public void readRow(ResultSet rs) throws SQLException {

        ObligationsDueDTO obligationDTO = new ObligationsDueDTO();
        obligationDTO.setObligationId(new Integer(rs.getInt("PK_RA_ID")));
        obligationDTO.setObligationTitle(rs.getString("TITLE"));
        obligationDTO.setLastUpdate(rs.getString("LAST_UPDATE"));
        obligationDTO.setValidatedBy(rs.getString("VALIDATED_BY"));
        obligationDTO.setNextUpdate(rs.getString("RM_NEXT_UPDATE"));
        obligationDTO.setVerified(rs.getString("RM_VERIFIED"));
        obligationDTO.setVerifiedBy(rs.getString("RM_VERIFIED_BY"));

        resultList.add(obligationDTO);
    }

    /**
     * @return the resultList
     */
    public List<ObligationsDueDTO> getResultList() {
        return resultList;
    }

}
