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

package eionet.rod.countrysrv.services;

import eionet.rod.countrysrv.services.modules.DbServiceImpl;
import eionet.rod.countrysrv.services.modules.FileServiceImpl;

/**
 * Proxy class for accessing CountrySrv services
 * @author Kaido Laine
 */

public class CSServices  {


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

}

 