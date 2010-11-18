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
 * The Original Code is "EINRC-4 / WebROD Project".
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

import java.util.Vector;

import eionet.rod.RODUtil;
import eionet.rod.services.modules.db.dao.IClientDao;
import eionet.rod.services.modules.db.dao.IGenericDao;
import eionet.rod.services.modules.db.dao.IIssueDao;
import eionet.rod.services.modules.db.dao.IObligationDao;
import eionet.rod.services.modules.db.dao.ISpatialDao;
import eionet.rod.services.modules.db.dao.mysql.ObligationMySqlDao;

/**
 * Container class for providing public services of WebROD
 * through XML/RPC || SOAP
 * <P>
 * @author Kaido Laine
 */
public  class WebRODService {

	private static LogServiceIF logger = RODServices.getLogService();
	
  	private IObligationDao obligationDao ;
  	private ISpatialDao spatialDao;
  	private IIssueDao issueDao;
  	private IClientDao clientDao;
  	private IGenericDao genericDao;


   public WebRODService() { 
		try {
			obligationDao = RODServices.getDbService().getObligationDao();
			clientDao = RODServices.getDbService().getClientDao();
			spatialDao = RODServices.getDbService().getSpatialDao();
			issueDao = RODServices.getDbService().getIssueDao();
			genericDao = RODServices.getDbService().getGenericlDao();
		} catch (ServiceException e) {
			logger.fatal(e);
		}		

   }
  
  /**
	* Returns Activity Ids and Titles
  * @return Vector (contains hashtables, one for each activity)
  * @throw ServiceException  
	*/
  public Vector getActivities() throws ServiceException {
    return obligationDao.getActivities();
  }

  /**
  * Returns all countries
  * @return Vector (contains hashtables, one for each record)
  * @throw ServiceException  
	*/
  public Vector getCountries() throws ServiceException {

    return spatialDao.getCountries();
  }
  
  public Vector getROComplete() throws ServiceException {
	  return obligationDao.getROComplete();
  }

  public Vector getObligations(String countryId, String issueId, String client, String terminated) throws ServiceException {
	  boolean ccClients = false;
	  if(!RODUtil.isNullOrEmpty(client) && !client.equals("-1"))
		  ccClients = true;
	  Vector ret = obligationDao.getObligationsVector(null, countryId, issueId, client, terminated, ccClients); 
	  return ret;
  }
  public Vector getRODeadlines() throws ServiceException {
    return obligationDao.getRODeadlines();
  }
  public Vector getROSummary() throws ServiceException {
   return obligationDao.getROSummary();
  }
  public Vector getObligationIssues(String id) throws ServiceException {
      return issueDao.getObligationIssues(Integer.valueOf(id));
  }
  public Vector getObligationOrg(String id) throws ServiceException {
      return clientDao.getObligationOrg(Integer.valueOf(id));
  }
  public Vector getObligationDetail(String id) throws ServiceException {
      return obligationDao.getObligationDetail(Integer.valueOf(id));
  }
  public Vector getTable(String tablename) throws ServiceException {
      return genericDao.getTable(tablename);
  }
  public Vector getTableDesc(String tablename) throws ServiceException {
      return genericDao.getTableDesc(tablename);
  }
  
}
