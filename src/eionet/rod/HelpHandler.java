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
 * The Original Code is "EINRC-5 / WebROD Project".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Ander Tenno (TietoEnator)
 */

package eionet.rod;

import java.io.*;
import javax.servlet.http.*;

import com.tee.util.*;
import com.tee.xmlserver.*;


public class HelpHandler extends ROHandler {

   public int setCommitLevel() {
      return AUTO_COMMIT;
   }

   public HelpHandler(ROEditServletAC servlet) {
      super(servlet);
   }
   protected boolean sqlReady(SQLGenerator gen, String context) {
      String userName = this.user.getUserName();
      boolean isAuth = false;

      try {
         isAuth = servlet.getAcl().checkPermission(userName, "h");
      } 
      catch (Exception e) {
         return false;
      }        
      if(!isAuth)
         return false;

      gen.setPKField("PK_HELP_ID");  
      defaultProcessing(gen, null);
      return !getError();
   }
}