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

import java.sql.*;

import com.tee.util.*;
import com.tee.xmlserver.*;

/**
 * <P><CODE>com.tee.xmlserver.SaveHandler</CODE> specialization for WebROD package. 
 * Contains some common routines and defines RO_pool specific <CODE>getRecordID()</CODE> method.</P>
 *
 * @author  Rando Valt
 * @version 1.0
 */

public class ROHandler extends SaveHandler {
   protected ROEditServletAC servlet;
   protected String id; // holds current record's id
   
/**
 * Overrides superclass method to retrieve the current records ID
 */
   protected String getRecordID(SQLGenerator gen) throws SQLException {
      if (gen.getState() == INSERT_RECORD) {
         return "" + getDBInt("SELECT LAST_INSERT_ID()");
      }
      else  {
         String pkField = gen.getPKField();
         return pkField != null ? gen.getFieldValue(pkField) : null;
      }
   }
   
   public static String str2Date(String date) {
      int len = date.length();
      if (len == 0)
         return "";
         
      //formats the input string in the form dd/mm/yyyy to MySQL date format yyyy-mm-dd
      //
      //                 0123456789
      // WebROD format: dd/mm/yyyy
      // MySQL format:   yyyy-mm-dd
      //
      
      if (len == 10) {
         char d1 = date.charAt(0);
         char d2 = date.charAt(1);
         char m1 = date.charAt(3);
         char m2 = date.charAt(4);
         char y1 = date.charAt(6);
         char y2 = date.charAt(7);
         char y3 = date.charAt(8);
         char y4 = date.charAt(9);
         char s1 = date.charAt(2);
         char s2 = date.charAt(5);
         
         if (Character.isDigit(d1) &&
             Character.isDigit(d2) &&
             Character.isDigit(m1) &&
             Character.isDigit(m2) &&
             Character.isDigit(y1) &&
             Character.isDigit(y2) &&
             Character.isDigit(y3) &&
             Character.isDigit(y4) &&
             s1 == '/' && s2 == '/') {
            StringBuffer ret = new StringBuffer(10);
            ret.insert(0, y1)
               .insert(1, y2)
               .insert(2, y3)
               .insert(3, y4)
               .insert(4, '-')
               .insert(5, m1)
               .insert(6, m2)
               .insert(7, '-')
               .insert(8, d1)
               .insert(9, d2);
               
            return ret.toString();      
         }
      }

      Logger.log("Invalid date expression: " + date);
      
      return "";
   }

   public static final String getID(String value) {
      int pos = value.indexOf(':');
      if (pos != -1)
         return value.substring(0, pos);
      else
         return value;
   }

   public static final String getText(String value) {
      int pos = value.indexOf(':');
      if (pos != -1)
         return value.substring(pos + 1);
      else
         return "";
   }

   public static final String getUnitID(String value) {
      int pos = value.indexOf(':');
      int lpos = value.lastIndexOf(':');
      if (pos != lpos)
         return value.substring(lpos + 1);
      else
         return "";
   }
/**
 *
 */
   protected final void setIntValue(SQLGenerator gen, String fldName) {
      String fldValue = gen.getFieldValue(fldName);
      if (fldValue != null && fldValue.length() == 0)
         gen.setFieldExpr(fldName, "NULL");
   }
/**
 *
 */
   protected final void setDateValue(SQLGenerator gen, String fldName) {
      String fldValue = gen.getFieldValue(fldName);
      if ( !Util.nullString(fldValue) )
         gen.setField(fldName, str2Date(fldValue));
      else
         gen.setFieldExpr(fldName, "NULL");
   }
/**
 * Performs single integer value database query.
 */
   protected final int getDBInt(String sqlStmt) {
      int res = 0;
      Statement stmt = null;

      try {
         stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sqlStmt);
         rs.next();
         
         res = rs.getInt(1);
         
         rs.close();
      } catch (SQLException e) {
         addErrorInfo(e, sqlStmt);            
      } finally {
         try { if (stmt != null) stmt.close(); } catch (SQLException e1) {}
      }

      return res;
   }
   
/**
 * Set commit mode to DOCUMENT_COMMIT
 */
   public int setCommitLevel() {
      return DOCUMENT_COMMIT;
   }

   protected ROHandler(ROEditServletAC servlet) {
      super();
      this.servlet = servlet;
      id = null;
      servlet.setCurrentID(null);
   }
   
   protected ROHandler(DBPoolIF dbPool, DBVendorIF dbVendor) {
      super(dbPool, dbVendor);
      servlet = null;
   }
}