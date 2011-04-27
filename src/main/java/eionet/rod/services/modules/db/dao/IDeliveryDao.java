package eionet.rod.services.modules.db.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.openrdf.query.TupleQueryResult;

import eionet.rod.dto.CountryDeliveryDTO;
import eionet.rod.dto.CountryDeliveryDataDTO;
import eionet.rod.services.ServiceException;

public interface IDeliveryDao {

    /**
     * @throws ServiceException
     */
    public void rollBackDeliveries() throws ServiceException;

    /**
     * @param deliveredCountriesByObligations
     * @throws ServiceException
     */
    public void commitDeliveries(HashMap<String,HashSet<Integer>> deliveredCountriesByObligations) throws ServiceException;

    /**
     * @throws ServiceException
     */
    public void backUpDeliveries() throws ServiceException;

    /**
     * @param actDetailsId
     * @param spatialId
     * @return List<CountryDeliveryDTO>
     * @throws ServiceException
     */
    public List<CountryDeliveryDTO> getCountyDeliveriesList(String actDetailsId, String spatialId) throws ServiceException;

    /**
     * @param actDetailsId
     * @return CountryDeliveryDataDTO
     * @throws ServiceException
     */
    public CountryDeliveryDataDTO getDeliveryData(String actDetailsId) throws ServiceException;

    /**
     *
     * @param bindings
     * @param savedCountriesByObligationId
     * @return int
     * @throws ServiceException
     */
    public int saveDeliveries(TupleQueryResult bindings, HashMap<String,HashSet<Integer>> savedCountriesByObligationId) throws ServiceException;
}