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

package eionet.rod.services.modules;


import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.StringTokenizer;

import java.util.Vector;
import java.util.Hashtable;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


import com.tee.util.Util;
import com.tee.xmlserver.DBPoolIF;
import com.tee.xmlserver.XDBApplication;
//import com.tee.xmlserver.Logger;

import eionet.rod.services.RODServices;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.DbServiceIF;
import eionet.rod.services.ServiceException;

import java.net.URLEncoder;
import eionet.rod.countrysrv.Extractor;
import eionet.rod.services.LogServiceIF;

/**
 * CountrySrv database methods implementation.
 *
 * @author  Kaido Laine
 * @version 1.0
 */
public class DbServiceImpl implements DbServiceIF, eionet.rod.services.Config {

  //private static String ANSI_DATE_FORMAT = "yyyy-MM-dd";
	private static String ANSI_DATETIME_FORMAT = "yyyy-MM-dd hh:MM";

  public static final int OTHER = 0;
  public static final int ORACLE = 1;
  public static final int MYSQL = 2;

  private int _driverType = MYSQL;

  // parameters to create DB connection
  private String crUrlPref;
  private String dbUrl;
  private String dbDriver;
  private String dbUser;
  private String dbPsw;

  private static String rodDomain;

  private DBPoolIF dbPool = null;

  private LogServiceIF logger;
  //private boolean useTestPool = true;


  // Helper class: stores 1..n dates as strings and tells if the date is OK,
  // such as "28-11-2001", or just some text, for example, "when it is ready".
  //
  private class DateHelper {
    boolean isProperDate;
    boolean isEmptyString;
    String[] dateArr;
    
    private DateHelper(int noOfDates, boolean isPropDate, boolean isEmptyStr) {
      isProperDate = isPropDate;
      isEmptyString = isEmptyStr;
      dateArr = new String[noOfDates];
      dateArr[0] = "";
    }
  }


  private String getDeliveryId(String identifier) throws ServiceException {
    String deliveryId = _executeStringQuery("SELECT PK_DELIVERY_ID FROM " +
                     "T_DELIVERY WHERE DELIVERY_URL='" + identifier + "'")[0][0];
    return deliveryId;
  }

  private int getActivitiesCountInGroup(String groupId, String raId) throws ServiceException {
    String sql = "SELECT COUNT(RA_ID) FROM T_GROUP_LNK WHERE FK_GROUP_ID=" + groupId + " AND RA_ID=" + raId;
    String[][] res = _executeStringQuery(sql);
    int count = Integer.parseInt(res[0][0]);

    return count;

  }

  public int getActivitiesCountInIssue( String issueId, String raId) throws ServiceException {
      String sqlq = "SELECT COUNT(RA_ID) FROM T_ISSUE_LNK WHERE FK_ISSUE_ID=" + issueId + " AND RA_ID=" + raId ;
      String[][] res = _executeStringQuery(sqlq);
      int cnt = Integer.parseInt(res[0][0]);

      return cnt;

  }

    public int getDetailsId( int countryId, String raUrl) throws ServiceException {
      String detailsIdStr[][] = _executeStringQuery("SELECT PK_DETAILS_ID FROM " +
                        "T_ACTIVITY_DETAILS WHERE FK_COUNTRY_ID=" + countryId + 
                        " AND RA_URL='" + raUrl + "'");
    if(detailsIdStr == null || detailsIdStr.length == 0) 
      throw new ServiceException (" Unable to link delivery to RA: no data in the database for the given RA URL (" + 
                  raUrl + ") and country id (" + countryId + ").");

    int detailsId = Integer.parseInt(detailsIdStr[0][0]);
    return detailsId;
    }
    public int getCountryId(String twoLetter ) throws ServiceException {
      String[][] countryIdStr = _executeStringQuery("SELECT PK_COUNTRY_ID FROM " +
                              "T_COUNTRY WHERE COUNTRY_TWOLETTER='" + twoLetter + "'");

      if(countryIdStr == null || countryIdStr.length == 0) 
        throw new ServiceException("*** Unable to link delivery to RA: invalid country code (" + twoLetter + ").");

    int countryId = Integer.parseInt(countryIdStr[0][0]);                                        
    return countryId;
    
    }


/**
 * DB dependent date conversion function
 */
  private String _dateLiteral(String dateLit) {
     if (_driverType == ORACLE)
       return "TO_DATE('" + dateLit + "','" + ANSI_DATETIME_FORMAT + "')";
     else
       return "'" + dateLit + "'";
  }


/**
 *
 */
   private void _close(Connection con, Statement stmt, ResultSet rset)
      throws ServiceException {
      try {
         if (rset != null)
            rset.close();
         if (stmt != null) {
            stmt.close();
            if (!con.getAutoCommit())
               con.commit();
         }
      } catch (Exception e) {
         throw new ServiceException("Error"  + e.getMessage()) ;
      } finally {  
         try { con.close(); } catch (SQLException e) {
	         throw new ServiceException("Error"  + e.getMessage()) ;
         }
      }
   }

/**
 *
 */

