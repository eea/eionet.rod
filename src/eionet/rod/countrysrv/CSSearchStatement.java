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
 * Original Code: Andre Karpistsenko (TietoEnator)
 */

package eionet.rod.countrysrv;

import java.util.Vector;
import com.tee.xmlserver.*;
import com.tee.util.*;
import eionet.rod.Constants;
import java.util.Calendar;
import java.sql.*;

/**
 * <P>Dynamic SQL generator for CS filtering.</P>
 *
 * @author  Andre Karpistsenko, Rando Valt, Kaido Laine
 * @version 1.1
 */

public class CSSearchStatement extends QueryStatement implements Constants {

   private StringBuffer constr = new StringBuffer();
   private Vector vFields;
   private Vector vTables;
   private PreparedStatement  stmt = null;
   private Parameters _params = null;
   private boolean _ccClients = false;
   private boolean _union = false;

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

   /*public CSSearchStatement(Parameters params, boolean ccClients) {
	   return CSSearchStatement(params, ccClients, false);
   }*/
   public CSSearchStatement(Parameters params, boolean ccClients, boolean union) {

	   _params = params;
	   _ccClients = ccClients;
	   _union = union;
    String ord = params.getParameter("ORD");
    String spatialId = params.getParameter("COUNTRY_ID");
    String clientId = params.getParameter("CLIENT_ID");
    String issueId = params.getParameter("ISSUE_ID");

    String date1= params.getParameter("DATE_1");
    String date2= params.getParameter("DATE_2");
    String dlCase= params.getParameter("DEADLINES");

     queryName = "Main";

     distinct = true;
     //qryTableName = null;

      vFields = new Vector();
      vTables = new Vector();


      vTables.add(new TableInfo("T_OBLIGATION"));

      vFields.add(new FieldInfo("PK_RA_ID","T_OBLIGATION"));
      vFields.add(new FieldInfo("TITLE","T_OBLIGATION"));
      vFields.add(new FieldInfo("RESPONSIBLE_ROLE","T_OBLIGATION"));
      vFields.add(new FieldInfo("NEXT_REPORTING","T_OBLIGATION"));
      vFields.add(new FieldInfo("NEXT_DEADLINE","T_OBLIGATION"));
      vFields.add(new FieldInfo("FK_SOURCE_ID","T_OBLIGATION"));
      vFields.add(new FieldInfo("TERMINATE","T_OBLIGATION"));
      vFields.add(new FieldInfo("FK_CLIENT_ID","T_OBLIGATION"));
      vFields.add(new FieldInfo("FK_DELIVERY_COUNTRY_IDS","T_OBLIGATION"));


      FieldInfo o1 = new FieldInfo("DEADLINE","T_OBLIGATION");
      if (union){
    	  o1.setFieldExpr("NEXT_DEADLINE2 AS DEADLINE");
      }
      else{
          o1.setFieldExpr("IF(NEXT_DEADLINE IS NULL, NEXT_REPORTING, NEXT_DEADLINE) AS DEADLINE");
      }
      vFields.add(o1);

      FieldInfo o2 = new FieldInfo("DEADLINE2","T_OBLIGATION");
      if (union){
    	  o2.setFieldExpr("NEXT_DEADLINE AS DEADLINE2");
      }
      else{
          o2.setFieldExpr("NEXT_DEADLINE2 AS DEADLINE2");
      }
      vFields.add(o2);

      // Add field for ordering deliveries
      //EK 20.05.2005
      FieldInfo o3 = new FieldInfo("HAS_DELIVERY","T_OBLIGATION");
      o3.setFieldExpr("FK_DELIVERY_COUNTRY_IDS REGEXP CONCAT(',',T_SPATIAL.PK_SPATIAL_ID,',') AS HAS_DELIVERY");
      vFields.add(o3);
      
      // ROLE
      TableInfo resp = new TableInfo("T_ROLE", "CONCAT(T_OBLIGATION.RESPONSIBLE_ROLE,'-',IF(T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY='Y','mc','cc'),'-',LCASE(T_SPATIAL.SPATIAL_TWOLETTER))=RESPONSIBLE.ROLE_ID" , TableInfo.OUTER_JOIN);
      resp.setAlias("RESPONSIBLE");
      vTables.add(resp);

      FieldInfo r1 = new FieldInfo("ROLE_DESCR", "RESPONSIBLE");
      //r1.setFieldExpr("IF(PERSON != '', IF(INSTITUTE='', PERSON, CONCAT(PERSON, ' [', INSTITUTE, ']')), ROLE_NAME) AS ROLE_DESCR");
      r1.setFieldExpr("ROLE_NAME AS ROLE_DESCR");
      vFields.add(r1);
      vFields.add(new FieldInfo("ROLE_URL","RESPONSIBLE"));
      vFields.add(new FieldInfo("ROLE_MEMBERS_URL","RESPONSIBLE"));
      //

      vTables.add(new TableInfo("T_CLIENT_LNK", "T_CLIENT_LNK.TYPE='A' AND T_CLIENT_LNK.STATUS='M' AND T_CLIENT_LNK.FK_OBJECT_ID=T_OBLIGATION.PK_RA_ID", TableInfo.OUTER_JOIN));
      vFields.add(new FieldInfo( "FK_CLIENT_ID", "T_CLIENT_LNK"));
      vFields.add(new FieldInfo("FK_OBJECT_ID", "T_CLIENT_LNK"));
      vFields.add(new FieldInfo("TYPE", "T_CLIENT_LNK"));
      vFields.add(new FieldInfo("STATUS", "T_CLIENT_LNK"));

      vTables.add(new TableInfo("T_CLIENT", "T_CLIENT.PK_CLIENT_ID = T_CLIENT_LNK.FK_CLIENT_ID", TableInfo.OUTER_JOIN));
      vFields.add(new FieldInfo("PK_CLIENT_ID","T_CLIENT"));
      vFields.add(new FieldInfo("CLIENT_NAME","T_CLIENT"));
      FieldInfo c1 = new FieldInfo("CLIENT_DESCR", "T_CLIENT");
      c1.setFieldExpr("IF(CLIENT_ACRONYM='', CLIENT_NAME, CLIENT_ACRONYM) AS CLIENT_DESCR");
      vFields.add(c1);

      vTables.add(new TableInfo("T_RASPATIAL_LNK", "T_RASPATIAL_LNK.FK_RA_ID=T_OBLIGATION.PK_RA_ID", TableInfo.INNER_JOIN));
      vFields.add(new FieldInfo("FK_RA_ID", "T_RASPATIAL_LNK"));
      vFields.add(new FieldInfo("FK_SPATIAL_ID", "T_RASPATIAL_LNK"));

      vTables.add(new TableInfo("T_SPATIAL", "T_RASPATIAL_LNK.FK_SPATIAL_ID=T_SPATIAL.PK_SPATIAL_ID", TableInfo.OUTER_JOIN));
      vFields.add(new FieldInfo("PK_SPATIAL_ID", "T_SPATIAL"));
      vFields.add(new FieldInfo("SPATIAL_NAME", "T_SPATIAL"));
      vFields.add(new FieldInfo("SPATIAL_TWOLETTER", "T_SPATIAL"));
      vFields.add(new FieldInfo("SPATIAL_ISMEMBERCOUNTRY", "T_SPATIAL"));

      vTables.add(new TableInfo("T_SOURCE", "T_SOURCE.PK_SOURCE_ID = T_OBLIGATION.FK_SOURCE_ID", TableInfo.OUTER_JOIN));
      vFields.add(new FieldInfo("PK_SOURCE_ID", "T_SOURCE"));
      vFields.add(new FieldInfo("SOURCE_CODE", "T_SOURCE"));


      if (union){
    	  appendConstraint("NEXT_DEADLINE2 IS NOT NULL", "1");
    	  appendConstraint("NEXT_DEADLINE2 != NEXT_DEADLINE", "1");
      }

      appendConstraint("TERMINATE='N'", "1");


      if (!Util.nullString(spatialId))
        appendConstraint("PK_SPATIAL_ID=" + Util.strLiteral(spatialId), "1");


      if (!Util.nullString(clientId) && !clientId.equals("0") )
        appendConstraint("PK_CLIENT_ID=" + Util.strLiteral(clientId), "1");

      if (!Util.nullString(issueId) && !issueId.equals("0")) {
        vTables.add(new TableInfo("T_RAISSUE_LNK", "T_OBLIGATION.PK_RA_ID=T_RAISSUE_LNK.FK_RA_ID", TableInfo.INNER_JOIN));
        vFields.add(new FieldInfo("FK_RA_ID", "T_RAISSUE_LNK"));
        vFields.add(new FieldInfo("FK_ISSUE_ID", "T_RAISSUE_LNK"));
        appendConstraint("FK_ISSUE_ID=" + Util.strLiteral(issueId), "1");
      }


      /*System.out.println("======== spatial id " + spatialId);
      System.out.println("======== issue id " + issueId);
      System.out.println("======== client id " + clientId);

      System.out.println("======== date1 " + date1);
      System.out.println("======== date2 " + date2);
      */

      if ( (date1 !=null && !date1.equals("dd/mm/yyyy")) || (date2 !=null && !date2.equals("dd/mm/yyyy")) || dlCase != null )
        handleDeadlines(dlCase, date1, date2) ;

      setFields(vFields);
      setTables(vTables);

      whereClause = constr.toString();
       if(ord==null)
    		ord="TITLE";
       		//ord="T_OBLIGATION.TITLE";

       if (!union)
    	   addAttribute("Sort_order", ord);

		 orderClause = ord;

   }

