package eionet.rod.services.modules.db.dao;

import java.util.Hashtable;

import eionet.rod.services.ServiceException;

public interface IRoleDao {

	/**
	 * Saves person Full Name + institute name
	 * @params roleId, Full Name, organisation name
	 * @param fullName
	 * @param orgName
	 * @throws ServiceException
	 */
	public void savePerson(String roleId, String fullName, String orgName) throws ServiceException;

	/**
	 * Returns all role IDs existing in T_ROLE table
	 * @return All role IDs existing in T_ROLE tablke
	 * @throws ServiceException
	 */
	public String[][] getRoleIds() throws ServiceException;

	/**
	 * Saves roles
	 * @param role
	 * @param person
	 * @param org
	 * @throws ServiceException
	 */
	public void saveRole(Hashtable role, String person, String org) throws ServiceException;

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

}