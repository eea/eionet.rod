package eionet.rod;

import java.util.Date;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jaanus Heinlaid
 *
 */
public class RODUtilTest {

    @Test
    public void test_replaceTags() {
        assertEquals(
                "<a href=\"http://cdr.eionet.europa.eu/search?y=1&amp;z=2\">http://cdr.eionet.europa.eu/search?y=1&amp;z=2</a>",
                RODUtil.replaceTags("http://cdr.eionet.europa.eu/search?y=1&z=2"));

        // Simple &
        assertEquals("Fruit &amp; Vegetables", RODUtil.replaceTags("Fruit & Vegetables"));

        // Simple & with ; appended
        assertEquals("Fruit &amp;; Vegetables", RODUtil.replaceTags("Fruit &; Vegetables"));

        // Long decimal entity (This is the € sign)
        assertEquals("Grand total: &#8364; 50.000", RODUtil.replaceTags("Grand total: &#8364; 50.000"));

        // Simple hex entity
        assertEquals("Fruit &amp;#x26; Vegetables", RODUtil.replaceTags("Fruit &#x26; Vegetables"));

        // Long hexadecimal entity (This is the € sign)
        assertEquals("Grand total: &amp;#x20AC; 50.000", RODUtil.replaceTags("Grand total: &#x20AC; 50.000"));

        // Already encoded
        assertEquals("Fruit &amp; Vegetables", RODUtil.replaceTags("Fruit &amp; Vegetables"));

        // Unknown entity
        assertEquals("Fruit &amp;unknown; Vegetables", RODUtil.replaceTags("Fruit &unknown; Vegetables"));

        // Unusual entity
        assertEquals("Fruit &euro; Vegetables", RODUtil.replaceTags("Fruit &euro; Vegetables"));

        // Test newline
        assertEquals("Fruit<br/>Vegetables", RODUtil.replaceTags("Fruit\nVegetables"));

        // Don't create anchors = true
        assertEquals("http://cdr.eionet.europa.eu/search?y=1&amp;z=7",
                RODUtil.replaceTags("http://cdr.eionet.europa.eu/search?y=1&z=7", true));

        // Test Unicode char
        assertEquals("€", RODUtil.replaceTags("€"));

        // Test HTML tags
        assertEquals("&lt;div class=&#039;Apostrophs&#039;&gt;", RODUtil.replaceTags("<div class='Apostrophs'>"));
        assertEquals("&lt;div class=&quot;Quotes&quot;&gt;", RODUtil.replaceTags("<div class=\"Quotes\">"));
        assertEquals("&lt;a href=&quot;http://cnn.org/&quot;&gt;CNN&lt;/a&gt;",
                RODUtil.replaceTags("<a href=\"http://cnn.org/\">CNN</a>"));
    }

    /*
     * This test documents an actual bug in the system.
     */
    @Test
    public void test_replaceTagsMultiLine() {
        String input =
                "The templates (XML schema) and the reporting manual are available"
                        + " at: http://www.eionet.europa.eu/schemas/eprtr\n\nTemplate for resubmissions is"
                        + " available at: http://circa.europa.eu/Public/irc/env/e_prtr/library?l=/e-prtr_re-delivery/resubmissionxls/_EN_1.0_&a=i";

        String expected =
                "The templates (XML schema) and the reporting manual are available"
                        + " at: <a href=\"http://www.eionet.europa.eu/schemas/eprtr\">http://www.eionet.e"
                        + "uropa.eu/schemas/eprtr</a><br/><br/>Template for resubmissions is"
                        + " available at: <a href=\"http://circa.europa.eu/Public/irc/env/e_prtr/library?"
                        + "l=/e-prtr_re-delivery/resubmissionxls/_EN_1.0_&amp;a=i\">http://circa.europa.eu"
                        + "/Public/irc/env/e_prtr/libra...</a>";

        String result = RODUtil.replaceTags(input);
        assertEquals(expected, result);
    }

    @Test
    public void test_replaceTags2() {
        // Unusual entity
        assertEquals("Fruit &euro; Vegetables", RODUtil.replaceTags("Fruit &euro; Vegetables"));

        // double spaces
        assertEquals(" &nbsp;", RODUtil.replaceTags2("  "));

        // enclosed double spaces
        assertEquals("X &nbsp;X", RODUtil.replaceTags2("X  X"));
    }

