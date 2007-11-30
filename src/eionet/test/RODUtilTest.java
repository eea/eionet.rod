package eionet.test;

import eionet.rod.RODUtil;
import junit.framework.TestCase;

/**
 * 
 * @author Jaanus Heinlaid, e-mail: <a href="mailto:jaanus.heinlaid@tietoenator.com">jaanus.heinlaid@tietoenator.com</a>
 *
 */
public class RODUtilTest extends TestCase {
	

	public void test_replaceTags() {
		assertEquals(RODUtil.replaceTags("http://cdr.eionet.europa.eu/search?y=1&z=2"),
                "<a href=\"http://cdr.eionet.europa.eu/search?y=1&amp;z=2\">http://cdr.eionet.europa.eu/search?y=1&amp;z=2</a>");

		// Test simple &
		assertEquals(RODUtil.replaceTags("Fruit & Vegetables"),"Fruit &amp; Vegetables");

		// Test newline
		assertEquals(RODUtil.replaceTags("Fruit\nVegetables"),"Fruit<br/>Vegetables");

		// Don't create anchors = true
		assertEquals(RODUtil.replaceTags("http://cdr.eionet.europa.eu/search?y=1&z=7", true),
                "http://cdr.eionet.europa.eu/search?y=1&amp;z=7");

		// Test Unicode char
		assertEquals(RODUtil.replaceTags("€"),"€");

		// Test HTML tags
		assertEquals(RODUtil.replaceTags("<div class='Apostrophs'>"),"&lt;div class='Apostrophs'&gt;");
		assertEquals(RODUtil.replaceTags("<div class=\"Quotes\">"),"&lt;div class=&quot;Quotes&quot;&gt;");
		assertEquals(RODUtil.replaceTags("<a href=\"http://cnn.org/\">CNN</a>"),"&lt;a href=&quot;http://cnn.org/&quot;&gt;CNN&lt;/a&gt;");
	}

	public void test_isURL() {
		assertTrue(RODUtil.isURL("http://cdr.eionet.europa.eu/"));
		assertTrue(RODUtil.isURL("ftp://ftp.eionet.europa.eu/"));
		//assertFalse(RODUtil.isURL("mailto:jaanus.heinlaid@tietoenator.com"));
		assertFalse(RODUtil.isURL("XXX"));
	}

}
