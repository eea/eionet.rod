package eionet.rod.services.modules.db.dao;

import java.util.Vector;

import eionet.rod.services.ServiceException;

public interface IGenericDao {

	/**
	 * @param tablename
	 * @return
	 * @throws ServiceException
	 */
	public Vector getTable(String tablename) throws ServiceException;

	/**
	 * @param tablename
	 * @return
	 * @throws ServiceException
	 */
	public Vector getTableDesc(String tablename) throws ServiceException;

	/**
	 * @param id
	 * @param table
	 * @return
	 * @throws ServiceException
	 */
	public boolean isIdAvailable(String id, String table) throws ServiceException ;
	
	
	/**
	 * @return
	 * @throws ServiceException
	 */
	public String getLastUpdate() throws ServiceException ;

}