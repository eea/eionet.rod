/*
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

import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import eionet.rod.RODUtil;
import eionet.rod.services.modules.db.dao.IClientDao;
import eionet.rod.services.modules.db.dao.IGenericDao;
import eionet.rod.services.modules.db.dao.IIssueDao;
import eionet.rod.services.modules.db.dao.IObligationDao;
import eionet.rod.services.modules.db.dao.ISpatialDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Container class for providing public services of WebROD through XML/RPC or SOAP.
 * 
 * @author Kaido Laine
 */
public class WebRODService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebRODService.class);

    private IObligationDao obligationDao;
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
            LOGGER.error(e.getMessage(), e);
        }

    }

    /**
     * Returns Activity Ids and Titles.
     * 
     * @return Vector<Map<String, String>> (contains hashtables, one for each activity)
     * @throws ServiceException
     */
    public Vector<Map<String, String>> getActivities() throws ServiceException {
        return obligationDao.getActivities();
    }

    /**
     * Returns all countries.
     * 
     * @return Vector<Map<String, String>> (contains hashtables, one for each record)
     * @throws ServiceException
     */
    public Vector<Map<String, String>> getCountries() throws ServiceException {

        return spatialDao.getCountries();
    }

    /**
     * @return Vector<Map<String, String>>
     * @throws ServiceException
     */
    public Vector<Map<String, String>> getROComplete() throws ServiceException {
        return obligationDao.getROComplete();
    }

    /**
     * @param countryId 
     * @param issueId 
     * @param client 
     * @param terminated 
     * @return Vector<Hashtable<String, String>>
     * @throws ServiceException
     */
    public Vector<Hashtable<String, String>> getObligations(String countryId, String issueId, String client, String terminated) throws ServiceException {
        boolean ccClients = false;
        if (!RODUtil.isNullOrEmpty(client) && !client.equals("-1"))
            ccClients = true;
        Vector<Hashtable<String, String>> ret = obligationDao.getObligationsVector(null, countryId, issueId, client, terminated, ccClients);
        return ret;
    }

    /**
     * @return Vector<Map<String, String>>
     * @throws ServiceException
     */
    public Vector<Map<String, String>> getRODeadlines() throws ServiceException {
        return obligationDao.getRODeadlines();
    }

    /**
     * @return Vector<Map<String, String>>
     * @throws ServiceException
     */
    public Vector<Map<String, String>> getROSummary() throws ServiceException {
        return obligationDao.getROSummary();
    }

    /**
     * @param id 
     * @return Vector<Map<String, String>>
     * @throws ServiceException
     */
    public Vector<Map<String, String>> getObligationIssues(String id) throws ServiceException {
        return issueDao.getObligationIssues(Integer.valueOf(id));
    }

    /**
     * @param id 
     * @return Vector<Map<String, String>>
     * @throws ServiceException
     */
    public Vector<Map<String, String>> getObligationOrg(String id) throws ServiceException {
        return clientDao.getObligationOrg(Integer.valueOf(id));
    }

    /**
     * @param id 
     * @return Vector<Map<String, String>>
     * @throws ServiceException
     */
    public Vector<Map<String, String>> getObligationDetail(String id) throws ServiceException {
        return obligationDao.getObligationDetail(Integer.valueOf(id));
    }

    /**
     * @param tablename 
     * @return Vector<Map<String, String>>
     * @throws ServiceException
     */
    public Vector<Map<String, String>> getTable(String tablename) throws ServiceException {
        return genericDao.getTable(tablename);
    }

    /**
     * @param tablename 
     * @return Vector<Hashtable<String, String>>
     * @throws ServiceException
     */
    public Vector<Hashtable<String, String>> getTableDesc(String tablename) throws ServiceException {
        return genericDao.getTableDesc(tablename);
    }

}
