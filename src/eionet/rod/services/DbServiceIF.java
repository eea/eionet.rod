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
import java.util.Vector;
import java.sql.Connection;


/**
 * Method definitions for database operations (SQL queries)
 *
 * @author  Kaido Laine
 * @version 1.0
 */
public interface DbServiceIF  {

/**
 * Creates a connection to the database
 * @return java.sql.Connection
 */

  public Connection getConnection() throws ServiceException;

 /**
 * returns RA IDs in an array
 */
//  public String[][] getActivityIds() throws ServiceException;  

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
  * Deltes data from all tables
  */
  public void dropTables(int mode) throws ServiceException;

 /**
 * Saves countries
 */
 // public void saveCountries( Vector countries ) throws ServiceException;

 /**
 * Saves parameter groups
 */
 // public void saveParamGroups(Vector paramGroups ) throws ServiceException ;  

 /**
 * Saves issues
 */
//  public void saveIssues(Vector issues ) throws ServiceException ;    

 /**
 * Saves activity details
 */
  //public void saveActivities(Vector activities ) throws ServiceException ;    

 /**
 * Saves param group links
 */
//  public void saveParamGroupLinks( Vector prmGrpLinks ) throws ServiceException ;  

 /**
 * Saves issue links
 */
//  public void saveIssueLinks( Vector issueLinks ) throws ServiceException ;  

 /**
 * Saves deadlines
 */
//  public void saveDeadlines( String raId, String deadline) throws ServiceException ;     


 /**
 * Saves roles
 */
  public void saveRole(Hashtable role ) throws ServiceException ;

 /**
 * Saves delivery
 */
  public void saveDelivery(String raId, String countryId, Hashtable delivery ) throws ServiceException ;   

 /**
 * Get Activity details
 */
  //public Vector getActivityDetails() throws ServiceException ;   
  /**
  * Deadline of the activity 
  */
  //public String getDeadLine( String activityId) throws ServiceException ;


  /**
  * Activities
  * @return array of hashes (PK_RA_ID, TITLE)
  */
  public Vector getActivities(  ) throws ServiceException ;
  
}

