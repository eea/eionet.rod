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

/**
 * Container class for providing public services of WebROD
 * through XML/RPC || SOAP
 * <P>
 * @author Kaido Laine
 */
public  class WebRODService {

  private static DbServiceIF dbSrv;


 public WebRODService() {}
  
  /**
	* Returns Activity Ids and Titles
  * @return Vector (contains hashtables, one for each activity)
  * @throw ServiceException  
	*/
  public Vector getActivities() throws ServiceException {

  if (dbSrv == null )
    dbSrv = RODServices.getDbService();

    return dbSrv.getActivities();
  }

  /**
  * Returns all countries
  * @return Vector (contains hashtables, one for each record)
  * @throw ServiceException  
	*/
  public Vector getCountries() throws ServiceException {

    if (dbSrv == null )
      dbSrv = RODServices.getDbService();

    return dbSrv.getCountries();
  } 


}
