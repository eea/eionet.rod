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

import eionet.rod.services.modules.DbServiceImpl;
import eionet.rod.services.modules.FileServiceImpl;
import eionet.rod.services.modules.Log4jLoggerImpl;

/**
 * Proxy class for accessing CountrySrv services
 * @author Kaido Laine
 */

public class RODServices  {


private static LogServiceIF   _logSrv = null;
  /**
  * Instance of DirectoryService
  * @DEPRECATED
  */
	/*public static DirectoryServiceIF getDirectoryService() throws ServiceException {
  	return new DirectoryServiceImpl();
  } */

  /**
  * Instance of DbServiceIF
  */
	public static DbServiceIF getDbService() throws ServiceException {
  	return new DbServiceImpl();
  }

  /**
  * Instance of FileServiceIF (reads from props file)
  */
 	public static FileServiceIF getFileService() throws ServiceException {
  	return new FileServiceImpl();
  }


  /**
   * Logging Service
   * @return LogServiceIF
   */
  public static LogServiceIF getLogService() {
    if (_logSrv == null)  {
      try {
        _logSrv = new Log4jLoggerImpl();
      } catch (Exception se) {
        _logSrv = new StderrLogger();
      }
    }
    
    return _logSrv;
  }


  // stderr logger for a case, if no logger module is available
  static class StderrLogger implements LogServiceIF {
    
    private void out(String severity, Object msg, Throwable t) {
      System.err.println("<" + severity + "> " + msg);
      if (t != null)
        t.printStackTrace(System.err);
    }
    
    public boolean enable(int level)              { return true; }
    
    public void debug(Object msg)                 { debug(msg, null); }
    public void debug(Object msg, Throwable t)    { out("DEBUG", msg, t); }
    
    public void info(Object msg)                  { info(msg, null); }
    public void info(Object msg, Throwable t)     { out("INFO", msg, t); }
    
    public void warning(Object msg)               { warning(msg, null); }
    public void warning(Object msg, Throwable t)  { out("WARNING", msg, t); }
    
    public void error(Object msg)                 { error(msg, null); }
    public void error(Object msg, Throwable t)    { out("ERROR", msg, t); }
    
    public void fatal(Object msg)                 { fatal(msg, null); }
    public void fatal(Object msg, Throwable t)    { out("FATAL", msg, t); }
  }

}

 