	 private String[][] _executeStringQuery(String sql_stmt) throws ServiceException {
      Vector rvec = new Vector(); // Return value as Vector
  		String rval[][] = {};       // Return value
      Connection con = null;
      Statement stmt = null;
      ResultSet rset = null;

      _log(sql_stmt);
      
      // Process the result set
      con = getConnection();
      
      try {
        stmt = con.createStatement();
			  rset = stmt.executeQuery(sql_stmt);
			  ResultSetMetaData md = rset.getMetaData();

        //number of columns in the result set
  			int colCnt = md.getColumnCount();

  			while (rset.next()) {
          String row[] = new String[colCnt]; // Row of the result set

				// Retrieve the columns of the result set
				for (int i = 0; i < colCnt; ++i)
				   row[i] = rset.getString(i + 1);

					rvec.addElement(row); // Store the row into the vector
				}
			} catch (SQLException e) {
				 logger.error("Error occurred when processing result set: " + sql_stmt,e);
         throw new ServiceException("Error occurred when processing result set: " + sql_stmt);
			} finally {
				 // Close connection
				//_close(con, stmt, rset);
				 _close(con, stmt, null);
			}

			// Build return value
			if (rvec.size() > 0) {
				 rval = new String[rvec.size()][];

				 for (int i = 0; i < rvec.size(); ++i)
						rval[i] = (String[])rvec.elementAt(i);
			}

			// Success
			return rval;
	 }

/**
 *
 */
   private int _executeUpdate(String sql_stmt) throws ServiceException {

     _log(sql_stmt);

      Connection con = null; // Connection object
      Statement stmt = null; // Statement object
      int rval = 0;          // Return value
      // Get connection object
      con = getConnection();

      // Create statement object
      try {
         stmt = con.createStatement();
      } catch (SQLException e) {
         // Error handling
         //   logger.error( "UpdateStatement failed: " + e.toString());
         // Free resources
         try {
            _close(con, stmt, null);
         } catch (Throwable exc) {
	          throw new ServiceException("_close() failed: " + sql_stmt);
         }
        logger.error("Connection.createStatement() failed: " + sql_stmt,e);
        throw new ServiceException("Update failed: " + sql_stmt);        
      }


   // Execute update
   try {
      rval = stmt.executeUpdate(sql_stmt);
   } catch (Exception e) {
      // Error handling
      throw new ServiceException( "Statement.executeUpdate(" + sql_stmt + ") failed" + e.getMessage() );
   } finally {
      // Free resources
      _close(con, stmt, null);
   }
    // Success
    return rval;
  }

/**
* Creates new DbServiceImpl object
*/
  public DbServiceImpl() throws ServiceException {

      dbUrl = RODServices.getFileService().getStringProperty( FileServiceIF.DB_URL);
      dbDriver = RODServices.getFileService().getStringProperty( FileServiceIF.DB_DRV);
      dbUser = RODServices.getFileService().getStringProperty( FileServiceIF.DB_USER_ID);
      dbPsw = RODServices.getFileService().getStringProperty( FileServiceIF.DB_USER_PWD);

      rodDomain  = RODServices.getFileService().getStringProperty( ROD_URL_DOMAIN );

      crUrlPref = RODServices.getFileService().getStringProperty( FileServiceIF.CR_URL_PREFIX);

      logger = RODServices.getLogService();
      
// metadata attr names->
//<-
            
       try {
         dbPool = new DBPool( dbUrl, dbDriver, dbUser, dbPsw ) ;        
       } catch(Exception e ){
	       throw new ServiceException("Error getting DBPool: " + e.toString());
       }
   }

/**
 * Returns new database connection. 
 *
 * @throw ServiceException if no connections were available.
 */   
  public Connection getConnection() throws ServiceException {
    Connection con = dbPool.getConnection( dbUser, dbPsw );
    if (con == null)
      throw new ServiceException("Failed to get database connection");

    return con;
    }



