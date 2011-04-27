package eionet.rod.services.modules.db.dao;

import eionet.rod.services.ServiceException;

public interface ISpatialHistoryDao {

    /**
     * @param raId
     * @param spatialId
     * @param voluntary
     * @throws ServiceException
     */
    public void logSpatialHistory(int raId, int spatialId, String voluntary) throws ServiceException;

    /**
     * @param start
     * @param end
     * @param spatialHistoryID
     * @param ra_id
     * @throws ServiceException
     */
    public void editPeriod(String start, String end, String spatialHistoryID, String ra_id) throws ServiceException;

    /**
     * Updates end date for to now for an obligation
     * @param raId
     * @throws ServiceException
     */
    public void updateEndDateForObligation(Integer raId) throws ServiceException;
}