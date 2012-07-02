package eionet.rod.web.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.services.ServiceException;

/**
 *
 * @author altnyris
 *
 */
@UrlBinding("/spatial/{idspatial}/{tab}")
public class SpatialActionBean extends AbstractRODActionBean {
    /** Spatial id. */
    private String idspatial;

    /** tab part of the url. */
    private String tab;

    private String issueId;
    private String deadlines;
    private String clientId;
    /**
     * Default handler.
     *
     * @return Resolution
     * @throws ServiceException if query fails.
     */
    @DefaultHandler
    public Resolution deadlines() throws ServiceException {
        //default Forwarding: Tab is not taken into account at the moment
        return new ForwardResolution(DeadlinesActionBean.class).addParameter("idspatial", idspatial);
    }

    public String getIdspatial() {
        return idspatial;
    }

    public void setIdspatial(String idspatial) {
        this.idspatial = idspatial;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getDeadlines() {
        return deadlines;
    }

    public void setDeadlines(String deadlines) {
        this.deadlines = deadlines;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
