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
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import com.tee.util.Util;
import com.tee.xmlserver.DBPoolIF;

import eionet.rod.ActivityHandler;
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
   
   public Vector getHistoricDeadlines(String start_date, String end_date) throws ServiceException {
       SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
       String d = formatter.format(new Date());
       if(start_date == null || start_date.equals(""))
           start_date = "00/00/0000";
       if(end_date == null || end_date.equals(""))
           end_date = d;
       String start = changeDateDelimeter(start_date);
       String end = changeDateDelimeter(end_date);
       String sql = "SELECT h.FK_RA_ID AS id, h.DEADLINE AS deadline, o.TITLE AS title, o.FK_SOURCE_ID AS source " +
                    "FROM T_HISTORIC_DEADLINES h, T_OBLIGATION o "+
                    "WHERE h.DEADLINE >= '"+start+"' AND h.DEADLINE <= '"+end+"' AND h.FK_RA_ID = o.PK_RA_ID ORDER BY h.DEADLINE DESC";
       return _getVectorOfHashes(sql);
    }

   public void saveDeadline(String raId, String next, String next2, String current) throws ServiceException {
      String sql;
      if(next2.length() > 0)
         sql = "UPDATE T_OBLIGATION SET NEXT_DEADLINE='" + next +
               "', NEXT_DEADLINE2='" + next2 + "' WHERE PK_RA_ID=" + raId;
      else
         sql = "UPDATE T_OBLIGATION SET NEXT_DEADLINE='" + next +
               "', NEXT_DEADLINE2=NULL WHERE PK_RA_ID=" + raId;
      _executeUpdate(sql);
      
      String insert_stmt = "INSERT IGNORE INTO T_HISTORIC_DEADLINES SET FK_RA_ID = "+raId+", DEADLINE = '"+current+"'";
      _executeUpdate(insert_stmt);
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
                e.printStackTrace();
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
   * returns result as vector of countries
   */
  private  Vector _getVectorOfNames(String sql_stmt, String tab, String col, String vol, String sql, String status) throws ServiceException {

      Vector rvec = new Vector(); // Return value as Vector

      Connection con = null;
      Statement stmt = null;
      ResultSet rset = null;

      _log(sql_stmt);

      con = getConnection();
          try {
              stmt = con.createStatement();
              rset = stmt.executeQuery(sql_stmt);

              ResultSetMetaData md = rset.getMetaData();

              while (rset.next()) {
                  String value = rset.getString(1);
                  if ( value == null)
                      value = "";
                  String sub_trans_nr = null;
                  String val = null;
                  if(vol != null){
                      sub_trans_nr = rset.getString(2);
                      String s = "SELECT value FROM T_UNDO where tab='"+tab+"' " +
                      "AND col = '"+col+"' AND sub_trans_nr = " + sub_trans_nr;
                      String[][] sa = _executeStringQuery(s);
                      if(sa.length > 0)
                          val = sa[0][0];
                      if(val.equals(vol)){
                          String name_sql = sql + value;
                          String[][] na = _executeStringQuery(name_sql);
                          if(na.length > 0){
                              if(status != null){
                                  String s2 = "SELECT value FROM T_UNDO WHERE tab = '"+tab+"' AND col = 'STATUS' AND sub_trans_nr = "+sub_trans_nr;
                                  String[][] sa2 = _executeStringQuery(s2);
                                  if(sa2.length > 0)
                                      if(sa2[0][0].equals(status))
                                          rvec.addElement(na[0][0]);
                              } else {
                                  rvec.addElement(na[0][0]);
                              }
                          }
                      }
                  } else {
                      String name_sql = sql + "'" + value + "'";
                      String[][] na = _executeStringQuery(name_sql);
                      if(na.length > 0)
                          rvec.addElement(na[0][0]);
                  }
              }
          } catch (SQLException e) {
              e.printStackTrace();
              logger.error("Error occurred when processing result set: " + sql_stmt,e);
              throw new ServiceException("Error occurred when processing result set: " + sql_stmt);
          }  catch (NullPointerException nue ) {
              nue.printStackTrace( System.out );
          } finally {
              _close(con, stmt, null);
          }
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

      String sql = " SELECT o.TITLE AS title, o.PK_RA_ID AS id, o.FK_SOURCE_ID AS src_id, o.REPORT_FREQ_MONTHS AS freq, " +
      " c.CLIENT_NAME AS client, o.NEXT_DEADLINE AS next_deadline, o.NEXT_DEADLINE2 AS next_deadline2, o.RESPONSIBLE_ROLE AS responsible_role FROM T_OBLIGATION o, T_CLIENT c WHERE " +
      " CURDATE() < o.NEXT_DEADLINE AND (CURDATE() + INTERVAL (o.REPORT_FREQ_MONTHS * " + days + ") DAY) > o.NEXT_DEADLINE " +
      "AND c.PK_CLIENT_ID = o.FK_CLIENT_ID ";

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

  /**
  *
  */
  public Vector getDeletedItemsVector(String itemType) throws ServiceException {
    String sql = "SELECT ITEM_ID, ACTION_TYPE, ITEM_TYPE, LOG_TIME, USER FROM T_HISTORY WHERE " +
      " ACTION_TYPE = '" + DELETE_ACTION_TYPE + "' AND ITEM_TYPE = '" + itemType + "'";

    return _getVectorOfHashes(sql);
  }

  public String[][] getActivityDeadlines(StringTokenizer issues, StringTokenizer countries) throws ServiceException  {

      String sql = "";

      if ( issues != null && countries != null) {
          sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.REPORT_FREQ_MONTHS, a.FK_SOURCE_ID," +
          rplAmp("a.DESCRIPTION", "DESCRIPTION") +
        " FROM T_OBLIGATION a, T_RAISSUE_LNK il, T_RASPATIAL_LNK r WHERE " +
          "  a.PK_RA_ID = il.FK_RA_ID AND a.PK_RA_ID = r.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " +
        "a.NEXT_DEADLINE > '0000-00-00'";

         sql = sql + " AND " +  getWhereClause("il.FK_ISSUE_ID", issues );
         sql = sql + " AND " +  getWhereClause("r.FK_SPATIAL_ID", countries );
      } else if (issues!= null && countries == null) {
       sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.REPORT_FREQ_MONTHS, a.FK_SOURCE_ID, " +
         rplAmp("a.DESCRIPTION", "DESCRIPTION") +
       " FROM T_OBLIGATION a, T_RAISSUE_LNK il WHERE " +
         "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " +
        "a.NEXT_DEADLINE > '0000-00-00'";

        sql = sql + " AND " +  getWhereClause("il.FK_ISSUE_ID", issues );
       } else if (issues == null && countries != null) {
           sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.REPORT_FREQ_MONTHS, a.FK_SOURCE_ID, " +
           rplAmp("a.DESCRIPTION", "DESCRIPTION") +
         " FROM T_OBLIGATION a, T_RASPATIAL_LNK il WHERE " +
           "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " +
        "a.NEXT_DEADLINE > '0000-00-00'";

          sql = sql + " AND " +  getWhereClause("il.FK_SPATIAL_ID", countries );
       }else {
           sql = "SELECT PK_RA_ID, REPLACE(TITLE, '&', '&#038;') AS TITLE , NEXT_DEADLINE, REPORT_FREQ_MONTHS, FK_SOURCE_ID, " +
           rplAmp("DESCRIPTION", "DESRCIPTION")  +
             " FROM T_OBLIGATION WHERE NEXT_DEADLINE IS NOT NULL AND " +
             "NEXT_DEADLINE > '0000-00-00'";
       }

    return _executeStringQuery( sql);
  }
  /*
   * Returns all deadlines - NEXT_DEADLINE & NEXT_DEADLINE2
   */
  public String[][] getAllActivityDeadlines(StringTokenizer issues, StringTokenizer countries) throws ServiceException  {

      StringBuffer buf_sql = new StringBuffer();
      String whereClause1 = null;
      String whereClause2 = null;

      if ( issues != null && countries != null) {
    	  whereClause1 = getWhereClause("il.FK_ISSUE_ID", issues );
    	  whereClause2 = getWhereClause("r.FK_SPATIAL_ID", countries );

    	  buf_sql.append("SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE AS DEADLINE, a.FK_SOURCE_ID,");
    	  buf_sql.append(rplAmp("a.DESCRIPTION", "DESCRIPTION"));
    	  buf_sql.append(" FROM T_OBLIGATION a, T_RAISSUE_LNK il, T_RASPATIAL_LNK r WHERE " +
          "  a.PK_RA_ID = il.FK_RA_ID AND a.PK_RA_ID = r.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " +
        "a.NEXT_DEADLINE > '0000-00-00'");
    	  buf_sql.append(" AND ");
    	  buf_sql.append(whereClause1);
    	  buf_sql.append(" AND ");
    	  buf_sql.append(whereClause2);

    	  // EK 010306
    	  //Create UNION SELECT
    	  buf_sql.append(" UNION SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE2 AS DEADLINE, a.FK_SOURCE_ID,");
    	  buf_sql.append(rplAmp("a.DESCRIPTION", "DESCRIPTION"));
    	  buf_sql.append(" FROM T_OBLIGATION a, T_RAISSUE_LNK il, T_RASPATIAL_LNK r WHERE " +
          "  a.PK_RA_ID = il.FK_RA_ID AND a.PK_RA_ID = r.FK_RA_ID AND a.NEXT_DEADLINE2 IS NOT NULL AND " +
        "a.NEXT_DEADLINE2 > '0000-00-00'");
    	  buf_sql.append(" AND ");
    	  buf_sql.append(whereClause1);
    	  buf_sql.append(" AND ");
    	  buf_sql.append(whereClause2);


      } else if (issues!= null && countries == null) {
    	  whereClause1 = getWhereClause("il.FK_ISSUE_ID", issues );
    	  buf_sql.append("SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE AS DEADLINE, a.FK_SOURCE_ID, ");
    	  buf_sql.append(rplAmp("a.DESCRIPTION", "DESCRIPTION"));
    	  buf_sql.append(" FROM T_OBLIGATION a, T_RAISSUE_LNK il WHERE " +
    		"  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " +
    	  	"a.NEXT_DEADLINE > '0000-00-00'");

    	 buf_sql.append(" AND ");
    	 buf_sql.append(whereClause1);
   	  // EK 010306
   	  //Create UNION SELECT
    	 buf_sql.append(" UNION SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE2 AS DEADLINE, a.FK_SOURCE_ID, ");
    	 buf_sql.append(rplAmp("a.DESCRIPTION", "DESCRIPTION"));
    	 buf_sql.append(" FROM T_OBLIGATION a, T_RAISSUE_LNK il WHERE " +
    		"  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE2 IS NOT NULL AND " +
	  		"a.NEXT_DEADLINE2 > '0000-00-00'");

    	 buf_sql.append(" AND ");
    	 buf_sql.append(whereClause1);


       } else if (issues == null && countries != null) {
    	   whereClause1= getWhereClause("il.FK_SPATIAL_ID", countries );

           buf_sql.append("SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE AS DEADLINE, a.FK_SOURCE_ID, ");
           buf_sql.append(rplAmp("a.DESCRIPTION", "DESCRIPTION"));
           buf_sql.append(" FROM T_OBLIGATION a, T_RASPATIAL_LNK il WHERE " +
        		   "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE IS NOT NULL AND " +
        		   "a.NEXT_DEADLINE > '0000-00-00'");

           buf_sql.append(" AND ");
           buf_sql.append(whereClause1);
        	  // EK 010306
        	  //Create UNION SELECT
           buf_sql.append(" UNION SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE2 AS DEADLINE, a.FK_SOURCE_ID, ");
           buf_sql.append(rplAmp("a.DESCRIPTION", "DESCRIPTION"));
           buf_sql.append(" FROM T_OBLIGATION a, T_RASPATIAL_LNK il WHERE " +
        		   "  a.PK_RA_ID = il.FK_RA_ID AND a.NEXT_DEADLINE2 IS NOT NULL AND " +
        		   "a.NEXT_DEADLINE2 > '0000-00-00'");

           buf_sql.append(" AND ");
           buf_sql.append(whereClause1);
       }
       else {
     	   	buf_sql.append("SELECT PK_RA_ID, REPLACE(TITLE, '&', '&#038;') AS TITLE , NEXT_DEADLINE AS DEADLINE, FK_SOURCE_ID, ");
     	   	buf_sql.append(rplAmp("DESCRIPTION", "DESRCIPTION"));
     	   	buf_sql.append(" FROM T_OBLIGATION WHERE NEXT_DEADLINE IS NOT NULL AND " +
              "NEXT_DEADLINE > '0000-00-00'");
      	  // EK 010306
      	  //Create UNION SELECT
     	   	buf_sql.append(" UNION SELECT PK_RA_ID, REPLACE(TITLE, '&', '&#038;') AS TITLE , NEXT_DEADLINE2 AS DEADLINE, FK_SOURCE_ID, ");
     	   	buf_sql.append(rplAmp("DESCRIPTION", "DESRCIPTION"));
     	   	buf_sql.append(" FROM T_OBLIGATION WHERE NEXT_DEADLINE2 IS NOT NULL AND " +
              "NEXT_DEADLINE2 > '0000-00-00'");
        }
    return _executeStringQuery( buf_sql.toString());
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
       "  a.PK_RA_ID = il.FK_RA_ID AND a.PK_RA_ID = r.FK_RA_ID";

      sql = sql + " AND " +  getWhereClause("il.FK_ISSUE_ID", issues );
      sql = sql + " AND " +  getWhereClause("r.FK_SPATIAL_ID", countries );
   } else if (issues!= null && countries == null) {
    sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.FK_SOURCE_ID, " +
      rplAmp("a.DESCRIPTION", "DESCRIPTION") +
    " FROM T_OBLIGATION a, T_RAISSUE_LNK il WHERE " +
      "  a.PK_RA_ID = il.FK_RA_ID";

     sql = sql + " AND " +  getWhereClause("il.FK_ISSUE_ID", issues );
    } else if (issues == null && countries != null) {
        sql = "SELECT DISTINCT a.PK_RA_ID, REPLACE(a.TITLE, '&', '&#038;') AS TITLE, a.NEXT_DEADLINE, a.FK_SOURCE_ID, " +
        rplAmp("a.DESCRIPTION", "DESCRIPTION") +
      " FROM T_OBLIGATION a, T_RASPATIAL_LNK il WHERE " +
        "  a.PK_RA_ID = il.FK_RA_ID";

       sql = sql + " AND " +  getWhereClause("il.FK_SPATIAL_ID", countries );
    }else {
      sql = "SELECT PK_RA_ID, REPLACE(TITLE, '&', '&#038;') AS TITLE, NEXT_DEADLINE, " +
      " FK_SOURCE_ID, " + rplAmp("DESCRIPTION", "DESCRIPTION") + " FROM T_OBLIGATION";
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

  public Vector getObligationDetail(String id) throws ServiceException {

    String sql = "SELECT TITLE AS title, DESCRIPTION as description, NEXT_DEADLINE AS next_deadline, NEXT_DEADLINE2 AS next_deadline2, " +
    "COMMENT as comment, DATE_COMMENTS as date_comments, REPORT_FREQ as report_freq, "+
    " CONCAT('" + rodDomain + "/show.jsv?id=', PK_RA_ID,'&aid=', FK_SOURCE_ID, '&mode=A') AS details_url " +
    " FROM T_OBLIGATION WHERE PK_RA_ID = '" + id + "'";


    return _getVectorOfHashes(sql);
  }
  
  private boolean allowedTable(String tablename) throws ServiceException {
      
      if(tablename.equalsIgnoreCase("T_CLIENT") || 
              tablename.equalsIgnoreCase("T_CLIENT_LNK") ||
              tablename.equalsIgnoreCase("T_DELIVERY") ||
              tablename.equalsIgnoreCase("T_INDICATOR") ||
              tablename.equalsIgnoreCase("T_INFO_LNK") ||
              tablename.equalsIgnoreCase("T_ISSUE") ||
              tablename.equalsIgnoreCase("T_LOOKUP") ||
              tablename.equalsIgnoreCase("T_OBLIGATION") ||
              tablename.equalsIgnoreCase("T_RAISSUE_LNK") ||
              tablename.equalsIgnoreCase("T_RASPATIAL_LNK") ||
              tablename.equalsIgnoreCase("T_ROLE") ||
              tablename.equalsIgnoreCase("T_SOURCE") ||
              tablename.equalsIgnoreCase("T_SOURCE_CLASS") ||
              tablename.equalsIgnoreCase("T_SOURCE_LNK") ||
              tablename.equalsIgnoreCase("T_SOURCE_TYPE") ||
              tablename.equalsIgnoreCase("T_SPATIAL")){
          
          return true;
      }
      return false;
  }
  
  public Vector getTable(String tablename) throws ServiceException {
      
      tablename = tablename.toUpperCase();
      if(allowedTable(tablename)){       
          String sql_stmt = "SELECT * FROM "+tablename;
          return _getVectorOfHashes(sql_stmt);
      }
      
    return null;
    }
  
  public Vector getTableDesc(String tablename) throws ServiceException {
      
      tablename = tablename.toUpperCase();
      if(allowedTable(tablename)){
          Vector rvec = new Vector();
    
          Connection con = null;
          Statement stmt = null;
          ResultSet rset = null;
          
          Hashtable h = null;
          
          String sql_stmt = "SHOW FULL COLUMNS FROM "+tablename;
          _log(sql_stmt);
    
          con = getConnection();
          try {
              stmt = con.createStatement();
              rset = stmt.executeQuery(sql_stmt);
              ResultSetMetaData md = rset.getMetaData();
              
              int columnCnt = md.getColumnCount();
    
              while (rset.next()) {
                  h = new Hashtable();
                  for(int i = 0; i<columnCnt; i++){
                      String name = md.getColumnName(i+1);
                      String value = rset.getString(i+1);
                      if ( value == null)
                          value = "";
                      if(name.equals("Field") || name.equals("Comment"))
                          h.put( name, value  );
                      if(name.equals("Type")){
                          int start = value.indexOf("(");
                          int end = value.indexOf(")");
                          String length = "";
                          String newVal = "";
                          if(start != -1 && end != -1){
                              newVal = value.substring(0,start);
                              if(!newVal.equalsIgnoreCase("enum")){
                                  length = value.substring(start+1, end);
                                  value = newVal;
                              }
                          }
                          h.put("Type",value);
                          h.put("Length",length);
                      }
                  }
                  rvec.addElement(h);
              }          
          } catch (SQLException e) {
              e.printStackTrace();
              logger.error("Error occurred when processing result set: " + sql_stmt,e);
              throw new ServiceException("Error occurred when processing result set: " + sql_stmt);
          } catch (NullPointerException nue ) {
              nue.printStackTrace( System.out );
          } finally {
              _close(con, stmt, null);
          }
        //_log(" return vec");
        return rvec;
      }
      return null;
  }

  public String[][] getParentObligationId(String id) throws ServiceException {

    String sql = "SELECT PARENT_OBLIGATION FROM T_OBLIGATION WHERE " +
    " PK_RA_ID = '" + id + "'";

    return _executeStringQuery(sql);
  }

  public String[][] getLatestVersionId(String id) throws ServiceException {

    String sql = "select PK_RA_ID from T_OBLIGATION where " +
    " (PARENT_OBLIGATION='" + id + "' OR PK_RA_ID='" + id + "')";

    return _executeStringQuery(sql);
  }

  public Vector getPreviousActions(String id, String tab, String id_field) throws ServiceException {

    String sql = null;
    if(id.equals("-1")){
        sql = "select undo_time, col, tab, operation, value, show_object from T_UNDO where operation='U' OR operation='D' OR operation='UN' OR operation='UD' OR operation='UDD' ORDER BY undo_time DESC, tab";
    } else {
        sql = "select a.undo_time, a.col, a.tab, a.operation, a.value, a.show_object from T_UNDO a, T_UNDO b WHERE a.undo_time = b.undo_time AND b.col = '"+id_field+"' AND b.value = "+id+" AND a.tab = '"+tab+"' AND (a.operation='U' OR a.operation='D' OR a.operation='UN' OR a.operation='UD' OR a.operation='UDD') ORDER BY a.undo_time DESC";
    }

    return _getVectorOfHashes(sql);
  }

  public Vector getDeletedFromUndo(String item_type) throws ServiceException {

      String tab = null;
      if(item_type.equals("O' OR ITEM_TYPE='A"))
          tab = "T_OBLIGATION";
      else if(item_type.equals("L"))
          tab = "T_SOURCE";

      String sql = sql = "select undo_time, col, tab, operation, value, show_object from T_UNDO where tab='"+tab+"' AND operation='D' ORDER BY undo_time DESC, tab";

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

   public void addObligationIdsIntoUndo(String id, long ts, String table) throws ServiceException {
       String sqlStmt = "SELECT PK_RA_ID AS id FROM T_OBLIGATION WHERE FK_SOURCE_ID=" + id;
       Vector ids = _getVectorOfHashes(sqlStmt);
       StringBuffer obligation_ids = new StringBuffer();
       for(Enumeration en = ids.elements(); en.hasMoreElements();){
           Hashtable hash = (Hashtable) en.nextElement();
           obligation_ids.append(hash.get("id"));
           if(en.hasMoreElements())
               obligation_ids.append(",");
       }
       String ids_sql = "INSERT INTO T_UNDO VALUES ("+
           ts + ",'"+table+"','OBLIGATIONS','O','y','n','"+obligation_ids.toString()+"',0,'y')";
       _executeUpdate(ids_sql);

    }

   public void insertTransactionInfo(String id, String state, String table, String id_field, long ts, String extraSQL) throws ServiceException {

           String whereClause = id_field + " = " + id +" "+ extraSQL;
           String insert_stmt = "INSERT INTO T_UNDO VALUES ("+
               ts + ",'"+ table +"','"+id_field+"','"+state+"','y','n','"+whereClause+"',0,'n')";
           _executeUpdate(insert_stmt);

   }

   public boolean insertIntoUndo(String id, String state, String table, String id_field, long ts, String extraSQL, String show, String whereClause) throws ServiceException {
          Connection con = null;
          Statement stmt = null;
          ResultSet rset = null;
          String sql_stmt = null;

          // Process the result set
          con = getConnection();

          try {
                stmt = con.createStatement();
                if(whereClause != null)
                    sql_stmt = "SELECT * FROM "+ table +" WHERE "+whereClause;
                else
                    sql_stmt = "SELECT * FROM "+ table +" WHERE "+id_field+" = " + id +" "+ extraSQL;
                rset = stmt.executeQuery(sql_stmt);
                ResultSetMetaData md = rset.getMetaData();

                if (state.equals("U") || state.equals("D") || state.equals("UN") || state.equals("UDD")){
                    int colCnt = md.getColumnCount();
                    int rowCnt = 0;
                    while (rset.next()) {
                        for (int i = 1; i < (colCnt + 1); ++i){
                            String value = rset.getString(i);
                            if(value != null)
                                value = value.replaceAll("'", "''");
                            String column = md.getColumnName(i);
                            int type = md.getColumnType(i);
                            String quotes = "y";
                            if (type == Types.INTEGER)
                                quotes = "n";
                            String isPrimary = isPrimaryKey(table, column);

                            String a = "";
                            if(quotes.equalsIgnoreCase("y"))
                                a = "'";

                            String insert_stmt = "INSERT INTO T_UNDO VALUES ("+
                                    ts + ",'"+ table +"','"+column+"','"+state+"','"+quotes+"','"+isPrimary+"',"+a+value+a+","+rowCnt+",'"+show+"')";
                            _executeUpdate(insert_stmt);
                        }
                        rowCnt++;
                    }
                }
          } catch (SQLException e) {
                logger.error("Error occurred when processing result set: " + sql_stmt,e);
                throw new ServiceException("Error occurred when processing result set: " + sql_stmt);
          } finally {
                 _close(con, stmt, null);
          }
          return true;
     }
   
   private String isPrimaryKey(String table, String column) throws ServiceException {
       String sql_stmt = "SHOW KEYS FROM "+table;
       Vector result = _getVectorOfHashes(sql_stmt);

       for(Enumeration en = result.elements(); en.hasMoreElements();){
           Hashtable hash = (Hashtable) en.nextElement();
           String column_name = (String) hash.get("Column_name");
           String key_name = (String) hash.get("Key_name");
           if(column_name != null && key_name != null){
               if(column.equalsIgnoreCase(column_name) && key_name.equalsIgnoreCase("PRIMARY")){
                   return "y";
               }
           }
       }
       return "n";
 }

   private void copyUndo(String state, String ts, boolean del) throws ServiceException {
       
       long ut = System.currentTimeMillis();

       if(!state.equals("UD")){
           String tinfo_sql = "SELECT undo_time, col, tab, operation, value, quotes, sub_trans_nr FROM T_UNDO "+
           "WHERE undo_time = "+ts+" AND operation = 'A' ORDER BY undo_time,tab,sub_trans_nr";
           
           Vector tinfo_vec = _getVectorOfHashes(tinfo_sql);
           for(Enumeration en = tinfo_vec.elements(); en.hasMoreElements();){
               Hashtable hash = (Hashtable) en.nextElement();
               String t = (String)hash.get("tab");
               String v = (String)hash.get("value");
               if(!state.equals("UNN") && !state.equals("UDD"))
                   insertIntoUndo(null,state,t,null,ut,null,"y",v);
               if(del){
                   String delete_stmt = "DELETE FROM "+hash.get("tab")+" WHERE "+hash.get("value");
                   _executeUpdate(delete_stmt);
               }
           }
       }
       if(!state.equals("UNN") && !state.equals("UDD")){
           String temp = "";
           if(state.equals("UD"))
               temp = " OR operation = 'D'";
           
           String opinfo_sql = "SELECT undo_time, col, tab, operation, value, quotes, sub_trans_nr, p_key, show_object FROM T_UNDO "+
           "WHERE undo_time = "+ts+" AND (operation = 'A' OR operation = 'K' OR operation = 'T' OR operation = 'O' OR operation = 'L' "+temp+") ORDER BY undo_time,tab,sub_trans_nr";
           
           Vector opinfo_vec = _getVectorOfHashes(opinfo_sql);
           for(Enumeration en = opinfo_vec.elements(); en.hasMoreElements();){
               Hashtable h = (Hashtable) en.nextElement();
               String t = (String) h.get("tab");
               String c = (String) h.get("col");
               String val = (String) h.get("value");
               String quotes = (String) h.get("quotes");
               String sub_trans_nr = (String) h.get("sub_trans_nr");
               String p_key = (String) h.get("p_key");
               String show = (String) h.get("show_object");
               String operation = (String) h.get("operation");
               
               if(state.equals("UD") && (operation.equals("D")))
                   operation = "UD";
               
               if(val != null)
                   val = val.replaceAll("'", "''");
               
               String a = "";
               if(quotes.equalsIgnoreCase("y") || val.equals(""))
                   a = "'";
               
               String insert_stmt = "INSERT INTO T_UNDO VALUES ("+
                       ut + ",'"+ t +"','"+c+"','"+operation+"','"+quotes+"','"+p_key+"',"+a+val+a+","+sub_trans_nr+",'"+show+"')";
               _executeUpdate(insert_stmt);
           }
       }
   }

   public String undo(String ts, String tab, String op, String id) throws ServiceException {
       Connection con = null;
       Statement stmt = null;
       ResultSet rset = null;
       String sql_stmt = null;
       String location = "versions.jsp?id=-1";

       // Process the result set
       con = getConnection();

       try {
             stmt = con.createStatement();
             sql_stmt = "SELECT undo_time, col, tab, operation, value, quotes, sub_trans_nr FROM T_UNDO "+
                 "WHERE undo_time = "+ts+" AND operation = '"+op+"' ORDER BY undo_time,tab,sub_trans_nr";
             rset = stmt.executeQuery(sql_stmt);
             ResultSetMetaData md = rset.getMetaData();

                 if(op.equals("U")){
                     copyUndo("UN", ts, true);
                 } else if(op.equals("UN")){
                     copyUndo("UNN", ts, true);
                 } else if(op.equals("D")){
                     if(tab.equals("T_SOURCE")){
                         String s = "SELECT value FROM T_UNDO WHERE undo_time="+ts+" AND operation='O' AND tab='"+tab+"'";
                         String[][] array = _executeStringQuery(s);
                         if(array.length > 0){
                             String ids = array[0][0];
                             StringTokenizer st = new StringTokenizer(ids, ",");
                             while(st.hasMoreTokens()){
                                 String oid = st.nextToken();
                                 String getObligationSql = "SELECT undo_time FROM T_UNDO WHERE tab='T_OBLIGATION' AND col='PK_RA_ID' AND operation='"+op+"' AND value="+oid;
                                 String[][] oa = _executeStringQuery(getObligationSql);
                                 if(oa.length > 0){
                                     String time = oa[0][0];
                                     if(isIdAvailable(oid, "T_OBLIGATION"))
                                         undo(time, "T_OBLIGATION",op,oid);
                                 }
                             }
                         }
                     }
                     copyUndo("UD",ts,false);
                 } else if(op.equals("UD")){
                     if(tab.equals("T_SOURCE")){
                         String s = "SELECT value FROM T_UNDO WHERE undo_time="+ts+" AND operation='O' AND tab='"+tab+"'";
                         String[][] array = _executeStringQuery(s);
                         if(array.length > 0){
                             String ids = array[0][0];
                             StringTokenizer st = new StringTokenizer(ids, ",");
                             while(st.hasMoreTokens()){
                                 String oid = st.nextToken();
                                 String getObligationSql = "SELECT undo_time FROM T_UNDO WHERE tab='T_OBLIGATION' AND col='PK_RA_ID' AND operation='"+op+"' AND value="+oid;
                                 String[][] oa = _executeStringQuery(getObligationSql);
                                 if(oa.length > 0){
                                     String time = oa[0][0];
                                     undo(time,"T_OBLIGATION","UD",oid);
                                 }
                             }
                         }
                     }
                     copyUndo("UDD",ts,true);
                 }
                 
                 String user_stmt = "SELECT value FROM T_UNDO WHERE operation='K' AND undo_time="+ts+" AND tab='"+tab+"'";
                 String[][] user_array = _executeStringQuery(user_stmt);
                 String user = "";
                 if(user_array.length > 0)
                     user = user_array[0][0];
                 
                 String type_stmt = "SELECT value FROM T_UNDO WHERE operation='T' AND undo_time="+ts+" AND tab='"+tab+"'";
                 String[][] type_array = _executeStringQuery(type_stmt);
                 String type = "";
                 if(type_array.length > 0)
                     type = type_array[0][0];
                 
                 if(op.equals("U"))
                     logHistory(type,id,user,"N","Undo Update");
                 else if(op.equals("D"))
                     logHistory(type,id,user,"N","Undo Delete");
                 else if(op.equals("UN"))
                     logHistory(type,id,user,"R","Redo Update");
                 else if(op.equals("UD"))
                     logHistory(type,id,user,"R","Redo Delete");

                 int colCnt = md.getColumnCount();

                 String prev_ut = null;
                 String prev_table = null;
                 String prev_subtransnr = null;
                 String prev_column = null;
                 String prev_value = null;
                 String prev_quotes = null;

                 Vector tvec = new Vector();

                 while (rset.next()) {
                     String ut = rset.getString(1);
                     String value = rset.getString(5);
                     String column = rset.getString(2);
                     String table = rset.getString(3);
                     String quotes = rset.getString(6);
                     String sub_trans_nr = rset.getString(7);

                     /*if(column.equals("REDIRECT_URL")){
                         location = value;
                     }*/

                     if(value != null)
                         value = value.replaceAll("'", "''");

                     if (rset.isLast()){
                         String[] ta = new String[3];
                         ta[0] = column;
                         ta[1] = value;
                         ta[2] = quotes;
                         tvec.add(ta);
                     }

                     if(prev_column != null && prev_value != null && prev_quotes != null){
                         String[] array = new String[3];
                         array[0] = prev_column;
                         array[1] = prev_value;
                         array[2] = prev_quotes;
                         tvec.add(array);

                         if((!sub_trans_nr.equals(prev_subtransnr) || !ut.equals(prev_ut) || !table.equals(prev_table)) || rset.isLast()){

                             StringBuffer cols = new StringBuffer();
                             StringBuffer values = new StringBuffer();
                             for(Enumeration en = tvec.elements(); en.hasMoreElements();){
                                 String[] ar = (String[]) en.nextElement();
                                     cols.append(ar[0]);
                                     String q = "";
                                     if(ar[2].equals("y"))
                                         q = "'";
                                     values.append(q+ar[1]+q);
                                     if(en.hasMoreElements()){
                                         cols.append(",");
                                         values.append(",");
                                     }
                             }
                             String insert_sql = "INSERT INTO "+prev_table+" ("+cols.toString()+") VALUES ("+values.toString()+")";
                             if(!op.equals("UD"))
                                 _executeUpdate(insert_sql);
                             String delete_stmt = "DELETE FROM T_UNDO WHERE undo_time = "+prev_ut+" AND tab='"+prev_table+"' AND operation = '"+op+"'";
                             _executeUpdate(delete_stmt);
                             String delete_stmt2 = "DELETE FROM T_UNDO WHERE undo_time = "+prev_ut+" AND (operation = 'A' OR operation = 'K' OR operation = 'O' OR operation = 'T')";
                             _executeUpdate(delete_stmt2);
                             tvec = new Vector();
                         }
                     }
                     prev_ut = ut;
                     prev_table = table;
                     prev_subtransnr = sub_trans_nr;
                     prev_column = column;
                     prev_value = value;
                     prev_quotes = quotes;
                 }
                 String url_stmt = "SELECT value FROM T_UNDO WHERE operation='L' AND undo_time="+ts+" AND tab='"+tab+"'";
                 String[][] url_array = _executeStringQuery(url_stmt);
                 if(url_array.length > 0){
                     if(!op.equals("UD")){
                         location = url_array[0][0];
                     }
                     String delete_location = "DELETE FROM T_UNDO WHERE undo_time = "+ts+" AND operation = 'L'";
                     _executeUpdate(delete_location);
                 }

             //}
       } catch (SQLException e) {
             logger.error("Error occurred when processing result set: " + sql_stmt,e);
             throw new ServiceException("Error occurred when processing result set: " + sql_stmt);
       } finally {
              _close(con, stmt, null);
       }
       return location;
     }

       public boolean isIdAvailable(String id, String table) throws ServiceException {

           String sqlStmt = null;
           if(table.equalsIgnoreCase("T_OBLIGATION"))
               sqlStmt = "SELECT PK_RA_ID AS id FROM T_OBLIGATION WHERE PK_RA_ID=" + id;
           else if(table.equalsIgnoreCase("T_SOURCE"))
               sqlStmt = "SELECT PK_SOURCE_ID AS id FROM T_SOURCE WHERE PK_SOURCE_ID=" + id;

           String[][] ids = _executeStringQuery(sqlStmt);
           if(ids.length > 0)
               return false;

           return true;

       }

       public String areRelatedObligationsIdsAvailable(String id) throws ServiceException {

           String ids = "";
           String ts = "SELECT undo_time FROM T_UNDO WHERE tab='T_SOURCE' AND col='PK_SOURCE_ID' AND value="+id;
           String[][] tsa = _executeStringQuery(ts);
           if(tsa.length > 0){
               String undo_time = tsa[0][0];
               String ids_sql = "SELECT value FROM T_UNDO WHERE tab='T_SOURCE' AND undo_time="+undo_time+" AND col='OBLIGATIONS'";
               String[][] idsa = _executeStringQuery(ids_sql);
               if(idsa.length > 0)
                   ids = idsa[0][0];
           }

           Vector vec = new Vector();
           StringTokenizer st = new StringTokenizer(ids,",");
           while(st.hasMoreTokens()){
               String token = st.nextToken();
               String sqlStmt = "SELECT PK_RA_ID AS id FROM T_OBLIGATION WHERE PK_RA_ID=" + token;
               String[][] sa = _executeStringQuery(sqlStmt);
               if(sa.length > 0){
                   vec.add(sa[0][0]);
               }
           }
           StringBuffer sb = new StringBuffer();
           for(Enumeration en = vec.elements(); en.hasMoreElements();){
               String match = (String) en.nextElement();
               sb.append(match);
               if(en.hasMoreElements())
                   sb.append(" ");
           }

           return sb.toString();
       }

       public Vector getUndoInformation(String ts, String op, String tab, String id) throws ServiceException {

           Vector vec = new Vector();

           String sql_stmt = "SELECT * FROM T_UNDO "+
           "WHERE undo_time = "+ts+" AND operation = '"+op+"' AND tab = '"+tab+"' ORDER BY undo_time,tab,sub_trans_nr";

           vec = _getVectorOfHashes(sql_stmt);

           if(tab.equals("T_SOURCE") && op.equals("D")){
               String ids = "";
               String ut = "SELECT undo_time FROM T_UNDO WHERE tab='T_SOURCE' AND col='PK_SOURCE_ID' AND value="+id;
               String[][] tsa = _executeStringQuery(ut);
               if(tsa.length > 0){
                   String undo_time = tsa[0][0];
                   String ids_sql = "SELECT value FROM T_UNDO WHERE tab='T_SOURCE' AND operation='O' AND undo_time="+undo_time;
                   String[][] idsa = _executeStringQuery(ids_sql);
                   if(idsa.length > 0)
                       ids = idsa[0][0];
               }
               Vector obligations_vec = new Vector();
               StringTokenizer st = new StringTokenizer(ids,",");
               while(st.hasMoreTokens()){
                   String token = st.nextToken();
                   String utime_sql = "SELECT undo_time FROM T_UNDO WHERE tab = 'T_OBLIGATION' AND operation = 'D' AND col='PK_RA_ID' AND value="+token;
                   String[][] utime = _executeStringQuery(utime_sql);
                   if(utime.length > 0){
                       String ob_sql = "SELECT * FROM T_UNDO WHERE tab='T_OBLIGATION' AND operation='D' AND undo_time="+utime[0][0];
                       obligations_vec = _getVectorOfHashes(ob_sql);
                       if(obligations_vec.size() > 0)
                           vec.addAll(obligations_vec);
                   }
               }
           }

           return vec;
       }

       public String getLastUpdate() throws ServiceException {

           String ret = null;
           Date o_date = new Date();
           Date s_date = new Date();
           DateFormat df = new SimpleDateFormat ("dd/MM/yy");

           String o_sql = "SELECT MAX(LAST_UPDATE) FROM T_OBLIGATION";
           String s_sql = "SELECT MAX(LAST_UPDATE) FROM T_SOURCE";

           String[][] oa = _executeStringQuery(o_sql);
           String[][] sa = _executeStringQuery(s_sql);
           if(oa.length > 0){
               o_date.setYear(Integer.parseInt(oa[0][0].substring(0,4)) - 1900);
               o_date.setMonth(Integer.parseInt(oa[0][0].substring(5,7)) - 1);
               o_date.setDate(Integer.parseInt(oa[0][0].substring(8,10)));
           }
           if(sa.length > 0){
               s_date.setYear(Integer.parseInt(sa[0][0].substring(0,4)) - 1900);
               s_date.setMonth(Integer.parseInt(sa[0][0].substring(5,7)) - 1);
               s_date.setDate(Integer.parseInt(sa[0][0].substring(8,10)));
           }

           if(s_date == null && o_date != null)
               return df.format(o_date);
           else if(s_date != null && o_date == null)
               return df.format(s_date);

           if(o_date != null && s_date != null){
               if(o_date.after(s_date))
                   ret = df.format(o_date);
               else
                   ret = df.format(s_date);
           }

           return ret;

       }

       public Vector getHistory(String id, String tab) throws ServiceException {

           String type = null;
           if(tab.equals("T_OBLIGATION"))
               type = "A";
           else if(tab.equals("T_SOURCE"))
               type = "L";

           String sql = "SELECT LOG_TIME AS time, ACTION_TYPE AS action, USER AS user, DESCRIPTION AS description " +
           "  FROM T_HISTORY WHERE ITEM_ID = " + id + " AND ITEM_TYPE = '" + type + "' ORDER BY LOG_TIME desc";

           return _getVectorOfHashes(sql);
       }

       public String getUndoUser(String ts, String tab) throws ServiceException {

           String sql = "SELECT value FROM T_UNDO WHERE " +
           "  undo_time = " + ts + " AND tab = '" + tab + "' AND operation='K'";

           String[][] ua = _executeStringQuery(sql);
           if(ua.length > 0)
               return ua[0][0];

           return null;
       }

       public String getDifferences(String ts, String tab, String col) throws ServiceException {

           String whereClause = null;
           String ret = null;
           String where_sql = "SELECT value FROM T_UNDO WHERE " +
           "undo_time = " + ts + " AND tab = '" + tab + "' AND operation='A'";

           String[][] wa = _executeStringQuery(where_sql);
           if(wa.length > 0)
               whereClause = wa[0][0];

           if(whereClause != null){
               String value_sql = "SELECT "+col+" FROM "+tab+" WHERE "+whereClause;
               String[][] va = _executeStringQuery(value_sql);
               if(va.length > 0)
                   ret = va[0][0];
           }
           if(ret == null)
               ret = "null";

           return ret;
       }

       public Hashtable getDifferencesInCountries(String ts, String id, String voluntary, String op) throws ServiceException {

           Vector current = new Vector();
           Vector undo = new Vector();

           StringBuffer currentCountries = new StringBuffer();
           StringBuffer undoCountries = new StringBuffer();
           StringBuffer added = new StringBuffer();
           StringBuffer removed = new StringBuffer();

           Hashtable ret = new Hashtable();

           String current_sql = "SELECT FK_SPATIAL_ID FROM T_RASPATIAL_LNK WHERE " +
           "FK_RA_ID = " + id + " AND VOLUNTARY = '"+voluntary+"'";

           String undo_sql = "SELECT value, sub_trans_nr FROM T_UNDO WHERE " +
           "undo_time = " + ts + " AND col = 'FK_SPATIAL_ID' AND operation = '"+op+"'";

           current = _getVectorOfNames(current_sql,null,null,null,"SELECT SPATIAL_NAME FROM T_SPATIAL WHERE PK_SPATIAL_ID = ",null);
           undo = _getVectorOfNames(undo_sql,"T_RASPATIAL_LNK","VOLUNTARY",voluntary,"SELECT SPATIAL_NAME FROM T_SPATIAL WHERE PK_SPATIAL_ID = ",null);

           int cnt1 = 0;
           int i = 0;
           for(Enumeration en = current.elements(); en.hasMoreElements();){
               if(cnt1 != 0){
                   currentCountries.append(", ");
               }
               String match = (String) en.nextElement();
               currentCountries.append(match);
               if(!undo.contains(match)){
                   if(i != 0)
                       added.append(", ");
                   added.append(match);
                   i++;
               }
               cnt1++;
           }

           int cnt2 = 0;
           int j = 0;
           for(Enumeration en = undo.elements(); en.hasMoreElements();){
               if(cnt2 != 0){
                   undoCountries.append(", ");
               }
               String match = (String) en.nextElement();
               undoCountries.append(match);
               if(!current.contains(match)){
                   if(j != 0)
                       removed.append(", ");
                   removed.append(match);
                   j++;
               }
               cnt2++;
           }

           ret.put("undo",undoCountries.toString());
           ret.put("current",currentCountries.toString());
           ret.put("added",added.toString());
           ret.put("removed",removed.toString());

           return ret;
       }

       public Hashtable getDifferencesInClients(String ts, String id, String status, String op, String type) throws ServiceException {

           Vector current = new Vector();
           Vector undo = new Vector();

           StringBuffer currentClients = new StringBuffer();
           StringBuffer undoClients = new StringBuffer();
           StringBuffer added = new StringBuffer();
           StringBuffer removed = new StringBuffer();

           Hashtable ret = new Hashtable();

           String current_sql = "SELECT FK_CLIENT_ID FROM T_CLIENT_LNK WHERE " +
           "FK_OBJECT_ID = " + id + " AND STATUS = '"+status+"' AND TYPE = '"+type+"'";

           String undo_sql = "SELECT value, sub_trans_nr FROM T_UNDO WHERE " +
           "undo_time = " + ts + " AND col = 'FK_CLIENT_ID' AND tab = 'T_CLIENT_LNK' AND operation = '"+op+"'";

           current = _getVectorOfNames(current_sql,null,null,null,"SELECT CLIENT_NAME FROM T_CLIENT WHERE PK_CLIENT_ID = ",null);
           undo = _getVectorOfNames(undo_sql,"T_CLIENT_LNK","TYPE",type,"SELECT CLIENT_NAME FROM T_CLIENT WHERE PK_CLIENT_ID = ",status);

           int cnt1 = 0;
           int i = 0;
           for(Enumeration en = current.elements(); en.hasMoreElements();){
               if(cnt1 != 0){
                   currentClients.append(",<br/>");
               }
               String match = (String) en.nextElement();
               currentClients.append(match);
               if(!undo.contains(match)){
                   if(i != 0)
                       added.append(",<br/>");
                   added.append(match);
                   i++;
               }
               cnt1++;
           }

           int cnt2 = 0;
           int j = 0;
           for(Enumeration en = undo.elements(); en.hasMoreElements();){
               if(cnt2 != 0){
                   undoClients.append(",<br/>");
               }
               String match = (String) en.nextElement();
               undoClients.append(match);
               if(!current.contains(match)){
                   if(j != 0)
                       removed.append(",<br/>");
                   removed.append(match);
                   j++;
               }
               cnt2++;
           }

           ret.put("undo",undoClients.toString());
           ret.put("current",currentClients.toString());
           ret.put("added",added.toString());
           ret.put("removed",removed.toString());

           return ret;
       }

       public Hashtable getDifferencesInIssues(String ts, String id, String op) throws ServiceException {

           Vector current = new Vector();
           Vector undo = new Vector();

           StringBuffer currentIssues = new StringBuffer();
           StringBuffer undoIssues = new StringBuffer();
           StringBuffer added = new StringBuffer();
           StringBuffer removed = new StringBuffer();

           Hashtable ret = new Hashtable();

           String current_sql = "SELECT FK_ISSUE_ID FROM T_RAISSUE_LNK WHERE " +
           "FK_RA_ID = " + id;

           String undo_sql = "SELECT value, sub_trans_nr FROM T_UNDO WHERE " +
           "undo_time = " + ts + " AND col = 'FK_ISSUE_ID' AND operation = '"+op+"'";

           current = _getVectorOfNames(current_sql,null,null,null,"SELECT ISSUE_NAME FROM T_ISSUE WHERE PK_ISSUE_ID = ",null);
           undo = _getVectorOfNames(undo_sql,null,null,null,"SELECT ISSUE_NAME FROM T_ISSUE WHERE PK_ISSUE_ID = ",null);

           int cnt1 = 0;
           int i = 0;
           for(Enumeration en = current.elements(); en.hasMoreElements();){
               if(cnt1 != 0){
                   currentIssues.append(", ");
               }
               String match = (String) en.nextElement();
               currentIssues.append(match);
               if(!undo.contains(match)){
                   if(i != 0)
                       added.append(", ");
                   added.append(match);
                   i++;
               }
               cnt1++;
           }

           int cnt2 = 0;
           int j = 0;
           for(Enumeration en = undo.elements(); en.hasMoreElements();){
               if(cnt2 != 0){
                   undoIssues.append(", ");
               }
               String match = (String) en.nextElement();
               undoIssues.append(match);
               if(!current.contains(match)){
                   if(j != 0)
                       removed.append(", ");
                   removed.append(match);
                   j++;
               }
               cnt2++;
           }

           ret.put("undo",undoIssues.toString());
           ret.put("current",currentIssues.toString());
           ret.put("added",added.toString());
           ret.put("removed",removed.toString());

           return ret;
       }

       public Hashtable getDifferencesInInfo(String ts, String id, String op, String cat) throws ServiceException {

           Vector current = new Vector();
           Vector undo = new Vector();

           StringBuffer currentInfo = new StringBuffer();
           StringBuffer undoInfo = new StringBuffer();
           StringBuffer added = new StringBuffer();
           StringBuffer removed = new StringBuffer();

           Hashtable ret = new Hashtable();

           String current_sql = "SELECT FK_INFO_ID FROM T_INFO_LNK WHERE " +
           "FK_RA_ID = " + id;

           String undo_sql = "SELECT value, sub_trans_nr FROM T_UNDO WHERE " +
           "undo_time = " + ts + " AND col = 'FK_INFO_ID' AND operation = '"+op+"'";

           current = _getVectorOfNames(current_sql,null,null,null,"SELECT C_TERM FROM T_LOOKUP WHERE CATEGORY = '"+cat+"' AND C_VALUE = ",null);
           undo = _getVectorOfNames(undo_sql,null,null,null,"SELECT C_TERM FROM T_LOOKUP WHERE CATEGORY = '"+cat+"' AND C_VALUE = ",null);

           int cnt1 = 0;
           int i = 0;
           for(Enumeration en = current.elements(); en.hasMoreElements();){
               if(cnt1 != 0){
                   currentInfo.append(", ");
               }
               String match = (String) en.nextElement();
               currentInfo.append(match);
               if(!undo.contains(match)){
                   if(i != 0)
                       added.append(", ");
                   added.append(match);
                   i++;
               }
               cnt1++;
           }

           int cnt2 = 0;
           int j = 0;
           for(Enumeration en = undo.elements(); en.hasMoreElements();){
               if(cnt2 != 0){
                   undoInfo.append(", ");
               }
               String match = (String) en.nextElement();
               undoInfo.append(match);
               if(!current.contains(match)){
                   if(j != 0)
                       removed.append(", ");
                   removed.append(match);
                   j++;
               }
               cnt2++;
           }

           ret.put("undo",undoInfo.toString());
           ret.put("current",currentInfo.toString());
           ret.put("added",added.toString());
           ret.put("removed",removed.toString());

           return ret;
       }

       public void dpsirValuesFromExcelToDB(int id, String value) throws ServiceException {

           String sql = "UPDATE T_OBLIGATION SET DPSIR_"+value+" = 'yes' WHERE PK_RA_ID = "+id;
           _executeUpdate(sql);
       }

       public void editPeriod(String start, String end, String spatialHistoryID, String ra_id) throws ServiceException {

           String start_date = changeDateDelimeter(start);
           String end_date = changeDateDelimeter(end);
           String sql = null;
           
           if(ra_id == null || ra_id.equals(""))
               sql = "UPDATE T_SPATIAL_HISTORY SET START_DATE = '"+start_date+"', END_DATE = '"+end_date+"' WHERE PK_SPATIAL_HISTORY_ID = "+spatialHistoryID;
           else{
               if(!start.equals("") && !end.equals(""))
                   sql = "UPDATE T_SPATIAL_HISTORY SET START_DATE = '"+start_date+"', END_DATE = '"+end_date+"' WHERE FK_RA_ID = "+ra_id;
               else if(!start.equals("") && end.equals(""))
                   sql = "UPDATE T_SPATIAL_HISTORY SET START_DATE = '"+start_date+"' WHERE FK_RA_ID = "+ra_id;
               else if(!end.equals("") && start.equals(""))
                   sql = "UPDATE T_SPATIAL_HISTORY SET END_DATE = '"+end_date+"' WHERE FK_RA_ID = "+ra_id;
               else if(start.equals("") && end.equals(""))
                   return;
           }
           _executeUpdate(sql);
       }

       private String changeDateDelimeter(String date){
           if(date != null && !date.equals("")){
               StringTokenizer st = new StringTokenizer(date, "/");
               String[] dmy = new String[3];
               int cnt = 0;
               while(st.hasMoreTokens()){
                   String token = st.nextToken();
                   dmy[cnt] = token;
                   cnt++;
               }
               String sDate = dmy[2] + "-" + dmy[1] + "-" + dmy[0];

               return sDate;
           }
           return "NULL";
       }

   }
