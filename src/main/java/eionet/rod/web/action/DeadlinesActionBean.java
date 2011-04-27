package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import eionet.rod.RODUtil;
import eionet.rod.dto.SearchDTO;
import eionet.rod.dto.ClientDTO;
import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.IssueDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 *
 * @author altnyris
 *
 */
@UrlBinding("/deadlines")
public class DeadlinesActionBean extends AbstractRODActionBean {

    private List<CountryDTO> countries;
    private List<IssueDTO> issues;
    private List<ClientDTO> clients;

    private List<SearchDTO> searchList;

    private String spatialId;
    private String spatialName;
    private String issueId;
    private String clientId;
    private String deadlines;
    private String date1;
    private String date2;
    private String order;


    /**
     *
     * @return
     */
    @DefaultHandler
    public Resolution search() throws ServiceException {
        countries = RODServices.getDbService().getSpatialDao().getCountriesList();
        issues = RODServices.getDbService().getIssueDao().getIssuesList();
        clients = RODServices.getDbService().getClientDao().getClientsList();

        if (!RODUtil.isNullOrEmpty(spatialId))
            spatialName = RODServices.getDbService().getSpatialDao().getCountryById(new Integer(spatialId).intValue());

        searchList = RODServices.getDbService().getObligationDao().getSearchObligationsList(spatialId, clientId, issueId, date1, date2, deadlines, order);
        return new ForwardResolution("/pages/deadlines.jsp");
    }

    public List<CountryDTO> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryDTO> countries) {
        this.countries = countries;
    }

    public List<IssueDTO> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueDTO> issues) {
        this.issues = issues;
    }

    public List<ClientDTO> getClients() {
        return clients;
    }

    public void setClients(List<ClientDTO> clients) {
        this.clients = clients;
    }

    public String getSpatialId() {
        return spatialId;
    }

    public void setSpatialId(String spatialId) {
        this.spatialId = spatialId;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<SearchDTO> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<SearchDTO> searchList) {
        this.searchList = searchList;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getDeadlines() {
        return deadlines;
    }

    public void setDeadlines(String deadlines) {
        this.deadlines = deadlines;
    }

    public String getSpatialName() {
        return spatialName;
    }

    public void setSpatialName(String spatialName) {
        this.spatialName = spatialName;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}