   private void handleDeadlines(String dlCase, String date1, String date2) {
     if ( dlCase != null ) { //selected in combo
        Calendar today = Calendar.getInstance();
        //all Deadlines
        if (dlCase.equals("0")) {
          date1 ="dd/mm/yyyy";
          date2 ="dd/mm/yyyy";
        }
        //next month
        else if (dlCase.equals("1")) {
          date1=getDate(today);
          today.add(Calendar.MONTH, 1);
          date2=getDate(today);
        }
        //next 3 months
        else if (dlCase.equals("2")) {
          date1=getDate(today);
          today.add(Calendar.MONTH, 3);
          date2=getDate(today);
        }
        //next 6 months
        else if (dlCase.equals("3")) {
          date1=getDate(today);
          today.add(Calendar.MONTH, 6);
          date2=getDate(today);
        }
        //passed
        else if (dlCase.equals("4")) {
          date2=getDate(today);
    		  today.add(Calendar.MONTH, -3);
          date1=getDate(today);
        }
    }

     if (date1.equals("dd/mm/yyyy"))
        date1 ="00/00/0000";

      date1=cnvDate(date1);

      if (date2.equals("dd/mm/yyyy"))
        date2="31/12/9999";

      date2=cnvDate(date2);
      /*
      System.out.println("===== DATE1 " + date1);
      System.out.println("===== DATE2 " + date2);
      */
      if (_union)
    	  appendConstraint(" ((NEXT_DEADLINE2 >= '" + date1 + "' AND NEXT_DEADLINE2 <= '" + date2 + "')) ", "1");
      else
    	  appendConstraint(" ((NEXT_DEADLINE >= '" + date1 + "' AND NEXT_DEADLINE <= '" + date2 + "')) ", "1");

   }

