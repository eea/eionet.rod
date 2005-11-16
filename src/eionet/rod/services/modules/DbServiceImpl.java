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


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import com.tee.util.Util;
import com.tee.xmlserver.DBPoolIF;

import eionet.rod.services.DbServiceIF;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.LogServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
//import eionet.directory.DirectoryService;

/**
 * CountrySrv database methods implementation.
 *
 * @author  Kaido Laine
 * @version 1.0
 */
public class DbServiceImpl implements DbServiceIF, eionet.rod.Constants {

  //private static String ANSI_DATE_FORMAT = "yyyy-MM-dd";
	private static String ANSI_DATETIME_FORMAT = "yyyy-MM-dd hh:MM";

  public static final int OTHER = 0;
  public static final int ORACLE = 1;
  public static final int MYSQL = 2;

  private int _driverType = MYSQL;

  // parameters to create DB connection
  //private String crUrlPref;
  private String dbUrl;
  private String dbDriver;
  private String dbUser;
  private String dbPsw;

  private static String rodDomain;

  private static String roNs;

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

   public String[][] getDeadlines() throws ServiceException {
      String sql = "SELECT PK_RA_ID, FIRST_REPORTING, REPORT_FREQ_MONTHS, VALID_TO, TERMINATE " +
                   "FROM T_OBLIGATION WHERE FIRST_REPORTING > 0 AND VALID_TO > 0";
      return _executeStringQuery(sql);
   }

   public void saveDeadline(String raId, String next, String next2) throws ServiceException {
      String sql;
      if(next2.length() > 0)
         sql = "UPDATE T_OBLIGATION SET NEXT_DEADLINE='" + next + 
               "', NEXT_DEADLINE2='" + next2 + "' WHERE PK_RA_ID=" + raId;
      else
         sql = "UPDATE T_OBLIGATION SET NEXT_DEADLINE='" + next + 
               "', NEXT_DEADLINE2=NULL WHERE PK_RA_ID=" + raId;
      _executeUpdate(sql);
   }

   public void saveTerminate(String raId, String terminated) throws ServiceException {
      String sql = "UPDATE T_OBLIGATION SET TERMINATE='" + terminated + 
                   "' WHERE PK_RA_ID=" + raId;
      _executeUpdate(sql);
   }


/*  public int getActivitiesCountInIssue( String issueId, String raId) throws ServiceException {
      
      String sqlq = "SELECT COUNT(RA_ID) FROM T_ISSUE_LNK WHERE FK_ISSUE_ID=" + issueId + " AND RA_ID=" + raId ;
      String[][] res = _executeStringQuery(sqlq);
      int cnt = Integer.parseInt(res[0][0]);

      return cnt;

  }
  */



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
      //!!! FIX ME in the future!!!
      //if there is a servlet contecxt, take the DbPool from the XDBApplication
      dbUrl = RODServices.getFileService().getStringProperty( FileServiceIF.DB_URL);
      dbDriver = RODServices.getFileService().getStringProperty( FileServiceIF.DB_DRV);
      dbUser = RODServices.getFileService().getStringProperty( FileServiceIF.DB_USER_ID);
      dbPsw = RODServices.getFileService().getStringProperty( FileServiceIF.DB_USER_PWD);

      rodDomain  = RODServices.getFileService().getStringProperty( ROD_URL_DOMAIN );
      roNs= RODServices.getFileService().getStringProperty( FileServiceIF.RO_NAMESPACE);    
      

