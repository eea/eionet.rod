package eionet.rod.services.modules.db.dao;

import java.util.Vector;

import eionet.rod.services.ServiceException;

public interface IClientDao {

	/**
	 * @return
	 * @throws ServiceException
	 */
	public Vector getOrganisations() throws ServiceException;

	/**
	 * Returns obligation organisations
	 * @param obligationId
	 * @return obligation organisations
	 * @throws ServiceException
	 */
	public Vector getObligationOrg(Integer obligationId) throws ServiceException;

	
	/**
	 * Deletes link with an obligation
	 * @param raId
	 * @throws ServiceException
	 */
	public void deleteObligationLink(Integer raId) throws ServiceException;
	
	/**
	 * @param clientId
	 * @param objectId
	 * @param status
	 * @param type
	 * @throws ServiceException
	 */
	public void insertClientLink(Integer clientId, Integer objectId, String status, String type) throws ServiceException;
	
	
	
	/**
	 * Deletes source link
	 * @param srcId
	 * @throws ServiceException
	 */
	public void deleteSourceLink(Integer srcId) throws ServiceException;
	
	
	/**
	 * Deletes all linked parameter and medium records and in delete mode also the self record
	 * @param objectId
	 * @throws ServiceException
	 */
	public void deleteParameterLink(Integer objectId) throws ServiceException;
}