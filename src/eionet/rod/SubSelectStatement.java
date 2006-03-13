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
 * <P>Dynamic SQL generator for creating temporary tables, used as a replacement for
 * lacking sub-select statements in MySQL database.</P>
 *
 * @author  Rando Valt, Andre Karpistsenko
 * @version 1.1
 */

class SubSelectStatement extends QueryStatement {

   private void construct(String tmpTable, String _tbl, String _pk, String _fk, String _name1, String _name2, String constraint) {
      StringBuffer join = new StringBuffer(_tbl).append('.').append(_pk).append('=')
                                 .append(tmpTable).append('.').append(_fk);
      StringBuffer constr = new StringBuffer(tmpTable).append('.').append(_fk).append(" IS NULL");
      if ( (constraint != null) && (!constraint.equals("")) )
        constr.append(" AND "+constraint);

      Vector vFields = new Vector();
      Vector vTables = new Vector();

      vFields.add(new FieldInfo(_pk, _tbl));
      vFields.add(new FieldInfo(_name1, _tbl));
      if (_name2 != null)
         vFields.add(new FieldInfo(_name2, _tbl));
      if(_tbl.equals("T_SPATIAL"))
          vFields.add(new FieldInfo("SPATIAL_TWOLETTER", _tbl));

      vTables.add(new TableInfo(_tbl));
      vTables.add(new TableInfo(tmpTable, join.toString(), TableInfo.OUTER_JOIN));

      queryName = _tbl.substring(2);
      distinct = true;
      qryTableName = null;
      omitRow = true;      

      setFields(vFields);
      setTables(vTables);
      
      whereClause = constr.toString();
      orderClause = _tbl + "." + _name1;
   }

   SubSelectStatement(String entity, String tmpTable) {
      construct(tmpTable, "T_" + entity, "PK_" + entity + "_ID", "FK_" + entity + "_ID", entity + "_NAME", null,"");
   }

   SubSelectStatement(String entity, String name2, String tmpTable, String x1, String x2) {
      construct(tmpTable, "T_" + entity, "PK_" + entity + "_ID", "FK_" + entity + "_ID", entity + "_NAME", name2,"");
   }

   SubSelectStatement(String entity, String tmpTable, String constraint) {
      construct(tmpTable, "T_" + entity, "PK_" + entity + "_ID", "FK_" + entity + "_ID", entity + "_NAME", null,constraint);
   }

   SubSelectStatement(String entity, String name2, String tmpTable, String constraint) {
      construct(tmpTable, "T_" + entity, "PK_" + entity + "_ID", "FK_" + entity + "_ID", entity + "_NAME", name2,constraint);
   }

   SubSelectStatement(String tableName, String pkField, String fkField, String name1, String name2, String tmpTable) {
      construct(tmpTable, tableName, pkField, fkField, name1, name2,"");
   }

   SubSelectStatement(String tableName, String pkField, String fkField, String name1, String name2, String tmpTable, String constraint) {
      construct(tmpTable, tableName, pkField, fkField, name1, name2,constraint);
   }

}