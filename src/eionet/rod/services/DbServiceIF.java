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

package eionet.rod.services;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.sql.Connection;
import java.util.Set;
import java.util.HashMap;


/**
 * Method definitions for database operations (SQL queries)
 *
 * @author  Kaido Laine
 * @version 1.0
 */
public interface DbServiceIF  {

  /**
  * Type for RO in HISTORY table
  */
  public static final String RO_LOG_TYPE = "O";

  /**
  * Type for RA in HISTORY table
  */
  public static final String RA_LOG_TYPE = "A";

  /**
  * Type for LI in HISTORY table
  */
  public static final String LI_LOG_TYPE = "L";

  /**
  * Action type for UPDATE statements in HISTORY table
  */
  public static final String UPDATE_ACTION_TYPE = "U";

  /**
  * Action type for DELETE statements in HISTORY table
  */
  public static final String DELETE_ACTION_TYPE = "D";

  /**
  * Action type for INSERT statements in HISTORY table
  */
  public static final String INSERT_ACTION_TYPE = "I";
/**
 * Creates a connection to the database
 * @return java.sql.Connection
 */

  public Connection getConnection() throws ServiceException;


 /**
 * returns RA data in 2-dimensional array
 * 0-PK_ACTIVITTY_DETAILS_ID, 1-TITLE, 2-COUNTRY_ID, 3-COUNTRY_NAME
 */
  public String[][] getRaData() throws ServiceException;  

 /**
 * returns all responsible role ids from T_ACTIVITY_DETAILS
 */
  public String[][] getRespRoles() throws ServiceException;    

 /**
 * Returns deadline data in 2-dimensional array
 * 0-PK_RA_ID, 1-FIRST_REPORTING, 2-REPORT_FREQ_MONTHS, 3-VALID_TO, 4-TERMINATE
 */
  public String[][] getDeadlines() throws ServiceException;  

 /**
 * Saves next deadlines for given RA
 */
  public void saveDeadline(String raId, String next, String next2) throws ServiceException ;

 /**
 * Saves next terminated value for given RA
 */
  public void saveTerminate(String raId, String terminated) throws ServiceException ;

 /**
 * Saves roles
 */
  public void saveRole(Hashtable role ) throws ServiceException ;

 /**
 * Saves delivery
 * @param countryMAp : HAShMap for holding country names and Id's
 */
  public void saveDeliveries(String raId, Vector deliveries, HashMap countryMap ) throws ServiceException ;   

 
  /**
  * Activities used in RDF
  * @return array of hashes (PK_RA_ID, TITLE, etc)
  */
  public Vector getActivities(  ) throws ServiceException ;

  /**
  * Legal Instruments ARRAY of STRUCTs for RDF
  * @return array of hashes
  */
  public Vector getInstruments() throws ServiceException ;

  /**
  * Logs record insert/update/delete history
  * @param String itemType
  * @param String itemId
  * @param String userName
  * @param String actionType  
  * @param String description  
  */
  
  public void logHistory(String itemType, String itemId, String userName, String actionType, String description) throws ServiceException ;   


  /**
  * Returns the change history of the item
  * @param String itemType
  * @param String itemId
  * @return String[][]  (0:log_time, 1:action_type, 2:user, 3:description)
  */

  public String[][] getItemHistory(String itemType, String itemId) throws ServiceException ;


  /**
  * Returns deleted items of this type
  * @param String itemType
  * @return String[][]  (0:ITEM_ID, 1:LOG_TIME, 2:USER )
  */

  public String[][] getDeletedItems(String itemType) throws ServiceException ;

  /**
  * Returns next deadlines of activities
  * Activities, not having a deadline are not returned
  * @return String[][]  (0:PK_RA_ID, 1:TITLE, 2:NEXT_REPORTING, 3:FK_RO_ID)
  */

  public String[][] getActivityDeadlines() throws ServiceException ;

  /**
  * Returns obligations, corresponging to the issue ids, given as parameters
  * @return String[][]  (0:PK_RO_ID, 1:ALIAS, 2:SOURCE.TITLE, 3:FK_SOURCE_ID)
  */

  public String[][] getIssueObligations(StringTokenizer ids) throws ServiceException ;

  /**
  * Returns activities, corresponging to the issue ids, given as parameters
  * @return String[][]  (0:PK_RO_ID, 1:ALIAS, 2:SOURCE.TITLE, 3:FK_SOURCE_ID)
  */

  public String[][] getIssueActivities(StringTokenizer ids) throws ServiceException ;


  /**
  * Stores to RA table country Ids for RAs have deliveries
  */

  //public void markDeliveries ( HashMap countryIds ) throws ServiceException;

  /**
  * Returns countries from the DB: PK_SPATIAL_ID, SPATIAL_NAME from T_SPATIAL
  */
  public Vector getCountries() throws ServiceException;

  /**
  * Returns max PK_RO_ID
  */
  public String getMaxROId() throws ServiceException;
  

  public void backUpDeliveries() throws ServiceException;

  /**
  * Harvesting successful, remove backup
  */
  public void commitDeliveries() throws ServiceException;  

  public void rollBackDeliveries (String raId ) throws ServiceException ;

  public void commitRoles() throws ServiceException;  
  public void backUpRoles() throws ServiceException;  

 // public Vector backupSpatialHistory(String raId) throws ServiceException;  
  public void logSpatialHistory(String raId, String spatialId, String voluntary)   throws ServiceException;  

  //used in AddClient screen
  public String[][] getCountryIdPairs() throws ServiceException;
  
}



