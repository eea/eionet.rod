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

import eionet.acl.AuthMechanism;
import eionet.acl.SignOnException;
import eionet.directory.DirServiceException;
import eionet.directory.DirectoryService;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.LogServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.RODDaoFactory;
import org.apache.commons.lang3.StringUtils;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.SPARQLRepository;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import org.openrdf.query.BindingSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pulls information from various services and saves it to DB.
 */

public class Extractor implements ExtractorConstants {

    private static final Logger LOGGER = LoggerFactory.getLogger(Extractor.class);

    public static final int ALL_DATA = 0;
    public static final int DELIVERIES = 1;
    public static final int ROLES = 2;

    public static final int PARAMS = 3;

    private static FileServiceIF fileSrv = null;
    boolean debugLog = true;
    private static PrintWriter out = null;
    private static LogServiceIF logger;
    private RODDaoFactory daoFactory;

    private static Extractor extractor;

    // For debugLog: returns the current date and time (wrapped) as String

    static {
        logger = RODServices.getLogService();
    }

    public Extractor() {

        try {
            if (daoFactory == null) {
                daoFactory = RODServices.getDbService();
            }
        } catch (Exception e) {
            // extractor.out.println("Opening connection to database failed. The following error was reported:\n" + e.toString());
            log("Opening connection to database failed. The following error was reported:\n" + e.toString());
            LOGGER.error(e.getMessage(), e);
            exitApp(false);
            // throw new ServiceException("Opening connection to database failed." );
        }
        log("Db connection ok.");

    }

    /**
     * Called when script is run from the command-line. Takes one optional argument. The mode, which can be 0-3. Assumes 0 if not
     * provided.
     *
     * @param args
     *            command-line arguments
     */
    public static void main(String[] args) {

        try {
            String mode = null;
            String userName = SYSTEM_USER;

            if (args.length == 1) {
                mode = args[0];
            } else if (args.length > 1) {
                //this is feedback to the user not debugging as this class is executed in the cmd line
                //also log to file for other applications
                LOGGER.info("Usage: Extractor [mode]");
                LOGGER.error("Usage: Extractor [mode]");
                return;
            } else {
                mode = String.valueOf(ALL_DATA);
            }

/*
            if (extractor == null) {
                extractor = new Extractor();
            }

            extractor.harvest(Integer.parseInt(mode), userName);
*/

            execute(Integer.parseInt(mode), userName);
        } catch (Exception se) {
            logger.error(se.toString());
        }
    }

    public static void execute(int mode, String userName) {
        try {
            if (extractor == null) {
                extractor = new Extractor();
            }

            extractor.harvest(mode, userName);
        } catch (Exception se) {
            logger.error(se.toString());
            RODServices.sendEmail("Error in extractor", se.toString());
        }
    }
    private String cDT() {
        Date d = new Date();
        return new String("[" + d.toString() + "] ");
    }

    // Cleanup after everything is done
    //
    public void exitApp(boolean successful) {
        log(this.cDT() + "Extractor v1.0 - " + ((successful) ? "finished succesfully." : "failed to complete."));
        if (out != null) {
            out.flush();
            out.close();
        }
    }

