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



package eionet.rod;
import com.tee.xmlserver.QueryStatement;
import java.util.Vector;
import com.tee.xmlserver.TableInfo;
import com.tee.xmlserver.FieldInfo;

/**
* Helper class for adding metainfo rowset to the datasource
*
*/

public class MetaData extends QueryStatement implements Constants {

  private Vector vTables;
  private Vector vFields;

  private String _name;
  private String _tblName;
 
  public MetaData(String name, String tableName)  {
    _name=name;
    _tblName=tableName;
    init();
  }

  private void init() {
    queryName=_name;
    vTables=new Vector();
    vTables.add(new TableInfo(_tblName));
    vFields=new Vector();

    FieldInfo fld  = new FieldInfo(TIMESTAMP_FILEDNAME, _tblName );
    fld.setFieldExpr("MAX(" + TIMESTAMP_FILEDNAME + ") AS " + TIMESTAMP_FILEDNAME );
    vFields.add( fld );

    setTables(vTables);
    setFields(vFields);
  }
}