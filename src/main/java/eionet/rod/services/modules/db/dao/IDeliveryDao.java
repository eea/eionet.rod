package eionet.rod.services.modules.db.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import eionet.rod.dto.CountryDeliveryDTO;
import eionet.rod.dto.CountryDeliveryDataDTO;
import eionet.rod.services.ServiceException;

public interface IDeliveryDao {

	/**
	 * @throws ServiceException
	 */
	public void rollBackDeliveries() throws ServiceException;

	/**
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
	 * @throws ServiceException
	 */
	public List<CountryDeliveryDTO> getCountyDeliveriesList(String actDetailsId, String spatialId) throws ServiceException;
	
	/**
	 * @param actDetailsId
	 * @throws ServiceException
	 */
	public CountryDeliveryDataDTO getDeliveryData(String actDetailsId) throws ServiceException;

	/**
	 * 
	 * @param deliveries
	 * @param existingCountryIdsByNames
	 * @param savedCountriesByObligationId
	 * @return
	 * @throws ServiceException
	 */
	public int saveDeliveries(Vector<Hashtable<String,Object>> deliveries, HashMap<String,Integer> existingCountryIdsByNames, HashMap<String,HashSet<Integer>> savedCountriesByObligationId) throws ServiceException;
}