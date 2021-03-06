package eionet.rod.services.modules.db.dao;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import eionet.rod.dto.LookupDTO;
import eionet.rod.dto.ObligationFactsheetDTO;
import eionet.rod.dto.ObligationRdfDTO;
import eionet.rod.dto.ObligationsDueDTO;
import eionet.rod.dto.ObligationsListDTO;
import eionet.rod.dto.SearchDTO;
import eionet.rod.dto.SiblingObligationDTO;
import eionet.rod.dto.UrlDTO;
import eionet.rod.services.ServiceException;

/**
 * Queries for obligations in MySQL.
 *
 * @author sasam
 */
public interface IObligationDao {

    /**
     * Returns deadline data in 2-dimensional array.
     *
     * 0-PK_RA_ID, 1-FIRST_REPORTING, 2-REPORT_FREQ_MONTHS, 3-VALID_TO, 4-TERMINATE
     * @return deadline data in 2-dimensional array
     * @throws ServiceException
     */
    String[][] getDeadlines() throws ServiceException;

    /**
     * Saves next deadlines for given RA.
     *
     * @param raId
     * @param next
     * @param next2
     * @param current
     * @throws ServiceException
     */
    void saveDeadline(Integer raId, String next, String next2, String current) throws ServiceException;

    /**
     * Saves next terminated value for given RA.
     * @param raId
     * @param terminated
     * @throws ServiceException
     */
    void saveTerminate(Integer raId, String terminated) throws ServiceException;

    /**
     * Returns all responsible role ids from T_ACTIVITY_DETAILS.
     * @return all responsible role ids from T_ACTIVITY_DETAILS
     * @throws ServiceException
     */
    String[] getRespRoles() throws ServiceException;

    /**
     * Returns upcoming deadlines.
     * @param days
     * @return pcoming deadlines
     * @throws ServiceException
     */
    Vector<Map<String, String>> getUpcomingDeadlines(double days) throws ServiceException;

    /**
     * Activities used in XML/RPC.
     * @return array of hashes (PK_RA_ID, TITLE, etc)
     * @throws ServiceException
     */
    Vector<Map<String, String>> getActivities() throws ServiceException;

    /**
     * @return List of ObligationRdfDTO objects
     * @throws ServiceException
     */
    List<ObligationRdfDTO> getObligationsForRDF() throws ServiceException;

    /**
     * @param String obligation ID
     * @return ObligationRdfDTO object
     * @throws ServiceException
     */
    ObligationRdfDTO getObligationForRDF(String obligationId) throws ServiceException;

    /**
     * obligations
     * @return
     * @throws ServiceException
     */
    List<String> getSubscribeObligations() throws ServiceException;

    /**
     * Returns next deadlines of activities. Activities not having a deadline are not returned
     *
     * @param issues
     * @param countries
     * @return String[][]  (0:PK_RA_ID, 1:TITLE, 2:NEXT_REPORTING, 3:FK_RO_ID)
     * @throws ServiceException
     */
    String[][] getActivityDeadlines(StringTokenizer issues, StringTokenizer countries) throws ServiceException;

    /**
     * Returns next deadlines of activities. One activity can occure twice, if NEXT_DEADLINE2 is set.
     * Activities, not having a deadline are not returned
     *
     * @param issues
     * @param countries
     * @return String[][]  (0:PK_RA_ID, 1:TITLE, 2:NEXT_REPORTING, 3:FK_RO_ID)
     * @throws ServiceException
     */
    String[][] getAllActivityDeadlines(StringTokenizer issues, StringTokenizer countries) throws ServiceException;

    /**
     * Returns activities, corresponging to the issue ids and country ids, given as parameters.
     * @param issues
     * @param countries
     * @return String[][]  (0:PK_RO_ID, 1:ALIAS, 2:SOURCE.TITLE, 3:FK_SOURCE_ID)
     * @throws ServiceException
     */
    String[][] getIssueActivities(StringTokenizer issues, StringTokenizer countries) throws ServiceException;

    /**
     * Returns obligation by id and client.
     * @param id
     * @return obligation by id and client
     * @throws ServiceException
     */
    Hashtable<String, String> getObligationById(Integer id) throws ServiceException;

    /**
     * Returns obligation details.
     * @param id
     * @return obligation details
     * @throws ServiceException
     */
    Vector<Map<String, String>> getObligationDetail(Integer id) throws ServiceException;

    /**
     * @param id
     * @return
     * @throws ServiceException
     */
    String[][] getParentObligationId(Integer id) throws ServiceException;

    /**
     * @param id
     * @return
     * @throws ServiceException
     */
    String[][] getLatestVersionId(Integer id) throws ServiceException;

    /**
     * @param id
     * @param pid
     * @param latestVersion
     * @return
     * @throws ServiceException
     */
    int getRestoreObligation(Integer id, Integer pid, int latestVersion) throws ServiceException;

    /**
     * @return
     * @throws ServiceException
     */
    String[][] getObligationIds() throws ServiceException;