  private void _log(String s) {
  	logger.debug("*********** " + s);
	  }

  public String[][] getActivityIds() throws ServiceException {
    return _executeStringQuery("SELECT DISTINCT RA_ID FROM T_ACTIVITY_DETAILS");
  }

  public String[][] getRaData() throws ServiceException {
    String sql = "SELECT a.PK_RA_ID, a.TITLE, s.PK_SPATIAL_ID, s.SPATIAL_NAME " + 
      " FROM T_ACTIVITY a, T_REPORTING r, T_SPATIAL_LNK sl, T_SPATIAL s " + 
      " WHERE a.FK_Ro_id = r.pk_ro_id and r.pk_ro_id = sl.fk_ro_id " + 
      " AND sl.FK_SPATIAL_ID = s.PK_SPATIAL_ID AND s.SPATIAL_TYPE='C';";

     return _executeStringQuery(sql);
  }  
/*  public String[][] getRaUrls() throws ServiceException {
    return _executeStringQuery("SELECT DISTINCT RA_URL FROM T_ACTIVITY_DETAILS");
  }
*/

  public String[][] getRespRoles() throws ServiceException {
    //return _executeStringQuery("SELECT RESPONSIBLE_ROLE FROM T_ACTIVITY_DETAILS");
    String sql = "SELECT CONCAT(a.RESPONSIBLE_ROLE, '-' , LCASE(s.SPATIAL_TWOLETTER))  " + 
      " FROM T_ACTIVITY a, T_SPATIAL s, T_REPORTING r, T_SPATIAL_LNK sl  " + 
      " WHERE  a.FK_RO_ID = r.PK_RO_ID AND sl.FK_RO_ID=r.PK_RO_ID " +
      " AND sl.FK_SPATIAL_ID = s.PK_SPATIAL_ID AND a.RESPONSIBLE_ROLE IS NOT NULL " +
      " AND a.RESPONSIBLE_ROLE <> '' AND s.SPATIAL_TYPE = 'C';" ;

      return _executeStringQuery(sql);
  }

