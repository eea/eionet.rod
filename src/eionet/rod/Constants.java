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

import com.tee.util.*;
import com.tee.xmlserver.*;

/**
 * <P>Interface defining constants used in the WebROD system.</P>
 *
 * @author  Andre Karpistsenko, Rando Valt
 * @version 1.1
 */

public interface Constants {
// index servlet constants
   public static final String INDEX_XSL = "index.xsl";
   public static final String INDEX_QUERY = "index.xrs";
// reporting obligation browse servlet constants
   public static final String RORABROWSE_XSL = "rorabrowse.xsl";
   public static final String RORABROWSE_QUERY = "rorabrowse.xrs";
// show servlet constants
   public static final String SOURCE_XSL = "source.xsl";
   public static final String SOURCE_QUERY  = "source.xrs";
   public static final String HIERARCHY_XSL = "hierarchy.xsl";
   public static final String HIERARCHY_QUERY  = "hierarchy.xrs";
   public static final String HIERARCHYX_XSL = "hierarchyx.xsl";
   public static final String REPORTING_XSL = "reporting.xsl";
   public static final String REPORTING_QUERY  = "reporting.xrs";
   public static final String ACTIVITY_XSL = "activity.xsl";
   public static final String ACTIVITY_QUERY  = "activity.xrs";
   public static final String REPORTING_PARAM_XSL = "reportingparam.xsl";
   public static final String REPORTING_PARAM_QUERY  = "reportingparam.xrs";
   public static final String REPORTING_PRINT_XSL = "printreporting.xsl";
   public static final String ACTIVITY_PRINT_XSL = "printactivity.xsl";

   // show mode constants
   public static final String SOURCE_MODE = "S";
   public static final String HIERARCHY_MODE = "C";
   public static final String HIERARCHYX_MODE = "X";
   public static final String REPORTING_MODE = "R";
   public static final String REPORTING_PARAM_MODE = "ROP";
   public static final String ACTIVITY_MODE = "A";
   public static final String REPORTING_PRINT_MODE = "PR";
   public static final String ACTIVITY_PRINT_MODE = "PA";

   // editor
   public static final String E_SOURCE_XSL = "esource.xsl";
   public static final String E_SOURCE_QUERY  = "esource.xrs";
   public static final String E_REPORTING_XSL = "ereporting.xsl";
   public static final String E_REPORTING_QUERY  = "ereporting.xrs";
   public static final String E_ACTIVITY_XSL = "eactivity.xsl";
   public static final String E_ACTIVITY_QUERY  = "eactivity.xrs";

   public static final String ID_PARAM = "id";
   public static final String MODE_PARAM = "mode";
   public static final String AID_PARAM = "aid";
   public static final String ENTITY_PARAM ="entity";

// Filter parameters for Reporting Obligation
   public static final String SHOWFILTER ="showfilters";
   public static final String ENV_ISSUE_FILTER ="env_issue";
   public static final String COUNTRY_FILTER ="country";
   public static final String RIVER_FILTER ="river";
   public static final String SEA_FILTER ="sea";
   public static final String LAKE_FILTER ="lake";
   public static final String PARAM_GROUP_FILTER ="param_group";
   public static final String ROTYPE_FILTER ="type";
   public static final String SOURCE_FILTER ="source";

// Spatial attribute types
   public static final String SPATIAL_COUNTRY ="C";
   public static final String SPATIAL_SEA ="S";
   public static final String SPATIAL_RIVER ="R";
   public static final String SPATIAL_LAKE ="L";
   public static final String SPATIAL_RESERVOIR ="O";

}
