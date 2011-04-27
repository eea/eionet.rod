package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.dto.HistDeadlineDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 *
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/histdeadlines")
public class HistDeadlinesActionBean extends AbstractRODActionBean {

    private String startDate;
    private String endDate;

    private List<HistDeadlineDTO> deadlines;


    /**
     *
     * @return
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {
        deadlines = RODServices.getDbService().getHistoricDeadlineDao().getHistoricDeadlines(startDate, endDate);
        return new ForwardResolution("/pages/histdeadlines.jsp");
    }


    public String getStartDate() {
        return startDate;
    }


    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public String getEndDate() {
        return endDate;
    }


    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    public List<HistDeadlineDTO> getDeadlines() {
        return deadlines;
    }


    public void setDeadlines(List<HistDeadlineDTO> deadlines) {
        this.deadlines = deadlines;
    }

}
