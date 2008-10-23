package eionet.rod.web.util;

import javax.servlet.jsp.PageContext;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

import eionet.rod.RODUtil;


/**
 * Simple column decorator which encodes column value.
 * @author altnyris
 */
public class ReplaceTagsWrapper implements DisplaytagColumnDecorator
{

    public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException
    {
        String ret="";
    	String value = (String) columnValue;
        if(!RODUtil.isNullOrEmpty(value))
        	ret = RODUtil.replaceTags(value, true, true);
        
        return ret;
    }
}
