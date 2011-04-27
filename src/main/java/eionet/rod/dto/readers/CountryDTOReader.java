package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.CountryDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class CountryDTOReader extends ResultSetBaseReader {

    /** */
    List<CountryDTO> resultList = new ArrayList<CountryDTO>();

    /*
     * (non-Javadoc)
     * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
     */
    public void readRow(ResultSet rs) throws SQLException {

        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setCountryId(new Integer(rs.getInt("PK_SPATIAL_ID")));
        countryDTO.setName(rs.getString("SPATIAL_NAME"));
        countryDTO.setType(rs.getString("SPATIAL_TYPE"));
        countryDTO.setTwoletter(rs.getString("SPATIAL_TWOLETTER"));
        countryDTO.setIsMember(rs.getString("SPATIAL_ISMEMBERCOUNTRY"));

        resultList.add(countryDTO);
    }

    /**
     * @return the resultListAAA
     */
    public List<CountryDTO> getResultList() {
        return resultList;
    }

}