  public void dropTables(int mode) throws ServiceException {
  /*
      _executeUpdate("DELETE FROM T_COUNTRY");
      _executeUpdate("DELETE FROM T_GROUP");
      _executeUpdate("DELETE FROM T_ISSUE");
  */
      /*if (mode == Extractor.ALL_DATA || mode == Extractor.ACTS || mode == Extractor.ACTS_DELIVERIES ) {
        _executeUpdate("DELETE FROM T_ACTIVITY_DETAILS");
        _executeUpdate("DELETE FROM T_DEADLINE");
      }
      */
/*     
      _executeUpdate("DELETE FROM T_GROUP_LNK");
      _executeUpdate("DELETE FROM T_ISSUE_LNK");
*/
      if (mode == Extractor.ALL_DATA || mode == Extractor.DELIVERIES  ) {
        _executeUpdate("DELETE FROM T_DELIVERY");
        //_executeUpdate("DELETE FROM T_DELIVERY_LNK");
      }
    
      //_executeUpdate("DELETE FROM T_ORGANISATION");
      if (mode == Extractor.ALL_DATA || mode == Extractor.ROLES ) {
        _executeUpdate("DELETE FROM T_ROLE");
      //_executeUpdate("DELETE FROM T_PERSON");
      }
  }


/*
  public void saveCountries( Vector countries ) throws ServiceException {

      
    for(int i = 0; i < countries.size(); i++) {

      //record for country
      Hashtable country = (Hashtable)countries.elementAt(i);

      String pkID , name, twoLetter;

      pkID = (String)country.get("PK_SPATIAL_ID");
      pkID = (pkID==null ? "" : pkID);

      name = (String)country.get("SPATIAL_NAME");
      name = (name==null ? "" : name);      

      twoLetter = (String)country.get("SPATIAL_TWOLETTER");
      twoLetter = (twoLetter==null ? "" : twoLetter);      
      
      String sql = "INSERT INTO T_COUNTRY SET PK_COUNTRY_ID=" + pkID + ", COUNTRY_NAME='" + 
                   name + "', COUNTRY_TWOLETTER='" + twoLetter + "'";

      _executeUpdate(sql);
    }
  }
*/
/*
  public void saveParamGroups(Vector paramGroups ) throws ServiceException {

    for(int i = 0; i < paramGroups.size(); i++) {
      String pkId, name, type;
      Hashtable prmGrp = (Hashtable)paramGroups.elementAt(i);
      
      pkId = (String)prmGrp.get("PK_GROUP_ID");
      pkId = (pkId == null ? "" : pkId);
      name = (String)prmGrp.get("GROUP_NAME");
      name = (name == null ? "" : name);
      type = (String)prmGrp.get("GROUP_TYPE");
      type = (type == null ? "" : type);

      
      String sql = "INSERT INTO T_GROUP SET PK_GROUP_ID=" + pkId + ", GROUP_NAME='" + 
                   name + "', GROUP_TYPE='" + type + "'";
      _executeUpdate(sql);
    }
  }
*/
/*
  public void saveIssues(Vector issues ) throws ServiceException {

  for(int i = 0; i < issues.size(); i++) {
      String pkId, name;
      Hashtable issue = (Hashtable)issues.elementAt(i);
      
      pkId = (String)issue.get("PK_ISSUE_ID");
      pkId = (pkId == null ? "" : pkId);
      name = (String)issue.get("ISSUE_NAME");
      name = (name == null ? "" : name);

      String sql = "INSERT INTO T_ISSUE SET PK_ISSUE_ID=" + pkId + ", ISSUE_NAME='" + 
                   name + "'";
      _executeUpdate(sql);
    }
  }
*/
/*
 public void saveActivities(Vector activities ) throws ServiceException {

    for(int i = 0; i < activities.size(); i++) {
      String raId, roId, title, type, reporting_format, report_to,
        roUrl, resp_role, countryId, raUrl, format_name, report_format_url, alias;
        
      Hashtable act = (Hashtable)activities.elementAt(i);
      
      raId = (String)act.get("PK_RA_ID");
      raId = (raId == null ? "" : raId);

      roId = (String)act.get("FK_RO_ID");
      roId = (roId == null ? "" : roId);

      countryId = (String)act.get("FK_SPATIAL_ID");
      countryId = (countryId == null ? "" : countryId);

      roUrl = (String)act.get("RO_URL");
      roUrl = (roUrl == null ? "" : roUrl);

      raUrl = (String)act.get("RA_URL");
      raUrl = (raUrl == null ? "" : raUrl);

      title = (String)act.get("TITLE");
      title = (title == null ? "" : title);
      
      resp_role = (String)act.get("RESPONSIBLE_ROLE");
      resp_role = (resp_role == null ? "" : resp_role);
      
      report_to = (String)act.get("CLIENT_NAME");
      report_to= ( report_to == null ? "" : report_to);

      reporting_format = (String)act.get("REPORTING_FORMAT");
      reporting_format = (reporting_format == null ? "" : reporting_format);

      format_name = (String)act.get("FORMAT_NAME");
      format_name = (format_name == null ? "" : format_name);

      alias = (String)act.get("ALIAS");
      alias = (alias == null ? "" : alias );

      report_format_url = (String)act.get("REPORT_FORMAT_URL");
      report_format_url = (report_format_url == null ? "" : report_format_url);


      
      String sql = "INSERT INTO T_ACTIVITY_DETAILS SET RA_ID=" + raId + ", RO_ID=" + 
                   roId + ", FK_COUNTRY_ID=" + countryId + ", RA_URL='" + raUrl +
                   "', TITLE='" + title + 
                   "', REPORT_TO='" + ((report_to.trim().length() == 0)? "" : report_to) + 
                   "', RESPONSIBLE_ROLE='" + ((resp_role.trim().length() == 0)? "" : resp_role) + 
                   "', EXCHANGE_FORMAT='" + strLit(reporting_format) + "', GUIDELINE_URL='" + 
                   report_format_url + "', GUIDELINE_NAME='" + strLit(format_name) + "', RO_TITLE='" + 
                   strLit(alias) + "', RO_URL='" + roUrl + "'";
      _executeUpdate(sql);
    }
  }
*/
/*
   public void saveParamGroupLinks( Vector prmGrpLinks) throws ServiceException {

  
    for(int i = 0; i < prmGrpLinks.size(); i++) {
      String groupId= (String)((Hashtable)prmGrpLinks.elementAt(i)).get("FK_GROUP_ID");
      String raId= (String)((Hashtable)prmGrpLinks.elementAt(i)).get("FK_RA_ID");
      
      int actC = getActivitiesCountInGroup( groupId, raId);
      if(actC == 0) {
        String sql = "INSERT INTO T_GROUP_LNK SET FK_GROUP_ID=" + groupId + ", RA_ID=" + raId;
        _executeUpdate(sql);
      }
    }
  }
*/
/*
   public void saveIssueLinks( Vector issueLinks ) throws ServiceException {
    for(int i = 0; i < issueLinks.size(); i++) {
      String issueId= (String)((Hashtable)issueLinks.elementAt(i)).get("FK_ISSUE_ID");
      String raId= (String)((Hashtable)issueLinks.elementAt(i)).get("PK_RA_ID");
      
      if(getActivitiesCountInIssue( issueId,  raId) == 0 ) {
        String sql = "INSERT INTO T_ISSUE_LNK SET FK_ISSUE_ID=" + issueId + ", RA_ID=" + raId;
        _executeUpdate(sql);
      }
    }

   }
*/
/*
   public void saveDeadlines( String raId, String deadline ) throws ServiceException {
    if(deadline == null)
      return;

    //_log( "=============== DEADLINES ");
    String sql = "INSERT INTO T_DEADLINE SET RA_ID=" + raId;
    DateHelper dates = dateParser(deadline);
    if(dates.isEmptyString == true)
      _executeUpdate(sql);
    else if(dates.isProperDate == false)
      _executeUpdate(sql + ", DEADLINE_TEXT='" + dates.dateArr[0] + "'");
    else
      for(int i = 0; i < dates.dateArr.length; i++)
        _executeUpdate(sql + ", DEADLINE='" + dates.dateArr[i] + "'");

    //_log( "=============== DEADLINES OK FOR RA_ID=" + raId);

  }
*/
// Takes in a raw date string and parses it to correct date string(s).
  // There are four types of date:
  // 1. No date
  // 2. One date (dd/mm/yyyy)
  // 3. Multiple dates (dd/mm/yyyy) separated by commas
  // 4. Textual date
  //
  public DateHelper dateParser(String dateStr) {
    Date date;
    String s;
    DateHelper dateHelper;
    SimpleDateFormat rodDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    // Empty date or surely textual date?
    //
    if(dateStr.trim().length() < 10) {
      dateHelper = new DateHelper(1, false, (dateStr.trim().length() == 0));
      return dateHelper;
    }

    // Slice the string in tokens and attempt to convert each of them to a
    // date format. Succesful attempt results in 1+ strings in dateHelper.dateArr 
    // and dateHelper.isProperDate set to true. Otherwise, if even one date 
    // conversion fails, write the entire dateStr to dateHelper.dateArr[0] and set
    // dateStr.isProperDate to false.
    //
    StringTokenizer strTok = new StringTokenizer(dateStr, ",");
    dateHelper = new DateHelper(strTok.countTokens(), true, false);
    for(int i = 0; strTok.hasMoreTokens(); i++) {
      try {
        date = rodDateFormat.parse(strTok.nextToken().trim());
      }
      catch (ParseException e) {
        dateHelper.isProperDate = false;
        dateHelper.dateArr[0] = dateStr;
        break;
      }
      dateHelper.dateArr[i] = dateFormat.format(date);
    }

    return dateHelper;
  }


