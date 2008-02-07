package eionet.rod.services.modules.db.dao;

import java.util.Hashtable;
import java.util.Vector;

import eionet.rod.services.ServiceException;

public interface ISpatialDao {

	/**
	 * Returns countries from the DB
	 * Used in RDF and XML/RPC
	 * @return countries from the DB
	 * @throws ServiceException
	 */
	public Vector getCountries() throws ServiceException;

	/**
	 * Returns country by its id
	 * @param id:Country's ID
	 * @return country by its id
	 * @throws ServiceException
	 */
	public String getCountryById(int id) throws ServiceException;

	/**
	 * Returns countries of an obligation
	 * NB! SPATIAL_TYPE='C'
	 * @param raId Report obligation's ID
	 * @return  countries of an obligation
	 * @throws ServiceException
	 */
	public String[][] getCountries(int raId) throws ServiceException;

	/**
	 * Returns obligation countries
	 * @param id Report obligation' ID
	 * @return obligation countries
	 * @throws ServiceException
	 */
	public Vector getObligationCountries(int id) throws ServiceException;

	/**
	 * Returns the id-name pars for countries.
	 * Used in AddClient screen
	 * @return the id-name pars for countries
	 * @throws ServiceException
	 */
	public String[][] getCountryIdPairs() throws ServiceException;

	
	
	/**
	 * @param ra_id
	 * @param spatial_id
	 * @return
	 * @throws ServiceException
	 */
	public Hashtable getCountryInfo(int ra_id, int spatial_id) throws ServiceException ;
	
	/**
	 * Returns true if country twoletter exists
	 * @param twoletter
	 * @throws ServiceException
	 */
	public boolean checkCountry(String twoletter) throws ServiceException;
	
	/**
	 * Returns true if country id exists
	 * @param id
	 * @throws ServiceException
	 */
	public boolean checkCountryById(String id) throws ServiceException;
}