    // dd/mm/yyyy -> yyyy-mm-dd
    private String cnvDate(String date ){
    date = date.substring(6) +"-"+  date.substring(3,5) +"-"+  date.substring(0,2);
    return date;
  }

    //formats Calendar object date to dd/mm/yyyy
   private String getDate(Calendar cal) {
    String day = Integer.toString( cal.get( Calendar.DATE) );
    if (day.length() == 1)
      day  ="0" + day;
    String month = Integer.toString( cal.get( Calendar.MONTH) +1 );
    if (month.length() == 1)
      month  ="0" + month;

    String year = Integer.toString( cal.get( Calendar.YEAR) );

    return day + "/" + month + "/" + year;
  }
   //EK 010306
   /**
   *	Overridden prepeareStatement - allows to create own sql statement. This method
   *	creates another QueryStatemnt for UNION SELECT. Concated 2 sql and passed to connection.
   */
     public PreparedStatement prepareStatement(Connection conn) throws SQLException {
    	String sql = getSQL();

    	//create another QueryStatement
    	CSSearchStatement unionStatement  = new CSSearchStatement(_params, _ccClients, true);
    	String sql2 = unionStatement.getSQL();

    	// cut ORDER BY statement and append it to the end of UNION SELECT
 	    int i = sql.indexOf("ORDER BY");
 	    StringBuffer union_sql = new StringBuffer();
 	    union_sql.append(sql.substring(0,i));
 	    union_sql.append(" UNION ");
 	    union_sql.append(sql2);

 	   System.out.println("-->unionn sql" + union_sql.toString());

 	   stmt = conn.prepareStatement(union_sql.toString());

 	 //   stmt = conn.prepareStatement(sql);
        return stmt;
     }
  /**
   * Shortcut method to the main "work-horse" of the SQL statement object.
   */
     public ResultSet executeQuery() throws SQLException {
        if (stmt == null)
           throw new SQLException("Statement object is not prepared");

        return stmt.executeQuery();
     }

}
