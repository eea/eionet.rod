/**
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

import java.util.*;
import java.text.*;
import org.apache.log4j.Priority;

import eionet.rod.services.*;

public class DeadlineCalc {
   private static LogServiceIF logger ;  

   static { 
      logger = RODServices.getLogService();
   }

   public void exitApp(boolean successful) {
      if(successful == true)
         logger.info("DeadlineCalc v1.0 - finished succesfully.");
      else
         logger.error("DeadlineCalc v1.0 - failed to complete.");
   }

   public static void main(String[] args) {
      DeadlineCalc dCalc = new DeadlineCalc();
      DbServiceIF db;
      String[][] deadlines;

      logger.info("DeadlineCalc v1.0 - getting deadlines...");

      // Get DB connection
      //
      try {
         db = RODServices.getDbService();
      }  
      catch (Exception e) {
         logger.error("Opening connection to database failed. The following error was reported:\n" + e.toString());      
         e.printStackTrace();
         dCalc.exitApp(false);
         return;
      }

      // Get deadlines
      //
      try {
         deadlines = db.getDeadlines();
      }  
      catch (Exception e) {
         logger.error("Getting deadlines from database failed. The following error was reported:\n" + e.toString());      
         e.printStackTrace();
         dCalc.exitApp(false);
         return;
      }
      if(deadlines == null) {
         logger.info("0 deadlines found");      
         dCalc.exitApp(true);
         return;
      }
      logger.info(deadlines.length + " deadlines found, updating...");      
      
//      for(int i = 0; i < deadlines.length; i++)
//         logger.info(deadlines[i][0] + "\t" + deadlines[i][1] + "\t" + deadlines[i][2] + 
//                     "\t" + deadlines[i][3] + "\t" + deadlines[i][4]);
//      logger.info("");
      
      // Update deadlines and save them back to the database
      //
      for(int i = 0; i < deadlines.length; i++) {
         int m;
         int year = Integer.parseInt(deadlines[i][1].substring(0, 4));
         int month = Integer.parseInt(deadlines[i][1].substring(5, 7));
         int day = Integer.parseInt(deadlines[i][1].substring(8));
         int yearTo = Integer.parseInt(deadlines[i][3].substring(0, 4));
         int monthTo = Integer.parseInt(deadlines[i][3].substring(5, 7));
         int dayTo = Integer.parseInt(deadlines[i][3].substring(8));

         GregorianCalendar repDate = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
         repDate.set(year, month-1, day, 20, 0);
         GregorianCalendar toDate = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
         toDate.set(yearTo, monthTo-1, dayTo, 23, 59);
         GregorianCalendar currDate = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

         // Update TERMINATE field
         //
         try {
            db.saveTerminate(deadlines[i][0], currDate.after(toDate)? "Y" : "N");
         }  
         catch (Exception e) {
            logger.error("Saving TERMINATE value to database failed. The following error was reported:\n" + e.toString());      
         }

         // If not date-based deadline, skip the rest
         //
         if(deadlines[i][2] == null)
            continue;
         int freq = Integer.parseInt(deadlines[i][2]); 
         currDate.add(Calendar.DATE, -3 * freq);
         if(day < 28) {
            while(repDate.before(currDate) && repDate.before(toDate))
               repDate.add(Calendar.MONTH, freq);
            if(repDate.after(toDate))
               repDate.add(Calendar.MONTH, -freq);
         }
         else {
            repDate.add(Calendar.DATE, -3);
            while(repDate.before(currDate) && repDate.before(toDate))
               repDate.add(Calendar.MONTH, freq);
            if(repDate.after(toDate))
               repDate.add(Calendar.MONTH, -freq);
            GregorianCalendar rewindDate = (GregorianCalendar)repDate.clone(); // Save for check below
            m = repDate.get(Calendar.MONTH);
            while(repDate.get(Calendar.MONTH) == m)
               repDate.add(Calendar.DATE, 1);
            repDate.add(Calendar.DATE, -1);
      		// If we went over Valid To date, rewind and repeat
            //
      		if(repDate.after(toDate)) {
               repDate = rewindDate;
               repDate.add(Calendar.MONTH, -freq);
               m = repDate.get(Calendar.MONTH);
               while(repDate.get(Calendar.MONTH) == m)
                  repDate.add(Calendar.DATE, 1);
               repDate.add(Calendar.DATE, -1);
            }
         }

         SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
         String currStr = dFormat.format(currDate.getTime());
         String repStr = dFormat.format(repDate.getTime());

         // Deadline after the next
         //
         if(day < 28)
            repDate.add(Calendar.MONTH, freq);
         else {
            repDate.add(Calendar.DATE, -3);
            repDate.add(Calendar.MONTH, freq);
            m = repDate.get(Calendar.MONTH);
            while(repDate.get(Calendar.MONTH) == m)
               repDate.add(Calendar.DATE, 1);
            repDate.add(Calendar.DATE, -1);
         }
         String repStr2;
         if(repDate.after(toDate))
            repStr2 = "";
         else
            repStr2 = dFormat.format(repDate.getTime());

         try {
            db.saveDeadline(deadlines[i][0], repStr, repStr2);
         }  
         catch (Exception e) {
            logger.error("Saving deadline to database failed. The following error was reported:\n" + e.toString());      
         }
         
//         logger.info("\t\t\t\t\t" + repStr + "\t" + plusStr + "\t" + currStr);
      }
      logger.info("Update complete.");      

      dCalc.exitApp(true);   
   }
}