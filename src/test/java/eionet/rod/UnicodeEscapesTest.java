package eionet.rod;

import eionet.rod.UnicodeEscapes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author Jaanus Heinlaid
 *
 */
public class UnicodeEscapesTest {
    
    /** */
    protected UnicodeEscapes unicodeEscapes = null;

    @Before
    public void setUp() throws Exception {
        unicodeEscapes = new UnicodeEscapes();
    }

    @Test
    public void testIsXHTMLEntity() {
        
        assertEquals(true, unicodeEscapes.isXHTMLEntity("&euro;"));
        assertEquals(false, unicodeEscapes.isXHTMLEntity("&euro"));
        assertEquals(false, unicodeEscapes.isXHTMLEntity("euro;"));
        assertEquals(false, unicodeEscapes.isXHTMLEntity("&;"));
    }

}
