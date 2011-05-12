package eionet.rod.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.displaytag.decorator.TableDecorator;

import eionet.sparqlClient.helpers.ResultValue;

/**
 * 
 * @author altnyris
 * 
 */
public class ProductsTableDecorator extends TableDecorator {
    
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 
     * @return String
     * @throws Exception
     */
    public String getTitle() throws Exception {

        Map<String, ResultValue> wm = (Map<String, ResultValue>) getCurrentRowObject();
        if (wm == null) {
            return "";
        } else {
            ResultValue product = (ResultValue) wm.get("product");
            ResultValue prodtitle = (ResultValue) wm.get("title");

            String title = "- no-label -";
            if (prodtitle != null && !StringUtils.isBlank(prodtitle.getValue())) {
                title = prodtitle.getValue();
            }
            if (product != null && !StringUtils.isBlank(product.getValue())) {
                StringBuffer ret = new StringBuffer();
                ret.append("<a href=\"").append(product.getValue()).append("\">");
                ret.append(title);
                ret.append("</a>");
                title = ret.toString();
            }

            return title;
        }
    }

    /**
     * 
     * @return String
     * @throws Exception
     */
    public String getTitleLabel() throws Exception {

        String ret = "";
        Map<String, ResultValue> wm = (Map<String, ResultValue>) getCurrentRowObject();
        if (wm != null) {
            ResultValue title = (ResultValue) wm.get("title");
            if (title != null && title.getValue() != null) {
                ret = title.getValue();
            }
        }
        return ret;
    }
    
    /**
     * 
     * @return Date
     * @throws Exception
     */
    public Date getPublished() throws Exception {

        Map<String, ResultValue> wm = (Map<String, ResultValue>) getCurrentRowObject();
        if (wm == null) {
            return null;
        } else {
            ResultValue published = (ResultValue) wm.get("published");

            Date ret = null;
            if (published != null && !StringUtils.isBlank(published.getValue())) {
                ret = dateFormat.parse(published.getValue());
            }

            return ret;
        }
    }
}
