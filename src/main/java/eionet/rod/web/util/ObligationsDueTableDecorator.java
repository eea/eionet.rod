package eionet.rod.web.util;

import java.util.Iterator;
import java.util.List;

import org.displaytag.decorator.TableDecorator;

import eionet.rod.RODUtil;
import eionet.rod.dto.IssueDTO;
import eionet.rod.dto.ObligationsDueDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author altnyris
 * 
 */
public class ObligationsDueTableDecorator extends TableDecorator {

    /**
     * 
     * @return String
     */
    public String getTitle() {

        StringBuilder ret = new StringBuilder();
        ObligationsDueDTO obligation = (ObligationsDueDTO) getCurrentRowObject();
        ret.append("<a href='obligations/").append(obligation.getObligationId()).append("'>");
        if (!RODUtil.isNullOrEmpty(obligation.getObligationTitle()))
            ret.append(RODUtil.replaceTags(RODUtil.threeDots(obligation.getObligationTitle(), 55), true, true));
        ret.append("</a>");

        return ret.toString();
    }

    /**
     * 
     * @return String
     */
    public String getId() {

        StringBuilder ret = new StringBuilder();
        ObligationsDueDTO obligation = (ObligationsDueDTO) getCurrentRowObject();
        ret.append("<a href='obligations/").append(obligation.getObligationId()).append("'>");
        ret.append(obligation.getObligationId());
        ret.append("</a>");

        return ret.toString();
    }

    /**
     * 
     * @return String
     * @throws ServiceException
     */
    public String getIssues() throws ServiceException {

        StringBuilder ret = new StringBuilder();
        ObligationsDueDTO obligation = (ObligationsDueDTO) getCurrentRowObject();
        List<IssueDTO> issues = RODServices.getDbService().getIssueDao()
                .getObligationIssuesList(obligation.getObligationId().toString());
        for (Iterator<IssueDTO> it = issues.iterator(); it.hasNext();) {
            IssueDTO issue = it.next();
            ret.append(RODUtil.replaceTags(issue.getName(), true, true));
            if (it.hasNext())
                ret.append("<br/>");
        }

        return ret.toString();
    }

}