  public void saveRole(Hashtable role) throws ServiceException {
  
    /*if(role == null)
      return; */

    String roleId = (String)role.get("ID");
    String circaUrl = (String)role.get("URL");
    String desc = (String)role.get("DESCRIPTION");
    String mail = (String)role.get("MAIL");
    
    String sql = "INSERT INTO T_ROLE SET ROLE_NAME='" + desc +
                 "', ROLE_EMAIL='" + mail + "', ROLE_ID='" + roleId + "',  " + 
                " ROLE_URL ='" + circaUrl + "'";
    if (roleId != null)
      _executeUpdate(sql);

  }

  /**
  *
  */
   public void saveDelivery(String raId, String countryId, Hashtable delivery ) throws ServiceException {

    if(delivery == null || delivery.size() == 0)
      return;
    //_log("delivery = " + delivery );
    String crId = (String)(delivery.keys().nextElement());
    Hashtable metaData = (Hashtable)delivery.get( crId );

    FileServiceIF fS = new FileServiceImpl();

    String titlePred = fS.getStringProperty( fS.CONTREG_TITLE_PREDICATE );
    String typePred = fS.getStringProperty( fS.CONTREG_TYPE_PREDICATE );    
    String datePred = fS.getStringProperty( fS.CONTREG_DATE_PREDICATE );    
    String identifierPred = fS.getStringProperty( fS.CONTREG_IDENTIFIER_PREDICATE );    
    String formatPred = fS.getStringProperty( fS.CONTREG_FORMAT_PREDICATE );    
    
    //Each hash contains Strings as Keys and Vectors as values

    String title = cnvVector( (Vector)metaData.get( titlePred ) );
    String date = cnvVector( (Vector)metaData.get(datePred));
    String identifier = cnvVector( (Vector)metaData.get( identifierPred ));
    String type =  cnvVector( (Vector)metaData.get( typePred ));
    String format = cnvVector( (Vector)metaData.get(formatPred ));
    
    String crURL = composeCrURL( crId );

    String sql = "INSERT INTO T_DELIVERY SET TITLE='" + 
                 ((title == null)? "" : title) + "', UPLOAD_DATE='" +
                 ((date == null)? "" : date) + "', CONTREG_URL=\"" + 
                 ((crURL == null)? "" : crURL) + "\", DELIVERY_URL='" +
                 identifier + "', TYPE='" +
                 ((type == null)? "" : type) + "', FORMAT='" +
                 ((format == null)? "" : format) + "'," +
                 " FK_SPATIAL_ID = " + countryId + ", " +
                 " FK_RA_ID = " + raId + " ;" ;

    _executeUpdate(sql);
 
    //delivery_link

    //String deliveryId = getDeliveryId( identifier );
    //saveDeliveryLink( deliveryId, detailsId );


   }  

/*  
  private void saveDeliveryLink( String deliveryId, String actDetailsId ) throws ServiceException {
    String sql = "INSERT INTO T_DELIVERY_LNK (FK_DELIVERY_ID, FK_DETAILS_ID ) VALUES (" +
      deliveryId + ", " + actDetailsId + ")";

    _executeUpdate(sql);
}
*/
  private String composeCrURL( String deliveryURL ){
    deliveryURL = URLEncoder.encode( deliveryURL );
    String url = crUrlPref + "md5('" + deliveryURL + "')";
    
    return url;
  }

