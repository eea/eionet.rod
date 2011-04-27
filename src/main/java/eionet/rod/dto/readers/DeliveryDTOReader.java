package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.DeliveryDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class DeliveryDTOReader extends ResultSetBaseReader {

    /** */
    List<DeliveryDTO> resultList = new ArrayList<DeliveryDTO>();

    /*
     * (non-Javadoc)
     * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
     */
    public void readRow(ResultSet rs) throws SQLException {

        DeliveryDTO deliveryDTO = new DeliveryDTO();
        deliveryDTO.setTitle(rs.getString("TITLE"));
        deliveryDTO.setUrl(rs.getString("DELIVERY_URL"));

        resultList.add(deliveryDTO);
    }

    /**
     * @return the resultList
     */
    public List<DeliveryDTO> getResultList() {
        return resultList;
    }

}
