package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.RODUtil;
import eionet.rod.dto.ObligationDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 *
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/issues/{idissue}")
public class IssuesActionBean extends AbstractRODActionBean {

    private String idissue;
    private String name;
    private List<ObligationDTO> obligations;

    /**
     *
     * @return Resolution
     * @throws ServiceException
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {

        if (!RODUtil.isNullOrEmpty(idissue)) {
            name = RODServices.getDbService().getIssueDao().getIssueNameById(idissue);
            obligations = RODServices.getDbService().getIssueDao().getIssueObligationsList(idissue);
        }

        return new ForwardResolution("/pages/issue.jsp");
    }

    public String getIdissue() {
        return idissue;
    }

    public void setIdissue(String idissue) {
        this.idissue = idissue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ObligationDTO> getObligations() {
        return obligations;
    }

    public void setObligations(List<ObligationDTO> obligations) {
        this.obligations = obligations;
    }

}
