package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.RODUtil;
import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.ObligationDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 *
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/spatial/{idspatial}")
public class SpatialActionBean extends AbstractRODActionBean {

    private CountryDTO spatial;
    private String idspatial;
    private List<ObligationDTO> obligations;

    /**
     *
     * @return Resolution
     * @throws ServiceException
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {

        if (!RODUtil.isNullOrEmpty(idspatial)) {
            spatial = RODServices.getDbService().getSpatialDao().getCountry(idspatial);
            obligations = RODServices.getDbService().getSpatialDao().getCountryObligationsList(idspatial);
        }

        return new ForwardResolution("/pages/spatial.jsp");
    }

    public List<ObligationDTO> getObligations() {
        return obligations;
    }

    public void setObligations(List<ObligationDTO> obligations) {
        this.obligations = obligations;
    }

    public CountryDTO getSpatial() {
        return spatial;
    }

    public void setSpatial(CountryDTO spatial) {
        this.spatial = spatial;
    }

    public String getIdspatial() {
        return idspatial;
    }

    public void setIdspatial(String idspatial) {
        this.idspatial = idspatial;
    }

}
