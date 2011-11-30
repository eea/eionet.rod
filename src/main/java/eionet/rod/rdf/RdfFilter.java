/**
 *
 */
package eionet.rod.rdf;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import eionet.rod.util.sql.ConnectionUtil;

/**
 * @author Risto Alt
 *
 */
public class RdfFilter implements Filter {

    private static final String ACCEPT_RDF_HEADER = "application/rdf+xml";

    private String identifier = null;
    private String table = null;

    /**
     * Take this filter out of service.
     */
    public void destroy() {
    }

    /**
     * Check if RDF is requested.
     *
     * @param request - The servlet request we are processing
     * @param result - The servlet response we are creating
     * @param chain - The filter chain we are processing
     * @exception IOException - if an input/output error occurs
     * @exception ServletException - if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        this.identifier = null;
        this.table = null;

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();
        if (uri != null && uri.endsWith("/rdf")){
            String cpath = httpRequest.getContextPath();
            if (!StringUtils.isBlank(cpath) && !cpath.equals("/")) {
                uri = uri.replace(cpath, "");
            }

            if (uri.startsWith("/") && uri.length() > 1) {
                uri = uri.substring(1);
            }

            String[] st = uri.split("/");

            if (st != null && ((st.length > 2 && st[2].equals("rdf")) || (st.length == 2 && st[1].equals("rdf")))) {
                table = st[0];
                if (st.length > 2) {
                    identifier = st[1];
                }

                try {
                    Connection con = ConnectionUtil.getConnection();
                    httpResponse.setContentType(ACCEPT_RDF_HEADER);
                    httpResponse.setCharacterEncoding("UTF-8");
                    GenerateRDF genRdf = new GenerateRDF(new PrintStream(httpResponse.getOutputStream()), con);
                    genRdf.exportTable(table, identifier);
                    genRdf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ServletException(e.getMessage(), e);
                }
            }
            // Also check for ".rdf" to ensure that old URLs work
        } else if(uri != null && uri.endsWith(".rdf")) {
            uri = uri.replace(".rdf", "/rdf");
            httpResponse.setStatus(HttpServletResponse.SC_SEE_OTHER);
            httpResponse.setHeader("Location", uri);
        } else if (StringUtils.contains(httpRequest.getHeader("accept"), ACCEPT_RDF_HEADER)){
            httpResponse.sendRedirect(uri + "/rdf");
        } else {
            // Pass control on to the next filter
            chain.doFilter(request, response);
        }
    }

    /**
     * Place this filter into service.
     *
     * @param filterConfig - The filter configuration object
     */
    public void init(FilterConfig filterConfig) throws ServletException {
    }

}
