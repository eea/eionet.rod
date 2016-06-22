package eionet.rod.services.modules.db.dao;

import eionet.rod.services.ServiceException;

public interface IAnalysisDao {

    /**
     * Returns total number of obligations.
     *
     * @return total number of obligations as int
     * @throws ServiceException
     */
    int getTotalRa() throws ServiceException;

    /**
     * Returns last update of obligations.
     * 
     * @return last update of obligations as String
     * @throws ServiceException
     */
    String getLastUpdateRa() throws ServiceException;

    /**
     * Returns total number of legislative instruments.
     *
     * @return total number of legislative instruments as int
     * @throws ServiceException
     */
    int getTotalLi() throws ServiceException;

    /**
     * Returns last update of legislative instruments.
     * 
     * @return last update of legislative instruments as String
     * @throws ServiceException
     */
    String getLastUpdateLi() throws ServiceException;

    /**
     * Number of reporting obligations used for the EEA Core set of indicators.
     *
     * @throws ServiceException
     */
    int getEeaCore() throws ServiceException;

    /**
     * Number of reporting obligations used for the Eionet core data flows.
     *
     * @throws ServiceException
     */
    int getEeaPriority() throws ServiceException;

    /**
     * Number of reporting obligations where the delivery process or content overlaps with another reporting obligation.
     *
     * @throws ServiceException
     */
    int getOverlapRa() throws ServiceException;

    /**
     * Number of reporting obligations where the delivery process is managed by EEA.
     *
     * @throws ServiceException
     */
    int getFlaggedRa() throws ServiceException;

    /**
     * Number of instruments.
     *
     * @throws ServiceException
     */
    int getInstrumentsDue() throws ServiceException;

    /**
     * Number of reporting obligations with no issue allocated.
     *
     * @throws ServiceException
     */
    int getNoIssueAllocated() throws ServiceException;

}
