package eionet.rod.web.util;

import org.displaytag.decorator.TableDecorator;

import eionet.rod.RODUtil;
import eionet.rod.dto.InstrumentsDueDTO;

/**
 *
 * @author altnyris
 *
 */
public class InstrumentsDueTableDecorator extends TableDecorator{

    /**
     *
     * @return
     */
    public String getInstrumentTitle() {

        StringBuilder ret = new StringBuilder();
        InstrumentsDueDTO instrument = (InstrumentsDueDTO) getCurrentRowObject();
        ret.append("<a href='instruments/").append(instrument.getInstrumentId()).append("'>");
        if (!RODUtil.isNullOrEmpty(instrument.getTitle()))
            ret.append(RODUtil.replaceTags(instrument.getTitle(), true, true));
        ret.append("</a>");

        return ret.toString();
    }



}
