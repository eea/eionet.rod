package eionet.rod;
import com.tee.xmlserver.BaseServletAC;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LogoutServlet  extends BaseServletAC {

 public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      freeSession(req);
      //printPage(res, "<html><script>document.location='index.html'</script></html>");
      String location = "index.html";
      res.sendRedirect(location);      
   }

}