package eionet.rod;

import eionet.rod.services.DbServiceIF;
import eionet.rod.services.LogServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import com.tee.xmlserver.SaveHandler;
import eionet.directory.DirectoryService;
import eionet.directory.DirServiceException;

public class HistoryLogger extends SaveHandler {
  private static DbServiceIF db;
  private static LogServiceIF logger;

  //public HistoryLogger() throws ServiceException {  }
  
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
  static void logObligationHistory(String id, String user, int state, String desc ) {
    try {  
      logHistory( DbServiceIF.RO_LOG_TYPE, id, user, state, desc);
    } catch (ServiceException se ) {
      logger.error("Error saving RO history " + se.toString());
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
      user = DirectoryService.getFullName(user);
    } catch (DirServiceException dir) {
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