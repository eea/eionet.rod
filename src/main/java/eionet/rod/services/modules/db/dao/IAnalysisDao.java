package eionet.rod.services.modules.db.dao;

import eionet.rod.services.ServiceException;

public interface IAnalysisDao {
	
	/**
	 * Returns total number of obligations
	 * @return total number of obligations as int
	 * @throws ServiceException
	 */
	public int getTotalRa() throws ServiceException;
	
	/**
	 * Returns last update of obligations
	 * @return last update of obligations as String
	 * @throws ServiceException
	 */
	public String getLastUpdateRa() throws ServiceException;
	
	/**
	 * Returns total number of legislative instruments
	 * @return total number of legislative instruments as int
	 * @throws ServiceException
	 */
	public int getTotalLi() throws ServiceException;
	
	/**
	 * Returns last update of legislative instruments
	 * @return last update of legislative instruments as String
	 * @throws ServiceException
	 */
	public String getLastUpdateLi() throws ServiceException;
	
	/**
	 * Number of reporting obligations used for the EEA Core set of indicators
	 * @throws ServiceException
	 */
	public int getEeaCore() throws ServiceException;
	
	/**
	 * Number of reporting obligations used for the EIONET Priority Data flows
	 * @throws ServiceException
	 */
	public int getEeaPriority() throws ServiceException;
	
	/**
	 * Number of reporting obligations where the delivery process or content overlaps with another reporting obligation
	 * @throws ServiceException
	 */
	public int getOverlapRa() throws ServiceException;
	
	/**
	 * Number of reporting obligations flagged 
	 * @throws ServiceException
	 */
	public int getFlaggedRa() throws ServiceException;
	
	/**
	 * Number of instruments 
	 * @throws ServiceException
	 */
	public int getInstrumentsDue() throws ServiceException;
	
	/**
	 * Number of reporting obligations with no issue allocated
	 * @throws ServiceException
	 */
	public int getNoIssueAllocated() throws ServiceException;

}
