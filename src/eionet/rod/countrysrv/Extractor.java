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
 * Original Code: Ander Tenno (TietoEnator)
 */


package eionet.rod.countrysrv;

import com.tee.uit.client.ServiceClientIF;
import com.tee.uit.client.ServiceClients;
import com.tee.uit.client.ServiceClientException;


import java.util.Vector;

import java.util.Date;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import eionet.rod.services.*;
import eionet.directory.DirectoryService;
import eionet.directory.DirServiceException;
import java.util.HashMap;
import com.tee.uit.security.AuthMechanism;
import com.tee.uit.security.SignOnException;


/**
* Pulls information from various services and saves it to DB.
*/

public class Extractor implements ExtractorConstants {

  public static final    int ALL_DATA = 0;
  public static final    int DELIVERIES = 1;
  public static final    int ROLES = 2;

  public static final    int PARAMS = 3;

  private static FileServiceIF fileSrv = null;
  boolean debugLog = true;
  private static PrintWriter out = null;
  private static LogServiceIF logger ;  
  private  DbServiceIF csDb;

  private static Extractor extractor;

	// For debugLog: returns the current date and time (wrapped) as String
  //

  static { 
    logger = RODServices.getLogService();
  }

  public Extractor() {

    try {
      if (csDb==null) {

        csDb = RODServices.getDbService();
      }
    }  catch (Exception e) {
      //extractor.out.println("Opening connection to database failed. The following error was reported:\n" + e.toString());
      log("Opening connection to database failed. The following error was reported:\n" + e.toString());      
      e.printStackTrace();
      exitApp(false); 
      //throw new ServiceException("Opening connection to database failed." );
    }
    log("Db connection ok.");

  }  
  public static void main(String[] args) {
    try {
      //logger = RODServices.getLogService();
      String userName = SYSTEM_USER;
      
      String type = null;

      if (args.length == 1 ) 
        type = args[0];
      else if (args.length > 1 ) {
         System.out.println("Usage: Extractor [-mode]");
         return;
      }
      else
        type="0";


      int iType = Integer.valueOf(type).intValue();

      if (extractor==null)
        extractor=new Extractor();
            
      extractor.harvest(iType, userName);

    } catch (ServiceException se ) {
      logger.error(se.toString());
    }
  }
  private String cDT() {
    Date d = new Date();
    return new String("[" + d.toString() + "] ");
  }

  // Cleanup after everything is done
  //
  public void exitApp(boolean successful) {
    log(this.cDT() + "Extractor v1.0 - " + ((successful)? "finished succesfully." : "failed to complete."));
    if (out != null) {
      out.flush();
      out.close();
    }
  }


