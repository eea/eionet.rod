package eionet.rod.services.modules.db.dao;

import java.util.List;

import eionet.rod.dto.HistDeadlineDTO;
import eionet.rod.services.ServiceException;

public interface IHistoricDeadlineDao {

	/**
	 * Returns historical deadlines between given range
	 * @param start_date
	 * @param end_date
	 * @return historical deadlines between given range
	 * @throws ServiceException
	 */
	public List<HistDeadlineDTO> getHistoricDeadlines(String start_date, String end_date) throws ServiceException;
	
	
	/**
	 * Deletes historical deadlines for an obligation
	 * @param raId
	 * @throws ServiceException
	 */
	public void  deleteByObligationId(Integer raId) throws ServiceException;

}