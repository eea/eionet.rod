package eionet.rod.services.modules.db.dao;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.CountryInfoDTO;
import eionet.rod.dto.ObligationCountryDTO;
import eionet.rod.dto.ObligationDTO;
import eionet.rod.services.ServiceException;

public interface ISpatialDao {

    /**
     * Returns countries from the DB.
     * Used in RDF and XML/RPC
     * @return countries from the DB
     * @throws ServiceException
     */
    Vector<Map<String,String>> getCountries() throws ServiceException;

    /**
     * Returns country by its id.
     * @param id:Country's ID
     * @return country by its id
     * @throws ServiceException
     */
    String getCountryById(int id) throws ServiceException;

    /**
     * Returns countries of an obligation.
     * NB! SPATIAL_TYPE='C'
     * @param raId Report obligation's ID
     * @param boolean is voluntary
     * @return  countries of an obligation
     * @throws ServiceException
     */
    List<Integer> getObligationCountries(int raId, boolean voluntary) throws ServiceException;

    /**
     * Returns obligation countries.
     * @param id Report obligation' ID
     * @return obligation countries
     * @throws ServiceException
     */
    Vector<Map<String,String>> getObligationCountries(int id) throws ServiceException;

    /**
     * Returns the id-name pars for countries.
     * Used in AddClient screen
     * @return the id-name pars for countries
     * @throws ServiceException
     */
    String[][] getCountryIdPairs() throws ServiceException;



    /**
     * @param ra_id
     * @param spatial_id
     * @return
     * @throws ServiceException
     */
    CountryInfoDTO getCountryInfo(String oid, String sid) throws ServiceException ;

    /**
     * Returns true if country twoletter exists.
     * @param twoletter
     * @throws ServiceException
     */
    boolean checkCountry(String twoletter) throws ServiceException;

    /**
     * Returns true if country id exists.
     * @param id
     * @throws ServiceException
     */
    boolean checkCountryById(String id) throws ServiceException;

    /**
     * Returns CountryDTO.
     * @param spatial id
     * @throws ServiceException
     */
    CountryDTO getCountry(String spatialId) throws ServiceException;

    /**
     * @return list of obligations with same spatial
     * @param spatial id
     * @throws ServiceException
     */
    List<ObligationDTO> getCountryObligationsList(String spatialId) throws ServiceException;

    /**
     * Returns member countries from the DB.
     * @return member countries from the DB
     * @throws ServiceException
     */
    List<CountryDTO> getMemberCountries() throws ServiceException;

    /**
     * Returns non member countries from the DB.
     * @return non member countries from the DB
     * @throws ServiceException
     */
    List<CountryDTO> getNonMemberCountries() throws ServiceException;

    /**
     * Returns all countries from the DB.
     * @return all countries from the DB as list of CountryDTOs
     * @throws ServiceException
     */
    List<CountryDTO> getCountriesList() throws ServiceException;

    /**
     * Returns all spatials from the DB.
     * @return all spatials from the DB as list of CountryDTOs
     * @throws ServiceException
     */
    List<CountryDTO> getSpatialsList() throws ServiceException;

    /**
     * Returns countries list from the DB.
     * @param list of country ids
     * @return countries from the DB as list of ObligationCountryDTOs
     * @throws ServiceException
     */
    List<CountryDTO> getObligationCountriesList(List<String> countryIds) throws ServiceException;

    /**
     * Returns countries for obligation.
     * @param obligation id
     * @throws ServiceException
     */
    List<ObligationCountryDTO> getObligationCountriesList(String id) throws ServiceException;

    /**
     * Returns countries for obligation.
     * @param obligation id, is voluntary
     * @throws ServiceException
     */
    List<CountryDTO> getEditObligationCountriesList(String id, String voluntary) throws ServiceException;

    /**
     * @param obligation id
     * @param list of selected countries
     * @param voluntary
     * @throws ServiceException
     */
    void insertObligationCountries(String obligationId, List<String> selectedCountries, String voluntary) throws ServiceException;

}
