package eionet.rod.services.modules.db.dao;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import eionet.rod.dto.IssueDTO;
import eionet.rod.dto.ObligationDTO;
import eionet.rod.services.ServiceException;

public interface IIssueDao {

    /**
     * Returns obligation issues
     * @param id
     * @return obligation issues
     * @throws ServiceException
     */
    Vector<Map<String, String>> getObligationIssues(Integer id) throws ServiceException;


    /**
     * @return list of all issues
     * @throws ServiceException
     */
    List<IssueDTO> getIssuesList() throws ServiceException;

    /**
     * @return list of obligations with same issue
     * @param issue id
     * @throws ServiceException
     */
    List<ObligationDTO> getIssueObligationsList(String issueId) throws ServiceException;

    /**
     * @return list of obligation issues
     * @param obligation id
     * @throws ServiceException
     */
    List<IssueDTO> getObligationIssuesList(String obligationId) throws ServiceException;

    /**
     * @return list of obligation issues
     * @param issue ids
     * @throws ServiceException
     */
    List<IssueDTO> getObligationIssuesList(List<String> issueIds) throws ServiceException;

    /**
     * @return issue name
     * @param issue id
     * @throws ServiceException
     */
    String getIssueNameById(String id) throws ServiceException;

    /**
     * @param obligation id
     * @param list of selected issues
     * @throws ServiceException
     */
    void insertObligationIssues(String obligationId, List<String> selectedIssues) throws ServiceException;

}
