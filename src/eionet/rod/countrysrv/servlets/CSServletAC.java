/**
 */
 
package eionet.rod.countrysrv.servlets;

// import eionet.meta.processor.*;
// this import is for debugging into XmlServer XRS processing
// (look for setProcName() below)

import javax.servlet.http.*;
import javax.servlet.*;

import com.tee.xmlserver.*;
import com.tee.util.*;

import eionet.rod.ROServletAC;

/**
 * This is the base servlet for all CountrySrv servlets
 * So its an abstract super class for other servlets to extend.
 * BaseServletAC itself extends XHTMLServletAC from XmlServer,
 * so it inherits all the latter's public functionality.
 * The class also implements AppFileNames.<BR><BR>
 *
 * Main role of this class is to set URL host, port and
 * application context for XSL and XRS files whihc are
 * accessed via HTTP. The setting takes place at servlet
 * initialization and is based on servlet's context parameters.<BR><BR>
 *
 * @author Kaido Laine
 */

public abstract class CSServletAC extends ROServletAC {
//public abstract class CSServletAC extends XHTMLServletAC  {
    
    
/**
 * This is an over-ride of the <I>setMode()</I> method inheritid from
 * super class.
 */
    protected int setMode() {
        return FORM_HANDLER;
    }

/**
 * This is an over-ride of the <I>setTblsInvolved()</I> method inheritid from
 * super class.
 */
    protected String[] setTblsInvolved(HttpServletRequest req) {
        String[] tbls = {};
        return tbls;
    }

/**
 * This is an over-ride of the <I>setProcName()</I> method inheritid from
 * super class. This method is usually not over-ridden, but we need it here
 * to set our own extension of XSQLProcessorAC from XmlServer.
 *
 *   protected String setProcName(Parameters parameters) {
 *       return eionet.meta.processor.MetaProcessor.PROCESSOR_NAME;
 *   }
 */

/**
 * This is an over-ride of the <I>setDataHandler()</I> method inheritid from
 * super class. This over-ride states that by default a Meta servlet does
 * not have a data handler (is not a data saving-able servlet)
 */
    protected SaveHandler setDataHandler() {
        return null;
    }


/**
 * If request contains authenitcated user, adds auth="true" attribute to the first
 * rowset element of the XML. Otherwise the auth attribute value will be "false".
 */
/*   protected final DataSourceIF userInfo(HttpServletRequest req, DataSourceIF dataSrc) {
      // add/remove 'auth' attribute to the generated XML

      //Logger.log("*************************** userInfo ");    
      java.util.Enumeration e = dataSrc.getQueries();
      if (e != null) {
         QueryStatementIF qry = (QueryStatementIF)e.nextElement();
         if (getUser(req) != null) 
            qry.addAttribute("auth", "true");
         else
            qry.addAttribute("auth", "false");
        }
      
      
      return dataSrc;
   } */

  /**
  * Error xslt
  */
   protected String setErrorXSLT() {
      return PREFIX + "error.xsl";
   }
    
}