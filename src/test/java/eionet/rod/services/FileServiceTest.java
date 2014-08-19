package eionet.rod.services;

import org.junit.Assert;
import org.junit.Test;

/**
 * fileservice tests.
 */
public class FileServiceTest {

    @Test
    public void getTimePropertyTest() throws  Exception {
        FileServiceIF fileService = RODServices.getFileService();

        long t1 = fileService.getTimePropertyMilliseconds("test.time1", 124L);

        Assert.assertTrue(t1 == 124L);

        long t2 = fileService.getTimePropertyMilliseconds("test.time2", 1L);
        Assert.assertTrue(t2 == 86400000L);
    }

    @Test
    public void getStrPropertyTest() throws  Exception {
        FileServiceIF fileService = RODServices.getFileService();

        String t1 = fileService.getStringProperty("test.text1", "Red");

        Assert.assertEquals(t1, "Green");

        String t2 = fileService.getStringProperty("test.text2", "Blue");

        Assert.assertEquals(t2, "Blue");

    }


}
