package eionet.rod.web.util;

import org.displaytag.decorator.TableDecorator;
import eionet.rod.RODUtil;
import eionet.rod.dto.ObligationsListDTO;

/**
 *
 * @author altnyris
 *
 */
public class ObligationsTableDecorator extends TableDecorator{

    /**
     *
     * @return
     */
    public String getTitle() {

        StringBuilder ret = new StringBuilder();
        ObligationsListDTO obligation = (ObligationsListDTO) getCurrentRowObject();
        ret.append("<a href='obligations/").append(obligation.getObligationId()).append("'>");
        if (!RODUtil.isNullOrEmpty(obligation.getObligationTitle()))
            ret.append(RODUtil.replaceTags(obligation.getObligationTitle(), true, true));
        else
            ret.append("Reporting obligation");
        ret.append("</a>");
        if (obligation.getTerminate().equals("Y"))
            ret.append("<span class='smallfont' style='color:red'> [terminated]</span>");

        return ret.toString();
    }

    /**
     *
     * @return
     */
    public String getInstrument() {

        StringBuilder ret = new StringBuilder();
        ObligationsListDTO obligation = (ObligationsListDTO) getCurrentRowObject();
        ret.append("<a href='instruments/").append(obligation.getSourceId()).append("'>");
        ret.append(RODUtil.replaceTags(obligation.getSourceTitle(), true, true));
        ret.append("</a>");

        return ret.toString();
    }

    /**
     *
     * @return
     */
    public String getClient() {

        StringBuilder ret = new StringBuilder();
        ObligationsListDTO obligation = (ObligationsListDTO) getCurrentRowObject();
        ret.append("<a href='clients/").append(obligation.getClientId()).append("' title='");
        ret.append(RODUtil.replaceTags(obligation.getClientName(), true, true)).append("'>");
        ret.append(RODUtil.replaceTags(obligation.getClientDescr(), true, true));
        ret.append("</a>");

        return ret.toString();
    }

    /**
     *
     * @return
     */
    public String getDeadline() {

        StringBuilder ret = new StringBuilder();
        ObligationsListDTO obligation = (ObligationsListDTO) getCurrentRowObject();
        String nextDeadline = obligation.getNextDeadline();
        String nextReporting = obligation.getNextReporting();
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
     * @return
     */
    public String getDeliveries() {

        StringBuilder ret = new StringBuilder();
        ObligationsListDTO obligation = (ObligationsListDTO) getCurrentRowObject();

        if (!RODUtil.isNullOrEmpty(obligation.getFkDeliveryCountryIds()) && obligation.getFkDeliveryCountryIds().length() > 0) {
            ret.append("<a href='countrydeliveries?actDetailsId=").append(obligation.getObligationId()).append("'>");
            ret.append("Show list");
            ret.append("</a>");
        } else {
            ret.append("None");
        }

        return ret.toString();
    }

}
