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

/**
* Interface, providing common constants
*/

public interface Config {

/**
* Properties file name
*/
  
  public static final String PROP_FILE = "rod";


/**
* Parameter in the props file for the domain of WebROD
*/
  public static final String ROD_URL_NS = "rod.url.namespace";
/**
*
*/
  public static final String ROD_URL_RO_NS = "rod.url.ro_namespace";
  
/**
* Parameter in the props file for the domain of WebROD
*/
  public static final String ROD_URL_DOMAIN = "rod.url.domain";

/**
* Servlet name showing RA
*/
  public static final String URL_SERVLET = "show.jsv";
/**
* RA ID in Url
*/
  public static final String URL_ACTIVITY_ID = "id";

/**
* AID for RA in url
*/
  public static final String URL_ACTIVITY_AID = "aid";

/**
* Mode, for RA Mode=A in url
*/

  public static final String URL_ACTIVITY_AMODE = "mode=A";

/**
* Mode, for RA Mode=R in url
*/

  public static final String URL_ACTIVITY_RMODE = "mode=R";


  
/**
* DB connection URL for DB connection
*/
//  public static final String DB_URL = "db.url";

/**
* DB driver 
  */
//  public static final String DB_DRV = "db.drv";

/**
* User PWD  for DB connection 
*/
//  public static final String DB_USER_PWD = "db.pwd";


/**
* User ID for DB connection 
*/
//  public static final String DB_USER_ID = "db.usr";


 
}