    /**
     * XML/RPC methods for WebRODService.
     * @return
     * @throws ServiceException
     */
    Vector<Map<String, String>> getROComplete() throws ServiceException;

    /**
     * XML/RPC methods for WebRODService.
     * @return
     * @throws ServiceException
     */
    Vector<Map<String, String>> getROSummary() throws ServiceException;

    /**
     * XML/RPC methods for WebRODService.
     * @return
     * @throws ServiceException
     */
    Vector<Map<String, String>> getRODeadlines() throws ServiceException;

    /**
     * takes DPSIR values from Excel file and insert them into database.
     * @param id
     * @param value
     * @throws ServiceException
     */
    void dpsirValuesFromExcelToDB(int id, String value) throws ServiceException;

    /**
     * DO NOT CALL: TABLE T_PARAMETER doesn't exists anymore according to rodupdate040415.sql.
     * Harvests parameters from link tables and stores in the TEXT field of T_OBLIGATION
     * ONLY if the content of PARAMETERS is EMPTY
     * @param raId report obligation's ID
     * @throws ServiceException
     */
    void harvestParams(Integer raId) throws ServiceException;

    /**
     * Deletes obligation link with issue.
     * @param raId
     * @throws ServiceException
     */
    void deleteIssueLink(Integer raId) throws ServiceException;

    /**
     * Deletes spatial link.
     * @param raId
     * @throws ServiceException
     */
    void deleteSpatialLink(Integer raId) throws ServiceException;


    /**
     * Deletes info link.
     * @param raId
     * @throws ServiceException
     */
    void deleteInfoLink(Integer raId) throws ServiceException;


    /**
     * Deletes obligation from database.
     * @param raId
     * @throws ServiceException
     */
    void deleteObligation(Integer raId) throws ServiceException;


    /**
     * Links obligation to info.
     * @param raId
     * @param infoId
     * @throws ServiceException
     */
    void insertInfoLink(Integer raId, String infoId) throws ServiceException;


    /**
     * Deletes spatial link.
     * @param raId
     * @param spatialId
     * @throws ServiceException
     */
    void deleteSpatialLink(Integer raId, Integer spatialId) throws ServiceException;


    /**
     * Returns all obligations for given source.
     * @param sourceId
     * @return
     * @throws ServiceException
     */
    List<String> getObligationsBySource(Integer sourceId) throws ServiceException;

    /**
     * Returns search list for search.
     * @param spatialId
     * @param clientId
     * @param issueId
     * @param date1
     * @param date2
     * @param dlCase
     * @param order
     * @throws ServiceException
     */
    List<SearchDTO> getSearchObligationsList(String spatialId, String clientId, String issueId, String date1, String date2, String dlCase, String order) throws ServiceException;

    /**
     * Returns obligation factsheet.
     * @param obligationId
     * @throws ServiceException
     */
    ObligationFactsheetDTO getObligationFactsheet(String obligationId) throws ServiceException;

    /**
     * Returns t_lookup info for obligation.
     * @param obligationId
     * @throws ServiceException
     */
    List<LookupDTO> getLookupList(String obligationId) throws ServiceException;

    /**
     * Returns t_lookup info by category.
     * @param category
     * @throws ServiceException
     */
    List<LookupDTO> getLookupListByCategory(String cat) throws ServiceException;

    /**
     * Returns sibling obligations for legislation tab.
     * @param obligationId
     * @throws ServiceException
     */
    List<SiblingObligationDTO> getSiblingObligations(String obligationId) throws ServiceException;

    /**
     * Returns list of obligations.
     * @param anmode
     * @param country
     * @param issue
     * @param client
     * @param terminated
     * @param ccClients
     * @throws ServiceException
     */
    List<ObligationsListDTO> getObligationsList(String anmode, String country, String issue, String client, String terminated, boolean ccClients) throws ServiceException;

    /**
     * Returns list of obligations. Used for XML-RPC method
     * @param anmode
     * @param country
     * @param issue
     * @param client
     * @param terminated
     * @param ccClients
     * @throws ServiceException
     */
    Vector<Hashtable<String, String>> getObligationsVector(String anmode, String country, String issue, String client, String terminated, boolean ccClients) throws ServiceException;

    /**
     * Returns obligations ordered by next update.
     * @throws ServiceException
     */
    List<ObligationsDueDTO> getObligationsDue() throws ServiceException;

    /**
     * Updates obligation.
     * @param obligationId
     * @throws ServiceException
     */
    void editObligation(ObligationFactsheetDTO obligation) throws ServiceException;

    /**
     * Updates obligation.
     * @param obligationDTO
     * @return obligation ID
     * @throws ServiceException
     */
    Integer insertObligation(ObligationFactsheetDTO obligation) throws ServiceException;

    /**
     * @return list of urls in T_OBLIGATION
     * @throws ServiceException
     */
    List<UrlDTO> getObligationsUrls() throws ServiceException;
}