  private String cnvVector( Vector v ) {

    //quick fix
    if (v == null)
      return "";
      
    //_log("vector = " + v);  
    StringBuffer s = new StringBuffer();
    for (int i =0; i< v.size(); i++) {
      if (v.elementAt(i) != null) {
        s.append((String)v.elementAt(i));
        if (i < v.size() -1 )
          s.append (",");
      }
    }    

    return s.toString();
  }


 /**
	* Returns all Reporting activities
	*/
/*
  public Vector getActivityDetails(  ) throws ServiceException {

  //_log("*********** HAAAAAAAAAAAAAAAAAAAAAAAAAAA");

    String sql = "SELECT PK_RA_ID, A.FK_RO_ID, FK_SPATIAL_ID, CONCAT(RESPONSIBLE_ROLE, '-',  LOWER(S.SPATIAL_TWOLETTER)) AS RESPONSIBLE_ROLE, " + 
      " CONCAT('" + rodDomain + "/" + URL_SERVLET + "?" + URL_ACTIVITY_ID + "=" + "', PK_RA_ID, " + 
      "'&" + URL_ACTIVITY_AID + "=', A.FK_RO_ID, '&" + URL_ACTIVITY_AMODE + "') AS RA_URL, IF(A.TITLE='','Activity', A.TITLE) AS TITLE, " +
      " C.CLIENT_NAME, " + 
      " REPORTING_FORMAT, " +
      " REPORT_FORMAT_URL, FORMAT_NAME, IF(R.ALIAS='', 'Obligation', R.ALIAS) AS ALIAS, " +
      " CONCAT('" + rodDomain + "/" + URL_SERVLET + "?" + URL_ACTIVITY_ID + "=" + "', A.FK_RO_ID, '&" + URL_ACTIVITY_AID + "='" +
      " , A.FK_SOURCE_ID, '&" + URL_ACTIVITY_RMODE + "') AS RO_URL " +
      " FROM T_ACTIVITY A, T_SPATIAL_LNK SL, T_SPATIAL S, T_REPORTING R LEFT OUTER JOIN T_CLIENT C ON R.FK_CLIENT_ID = C.PK_CLIENT_ID  " +
      " WHERE A.FK_RO_ID = R.PK_RO_ID AND SL.FK_RO_ID = R.PK_RO_ID AND S.PK_SPATIAL_ID = SL.FK_SPATIAL_ID " ;

    //_log("*********** SQL = " + sql);

      Vector ret = null;

      //_log("SQL="+ sql)      ;

      ret = _getVectorOfHashes(sql);

      return ret;          
  }

*/
 /**
 * returns result as vector of String arrays
 */
  private  Vector _getVectorOfHashes(String sql_stmt) throws ServiceException {

     Vector rvec = new Vector(); // Return value as Vector

      Connection con = null;
      Statement stmt = null;
      ResultSet rset = null;

      Hashtable h = null;

      _log(sql_stmt);

      con = getConnection();
      //_log("CONN OK");      
      try {
        stmt = con.createStatement();
			  rset = stmt.executeQuery(sql_stmt);
        //_log("RESULT OK");        
			  ResultSetMetaData md = rset.getMetaData();

        //number of columns in the result set
  			int colCnt = md.getColumnCount();
        //_log("COLS OK" + colCnt);    

  			while (rset.next()) {
          h = new Hashtable();

				// Retrieve the columns of the result set
				for (int i = 0; i < colCnt; ++i) {
          String name = md.getColumnName(i + 1);
          String value = rset.getString(i + 1);
          if ( value == null)
            value = "";
            
          h.put( name, value  );
        }

					rvec.addElement(h); // Store the row into the vector
				}
			} catch (SQLException e) {
				 logger.error("Error occurred when processing result set: " + sql_stmt,e);
         throw new ServiceException("Error occurred when processing result set: " + sql_stmt);
			}  catch (NullPointerException nue ) {
          nue.printStackTrace( System.out );
      } finally {
				 _close(con, stmt, null);
			}
    //_log(" return vec");
    return rvec;

  }


