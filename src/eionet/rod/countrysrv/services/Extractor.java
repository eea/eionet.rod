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


package eionet.rod.countrysrv.services;

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
import eionet.rod.countrysrv.services.*;


/**
* Pulls information from various services and saves it to DB.
*/

public class Extractor implements eionet.rod.countrysrv.services.Constants {

 public static final    int ALL_DATA = 0;
 public static final    int ACTS = 1;
 public static final    int DELIVERIES = 2;
 public static final    int ROLES = 3;
 public static final    int ACTS_DELIVERIES = 4;

  private static FileServiceIF fileSrv = null;
  boolean debugLog = true;
  PrintWriter out;
  

	// For debugLog: returns the current date and time (wrapped) as String
  //

  public static void main(String[] args) {
    harvest(0);
  }
  private String cDT() {
    Date d = new Date();
    return new String("[" + d.toString() + "] ");
  }

  // Prints the supplied two-dimensional array to output stream
  //
/*  private void outputArray(String[][] arr) {
    if(arr == null || arr.length == 0)
      return;
    for(int i = 0; i < arr.length; i++) {
      for(int j = 0; j < arr[i].length; j++)
        System.out.print(arr[i][j] + "\t");
      System.out.println("");
    }
  } */
  

  // Cleanup after everything is done
  //
  public void exitApp(boolean successful) {
    this.out.println(this.cDT() + "Extractor v1.0 - " + ((successful)? "finished succesfully." : "failed to complete."));
    this.out.close();
  }


