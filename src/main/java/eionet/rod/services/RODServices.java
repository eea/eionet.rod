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
import eionet.rod.services.modules.Log4jLoggerImpl;
import eionet.rod.services.modules.db.dao.RODDaoFactory;
import eionet.rod.services.modules.db.dao.mysql.MySqlDaoFactory;

/**
 * Proxy class for accessing CountrySrv services.
 * 
 * @author Kaido Laine
 */
public class RODServices {

    private static LogServiceIF _logSrv = null;
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
     * Logging Service.
     * 
     * @return LogServiceIF
     */
    public static LogServiceIF getLogService() {
        if (_logSrv == null) {
            try {
                _logSrv = new Log4jLoggerImpl();
            } catch (Exception se) {
                _logSrv = new StderrLogger();
            }
        }

        return _logSrv;
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
     * Stderr logger for a case, if no logger module is available.
     */
    static class StderrLogger implements LogServiceIF {

        private void out(String severity, Object msg, Throwable t) {
            System.err.println("<" + severity + "> " + msg);
            if (t != null)
                t.printStackTrace(System.err);
        }

        public boolean enable(int level) {
            return true;
        }

        public void debug(Object msg) {
            debug(msg, null);
        }

        public void debug(Object msg, Throwable t) {
            out("DEBUG", msg, t);
        }

        public void info(Object msg) {
            info(msg, null);
        }

        public void info(Object msg, Throwable t) {
            out("INFO", msg, t);
        }

        public void warning(Object msg) {
            warning(msg, null);
        }

        public void warning(Object msg, Throwable t) {
            out("WARNING", msg, t);
        }

        public void error(Object msg) {
            if (msg instanceof Throwable) {
                Throwable t = (Throwable) msg;
                error(t.getMessage(), t);
            } else {
                error(msg, null);
            }
        }

        public void error(Object msg, Throwable t) {
            out("ERROR", msg, t);
        }

        public void fatal(Object msg) {
            if (msg instanceof Throwable) {
                Throwable t = (Throwable) msg;
                fatal(t.getMessage(), t);
            } else {
                fatal(msg, null);
            }
        }

        public void fatal(Object msg, Throwable t) {
            out("FATAL", msg, t);
        }
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
            getLogService().error("SEnding email failed " + e);
        }
        
    }
}
