package eionet.rod.rdf;

import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 */
public class EventsTest {

    //private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private Date dateFromString(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7)) - 1;
        int day = Integer.parseInt(date.substring(8, 10));
        GregorianCalendar cal = new GregorianCalendar(year, month, day);
        return cal.getTime();
    }

    private String pretty(String nextDeadline, String freq, Date testFrom) {
        return nextDeadline + " is not upcoming compared to "
                + DATE_FORMAT.format(testFrom.getTime())
                + " frequency " + freq;
    }

    private void assertAppear(String nextDeadline, String freq, Date testFrom) {
        String explanation = pretty(nextDeadline, freq, testFrom);
        boolean result = Events.isUpcomingEvent(nextDeadline, freq, testFrom);
        Assert.assertTrue(explanation, result);
    }

    private void assertNotAppear(String nextDeadline, String freq, Date testFrom) {
        String explanation = pretty(nextDeadline, freq, testFrom);
        boolean result = Events.isUpcomingEvent(nextDeadline, freq, testFrom);
        Assert.assertFalse(explanation, result);
    }

    /**
     * If the frequency is every 12 months, then the event will not appear
     * until 36 days before.
     */
    @Test
    public void yearlyIsUpcomingWithin36Days() throws  Exception {
        Date testFrom = dateFromString("2014-07-15");
        String nextDeadline = "2014-07-25"; // 10 days from base
        String freq = "12";
        assertAppear(nextDeadline, freq, testFrom);
    }

    /**
     * If the frequency is every 12 months, then the event will not appear
     * until 36 days before.
     */
    @Test
    public void yearlyIsUpcomingBeyond36Days() throws  Exception {
        Date testFrom = dateFromString("2014-07-30");
        String nextDeadline = "2014-09-30"; // 60 days away
        String freq = "12";
        assertNotAppear(nextDeadline, freq, testFrom);
    }

    /**
     * The event will not appear if the deadline is before the cut-off date.
     * NOT LOGICALLY POSIBLE.
     */
    @Test
    public void yearlyIs15DaysBefore() throws  Exception {
        Date testFrom = dateFromString("2014-07-30");
        String nextDeadline = "2014-07-15"; // 15 days before
        String freq = "12";
        assertNotAppear(nextDeadline, freq, testFrom);
    }

    /**
     * If the frequency is every 72 months, then the event will not appear
     * until 215 days before.
     */
    @Test
    public void sixYearlyIsUpcomingWithin212Days() throws  Exception {
        Date testFrom = dateFromString("2014-01-15");
        String nextDeadline = "2014-08-15"; // 212 days from base
        String freq = "72";
        assertAppear(nextDeadline, freq, testFrom);
    }

    /**
     * If the frequency is every 72 months, then the event will not appear
     * until 215 days before.
     */
    @Test
    public void sixYearlyIsUpcomingWithin217Days() throws  Exception {
        Date testFrom = dateFromString("2014-01-15");
        String nextDeadline = "2014-08-20"; // 217 days from base
        String freq = "72";
        assertNotAppear(nextDeadline, freq, testFrom);
    }

    /**
     * Once-only events are not upcoming.
     */
    @Test
    public void onceIsUpcomingEvent() throws  Exception {
        Date testFrom = dateFromString("2014-01-15");
        String nextDeadline = "2014-02-15";
        String freq = "0";
        assertNotAppear(nextDeadline, freq, testFrom);
    }
}