      logger = RODServices.getLogService();
      
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
	if (logger.enable(logger.DEBUG))
	  	logger.debug(s);
  }

  /*public String[][] getActivityIds() throws ServiceException {
    return _executeStringQuery("SELECT DISTINCT RA_ID FROM T_ACTIVITY_DETAILS");
  } */

  /**
  * Used by CS extractor 
  */
  public String[][] getRaData() throws ServiceException {

    String sql = "SELECT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') as TITLE " +
      " FROM T_OBLIGATION a ORDER BY a.PK_RA_ID";

     return _executeStringQuery(sql);
  }  

  public String[] getRespRoles() throws ServiceException {
    Set roles = new HashSet();
    
    //country roles with suf
    String sql = "SELECT DISTINCT CONCAT(a.RESPONSIBLE_ROLE, '-' , LCASE(s.SPATIAL_TWOLETTER)) AS ohoo " + 
      " FROM T_OBLIGATION a, T_SPATIAL s,  T_RASPATIAL_LNK sl  " + 
      " WHERE  a.RESPONSIBLE_ROLE_SUF=1 AND sl.FK_RA_ID=a.PK_RA_ID " +
      " AND sl.FK_SPATIAL_ID = s.PK_SPATIAL_ID AND a.RESPONSIBLE_ROLE IS NOT NULL " +
      " AND a.RESPONSIBLE_ROLE <> '' AND s.SPATIAL_TYPE = 'C' AND s.SPATIAL_TWOLETTER IS NOT NULL AND " +
       " TRIM(s.SPATIAL_TWOLETTER) <> '' " ;
    
      String r[][] = _executeStringQuery(sql);
      addStringsToSet(roles,r);
      sql = "SELECT DISTINCT RESPONSIBLE_ROLE FROM T_OBLIGATION WHERE RESPONSIBLE_ROLE IS NOT NULL AND RESPONSIBLE_ROLE <> '' " ;
      r = _executeStringQuery(sql);
      addStringsToSet(roles,r);      

    sql = "SELECT DISTINCT CONCAT(a.COORDINATOR_ROLE, '-' , LCASE(s.SPATIAL_TWOLETTER)) " + 
      " FROM T_OBLIGATION a, T_SPATIAL s,  T_RASPATIAL_LNK sl  " + 
      " WHERE  a.COORDINATOR_ROLE_SUF=1 AND sl.FK_RA_ID=a.PK_RA_ID " +
      " AND sl.FK_SPATIAL_ID = s.PK_SPATIAL_ID AND a.COORDINATOR_ROLE IS NOT NULL " +
      " AND a.COORDINATOR_ROLE <> '' AND s.SPATIAL_TYPE = 'C' AND s.SPATIAL_TWOLETTER IS NOT NULL AND " +
       " TRIM(s.SPATIAL_TWOLETTER) <> '' " ;
      r = _executeStringQuery(sql);
      addStringsToSet(roles,r);      

      sql = "SELECT DISTINCT COORDINATOR_ROLE FROM T_OBLIGATION WHERE COORDINATOR_ROLE IS NOT NULL AND COORDINATOR_ROLE <> '' " ;
      r = _executeStringQuery(sql);
      addStringsToSet(roles,r);      

      
      return (String[]) roles.toArray(new String[0]);
  }

  private void addStringsToSet(Set set, String[][] str) {
    for (int i=0; i<str.length;i++)
      set.add(str[i][0]);
  }

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

  public void savePerson(String roleId, String fullName, String orgName) throws ServiceException {
    String sql="UPDATE T_ROLE SET PERSON=' " + strLit(fullName) + "', INSTITUTE ='" + strLit(orgName) + "' " +
      " WHERE ROLE_ID='" + roleId + "'";
    _executeUpdate(sql);
    
  }

  public String[][] getRoleIds() throws ServiceException {  
    return _executeStringQuery("SELECT ROLE_ID, PERSON FROM T_ROLE");
  }
  
  public void saveRole(Hashtable role, String person, String org) throws ServiceException {
    //backup ->
    String sql;
    String roleId = (String)role.get("ID");        


    try {

      //DELETE OLD DATA
      sql = "DELETE FROM T_ROLE WHERE ROLE_ID='" + roleId + "' AND STATUS=1";
      _executeUpdate(sql);
      
      String circaUrl = (String)role.get("URL");
      String circaMembersUrl = (String)role.get("URL_MEMBERS");
      String desc = (String)role.get("DESCRIPTION");
      String mail = (String)role.get("MAIL");

      //Vector occupants = (Vector)role.get("OCCUPANTS");

      //String person = "";

      /*if (occupants != null && occupants.size() > 0 ) 
        person = (String)occupants.elementAt(0);
      */
      if (roleId != null)    {

        
        sql = "INSERT INTO T_ROLE SET STATUS=1, LAST_HARVESTED=NOW(), ROLE_NAME='" + strLit(desc) +
                   "', ROLE_EMAIL='" + mail + "', ROLE_ID='" + roleId + "',  " + 
                  " ROLE_URL ='" + circaUrl + "'," +
                  " ROLE_MEMBERS_URL ='" + circaMembersUrl + "', " +
                  " PERSON = '" + person + "'," +
                  " INSTITUTE = '" + strLit(org) + "'";

       _executeUpdate(sql);
     
      }
    } catch (Exception e ) {
      //recover
      sql = "DELETE FROM T_ROLE WHERE STATUS=1 AND ROLE_ID ='" + roleId + "'";    
      _executeUpdate(sql);
        
      sql = "UPDATE T_ROLE SET STATUS=1 WHERE STATUS=0 AND ROLE_ID ='" + roleId + "'";    
      _executeUpdate(sql);

    }
  }

  /**
  * Save all deliveries for this RA
  */
  public void saveDeliveries(String raId, Vector deliveries, HashMap cMap ) throws ServiceException {
    
    Vector cIds = new Vector();
    String countryIds="";

    FileServiceIF fS = new FileServiceImpl();

    String titlePred = fS.getStringProperty( fS.CONTREG_TITLE_PREDICATE );
    String typePred = fS.getStringProperty( fS.CONTREG_TYPE_PREDICATE );    
    String datePred = fS.getStringProperty( fS.CONTREG_DATE_PREDICATE );    
    String identifierPred = fS.getStringProperty( fS.CONTREG_IDENTIFIER_PREDICATE );    
    String formatPred = fS.getStringProperty( fS.CONTREG_FORMAT_PREDICATE );
    String coveragePred = fS.getStringProperty( fS.CONTREG_COVERAGE_PREDICATE );        
    String countryPred = fS.getStringProperty( fS.COUNTRY_NAMESPACE );


    for (int ii=0; ii<deliveries.size(); ii++){
      //deliveryKEY:STRING(url), VALUE:HASH (metadata)
       Hashtable delivery = (Hashtable)deliveries.elementAt(ii);
      if(delivery != null && delivery.size() > 0)
        try {    
          //Hash contains only one member that's delivery data
          String crId = (String)(delivery.keys().nextElement());
          Hashtable metaData = (Hashtable)delivery.get( crId );

        //Each hash contains Strings as Keys and Vectors as values
        String title = cnvVector( (Vector)metaData.get( titlePred ), "," );
        String date = cnvVector( (Vector)metaData.get(datePred),",");
        String identifier = cnvVector( (Vector)metaData.get( identifierPred ),",");
        String type =  cnvVector( (Vector)metaData.get( typePred ),",");
        String format = cnvVector( (Vector)metaData.get(formatPred ),",");
        String coverage = cnvVector( (Vector)metaData.get(coveragePred )," ");
        String country = cnvVector((Vector)metaData.get(countryPred )," ");
        String countryId=getCountryId(country, cMap);

        String sql = "INSERT INTO T_DELIVERY SET STATUS=1, " +
                      " TITLE='" + 
                     ((title == null)? "" : strLit(title)) + "', UPLOAD_DATE='" +
                     ((date == null)? "" : date) + "', DELIVERY_URL='" +
                     strLit(identifier) + "', TYPE='" +
                     ((type == null)? "" : strLit(type)) + "', FORMAT='" +
                     ((format == null)? "" : strLit(format)) + "', COVERAGE='" +
                     ((coverage == null)? "" : strLit(coverage)) + "'," +
                     " FK_SPATIAL_ID = " + countryId + ", " +
                     " FK_RA_ID = " + raId + " ;" ;

        if (countryId!=null)
          _executeUpdate(sql);
        else
          _log("!!! Delivery not saved, Country is not in T_SPATIAL " + country);
          
        if ( countryId != null && !cIds.contains(countryId))
          cIds.add(countryId);

      } catch (Exception e ) {
        rollBackDeliveries(raId);
        throw new ServiceException ("Harvesting deliveries for RA: " + raId + " failed with reason " + e.toString());
      }

    }

     if (cIds.size() > 0)
       countryIds="," + cnvVector(cIds,",") + "," ;
     else
      countryIds="";
        
     markCountries(raId, countryIds);      
   }  

  private String cnvVector( Vector v, String separator ) {

    //quick fix
    if (v == null)
      return "";
      
    StringBuffer s = new StringBuffer();
    for (int i =0; i< v.size(); i++) {
      if (v.elementAt(i) != null) {
        s.append((String)v.elementAt(i));
        if (i < v.size() -1 )
          s.append (separator);
      }
    }    

    return s.toString();
  }

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
*
*/

  public void logHistory(String itemType, String itemId, String userName, String actionType, String description) throws ServiceException {
    String time = currentDate();
    
    String sql = "INSERT INTO T_HISTORY ( ITEM_ID,  ITEM_TYPE, 	ACTION_TYPE, 	LOG_TIME, USER, DESCRIPTION ) " +
      " VALUES (" + itemId + ", '" + itemType + "', '" + actionType + "', '" + time + "', '" + userName + "', '" +
      (description == null ? "" : description) +   "')";     


    _executeUpdate(sql);
  }

  private String rplAmp(String fld, String alias ){
    return "REPLACE(" + fld +", '&', '&#038;') AS " + alias;
  }
  
  public  Vector getUpcomingDeadlines(double days) throws ServiceException {
      
      String sql = " SELECT o.TITLE AS title, o.FK_DELIVERY_COUNTRY_IDS AS country_ids, " + 
      " c.CLIENT_NAME AS client, o.NEXT_DEADLINE AS next_deadline, o.NEXT_DEADLINE2 AS next_deadline2, o.RESPONSIBLE_ROLE AS responsible_role FROM T_OBLIGATION o, T_CLIENT c WHERE " +
      " CURDATE() < o.NEXT_DEADLINE AND (CURDATE() + INTERVAL (o.REPORT_FREQ_MONTHS * " + days + ") DAY) > o.NEXT_DEADLINE " + 
      " AND c.PK_CLIENT_ID = o.FK_CLIENT_ID ";

      return  _getVectorOfHashes(sql);        
  }

  public  Vector getInstruments() throws ServiceException {
    String sql = "SELECT s.PK_SOURCE_ID," +  rplAmp("s.TITLE", "TITLE") +"," +
      rplAmp("s.ALIAS", "ALIAS") +"," +
      rplAmp("s.URL", "URL") + "," + rplAmp("s.ABSTRACT", "ABSTRACT") + ", " +
      rplAmp("c.CLIENT_NAME", "ISSUED_BY") + "," +
      rplAmp("s.LEGAL_NAME", "LEGAL_NAME") + "," +
      rplAmp("s.CELEX_REF", "CELEX_REF") + "," +
      " CONCAT('" + rodDomain + "/show.jsv?id=', PK_SOURCE_ID, '&#038;mode=S') AS details_url, " +
      "DATE_FORMAT(s.LAST_UPDATE, '%Y-%m-%d') AS LAST_UPDATE " +
      " FROM T_SOURCE s LEFT OUTER JOIN T_CLIENT c ON s.FK_CLIENT_ID=c.PK_CLIENT_ID ORDER BY s.ALIAS ";

    return  _getVectorOfHashes(sql);        
  }


  //XML-RPC
  public  Vector getActivities() throws ServiceException {
      String sql = "SELECT a.PK_RA_ID, s.PK_SOURCE_ID,  a.TITLE, " +
        " IF( s.ALIAS IS NULL OR TRIM(s.ALIAS) = '', s.TITLE, s.ALIAS) AS SOURCE_TITLE, a.LAST_UPDATE, " +
        " CONCAT('" + rodDomain + "/show.jsv?id=', PK_RA_ID, '&aid=', FK_SOURCE_ID, '&mode=A') AS details_url, " +
        " CONCAT('" + roNs + "', '/',  a.PK_RA_ID) AS uri, " +
        " IF (TERMINATE='Y', 1, 0) AS 'terminated'" +
        " FROM T_OBLIGATION a , T_SOURCE s " +
        " WHERE a.FK_SOURCE_ID = s.PK_SOURCE_ID " ;

        //if (!all)        
         //sql += " AND a.TERMINATE = 'N' ";

        sql += " ORDER BY a.PK_RA_ID";
        
      return  _getVectorOfHashes(sql);

  }
 /**
	* Returns all Reporting activities pk_id + title
  * used in RDF generating
  * URL parameters hard-coded
	*/
  public  Vector getObligations() throws ServiceException {
      
      String sql = "SELECT a.PK_RA_ID, s.PK_SOURCE_ID, REPLACE(a.TITLE, '&', '&#038;') as TITLE, " +
        " IF( s.ALIAS IS NULL OR TRIM(s.ALIAS) = '', s.TITLE, s.ALIAS) AS SOURCE_TITLE, a.LAST_UPDATE, " +
        " CONCAT('" + rodDomain + "/show.jsv?id=', PK_RA_ID, '&#038;aid=', FK_SOURCE_ID, '&#038;mode=A') AS details_url, " +
        " CONCAT('" + roNs + "', '/',  a.PK_RA_ID) AS uri, " +
        " IF (TERMINATE='Y', 1, 0) AS 'terminated', a.VALID_SINCE, a.EEA_PRIMARY, " + 
          rplAmp("a.RESPONSIBLE_ROLE", "RESPONSIBLE_ROLE") + ", " +
          rplAmp("a.DESCRIPTION", "DESCRIPTION") + ", " +
          " a.NEXT_DEADLINE, " +
        " a.NEXT_DEADLINE2, "  + 
          rplAmp("a.COMMENT", "COMMENT") + ", " +
          rplAmp("a.REPORTING_FORMAT", "REPORTING_FORMAT") + ", " +
          rplAmp("a.FORMAT_NAME", "FORMAT_NAME") + ", " +
          rplAmp("a.REPORT_FORMAT_URL", "REPORT_FORMAT_URL") + 
        " FROM T_OBLIGATION a , T_SOURCE s " +
        " WHERE a.FK_SOURCE_ID = s.PK_SOURCE_ID " ;

        //if (!all)        
         //sql += " AND a.TERMINATE = 'N' ";

        sql += " ORDER BY SOURCE_TITLE, TITLE;";
        
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

  /**
   *	Returns the current date in ANSI-DATETIME FORMAT
   * @author KL 011105
   */
  private String currentDate() throws ServiceException {
    String dateFormat="yyyy-MM-dd HH:mm:ss";
    java.util.Date dt = new java.util.Date();
    Date dt2 = new Date(dt.getTime());
    String curDate="" ;//= dt2.toString(); //objToStr(dt2, ANSI_DATETIME_FORMAT);
    
    try {
      SimpleDateFormat   sdf = new SimpleDateFormat(dateFormat);
      curDate = sdf.format(dt2);
    } catch (Exception e) {
      throw new ServiceException("Getting today's date failed");
    }
    return curDate;
  }

  /**
  *
  */
  public String[][] getItemHistory(String itemType, String itemId) throws ServiceException {
    String sql = " SELECT LOG_TIME, ACTION_TYPE, USER, DESCRIPTION FROM T_HISTORY WHERE ITEM_TYPE = '" + itemType +
      "' AND ITEM_ID = " + itemId + " ORDER BY LOG_TIME ";
    String s[][] = _executeStringQuery(sql);

    return s;
  }

  /**
  *
  */
  public String[][] getDeletedItems(String itemType) throws ServiceException {
    String sql = "SELECT ITEM_ID, LOG_TIME, USER FROM T_HISTORY WHERE " + 
      " ACTION_TYPE = '" + DELETE_ACTION_TYPE + "' AND ITEM_TYPE = '" + itemType + "'";

    return _executeStringQuery(sql);
  }

  public String[][] getActivityDeadlines(StringTokenizer issues, StringTokenizer countries) throws ServiceException  {
    
      String sql = "";
      
      if ( issues != null && countries != null) {
          sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.FK_SOURCE_ID," +
          rplAmp("a.DESCRIPTION", "DESCRIPTION") + 
        " FROM T_OBLIGATION a, T_RAISSUE_LNK il, T_RASPATIAL_LNK r WHERE " +
          "  a.PK_RA_ID = il.FK_RA_ID AND a.PK_RA_ID = r.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " +
        "a.NEXT_DEADLINE > '0000-00-00'";

         sql = sql + "AND " +  getWhereClause("il.FK_ISSUE_ID", issues );
         sql = sql + "AND " +  getWhereClause("r.FK_SPATIAL_ID", countries );
      } else if (issues!= null && countries == null) {
       sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.FK_SOURCE_ID, " +
         rplAmp("a.DESCRIPTION", "DESCRIPTION") + 
       " FROM T_OBLIGATION a, T_RAISSUE_LNK il WHERE " +
         "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " +
        "a.NEXT_DEADLINE > '0000-00-00'";

        sql = sql + "AND " +  getWhereClause("il.FK_ISSUE_ID", issues );
       } else if (issues == null && countries != null) {
           sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.FK_SOURCE_ID, " +
           rplAmp("a.DESCRIPTION", "DESCRIPTION") + 
         " FROM T_OBLIGATION a, T_RASPATIAL_LNK il WHERE " +
           "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " +
        "a.NEXT_DEADLINE > '0000-00-00'";

          sql = sql + "AND " +  getWhereClause("il.FK_SPATIAL_ID", countries );
       }else {
           sql = "SELECT PK_RA_ID, REPLACE(TITLE, '&', '&#038;') AS TITLE , NEXT_DEADLINE, FK_SOURCE_ID, " +
           rplAmp("DESCRIPTION", "DESRCIPTION")  +
             " FROM T_OBLIGATION WHERE NEXT_DEADLINE IS NOT NULL AND " +
             "NEXT_DEADLINE > '0000-00-00'";
       }  

    return _executeStringQuery( sql);
  }

  /**
  *
  */
  //DEPRECATED
/*
  public String[][] getIssueObligations(StringTokenizer ids) throws ServiceException {
    String sql = "";

    if (ids!= null) {
      sql = "SELECT DISTINCT r.PK_RO_ID, REPLACE(r.ALIAS, '&', '&#038;') as ALIAS, REPLACE(s.TITLE, '&', '&#038;') AS TITLE, r.FK_SOURCE_ID FROM " +
        " T_REPORTING r, T_ACTIVITY a, T_SOURCE s, T_RAISSUE_LNK il WHERE " +
        " r.FK_SOURCE_ID = s.PK_SOURCE_ID AND r.PK_RO_ID = a.FK_RO_ID AND a.PK_RA_ID = il.FK_RA_ID ";

      sql = sql + "AND " +  getWhereClause("il.FK_ISSUE_ID", ids );
    }  
    else
      sql = "SELECT r.PK_RO_ID, REPLACE(r.ALIAS, '&', '&#038;') AS  ALIAS, REPLACE(s.TITLE, '&', '&#038;') AS TITLE, r.FK_SOURCE_ID FROM T_REPORTING r, T_SOURCE s " +
        " WHERE r.FK_SOURCE_ID = s.PK_SOURCE_ID";


    sql += " ORDER BY PK_RO_ID ";
    
    return _executeStringQuery(sql);
  } */

 public String[][] getIssueActivities(StringTokenizer issues, StringTokenizer countries) throws ServiceException  {
   String sql = "";
   
   if ( issues != null && countries != null) {
       sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.FK_SOURCE_ID," +
       rplAmp("a.DESCRIPTION", "DESCRIPTION") + 
     " FROM T_OBLIGATION a, T_RAISSUE_LNK il, T_RASPATIAL_LNK r WHERE " +
       "  a.PK_RA_ID = il.FK_RA_ID AND a.PK_RA_ID = r.FK_RA_ID ";

      sql = sql + "AND " +  getWhereClause("il.FK_ISSUE_ID", issues );
      sql = sql + "AND " +  getWhereClause("r.FK_SPATIAL_ID", countries );
   } else if (issues!= null && countries == null) {
    sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.FK_SOURCE_ID, " +
      rplAmp("a.DESCRIPTION", "DESCRIPTION") + 
    " FROM T_OBLIGATION a, T_RAISSUE_LNK il WHERE " +
      "  a.PK_RA_ID = il.FK_RA_ID ";

     sql = sql + "AND " +  getWhereClause("il.FK_ISSUE_ID", issues );
    } else if (issues == null && countries != null) {
        sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.FK_SOURCE_ID, " +
        rplAmp("a.DESCRIPTION", "DESCRIPTION") + 
      " FROM T_OBLIGATION a, T_RASPATIAL_LNK il WHERE " +
        "  a.PK_RA_ID = il.FK_RA_ID ";

       sql = sql + "AND " +  getWhereClause("il.FK_SPATIAL_ID", countries );
    }else {
      sql = "SELECT PK_RA_ID, REPLACE(TITLE, '&', '&#038;') AS TITLE, NEXT_DEADLINE, " +
      " FK_SOURCE_ID, " + rplAmp("DESCRIPTION", "DESCRIPTION") + " FROM T_OBLIGATION ";
    }

    sql += " ORDER BY PK_RA_ID";

    return _executeStringQuery(sql);
 
 }
  
 /**
 * builds an addition to where clause, OR condition of field IDs, given in the param
 * 
 * example ids=[1,4,5], fldName=ITEM_ID
 * return ( ITEM_ID=1 OR ITEM_ID=4 OR ITEM_ID=5 )
 */
 private String getWhereClause(String fldName, StringTokenizer ids ) {
    StringBuffer s = new StringBuffer();
    s.append(" (");
    while (ids.hasMoreTokens()) {
      s.append(fldName).append("=").append(ids.nextToken()).append(" OR ");
    }
    s.delete( s.length() -4, s.length() )          ;
    s.append(" ) ");
    return s.toString();
 }

    public String[][] getCountryIdPairs() throws ServiceException {
        String sql = "SELECT PK_SPATIAL_ID, SPATIAL_NAME FROM T_SPATIAL WHERE SPATIAL_TYPE ='C' " +
          " ORDER BY SPATIAL_NAME";
      String [][] s = _executeStringQuery(sql);

      return s;
    }


  /***
  * used in RDF  and XML/RPC
  */
   public Vector getCountries() throws ServiceException {
      String spatialNs= RODServices.getFileService().getStringProperty( FileServiceIF.SPATIAL_NAMESPACE);    
      String sql = "SELECT CONCAT('" + spatialNs +"', PK_SPATIAL_ID) AS uri, SPATIAL_NAME AS name, UPPER(SPATIAL_TWOLETTER) AS iso " +
        " FROM T_SPATIAL WHERE SPATIAL_TYPE='C' ORDER BY SPATIAL_NAME ";
      
      return _getVectorOfHashes(sql);
   }

    private String getCountryId(String countryName, HashMap countryMap) throws ServiceException {

      if (!countryMap.containsKey(countryName)) {
        String sql = "SELECT PK_SPATIAL_ID FROM T_SPATIAL WHERE " +
            " SPATIAL_TYPE='C' AND SPATIAL_NAME='" + countryName + "'";
        
        String [][] s = _executeStringQuery(sql);

        if (s.length>0)
          countryMap.put(countryName, s[0][0]);
        else 
          return null;
        //return s[0][0];
      }

      return (String)(countryMap.get(countryName));
      
    }
    
    public String getCountryById(String id) throws ServiceException {
        String sql = "SELECT SPATIAL_NAME AS name " +
          " FROM T_SPATIAL WHERE PK_SPATIAL_ID = '" + id + "'";
        
        String [][] s = _executeStringQuery(sql);
        String result = null;
        
        if (s.length>0)
            result = s[0][0];
        
        return result;
     }


  public void commitDeliveries () throws ServiceException {
       String sql = "DELETE FROM T_DELIVERY WHERE STATUS=0";
      _executeUpdate(sql);
  }

  /**
  * Harvesting for this RA OK, delete backed-up deliveries
  * Update harvesting time in T_ACTIVITY TABLE
  */
  private void markCountries (String raId, String cIds ) throws ServiceException {

      String sql = "UPDATE T_OBLIGATION SET LAST_HARVESTED=NOW(), FK_DELIVERY_COUNTRY_IDS = '" +
        cIds + "' WHERE PK_RA_ID=" + raId;
      _executeUpdate(sql);
  

  }

  /**
  * Backup deliveries for this RA
  */
  public void backUpDeliveries () throws ServiceException {
      //String sql = "UPDATE T_DELIVERY SET STATUS=0 WHERE FK_RA_ID=" + raId;
      String sql = "UPDATE T_DELIVERY SET STATUS=0";
      _executeUpdate(sql);
  }

  public void commitRoles () throws ServiceException {
       String sql = "DELETE FROM T_ROLE WHERE STATUS=0";
      _executeUpdate(sql);
  }

  public void backUpRoles () throws ServiceException {
      //String sql = "UPDATE T_DELIVERY SET STATUS=0 WHERE FK_RA_ID=" + raId;
      String sql = "UPDATE T_ROLE SET STATUS=0";
      _executeUpdate(sql);
      
  }


  /**
  * Harvesting crashed, recover old deliveries
  */
  public void rollBackDeliveries (String raId ) throws ServiceException {
      String sql = "DELETE FROM T_DELIVERY WHERE STATUS=1 AND FK_RA_ID=" + raId;
      _executeUpdate(sql);

      sql = "UPDATE T_DELIVERY SET STATUS=1 WHERE STATUS=0 AND FK_RA_ID=" + raId;
      _executeUpdate(sql);
  }

  public void logSpatialHistory(String raId, String spatialId, String voluntary)   throws ServiceException {
      String sql="SELECT * FROM T_SPATIAL_HISTORY WHERE VOLUNTARY = '"+ voluntary +"' AND END_DATE=NOW() AND FK_RA_ID = " + raId + " AND " +
        " FK_SPATIAL_ID= " + spatialId;

      String s[][]=_executeStringQuery(sql);

      if (s.length==0) {
        sql="INSERT INTO T_SPATIAL_HISTORY (FK_RA_ID, FK_SPATIAL_ID, VOLUNTARY, START_DATE) " +
          " VALUES(" + raId + ", " + spatialId + ", '" + voluntary + "', NOW())";

      }
      else
        sql="UPDATE T_SPATIAL_HISTORY SET END_DATE=NULL WHERE END_DATE=NOW() " +
          "AND VOLUNTARY='" + voluntary + "' AND FK_RA_ID=" + raId + " AND " +
          " FK_SPATIAL_ID="  + spatialId;

      _executeUpdate(sql);

      //clear rubbish
      sql="DELETE FROM T_SPATIAL_HISTORY WHERE START_DATE=END_DATE AND END_DATE=NOW()";
      _executeUpdate(sql);
  }  
  public void harvestParams(String raId) throws ServiceException {
    String sql="SELECT p.PARAMETER_NAME, u.UNIT_NAME FROM " + 
      "T_PARAMETER p, T_PARAMETER_LNK pl LEFT OUTER JOIN T_UNIT u " +
      " ON pl.FK_UNIT_ID=u.PK_UNIT_ID WHERE pl.FK_PARAMETER_ID=p.PK_PARAMETER_ID " +
      " AND pl.FK_RA_ID="+ raId + " ORDER BY PARAMETER_NAME";

    String p[][] = _executeStringQuery(sql)      ;

    String prmName="";
    String uName=""; //unit name
    StringBuffer s = new StringBuffer();
    
    for (int i=0; i< p.length; i++) {
      prmName=p[i][0];
      uName=p[i][1];
      s.append(prmName);  

      if (!Util.nullString(uName))
        s.append("(").append(uName).append(")");

      s.append("\n");
    }

    if (s.length() > 0) {
      sql="UPDATE T_OBLIGATION SET PARAMETERS='" + strLit(s.toString()) + "' WHERE (PARAMETERS IS NULL OR PARAMETERS='') " 
        + " AND PK_RA_ID=" + raId;
      
      _executeUpdate(sql);
    }

  }

  public String[][] getCountries(String raId) throws ServiceException {
    String sql="SELECT sl.FK_SPATIAL_ID FROM T_RASPATIAL_LNK sl, T_SPATIAL s " +
      "WHERE s.SPATIAL_TYPE='C' AND sl.FK_SPATIAL_ID=s.PK_SPATIAL_ID AND sl.FK_RA_ID=" + raId + 
      " ORDER BY FK_SPATIAL_ID";
      
    return _executeStringQuery(sql);
    
  }
  
  /*
   *  (non-Javadoc)
   * @see eionet.rod.services.DbServiceIF#getIssues()
   */
  public Vector getIssues() throws ServiceException {
  	
	String issueNs= RODServices.getFileService().
	getStringProperty( FileServiceIF.ISSUE_NAMESPACE);
	
	  String sql = "SELECT ISSUE_NAME AS name, PK_ISSUE_ID AS id, " +
	    "CONCAT('" + issueNs +"', PK_ISSUE_ID) AS uri" +
		" FROM T_ISSUE ORDER BY ISSUE_NAME ";
      
	  return _getVectorOfHashes(sql);
  }

  /*
   * 
   */
  public Vector getOrganisations() throws ServiceException {

	String orgNs= RODServices.getFileService().
	getStringProperty( FileServiceIF.ORGANISATION_NAMESPACE);

		String sql =
	"SELECT CONCAT(CLIENT_ACRONYM,'-',CLIENT_NAME) AS name, PK_CLIENT_ID AS id, " +
	"CONCAT('" + orgNs +"', PK_CLIENT_ID) AS uri" +
		  " FROM T_CLIENT ORDER BY CLIENT_NAME ";
      
		return _getVectorOfHashes(sql);
  }
  
  /*
   *  (non-Javadoc)
   * @see eionet.rod.services.DbServiceIF#getObligationById(java.lang.String)
   */
  public Vector getObligationById(String id) throws ServiceException {
            
		String sql = "SELECT REPLACE(o.TITLE, '&', '&#038;') as title, c.CLIENT_NAME AS client, o.PK_RA_ID AS obligationID, c.PK_CLIENT_ID AS clientID" +
		  " FROM T_OBLIGATION o, T_CLIENT c WHERE c.PK_CLIENT_ID = o.FK_CLIENT_ID AND o.PK_RA_ID =" + id;
      
		return _getVectorOfHashes(sql);
  }
  
  public Vector getObligationCountries(String id) throws ServiceException {
      
    String sql = "SELECT s.SPATIAL_NAME AS name FROM T_SPATIAL s, T_RASPATIAL_LNK r WHERE " + 
    " r.FK_RA_ID = '" + id + "' AND s.PK_SPATIAL_ID = r.FK_SPATIAL_ID ORDER BY s.SPATIAL_NAME";

      
    return _getVectorOfHashes(sql);
  }
  
  public Vector getObligationOrg(String id) throws ServiceException {
      
    String sql = "SELECT c.CLIENT_NAME AS name FROM T_CLIENT c, T_OBLIGATION o WHERE " + 
    " o.PK_RA_ID = '" + id + "' AND c.PK_CLIENT_ID = o.FK_CLIENT_ID ORDER BY c.CLIENT_NAME";

      
    return _getVectorOfHashes(sql);
  }
  
  public Vector getObligationIssues(String id) throws ServiceException {
      
    String sql = "SELECT i.ISSUE_NAME AS name FROM T_ISSUE i, T_RAISSUE_LNK r WHERE " + 
    " r.FK_RA_ID = '" + id + "' AND i.PK_ISSUE_ID = r.FK_ISSUE_ID ORDER BY i.ISSUE_NAME";

      
    return _getVectorOfHashes(sql);
  }
  
  public String[][] getParentObligationId(String id) throws ServiceException {
      
    String sql = "SELECT PARENT_OBLIGATION FROM T_OBLIGATION WHERE " + 
    " PK_RA_ID = '" + id + "'";

    return _executeStringQuery(sql);
  }
  
  public String[][] getLatestVersionId(String id) throws ServiceException {
      
    String sql = "select PK_RA_ID from T_OBLIGATION where " + 
    " (PARENT_OBLIGATION='" + id + "' OR PK_RA_ID='" + id + "') AND HAS_NEWER_VERSION = '-1'";

    return _executeStringQuery(sql);
  }
  
  public Vector getPreviousVersions(String id) throws ServiceException {
      
    String sql = "select PK_RA_ID AS id, TITLE AS title, VERSION AS version, FK_SOURCE_ID AS source, PARENT_OBLIGATION AS parentid from T_OBLIGATION where " + 
    " PARENT_OBLIGATION='" + id + "' OR PK_RA_ID = '" + id + "' ORDER BY VERSION";

    return _getVectorOfHashes(sql);
  }
  
  public int getRestoreObligation(String id, String pid, int latestVersion) throws ServiceException {

    int newVer = latestVersion + 1;  
    String sql1 = "UPDATE T_OBLIGATION SET VERSION='" + newVer + "', HAS_NEWER_VERSION='-1' WHERE PK_RA_ID='" + id + "'";
    String sql2 = "UPDATE T_OBLIGATION SET HAS_NEWER_VERSION='1' WHERE (PK_RA_ID = '" + pid + "' OR PARENT_OBLIGATION= '" + pid + "') AND VERSION='" + latestVersion + "'";
    int u = 0;
    if ((_executeUpdate(sql1) == 1) && (_executeUpdate(sql2) == 1)){
        u = 1;
    }
    return (u);

  }
  
  public String[][] getIssues(String raId) throws ServiceException {
    return _executeStringQuery("SELECT FK_ISSUE_ID FROM T_OBLIGATION o, T_RAISSUE_LNK il " +
      " WHERE il.FK_RA_ID=o.PK_RA_ID AND il.FK_RA_ID=" + raId + " ORDER BY FK_ISSUE_ID" );
  }

  public String[][] getObligationIds() throws ServiceException {
    return _executeStringQuery("SELECT PK_RA_ID FROM T_OBLIGATION ORDER BY PK_RA_ID");
    
  } 

  public String[][] getIssueIdPairs() throws ServiceException {
    return _executeStringQuery("SELECT PK_ISSUE_ID, " + rplAmp("ISSUE_NAME", "ISSUE_NAME") + " FROM T_ISSUE ORDER BY PK_ISSUE_ID");
  }  

  public Vector getROComplete() throws ServiceException {
    return _getVectorOfHashes("SELECT * FROM T_OBLIGATION");
  }
  public Vector getROSummary() throws ServiceException {
    return _getVectorOfHashes("SELECT TITLE, LAST_UPDATE, DESCRIPTION, " +
     "CONCAT('" + rodDomain + "/show.jsv?id=', PK_RA_ID,'&aid=', FK_SOURCE_ID, '&mode=A') AS details_url " +
     " FROM T_OBLIGATION ");
  }
  public Vector getRODeadlines() throws ServiceException {
    return _getVectorOfHashes("SELECT o.TITLE, c.CLIENT_NAME, " +
    " IF (o.NEXT_DEADLINE IS NULL, o.NEXT_REPORTING, o.NEXT_DEADLINE) AS NEXT_DEADLINE, "+
    " o.NEXT_DEADLINE2, o.DATE_COMMENTS FROM T_OBLIGATION o " +
    "LEFT OUTER JOIN T_CLIENT c ON o.FK_CLIENT_ID=c.PK_CLIENT_ID");

  }  

   public String[][] getInstrumentsRSS() throws ServiceException {
      String sql = "SELECT PK_SOURCE_ID, " + rplAmp("TITLE", "TITLE") + ", " +
        "CONCAT('" + rodDomain + "/show.jsv?id=', PK_SOURCE_ID, '&amp;mode=S') AS LINK, " +  
        rplAmp("COMMENT", "COMMENT") + " FROM T_SOURCE ORDER BY PK_SOURCE_ID ";

      return _executeStringQuery(sql);
   }
 }
