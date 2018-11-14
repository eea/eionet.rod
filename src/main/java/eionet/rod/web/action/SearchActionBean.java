package eionet.rod.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
import eionet.sparqlClient.helpers.ResultValue;

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
     * Perform a full text search using bif:contains. This is a query
     * that is supposed to only return objects stored on ROD. We could
     * in the future get other organisations to provide instruments using the
     * same vocabulary. We don't want to see those currently.
     * @return Resolution
     * @throws ServiceException if Sparql endpoint is unaccessible.
     */
    @DefaultHandler
    public Resolution execute() throws ServiceException {

        if (!StringUtils.isBlank(expression)) {
            
            String query = "PREFIX rod: <http://rod.eionet.europa.eu/schema.rdf#> "
                + "PREFIX dct: <http://purl.org/dc/terms/> "
                + "SELECT DISTINCT ?subject ?type ?found ?name "
                + "FROM <https://rod.eionet.europa.eu/obligations/rdf> "
                + "FROM <https://rod.eionet.europa.eu/instruments/rdf> "
                + "FROM <https://rod.eionet.europa.eu/clients/rdf> "
                + "WHERE { "
                + "?subject a ?type . FILTER (?type IN (rod:Instrument, rod:Obligation, rod:Client)) "
                + "?subject ?p ?found . FILTER (contains(str(?found), \""+expression+"\")) . "
                + "OPTIONAL { { ?subject dct:title ?name } UNION { ?subject rod:clientName ?name } } "
                + "}";
            
            String CRSparqlEndpoint = RODServices.getFileService().getStringProperty(FileServiceIF.CR_SPARQL_ENDPOINT);
            QueryExecutor executor = new QueryExecutor();
            executor.executeQuery(CRSparqlEndpoint, query);
            result = executor.getResults();
            
            // Remove duplicate subjects
            removeDuplicates();
        }
        return new ForwardResolution("/pages/simpleSearch.jsp");
    }

    /**
     * 
     */
    private void removeDuplicates() {
        List<String> existingSubjects = new ArrayList<String>();
        ArrayList<HashMap<String, ResultValue>> newRows = new ArrayList<HashMap<String, ResultValue>>();
        if (result != null && result.getRows() != null) {
            ArrayList<HashMap<String, ResultValue>> rows = result.getRows();
            for (Iterator<HashMap<String, ResultValue>> it = rows.iterator(); it.hasNext(); ){
                HashMap<String, ResultValue> row = it.next();
                ResultValue subject = row.get("subject");
                if (subject != null && !existingSubjects.contains(subject.getValue())) {
                    existingSubjects.add(subject.getValue());
                    newRows.add(row);
                }
            }
            result.setRows(newRows);
        }
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
