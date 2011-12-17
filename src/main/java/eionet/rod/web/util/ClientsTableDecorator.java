package eionet.rod.web.util;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.decorator.TableDecorator;

import eionet.rod.RODUtil;
import eionet.rod.dto.ClientDTO;

/**
 *
 * @author altnyris
 *
 */
public class ClientsTableDecorator extends TableDecorator {

    /**
     *
     * @return String
     */
    public String getClientName() {

        HttpServletRequest req = (HttpServletRequest) getPageContext().getRequest();
        String path = req.getContextPath();
        StringBuilder ret = new StringBuilder();
        ClientDTO client = (ClientDTO) getCurrentRowObject();
        ret.append("<a href='").append(path).append("/clients/").append(client.getClientId()).append("'>");
        if (!RODUtil.isNullOrEmpty(client.getName()))
            ret.append(RODUtil.replaceTags(client.getName()));
        ret.append("</a>");

        return ret.toString();
    }

}
