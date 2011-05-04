package eionet.rod.web.action;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import eionet.sparqlClient.helpers.QueryExecutor;
import eionet.sparqlClient.helpers.QueryResult;

/**
 *
 * @author altnyris
 *
 */
@UrlBinding("/simpleSearch")
public class SearchActionBean extends AbstractRODActionBean {
    
    private String expression;
    private QueryResult result;

    /**
     *
     * @return Resolution
     * @throws ServiceException 
     */
    @DefaultHandler
    public Resolution execute() throws ServiceException {

        if (!StringUtils.isBlank(expression)) {
            
            String query = "PREFIX rod: <http://rod.eionet.europa.eu/schema.rdf#> "
                + "PREFIX dc: <http://purl.org/dc/terms/> "
                + "SELECT DISTINCT ?subject ?type ?found ?name WHERE { "
                + "?subject a ?type . FILTER ( ?type = rod:Instrument || ?type = rod:Obligation || ?type = rod:Client) "
                + "?subject ?p ?found . ?found bif:contains \""+expression+"\" . "
                + "OPTIONAL { { ?subject dc:title ?name } UNION { ?subject rod:clientName ?name } } "
                + "}";
            
            String CRSparqlEndpoint = RODServices.getFileService().getStringProperty(FileServiceIF.CR_SPARQL_ENDPOINT);
            QueryExecutor executor = new QueryExecutor();
            executor.executeQuery(CRSparqlEndpoint, query);
            result = executor.getResults();
        }
        return new ForwardResolution("/pages/simpleSearch.jsp");
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public QueryResult getResult() {
        return result;
    }

    public void setResult(QueryResult result) {
        this.result = result;
    }


}
