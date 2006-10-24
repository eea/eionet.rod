package eionet.rod.services.modules.db.dao;

import java.util.Vector;

import eionet.rod.services.ServiceException;

public interface IIssueDao {

	/**
	 * @return
	 * @throws ServiceException
	 */
	public Vector getIssues() throws ServiceException;

	/**
	 * Returns obligation issues
	 * @param id
	 * @return obligation issues
	 * @throws ServiceException
	 */
	public Vector getObligationIssues(Integer id) throws ServiceException;

	/**
	 * @return
	 * @throws ServiceException
	 */
	public String[][] getIssueIdPairs() throws ServiceException;

	/**
	 * Returns issues of an obligations
	 * @param raId
	 * @return issues of an obligations
	 * @throws ServiceException
	 */
	public String[][] getIssues(Integer raId) throws ServiceException;

}