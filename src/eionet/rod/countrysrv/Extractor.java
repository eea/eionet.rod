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

//import eionet.countrysrv.soap.*;
import eionet.rod.services.*;


/**
* Pulls information from various services and saves it to DB.
*/

public class Extractor implements ExtractorConstants {

 public static final    int ALL_DATA = 0;
 public static final    int ACTS = 1;
 public static final    int DELIVERIES = 2;
 public static final    int ROLES = 3;
 public static final    int ACTS_DELIVERIES = 4;

  private static FileServiceIF fileSrv = null;
  boolean debugLog = true;
  private static PrintWriter out = null;

  private static LogServiceIF logger ;  

	// For debugLog: returns the current date and time (wrapped) as String
  //

  static { 
    logger = RODServices.getLogService();
  }
  
  public static void main(String[] args) {
    try {
      //logger = RODServices.getLogService();
      harvest(0);
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


  public static void harvest( ) throws ServiceException {
    harvest( 0 );
  }
 /**
 * Extract the data
 */
  public static void harvest( int mode ) throws ServiceException {
    // Initial set-up: create class; open the log file
    //mode, which data to harvest
    
    int remoteSrvType = SERVICE_CLIENT_TYPE;
    
    DbServiceIF csDb;
    String logPath = null;
    String logfileName = null;

    //ServiceClientIF wrClient;

    ServiceClientIF crClient;
    ServiceClientIF ldap;

    //service urls
    String crUrl, wrUrl, dirUrl;

    //namespaces
    String raNs=null, countryNs=null;
    
    //KL 020423
    //Extractor extractor = new Extractor();
    Extractor extractor = null;

    //logger = RODServices.getLogService();

    try {
      extractor = new Extractor();
      fileSrv = RODServices.getFileService();      

      raNs = fileSrv.getStringProperty( FileServiceIF.RA_NAMESPACE );
      countryNs = fileSrv.getStringProperty( FileServiceIF.COUNTRY_NAMESPACE);
      extractor.debugLog = RODServices.getFileService().getBooleanProperty("extractor.debugmode");     
      
      try {
        remoteSrvType = fileSrv.getIntProperty( fileSrv.REMOTE_SRV_TYPE );

        logPath = fileSrv.getStringProperty("extractor.logpath");
        logfileName = fileSrv.getStringProperty("extractor.logfilename");
        //extractor.debugLog = RODServices.getFileService().getBooleanProperty("extractor.debugmode");             
         
      }  catch (ServiceException e) {
        //use default type (XML/RPC), if not specified
        logger.warning("Unable to get logger settings from properties file. using default The following error was reported:\n" + e.toString());      
        //logPath = "";
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
        out = new PrintWriter(new FileWriter(logPath + logfileName, !extractor.debugLog), true);
      } catch (java.io.IOException e) {
        //extractor.out.println("Unable to open log file for writing. The following error was reported:\n" + e.toString());
        //using default logger instead
        logger.warning("Unable to open log file for writing. using default. The following error was reported:\n" + e.toString());        
        e.printStackTrace();
      }

    // Start processing
    //extractor.out.println(extractor.cDT() + "Extractor v1.1 - processing... Please wait.");
    log(extractor.cDT() + "Extractor v1.1 - processing... Please wait.");    
    
    long a = System.currentTimeMillis();

    //DB connection
    try {
      csDb = RODServices.getDbService();
    }  catch (Exception e) {
      //extractor.out.println("Opening connection to database failed. The following error was reported:\n" + e.toString());
      log("Opening connection to database failed. The following error was reported:\n" + e.toString());      
      e.printStackTrace();
      extractor.exitApp(false); 
      throw new ServiceException("Opening connection to database failed." );
    }

    if(extractor.debugLog) 
      log("Db connection ok.");
      //extractor.out.println("Successfully opened connections to Content Registry, Directory, and CS DB.");
    // Empty the database first
    try {
      csDb.dropTables( mode );
    }  catch (Exception e) {
      //extractor.out.println("Database operation failed. The following error was reported:\n" + e.toString());
      log("Database operation failed. The following error was reported:\n" + e.toString());      
      e.printStackTrace();
      extractor.exitApp(false); 
      throw new ServiceException("Deleting data from tables failed" + e.toString());
    }

    if(extractor.debugLog) 
      log("Successfully emptied the database.");


    /***************************************************
    * Start extracting
    ***************************************************/

/*
  if ( mode == ALL_DATA || mode == ACTS || mode == ACTS_DELIVERIES ) {    
    try {
      if(extractor.debugLog) 
        log("Filling the database with new data:");

      Vector prms = new Vector();
      Vector acts = csDb.getActivityDetails();

      csDb.saveActivities(acts );

      if(extractor.debugLog) 
          log("* Activity details saved");

      // deadlines
      String[][] raIds = csDb.getActivityIds();
      prms.add(null);
      for(int i = 0; raIds != null && i < raIds.length; i++) {
          prms.set(0, raIds[i][0] );

          String deadLine = csDb.getDeadLine( raIds[i][0] );
          csDb.saveDeadlines( raIds[i][0], deadLine );
      }

      if(extractor.debugLog) 
        log("* Deadlines OK");
        
     }  catch (Exception e) {
      //extractor.out.println("Operation failed while filling the database from WebROD. The following error was reported:\n" + e.toString());
      log("Operation failed while saving act details / deadlines. The following error was reported:\n" + e.toString());      
      e.printStackTrace();
      extractor.exitApp(false); //return;
      throw new ServiceException("Operation failed while saving act details / deadlines. The following error was reported:\n" + e.toString());
    }
  } //mode includes activities


*/
  
    // Get delivery list from Content Registry and save it also
    if (mode == ALL_DATA || mode == DELIVERIES || mode == ACTS_DELIVERIES ) {   
      try {
        crUrl =  fileSrv.getStringProperty( FileServiceIF.CONTREG_SRV_URL );
      }  catch (Exception e) {
         log("Opening connection to Content Registry failed. The following error was reported:\n" + e.toString());
         extractor.exitApp(false); //return;         
         throw new ServiceException("No URL to ContReg service specified in the props file");        
      }      
      try {
        //crUrl =  fileSrv.getStringProperty( FileServiceIF.CONTREG_SRV_URL );
        crClient = ServiceClients.getServiceClient(CONTREG_SRV_NAME, crUrl, remoteSrvType );
      }  catch (Exception e) {
        log("Opening connection to Content Registry failed. The following error was reported:\n" + e.toString());
        e.printStackTrace();
        extractor.exitApp(false); //return;
        throw new ServiceException("Error creating a client top ContReg service at: " + crUrl);
      }

    try {
      
      //raData: 0: PK_DETAILS_ID, 1: RA_TITLE, 2: Country_id, 3:Country_Name
      String[][] raData = csDb.getRaData();

      Hashtable attrs = new Hashtable();
      Vector prms = new Vector();
      prms.add(null);

      //go through all details and get deliveries
      for(int i = 0; raData != null && i < raData.length; i++) {

        attrs.clear();
        attrs.put( countryNs , raData[i][3] ); //Country name
        attrs.put( raNs , raData[i][1] );      // RA title
        //prms.clear();        
        prms.setElementAt(attrs, 0);

//hack->
        //Vector deliveries = null;
//if ( raData[i][1].indexOf(':') != -1 )
        Vector deliveries = (Vector)crClient.getValue(CONTREG_GETENTRIES_METHOD, prms);
//else
//        deliveries =new Vector();

        log("Received " + deliveries.size() + "  deliveries to RA: " + raData[i][1] + " from " + raData[i][3] );
        
        for (int ii=0; ii<deliveries.size(); ii++){
          //deliveryKEY:STRING(url), VALUE:HASH (metadata)
          Hashtable delivery = (Hashtable)deliveries.elementAt(ii);
          //PK_RA_ID, PK_SPATIAL_ID, delivery
          csDb.saveDelivery( raData[i][0], raData[i][2], delivery );
        }
      }

      if(extractor.debugLog) 
        log("* Deliveries OK");
        
    }  catch (Exception e) {
      log("Operation failed while filling the database from Content Registry. The following error was reported:\n" + e.toString());
      e.printStackTrace();
      extractor.exitApp(false); //return;
      throw new ServiceException("Error getting data from Content Registry " + e.toString());
    } 
   } // mode includes deliveries data

    // Get roles from CIRCA Directory and save them, too
    if (mode == ALL_DATA || mode == ROLES ) {

      try {
        dirUrl = fileSrv.getStringProperty( FileServiceIF.DIRECTORY_SRV_URL);
      }  catch (Exception e) {
        logger.error("Directory service URL is not specified in the props file");
        //extractor.out.println("Directory service URL is not specified in the props file");
        //e.printStackTrace();
        extractor.exitApp(false); //return;
        throw new ServiceException("Error getting data fron Content Registry " + e.toString());        
      }
      
      try {
        //dirUrl = fileSrv.getStringProperty( FileServiceIF.DIRECTORY_SRV_URL);
        ldap = ServiceClients.getServiceClient(DIRECTORY_SRV_NAME, dirUrl, remoteSrvType );
      }   catch (Exception e) {
        logger.error("Opening connection to directory service failed. The following error was reported:\n" + e.toString());
        //extractor.out.println("Opening connection to directory service failed. The following error was reported:\n" + e.toString());
        e.printStackTrace();
        extractor.exitApp(false); //return;
        throw new ServiceException("Error getting data from Eionet Directory failed" + e.toString());        
      }

    try  {
      Set roleSet = new HashSet();
      String[][] respRoles = csDb.getRespRoles();
      
     for(int i = 0; respRoles != null && i < respRoles.length; i++) {
        //do not take the role, beginnning with '-' seriously, it is just a country code
          if(respRoles[i][0].length() > 0 && respRoles[i][0].charAt(0) != '-' )
            roleSet.add(respRoles[i][0]); 
      }

      Iterator roles = roleSet.iterator();

      Vector prms = new Vector();
      prms.add(null);
      
      while(roles.hasNext()) {

        String roleName = (String)(roles.next());
        if (roleName != null) {
        //_log("rolename = " + roleName);        
          prms.setElementAt(roleName,0);
          Hashtable role = null;
          try {
            role = (Hashtable)ldap.getValue(DIRECTORY_GETROLE_METHOD, prms );
            log("Received role info for " + roleName + " from Directory");
          } catch (ServiceClientException rpce ) {
            logger.error("Error getting role " + roleName + " " + rpce.toString());
            //extractor.out.println("Error getting role " + roleName + " " + rpce.toString());          
          } catch (Exception e ) {
            e.printStackTrace();
          }

          if (role != null)
            csDb.saveRole(role);
        }
      } // roles.next()
      
      if(extractor.debugLog) 
        log("* Roles OK");
        
    }  catch (Exception e) {
      log("Operation failed while filling the database from CIRCA Directory. The following error was reported:\n" + e.toString());
      e.printStackTrace();
      extractor.exitApp(false); //return;
      throw new ServiceException("Operation failed while filling the database from CIRCA Directory. The following error was reported:\n" + e.toString());
    }
  } // mode includes roles

    // End processing
    //
    long b = System.currentTimeMillis();    
    log(" ** Harvesting successful TOTAL TIME = " + (b-a));

    extractor.exitApp(true);
  }

  private static void log(String s ) { 
    if (out == null)
      logger.debug( s );
    else
      out.println( s );
      
    //System.out.println("============= " + s); 
  }
}