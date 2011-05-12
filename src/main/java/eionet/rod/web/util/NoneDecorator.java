package eionet.rod.web.util;

import javax.servlet.jsp.PageContext;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

import eionet.rod.RODUtil;

/**
 * Simple column decorator which replaces empty fields with 'None'
 * 
 * @author altnyris
 */
public class NoneDecorator implements DisplaytagColumnDecorator {

    public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
        String value = (String) columnValue;
        if (RODUtil.isNullOrEmpty(value))
            value = "None";
        else
            value = RODUtil.replaceTags(value, true, true);

        return value;
    }
}
