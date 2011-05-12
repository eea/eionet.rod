package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.dto.ClientDTO;
import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.IssueDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 * 
 */
@UrlBinding("/index.html")
public class IndexActionBean extends AbstractRODActionBean {

    private List<CountryDTO> formCountries;
    private List<IssueDTO> formIssues;
    private List<ClientDTO> formClients;
    private String introduction;
    private String twoBoxes;

    /**
     * 
     * @return Resolution
     * @throws ServiceException
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {

        introduction = RODServices.getDbService().getHelpDao().getHelpAreaHtml("Introduction");
        twoBoxes = RODServices.getDbService().getHelpDao().getHelpAreaHtml("Two_boxes");

        formCountries = RODServices.getDbService().getSpatialDao().getCountriesList();
        formIssues = RODServices.getDbService().getIssueDao().getIssuesList();
        formClients = RODServices.getDbService().getClientDao().getClientsList();

        return new ForwardResolution("/pages/index.jsp");
    }

    public List<CountryDTO> getFormCountries() {
        return formCountries;
    }

    public void setFormCountries(List<CountryDTO> formCountries) {
        this.formCountries = formCountries;
    }

    public List<IssueDTO> getFormIssues() {
        return formIssues;
    }

    public void setFormIssues(List<IssueDTO> formIssues) {
        this.formIssues = formIssues;
    }

    public List<ClientDTO> getFormClients() {
        return formClients;
    }

    public void setFormClients(List<ClientDTO> formClients) {
        this.formClients = formClients;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTwoBoxes() {
        return twoBoxes;
    }

    public void setTwoBoxes(String twoBoxes) {
        this.twoBoxes = twoBoxes;
    }

}
