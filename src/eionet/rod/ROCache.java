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

import java.util.Date;
import java.sql.*;

import com.tee.xmlserver.*;

import com.tee.util.Util;

/**
 * <P>WebROD specific <CODE>com.tee.xmlserver.AppCacheIF</CODE> implementation.</P>
 *
 * @author  Rando Valt
 * @version 1.0
 */

public class ROCache implements AppCacheIF {
/**
 * Queries from the EMS database the modification time for particular table.
 *
 * @param String  the name of the database table.
 *
 * @return long last modification time of of the given database table in milliseconds 
 * since January 1, 1970, 00:00:00 GMT
 */
   public long tblLastModified(String tblName) {
      long ret = -1;

    /*      
      if (dbPool == null) {
         dbPool = XDBApplication.getDBPool();
      }

      Connection dbCon = dbPool.getConnection();
      
      try {
         Statement stmt = dbCon.createStatement();
         // DBG
         if (Logger.enable(5))
            Logger.log("tblLastModified: " + sqlStmt + Util.strLiteral(tblName));
         //
         ResultSet rs = stmt.executeQuery(sqlStmt + Util.strLiteral(tblName));
         // DBG
         if (Logger.enable(5))
            Logger.log("tblLastModified: executeQuery done");
         //
         
         if (rs.next() == false) // if the resultset was empty
            return ret;
            
         Date modTime = rs.getDate(1);
         ret = modTime.getTime();
         // DBG
         if (Logger.enable(5))
            Logger.log("tblLastModified: ret = " + ret);
         //
         rs.close();
         stmt.close();
         dbCon.close();
      } catch (SQLException e) {
         // we log the error, method returns true
         Logger.log("EmsCache.isTblModified: quering database failed", e);
      }
*/      
      ret = 10;
      return ret; 
   }
   
   public ROCache() {
   }
/**
 * Public contructor for using the processor class outside of XSQLServlet framework.
 */
   public ROCache(DBPoolIF dbPool) {
      this.dbPool = dbPool;
   }
   //
   private DBPoolIF dbPool = null;
}
