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
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.DataSourceIF;

public class LogoutServlet extends HttpServlet { //BaseServletAC {

 protected DataSourceIF prepareDataSource( Parameters prms) throws com.tee.xmlserver.XSQLException {
  return null;
 }

	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// quick fix, if acl admin performs logout action, the ACL is reset
		EionetCASFilter.attachEionetLoginCookie(res,false);
		res.sendRedirect(EionetCASFilter.getCASLogoutURL(req));
	}
	
}