 /**
 * Extract the data
 */
  public void harvest( int mode, String userName ) throws ServiceException {
    // Initial set-up: create class; open the log file
    //mode, which data to harvest
    
    int remoteSrvType = SERVICE_CLIENT_TYPE;
    

    String logPath = null;
    String logfileName = null;

    //ServiceClientIF wrClient;

    ServiceClientIF crClient;
    ServiceClientIF ldap;

    //service urls
    String crUrl, wrUrl;

    //namespaces
    String raNs=null, countryNs=null;

    //prefix for RA ID to get the data from CR KL040303
    String raIdPref=null;
    
    //KL 020423
    //Extractor extractor = new Extractor();
    //Extractor extractor = null;

    //logger = RODServices.getLogService();

    try {
      //extractor = new Extractor();
      fileSrv = RODServices.getFileService();      

      raNs = fileSrv.getStringProperty( FileServiceIF.RA_NAMESPACE );
      raIdPref = fileSrv.getStringProperty( FileServiceIF.RO_NAMESPACE );
      countryNs = fileSrv.getStringProperty( FileServiceIF.COUNTRY_NAMESPACE);
      //extractor.debugLog = RODServices.getFileService().getBooleanProperty("extractor.debugmode");     
      debugLog = RODServices.getFileService().getBooleanProperty("extractor.debugmode");           
      
      try {
        remoteSrvType = fileSrv.getIntProperty( fileSrv.REMOTE_SRV_TYPE );

        logPath = fileSrv.getStringProperty("extractor.logpath");
        logfileName = fileSrv.getStringProperty("extractor.logfilename");
        //extractor.debugLog = RODServices.getFileService().getBooleanProperty("extractor.debugmode");             
         
      }  catch (ServiceException e) {
        //use default type (XML/RPC), if not specified
        logger.warning("Unable to get logger settings from properties file. using default The following error was reported:\n" + e.toString());      
        //logfileName = LOG_FILE ; //"extractorlog.txt";
        //extractor.debugLog = false;
      }
      
    }  catch (ServiceException e) {
      //KL 021009 -> cannot print out, when creating logger does not succeed
      //extractor.out.println("Unable to get log file settings from properties file, using defaults. The following error was reported:\n" + e.toString());
      logger.error("Unable to get settings from properties file. The following error was reported:\n" + e.toString());
      e.printStackTrace();
      throw new ServiceException("Unable to get settings from properties file. The following error was reported:\n" + e.toString());
    }

    //KL021009
    //noncense, cannot write to the log file, if opening it does not succeed :)
    if (logfileName != null)
      try {
        out = new PrintWriter(new FileWriter(logPath + logfileName, !debugLog), true);
      } catch (java.io.IOException e) {
        //using default logger instead
        logger.warning("Unable to open log file for writing. using default. The following error was reported:\n" + e.toString());        
        e.printStackTrace();
      }

    // Start processing
    //extractor.out.println(extractor.cDT() + "Extractor v1.1 - processing... Please wait.");
    log(cDT() + "Extractor v1.1 - processing... Please wait.");    
    
    long a = System.currentTimeMillis();

    //DB connection

      
    String userFullName = userName;
    String[][] raData;
    
    if (!userName.equals(SYSTEM_USER))
      try {
        //userFullName = DirectoryService.getFullName(userName);
        userFullName = AuthMechanism.getFullName(userName);
      } catch (SignOnException se ) {
        log("Error getting full name " + se.toString());
      }
    
    // Empty the database first
    //DEPRECATED
    
    String actionText = "Harvesting - ";

    /***************************************************
    * Start extracting
    ***************************************************/
  
    // Get delivery list from Content Registry and save it also
    if (mode == ALL_DATA || mode == DELIVERIES  ) {   
      actionText += " deliveries ";
      try {
        crUrl =  fileSrv.getStringProperty( FileServiceIF.CONTREG_SRV_URL );
      }  catch (Exception e) {
         log("Opening connection to Content Registry failed. The following error was reported:\n" + e.toString());
         exitApp(false); //return;         
         throw new ServiceException("No URL to ContReg service specified in the props file");        
      }      
      try {
        crClient = ServiceClients.getServiceClient(CONTREG_SRV_NAME, crUrl, remoteSrvType );
      }  catch (Exception e) {
        log("Opening connection to Content Registry failed. The following error was reported:\n" + e.toString());
        e.printStackTrace();
        exitApp(false); //return;
        throw new ServiceException("Error creating a client top ContReg service at: " + crUrl);
      }

    try {
      raData = csDb.getRaData();

      Hashtable attrs = new Hashtable();
      Vector prms = new Vector();
      prms.add(null);

      //go through all RA's and get deliveries
      HashMap cMap = new HashMap();
      csDb.backUpDeliveries();
      
      for(int i = 0; raData != null && i < raData.length; i++) {
        attrs.clear();
        attrs.put(raNs , raIdPref + "/" + raData[i][0]);      // PREFIX + RA ID
        prms.setElementAt(attrs, 0);

        try {
          Vector deliveries = (Vector)crClient.getValue(CONTREG_GETENTRIES_METHOD, prms);
          log("Received " + deliveries.size() + "  deliveries for RA: " + raData[i][1]);
          csDb.saveDeliveries( raData[i][0], deliveries, cMap);
        } catch (Exception se ) {
          csDb.rollBackDeliveries(raData[i][0]);
          log ("Error harvesintg deliveries for RA: " + raData[i][0] + " " + se.toString());
        }
      }

      csDb.commitDeliveries();
      
      if(debugLog) 
        log("* Deliveries OK");
        
    }  catch (Exception e) {
      log("Operation failed while filling the database from Content Registry. The following error was reported:\n" + e.toString());
      e.printStackTrace();
      exitApp(false); //return;
      throw new ServiceException("Error getting data from Content Registry " + e.toString());
    } 
   } // mode includes deliveries data

    // Get roles from CIRCA Directory and save them, too
    if (mode == ALL_DATA || mode == ROLES ) {
      actionText += " - roles ";
      try  {
        Set roleSet = new HashSet();
        String[] respRoles = csDb.getRespRoles();

      log("Found " + respRoles.length + " roles from database");      
        
      
      csDb.backUpRoles();
      
      for(int i = 0; i < respRoles.length; i++) {

        saveRole(respRoles[i]);
      
        /*String roleName =  respRoles[i]; 
        if (roleName != null) {
          Hashtable role = null;
          try {
            role = DirectoryService.getRole(roleName);
            log("Received role info for " + roleName + " from Directory");
          } catch (DirServiceException de ) {
            logger.error("Error getting role " + roleName + " " + de.toString());
          } catch (Exception e ) {
            e.printStackTrace();
          }

          if (role != null) {
            String uid="";
            String orgId="";
            String orgName=orgId;
            String fullName=uid;
            Hashtable person = null;
            Hashtable org = null;
            
            Vector occupants = (Vector)role.get("OCCUPANTS");
            if (occupants != null && occupants.size() > 0 )
              uid = (String)occupants.elementAt(0);
            try {
              person=DirectoryService.getPerson(uid);
            } catch (DirServiceException dire) {
              logger.error("Error getting person " + uid + " " + dire.toString());
            } catch (Exception e) {
               e.printStackTrace();        
            }

            if (person != null) {
              fullName=(String)person.get("FULLNAME");
              orgId=(String)person.get("ORG_ID");
            }

            try {
              org =  DirectoryService.getOrganisation(orgId);
              if (org!=null)
                orgName=(String)org.get("NAME");
            } catch (DirServiceException dire) {
              logger.error("Error getting organisation" + orgId + " " + dire.toString());
            } catch (Exception e) {
               e.printStackTrace();        
            }        

            csDb.saveRole(role, fullName, orgName);
          }
          
        }*/
      } // roles.next()

      csDb.commitRoles();
    
      if(debugLog) 
        log("* Roles OK");

      //persons + org name

    }  catch (Exception e) {
      log("Operation failed while filling the database from CIRCA Directory. The following error was reported:\n" + e.toString());
      e.printStackTrace();
      exitApp(false); //return;
      throw new ServiceException("Operation failed while filling the database from CIRCA Directory. The following error was reported:\n" + e.toString());
    }
  } // mode includes roles

  if (mode == ALL_DATA || mode == PARAMS) {
    actionText += " -parameters";
    //Extract from DD will come here!!!
    //duplicate query, fix me!
  /*
    raData=csDb.getRaData();

    for(int i = 0; raData != null && i < raData.length; i++) {
      csDb.harvestParams(raData[i][0]);
   }
  */          
    //DataDict client:
    
  }
    
    csDb.logHistory("H", "0",  userFullName, "X", actionText);
    // End processing
    //
    long b = System.currentTimeMillis();    
    log(" ** Harvesting successful TOTAL TIME = " + (b-a));

    exitApp(true);
  }

