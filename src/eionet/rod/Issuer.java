package eionet.rod;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.XSQLException;
import com.tee.xmlserver.XMLSource;

public class Issuer extends ROServletAC {
  protected String setXSLT(HttpServletRequest req) {
      return "../app/issuer.xsl";
   }

  protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
    String querySource = "../app/issuer.xrs";

    String param = params.getParameter(Constants.ID_PARAM);

    String queryPars[][] = {{"ID", param}};

    DataSourceIF dataSrc = XMLSource.getXMLSource(querySource, params.getRequest());

    dataSrc.setParameters(queryPars);

      return dataSrc;
  }

}