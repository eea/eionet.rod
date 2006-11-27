package eionet.rod.services.modules.db.dao;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Vector;

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
	public Hashtable getPreviousActions(String id, String tab, String id_field) throws ServiceException;

	/**
	 * Insert obligations related to instrument into T_UNDO table
	 * @param item_type
	 * @return
	 * @throws ServiceException
	 */
	public Vector getDeletedFromUndo(String item_type) throws ServiceException;

	public void addObligationIdsIntoUndo(Integer id, long ts, String table) throws ServiceException;

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
	public void insertTransactionInfo(String id, String state, String table, String id_field, long ts, String extraSQL) throws ServiceException;

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
	public boolean insertIntoUndo(Connection con, String id, String state, String table, String id_field, long ts, String extraSQL, String show, String whereClause) throws ServiceException;

	/**
	 * Insert into T_UNDO table
	 * @param ts
	 * @param tab
	 * @param op
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public String undo(long ts, String tab, String op, String id) throws ServiceException;

	/**
	 * @param ts
	 * @param op
	 * @param tab
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public Vector getUndoInformation(long ts, String op, String tab, String id) throws ServiceException;

	/**
	 * Returns user who performed the action
	 * @param ts
	 * @param tab
	 * @return
	 * @throws ServiceException
	 */
	public String getUndoUser(long ts, String tab) throws ServiceException;

	/**
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public String areRelatedObligationsIdsAvailable(String id) throws ServiceException;

	
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
	public void insertIntoUndo(long ts, String table, String column, String state, String quotes, String isPrimary, String value, int rowCnt , String show) throws ServiceException;
}