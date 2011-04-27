package eionet.rod.web.util;

import org.displaytag.decorator.TableDecorator;
import eionet.rod.RODUtil;
import eionet.rod.dto.HistDeadlineDTO;

/**
 *
 * @author altnyris
 *
 */
public class HistDeadlinesTableDecorator extends TableDecorator{

    /**
     *
     * @return
     */
    public String getTitle() {

        StringBuilder ret = new StringBuilder();
        HistDeadlineDTO deadline = (HistDeadlineDTO) getCurrentRowObject();
        ret.append("<a href='obligations/").append(deadline.getObligationId()).append("'>");
        ret.append(RODUtil.replaceTags(RODUtil.threeDots(deadline.getObligationTitle(), 80), true, true));
        ret.append("</a>");

        return ret.toString();
    }

}
