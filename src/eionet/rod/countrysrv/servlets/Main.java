package eionet.rod.countrysrv.servlets;

import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.XSQLException;
import com.tee.xmlserver.XMLSource;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Main extends CSServletAC {

  protected String setXSLT(HttpServletRequest req) {
    String mode = req.getParameter("MODE");
      if (mode!= null && mode.equals("PR"))
        return "../app/csprintmain.xsl";
      else
        return "../app/csmain.xsl";
   }

  protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
    String querySource = "";
    String queryPars[][]=null;
    String countryID = params.getParameter("COUNTRY_ID");
    String countryPar;

    if (countryID.equals("0"))
      countryPar = "'%%'";
    else
      countryPar = "'" + countryID + "'";
      
    String order = params.getParameter("ORD");

    //!!! 
    if (order == null)
      order = "TITLE";
      
    //int parCount = 1;


    boolean issue=false;
    boolean deadline =false;

    /*
    boolean ra = false;

    String raID = params.getParameter("RA_ID");

    if (raID != null && !raID.equals("0")){
      ra=true;
      raID = "'" + raID + "'";
    }
    else
      raID="'%%'";
   */ 
    String issueID = params.getParameter("ISSUE_ID");

    if (issueID != null && !issueID.equals("0")){
      issue=true;
      issueID = "'" + issueID + "'";
    }
    else
      issueID="'%%'";
   
    String date1 = params.getParameter("DATE_1");
    String date2 = params.getParameter("DATE_2");

    if ( (date1 !=null && !date1.equals("dd/mm/yyyy")) || (date2 !=null && !date2.equals("dd/mm/yyyy")) )
      deadline=true;

//_log("issue " + issue);
//_log("deadline   " + deadline);


    if (!issue && !deadline) { //only country
      querySource = "../app/csmain.xml";
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

      if (date1.equals("dd/mm/yyyy"))
        date1 ="00/00/0000";
    
      date1=cnvDate(date1);

      //!!!!!!!!!!! fix it
      if (date2.equals("dd/mm/yyyy"))
        date2="31/12/9999";
      
      date2=cnvDate(date2);
      
      queryPars[2][0] = "DEADLINES";
      //queryPars[2][1] = "DEADLINE >= '" + date1 + "' AND DEADLINE <= '" + date2 + "'" ;
      queryPars[2][1] = "NEXT_REPORTING >= '" + date1 + "' AND NEXT_REPORTING <= '" + date2 + "'" ;

      queryPars[3][0] = "ORD";
      queryPars[3][1] = order ;

  
      if( issue && deadline )
        querySource = "../app/csadvanced.xml";
      else if ( issue )
        querySource = "../app/csadvanced_issue.xml"; //only issue
      else 
        querySource = "../app/csadvanced_deadline.xml"; // only deadline
    }
    
    //String queryPars[][] = {{"COUNTRY_ID", countryID}};

    //_log("querysource " + querySource);

    DataSourceIF dataSrc = XMLSource.getXMLSource(querySource, params.getRequest());

    dataSrc.setParameters(queryPars);

    return dataSrc;    
  }

  /**
  * dd/mm/yyyy -> yyyy-MM-dd
  */
  private String cnvDate(String date ){


    date = date.substring(6) + "-" + date.substring(3,5) + "-" + date.substring(0,2);

    return date;

  }

  private void _log(String s ) {
    System.out.println("================= " + s);
  }
}