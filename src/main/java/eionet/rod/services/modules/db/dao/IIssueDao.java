package eionet.rod.services.modules.db.dao;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import eionet.rod.dto.IssueDTO;
import eionet.rod.dto.ObligationDTO;
import eionet.rod.services.ServiceException;

public interface IIssueDao {

    /**
     * @return
     * @throws ServiceException
     */
    public Vector<Map<String,String>> getIssues() throws ServiceException;

    /**
     * Returns obligation issues
     * @param id
     * @return obligation issues
     * @throws ServiceException
     */
    public Vector<Map<String,String>> getObligationIssues(Integer id) throws ServiceException;

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

    /**
     * @return list of all issues
     * @throws ServiceException
     */
    public List<IssueDTO> getIssuesList() throws ServiceException;

    /**
     * @return list of obligations with same issue
     * @param issue id
     * @throws ServiceException
     */
    public List<ObligationDTO> getIssueObligationsList(String issueId) throws ServiceException;

    /**
     * @return list of obligation issues
     * @param obligation id
     * @throws ServiceException
     */
    public List<IssueDTO> getObligationIssuesList(String obligationId) throws ServiceException;

    /**
     * @return list of obligation issues
     * @param issue ids
     * @throws ServiceException
     */
    public List<IssueDTO> getObligationIssuesList(List<String> issueIds) throws ServiceException;

    /**
     * @return issue name
     * @param issue id
     * @throws ServiceException
     */
    public String getIssueNameById(String id) throws ServiceException;

    /**
     * @param obligation id
     * @param list of selected issues
     * @throws ServiceException
     */
    public void insertObligationIssues(String obligationId, List<String> selectedIssues) throws ServiceException;

}