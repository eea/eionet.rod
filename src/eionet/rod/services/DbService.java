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
 * The Original Code is "EINRC-4 / WebROD Project".
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

package eionet.rod.services;

import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Vector;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.tee.xmlserver.DBPoolIF;
import com.tee.xmlserver.XDBApplication;
import com.tee.xmlserver.Logger;


/**
 * WebROD Remote service database methods implementation.
 */
class DbService implements Config {

  //static variables
  private static String domain;
  /*
  private static String dbUrl;
  private static String dbDriver;
  private static String dbUser;
  private static String dbPsw;
  */

  private static ResourceBundle props; //props file
 
  //later to the prop file
  private static String DATE_FORMAT = "yyyy-MM-dd";
	private static String ANSI_DATETIME_FORMAT = "yyyy-MM-dd hh:MM";

  //+RV000105 driver specific stuff
  public static final int OTHER = 0;
  public static final int ORACLE = 1;
  public static final int MYSQL = 2;

  private int _driverType = MYSQL;


  private static DBPoolIF dbPool ;

  private DbService() throws ServiceException {  }

  
  static  {
  //_log("*** static ");  
    if (props == null)
//     try {
        props = ResourceBundle.getBundle(Config.PROP_FILE);
//     } catch (MissingResourceException mre) {
//       throw new ServiceException("Properties file " + PROP_FILE + ".properties not found");
//     }

    if (domain == null)
      domain = props.getString(Config.ROD_URL_DOMAIN);

//     try {
        if (dbPool == null)
          dbPool = XDBApplication.getInstance().getDBPool();
         //dbPool = new DBPool( dbUrl, dbDriver, dbUser, dbPsw ) ; 
        //_log("DBPool OK");         
//     } catch(Exception e ){
//       throw new ServiceException("Error getting DBPool: " + e.toString());
//     }

  } 

  /**
	* Returns all Reporting activities
	*/

  public static Vector getActivityDetails(  ) throws ServiceException {


    String sql = "SELECT PK_RA_ID, A.FK_RO_ID, FK_SPATIAL_ID, CONCAT(RESPONSIBLE_ROLE, '-',  LOWER(S.SPATIAL_TWOLETTER)) AS RESPONSIBLE_ROLE, " + 
      " CONCAT('" + domain + "/" + URL_SERVLET + "?" + URL_ACTIVITY_ID + "=" + "', PK_RA_ID, " + 
      "'&" + URL_ACTIVITY_AID + "=', A.FK_RO_ID, '&" + URL_ACTIVITY_AMODE + "') AS RA_URL, IF(A.TITLE='','Activity', A.TITLE) AS TITLE, " +
      " C.CLIENT_NAME, " + 
      " REPORTING_FORMAT, " +
      " REPORT_FORMAT_URL, FORMAT_NAME, IF(R.ALIAS='', 'Obligation', R.ALIAS) AS ALIAS, " +
      " CONCAT('" + domain + "/" + URL_SERVLET + "?" + URL_ACTIVITY_ID + "=" + "', A.FK_RO_ID, '&" + URL_ACTIVITY_AID + "='" +
      " , A.FK_SOURCE_ID, '&" + URL_ACTIVITY_RMODE + "') AS RO_URL " +
      " FROM T_ACTIVITY A, T_SPATIAL_LNK SL, T_SPATIAL S, T_REPORTING R LEFT OUTER JOIN T_CLIENT C ON R.FK_CLIENT_ID = C.PK_CLIENT_ID " +
      " WHERE A.FK_RO_ID = R.PK_RO_ID AND SL.FK_RO_ID = R.PK_RO_ID AND S.PK_SPATIAL_ID = SL.FK_SPATIAL_ID ";



      Vector ret = null;

//_log("SQL="+ sql)      ;

      ret = _getVectorOfHashes(sql);

      return ret;          
  }



  /**
  * Returns all countries
	*/
  public  static Vector getCountries() throws ServiceException {
    String sql  = " SELECT  PK_SPATIAL_ID, SPATIAL_NAME, SPATIAL_TWOLETTER " +
      " FROM T_SPATIAL WHERE SPATIAL_TYPE = 'C' ";

     //Vector ret = _executeVectorQuery( sql );
     Vector ret = _getVectorOfHashes(sql);
     return ret; 
     }

  /**
	* Returns deadline for this activity
  * @return  Vector
  * @param String activity ID
  * @throw ServiceException
	*/
  public static String getDeadLine( String activityId) throws ServiceException {

    String deadLine = ""; 
    String sql = "SELECT NEXT_REPORTING FROM T_ACTIVITY " + 
      " WHERE PK_RA_ID = " + activityId; 

    String ret[][] = _executeStringQuery(sql );
 
    if (ret.length == 1) 
      deadLine = ret[0][0] == null ? "" : ret[0][0] ;
    else
      throw new ServiceException("No such RA ID=" + activityId);
    //_log("RA= "+ activityId + " deadline= " + deadLine);      
    return deadLine;
    }

  /**
	* Returns parameter groups
  * @return  Vector
  * @throw ServiceException
	*/
  public  static Vector getParamGroups( ) throws ServiceException {
    String sql = "SELECT PK_GROUP_ID, GROUP_NAME, GROUP_TYPE " + 
      " FROM T_PARAM_GROUP ";

    Vector ret = _getVectorOfHashes(sql); // _executeVectorQuery( sql );
    return ret;
  }

