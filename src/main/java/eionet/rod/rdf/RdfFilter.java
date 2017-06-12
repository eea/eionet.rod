package eionet.rod.rdf;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eionet.rod.web.action.ContactsActionBean;
import org.apache.commons.lang.StringUtils;

import eionet.rdfexport.RDFExportService;
import eionet.rdfexport.RDFExportServiceImpl;
import eionet.rdfexport.RDFExportServiceJSONLD;
import eionet.rod.util.sql.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Risto Alt
 *
 */
public class RdfFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RdfFilter.class);

    private static final String ACCEPT_RDF_HEADER = "application/rdf+xml";
    private static final String ACCEPT_JSONLD_HEADER = "application/ld+json";

    private String identifier = null;
    private String table = null;

    /**
     * Take this filter out of service.
     */
    @Override
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
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        this.identifier = null;
        this.table = null;

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();
        String cpath = httpRequest.getContextPath();
        //return data in RDF/XML Form
        if (uri != null && uri.endsWith("/rdf")) {
            boolean rdf = extractTableAndIdentifier(uri, cpath);
            if (rdf) {
                Connection conn = null;
                try {
                    conn = ConnectionUtil.getConnection();
                    httpResponse.setContentType(ACCEPT_RDF_HEADER);
                    httpResponse.setCharacterEncoding("UTF-8");

                    Properties props = new Properties();
                    props.load(getClass().getClassLoader().getResourceAsStream("rdfexport.properties"));
                    RDFExportService rdfExportService = new RDFExportServiceImpl(new PrintStream(httpResponse.getOutputStream()), conn, props);
                    rdfExportService.exportTable(table, identifier);
                    return;
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new ServletException(e.getMessage(), e);
                }
                finally{
                    ConnectionUtil.closeConnection(conn);
                }
            }
        } else if (uri != null && uri.endsWith(".rdf")) {
            // Also check for ".rdf" to ensure that old URLs work
            uri = uri.replace(".rdf", "/rdf");
            if (isRdfTable(uri, cpath)) {
                httpResponse.setStatus(HttpServletResponse.SC_SEE_OTHER);
                httpResponse.setHeader("Location", uri);
                return;
            }
        } 
        //return data in JSON-LD Form
        if (uri != null && uri.endsWith("/json")) {
            boolean jsonld = extractTableAndIdentifier(uri, cpath);
            if (jsonld) {
                Connection conn = null;
                try {
                    conn = ConnectionUtil.getConnection();
                    httpResponse.setContentType(ACCEPT_JSONLD_HEADER);
                    httpResponse.setCharacterEncoding("UTF-8");

                    Properties props = new Properties();
                    props.load(getClass().getClassLoader().getResourceAsStream("rdfexport.properties"));
                    RDFExportService rdfExportService = new RDFExportServiceJSONLD(new PrintStream(httpResponse.getOutputStream()), conn, props);
                    rdfExportService.exportTable(table, identifier);
                    return;
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new ServletException(e.getMessage(), e);
                }
                finally{
                    ConnectionUtil.closeConnection(conn);
                }
            }
        }
        else if (StringUtils.contains(httpRequest.getHeader("accept"), ACCEPT_RDF_HEADER)) {
            if (isRdfTable(uri, cpath)) {
                httpResponse.sendRedirect(uri + "/rdf");
                return;
            }
        }
        else if (StringUtils.contains(httpRequest.getHeader("accept"), ACCEPT_JSONLD_HEADER)) {
            if (isRdfTable(uri, cpath)) {
                httpResponse.sendRedirect(uri + "/json");
                return;
            }
        }
        // Pass control on to the next filter
        chain.doFilter(request, response);
    }

    private boolean extractTableAndIdentifier(String uri, String cpath) {
        boolean ret = false;
        if (!StringUtils.isBlank(uri)) {
            if (!StringUtils.isBlank(cpath) && !cpath.equals("/")) {
                uri = uri.replace(cpath, "");
            }

            if (uri.startsWith("/") && uri.length() > 1) {
                uri = uri.substring(1);
            }

            String[] st = uri.split("/");
            if (st != null && st.length > 0) {
                table = st[0];
                if (st.length > 2) {
                    identifier = st[1];
                }
                ret = true;
            }
        }
        return ret;
    }

    private boolean isRdfTable(String uri, String cpath) throws IOException {
        boolean ret = false;
        Properties props = new Properties();
        props.load(getClass().getClassLoader().getResourceAsStream("rdfexport.properties"));
        List<String> tables = Arrays.asList(props.getProperty("tables").split(" "));
        if (tables != null && tables.size() > 0) {
            extractTableAndIdentifier(uri, cpath);
            if (table != null && tables.contains(table)) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Place this filter into service.
     *
     * @param filterConfig - The filter configuration object
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

}
