package eionet.rod.countrysrv.servlets;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.XSQLException;
import com.tee.xmlserver.XMLSource;

public class Index extends CSServletAC {
  protected String setXSLT(HttpServletRequest req) {
      return PREFIX + "csindex.xsl";
   }

  protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
    String querySource = PREFIX + "csindex.xml";

    HttpServletRequest req = params.getRequest();
    DataSourceIF dataSrc = XMLSource.getXMLSource(querySource, req);
    //dataSrc.setParameters(queryPars);

    addMetaInfo(dataSrc);
    
      return userInfo( req, dataSrc);
  }

}