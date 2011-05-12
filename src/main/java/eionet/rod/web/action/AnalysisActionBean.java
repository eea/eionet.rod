package eionet.rod.web.action;

import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 * 
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 * 
 */
@UrlBinding("/analysis")
public class AnalysisActionBean extends AbstractRODActionBean {

    private int totalRa;
    private String lastUpdateRa;
    private int totalLi;
    private String lastUpdateLi;
    private int eeaCore;
    private int eeaPriority;
    private int overlapRa;
    private int flaggedRa;
    private int instrumentsDue;
    private int noIssue;

    /**
     * 
     * @return Resolution
     * @throws ServiceException
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {
        totalRa = RODServices.getDbService().getAnalysisDao().getTotalRa();
        lastUpdateRa = RODServices.getDbService().getAnalysisDao().getLastUpdateRa();
        totalLi = RODServices.getDbService().getAnalysisDao().getTotalLi();
        lastUpdateLi = RODServices.getDbService().getAnalysisDao().getLastUpdateLi();
        eeaCore = RODServices.getDbService().getAnalysisDao().getEeaCore();
        eeaPriority = RODServices.getDbService().getAnalysisDao().getEeaPriority();
        overlapRa = RODServices.getDbService().getAnalysisDao().getOverlapRa();
        flaggedRa = RODServices.getDbService().getAnalysisDao().getFlaggedRa();
        instrumentsDue = RODServices.getDbService().getAnalysisDao().getInstrumentsDue();
        noIssue = RODServices.getDbService().getAnalysisDao().getNoIssueAllocated();

        return new ForwardResolution("/pages/analysis.jsp");
    }

    public int getTotalRa() {
        return totalRa;
    }

    public void setTotalRa(int totalRa) {
        this.totalRa = totalRa;
    }

    public String getLastUpdateRa() {
        return lastUpdateRa;
    }

    public void setLastUpdateRa(String lastUpdateRa) {
        this.lastUpdateRa = lastUpdateRa;
    }

    public int getTotalLi() {
        return totalLi;
    }

    public void setTotalLi(int totalLi) {
        this.totalLi = totalLi;
    }

    public String getLastUpdateLi() {
        return lastUpdateLi;
    }

    public void setLastUpdateLi(String lastUpdateLi) {
        this.lastUpdateLi = lastUpdateLi;
    }

    public int getEeaCore() {
        return eeaCore;
    }

    public void setEeaCore(int eeaCore) {
        this.eeaCore = eeaCore;
    }

    public int getEeaPriority() {
        return eeaPriority;
    }

    public void setEeaPriority(int eeaPriority) {
        this.eeaPriority = eeaPriority;
    }

    public int getOverlapRa() {
        return overlapRa;
    }

    public void setOverlapRa(int overlapRa) {
        this.overlapRa = overlapRa;
    }

    public int getFlaggedRa() {
        return flaggedRa;
    }

    public void setFlaggedRa(int flaggedRa) {
        this.flaggedRa = flaggedRa;
    }

    public int getInstrumentsDue() {
        return instrumentsDue;
    }

    public void setInstrumentsDue(int instrumentsDue) {
        this.instrumentsDue = instrumentsDue;
    }

    public int getNoIssue() {
        return noIssue;
    }

    public void setNoIssue(int noIssue) {
        this.noIssue = noIssue;
    }

}
