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
 * The Original Code is "EINRC-5 / WebROD Project".
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


package eionet.rod;

import eionet.rod.services.DbServiceIF;
import eionet.rod.services.LogServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import com.tee.xmlserver.SaveHandler;
import eionet.directory.DirectoryService;
import eionet.directory.DirServiceException;
import com.tee.uit.security.AuthMechanism;
import com.tee.uit.security.SignOnException;

public class HistoryLogger extends SaveHandler {
  private static DbServiceIF db;
  private static LogServiceIF logger;

  
  static  {

    logger = RODServices.getLogService();
    if (db==null)
		try {
			db = RODServices.getDbService();
	  } catch (ServiceException se ) {
		logger.error("Error getting DbPool " + se.toString());
	  }
  }

 static void logLegisgationHistory(String id, String user, int state, String desc ) {
    try {  
      logHistory( DbServiceIF.LI_LOG_TYPE, id, user, state, desc);
    } catch (ServiceException se ) {
      logger.error("Error saving LI history " + se.toString());
    }
  }
/*
  static void logObligationHistory(String id, String user, int state, String desc ) {
    try {  
      logHistory( DbServiceIF.RO_LOG_TYPE, id, user, state, desc);
    } catch (ServiceException se ) {
      logger.error("Error saving RO history " + se.toString());
    }
  }
*/
  static void logSpatialHistory(String id, String spatialId, String voluntary) {
    try {
      db.logSpatialHistory(id, spatialId, voluntary);
    } catch (ServiceException se ) {
      logger.error("Error saving spatial link history " + se.toString());
    }
  }
  static void logActivityHistory(String id, String user, int state, String desc ) {
    try {  
      logHistory( DbServiceIF.RA_LOG_TYPE, id, user, state, desc);
    } catch (ServiceException se ) {
      logger.error("Error saving history " + se.toString());
    }
  }
  private static void logHistory(String type, String id, String user, int state, String desc) throws ServiceException {

    String action = getAction(state);

    try {
      user = AuthMechanism.getFullName(user); //DirectoryService.getFullName(user);
      
    } catch (SignOnException sece) {
      RODServices.getLogService().warning("Error getting full name for " + user);
    }

    db.logHistory( type, id, user, action, desc );
  }

 private static String getAction(int state) {
  String action = "";
  if ( state==INSERT_RECORD)
    action = DbServiceIF.INSERT_ACTION_TYPE;
  else if ( state==DELETE_RECORD)
    action = DbServiceIF.DELETE_ACTION_TYPE;
  else if ( state== MODIFY_RECORD)
    action = DbServiceIF.UPDATE_ACTION_TYPE;

  return action;    
    
 }
}