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

import java.util.Vector;
import com.tee.xmlserver.*;
import com.tee.util.*;

/**
 * <P>Dynamic SQL generator for WebROD filtering.</P>
 *
 * Accepts the following parameters:<UL>
 * <LI>env_issue - environmental issue</LI>
 * <LI>country - spatial coverage, country</LI>
 * <LI>river - spatial coverage, river runoff areas</LI>
 * <LI>sea - spatial coverage, seas</LI>
 * <LI>lake - spatial coverage, lakes and reservoirs</LI>
 * <LI>param_group - parameter groups</LI>
 * <LI>rotype - show obligations of certain type</LI>
 * <LI>source - show legal instrument's obligations</LI>
 * </UL>
 *
 * <P>Database tables involved: T_ACTIVITY, T_REPORTING, T_SOURCE, T_PARAMETER, T_PARAMETER_LNK,
 * T_ISSUE_LNK, T_SPATIAL_LNK, T_SOURCE_LNK</P>
 *
 * @author  Andre Karpistsenko, Rando Valt
 * @version 1.1
 */

public class SearchStatement extends QueryStatement implements Constants {

   private StringBuffer constr = new StringBuffer();
   private Vector vFields;
   private Vector vTables;

   class _Pair {
      String id = null;
      String name = "";
   }

   private _Pair splitParam(String in) {
      _Pair ret = new _Pair();
      if ( !Util.nullString(in) ) {
         int i = in.indexOf(':');
         if (i != -1) {
            ret.id = in.substring(0, i);
            ret.name = in.substring(i + 1);
         }
         else
            ret.id = in;
      }

      return ret;
   }

   private void appendConstraint(String clause, String op) {
      String s = " AND ";
      if (op != null && op.equals("0"))
         s = " OR ";

      if (constr.length() == 0)
         constr.append(clause);
      else
         constr.append(s)
               .append(clause);
   }

