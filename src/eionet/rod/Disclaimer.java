package eionet.rod;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.XSQLException;
import com.tee.xmlserver.XMLSource;

public class Disclaimer extends ROServletAC {
  protected String setXSLT(HttpServletRequest req) {
      return "../app/disclaimer.xsl";
   }

  protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
    String querySource = "../app/disclaimer.xrs";
    DataSourceIF dataSrc = XMLSource.getXMLSource(querySource, params.getRequest());
    addMetaInfo(dataSrc);
    return userInfo(params.getRequest(),dataSrc);
  }
}