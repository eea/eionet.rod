package eionet.rod;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.XSQLException;
import com.tee.xmlserver.XMLSource;

public class Analysis extends ROServletAC {
  protected String setXSLT(HttpServletRequest req) {
      return "../app/analysis.xsl";
   }

  protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
    String querySource = "../app/analysis.xrs";
    //String param = params.getParameter(Constants.ID_PARAM);
    //DataSourceIF dataSrc = XMLSource.getXMLSource(querySource, req);
    DataSourceIF dataSrc = XMLSource.getXMLSource(querySource, params.getRequest());
    addMetaInfo(dataSrc);
    return userInfo(params.getRequest(),dataSrc);
  }
}