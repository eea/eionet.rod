/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is "EINRC-5 / WebROD Project".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Ander Tenno (TietoEnator)
 */

package eionet.rod;

import eionet.rod.services.LogServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.modules.db.dao.IObligationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DeadlineCalc {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadlineCalc.class);

    public void exitApp(boolean successful) {
        if (successful == true)
            LOGGER.info("DeadlineCalc v1.0 - finished succesfully.");
        else
            LOGGER.error("DeadlineCalc v1.0 - failed to complete.");
    }

    public static void execute() {
        DeadlineCalc dCalc = new DeadlineCalc();
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        IObligationDao db;
        String[][] deadlines;

        LOGGER.info("DeadlineCalc v1.0 - getting deadlines...");

        // Get DB connection
        //
        try {
            db = RODServices.getDbService().getObligationDao();
        } catch (Exception e) {
            RODServices.sendEmail("Error in deadlines calc", "Opening connection to database failed. The following error was reported:\n" + e);
            LOGGER.error("Opening connection to database failed. The following error was reported:");
            LOGGER.error(e.getMessage(), e);
            dCalc.exitApp(false);
            return;
        }

        // Get deadlines
        //
        try {
            deadlines = db.getDeadlines();
        } catch (Exception e) {
            LOGGER.error("Getting deadlines from database failed. The following error was reported:\n" + e);
            RODServices.sendEmail("Error in deadlines calc", e.toString());
            LOGGER.error(e.getMessage(), e);
            dCalc.exitApp(false);
            return;
        }
        if (deadlines == null) {
            LOGGER.info("0 deadlines found");
            dCalc.exitApp(true);
            return;
        }
        LOGGER.info(deadlines.length + " deadlines found, updating...");

        // Update deadlines and save them back to the database
        for (int i = 0; i < deadlines.length; i++) {
            int m;
            int year = Integer.parseInt(deadlines[i][1].substring(0, 4));
            int month = Integer.parseInt(deadlines[i][1].substring(5, 7));
            int day = Integer.parseInt(deadlines[i][1].substring(8));
            int yearTo = Integer.parseInt(deadlines[i][3].substring(0, 4));
            int monthTo = Integer.parseInt(deadlines[i][3].substring(5, 7));
            int dayTo = Integer.parseInt(deadlines[i][3].substring(8));

            GregorianCalendar repDate = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            repDate.set(year, month - 1, day, 20, 0);
            GregorianCalendar toDate = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            toDate.set(yearTo, monthTo - 1, dayTo, 23, 59);
            GregorianCalendar currDate = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

            // Update TERMINATE field
            //
            try {
                db.saveTerminate(Integer.valueOf(deadlines[i][0]), currDate.after(toDate) ? "Y" : "N");
            } catch (Exception e) {
                RODServices.sendEmail("Error in deadlines calc", "Saving terminate failed " + e.toString());
                LOGGER.error("Saving TERMINATE value to database failed. The following error was reported:", e);
            }

            // If not date-based deadline, skip the rest
            //
            if (deadlines[i][2] == null)
                continue;
            int freq = Integer.parseInt(deadlines[i][2]);
            // No point in updating if non-repeating
            //
            if (freq == 0)
                continue;
            currDate.add(Calendar.DATE, -3 * freq);
            if (day < 28) {
                while (repDate.before(currDate) && repDate.before(toDate))
                    repDate.add(Calendar.MONTH, freq);
                if (repDate.after(toDate))
                    repDate.add(Calendar.MONTH, -freq);
            } else {
                repDate.add(Calendar.DATE, -3);
                while (repDate.before(currDate) && repDate.before(toDate))
                    repDate.add(Calendar.MONTH, freq);
                if (repDate.after(toDate))
                    repDate.add(Calendar.MONTH, -freq);
                GregorianCalendar rewindDate = (GregorianCalendar) repDate.clone(); // Save for check below
                m = repDate.get(Calendar.MONTH);
                while (repDate.get(Calendar.MONTH) == m)
                    repDate.add(Calendar.DATE, 1);
                repDate.add(Calendar.DATE, -1);
                // If we went over Valid To date, rewind and repeat
                //
                if (repDate.after(toDate)) {
                    repDate = rewindDate;
                    repDate.add(Calendar.MONTH, -freq);
                    m = repDate.get(Calendar.MONTH);
                    while (repDate.get(Calendar.MONTH) == m)
                        repDate.add(Calendar.DATE, 1);
                    repDate.add(Calendar.DATE, -1);
                }
            }

            String repStr = dFormat.format(repDate.getTime());

            // Update TERMINATE field
            //
            try {
                if (repDate.before(currDate))
                    db.saveTerminate(Integer.valueOf(deadlines[i][0]), "Y");
                // logger.info("Terminate!\t\t\t\t" + repStr + "\t" + plusStr + "\t" + currStr);
            } catch (Exception e) {
                LOGGER.error("Saving TERMINATE value to database failed. The following error was reported:", e);
                RODServices.sendEmail("Error in deadlines calc", "Saving terminate failed " + e.toString());
            }

            // Deadline after the next
            //
            if (day < 28)
                repDate.add(Calendar.MONTH, freq);
            else {
                repDate.add(Calendar.DATE, -3);
                repDate.add(Calendar.MONTH, freq);
                m = repDate.get(Calendar.MONTH);
                while (repDate.get(Calendar.MONTH) == m)
                    repDate.add(Calendar.DATE, 1);
                repDate.add(Calendar.DATE, -1);
            }
            String repStr2;
            if (repDate.after(toDate))
                repStr2 = "";
            else
                repStr2 = dFormat.format(repDate.getTime());

            try {
                db.saveDeadline(Integer.valueOf(deadlines[i][0]), repStr, repStr2, deadlines[i][1]);
            } catch (Exception e) {
                LOGGER.error("Saving deadline to database failed. The following error was reported:\n", e);
                RODServices.sendEmail("Error in deadlines calc", "Saving deadline failed " + e.toString());
            }

            // logger.info("\t\t\t\t\t" + repStr + "\t" + plusStr + "\t" + currStr);
        }
        LOGGER.info("Update complete.");

        dCalc.exitApp(true);
    }

    public static void main(String[] args) {
        execute();
    }
}
