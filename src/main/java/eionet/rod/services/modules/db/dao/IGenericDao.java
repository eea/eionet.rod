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
    Vector<Map<String, String>> getTable(String tablename) throws ServiceException;

    /**
     * @param tablename
     * @return
     * @throws ServiceException
     */
    Vector<Hashtable<String, String>> getTableDesc(String tablename) throws ServiceException;

    /**
     * @param id
     * @param table
     * @return
     * @throws ServiceException
     */
    boolean isIdAvailable(String id, String table) throws ServiceException ;


    /**
     * @return
     * @throws ServiceException
     */
    String getLastUpdate() throws ServiceException ;

}
