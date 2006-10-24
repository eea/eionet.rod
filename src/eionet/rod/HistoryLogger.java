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


import eionet.rod.services.LogServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import com.tee.xmlserver.SaveHandler;
import com.tee.uit.security.AuthMechanism;
import com.tee.uit.security.SignOnException;
import eionet.rod.services.modules.db.dao.IHistoryDao;

public class HistoryLogger extends SaveHandler {
  private static IHistoryDao historyDao;
  private static LogServiceIF logger;

  
  static  {

    logger = RODServices.getLogService();
	    try {  
	    		historyDao = RODServices.getDbService().getHistoryDao(); 
	      } catch (ServiceException se ) {
	        logger.fatal(se);
	      }
  }

 static void logLegisgationHistory(String id, String user, int state, String desc ) {
    try {  
      logHistory( historyDao.LI_LOG_TYPE, id, user, state, desc);
    } catch (ServiceException se ) {
      logger.error("Error saving LI history " + se.toString());
    }
  }
/*
  static void logObligationHistory(String id, String user, int state, String desc ) {
    try {  
      logHistory( historyDao.RO_LOG_TYPE, id, user, state, desc);
    } catch (ServiceException se ) {
      logger.error("Error saving RO history " + se.toString());
    }
  }
*/
  static void logSpatialHistory(String id, String spatialId, String voluntary) {
    try {
       int ra_id = Integer.valueOf(id).intValue();
       int spatial_id = Integer.valueOf(spatialId).intValue();
       RODServices.getDbService().getSpatialHistoryDao().logSpatialHistory(ra_id, spatial_id, voluntary);
    } catch (ServiceException se ) {
      logger.error("Error saving spatial link history " + se.toString());
    }
  }
  static void logActivityHistory(String id, String user, int state, String desc ) {
    try {  
      logHistory( historyDao.RA_LOG_TYPE, id, user, state, desc);
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

    RODServices.getDbService().getHistoryDao().logHistory( type, id, user, action, desc );
  }

 private static String getAction(int state) {
  String action = "";
  if ( state==INSERT_RECORD)
    action = historyDao.INSERT_ACTION_TYPE;
  else if ( state==DELETE_RECORD)
    action = historyDao.DELETE_ACTION_TYPE;
  else if ( state== MODIFY_RECORD)
    action = historyDao.UPDATE_ACTION_TYPE;

  return action;    
    
 }
}