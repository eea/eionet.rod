package eionet.rod.countrysrv.servlets;

import eionet.rod.ROServletAC;
import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.XSQLException;
import com.tee.xmlserver.XMLSource;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Search extends ROServletAC { // was: CSServletAC, changed to be able to use ACL

  protected String setXSLT(HttpServletRequest req) {
      return "../app/cssearch.xsl";
   }

  protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
    String querySource = "../app/cssearch.xml";
    HttpServletRequest req = params.getRequest();
    DataSourceIF dataSrc = XMLSource.getXMLSource(querySource, params.getRequest());

    addMetaInfo(dataSrc);
    return userInfo(req, dataSrc);
  }

}