package eionet.rod.services.modules.db.dao;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import eionet.rod.dto.HarvestHistoryDTO;
import eionet.rod.services.ServiceException;

public interface IHistoryDao {


	/**
	 * Type for RO in HISTORY table
	 */
	public static final String RO_LOG_TYPE = "O";

	/**
	 * Type for RA in HISTORY table
	 */
	public static final String RA_LOG_TYPE = "A";

	/**
	 * Type for LI in HISTORY table
	 */
	public static final String LI_LOG_TYPE = "L";

	/**
	 * Action type for UPDATE statements in HISTORY table
	 */
	public static final String UPDATE_ACTION_TYPE = "U";

	/**
	 * Action type for DELETE statements in HISTORY table
	 */
	public static final String DELETE_ACTION_TYPE = "D";

	/**
	 * Action type for INSERT statements in HISTORY table
	 */
	public static final String INSERT_ACTION_TYPE = "I";

	/**
	 * Returns the change history of the item
	 * @param String itemType
	 * @param String itemId
	 * @return String[][]  (0:log_time, 1:action_type, 2:user, 3:description)
	 * @throws ServiceException
	 */
	public String[][] getItemHistory(String itemType, int itemId) throws ServiceException;

	/**
	 * Returns deleted items of this type
	 * @param String itemType
	 * @return String[][]  (0:ITEM_ID, 1:LOG_TIME, 2:USER )
	 * @throws ServiceException
	 */
	public String[][] getDeletedItems(String itemType) throws ServiceException;

	/**
	 * Returns deleted items of this type
	 * @param String itemType
	 * @return Vector  (0:ITEM_ID, 1:LOG_TIME, 2:USER, 3:ACTION_TYPE, 4:ITEM_TYPE )
	 * @throws ServiceException
	 */
	public Hashtable<String,Object> getDeletedItemsVector(String itemType) throws ServiceException;

	/**
	 * Returns history of current object
	 * @param id
	 * @param tab
	 * @return history of current object
	 * @throws ServiceException
	 */
	public Vector<Map<String,String>> getHistory(int id, String tab) throws ServiceException;

	
	/**
	 * Logs record insert/update/delete history
	 * @param itemType
	 * @param itemId
	 * @param userName
	 * @param actionType
	 * @param description
	 * @throws ServiceException
	 */
	public void logHistory(String itemType, String itemId, String userName, String actionType, String description) throws ServiceException ;
	
	/**
	 * Returns list of harvesting history
	 * @return List of HarvestHistoryDTO
	 * @throws ServiceException
	 */
	public List<HarvestHistoryDTO> getHarvestHistory() throws ServiceException;
}