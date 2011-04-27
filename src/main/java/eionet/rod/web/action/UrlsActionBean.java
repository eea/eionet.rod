package eionet.rod.web.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.RODUtil;
import eionet.rod.dto.UrlDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 *
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/urls.rdf")
public class UrlsActionBean extends AbstractRODActionBean {

    protected static final String rdfHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    /**
     *
     * @return
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {

        return new StreamingResolution("application/rdf+xml;charset=UTF-8") {
            public void stream(HttpServletResponse response) throws Exception {
                String rdf = generateRDF();
                response.getWriter().write(rdf);
            }
        }.setFilename("urls.rdf");
    }

    private String generateRDF() throws ServiceException {
        StringBuffer s = new StringBuffer();
        s.append(rdfHeader)
        .append("<rdf:RDF")
        .append(" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"")
        .append(" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"")
        .append(" xmlns:seis=\"http://www.eionet.europa.eu/rdf/seis/\"")
        .append(">");

        List<UrlDTO> obligationUrls = RODServices.getDbService().getObligationDao().getObligationsUrls();
        s.append(iterateUrls(obligationUrls));

        List<UrlDTO> instrumentUrls = RODServices.getDbService().getSourceDao().getInstrumentsUrls();
        s.append(iterateUrls(instrumentUrls));

        s.append("</rdf:RDF>");

        return s.toString();
    }

    private String iterateUrls(List<UrlDTO> urls) throws ServiceException {
        StringBuilder s = new StringBuilder();

        for(Iterator<UrlDTO> it = urls.iterator(); it.hasNext(); ) {
            UrlDTO dto = it.next();
            String title = RODUtil.replaceTags(dto.getTitle(), true, true);
            String url = dto.getUrl();
            if (!RODUtil.isNullOrEmpty(url)) {
                if (!RODUtil.isNullOrEmpty(title) && !title.equals(url)) {
                    s.append("<seis:Resource rdf:about=\"").append(url).append("\">")
                    .append("<rdfs:label>").append(title).append("</rdfs:label>")
                    .append("</seis:Resource>");
                } else {
                    s.append("<seis:Resource rdf:about=\"").append(url).append("\"/>");
                }
            }
        }

        return s.toString();
    }


}
