package eionet.rod.web.util;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.displaytag.decorator.TableDecorator;

import eionet.rod.RODUtil;
import eionet.sparqlClient.helpers.ResultValue;

/**
 *
 * @author altnyris
 *
 */
public class SimpleSearchTableDecorator extends TableDecorator{

    /**
     *
     * @return String
     * @throws Exception 
     */
    public String getColumnValue() throws Exception {
        
        Map<String, ResultValue> wm = (Map<String, ResultValue>) getCurrentRowObject();
        if (wm == null) {
            return "";
        } else {
            ResultValue subject = (ResultValue) wm.get("subject");
            ResultValue type = (ResultValue) wm.get("type");
            ResultValue found = (ResultValue) wm.get("found");
            ResultValue name = (ResultValue) wm.get("name");
            
            String typeString = null;
            if (type != null && type.getValue() != null) {
                typeString = type.getValue();
                typeString = typeString.substring(typeString.lastIndexOf("#") + 1);
            }
        
            StringBuffer ret = new StringBuffer();
            if (name != null && subject != null) {
                ret.append("<b><a href=\"").append(subject.getValue()).append("\">").append(name.getValue()).append("</a></b>");
                if (!StringUtils.isBlank(typeString)) {
                    ret.append(" - <b>").append(typeString).append("</b>");
                }
                ret.append("<br/>");
                if (found != null && found.getValue() != null) {
                    ret.append(RODUtil.threeDots(found.getValue(), 400));
                }
            }

            return ret.toString();
        }
    }
    
    /**
    *
    * @return String
    * @throws Exception 
    */
   public String getSortValue() throws Exception {
       
       String ret = "";
       Map<String, ResultValue> wm = (Map<String, ResultValue>) getCurrentRowObject();
       if (wm != null) {
           ResultValue name = (ResultValue) wm.get("name");
           if (name != null && name.getValue() != null) {
               ret = name.getValue();
           }
       }
       return ret;
   }
}
