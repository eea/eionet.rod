package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.dto.VersionDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 *
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/versions")
public class VersionsActionBean extends AbstractRODActionBean {

    private List<VersionDTO> versions;
    private String id;
    private String tab;
    private String id_field;


    /**
     *
     * @return
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {

        if (id == null || id.equals("") || id.equals("-1"))
            versions = RODServices.getDbService().getUndoDao().getPreviousActionsGeneral();
        else
            versions = RODServices.getDbService().getUndoDao().getPreviousActionsReportSpecific(id, tab, id_field);

        return new ForwardResolution("/pages/versions.jsp");
    }


    public List<VersionDTO> getVersions() {
        return versions;
    }


    public void setVersions(List<VersionDTO> versions) {
        this.versions = versions;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getTab() {
        return tab;
    }


    public void setTab(String tab) {
        this.tab = tab;
    }


    public String getId_field() {
        return id_field;
    }


    public void setId_field(String id_field) {
        this.id_field = id_field;
    }

}