   SearchStatement(Parameters params) {
      String mode = params.getParameter(MODE_PARAM);
      if (mode == null || (!mode.equals(REPORTING_MODE) && !mode.equals(ACTIVITY_MODE)))
         throw new GeneralException(null, "Missing or invalid parameter '" + MODE_PARAM + "'");

      queryName = "Search results";
      distinct = true;
      qryTableName = null;

      vFields = new Vector();
      vTables = new Vector();

      if (mode.equals(REPORTING_MODE)) {
         vTables.add(new TableInfo("T_REPORTING"));
         vFields.add(new FieldInfo("PK_RO_ID", "T_REPORTING"));
         vFields.add(new FieldInfo("ALIAS", "T_REPORTING"));
         vTables.add(new TableInfo("T_SOURCE", "T_SOURCE.PK_SOURCE_ID = T_REPORTING.FK_SOURCE_ID", TableInfo.INNER_JOIN));
         vFields.add(new FieldInfo("PK_SOURCE_ID", "T_SOURCE"));
         vFields.add(new FieldInfo("TITLE", "T_SOURCE"));
         vFields.add(new FieldInfo("ALIAS", "T_SOURCE"));
         vFields.add(new FieldInfo("URL", "T_SOURCE"));
         vFields.add(new FieldInfo("FK_CLIENT_ID", "T_REPORTING")); //KL030213
      }
      else {
         vTables.add(new TableInfo("T_ACTIVITY"));
         vFields.add(new FieldInfo("PK_RA_ID","T_ACTIVITY"));
         vFields.add(new FieldInfo("TITLE","T_ACTIVITY"));
         vFields.add(new FieldInfo("NEXT_REPORTING","T_ACTIVITY"));
         vFields.add(new FieldInfo("NEXT_DEADLINE","T_ACTIVITY"));
         vFields.add(new FieldInfo("FK_RO_ID","T_ACTIVITY"));
         vFields.add(new FieldInfo("TERMINATE","T_ACTIVITY"));
         vTables.add(new TableInfo("T_REPORTING", "T_REPORTING.PK_RO_ID = T_ACTIVITY.FK_RO_ID", TableInfo.INNER_JOIN));
         vFields.add(new FieldInfo("PK_RO_ID", "T_REPORTING"));
         vFields.add(new FieldInfo("ALIAS", "T_REPORTING"));
         vTables.add(new TableInfo("T_SOURCE", "T_SOURCE.PK_SOURCE_ID = T_REPORTING.FK_SOURCE_ID", TableInfo.OUTER_JOIN));
         vFields.add(new FieldInfo("PK_SOURCE_ID", "T_SOURCE"));
         vFields.add(new FieldInfo("TITLE", "T_SOURCE"));
         vFields.add(new FieldInfo("ALIAS", "T_SOURCE"));
         vFields.add(new FieldInfo("URL", "T_SOURCE"));
         vFields.add(new FieldInfo("FK_CLIENT_ID", "T_REPORTING")); //KL030213
      }

      _Pair env_issue, country, river, sea, lake, param_group, rotype, client;
      String source;

      env_issue = splitParam(params.getParameter(ENV_ISSUE_FILTER));
      country = splitParam(params.getParameter(COUNTRY_FILTER));
      river = splitParam(params.getParameter(RIVER_FILTER));
      sea = splitParam(params.getParameter(SEA_FILTER));
      lake = splitParam(params.getParameter(LAKE_FILTER));
      param_group = splitParam(params.getParameter(PARAM_GROUP_FILTER));
      rotype = splitParam(params.getParameter(ROTYPE_FILTER));
      source = params.getParameter(SOURCE_FILTER);
      client = splitParam(params.getParameter(CLIENT_FILTER));      

      if ( !Util.nullString(env_issue.id) && !env_issue.id.equals("-1") ) {
/*         if (mode.equals(REPORTING_MODE)) {
           vTables.add(new TableInfo("T_ISSUE_LNK",
                       "T_REPORTING.PK_RO_ID = T_ISSUE_LNK.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           appendConstraint("T_ISSUE_LNK.FK_ISSUE_ID=" + env_issue.id, "1");
         }  */
//         else {

		if (mode.equals(REPORTING_MODE)) 
           vTables.add(new TableInfo("T_ACTIVITY", "T_ACTIVITY.FK_RO_ID = T_REPORTING.PK_RO_ID", TableInfo.INNER_JOIN));
	       //vTables.add(new TableInfo("T_ACTIVITY"));
		   //vFields.add(new FieldInfo("PK_RA_ID","T_ACTIVITY"));
		   //vFields.add(new FieldInfo("FK_RO_ID","T_ACTIVITY"));

           //appendConstraint("T_ACTIVITY.FK_RO_ID=T_REPORTING.PK_RO_ID", "1");

           vTables.add(new TableInfo("T_RAISSUE_LNK",
                       "T_ACTIVITY.PK_RA_ID = T_RAISSUE_LNK.FK_RA_ID",
                        TableInfo.INNER_JOIN));
           appendConstraint("T_RAISSUE_LNK.FK_ISSUE_ID=" + env_issue.id, "1");
/*
           vTables.add(new TableInfo("T_REPORTING AS TROENVISSUE",
                       "TROENVISSUE.PK_RO_ID = T_ACTIVITY.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           vTables.add(new TableInfo("T_ISSUE_LNK",
                       "TROENVISSUE.PK_RO_ID = T_ISSUE_LNK.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           appendConstraint("T_ISSUE_LNK.FK_ISSUE_ID=" + env_issue.id, "1");
*/
//         } 

         addAttribute("Environmental_issue_equals", env_issue.name);
      }

      if ( !Util.nullString(country.id) && !country.id.equals("-1") ) {
         if (mode.equals(REPORTING_MODE)) {
           vTables.add(new TableInfo("T_SPATIAL_LNK AS TCOUNTRY",
                       "T_REPORTING.PK_RO_ID = TCOUNTRY.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           appendConstraint("TCOUNTRY.FK_SPATIAL_ID=" + country.id, "1");
         } else {
           vTables.add(new TableInfo("T_REPORTING AS TROCOUNTRY",
                       "TROCOUNTRY.PK_RO_ID = T_ACTIVITY.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           vTables.add(new TableInfo("T_SPATIAL_LNK AS TCOUNTRY",
                       "TROCOUNTRY.PK_RO_ID = TCOUNTRY.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           appendConstraint("TCOUNTRY.FK_SPATIAL_ID=" + country.id, "1");
         }

         addAttribute("Country_equals", country.name);
      }

      if ( !Util.nullString(river.id) && !river.id.equals("-1") ) {
         if (mode.equals(REPORTING_MODE)) {
           vTables.add(new TableInfo("T_SPATIAL_LNK AS TRIVER",
                       "T_REPORTING.PK_RO_ID = TRIVER.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           appendConstraint("TRIVER.FK_SPATIAL_ID=" + river.id, "1");
         } else {
           vTables.add(new TableInfo("T_REPORTING AS TRORIVER",
                       "TRORIVER.PK_RO_ID = T_ACTIVITY.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           vTables.add(new TableInfo("T_SPATIAL_LNK AS TRIVER",
                       "T_REPORTING.PK_RO_ID = TRIVER.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           appendConstraint("TRIVER.FK_SPATIAL_ID=" + river.id, "1");

         }

         addAttribute("River_equals", river.name);
      }

      if ( !Util.nullString(sea.id) && !sea.id.equals("-1") ) {
         if (mode.equals(REPORTING_MODE)) {
           vTables.add(new TableInfo("T_SPATIAL_LNK AS TSEA",
                       "T_REPORTING.PK_RO_ID = TSEA.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           appendConstraint("TSEA.FK_SPATIAL_ID=" + sea.id, "1");
         } else {
           vTables.add(new TableInfo("T_REPORTING AS TROSEA",
                       "TROSEA.PK_RO_ID = T_ACTIVITY.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           vTables.add(new TableInfo("T_SPATIAL_LNK AS TSEA",
                       "T_REPORTING.PK_RO_ID = TSEA.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           appendConstraint("TSEA.FK_SPATIAL_ID=" + sea.id, "1");
         }

         addAttribute("Sea_equals", sea.name);
      }

      if ( !Util.nullString(lake.id) && !lake.id.equals("-1") ) {
         if (mode.equals(REPORTING_MODE)) {
           vTables.add(new TableInfo("T_SPATIAL_LNK AS TLAKE",
                       "T_REPORTING.PK_RO_ID = TLAKE.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           appendConstraint("TLAKE.FK_SPATIAL_ID=" + lake.id, "1");
         } else {
           vTables.add(new TableInfo("T_REPORTING AS TROLAKE",
                       "TROLAKE.PK_RO_ID = T_ACTIVITY.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           vTables.add(new TableInfo("T_SPATIAL_LNK AS TLAKE",
                       "T_REPORTING.PK_RO_ID = TLAKE.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           appendConstraint("TLAKE.FK_SPATIAL_ID=" + lake.id, "1");
         }

         addAttribute("Lake_or_reservoir_equals", lake.name);
      }

      if ( !Util.nullString(param_group.id) && !param_group.id.equals("-1") ) {
         if (mode.equals(REPORTING_MODE)) {
           vTables.add(new TableInfo("T_ACTIVITY",
                       "T_REPORTING.PK_RO_ID = T_ACTIVITY.FK_RO_ID",
                        TableInfo.INNER_JOIN));
           vTables.add(new TableInfo("T_PARAMETER_LNK",
                       "T_ACTIVITY.PK_RA_ID = T_PARAMETER_LNK.FK_RA_ID",
                        TableInfo.INNER_JOIN));
           vTables.add(new TableInfo("T_PARAMETER",
                       "T_PARAMETER_LNK.FK_PARAMETER_ID = T_PARAMETER.PK_PARAMETER_ID",
                        TableInfo.INNER_JOIN));
           appendConstraint("T_PARAMETER.FK_GROUP_ID=" + param_group.id, "1");
         } else {
           vTables.add(new TableInfo("T_PARAMETER_LNK",
                       "T_ACTIVITY.PK_RA_ID = T_PARAMETER_LNK.FK_RA_ID",
                        TableInfo.INNER_JOIN));
           vTables.add(new TableInfo("T_PARAMETER",
                       "T_PARAMETER_LNK.FK_PARAMETER_ID = T_PARAMETER.PK_PARAMETER_ID",
                        TableInfo.INNER_JOIN));
           appendConstraint("T_PARAMETER.FK_GROUP_ID=" + param_group.id, "1");
         }

         addAttribute("Parameter_group_equals", param_group.name);
      }

      if ( !Util.nullString(rotype.id) && !rotype.id.equals("-1") ) {
         if (mode.equals(REPORTING_MODE)) {
           vTables.add(new TableInfo("T_SOURCE_LNK",
                       "T_REPORTING.FK_SOURCE_ID = T_SOURCE_LNK.FK_SOURCE_CHILD_ID AND T_SOURCE_LNK.CHILD_TYPE='S'",
                        TableInfo.INNER_JOIN));
           boolean many = false;
           StringBuffer conID = new StringBuffer();
           while (rotype.id.indexOf(",") != -1) {
             int tmp = rotype.id.indexOf(",");
             if (!many)
               conID.append("(FK_SOURCE_PARENT_ID=" + rotype.id.substring(0,tmp));
             else
               conID.append(" OR FK_SOURCE_PARENT_ID=" + rotype.id.substring(0,tmp));

             rotype.id = rotype.id.substring(tmp+1);
             many = true;
           }
           if (many)
             appendConstraint(conID.toString()+" OR FK_SOURCE_PARENT_ID=" + rotype.id+")", "1");
           else
             appendConstraint("T_SOURCE_LNK.FK_SOURCE_PARENT_ID=" + rotype.id, "1");

           appendConstraint("T_SOURCE_LNK.PARENT_TYPE='C'", "1");
         }

         addAttribute("Reporting_obligation_type_equals", rotype.name);
      }

         if ( !Util.nullString(source) && !source.equals("-1") ) {
         if (mode.equals(REPORTING_MODE))
           appendConstraint("T_REPORTING.FK_SOURCE_ID=" + source, "1");

      }
   if ( !Util.nullString(client.id ) && !client.id.equals("-1") ) {
         //if (mode.equals(REPORTING_MODE)) {
        appendConstraint("T_REPORTING.FK_CLIENT_ID=" + client.id, "1");
        addAttribute("Reporting_client_equals", client.name);           
         //} 
         /*else {
           appendConstraint("T_REPORTING.FK_CLIENT_ID=" + client.id, "1");
         }*/
  }
//

      

      setFields(vFields);
      setTables(vTables);
      whereClause = constr.toString();

      if (mode.equals(REPORTING_MODE))
         orderClause = "T_REPORTING.ALIAS";
      else
         orderClause = "T_ACTIVITY.TITLE";

   }

}
