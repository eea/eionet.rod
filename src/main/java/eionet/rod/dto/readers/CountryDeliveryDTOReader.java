package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.CountryDeliveryDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class CountryDeliveryDTOReader extends ResultSetBaseReader {

    /** */
    List<CountryDeliveryDTO> resultList = new ArrayList<CountryDeliveryDTO>();

    /*
     * (non-Javadoc)
     * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
     */
    @Override
    public void readRow(ResultSet rs) throws SQLException {

        CountryDeliveryDTO deliveryDTO = new CountryDeliveryDTO();

        deliveryDTO.setDeliveryFKObligationId(new Integer(rs.getInt("FK_RA_ID")));
        deliveryDTO.setDeliveryFKSpatialId(new Integer(rs.getInt("FK_SPATIAL_ID")));
        deliveryDTO.setDeliveryTitle(rs.getString("TITLE"));
        deliveryDTO.setDeliveryUrl(rs.getString("DELIVERY_URL"));
        deliveryDTO.setDeliveryUploadDate(rs.getString("UPLOAD_DATE"));
        deliveryDTO.setDeliveryType(rs.getString("DELIVERY_TYPE"));
        deliveryDTO.setDeliveryFormat(rs.getString("FORMAT"));
        deliveryDTO.setDeliveryCoverage(rs.getString("COVERAGE"));
        deliveryDTO.setDeliveryCoverageNote(rs.getString("COVERAGE_NOTE"));

        deliveryDTO.setObligationId(new Integer(rs.getInt("PK_RA_ID")));
        deliveryDTO.setObligationFKSourceId(new Integer(rs.getInt("FK_SOURCE_ID")));
        deliveryDTO.setObligationTitle(rs.getString("OBLIGATION_TITLE"));
        deliveryDTO.setObligationReportFreqMonths(rs.getString("REPORT_FREQ_MONTHS"));
        deliveryDTO.setObligationTerminate(rs.getString("TERMINATE"));
        deliveryDTO.setObligationNextDeadline(rs.getString("NEXT_DEADLINE"));
        deliveryDTO.setObligationReportFormatUrl(rs.getString("REPORT_FORMAT_URL"));
        deliveryDTO.setObligationRespRole(rs.getString("RESPONSIBLE_ROLE"));
        deliveryDTO.setObligationFormatName(rs.getString("FORMAT_NAME"));
        deliveryDTO.setObligationFKDeliveryCountryIds(rs.getString("FK_DELIVERY_COUNTRY_IDS"));
        deliveryDTO.setObligationParameters(rs.getString("PARAMETERS"));

        deliveryDTO.setSpatialId(new Integer(rs.getInt("PK_SPATIAL_ID")));
        deliveryDTO.setSpatialName(rs.getString("SPATIAL_NAME"));
        deliveryDTO.setSpatialTwoLetter(rs.getString("SPATIAL_TWOLETTER"));
        deliveryDTO.setSpatialIsMember(rs.getString("SPATIAL_ISMEMBERCOUNTRY"));

        deliveryDTO.setRoleName(rs.getString("ROLE_NAME"));
        deliveryDTO.setRoleUrl(rs.getString("ROLE_URL"));
        deliveryDTO.setRoleMembersUrl(rs.getString("ROLE_MEMBERS_URL"));

        deliveryDTO.setClientLnkFKClientId(new Integer(rs.getInt("FK_CLIENT_ID")));
        deliveryDTO.setClientLnkFKObjectId(new Integer(rs.getInt("FK_OBJECT_ID")));
        deliveryDTO.setClientLnkStatus(rs.getString("STATUS"));
        deliveryDTO.setClientLnkType(rs.getString("TYPE"));

        deliveryDTO.setClientId(new Integer(rs.getInt("PK_CLIENT_ID")));
        deliveryDTO.setClientName(rs.getString("CLIENT_NAME"));

        resultList.add(deliveryDTO);
    }

    /**
     * @return the resultList
     */
    public List<CountryDeliveryDTO> getResultList() {
        return resultList;
    }

}
