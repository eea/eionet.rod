package eionet.rod;
import com.tee.xmlserver.BaseServletAC;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.DataSourceIF;

public class LogoutServlet  extends ROServletAC { //BaseServletAC {

 protected DataSourceIF prepareDataSource( Parameters prms) throws com.tee.xmlserver.XSQLException {
  return null;
 }

 public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      //quick fix, if acl admin performs logout action, the ACL is reset
      String userName = getUser(req).getUserName();
      String aclControl = "c"; //!! hard coded;
      boolean aclAdmin = false;
      try {
        aclAdmin = getAcl().checkPermission(userName, aclControl);
      } catch (Exception e ) {
        log("Error with ACL " + e.toString());
      }
      
      if (aclAdmin)
        resetAcl();
        
      freeSession(req);
      //printPage(res, "<html><script>document.location='index.html'</script></html>");
      String location = "index.html";
      res.sendRedirect(location);      
   }

}