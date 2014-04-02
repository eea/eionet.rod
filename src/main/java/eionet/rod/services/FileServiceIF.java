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
 * Original Code: Kaido Laine (TietoEnator)
 */

package eionet.rod.services;

/**
 * Methods for file operations. Property names in the props file
 * 
 * @author Rando Valt
 * @version 1.0
 */
public interface FileServiceIF {

    /**
     * DB connection URL for DB connection
     */
    public static final String DB_URL = "db.url";

    /**
     * DB driver
     */
    public static final String DB_DRV = "db.drv";

    /**
     * DB user id for the extractor
     */
    public static final String DB_USER_ID = "db.usr";

    /**
     * User PWD for DB connection
     */
    public static final String DB_USER_PWD = "db.pwd";

    /**
     * DB connection URL for DB connection
     */
    public static final String DB_TEST_URL = "db.unitest.url";

    /**
     * DB driver
     */
    public static final String DB_TEST_DRV = "db.unitest.drv";

    /**
     * DB user id for the extractor
     */
    public static final String DB_TEST_USER_ID = "db.unitest.usr";

    /**
     * User PWD for DB connection
     */
    public static final String DB_TEST_USER_PWD = "db.unitest.pwd";

    /**
     * RO namespace
     */
    public static final String RO_NAMESPACE = "rod.url.ro_namespace";

    /**
     * Namespace for RA titles
     */
    public static final String RA_NAMESPACE = "ra.namespace";

    /**
     * Namespace for countries
     */
    public static final String COUNTRY_NAMESPACE = "country.namespace";

    /**
     * Namespace for T_SPATIAL
     */
    public static final String SPATIAL_NAMESPACE = "spatial.namespace";

    /**
     * Namespace for T_ISSUE
     */
    public static final String ISSUE_NAMESPACE = "issue.namespace";

    /**
     * Namespace for T_CLIENT
     */
    public static final String ORGANISATION_NAMESPACE = "organisation.namespace";

    /**
     * Namespace for Delivery
     */
    public static final String DELIVERY_NAMESPACE = "delivery.namespace";

    /**
     * List of delivery namespaces
     */
    public static final String DELIVERY_NAMESPACES = "delivery.namespaces";

    /**
     * namespace separator used for separating namespaces in DELIVERY_NAMESPACES
     */
    public static final String NAMESPACE_SEPARATOR = "namespace.separator";

    /**
     * Content Registry SPARQL endpoint
     */
    public static final String CR_SPARQL_ENDPOINT = "cr.sparql.endpoint";

    /**
     * Title Predicate for rdf type
     */
    public static final String PRED_RDF_TYPE = "pred.rdf-type";

    /**
     * Percent of reporting frequence for approaching deadlines
     */
    public static final String PERCENT_OF_FREQ = "percent.of.freq";

    /**
     * Datefile for DeadlinesDaemon
     */
    public static final String DEADLINES_DAEMON_DATEFILE = "deadlines.daemon.datefile";

    /**
     * DPSIR values Excel file
     */
    public static final String DPSIR_VALUES_FILE = "dpsir.values.file";

    /**
     * XML-RPC server URL for UNS subscription
     */
    public static final String UNS_XMLRPC_SERVER_URL = "uns.xml.rpc.server.url";

    /**
     * Channel name for UNS subscription method
     */
    public static final String UNS_CHANNEL_NAME = "uns.channel.name";

    /**
     * Event type predicate for UNS subscription method
     */
    public static final String UNS_EVENTTYPE_PREDICATE = "uns.eventtype.predicate";

    /**
     * Country predicate for UNS subscription method
     */
    public static final String UNS_COUNTRY_PREDICATE = "uns.country.predicate";

    /**
     * Issue predicate for UNS subscription method
     */
    public static final String UNS_ISSUE_PREDICATE = "uns.issue.predicate";

    /**
     * Obligation predicate for UNS subscription method
     */
    public static final String UNS_OBLIGATION_PREDICATE = "uns.obligation.predicate";

    /**
     * Instrument predicate for UNS subscription method
     */
    public static final String UNS_INSTRUMENT_PREDICATE = "uns.instrument.predicate";

    /**
     * Organisation predicate for UNS subscription method
     */
    public static final String UNS_ORGANISATION_PREDICATE = "uns.organisation.predicate";

    /**
     * UNS subscriptions URL
     */
    public static final String UNS_MY_SUBSCRIPTIONS_URL = "uns.my.subscriptions.url";

    /**
     * username for UNS subscriptions
     */
    public static final String UNS_USERNAME = "uns.username";

    /**
     * password for UNS subscriptions
     */
    public static final String UNS_PWD = "uns.pwd";

    /**
     * UNS xmlrpc function that is called for making subscription
     */
    public static final String UNS_MAKE_SUBSCRIPTION = "uns.make.subsription.function";

    /**
     * UNS xmlrpc function that is called for sending notifications
     */
    public static final String UNS_SEND_NOTIFICATION = "uns.send.notification.function";

    /**
     * Undo - number of records per page
     */
    public static final String UNDO_STEP = "undo.step";

    /**
     * Undo - number of records per page
     */
    public static final String HISTORY_UNDO_STEP = "history.undo.step";

    /**
     * Returns String type property from the properties file
     * 
     * @param propName
     * @return String
     * @throws ServiceException
     */
    public String getStringProperty(String propName) throws ServiceException;

    /**
     * Returns String type property. If no property returns default value.
     * @param propName property name
     * @param defaultValue default value if no property
     * @return property value
     * @throws ServiceException if reading file does not succeed
     */
    public String getStringProperty(String propName, String defaultValue) throws ServiceException;


    /**
     * Returns boolean type property from the properties file
     * 
     * @param propName
     * @return boolean
     * @throws ServiceException
     */
    public boolean getBooleanProperty(String propName) throws ServiceException;

    /**
     * Returns int type property from the properties file
     * 
     * @param propName
     * @return int
     * @throws ServiceException
     */
    public int getIntProperty(String propName) throws ServiceException;

    /**
     * Returns String arrray property from the properties file
     * 
     * @param propName
     * @param separator
     * @return String[]
     * @throws ServiceException
     */
    public String[] getStringArrayProperty(String propName, String separator) throws ServiceException;

    /**
     * returns long value of property.
     * @param propName property name
     * @param defaultValue default value for case if property not specified
     * @return property value
     * @throws ServiceException if reading fails
     */
    long getLongProperty(String propName, long defaultValue) throws  ServiceException;

    /**
     * Get property value of time in milliseconds presented by time value and unit suffix (1h, 30m, 15s etc).
     *
     * @param key property name
     * @param defaultValue default value to be returned if file does not contain the property
     * @return propertyValue
     */
    public Long getTimePropertyMilliseconds(final String key, Long defaultValue);

    }
