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
import eionet.rod.services.modules.db.dao.RODDaoFactory;
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
	private  RODDaoFactory daoFactory;

	private static Extractor extractor;

	// For debugLog: returns the current date and time (wrapped) as String
	//

	static {
		logger = RODServices.getLogService();
	}

	public Extractor() {

		try {
			if (daoFactory==null) {

				daoFactory = RODServices.getDbService();
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
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			String mode = null;
			String userName = SYSTEM_USER;

			if (args.length==1){
				mode = args[0];
			}
			else if (args.length > 1 ){
				System.out.println("Usage: Extractor [-mode]");
				return;
			}
			else{
				mode = String.valueOf(ALL_DATA);
			}

			if (extractor == null){
				extractor = new Extractor();
			}

			extractor.harvest(Integer.parseInt(mode), userName);
		}
		catch (Exception se ) {
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
		// mode, which data to harvest

		String logPath = null;
		String logfileName = null;
		ServiceClientIF crClient = null;
		ServiceClientIF ldap = null;
		String crUrl = null;
		String wrUrl = null;
		String raNs=null, countryNs=null, predRdfType=null, separator=null;
		String[] deliveryNs=null;
		
		int remoteSrvType = SERVICE_CLIENT_TYPE;

		//prefix for RA ID to get the data from CR KL040303
		String raIdPref=null;

		try {
			fileSrv = RODServices.getFileService();

			raNs = fileSrv.getStringProperty( FileServiceIF.RA_NAMESPACE );
			raIdPref = fileSrv.getStringProperty( FileServiceIF.RO_NAMESPACE );
			countryNs = fileSrv.getStringProperty( FileServiceIF.COUNTRY_NAMESPACE);
			separator = fileSrv.getStringProperty( FileServiceIF.NAMESPACE_SEPARATOR);
			deliveryNs = fileSrv.getStringArrayProperty( FileServiceIF.DELIVERY_NAMESPACES, separator);
			predRdfType = fileSrv.getStringProperty( FileServiceIF.PRED_RDF_TYPE);
			//extractor.debugLog = RODServices.getFileService().getBooleanProperty("extractor.debugmode");
			debugLog = RODServices.getFileService().getBooleanProperty("extractor.debugmode");

			try {
				remoteSrvType = fileSrv.getIntProperty( fileSrv.REMOTE_SRV_TYPE );

				logPath = fileSrv.getStringProperty("extractor.logpath");
				logfileName = fileSrv.getStringProperty("extractor.logfilename");
				//extractor.debugLog = RODServices.getFileService().getBooleanProperty("extractor.debugmode");

			}
			catch (ServiceException e) {
				//use default type (XML/RPC), if not specified
				logger.warning("Unable to get logger settings from properties file. using default The following error was reported:\n" + e.toString());
				//logfileName = LOG_FILE ; //"extractorlog.txt";
				//extractor.debugLog = false;
			}

		}
		catch (ServiceException e) {
			//KL 021009 -> cannot print out, when creating logger does not succeed
			//extractor.out.println("Unable to get log file settings from properties file, using defaults. The following error was reported:\n" + e.toString());
			logger.error("Unable to get settings from properties file. The following error was reported:\n" + e.toString());
			e.printStackTrace();
			throw new ServiceException("Unable to get settings from properties file. The following error was reported:\n" + e.toString());
		}

		//KL021009
		// cannot write to the log file, if opening it does not succeed
		if (logfileName != null){
			try {
				out = new PrintWriter(new FileWriter(logPath + logfileName, !debugLog), true);
			} catch (java.io.IOException e) {
				//using default logger instead
				logger.warning("Unable to open log file for writing. using default. The following error was reported:\n" + e.toString());
				e.printStackTrace();
			}
		}

		// Start processing
		//extractor.out.println(extractor.cDT() + "Extractor v1.1 - processing... Please wait.");
		log(cDT() + "Extractor v1.1 - processing... Please wait.");

		String[][] raData;
		String userFullName = userName;
		long a = System.currentTimeMillis();
		
		if (!userName.equals(SYSTEM_USER)){
			
			try {
				//userFullName = DirectoryService.getFullName(userName);
				userFullName = AuthMechanism.getFullName(userName);
			} catch (SignOnException se ) {
				log("Error getting full name " + se.toString());
			}
		}

		String actionText = "Harvesting - ";

		/***************************************************
		 * Start extracting
		 ***************************************************/

		// Get delivery list from Content Registry and save it also
		if (mode == ALL_DATA || mode == DELIVERIES  ) {
			
			actionText += " deliveries ";
			extractDeliveries(remoteSrvType);
		}

		// Get roles from CIRCA Directory and save them, too
		if (mode == ALL_DATA || mode == ROLES ) {
			actionText += " - roles ";
			try  {
				Set roleSet = new HashSet();
				String[] respRoles = daoFactory.getObligationDao().getRespRoles();

				log("Found " + respRoles.length + " roles from database");

				//remove leftovers from previous harvest
				daoFactory.getRoleDao().commitRoles();

				daoFactory.getRoleDao().backUpRoles();

				for(int i = 0; i < respRoles.length; i++) {

					saveRole(respRoles[i]);
				} // roles.next()

				daoFactory.getRoleDao().commitRoles();

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
		}

		daoFactory.getHistoryDao().logHistory("H", "0",  userFullName, "X", actionText);
		
		long b = System.currentTimeMillis();
		log(" ** Harvesting successful TOTAL TIME = " + (b-a));

		exitApp(true);
	}

	/**
	 * 
	 * @param s
	 */
	private static void log(String s ) {
		logger.debug( s );
		if (out!=null){
			out.println( s );
		}
	}

	/**
	 * 
	 * @param remoteSrvType
	 * @throws ServiceException
	 */
	private void extractDeliveries(int remoteSrvType) throws ServiceException{
		
		log("Going to extract deliveries from CR");
		
		// load CR service URL from properties file
		String crUrl = null;
		try {
			//crUrl =  fileSrv.getStringProperty( FileServiceIF.CONTREG_SRV_URL );
			crUrl = "http://ww010646:8080/cr/xmlrpc";
		}
		catch (Exception e) {
			log("Opening connection to Content Registry failed. The following error was reported:\n" + e.toString());
			exitApp(false); //return;
			throw new ServiceException("No URL to ContReg service specified in the props file");
		}
		
		// initialize CR service client
		ServiceClientIF crClient = null;
		try {
			crClient = ServiceClients.getServiceClient(CONTREG_SRV_NAME, crUrl, remoteSrvType );
		}
		catch (Exception e) {
			log("Opening connection to Content Registry failed. The following error was reported:\n" + e.toString());
			e.printStackTrace();
			exitApp(false); //return;
			throw new ServiceException("Error creating a client top ContReg service at: " + crUrl);
		}

		try {
			int chunkSize = 1000;
			int maxLoops = 30;
			
			Vector remoteCallParams = new Vector();
			remoteCallParams.add(null); // here goes result set page number
			remoteCallParams.add(chunkSize); // here goes result set page size

			HashMap existingCountryIdsByNames = getKnownCountries();			
			HashMap savedCountriesByObligationId = new HashMap();
			
			log(existingCountryIdsByNames.size() + " existing countries found before calling CR");
			
			// back up currently existing deliveries
			daoFactory.getDeliveryDao().backUpDeliveries();
			
			HashSet deliveredCountryIds = new HashSet();
			try {

				int j;
				boolean noMoreDeliveries = false;
				int saveCount = 0;
				for (j=0; noMoreDeliveries==false && j<maxLoops; j++){

					remoteCallParams.setElementAt(new Integer(j + 1), 0);

					log("Making " + CONTREG_GETDELIVERIES_METHOD + remoteCallParams + " call to CR");
					
					Vector deliveries = (Vector)crClient.getValue(CONTREG_GETDELIVERIES_METHOD, remoteCallParams);
					if (deliveries!=null && !deliveries.isEmpty()){
						
						log(CONTREG_GETDELIVERIES_METHOD + remoteCallParams + " call returned " +
								deliveries.size() + " deliveries, going to save them");

						saveCount += daoFactory.getDeliveryDao().saveDeliveries(deliveries, existingCountryIdsByNames, savedCountriesByObligationId);
					}
					else{
						noMoreDeliveries = true;
						log(CONTREG_GETDELIVERIES_METHOD + remoteCallParams + " call returned 0 deliveries");
					}

					// sleep a little to ease the load on CR
					Thread.sleep(1000);
				}
				
				log("Going to commit the " + saveCount + " T_DELIVERY rows inserted");
				
				daoFactory.getDeliveryDao().commitDeliveries(savedCountriesByObligationId);

				log("All inserted T_DELIVERY rows committed succesfully!");
				log("Extracting deliveries from CR finished!");
			}
			catch (Exception se){
				
				daoFactory.getDeliveryDao().rollBackDeliveries();
				log ("Error harvesintg deliveries: " + se.toString());
			}
		}
		catch (Exception e) {
			log("Operation failed while filling the database from Content Registry. The following error was reported:\n" + e.toString());
			e.printStackTrace();
			exitApp(false); //return;
			throw new ServiceException("Error getting data from Content Registry " + e.toString());
		}
	}

	/**
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public HashMap getKnownCountries() throws ServiceException{
		
		HashMap result = new HashMap();
		String[][] idNamePairs = daoFactory.getSpatialDao().getCountryIdPairs();
		for (int i=0; i<idNamePairs.length; i++){
			result.put(idNamePairs[i][1], Integer.valueOf(idNamePairs[i][0]));
		}
		
		return result;
	}

	/**
	 * 
	 * @param roleId
	 * @throws ServiceException
	 */
	public  void saveRole(String roleId) throws ServiceException {

		if (roleId==null || roleId.trim().length()==0)
			return;

		String roleName =  roleId;
		Hashtable role = null;
		try{
			role = DirectoryService.getRole(roleName);
			log("Received role info for " + roleName + " from Directory");
		}
		catch (DirServiceException de ){
			logger.error("Error getting role " + roleName + ": " + de.toString());
		}
		catch (Exception e ){
			e.printStackTrace();
		}

		if (role==null)
			return;

		daoFactory.getRoleDao().saveRole(role);
	}

	/*
	 * 	Method validates and merges deliveries lists.
	 * 	Referrals rows will be added to deliveries vector except
	 * 	if the referral id already exisits in deliveries vector.
	 *
	 *	@param deliveries list of Content Registry deliveries
	 *	@param referral list of Content Registry referrals
	 *
	 * 	@return Vector - merged vectors without duplicates
	 */
	private Vector validateDeliveries(Vector allDeliveries, Vector addDeliveries){
		boolean bAddDelivery=true;

		if (allDeliveries == null) return addDeliveries;
		if (addDeliveries == null) return allDeliveries;

		for (int ii=0; ii<addDeliveries.size(); ii++){
			bAddDelivery=true;
			//deliveryKEY:STRING(url), VALUE:HASH (metadata)
			Hashtable addDelivery = (Hashtable)addDeliveries.elementAt(ii);
			if(addDelivery != null && addDelivery.size() > 0){
				//Hash contains only one member that's delivery data
				String addDeliveryId = (String)(addDelivery.keys().nextElement());
				for (int jj=0; jj<allDeliveries.size(); jj++){
					//deliveryKEY:STRING(url), VALUE:HASH (metadata)
					Hashtable delivery = (Hashtable)allDeliveries.elementAt(jj);
					if(delivery != null && delivery.size() > 0){
						String deliveryId = (String)(delivery.keys().nextElement());
						if (addDeliveryId.equals(deliveryId)){
							bAddDelivery=false;
							break;
						}
					}
				}
				if (bAddDelivery){
					allDeliveries.add(addDelivery);
				}
			}
		}

		return allDeliveries;
	}
}