    /**
     * Extract the data.
     *
     * @param mode
     * @param userName
     * @throws ServiceException
     */
    public void harvest(int mode, String userName) throws ServiceException {

        // Initial set-up: create class; open the log file
        // mode, which data to harvest

        String logPath = null;
        String logfileName = null;

        try {
            fileSrv = RODServices.getFileService();

            // extractor.debugLog = RODServices.getFileService().getBooleanProperty("extractor.debugmode");
            debugLog = fileSrv.getBooleanProperty("extractor.debugmode");

            try {

                logPath = fileSrv.getStringProperty("extractor.logpath");
                logfileName = fileSrv.getStringProperty("extractor.logfilename");
                // extractor.debugLog = RODServices.getFileService().getBooleanProperty("extractor.debugmode");

            } catch (ServiceException e) {
                // use default type (XML/RPC), if not specified
                logger.warning("Unable to get logger settings from properties file. using default The following error was reported:\n"
                        + e.toString());
                // logfileName = LOG_FILE ; //"extractorlog.txt";
                // extractor.debugLog = false;
            }

        } catch (ServiceException e) {
            // KL 021009 -> cannot print out, when creating logger does not succeed
            // extractor.out.println("Unable to get log file settings from properties file, using defaults. The following error was reported:\n"
            // + e.toString());
            LOGGER.error("Unable to get settings from properties file. The following error was reported:\n" + e.toString());
            LOGGER.error(e.getMessage(), e);
            throw new ServiceException("Unable to get settings from properties file. The following error was reported:\n" + e.toString());
        }

        // KL021009
        // cannot write to the log file, if opening it does not succeed
        if (logfileName != null) {
            try {
                out = new PrintWriter(new FileWriter(logPath + logfileName, !debugLog), true);
            } catch (java.io.IOException e) {
                // using default logger instead
                LOGGER.warn("Unable to open log file for writing. using default. The following error was reported:\n"
                        + e.toString());
                LOGGER.error(e.getMessage(), e);
                RODServices.sendEmail("Error in Extractor", "Unable to open log file for writing. \n"
                        + e.toString());
            }
        }

        // Start processing
        // extractor.out.println(extractor.cDT() + "Extractor v1.1 - processing... Please wait.");
        log(cDT() + "Extractor v1.1 - processing... Please wait.");

        String userFullName = userName;
        long a = System.currentTimeMillis();

        if (!userName.equals(SYSTEM_USER)) {

            try {
                // userFullName = DirectoryService.getFullName(userName);
                userFullName = AuthMechanism.getFullName(userName);
            } catch (SignOnException se) {
                log("Error getting full name " + se.toString());
            }
        }

        String actionText = "Harvesting - ";

        /***************************************************
         * Start extracting
         ***************************************************/

        // Get delivery list from Content Registry and save it also
        if (mode == ALL_DATA || mode == DELIVERIES) {
            actionText += " deliveries ";
            extractDeliveries();
        }

        // Get roles from Eionet Directory and save them, too
        if (mode == ALL_DATA || mode == ROLES) {
            actionText += " - roles ";
            try {
                
                StringBuilder errMsg = new StringBuilder();
                String[] respRoles = daoFactory.getObligationDao().getRespRoles();

                log("Found " + respRoles.length + " roles from database");

                // remove leftovers from previous harvest
                daoFactory.getRoleDao().commitRoles();

                daoFactory.getRoleDao().backUpRoles();

                for (int i = 0; i < respRoles.length; i++) {
                    try {
                        saveRole(respRoles[i]);
                    } catch (Exception e) {
                        errMsg.append('\n').append(e.getMessage());
                    }
                } // roles.next()

                daoFactory.getRoleDao().commitRoles();

                if (debugLog) {
                    log("* Roles OK");
                }

                if (StringUtils.isNotBlank(errMsg.toString())) {
                    RODServices.sendEmail("Error in Extractor ", errMsg.toString());
                }
                
                // persons + org name

            } catch (Exception e) {
                log("Operation failed while filling the database from Eionet Directory. The following error was reported:\n"
                        + e.toString());
                LOGGER.error(e.getMessage(), e);
                exitApp(false); // return;
                throw new ServiceException(
                        "Operation failed while filling the database from Eionet Directory. The following error was reported:\n"
                                + e.toString());
            }
        } // mode includes roles

        if (mode == ALL_DATA || mode == PARAMS) {
            actionText += " -parameters";
        }

        daoFactory.getHistoryDao().logHistory("H", "0", userFullName, "X", actionText);

        long b = System.currentTimeMillis();
        log(" ** Harvesting successful TOTAL TIME = " + (b - a));

        exitApp(true);
    }

    /**
     *
     * @param s
     */
    private static void log(String s) {
        logger.debug(s);
        if (out != null) {
            out.println(s);
        }
    }

