package eionet.rod.services.modules.db.dao;

import java.util.Hashtable;

import eionet.rod.dto.ResponsibleRoleDTO;
import eionet.rod.services.ServiceException;

public interface IRoleDao {

    /**
     * Returns all role IDs existing in T_ROLE table
     * @return All role IDs existing in T_ROLE tablke
     * @throws ServiceException
     */
    String[][] getRoleIds() throws ServiceException;

    /**
     * Saves roles
     * @param role
     * @throws ServiceException
     */
    void saveRole(Hashtable<String, Object> role) throws ServiceException;

    /**
     * @throws ServiceException
     */
    void backUpRoles() throws ServiceException;

    /**
     * @throws ServiceException
     */
    void commitRoles() throws ServiceException;

    /**
     * Returns role information
     * @param role_id
     * @param role_name
     * @return role information
     * @throws ServiceException
     */
    ResponsibleRoleDTO getRoleDesc(String role_id, String role_name) throws ServiceException;


    /**
     * Returns true if role exists
     * @param role_id
     * @throws ServiceException
     */
    boolean checkRole(String role_id) throws ServiceException;

}
