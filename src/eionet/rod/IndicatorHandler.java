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
 * The Original Code is "EINRC-7 / OPS Project".
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

package eionet.rod;

import java.io.*;
import javax.servlet.http.*;

import com.tee.util.*;
import com.tee.xmlserver.*;
import com.tee.uit.security.AccessControlListIF;


public class IndicatorHandler extends ROHandler {

   public int setCommitLevel() {
      return AUTO_COMMIT;
   }

   public IndicatorHandler(ROEditServletAC servlet) {
      super(servlet);
   }
   protected boolean sqlReady(SQLGenerator gen, String context) {

       String userName = this.user.getUserName();
       boolean ok = false;
       

      try {
        AccessControlListIF acl = servlet.getAcl(Constants.ACL_RA_NAME);
        ok = acl.checkPermission(userName, Constants.ACL_INSERT_PERMISSION) ||
          acl.checkPermission(userName, Constants.ACL_UPDATE_PERMISSION) ||
          acl.checkPermission(userName, Constants.ACL_DELETE_PERMISSION);
      } catch (Exception e ) {
        return false;
      }        

      if (!ok)
        return false;


        


     String tblName = gen.getTableName();
     int state = gen.getState();

     if (state==INSERT_RECORD) {
      gen.removeField("PK_INDICATOR_ID");
    }
    else
      gen.setPKField("PK_INDICATOR_ID");


      defaultProcessing(gen, null);
      return true; //!getError();
   }
}