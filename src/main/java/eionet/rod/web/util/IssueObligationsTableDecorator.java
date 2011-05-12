package eionet.rod.web.util;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.decorator.TableDecorator;
import eionet.rod.RODUtil;
import eionet.rod.dto.ObligationDTO;

/**
 * 
 * @author altnyris
 * 
 */
public class IssueObligationsTableDecorator extends TableDecorator {

    /**
     * 
     * @return String
     */
    public String getObligationTitle() {

        StringBuilder ret = new StringBuilder();
        ObligationDTO obligation = (ObligationDTO) getCurrentRowObject();
        HttpServletRequest req = (HttpServletRequest) getPageContext().getRequest();
        String path = req.getContextPath();
        ret.append("<a href='").append(path).append("/obligations/").append(obligation.getObligationId()).append("'>");
        if (!RODUtil.isNullOrEmpty(obligation.getTitle()))
            ret.append(RODUtil.replaceTags(obligation.getTitle(), true, true));
        else
            ret.append("Reporting obligation");
        ret.append("</a>");

        return ret.toString();
    }

}
