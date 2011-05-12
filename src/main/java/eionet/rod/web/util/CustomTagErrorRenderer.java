package eionet.rod.web.util;

import java.io.IOException;

import net.sourceforge.stripes.tag.InputTagSupport;
import net.sourceforge.stripes.tag.TagErrorRenderer;

public class CustomTagErrorRenderer implements TagErrorRenderer {
    private InputTagSupport tag;

    /** Store the tag that is in error. */
    public void init(InputTagSupport tag) {
        this.tag = tag;
    }

    /** Output our asterisk before the tag/field itself is rendered. */
    public void doBeforeStartTag() {
    }

    /** Doesn't need to do anything. */
    public void doAfterEndTag() {
        try {
            this.tag.getPageContext().getOut().write("<div class=\"error-hint\" id=\"error-description\">Field is mandatory</div>");
        } catch (IOException ioe) {
            // Not really a whole lot we can do if writing to out fails!
        }
    }
}
