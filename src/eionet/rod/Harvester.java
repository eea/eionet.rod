package eionet.rod;

import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.XSQLException;
import com.tee.xmlserver.AppUserIF;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import com.tee.xmlserver.Logger;
import java.io.IOException;

import eionet.rod.countrysrv.Extractor;

public class Harvester extends ROEditServletAC {


  public Harvester()  {
  }

/*   protected String setXSLT(HttpServletRequest req) {
      return PREFIX + "harvester.xsl";
   } */

   protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
    return null;
   }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) 
         throws javax.servlet.ServletException, java.io.IOException {
    try {
      AppUserIF user = getUser(req);
      java.sql.Connection conn = (user != null) ? user.getConnection() : null;

      if (conn == null)
        throw new XSQLException(null, "Not authenticated user");

    if  ( ! getAcl(Constants.ACL_ADMIN_NAME).checkPermission( user.getUserName(), Constants.ACL_VIEW_PERMISSION ))
        throw new XSQLException(null, "Not authorized user");

    StringBuffer s = new StringBuffer();
  s.append("<html>")
    .append("<script language = 'javascript'> function harvest( mode ) {")
      .append("var ff = document.f;")
      .append(" ff.action = ff.action + '?MODE=' + mode;")
      .append("alert('It takes some to harvest the data. Please wait');")
      .append("document.body.style.cursor='wait';")
      .append("ff.submit();")
      .append("} </script>")
      .append("<body bgcolor=\"#f0f0f0\">")
      .append("<form align='center' name=\"f\" method=\"POST\" action=\"harvester.jsv\">")
      .append("<b>Select data, you want to be harvested:</b>")
      .append("<br>").append("<input width='200'style=\"width:200\"  type='button' onClick='harvest(" + Extractor.ALL_DATA + ")' value='All'></input>")
      .append("<br>").append("<input width='200'style=\"width:200\" type='button' onClick='harvest(" + Extractor.DELIVERIES + ")' value='Deliveries'></input>")            
      .append("<br>").append("<input width='200'style=\"width:200\" type='button' onClick='harvest(" + Extractor.ROLES + ")' value='Roles'></input>")                  
      .append("</form>")
      .append("</body></html>");


    //buid html
    res.setContentType("text/html");
    res.getWriter().write( s.toString() );
      
     } catch (Exception xe) {
         printError(xe, res);
     }
  }

protected void doPost(HttpServletRequest req, HttpServletResponse res) 
         throws javax.servlet.ServletException, java.io.IOException {

      res.setContentType("text/html");

    res.getWriter().write("<html><body bgcolor=\"#f0f0f0\">");

      try {
        AppUserIF user = getUser(req);
        java.sql.Connection conn = (user != null) ? user.getConnection() : null;

        if (conn == null)
          throw new XSQLException(null, "Not authenticated user");
        else {
 
          //harvest
          //res.getWriter().write("Harvester starts. It may take several minutes to harvest");
          try {
            if  ( ! getAcl(Constants.ACL_ADMIN_NAME).checkPermission( user.getUserName(), Constants.ACL_UPDATE_PERMISSION ))
              throw new XSQLException(null, "Not authenticated user");
          
            int mode = Integer.parseInt( req.getParameter("MODE") ); 
            Extractor.harvest(mode);
            res.getWriter().write("<b>Harvested!</b> See log for details");
          } catch (Exception e ) {
             printError(e, res);
          }
        }
      } catch (XSQLException xs ) {
        printError(xs, res);
      }

    res.getWriter().write("</body></html>");      


  }

  private void printError( Exception e, HttpServletResponse res  ) throws IOException {
    res.setContentType("text/html");  
    res.getWriter().write("<html><body bgcolor=\"#f0f0f0\">");
    res.getWriter().write("<b>Error:</b><br>");
    res.getWriter().write(e.toString());
    //e.printStackTrace( res.getWriter() );
    res.getWriter().write("</body></html>");
  } 
}