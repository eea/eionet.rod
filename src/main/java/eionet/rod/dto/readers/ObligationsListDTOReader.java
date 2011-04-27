package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.ObligationsListDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class ObligationsListDTOReader extends ResultSetBaseReader {

    /** */
    List<ObligationsListDTO> resultList = new ArrayList<ObligationsListDTO>();

    /*
     * (non-Javadoc)
     * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
     */
    public void readRow(ResultSet rs) throws SQLException {

        ObligationsListDTO obligationDTO = new ObligationsListDTO();
        obligationDTO.setObligationId(new Integer(rs.getInt("PK_RA_ID")));
        obligationDTO.setObligationTitle(rs.getString("TITLE"));
        obligationDTO.setSourceId(new Integer(rs.getInt("PK_SOURCE_ID")));
        obligationDTO.setSourceTitle(rs.getString("SOURCE_TITLE"));
        obligationDTO.setClientId(new Integer(rs.getInt("PK_CLIENT_ID")));
        obligationDTO.setClientName(rs.getString("CLIENT_NAME"));
        obligationDTO.setClientDescr(rs.getString("CLIENT_DESCR"));
        obligationDTO.setNextDeadline(rs.getString("NEXT_DEADLINE"));
        obligationDTO.setNextReporting(rs.getString("NEXT_REPORTING"));
        obligationDTO.setFkDeliveryCountryIds(rs.getString("FK_DELIVERY_COUNTRY_IDS"));
        obligationDTO.setTerminate(rs.getString("TERMINATE"));

        resultList.add(obligationDTO);
    }

    /**
     * @return the resultList
     */
    public List<ObligationsListDTO> getResultList() {
        return resultList;
    }

}
