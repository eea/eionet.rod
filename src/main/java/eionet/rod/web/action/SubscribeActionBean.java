package eionet.rod.web.action;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcClient;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.Constants;
import eionet.rod.RODUtil;
import eionet.rod.dto.ClientDTO;
import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.IssueDTO;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author altnyris
 * 
 */
@UrlBinding("/subscribe")
public class SubscribeActionBean extends AbstractRODActionBean {

    private String mySubscriptionUrl;

    private String id;
    private String sid;
    private List<CountryDTO> countries;
    private String selectedCountry;
    private List<IssueDTO> issues;
    private String selectedIssue;
    private String clientName;
    private String clientId;
    private List<ClientDTO> clients;
    private String selectedClient;
    private List<String> eventType;
    private String obligationName;
    private String obligationId;
    private List<String> obligations;
    private String selectedObligation;
    private String instrumentName;
    private List<String> instruments;
    private String selectedInstrument;

    /**
     * 
     * @return Resolution
     * @throws ServiceException
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {

        String forwardPage = "/pages/subscribe.jsp";

        String userName = getUserName();
        if (RODUtil.isNullOrEmpty(userName)) {
            handleRodException("You are not logged in!", Constants.SEVERITY_WARNING);
            return new ForwardResolution(forwardPage);
        }

        mySubscriptionUrl = RODServices.getFileService().getStringProperty(FileServiceIF.UNS_MY_SUBSCRIPTIONS_URL)
                + RODServices.getFileService().getStringProperty(FileServiceIF.UNS_CHANNEL_NAME);

        countries = RODServices.getDbService().getSpatialDao().getCountriesList();
        issues = RODServices.getDbService().getIssueDao().getIssuesList();
        if (id != null && !id.equals("")) {
            Hashtable<String, String> clientHash = RODServices.getDbService().getObligationDao()
                    .getObligationById((new Integer(id)).intValue());
            clientName = (String) clientHash.get("client");
            obligationName = (String) clientHash.get("title");
        } else {
            clients = RODServices.getDbService().getClientDao().getSubscribeClients();
            obligations = RODServices.getDbService().getObligationDao().getSubscribeObligations();
        }

        if (sid != null && !sid.equals("")) {
            Hashtable<String, String> sourceHash = RODServices.getDbService().getSourceDao()
                    .getInstrumentById((new Integer(sid)).intValue());
            instrumentName = (String) sourceHash.get("TITLE");
        } else {
            instruments = RODServices.getDbService().getSourceDao().getSubscribeInstruments();
        }

        return new ForwardResolution(forwardPage);
    }

    public Resolution subscribe() throws ServiceException {
        String forwardPage = "/pages/subscribe.jsp";

        String userName = getUserName();
        if (RODUtil.isNullOrEmpty(userName)) {
            handleRodException("You are not logged in!", Constants.SEVERITY_WARNING);
            return new ForwardResolution(forwardPage);
        }

        try {
            FileServiceIF fileSrv = RODServices.getFileService();

            String predEventType = fileSrv.getStringProperty(FileServiceIF.UNS_EVENTTYPE_PREDICATE);
            String server_url = fileSrv.getStringProperty(FileServiceIF.UNS_XMLRPC_SERVER_URL);
            String CHANNEL_NAME = fileSrv.getStringProperty(FileServiceIF.UNS_CHANNEL_NAME);

            Vector<Hashtable<String, String>> filters = new Vector<Hashtable<String, String>>();
            Hashtable<String, String> filter = new Hashtable<String, String>();
            if (eventType != null) {
                for (Iterator<String> it = eventType.iterator(); it.hasNext();) {
                    String event = it.next();
                    filter = new Hashtable<String, String>();
                    if (event != null && event.length() > 0)
                        filter.put(predEventType, event);

                    String predCountry = fileSrv.getStringProperty(FileServiceIF.UNS_COUNTRY_PREDICATE);
                    if (selectedCountry != null && selectedCountry.length() > 0)
                        filter.put(predCountry, selectedCountry);

                    String predIssue = fileSrv.getStringProperty(FileServiceIF.UNS_ISSUE_PREDICATE);
                    if (selectedIssue != null && selectedIssue.length() > 0)
                        filter.put(predIssue, selectedIssue);

                    String predOrganisation = fileSrv.getStringProperty(FileServiceIF.UNS_ORGANISATION_PREDICATE);
                    if (selectedClient != null && selectedClient.length() > 0)
                        filter.put(predOrganisation, selectedClient);

                    String predObligation = fileSrv.getStringProperty(FileServiceIF.UNS_OBLIGATION_PREDICATE);
                    if (selectedObligation != null && selectedObligation.length() > 0)
                        filter.put(predObligation, selectedObligation);

                    String predInstrument = fileSrv.getStringProperty(FileServiceIF.UNS_INSTRUMENT_PREDICATE);
                    if (selectedInstrument != null && selectedInstrument.length() > 0)
                        filter.put(predInstrument, selectedInstrument);

                    if (filter.size() > 0)
                        filters.add(filter);
                }
            }

            if (filters.size() > 0) {
                XmlRpcClient server = new XmlRpcClient(server_url);
                server.setBasicAuthentication(fileSrv.getStringProperty(FileServiceIF.UNS_USERNAME),
                        fileSrv.getStringProperty(FileServiceIF.UNS_PWD));
                // make subscription
                Vector<Object> params = new Vector<Object>();
                params.add(CHANNEL_NAME);
                params.add(getUserName());
                params.add(filters);
                server.execute(fileSrv.getStringProperty(FileServiceIF.UNS_MAKE_SUBSCRIPTION), params);
            }

            showMessage("Subscription successful!");

        } catch (Exception e) {
            e.printStackTrace();
            handleRodException(e.getMessage(), Constants.SEVERITY_WARNING);
        }

        init();
        return new ForwardResolution(forwardPage);
    }

    public List<CountryDTO> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryDTO> countries) {
        this.countries = countries;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public List<IssueDTO> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueDTO> issues) {
        this.issues = issues;
    }

    public String getSelectedIssue() {
        return selectedIssue;
    }

    public void setSelectedIssue(String selectedIssue) {
        this.selectedIssue = selectedIssue;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<ClientDTO> getClients() {
        return clients;
    }

    public void setClients(List<ClientDTO> clients) {
        this.clients = clients;
    }

    public String getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(String selectedClient) {
        this.selectedClient = selectedClient;
    }

    public String getObligationName() {
        return obligationName;
    }

    public void setObligationName(String obligationName) {
        this.obligationName = obligationName;
    }

    public String getObligationId() {
        return obligationId;
    }

    public void setObligationId(String obligationId) {
        this.obligationId = obligationId;
    }

    public List<String> getObligations() {
        return obligations;
    }

    public void setObligations(List<String> obligations) {
        this.obligations = obligations;
    }

    public String getSelectedObligation() {
        return selectedObligation;
    }

    public void setSelectedObligation(String selectedObligation) {
        this.selectedObligation = selectedObligation;
    }

    public List<String> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<String> instruments) {
        this.instruments = instruments;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public String getSelectedInstrument() {
        return selectedInstrument;
    }

    public void setSelectedInstrument(String selectedInstrument) {
        this.selectedInstrument = selectedInstrument;
    }

    public String getMySubscriptionUrl() {
        return mySubscriptionUrl;
    }

    public void setMySubscriptionUrl(String mySubscriptionUrl) {
        this.mySubscriptionUrl = mySubscriptionUrl;
    }

    public List<String> getEventType() {
        return eventType;
    }

    public void setEventType(List<String> eventType) {
        this.eventType = eventType;
    }

}
