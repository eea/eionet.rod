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
 * Original Code: Andre Karpistsenko (TietoEnator)
 */

package eionet.rod;

import java.io.*;
import java.sql.*;
import javax.servlet.http.*;

import com.tee.xmlserver.*;

/**
 * <P>Common super-class for WebROD editor servlets.</P>
 *
 * @author  Rando Valt
 * @version 1.0
 */

public abstract class ROEditServletAC extends ROServletAC {
   protected String curRecord;

/**
 *
 */
   protected String setEncoding() {
      return "UTF-8";
   }
/**
 *
 */
   protected int setMode() {
      return FORM_HANDLER;
   }
   protected static final String DROP = "DROP TABLE IF EXISTS ";
   protected static final String CREATE1 = "CREATE TABLE  ";
   protected static final String CREATE2 = " TYPE=HEAP SELECT DISTINCT ";

   void setCurrentID(String id) {
      curRecord = id;
   }
}