  private static void log(String s ) { 
    if (out == null)
      logger.debug( s );
    else
      out.println( s );
      
  }

  public  void saveRole(String roleId) throws ServiceException {
    String roleName =  roleId;
    if (roleName != null) {
      Hashtable role = null;
      try {
        role = DirectoryService.getRole(roleName);
        log("Received role info for " + roleName + " from Directory");
      } catch (DirServiceException de ) {
        logger.error("Error getting role " + roleName + " " + de.toString());
      } catch (Exception e ) {
        e.printStackTrace();
      }

      if (role != null) {
        String uid="";
        String orgId="";
        String orgName=orgId;
        String fullName=uid;
        Hashtable person = null;
        Hashtable org = null;
            
        Vector occupants = (Vector)role.get("OCCUPANTS");
        if (occupants != null && occupants.size() > 0 )
          uid = (String)occupants.elementAt(0);
        try {
          person=DirectoryService.getPerson(uid);
        } catch (DirServiceException dire) {
          logger.error("Error getting person " + uid + " " + dire.toString());
        } catch (Exception e) {
           e.printStackTrace();        
        }

        if (person != null) {
          fullName=(String)person.get("FULLNAME");
          orgId=(String)person.get("ORG_ID");
        }

        try {
          org =  DirectoryService.getOrganisation(orgId);
          if (org!=null)
            orgName=(String)org.get("NAME");
        } catch (DirServiceException dire) {
          logger.error("Error getting organisation" + orgId + " " + dire.toString());
        } catch (Exception e) {
           e.printStackTrace();        
        }        

        csDb.saveRole(role, fullName, orgName);
      }
          
    }

  }
}