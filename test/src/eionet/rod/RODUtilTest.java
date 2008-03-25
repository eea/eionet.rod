package eionet.rod;

import eionet.rod.RODUtil;
import junit.framework.TestCase;


/**
 * 
 * @author Jaanus Heinlaid, e-mail: <a href="mailto:jaanus.heinlaid@tietoenator.com">jaanus.heinlaid@tietoenator.com</a>
 *
 */
public class RODUtilTest extends TestCase {
    
    public void test_replaceTags() {
        assertEquals(
                "<a href=\"http://cdr.eionet.europa.eu/search?y=1&amp;z=2\">http://cdr.eionet.europa.eu/search?y=1&amp;z=2</a>",
                RODUtil.replaceTags("http://cdr.eionet.europa.eu/search?y=1&z=2"));

        // Simple &
        assertEquals("Fruit &amp; Vegetables",
                RODUtil.replaceTags("Fruit & Vegetables"));

        // Simple & with ; appended
        assertEquals("Fruit &amp;; Vegetables",
                RODUtil.replaceTags("Fruit &; Vegetables"));

        // Long decimal entity (This is the € sign)
        assertEquals("Grand total: &#8364; 50.000",
                RODUtil.replaceTags("Grand total: &#8364; 50.000"));

        // Simple hex entity
        assertEquals("Fruit &amp;#x26; Vegetables",
                RODUtil.replaceTags("Fruit &#x26; Vegetables"));

        // Long hexadecimal entity (This is the € sign)
        assertEquals("Grand total: &amp;#x20AC; 50.000",
                RODUtil.replaceTags("Grand total: &#x20AC; 50.000"));

        // Already encoded
        assertEquals("Fruit &amp; Vegetables",
                RODUtil.replaceTags("Fruit &amp; Vegetables"));

        // Unknown entity
        assertEquals("Fruit &amp;unknown; Vegetables",
                RODUtil.replaceTags("Fruit &unknown; Vegetables"));

        // Unusual entity
        assertEquals("Fruit &euro; Vegetables",
                RODUtil.replaceTags("Fruit &euro; Vegetables"));

        // Test newline
        assertEquals("Fruit<br/>Vegetables",
                RODUtil.replaceTags("Fruit\nVegetables"));

        // Don't create anchors = true
        assertEquals("http://cdr.eionet.europa.eu/search?y=1&amp;z=7",
                RODUtil.replaceTags("http://cdr.eionet.europa.eu/search?y=1&z=7",
                true));

        // Test Unicode char
        assertEquals("€", RODUtil.replaceTags("€"));

        // Test HTML tags
        assertEquals("&lt;div class=&#039;Apostrophs&#039;&gt;",
                RODUtil.replaceTags("<div class='Apostrophs'>"));
        assertEquals("&lt;div class=&quot;Quotes&quot;&gt;",
                RODUtil.replaceTags("<div class=\"Quotes\">"));
        assertEquals("&lt;a href=&quot;http://cnn.org/&quot;&gt;CNN&lt;/a&gt;",
                RODUtil.replaceTags("<a href=\"http://cnn.org/\">CNN</a>"));
    }

    public void test_replaceTags2() {
        // Unusual entity
        assertEquals("Fruit &euro; Vegetables",
                RODUtil.replaceTags("Fruit &euro; Vegetables"));

        // double spaces
        assertEquals(" &nbsp;", RODUtil.replaceTags2("  "));

        // enclosed double spaces
        assertEquals("X &nbsp;X", RODUtil.replaceTags2("X  X"));
    }

    public void test_isURL() {
        assertTrue(RODUtil.isURL("http://cdr.eionet.europa.eu/"));
        assertTrue(RODUtil.isURL("ftp://ftp.eionet.europa.eu/"));
        // assertFalse(RODUtil.isURL("mailto:jaanus.heinlaid@tietoenator.com"));
        assertFalse(RODUtil.isURL("XXX"));
    }

    public void test_threeDots() {
        assertEquals("Fahrvergnügen", RODUtil.threeDots("Fahrvergnügen", 13));
        assertEquals("http://en....",
                RODUtil.threeDots("http://en.wikipedia.org/wiki/Fahrvergnügen",
                10));
    }

    public void test_setAnchors() {
        // Simple check
        assertEquals(
                "<a href=\"http://en.wikipedia.org/wiki/Fahrvergnügen\">http://en....</a>",
                RODUtil.setAnchors("http://en.wikipedia.org/wiki/Fahrvergnügen",
                false, 10));

        // Check with popup
        assertEquals(
                "<a target=\"_blank\" href=\"http://en.wikipedia.org/wiki/Fahrvergnügen\">http://en....</a>",
                RODUtil.setAnchors("http://en.wikipedia.org/wiki/Fahrvergnügen",
                true, 10));

        // setAnchors doesn't escape &-signs
        // Setting cutlinks to 0 does not do what the documentation says it does
        assertEquals(
                "<a href=\"http://cdr.eionet.europa.eu/search?y=1&z=2\">...</a>",
                RODUtil.setAnchors("http://cdr.eionet.europa.eu/search?y=1&z=2",
                false, 0));
    }

    // Testing correct syntax
    public void test_getDate() {
        assertEquals("Sat Mar 22 00:00:00 CET 2008",
                RODUtil.getDate("22/03/2008").toString());
    }
    
    // Having a bad syntax will cause the method to return null
    // .. and print stack trace
    public void test_getDateWithDashes() {
            assertEquals(null, RODUtil.getDate("22-03-2008"));
    }
}
