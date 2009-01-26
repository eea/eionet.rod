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
import javax.servlet.*;
import javax.servlet.http.*;

import com.tee.xmlserver.*;

/**
 * Simple servlet to handle login form submit. Uses authenticator module specified by application
 * descriptor <CODE>&lt;context-param authenticator="..."&gt;</CODE> parameter to authenticate passed user (request parameters
 * <CODE>j_username</CODE> and <CODE>j_password</CODE>. If authentication fails, redirects caller to error page 
 * specified by <CODE>&lt;context-param login-error-page="..."&gt;</CODE><BR><BR>
 *
 * Date:    03.08.00<BR>
 * Updates: <UL></UL>
 *
 * @author  Rando Valt
 * @version 1.02
 */

public class LoginServlet extends BaseServletAC {
// dummy implementations

/**
 * Empty implementation of <CODE>BaseServletAC.appInit()</CODE> method.
 */
   protected void appInit() {}
/**
 * Empty implementation of <CODE>BaseServletAC.appDestroy()</CODE> method.
 */
   protected void appDestroy() {}
/**
 *
 */
   public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

      
      String rd = req.getParameter("rd");
    
      AppUserIF user = XDBApplication.getAuthenticator();

      	// IMPORTANT user is alredy logged via CAS ***************************************
      	 user.authenticate(req.getRemoteUser(), null); 
         // store the authenticated user object to current session
         allocSession(req, user);
         // close current window
         //printPage(res, "<html><script>alert('Logged to WebROD as administrator')</script></html>");

         String location = "index.html";
         if(rd != null && rd.equals("subscribe")){
             location = "subscribe.jsp";
         }
         //String location = "test.html";         
         // DBG
         if (Logger.enable(5))
            Logger.log("Redirecting to " + location);
         res.sendRedirect(location);
      }


}
