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
		assertEquals(RODUtil.replaceTags("Fruit & Vegetables"),"Fruit &amp; Vegetables");
	}

	public void test_isURL() {
		assertTrue(RODUtil.isURL("http://cdr.eionet.europa.eu/"));
	}

}
