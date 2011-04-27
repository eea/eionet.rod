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

package eionet.rod.services.modules;

import java.util.ResourceBundle;
import java.util.MissingResourceException;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import eionet.rod.countrysrv.ExtractorConstants;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.ServiceException;

/**
 * File services implementation.
 *
 * @author  Rando Valt
 * @version 0.1
 */
public class FileServiceImpl implements FileServiceIF, ExtractorConstants {

  private ResourceBundle props;
  //private String appRoot;

/** Creates new FileServiceImpl */
  public FileServiceImpl() throws ServiceException {
     try {
        props = ResourceBundle.getBundle(PROP_FILE);
     } catch (MissingResourceException mre) {
       throw new ServiceException("Properties file " + PROP_FILE + ".properties not found");
     }

  }

/**
 *
 */
  public String getStringProperty(String propName) throws ServiceException {
    try {
       return props.getString(propName);
    } catch (MissingResourceException mre) {
       throw new ServiceException("Property value for key " + propName + " not found");
    }
  }

/**
 *
 */
  public boolean getBooleanProperty(String propName) throws ServiceException {
    try {
       String s = props.getString(propName);
       return Boolean.valueOf(s).booleanValue();
    } catch (MissingResourceException mre) {
       throw new ServiceException("Property value for key " + propName + " not found");
    }
  }

/**
 *
 */
  public int getIntProperty(String propName) throws ServiceException {
    try {
      String s = props.getString(propName);
      return Integer.parseInt(s);
    } catch (MissingResourceException mre) {
       throw new ServiceException("Property value for key " + propName + " not found");
    } catch (NumberFormatException nfe) {
       throw new ServiceException("Invalid value for integer property " + propName);
    }
  }

  /**
  *
  */
   public String[] getStringArrayProperty(String propName, String separator) throws ServiceException {
     try {
         String[] str= null;
         String s = props.getString(propName);

         if (separator == null || separator.length() == 0) {
             str = new String[1];
             str[0] = s;
         }
         else{
             char c = separator.charAt(0);
             String sep = Character.isLetterOrDigit(c) ? Character.toString(c) : "\\" + c;
             str =s.split(sep);
         }
        return str;
     } catch (MissingResourceException mre) {
        throw new ServiceException("Property value for key " + propName + " not found");
     }
   }
}