 /**
	* Returns deadline for this activity
  * @return  Vector
  * @param String activity ID
  * @throw ServiceException
	*/
  /*
  
  public String getDeadLine( String activityId) throws ServiceException {

    String deadLine = ""; 
    String sql = "SELECT NEXT_REPORTING FROM T_ACTIVITY " + 
      " WHERE PK_RA_ID = " + activityId; 

    String ret[][] = _executeStringQuery(sql );

    //_log ( "LENGTH = " + ret.length);
    if (ret.length == 1) 
      deadLine = ret[0][0] == null ? "" : ret[0][0] ;
    //quick fix, has to be studied, why empty array is returned
    else if ( ret.length == 0) 
      deadLine = "";
    else
      throw new ServiceException("No such RA ID=" + activityId);
    //_log("RA= "+ activityId + " deadline= " + deadLine);      
    return deadLine;
    }
*/

 /**
	* Returns all Reporting activities pk_id + title
  * used in RDF generating
	*/

  public  Vector getActivities(  ) throws ServiceException {
      String sql = "SELECT PK_RA_ID, TITLE FROM T_ACTIVITY ";
      return  _getVectorOfHashes(sql);
  }

   private String strLit(String s ) {
    int i  = 0;
    while (i< s.length() ) {
      if ( s.charAt(i) == '\''){
        s = s.substring(0,i) + "'" + s.substring(i);
        i++;
        }
      i++;
    }
    return s;
      
  }
}


