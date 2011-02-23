package eionet.rod.services.modules.db.dao;

import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import eionet.rod.services.ServiceException;

public interface IGenericDao {

	/**
	 * @param tablename
	 * @return
	 * @throws ServiceException
	 */
	public Vector<Map<String,String>> getTable(String tablename) throws ServiceException;

	/**
	 * @param tablename
	 * @return
	 * @throws ServiceException
	 */
	public Vector<Hashtable<String,String>> getTableDesc(String tablename) throws ServiceException;

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