package eionet.rod.web.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.displaytag.decorator.TableDecorator;

import eionet.rod.RODUtil;
import eionet.rod.dto.VersionDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author altnyris
 * 
 */
public class UpdateHistoryTableDecorator extends TableDecorator {

    /**
     * 
     * @return String
     */
    public String getDesc() {

        StringBuilder ret = new StringBuilder();
        VersionDTO ver = (VersionDTO) getCurrentRowObject();
        String title = null;

        try {
            title = RODServices.getDbService().getUndoDao()
                    .getUndoObjectTitle(Long.valueOf(ver.getUndoTime()).longValue(), ver.getTab());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        title = RODUtil.threeDots(title, 75);
        title = RODUtil.replaceTags(title, true, true);
        if (!ver.getOperation().equals("D")) {
            if (ver.getTab().equals("T_OBLIGATION"))
                ret.append("<a href='obligations/").append(ver.getValue()).append("'>");
            else if (ver.getTab().equals("T_SOURCE"))
                ret.append("<a href='instruments/").append(ver.getValue()).append("'>");

            ret.append(title).append("</a>");
        } else {
            ret.append(title);
        }

        return ret.toString();
    }

    /**
     * 
     * @return String
     */
    public String getObject() {

        StringBuilder ret = new StringBuilder();
        VersionDTO ver = (VersionDTO) getCurrentRowObject();

        String id = ver.getValue();

        if (ver.getTab().equals("T_OBLIGATION"))
            ret.append("<a href='updatehistory?id=").append(id).append("&amp;object=A").append("'>").append("/obligations/")
                    .append(id).append("</a>");
        else if (ver.getTab().equals("T_SOURCE"))
            ret.append("<a href='updatehistory?id=").append(id).append("&amp;object=S").append("'>").append("/instruments/")
                    .append(id).append("</a>");

        return ret.toString();
    }

    /**
     * 
     * @return String
     */
    public String getOper() {

        String ret = "";
        VersionDTO ver = (VersionDTO) getCurrentRowObject();

        if (ver.getOperation().equals("D")) {
            ret = "DELETE";
        } else if (ver.getOperation().equals("I")) {
            ret = "INSERT";
        } else if (ver.getOperation().equals("U")) {
            ret = "UPDATE";
        } else if (ver.getOperation().equals("UN") || ver.getOperation().equals("UD")) {
            ret = "UNDO";
        }

        return ret;
    }

    /**
     * 
     * @return String
     */
    public String getUser() {

        StringBuilder ret = new StringBuilder();
        VersionDTO ver = (VersionDTO) getCurrentRowObject();
        String user = "";

        try {
            user = RODServices.getDbService().getUndoDao().getUndoUser(Long.valueOf(ver.getUndoTime()).longValue(), ver.getTab());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        ret.append("<a href='updatehistory?username=").append(user).append("'>").append(user).append("</a>");

        return ret.toString();
    }

    /**
     * 
     * @return String
     */
    public String getTime() {

        VersionDTO search = (VersionDTO) getCurrentRowObject();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        Date date = new Date(new Long(search.getUndoTime()).longValue());
        String d = df.format(date);

        return d;
    }

}
