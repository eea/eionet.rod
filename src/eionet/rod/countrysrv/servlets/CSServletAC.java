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
 * Original Code: Kaido Laine (TietoEnator)
 */
 
package eionet.rod.countrysrv.servlets;


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
  * Error xslt
  */
   protected String setErrorXSLT() {
      return PREFIX + "error.xsl";
   }
    
}