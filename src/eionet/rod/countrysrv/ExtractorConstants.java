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


package eionet.rod.countrysrv;

import com.tee.uit.client.ServiceClientIF;

/**
* Constant values for external services, methods exc
*/
public interface ExtractorConstants {

  //default type for the remote services
  public final int SERVICE_CLIENT_TYPE = ServiceClientIF.CLIENT_TYPE_XMLRPC;

  public final String PROP_FILE = "rod";
  public final String LOG_FILE = "extractorlog.txt";
  
  public final String ROD_SRV_NAME = "WebRODService";
  public final String ROD_GETCOUNTRIES_METHOD = "getCountries";  
  public final String ROD_GETACTIVITIES_METHOD = "getActivityDetails";  
  public final String ROD_GETISSUES_METHOD = "getIssues";  
  public final String ROD_GETPARAMGROUPS_METHOD = "getParamGroups";  
  public final String ROD_GETISSUELINKS_METHOD = "getIssueLinks";  
  public final String ROD_GETGROUPLINKS_METHOD = "getGroupLinks";    
  public final String ROD_GETDEADLINE_METHOD = "getDeadline";    

  public final String CONTREG_SRV_NAME = "ContRegService";
  public final String CONTREG_GETENTRIES_METHOD = "getEntries";    

  public final String DIRECTORY_SRV_NAME = "DirectoryService";
  public final String DIRECTORY_GETROLE_METHOD = "getRole";  


}