  /**
	* Returns issue IDs and values
  * @return  Vector
  * @throw ServiceException
	*/
  public static Vector getIssues( ) throws ServiceException {

    String sql = "SELECT PK_ISSUE_ID, ISSUE_NAME FROM T_ISSUE ";
    //Vector ret = _executeVectorQuery( sql );
    Vector ret = _getVectorOfHashes( sql );    
  
    return ret;
  }

  /**
	* Returns issue links
  * @param String activityID
  * @return  Vector
  * @throw ServiceException
	*/
  public static Vector getIssueLinks( String activityID ) throws ServiceException {
    String sql = "SELECT IL.FK_ISSUE_ID, A.PK_RA_ID FROM T_ACTIVITY AS A, T_ISSUE_LNK AS IL " +
      " WHERE IL.FK_RO_ID = A.FK_RO_ID AND A.PK_RA_ID = " + activityID ;

    //Vector ret = _executeVectorQuery( sql );
    Vector ret = _getVectorOfHashes( sql );        
   
    return ret;

  }

  /**
	* Returns group links
  * @param String activityID
  * @return  Vector
  * @throw ServiceException
	*/
  public static Vector getGroupLinks( String activityID ) throws ServiceException {
  
    String sql = "SELECT P.FK_GROUP_ID, PL.FK_RA_ID FROM T_PARAMETER AS P, T_PARAMETER_LNK AS PL " +
      " WHERE PL.FK_PARAMETER_ID = P.PK_PARAMETER_ID AND PL.FK_RA_ID=" + activityID ;

    //Vector ret = _executeVectorQuery( sql );
    Vector ret = _getVectorOfHashes( sql );        
   
    return ret;

  }

// <-



/**
 * DB dependent date conversion function
 */
 /*  private String _dateLiteral(String dateLit) {
     if (_driverType == ORACLE)
       return "TO_DATE('" + dateLit + "','" + ANSI_DATETIME_FORMAT + "')";
     else
       return "'" + dateLit + "'";
  }
  */


/**
 *
 */
   private static void _close(Connection con, Statement stmt, ResultSet rset)
      throws ServiceException {
      try {
         if (rset != null)
            rset.close();
         if (stmt != null) {
            stmt.close();
            //+RV010914
            if (!con.getAutoCommit())
               con.commit();
         }
      } catch (Exception e) {
         throw new ServiceException("Error"  + e.getMessage()) ;
      } finally {  //+RV011003
         try { con.close(); } catch (SQLException e) {
	         throw new ServiceException("Error"  + e.getMessage()) ;
         }
      }
   }

/**
 *
 */
	 private static String[][] _executeStringQuery(String sql_stmt) throws ServiceException {
      Vector rvec = new Vector(); // Return value as Vector
  		String rval[][] = {};       // Return value
      Connection con = null;
      Statement stmt = null;
      ResultSet rset = null;

      Logger.log(sql_stmt);

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
				 Logger.log("Error occurred when processing result set: " + sql_stmt,e);
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
 * Not used yet
 */
 /*
   private int _executeUpdate(String sql_stmt) throws ServiceException {
      //KL011011
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
		      _log( "UpdateStatement failed: " + e.toString());
         // Free resources
         try {
            _close(con, stmt, null);
         } catch (Throwable exc) {
	          throw new ServiceException("_close() failed: " + sql_stmt);
	     }
      Logger.log("Connection.createStatement() failed: " + sql_stmt,e);
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
*/

/**
 * Creates new DbServiceImpl object
 */
/*
   DbService() throws ServiceException {

      dbUrl = RODServices.getFileService().getStringProperty( FileServiceIF.DB_URL);
      dbDriver = RODServices.getFileService().getStringProperty( FileServiceIF.DB_DRV);
      dbUser = RODServices.getFileService().getStringProperty( FileServiceIF.DB_USER_ID);
      dbPsw = RODServices.getFileService().getStringProperty( FileServiceIF.DB_USER_PWD);


            
       //tblNamePrefix = "T_" +System.currentTimeMillis() + "_" ;
       try {
		     //dbPool = useTestPool ? new DBPool() : XDBApplication.getInstance().getDBPool();
         dbPool = new DBPool( dbUrl, dbDriver, dbUser, dbPsw ) ;        
       } catch(Exception e ){
	       throw new ServiceException("Error getting DBPool: " + e.toString());
       }
   }
*/
/**
 * Returns new database connection. 
 *
 * @throw ServiceException if no connections were available.
 */   
  private static Connection getConnection() throws ServiceException {
    
    //Connection con = dbPool.getConnection( dbUser, dbPsw );
    Connection con = dbPool.getConnection( );
    
    if (con == null)
      throw new ServiceException("Failed to get database connection");

    return con;
    }



  /**
	* Returns all Reporting activities pk_id + title
  * used in RDF generating
	*/

  public static Vector getActivities(  ) throws ServiceException {
      String sql = "SELECT PK_RA_ID, TITLE FROM T_ACTIVITY ";
      return  _getVectorOfHashes(sql);
  }
  

  private static void _log(String s) {
  	Logger.log("*********** " + s);
	  }



 /**
 * returns result as vector of String arrays
 */
  private static Vector _getVectorOfHashes(String sql_stmt) throws ServiceException {

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
				 Logger.log("Error occurred when processing result set: " + sql_stmt,e);
			}  catch (NullPointerException nue ) {
          nue.printStackTrace( System.out );
      } finally {
				 _close(con, stmt, null);
			}
    //_log(" return vec");
    return rvec;

  }
    
  }


