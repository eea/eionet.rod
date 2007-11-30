package eionet.rod.services.modules.db.dao;

import java.util.Hashtable;
import java.util.Vector;

import eionet.rod.services.ServiceException;

public interface IRoleDao {

	/**
	 * Returns all role IDs existing in T_ROLE table
	 * @return All role IDs existing in T_ROLE tablke
	 * @throws ServiceException
	 */
	public String[][] getRoleIds() throws ServiceException;

	/**
	 * Saves roles
	 * @param role
	 * @throws ServiceException
	 */
	public void saveRole(Hashtable role) throws ServiceException;

	/**
	 * @throws ServiceException
	 */
	public void backUpRoles() throws ServiceException;

	/**
	 * @throws ServiceException
	 */
	public void commitRoles() throws ServiceException;

	/**
	 * Returns role information
	 * @param role_id
	 * @return role information
	 * @throws ServiceException
	 */
	public Hashtable getRoleDesc(String role_id) throws ServiceException;
	
	/**
	 * Returns role obligations
	 * @param role_id
	 * @return role obligations
	 * @throws ServiceException
	 */
	public Vector getRoleObligations(String role_id) throws ServiceException;

}