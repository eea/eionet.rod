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
 * The Original Code is "NaMod project".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Kaido Laine (TietoEnator)
 */

package eionet.rod.countrysrv.servlets;

import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.XSQLException;
import com.tee.xmlserver.XMLSource;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

public class Main extends CSServletAC {

  protected String setXSLT(HttpServletRequest req) {
    String mode = req.getParameter("MODE");
      if (mode!= null && mode.equals("PR"))
        return PREFIX + "csprintmain.xsl";
      else
        return PREFIX + "csmain.xsl";
   }

  protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
    String querySource = "";
//_log("1");    
    String queryPars[][]=null;
    String countryID = params.getParameter("COUNTRY_ID");
    String countryPar;

    if (countryID.equals("0"))
      countryPar = "'%%'";
    else
      countryPar = "'" + countryID + "'";
//_log("2");          
    String order = params.getParameter("ORD");

    //!!! 
    if (order == null)
      order = "TITLE";
      
    //int parCount = 1;


    boolean issue=false;
    boolean deadline =false;

    String issueID = params.getParameter("ISSUE_ID");

    if (issueID != null && !issueID.equals("0")){
      issue=true;
      issueID = "'" + issueID + "'";
    }
    else
      issueID="'%%'";
   
    String date1 = params.getParameter("DATE_1");
    String date2 = params.getParameter("DATE_2");

    String dlCase = params.getParameter("DEADLINES");

    if ( (date1 !=null && !date1.equals("dd/mm/yyyy")) || (date2 !=null && !date2.equals("dd/mm/yyyy")) || dlCase != null )
      deadline=true;

//_log("issue " + issue);
//_log("deadline   " + deadline);


    if (!issue && !deadline) { //only country
      querySource = PREFIX + "csmain.xml";
      queryPars = new String[2][2];
      queryPars[0][0] = "COUNTRY_ID";
      queryPars[0][1] = countryPar;

      queryPars[1][0] = "ORD";
      queryPars[1][1] = order;
      
    }
    else  { 

      //querySource = "app/advanced.xml";
      queryPars = new String[4][2];

      queryPars[0][0] = "COUNTRY_ID";
      queryPars[0][1] = countryPar;

      //issue id
      queryPars[1][0] = "ISSUE_ID";
      queryPars[1][1] = issueID;

      //deadlines
      queryPars[2][0] = "DEADLINES";

      if ( dlCase != null ) {

        //all Deadlines
        if (dlCase.equals("0")) {
          date1 ="dd/mm/yyyy";
          date2 ="dd/mm/yyyy";
        }
        //next month
        else if (dlCase.equals("1")) {
          Calendar today = Calendar.getInstance();
          date1=getDate(today);
          today.add(Calendar.MONTH, 1);
          date2=getDate(today);
        }

        //next 3 months
        else if (dlCase.equals("2")) {
          Calendar today = Calendar.getInstance();
          date1=getDate(today);
          today.add(Calendar.MONTH, 3);
          date2=getDate(today);
        
        }
        //next 6 months
        else if (dlCase.equals("3")) {
          Calendar today = Calendar.getInstance();
          date1=getDate(today);
          today.add(Calendar.MONTH, 6);
          date2=getDate(today);
        }

        //passed
        else if (dlCase.equals("4")) {
          Calendar today = Calendar.getInstance();
          date1="dd/mm/yyyy";
          date2=getDate(today);
        }
      }
            
      if (date1.equals("dd/mm/yyyy"))
        date1 ="00/00/0000";

      date1=cnvDate(date1);

      //!!!!!!!!!!! fix it
      if (date2.equals("dd/mm/yyyy"))
        date2="31/12/9999";

      date2=cnvDate(date2);


//      System.out.println("========== DATE 1 " + date1);
//      System.out.println("========== DATE 2 " + date2);


      //queryPars[2][1] = "DEADLINE >= '" + date1 + "' AND DEADLINE <= '" + date2 + "'" ;
      //queryPars[2][1] = "NEXT_REPORTING >= '" + date1 + "' AND NEXT_REPORTING <= '" + date2 + "'" ;

      /*String deadLineFld = " PERIOD_ADD(DATE_FORMAT(FIRST_REPORTING,'%Y%m%d'), "
          + "ROUND(PERIOD_DIFF(DATE_FORMAT(CURRENT_DATE,'%Y%m%d'), " 
          + "DATE_FORMAT(FIRST_REPORTING,'%Y%m%d')) / REPORT_FREQ_MONTHS) * REPORT_FREQ_MONTHS)  "; */
      
      queryPars[2][1] = "NEXT_DEADLINE >= '" + date1 + "' AND NEXT_DEADLINE <= '" + date2 + "'" ;
      //queryPars[2][1] = deadLineFld + " >= '" + date1 + "' AND " + deadLineFld + " <= '" + date2 + "' AND T_ACTIVITY.VALID_TO >= CURRENT_DATE " ;
         
      
      queryPars[3][0] = "ORD";
      queryPars[3][1] = order ;
//_log("5");

      if( issue && deadline )
        querySource = PREFIX + "csadvanced.xml";
      else if ( issue )
        querySource = PREFIX + "csadvanced_issue.xml"; //only issue
      else 
        querySource = PREFIX + "csadvanced_deadline.xml"; // only deadline

    }


    
    //String queryPars[][] = {{"COUNTRY_ID", countryID}};
    //_log("********************************");
    //_log("querysource " + querySource);
    //_log("********************************");

    HttpServletRequest req = params.getRequest();
    DataSourceIF dataSrc = XMLSource.getXMLSource(querySource, req);

    dataSrc.setParameters(queryPars);
    addMetaInfo(dataSrc);
    
    return userInfo(req,dataSrc);    
  }

  /**
  * dd/mm/yyyy -> yyyy-MM-dd
  */
  private String cnvDate(String date ){


    //date = date.substring(6) + "-" + date.substring(3,5) + "-" + date.substring(0,2);
    date = date.substring(6) +  date.substring(3,5) +  date.substring(0,2);

    return date;

  }
  private String getDate(Calendar cal ) {
    String day = Integer.toString( cal.get( Calendar.DATE) );
    if (day.length() == 1) day  ="0" + day;
    String month = Integer.toString( cal.get( Calendar.MONTH) +1 );    
    if (month.length() == 1) month  ="0" + month;        
    String year = Integer.toString( cal.get( Calendar.YEAR) );    
    
    return day + "/" + month + "/" + year;
  }
  private void _log(String s ) {
    System.out.println("================= " + s);
  }
}