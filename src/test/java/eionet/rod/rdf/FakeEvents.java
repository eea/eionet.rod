package eionet.rod.rdf;

import java.util.GregorianCalendar;
import java.util.Date;

public class FakeEvents extends Events {

    @Override
    protected Date getTodaysDate() {
        return dateFromString("2006-12-25");
    }

    private Date dateFromString(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7)) - 1;
        int day = Integer.parseInt(date.substring(8, 10));
        GregorianCalendar cal = new GregorianCalendar(year, month, day);
        return cal.getTime();
    }

}
