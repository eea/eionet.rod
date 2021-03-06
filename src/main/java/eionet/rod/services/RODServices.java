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

import eionet.rod.services.modules.EmailServiceImpl;
import eionet.rod.services.modules.FileServiceImpl;
import eionet.rod.services.modules.db.dao.RODDaoFactory;
import eionet.rod.services.modules.db.dao.mysql.MySqlDaoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy class for accessing CountrySrv services.
 * 
 * @author Kaido Laine
 */
public class RODServices {

    private static final Logger LOGGER = LoggerFactory.getLogger(RODServices.class);
    private static RODDaoFactory daoFactory = null;
    private static FileServiceIF _fSrv = null;
    private static EmailServiceIF emailService = null;

    /**
     * Instance of RODDaoFactory.
     * @return RODDaoFactory
     * @throws ServiceException 
     */
    public static RODDaoFactory getDbService() throws ServiceException {
        if (daoFactory == null)
            daoFactory = new MySqlDaoFactory();
        return daoFactory; // new DbServiceImpl();
    }

    /**
     * Instance of FileServiceIF (reads from props file).
     * @return FileServiceIF
     * @throws ServiceException 
     */
    public static FileServiceIF getFileService() throws ServiceException {
        if (_fSrv == null)
            _fSrv = new FileServiceImpl();

        return _fSrv; // new FileServiceImpl();
    }

    /**
     * new instance of emailservice.
     * @return
     * @throws ServiceException
     */
    public static EmailServiceIF getEmailService()  {
        if (emailService == null) {
            emailService = new EmailServiceImpl();
        }

        return emailService;
    }

    /**
     * send email to sys admins specified in the props file.
     * @param subject
     * @param msg
     */
    public static void sendEmail(String subject, String msg) {
        
        try {
            getEmailService().sendToSysAdmin(subject, msg);
        } catch (Exception e) {
            LOGGER.error("Sending email failed ", e);
        }
        
    }
}
