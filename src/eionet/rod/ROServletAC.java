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

import javax.servlet.http.*;
import javax.servlet.*;

import com.tee.util.*;
import com.tee.xmlserver.*;

import com.tee.uit.security.*;


/**
 * <P>Common super-class for all WebROD servlets.</P>
 *
 * @author  Rando Valt, Andre Karpistsenko
 * @version 1.1
 */

public abstract class ROServletAC extends XHTMLServletAC implements Constants {

  public static String PREFIX = "../app/";
  
  private static final String CTXT  = "/webrod/app/";
  private static final String APP_HOST = "127.0.0.1";
  private static final String APP_PORT = "80";

  private static AccessControlListIF acl ;
  
  public void appInit(){
        //log("****************************** appInit GO ");
        ServletContext ctx = getServletContext();
        StringBuffer urlPrefix = new StringBuffer("http://");

        if (Util.nullString(ctx.getInitParameter("app-host")))
            urlPrefix.append(APP_HOST);
        else
            urlPrefix.append(ctx.getInitParameter("app-host"));
        
        if (Util.nullString(ctx.getInitParameter("app-port")))
            urlPrefix.append(":").append(APP_PORT);
        else
            urlPrefix.append(":").append(ctx.getInitParameter("app-port"));

        String appCtxt = ctx.getInitParameter("app-ctxt");
        if (!Util.nullString(appCtxt)){
            if (!appCtxt.startsWith("/")) appCtxt = "/" + appCtxt;
            if (!appCtxt.endsWith("/")) appCtxt = appCtxt + "/";
            //PREFIX = urlPrefix.append(appCtxt).toString();
        }
        else{
            //PREFIX = urlPrefix.append(CTXT).toString();
        }

        //KL021029-> Access Control List for ROD
/*        try {
        
          acl = AccessController.getAcl("webrod");
          //log("************* ACL OK");
        } catch (SignOnException soe ) {
          log(" Error, getting ACL for webrod " + soe);
        } */
      //KL030127
      if (acl==null)
        initAcl();
        
    }

  protected void resetAcl() {
    acl=null;
    //Logger.log("******************* ACL = null");    
  }
  protected AccessControlListIF getAcl() throws SignOnException {

    if (acl== null)
      initAcl();
      
    return acl;
  }
  
/**
 *
 */
   protected String[] setTblsInvolved(HttpServletRequest req) {
      //return new String[]{"dummy"};
      return new String[]{};
   }
/**
 *
 */
   protected String setEncoding() {
      return "ISO-8859-1";
   }
/**
 *
 */
   protected SaveHandler setDataHandler() {
      return null;
   }
/**
 *
 */
   protected String setErrorXSLT() {
      return PREFIX + "error.xsl";
   }
/**
 * If request contains authenitcated user, adds auth="true" attribute to the first
 * rowset element of the XML. Otherwise the auth attribute value will be "false".
 */
   protected final DataSourceIF userInfo(HttpServletRequest req, DataSourceIF dataSrc) {
      // add/remove 'auth' attribute to the generated XML
      java.util.Enumeration e = dataSrc.getQueries();
      if (e != null) {
         QueryStatementIF qry = (QueryStatementIF)e.nextElement();
         AppUserIF u = getUser(req);
         if (u != null) {
            qry.addAttribute("auth", "true");
            //ACL
            if ( acl != null) {
              String prms = "";
              try {
                 prms = acl.getPermissions( u.getUserName() ) ;
              } catch (SignOnException soe ) {
                prms = null;

                //throw new Exception ("");
              }
              if (prms != null)
                qry.addAttribute("permissions", prms);
              //<- test acl
            }
          }
          else {
            qry.addAttribute("auth", "false");
            qry.addAttribute("permissions", "");
          }
      }
      
      return dataSrc;
   }

   private void initAcl() {
        try {
          acl = AccessController.getAcl("webrod");
/*
log("******************************");          
log("************* ACL initiated ");
log("******************************"); */
         } catch (SignOnException soe ) {
          log(" Error, getting ACL for webrod " + soe);
        }

   }
}