    @Test
    public void test_isURL() {
        assertTrue(RODUtil.isURL("http://cdr.eionet.europa.eu/"));
        assertTrue(RODUtil.isURL("ftp://ftp.eionet.europa.eu/"));
        // assertFalse(RODUtil.isURL("mailto:jaanus.heinlaid@tietoenator.com"));
        assertFalse(RODUtil.isURL("XXX"));
    }

    @Test
    public void test_threeDots() {
        assertEquals("Fahrvergnügen", RODUtil.threeDots("Fahrvergnügen", 13));
        assertEquals("Königsschloß und ...", RODUtil.threeDots("Königsschloß und Papstgefängnis", 17));
        assertEquals("http://en....", RODUtil.threeDots("http://en.wikipedia.org/wiki/Fahrvergnügen", 10));
    }

    @Test
    public void test_threeDotsElektra() {
        // Verify that threeDots() can handle "Elektra" in the Greek alphabet
        char data[] =
                {'\u03a4', '\u03af', '\u03c4', '\u03bb', '\u03bf', '\u03c2', '\u003a', '\u0020', '\u0397', '\u03bb', '\u03ad',
                        '\u03ba', '\u03c4', '\u03c1', '\u03b1'};
        assertEquals("Τίτλος: Ηλέκτρα", RODUtil.threeDots(new String(data), 60));
    }

    @Test
    public void test_threeDotsElektra6() {
        // Verify that threeDots() can cut after 6 greek characters
        // Ensure that it fails if javac's charset is not UNICODE
        char data[] =
                {'\u03a4', '\u03af', '\u03c4', '\u03bb', '\u03bf', '\u03c2', '\u003a', '\u0020', '\u0397', '\u03bb', '\u03ad',
                        '\u03ba', '\u03c4', '\u03c1', '\u03b1'};
        assertEquals("Τίτλος...", RODUtil.threeDots(new String(data), 6));
    }

    @Test
    public void test_setAnchors() {
        // Simple check
        assertEquals("<a href=\"http://en.wikipedia.org/wiki/Fahrvergnügen\">http://en....</a>",
                RODUtil.setAnchors("http://en.wikipedia.org/wiki/Fahrvergnügen", false, 10));

        // Check with popup
        assertEquals("<a target=\"_blank\" href=\"http://en.wikipedia.org/wiki/Fahrvergnügen\">http://en....</a>",
                RODUtil.setAnchors("http://en.wikipedia.org/wiki/Fahrvergnügen", true, 10));

        // setAnchors doesn't escape &-signs
        // Setting cutlinks to 0 does not do what the documentation says it does
        assertEquals("<a href=\"http://cdr.eionet.europa.eu/search?y=1&z=2\">...</a>",
                RODUtil.setAnchors("http://cdr.eionet.europa.eu/search?y=1&z=2", false, 0));
    }

    @Test
    public void test_setAnchorsInParenthesis() {
        assertEquals(
                "Some text (<a href=\"http://en.wikipedia.org/wiki/Fahrvergnügen\">http://en.wikipedia.org/wiki/Fahrvergnügen</a>).",
                RODUtil.setAnchors("Some text (http://en.wikipedia.org/wiki/Fahrvergnügen).", false, 100));

        assertEquals(
                "Some text (<a href=\"http://en.wikipedia.org/wiki/Fahrvergnügen\">http://en.wikipedia.org/wiki/Fahrvergnügen</a> ).",
                RODUtil.setAnchors("Some text (http://en.wikipedia.org/wiki/Fahrvergnügen ).", false, 100));
    }

    // Testing correct syntax
    @Test
    public void test_getDate() {
        Date date = RODUtil.getDate("22/03/2008");
        assertEquals(date.getDate(), 22);
        assertEquals(date.getMonth(), 02); // The value 0 represents January
        assertEquals(date.getYear(), 108);
    }

    // Testing getDate() with empty string param
    @Test
    public void test_getEmptyDate() {
        assertNull(RODUtil.getDate(""));
    }

    // Having a bad syntax will cause the method to return null
    // .. and print stack trace
    @Test
    public void test_getDateWithDashes() {
        assertNotNull(RODUtil.getDate("22/03/2008"));
    }
}
