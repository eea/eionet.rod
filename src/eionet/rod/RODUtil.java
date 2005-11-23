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

import javax.servlet.http.HttpServletRequest;

public class RODUtil {
   /**
   *  Dummy method for getting request parameter
   */
   public static String getParameter(HttpServletRequest req, String prmName) {

      
      String p =  req.getParameter(prmName);
      //strongly ugly and bad quick-fix
      if ( prmName.equals("printmode") &&  ( p ==null || p.trim().equals("") ) )
        p="N";
      return p;
      
   }
   
   /*
    * 
    */
	public static String threeDots(String s, int len){
		
		if (len<=0) return s;
		if (s==null || s.length()==0) return s;
		
		if (s.length()>len){
			StringBuffer buf = new StringBuffer(s.substring(0,len));
			buf.append("...");
			return buf.toString();
		}
		else
			return s;
	}
}
