package eionet.rod.services.modules.db.dao;

import eionet.rod.services.ServiceException;

public interface IAclDao {

    /**
     * @param aclPath
     * @param owner
     * @param description
     * @throws ServiceException
     */
    public void addAcl(String aclPath, String owner, String description) throws ServiceException;

    /**
     * @param acl_name
     * @param type
     * @return
     * @throws ServiceException
     */
    public String getAclId(String acl_name, String type) throws ServiceException;
}