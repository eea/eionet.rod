package eionet.rod.countrysrv.servlets;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.XSQLException;
import com.tee.xmlserver.XMLSource;

public class Deliveries extends CSServletAC {
  protected String setXSLT(HttpServletRequest req) {
      return "../app/csdeliveries.xsl";
   }

  protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
    String querySource = "../app/csdeliveries.xml";

    String param = params.getParameter("ACT_DETAILS_ID");
    String param2 = params.getParameter("COUNTRY_ID");
    
    String queryPars[][] = {{"ACT_DETAILS_ID", param}, {"COUNTRY_ID", param2}};

    DataSourceIF dataSrc = XMLSource.getXMLSource(querySource, params.getRequest());

    dataSrc.setParameters(queryPars);
      
      return dataSrc;    
  }

}