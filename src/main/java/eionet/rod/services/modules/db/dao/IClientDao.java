package eionet.rod.services.modules.db.dao;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import eionet.rod.dto.ClientDTO;
import eionet.rod.services.ServiceException;

public interface IClientDao {

    /**
     * @param clientId
     * @return client name
     * @throws ServiceException
     */
    String getOrganisationNameByID(String clientId) throws ServiceException;

    /**
     * @return
     * @throws ServiceException
     */
    Vector<Map<String, String>> getOrganisations() throws ServiceException;

    /**
     * Returns obligation organisations
     * @param obligationId
     * @return obligation organisations
     * @throws ServiceException
     */
    Vector<Map<String, String>> getObligationOrg(Integer obligationId) throws ServiceException;


    /**
     * Deletes link with an obligation.
     * @param raId
     * @throws ServiceException
     */
    void deleteObligationLink(Integer raId) throws ServiceException;

    /**
     * @param clientId
     * @param objectId
     * @param status
     * @param type
     * @throws ServiceException
     */
    void insertClientLink(Integer clientId, Integer objectId, String status, String type) throws ServiceException;



    /**
     * Deletes source link.
     * @param srcId
     * @throws ServiceException
     */
    void deleteSourceLink(Integer srcId) throws ServiceException;


    /**
     * Deletes all linked parameter and medium records and in delete mode also the self record.
     * @param objectId
     * @throws ServiceException
     */
    void deleteParameterLink(Integer objectId) throws ServiceException;

    /**
     * @return all clients as list of ClientDTOs
     * @throws ServiceException
     */
    List<ClientDTO> getClientsList() throws ServiceException;

    /**
     * Returns clients.
     * @param objectId
     * @throws ServiceException
     */
    List<ClientDTO> getClients(String objectId) throws ServiceException;

    /**
     * Returns clients.
     * @param clientIds
     * @throws ServiceException
     */
    List<ClientDTO> getClients(List<String> clientIds) throws ServiceException;

    /**
     * Returns list of all clients.
     * @throws ServiceException
     */
    List<ClientDTO> getAllClients() throws ServiceException;

    /**
     * Returns list of all clients.
     * @throws ServiceException
     */
    List<ClientDTO> getSubscribeClients() throws ServiceException;

    /**
     * Returns client factsheet.
     * @param clientId
     * @throws ServiceException
     */
    ClientDTO getClientFactsheet(String id) throws ServiceException;

    /**
     * Updates client.
     * @param client
     * @throws ServiceException
     */
    void editClient(ClientDTO client) throws ServiceException;

    /**
     * Inserts new client.
     * @param client
     * @throws ServiceException
     */
    Integer addClient(ClientDTO client) throws ServiceException;

    /**
     * Inserts obligation clients.
     * @param obligation ID
     * @param list of selected clients
     * @throws ServiceException
     */
    void insertObligationClients(String obligationId, List<String> selectedClients) throws ServiceException;
}
