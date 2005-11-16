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

import java.util.Iterator;
import java.io.File;


/**
 * Methods for file operations.
 * Property names in the props file
 *
 * @author  Rando Valt
 * @version 1.0
 */
public interface FileServiceIF  {

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
  * User PWD  for DB connection 
  */
  public static final String DB_USER_PWD = "db.pwd";

  /**
  * Default type for remote services
  */
  public static final String REMOTE_SRV_TYPE = "remote.services.type";



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
  * URL for ContReg service
  */
  public static final String CONTREG_SRV_URL = "contreg.service.url";


  /**
  * Coverage Predicate for ContReg service
  */
  public static final String CONTREG_COVERAGE_PREDICATE = "contreg.coverage.predicate";

  /**
  * Title Predicate for ContReg service
  */
  public static final String CONTREG_TITLE_PREDICATE = "contreg.title.predicate";

  /**
  * Date predicate for ContReg service
  */
  public static final String CONTREG_DATE_PREDICATE = "contreg.date.predicate";

  /**
  * Type predicate for ContReg service
  */
  public static final String CONTREG_TYPE_PREDICATE = "contreg.type.predicate";

  /**
  * Title predicate for ContReg service
  */
  public static final String CONTREG_IDENTIFIER_PREDICATE = "contreg.identifier.predicate";

  /**
  * Format predicate for ContReg service
  */
  public static final String CONTREG_FORMAT_PREDICATE = "contreg.format.predicate";

  /**
  * Title Predicate for rdf type
  */
  public static final String PRED_RDF_TYPE = "pred.rdf-type";
  
  /**
   * Percent of reporting frequence for approaching deadlines
   */
   public static final String PERCENT_OF_FREQ = "percent.of.freq";

  /**
  * Returns String type property from the properties file
  */
  public String getStringProperty(String propName) throws ServiceException;

  /**
  * Returns boolean type property from the properties file
  */
  public boolean getBooleanProperty(String propName) throws ServiceException;

  /**
  * Returns int type property from the properties file
  */
  public int getIntProperty(String propName) throws ServiceException;

}

