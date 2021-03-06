package eionet.rod.services.modules.db.dao;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import eionet.rod.dto.VersionDTO;
import eionet.rod.services.ServiceException;

/**
 * @author sasam
 *
 */
public interface IUndoDao {

    /**
     * @param id
     * @param tab
     * @param id_field
     * @return
     * @throws ServiceException
     */
    Hashtable getPreviousActions(String id, String tab, String id_field) throws ServiceException;

    /**
     * Returns all previous actions
     * @return
     * @throws ServiceException
     */
    List<VersionDTO> getPreviousActionsGeneral() throws ServiceException;

    /**
     * @param id
     * @param tab
     * @param id_field
     * @return
     * @throws ServiceException
     */
    List<VersionDTO> getPreviousActionsReportSpecific(String id, String tab, String id_field) throws ServiceException;

    /**
     * Insert obligations related to instrument into T_UNDO table
     * @param item_type
     * @return
     * @throws ServiceException
     */
    Vector getDeletedFromUndo(String item_type) throws ServiceException;

    void addObligationIdsIntoUndo(Integer id, long ts, String table) throws ServiceException;

    /**
     * Insert transaction info into T_UNDO table
     * @param id
     * @param state
     * @param table
     * @param id_field
     * @param ts
     * @param extraSQL
     * @throws ServiceException
     */
    void insertTransactionInfo(String id, String state, String table, String id_field, long ts, String extraSQL) throws ServiceException;

    /**
     * @param con
     * @param id
     * @param state
     * @param table
     * @param id_field
     * @param ts
     * @param extraSQL
     * @param show
     * @param whereClause
     * @return
     * @throws ServiceException
     */
    boolean insertIntoUndo(Connection con, String id, String state, String table, String id_field, long ts, String extraSQL, String show, String whereClause) throws ServiceException;

    /**
     * Insert into T_UNDO table
     * @param ts
     * @param tab
     * @param op
     * @param id
     * @return
     * @throws ServiceException
     */
    String undo(long ts, String tab, String op, String id) throws ServiceException;

    /**
     * @param ts
     * @param op
     * @param tab
     * @param id
     * @return
     * @throws ServiceException
     */
    Vector<Map<String, String>> getUndoInformation(long ts, String op, String tab, String id) throws ServiceException;

    /**
     * Returns user who performed the action
     * @param ts
     * @param tab
     * @return
     * @throws ServiceException
     */
    String getUndoUser(long ts, String tab) throws ServiceException;

    /**
     * Returns undo object id
     * @param ts
     * @param tab
     * @param col
     * @return
     * @throws ServiceException
     */
    String getUndoObjetcId(long ts, String tab, String col) throws ServiceException;

    /**
     * @param id
     * @return
     * @throws ServiceException
     */
    String areRelatedObligationsIdsAvailable(String id) throws ServiceException;


    /**
     * Inserts record into T_UNDO table
     * @param ts
     * @param table
     * @param column
     * @param state
     * @param quotes
     * @param isPrimary
     * @param value
     * @param rowCnt
     * @param show
     * @throws ServiceException
     */
    void insertIntoUndo(long ts, String table, String column, String state, String quotes, String isPrimary, String value, int rowCnt , String show) throws ServiceException;

    /**
     * @return 100 most recent ROD updates
     * @param id
     * @param object
     * @throws ServiceException
     */
    List<VersionDTO> getUpdateHistory(String id, String object) throws ServiceException;

    /**
     * Returns undo object title
     * @param ts
     * @param tab
     * @return
     * @throws ServiceException
     */
    String getUndoObjectTitle(long ts, String tab) throws ServiceException;

    /**
     * @return 100 users most recent ROD updates
     * @param username
     * @throws ServiceException
     */
    List<VersionDTO> getUpdateHistoryByUser(String username) throws ServiceException;

    /**
     * @return deleted obligations/instruments
     * @param type
     * @throws ServiceException
     */
    List<VersionDTO> getDeleted(String type) throws ServiceException;
}
