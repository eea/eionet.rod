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
import com.tee.util.Util;

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
      String username = req.getParameter("j_username");
      String password = req.getParameter("j_password");



      
      //KL
      /*if ( Util.nullString(username) || Util.nullString(password)) {
        freeSession(req);
        throw new ServletException("UserName or password cannot be emtpy");
      } */

      
      AppUserIF user = XDBApplication.getAuthenticator();

/*if (user == null)      
Logger.log("******************* userAuth is null");      
else
Logger.log("******************* userAuth " + user.getUserName());*/

      if (user.authenticate(username, password) == true) {
         // store the authenticated user object to current session
         allocSession(req, user);
         // close current window
         printPage(res, "<html><script>alert('Logged to WebROD as administrator')</script></html>");
//!!!!!!!!!!!!!!!!!!! 
         String location = "index.html";
         //String location = "test.html";         
         // DBG
         if (Logger.enable(5))
            Logger.log("Redirecting to " + location);
         res.sendRedirect(location);
      }
      else {
        //allocSession(req,user);


         String loginError = null;
         try {
          loginError = XDBApplication.getLoginError();
        } catch (GeneralException ge ) {
        }

         freeSession(req);
         if ( loginError != null)
           res.sendRedirect(loginError);
          else
            printPage(res, "<html>Not authenticated</html>");
  
      }

/*AppUserIF dbguser = getUser(req);
if (user == null)      
Logger.log("******************* AFTER login user = null");      
else {
  Logger.log("******************* AFTER LOGIN " + dbguser.getUserName());
  Logger.log("******************* AFTER LOGIN auth " + dbguser.isAuthentic());
} */

      
    }

/*
    private void fSess(javax.servlet.http.HttpServletRequest servReq ) {
      String aREMOTEUSER = "com.tee.xmlserver.user";
      String aremoteUser = aREMOTEUSER + "/" +  "WebROD";
     HttpSession httpSession = servReq.getSession(false);

      if (httpSession != null) {
         AppUserIF user = (AppUserIF)httpSession.getAttribute(aremoteUser);

         if (user != null) {
            Logger.log("*********** BAD 6")         ;
            user.invalidate();
Logger.log("*********** BAD 7")         ;            
          } else
Logger.log("*********** BAD 8")         ;                      

if ( user== null)
  Logger.log("*********** user is null" + user)         ;                      
else
  Logger.log("*********** user is not null" );
  
//Logger.log("*********** GU TOSTRING " + user.toString())         ;                      

Logger.log( "***************************************************** ")         ;                      
//Logger.log( user )         ;                      
Logger.log("*********** user " + user)         ;                      

         // DBG
         if (Logger.enable(5))
            Logger.log("freeSession: session=") ; //" + httpSession + " user=" + user);
         //
	      httpSession.invalidate();
      }    
    }

   public final AppUserIF gU(HttpServletRequest servReq) {
      AppUserIF user = null;
Logger.log("************** GU 1");
      String aREMOTEUSER = "com.tee.xmlserver.user";
      String aremoteUser = aREMOTEUSER + "/" +  "WebROD";
      
      HttpSession httpSession = servReq.getSession(false);
Logger.log("************** GU 2");      
      if (httpSession != null)  {
Logger.log("************** GU 3");      
          user = (AppUserIF)httpSession.getAttribute(aremoteUser);
Logger.log("************** GU 4");          
      }    
      // DBG
      if (Logger.enable(5))
         Logger.log("getUser: session=" + httpSession + " user=" + user + " isAuthentic=" + 
                    (user != null && user.isAuthentic() ? "true" : "false"));
      //
      if (user != null) {
Logger.log("************** GU 5");      
         return user.isAuthentic() ? user : null;
         
        }
      else  {
Logger.log("************** GU 6");      
         return null;
   } 
   }
*/    
}
