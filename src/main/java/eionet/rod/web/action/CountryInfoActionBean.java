package eionet.rod.web.action;

import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.dto.CountryInfoDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 * 
 */
@UrlBinding("/countryinfo")
public class CountryInfoActionBean extends AbstractRODActionBean {

    private String oid;
    private String sid;
    private String member;
    private String vol;

    private CountryInfoDTO countryInfo;
    private String rt;

    /**
     * 
     * @return Resolution
     * @throws ServiceException
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {

        if (oid != null && !oid.equals("") && sid != null && !sid.equals("") && member != null && !member.equals("")) {
            rt = "";
            if (member != null) {
                if (member.equalsIgnoreCase("Y")) {
                    rt = "mc";
                } else if (member.equalsIgnoreCase("N")) {
                    rt = "cc";
                }
            }

            countryInfo = RODServices.getDbService().getSpatialDao().getCountryInfo(oid, sid);

        } else {
            return new ErrorResolution(HttpServletResponse.SC_NOT_FOUND);
        }

        return new ForwardResolution("/pages/countryinfo.jsp");
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public CountryInfoDTO getCountryInfo() {
        return countryInfo;
    }

    public void setCountryInfo(CountryInfoDTO countryInfo) {
        this.countryInfo = countryInfo;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }
}
