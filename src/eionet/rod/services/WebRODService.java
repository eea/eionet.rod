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

import java.util.Vector;
import com.tee.uit.security.AppUser;
//import java.util.Hashtable;

/**
 * Container class for providing public services of WebROD
 * through XML/RPC || SOAP
 * <P>
 * @author Kaido Laine
 */
public  class WebRODService {

  private static DbServiceIF dbSrv;


 public WebRODService() {}
/*  public WebRODService(AppUser userCtx) {
    //System.out.println("====================== name= " + userCtx.getUser());
  } */
  
  /**
	* Returns Activity Ids and Titles
  * @return Vector (contains hashtables, one for each activity)
  * @throw ServiceException  
	*/
//  public static Vector getActivities() throws ServiceException {
  public Vector getActivities() throws ServiceException {

  if (dbSrv == null )
    dbSrv = RODServices.getDbService();

//System.out.println("====================== s= " + _s);    
    return dbSrv.getActivities();
  }

  /**
  * Returns all countries
  * @return Vector (contains hashtables, one for each record)
  * @throw ServiceException  
	*/
/*  public static Vector getCountries() throws ServiceException {
    return DbService.getCountries();
  } */


  /**
	* Returns deadline for this activity
  * @return String
  * @param String activity ID
  * @throw ServiceException
	*/
/*  public static String getDeadline( String activityId) throws ServiceException {
    return DbService.getDeadLine( activityId );
    } */

  /**
	* Returns parameter groups
  * @return Vector
  * @throw ServiceException
	*/
/*  public static Vector getParamGroups( ) throws ServiceException {
    return DbService.getParamGroups();
  } */

  /**
	* Returns issue IDs and values
  * @return Vector
  * @throw ServiceException
	*/
/*  public static Vector getIssues( ) throws ServiceException {
    return DbService.getIssues();
  } */

  /**
	* Returns issue links
  * Vector contains of one-dimensional String[] arrays
  * 
  * @param String activityID
  * @return Vector
  * @throw ServiceException
	*/
/*  public static Vector getIssueLinks( String activityID ) throws ServiceException {
    return DbService.getIssueLinks(activityID);
  } */

  /**
	* Returns group links
  * Vector contains of one-dimensional String[] arrays
  * 
  * @param String activityID
  * @return Vector
  * @throw ServiceException
	*/
/*  public static Vector getGroupLinks( String activityID ) throws ServiceException {
//    return RODServices.getDbService().getGroupLinks(activityID);
    return DbService.getGroupLinks( activityID );
    //return null;    
  } */

  /**
	* Returns Activity data for each country
  * @return Vector of Hashtables
  * @throw ServiceException
	*/
/*  public static Vector getActivityDetails( ) throws ServiceException {
    return DbService.getActivityDetails();
  } */

}

