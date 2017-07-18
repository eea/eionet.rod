package eionet.rod.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.TableDecorator;

import eionet.rod.RODUtil;
import eionet.rod.dto.SearchDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author altnyris
 *
 */
public class DeadlinesTableDecorator extends TableDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeadlinesTableDecorator.class);

    /**
     *
     * @return String
     */
    public String getTitle() {

        StringBuilder ret = new StringBuilder();
        SearchDTO search = (SearchDTO) getCurrentRowObject();
        ret.append("<a href='" + getContextPath() + "/obligations/").append(search.getObligationId()).append("'>");
        ret.append(RODUtil.replaceTags(RODUtil.threeDots(search.getObligationTitle(), 40), true, true));
        ret.append("</a>");
        if (!RODUtil.isNullOrEmpty(search.getSourceCode())) {
            ret.append("<br/><a href='" + getContextPath() + "/instruments/").append(search.getSourceId()).append("'>");
            ret.append(RODUtil.replaceTags(search.getSourceCode(), true, true));
            ret.append("</a>");
        }

        return ret.toString();
    }

    /**
     *
     * @return String
     */
    public String getClient() {

        StringBuilder ret = new StringBuilder();
        SearchDTO search = (SearchDTO) getCurrentRowObject();
        ret.append("<a href='" + getContextPath() + "/clients/").append(search.getClientId()).append("' title='");
        ret.append(RODUtil.replaceTags(search.getClientName(), true, true)).append("'>");
        ret.append(RODUtil.replaceTags(RODUtil.threeDots(search.getClientDescr(), 20), true, true));
        ret.append("</a>");

        return ret.toString();
    }

    /**
     *
     * @return String
     */
    public String getDeadline() {

        StringBuilder ret = new StringBuilder();
        SearchDTO search = (SearchDTO) getCurrentRowObject();
        String nextDeadline = search.getObligationNextDeadline();
        String nextReporting = search.getObligationNextReporting();
        if (RODUtil.isNullOrEmpty(nextDeadline)) {
            ret.append("<div title='").append(nextReporting).append("' style='color:#006666'>");
            ret.append(RODUtil.threeDots(nextReporting, 10));
            ret.append("</div>");
        } else {
            ret.append("<div title='").append(nextDeadline).append("' style='color:#000000'>");
            ret.append(RODUtil.threeDots(nextDeadline, 10));
            ret.append("</div>");
        }

        return ret.toString();
    }

    /**
     *
     * @return Date
     */
    public Date getDeadlineSort() {

        Date ret = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date zeroDate = new Date();
        try {
            zeroDate = df.parse("0000-00-00");
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }

        SearchDTO search = (SearchDTO) getCurrentRowObject();
        String nextDeadline = search.getObligationNextDeadline();
        if (RODUtil.isNullOrEmpty(nextDeadline)) {
            ret = zeroDate;
        } else {
            try {
                ret = df.parse(nextDeadline);
            } catch (ParseException e) {
                ret = zeroDate;
            }
        }

        return ret;
    }

    /**
     *
     * @return String
     */
    public String getRole() {

        StringBuilder ret = new StringBuilder();
        ret.append("");
        SearchDTO search = (SearchDTO) getCurrentRowObject();
        if (!RODUtil.isNullOrEmpty(search.getObligationRespRole())) {
            if (RODUtil.isNullOrEmpty(search.getRoleDescr())) {
                String role = search.getObligationRespRole();
                ret.append("<div title='").append(role).append("'>");
                ret.append(RODUtil.replaceTags(RODUtil.threeDots(role, 35), true, true));
                ret.append("</div>");
            } else {
                ret.append("<a href='" + getContextPath() +  "/contacts?roleId=").append(search.getObligationRespRole()).append("'>");
                ret.append(RODUtil.replaceTags(RODUtil.threeDots(search.getRoleDescr(), 15), true, true));
                ret.append("</a>");
            }
        } else {
            ret.append(RODUtil.replaceTags(search.getSpatialName(), true, true));
        }

        return ret.toString();
    }

    /**
     *
     * @return String
     */
    public String getHasDelivery() {

        StringBuilder ret = new StringBuilder();
        SearchDTO search = (SearchDTO) getCurrentRowObject();
        if (search.getObligationHasDelivery() == 1) {
            ret.append("<a href='" + getContextPath() + "/countrydeliveries?actDetailsId=").append(search.getObligationId());
            ret.append("&amp;spatialId=").append(search.getSpatialId()).append("'>Show list</a>");
        } else {
            ret.append("None");
        }

        return ret.toString();
    }

    /**
     * Since the JSP is used by different beans href's are dependent on context path.
     * @return context path
     */
    private String getContextPath() {

        PageContext pc = getPageContext();
        HttpServletRequest req = (HttpServletRequest) pc.getRequest();

        return req.getContextPath();
    }
}
