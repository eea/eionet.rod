package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.dto.HarvestHistoryDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 *
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/history")
public class HarvestHistoryActionBean extends AbstractRODActionBean {

    private List<HarvestHistoryDTO> list;

    /**
     *
     * @return
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {
        list = RODServices.getDbService().getHistoryDao().getHarvestHistory();
        return new ForwardResolution("/pages/history.jsp");
    }

    public List<HarvestHistoryDTO> getList() {
        return list;
    }

    public void setList(List<HarvestHistoryDTO> list) {
        this.list = list;
    }



}
