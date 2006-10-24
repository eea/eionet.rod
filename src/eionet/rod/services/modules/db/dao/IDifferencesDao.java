package eionet.rod.services.modules.db.dao;

import java.util.Hashtable;

import eionet.rod.services.ServiceException;

public interface IDifferencesDao {

	/**
	 * Returns difference between undo and current object countries
	 * @param ts
	 * @param id
	 * @param voluntary
	 * @param op
	 * @return difference between undo and current object countries
	 * @throws ServiceException
	 */
	public Hashtable getDifferencesInCountries(long ts, int id, String voluntary, String op) throws ServiceException;

	/**
	 * Returns c
	 * @param ts
	 * @param id
	 * @param status
	 * @param op
	 * @param type
	 * @return returns difference between undo and current object clients
	 * @throws ServiceException
	 */
	public Hashtable getDifferencesInClients(long ts, int id, String status, String op, String type) throws ServiceException;

	/**
	 * returns difference between undo and current object issues
	 * @param ts
	 * @param id
	 * @param op
	 * @return Hashtable difference between undo and current object issues
	 * @throws ServiceException
	 */
	public Hashtable getDifferencesInIssues(long ts, int id, String op) throws ServiceException;

	/**
	 * returns difference between undo and current object Type of info reported
	 * @param ts
	 * @param id
	 * @param op
	 * @param cat
	 * @return difference between undo and current object Type of info reported
	 * @throws ServiceException
	 */
	public Hashtable getDifferencesInInfo(long ts, int id, String op, String cat) throws ServiceException;
	
	
	/**
	 * Returns difference between undo and current object
	 * @param ts
	 * @param tab
	 * @param col
	 * @return difference between undo and current object
	 * @throws ServiceException
	 */
	public String getDifferences(long ts, String tab, String col) throws ServiceException ;

}