package eionet.rod.services.modules.db.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import eionet.rod.dto.CountryDeliveryDTO;
import eionet.rod.dto.CountryDeliveryDataDTO;
import eionet.rod.services.ServiceException;

public interface IDeliveryDao {

	/**
	 * Saves delivery
	 * @param raId
	 * @param deliveries
	 * @param cMap Map for holding country names and Id's
	 * @throws ServiceException
	 */
	public void saveDeliveries(Integer raId, Vector deliveries, HashMap cMap) throws ServiceException;

	/**
	 * @param raId
	 * @throws ServiceException
	 */
	public void rollBackDeliveries(Integer raId) throws ServiceException;

	/**
	 * @throws ServiceException
	 */
	public void commitDeliveries() throws ServiceException;

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

}