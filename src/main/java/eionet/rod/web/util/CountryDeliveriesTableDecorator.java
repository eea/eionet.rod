package eionet.rod.web.util;


import org.displaytag.decorator.TableDecorator;
import eionet.rod.RODUtil;
import eionet.rod.dto.CountryDeliveryDTO;

/**
 * 
 * @author altnyris
 * 
 */
public class CountryDeliveriesTableDecorator extends TableDecorator {

    /**
     * 
     * @return String
     */
    public String getTitle() {

        StringBuilder ret = new StringBuilder();
        CountryDeliveryDTO delivery = (CountryDeliveryDTO) getCurrentRowObject();
        ret.append("<a href='").append(RODUtil.replaceTags(delivery.getDeliveryUrl(), true, true)).append("'>");
        if (delivery.getDeliveryTitle() == null || delivery.getDeliveryTitle().equals(""))
            ret.append("-no-title-");
        else
            ret.append(RODUtil.replaceTags(delivery.getDeliveryTitle(), true, true));
        ret.append("</a>");

        return ret.toString();
    }

    /**
     * 
     * @return String
     */
    public String getDate() {

        StringBuilder ret = new StringBuilder();
        CountryDeliveryDTO delivery = (CountryDeliveryDTO) getCurrentRowObject();
        if (delivery.getDeliveryUploadDate() != null && !delivery.getDeliveryUploadDate().equals("0000-00-00"))
            ret.append(delivery.getDeliveryUploadDate());
        else
            ret.append("&lt;No date&gt;");

        return ret.toString();
    }

}