  public static void harvest( ) {
    harvest( 0 );
  }
 /**
 * Extract the data
 */
  public static void harvest( int mode ) {
    // Initial set-up: create class; open the log file
    //mode, which data to harvest
    
    int remoteSrvType = SERVICE_CLIENT_TYPE;
    
    DbServiceIF csDb;
    String logPath;
    String logfileName;

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

    try {
      extractor = new Extractor();
      fileSrv = CSServices.getFileService();      
      //logPath = CSServices.getFileService().getStringProperty("extractor.logpath");
      //logfileName = CSServices.getFileService().getStringProperty("extractor.logfilename");

      logPath = fileSrv.getStringProperty("extractor.logpath");
      logfileName = fileSrv.getStringProperty("extractor.logfilename");

      raNs = fileSrv.getStringProperty( FileServiceIF.RA_NAMESPACE );
      countryNs = fileSrv.getStringProperty( FileServiceIF.COUNTRY_NAMESPACE);
     
      try {
        remoteSrvType = fileSrv.getIntProperty( fileSrv.REMOTE_SRV_TYPE );
      }  catch (ServiceException e) {
        //use default type, if not specified
       
      }
      
      extractor.debugLog = CSServices.getFileService().getBooleanProperty("extractor.debugmode");
    }    catch (ServiceException e) {
      extractor.out.println("Unable to get log file settings from properties file, using defaults. The following error was reported:\n" + e.toString());
      e.printStackTrace();
      logPath = "";
      logfileName = LOG_FILE ; //"extractorlog.txt";
      extractor.debugLog = false;
    }
    try {
      extractor.out = new PrintWriter(new FileWriter(logPath + logfileName, !extractor.debugLog), true);
    }
    catch (java.io.IOException e) {
      extractor.out.println("Unable to open log file for writing. The following error was reported:\n" + e.toString());
      e.printStackTrace();
    }

    // Start processing
    extractor.out.println(extractor.cDT() + "Extractor v1.1 - processing... Please wait.");
    //long a = System.currentTimeMillis();
    // Create connections to WebROD, Content Registry, Directory, and CS DB
   /* try {
      wrUrl = fileSrv.getStringProperty( FileServiceIF.WEBROD_SRV_URL );
      wrClient = ServiceClients.getServiceClient(ROD_SRV_NAME, wrUrl, remoteSrvType );
    } 
    catch (Exception e) {
      extractor.out.println("Opening connection to WebROD failed. The following error was reported:\n" + e.toString());
      e.printStackTrace();
      extractor.exitApp(false); return;
    } */
    try {
      csDb = CSServices.getDbService();
    }  catch (Exception e) {
      extractor.out.println("Opening connection to database failed. The following error was reported:\n" + e.toString());
      e.printStackTrace();
      extractor.exitApp(false); return;
    }

    if(extractor.debugLog) extractor.out.println("Successfully opened connections to Content Registry, Directory, and CS DB.");

    // Empty the database first
    try {
      csDb.dropTables( mode );
    } 
    catch (Exception e) {
      extractor.out.println("Database operation failed. The following error was reported:\n" + e.toString());
      e.printStackTrace();
      extractor.exitApp(false); return;
    }
    if(extractor.debugLog) extractor.out.println("Successfully emptied the database.");


    // Start filling the database


/***************************************************
* Start extracting
***************************************************/



  if ( mode == ALL_DATA || mode == ACTS || mode == ACTS_DELIVERIES ) {    
    try {
      if(extractor.debugLog) 
        extractor.out.println("Filling the database with new data:");

      Vector prms = new Vector();

      Vector acts = csDb.getActivityDetails();

      csDb.saveActivities(acts );

      if(extractor.debugLog) 
          extractor.out.println("* Activities");

      // deadlines
      String[][] raIds = csDb.getActivityIds();

      for(int i = 0; raIds != null && i < raIds.length; i++) {

          if (prms.size() == 0 )
            prms.add(raIds[i][0]);
          else
            prms.set(0, raIds[i][0] );

          String deadLine = csDb.getDeadLine( raIds[i][0] );
          csDb.saveDeadlines( raIds[i][0], deadLine );
      }
      if(extractor.debugLog) 
        extractor.out.println("* Deadlines");
     }  catch (Exception e) {
      extractor.out.println("Operation failed while filling the database from WebROD. The following error was reported:\n" + e.toString());
      e.printStackTrace();
      extractor.exitApp(false); return;
    }
  } //mode includes activities
    // Get delivery list from Content Registry and save it also
    //
  if (mode == ALL_DATA || mode == DELIVERIES || mode == ACTS_DELIVERIES ) {   

    try {
      crUrl =       fileSrv.getStringProperty( FileServiceIF.CONTREG_SRV_URL );
      crClient = ServiceClients.getServiceClient(CONTREG_SRV_NAME, crUrl, remoteSrvType );
    }  catch (Exception e) {
      extractor.out.println("Opening connection to Content Registry failed. The following error was reported:\n" + e.toString());
      e.printStackTrace();
      extractor.exitApp(false); return;
    }


    try {
      
      //raData: 0: PK_DETAILS_ID, 1: RA_TITLE, 2: Country__Name
      String[][] raData = csDb.getRaData();

      Hashtable attrs = new Hashtable();
      Vector prms = new Vector();
      prms.add(null);

      //go through all details and get deliveries
      for(int i = 0; raData != null && i < raData.length; i++) {

        attrs.clear();
        attrs.put( countryNs , raData[i][2] );
        attrs.put( raNs , raData[i][1] );
        //prms.clear();        
        prms.setElementAt(attrs, 0);

        Vector deliveries = (Vector)crClient.getValue(CONTREG_GETENTRIES_METHOD, prms);
    
        for (int ii=0; ii<deliveries.size(); ii++){
          //deliveryKEY:STRING(url), VALUE:HASH (metadata)
          Hashtable delivery = (Hashtable)deliveries.elementAt(ii);
          csDb.saveDelivery( raData[i][0], delivery );
        }
      }

      if(extractor.debugLog) 
        extractor.out.println("* Deliveries");
    }  catch (Exception e) {
      extractor.out.println("Operation failed while filling the database from Content Registry. The following error was reported:\n" + e.toString());
      e.printStackTrace();
      extractor.exitApp(false); return;
    } 
   } // mode includes deliveries data

  // Get roles from CIRCA Directory and save them, too
  if (mode == ALL_DATA || mode == ROLES ) {

    try {
      dirUrl = fileSrv.getStringProperty( FileServiceIF.DIRECTORY_SRV_URL);
      ldap = ServiceClients.getServiceClient(DIRECTORY_SRV_NAME, dirUrl, remoteSrvType );
    }   catch (Exception e) {
      extractor.out.println("Opening connection to CIRCA Directory failed. The following error was reported:\n" + e.toString());
      e.printStackTrace();
      extractor.exitApp(false); return;
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
          } catch (ServiceClientException rpce ) {
            extractor.out.println("Error getting role " + roleName + " " + rpce.toString());          
          } catch (Exception e ) {
            e.printStackTrace();
          }

          if (role != null)
            csDb.saveRole(role);
        }
      } // roles.next()
      
      if(extractor.debugLog) 
        extractor.out.println("* Roles ");
    }  catch (Exception e) {
      extractor.out.println("Operation failed while filling the database from CIRCA Directory. The following error was reported:\n" + e.toString());
      e.printStackTrace();
      extractor.exitApp(false); return;
    }
  } // mode includes roles

    // End processing
    //
    //long b = System.currentTimeMillis();    
    //_log(" ** TOTAL TIME = " + (b-a));

    extractor.exitApp(true);
  }

  private static void _log(String s ) { System.out.println("============= " + s); }
}