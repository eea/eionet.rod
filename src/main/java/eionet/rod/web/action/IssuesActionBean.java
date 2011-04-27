package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.RODUtil;
import eionet.rod.dto.IssueDTO;
import eionet.rod.dto.ObligationDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import eionet.rod.web.util.SeeOtherRedirectResolution;

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


    private final String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<rdf:RDF  xmlns:rod=\"http://rod.eionet.europa.eu/schema.rdf#\" \n" +
            "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" \n" +
            "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\">\n";

    private static final String footer = "\n</rdf:RDF>";

    /**
     *
     * @return
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {

        String acceptHeader = getContext().getRequest().getHeader("accept");
        String[] accept = null;
        if (acceptHeader != null && acceptHeader.length() > 0)
            accept = acceptHeader.split(",");

        if (!RODUtil.isNullOrEmpty(idissue)) {
            name = RODServices.getDbService().getIssueDao().getIssueNameById(idissue);
            obligations = RODServices.getDbService().getIssueDao().getIssueObligationsList(idissue);

            if (accept != null && accept.length > 0 && accept[0].equals("application/rdf+xml")) {
                return new SeeOtherRedirectResolution("/issues");
            }
        } else {
            List<IssueDTO> issues = RODServices.getDbService().getIssueDao().getIssuesList();

            StringBuffer out = new StringBuffer();
            out.append(header);
            for(IssueDTO issue : issues) {
                out.append("<rod:Issue rdf:about=\"http://rod.eionet.europa.eu/issues/").append(issue.getIssueId()).append("\">\n");
                out.append("<rod:issueName>").append(RODUtil.replaceTags(issue.getName(),true,true)).append("</rod:issueName>\n");
                List<ObligationDTO> obligations = RODServices.getDbService().getIssueDao().getIssueObligationsList(issue.getIssueId().toString());
                for(ObligationDTO obligation : obligations) {
                    out.append("<rod:issueOf rdf:resource=\"http://rod.eionet.europa.eu/obligations/").append(obligation.getObligationId()).append("\"/>\n");
                }
                out.append("</rod:Issue>");
            }
            out.append(footer);

            return new StreamingResolution("application/rdf+xml;charset=UTF-8",out.toString());
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