    /**
     * Get Reportnet deliveries from the Content Registry using SPARQL. It first backs up the existing delivery information, then
     * executes the SPARQL query in chunks of 1000.
     *
     * @throws ServiceException
     */
    private void extractDeliveries() throws ServiceException {
        log("Going to extract deliveries from CR");

        final String prefixDeclarations = "PREFIX dct: <http://purl.org/dc/terms/> " + "PREFIX rod: <http://rod.eionet.europa.eu/schema.rdf#> ";
        final String queryPattern = "SELECT DISTINCT ?link ?title ?locality ?obligation ?period ?date ?note WHERE { " +
                "_:subj a rod:Delivery; " +
                "rod:link ?link; " +
                "dct:title ?title; " +
                "rod:locality ?locality; " +
                "rod:obligation ?obligation; " +
                "rod:released ?date " +
                "OPTIONAL { _:subj rod:period ?period } " +
                "OPTIONAL { _:subj rod:coverageNote ?note }" +
                "}";

        final String query = prefixDeclarations + queryPattern;
        final String countQuery = prefixDeclarations + "SELECT (COUNT(*) AS ?count) {{" +  queryPattern + "}}";

        RepositoryConnection conn = null;

        try {
            String endpointURL = fileSrv.getStringProperty(FileServiceIF.CR_SPARQL_ENDPOINT);
            SPARQLRepository CREndpoint = new SPARQLRepository(endpointURL);
            CREndpoint.initialize();

            conn = CREndpoint.getConnection();

            int resultsSize = 0;
            TupleQueryResult countTupleQueryResult = conn.prepareTupleQuery(QueryLanguage.SPARQL, countQuery).evaluate();
            if (countTupleQueryResult.hasNext()) {
                BindingSet bindingSet = countTupleQueryResult.next();
                resultsSize = Integer.valueOf(bindingSet.getValue("count").stringValue());
            }

            if (resultsSize > 0) {
                HashMap<String, HashSet<Integer>> savedCountriesByObligationId = new HashMap<String, HashSet<Integer>>();

                // back up currently existing deliveries
                daoFactory.getDeliveryDao().backUpDeliveries();

                int chunkSize = 1000;
                int offset = 0;
                int saveCount = 0;
                
                while (offset <= resultsSize) {
                    String limitedQuery = query + " LIMIT " + chunkSize + " OFFSET " + offset;
                    TupleQuery q = conn.prepareTupleQuery(QueryLanguage.SPARQL, limitedQuery);
                    TupleQueryResult bindings = q.evaluate();

                    // increase offset
                    offset += chunkSize;

                    if (bindings != null && bindings.hasNext()) {
                        saveCount += daoFactory.getDeliveryDao().saveDeliveries(bindings, savedCountriesByObligationId);
                    }
                }

                log("Going to commit the " + saveCount + " T_DELIVERY rows inserted");
                daoFactory.getDeliveryDao().commitDeliveries(savedCountriesByObligationId);
                log("All inserted T_DELIVERY rows committed succesfully!");
            } else {
                log("CR sparql query call returned 0 deliveries");
            }

            log("Extracting deliveries from CR finished!");
        } catch (Exception e) {
            daoFactory.getDeliveryDao().rollBackDeliveries();
            log("Error harvesting deliveries: " + e.toString());

            log("Operation failed while filling the database from Content Registry. The following error was reported:\n"
                    + e.toString());
            LOGGER.error(e.getMessage());
            exitApp(false); // return;
            throw new ServiceException("Error getting data from Content Registry " + e.toString());
        } finally {
            try {
                conn.close();
            } catch (RepositoryException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Get countries from the database and return them as a hash-map.
     *
     * @return HashMap<String, Integer> - hashmap where key is the country name and value is the numeric code.
     * @throws ServiceException - if there is no access to the database.
     */
    public HashMap<String, Integer> getKnownCountries() throws ServiceException {

        HashMap<String, Integer> result = new HashMap<String, Integer>();
        String[][] idNamePairs = daoFactory.getSpatialDao().getCountryIdPairs();
        for (int i = 0; i < idNamePairs.length; i++) {
            result.put(idNamePairs[i][1], Integer.valueOf(idNamePairs[i][0]));
        }

        return result;
    }

    /**
     * Get the role from the directory service and save it to database.
     *
     * @param roleId
     * @throws ServiceException
     */
    public void saveRole(String roleId) throws ServiceException {

        if (roleId == null || roleId.trim().length() == 0) {
            return;
        }

        String roleName = roleId;
        Hashtable<String, Object> role = null;
        try {
            role = DirectoryService.getRole(roleName);
            log("Received role info for " + roleName + " from Directory");
        } catch (DirServiceException de) {
            logger.error("Error getting role " + roleName + ": " + de.toString());
            //RODServices.sendEmail("Error in Extractor", "Error getting role " + roleName + ": " + de.toString());
            throw new ServiceException("Error getting role " + de);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            //RODServices.sendEmail("Error in Extractor", e.toString());
            throw new ServiceException("Error getting role " + e);
        }

        if (role == null) {
            return;
        }

        daoFactory.getRoleDao().saveRole(role);
    }


}
