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
import java.util.HashMap;


/**
 * <P>Common super-class for all WebROD servlets.</P>
 *
 * @author  Rando Valt, Andre Karpistsenko
 * @version 1.1
 */

public abstract class ROServletAC extends XHTMLServletAC implements Constants {

  public static String PREFIX = "../app/";
  //public static String PREFIX; // = "app/";
  
  private static final String CTXT  = "/webrod/app/";
  private static final String APP_HOST = "127.0.0.1";
  private static final String APP_PORT = "80";


  private QueryStatementIF metaRA, metaRO, metaLI;
  //private static AccessControlListIF acl ;
  private static HashMap  acls ;
  
  public void appInit(){
  
      //log("****************************** appInit GO ");
      //log("************************ " + XDBApplication.getInstance( getServletContext() ).getServerInfo());

        //if (XDBApplication.getInstance( getServletContext() ).getServerInfo().indexOf("Tomcat") !=-1 )
      PREFIX = "app/";
          
          
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

       
      //KL030127
      if (acls==null)
        initAcls();
        
    }

  protected void resetAcls() {
    acls=null;
  }

  
  protected AccessControlListIF getAcl(String name) throws SignOnException {
    
    if (acls== null)
      initAcls();
      
    
    return (AccessControlListIF)acls.get(name);
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
      //return "ISO-8859-1";
      return "UTF-8";
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
         if (u != null) 
            qry.addAttribute("auth", "true");
         else
            qry.addAttribute("auth", "false");
            //ACL

          if ( acls == null)
            initAcls();
              
          //if ( acls != null) {
            String prms = "";
            try {
               //prms = acl.getPermissions( u.getUserName() ) ;
               String uName = (u==null ? "" : u.getUserName() );
               prms=AccessController.getPermissions(uName);
            } catch (SignOnException soe ) {
              prms = null;

              //throw new Exception ("");
            }

            if (prms != null)
              qry.addAttribute("permissions", prms);
              //<- test acl
            //}
      }
      
      return dataSrc;
   }

   private void initAcls() {
        try {
          acls = AccessController.getAcls();
/*
log("******************************");          
log("************* ACL initiated ");
log("******************************"); */
         } catch (SignOnException soe ) {
          log(" Error, getting ACLs for webrod " + soe);
        }

   }

  /**
  * Adds metainfo from T_SOURCE, T_ACTIVITY, T_REPORTING
  */
   protected void addMetaInfo(DataSourceIF dataSrc) {
    //QueryStatementIF metaRA, metaRO, metaLI;
    if (metaRA==null) {
      metaRA = new MetaData("RAMetaInfo", "T_OBLIGATION");
    }
    
     dataSrc.unsetQuery(metaRA);
     dataSrc.setQuery(metaRA);

/*
    if (metaRO==null) {
      metaRO = new MetaData("ROMetaInfo", "T_REPORTING");
    }
     dataSrc.unsetQuery(metaRO);
     dataSrc.setQuery(metaRO);   

    */
    if (metaLI==null) {
      metaLI = new MetaData("LIMetaInfo", "T_SOURCE");
    }
     dataSrc.unsetQuery(metaLI);
     dataSrc.setQuery(metaLI);   

     //dataSrc.setQuery(new MetaData("LIMetaInfo", "T_SOURCE"));        
   }
}