package eionet.rod.web.action;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.dto.DifferenceDTO;
import eionet.rod.dto.UndoDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 *
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/undoinfo")
public class UndoInfoActionBean extends AbstractRODActionBean {

    private String ts;
    private String id;
    private String tab;
    private String op;
    private String user;

    private String date;
    private String action;

    private List<UndoDTO> undoList;

    private DifferenceDTO diffCountries;
    private DifferenceDTO diffVolCountries;
    private DifferenceDTO diffIssues;
    private DifferenceDTO diffClients;
    private DifferenceDTO diffInfo;

    /**
     *
     * @return
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {

        String forwardPage = "/pages/undoinfo.jsp";
        undoList = new ArrayList<UndoDTO>();

        long ts_long = Long.valueOf(ts).longValue();

        Vector rows = RODServices.getDbService().getUndoDao().getUndoInformation(ts_long,op,tab,id);

        for (int i=0; i<rows.size(); i++) {
            Hashtable hash = (Hashtable) rows.elementAt(i);

            String ut = (String) hash.get("undo_time");
            String tabel = (String) hash.get("tab");
            String col = (String) hash.get("col");
            String operation = (String) hash.get("operation");
            String value = (String) hash.get("value");
            String sub_trans_nr = (String) hash.get("sub_trans_nr");
            String currentValue = RODServices.getDbService().getDifferencesDao().getDifferences(Long.valueOf(ut).longValue(),tabel,col);
            if ((value != null && value.trim().equals("")) || (value != null && value.trim().equals("null"))) value = null;
            if ((currentValue != null && currentValue.trim().equals("")) || (currentValue != null && currentValue.trim().equals("null"))) currentValue = null;
            boolean diff = (value != null && currentValue != null && value.equals(currentValue)) || (value == null && currentValue == null)  ;

            if (tab.equals("T_OBLIGATION") && col.equals("FK_DELIVERY_COUNTRY_IDS")) {
                value = addCommas(value);
                currentValue = addCommas(currentValue);
            }

            UndoDTO undo = new UndoDTO();
            undo.setUndoTime(ut);
            undo.setTabel(tabel);
            undo.setColumn(col);
            undo.setOperation(operation);
            undo.setValue(value);
            undo.setCurrentValue(currentValue);
            undo.setSubTransNr(sub_trans_nr);
            undo.setDiff(diff);

            undoList.add(undo);
        }

        int id_int = Integer.valueOf(id).intValue();

        if (!op.equals("D") && !op.equals("UD") && !op.equals("UDD") && tab.equals("T_OBLIGATION")) {
            diffCountries = RODServices.getDbService().getDifferencesDao().getDifferencesInCountries(ts_long,id_int,"N",op);
            diffVolCountries = RODServices.getDbService().getDifferencesDao().getDifferencesInCountries(ts_long,id_int,"Y",op);
            diffIssues = RODServices.getDbService().getDifferencesDao().getDifferencesInIssues(ts_long,id_int,op);
            diffClients = RODServices.getDbService().getDifferencesDao().getDifferencesInClients(ts_long,id_int,"C",op,"A");
            diffInfo = RODServices.getDbService().getDifferencesDao().getDifferencesInInfo(ts_long,id_int,op,"I");
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        Date d = new Date(new Long(ts).longValue());
        date = df.format(d);

        if (op.equals("D"))
            action = "DELETE";
        else if (op.equals("I"))
            action = "INSERT";
        else if (op.equals("U"))
            action = "UPDATE";
        else if (op.equals("UN") || op.equals("UD"))
            action = "UNDO";

        return new ForwardResolution(forwardPage);
    }

    private String addCommas(String value) {

        String valuen = "";
        if (value != null && !value.equals("")) {
            StringTokenizer st = new StringTokenizer(value, ",");
            int tcnt = st.countTokens();

            int z = 1;
            while (st.hasMoreTokens()) {
                valuen = valuen + st.nextToken();
                if (z < tcnt) {
                    valuen = valuen + ", ";
                }
                z++;
            }
        }
        return valuen;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<UndoDTO> getUndoList() {
        return undoList;
    }

    public void setUndoList(List<UndoDTO> undoList) {
        this.undoList = undoList;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public DifferenceDTO getDiffCountries() {
        return diffCountries;
    }

    public void setDiffCountries(DifferenceDTO diffCountries) {
        this.diffCountries = diffCountries;
    }

    public DifferenceDTO getDiffVolCountries() {
        return diffVolCountries;
    }

    public void setDiffVolCountries(DifferenceDTO diffVolCountries) {
        this.diffVolCountries = diffVolCountries;
    }

    public DifferenceDTO getDiffIssues() {
        return diffIssues;
    }

    public void setDiffIssues(DifferenceDTO diffIssues) {
        this.diffIssues = diffIssues;
    }

    public DifferenceDTO getDiffClients() {
        return diffClients;
    }

    public void setDiffClients(DifferenceDTO diffClients) {
        this.diffClients = diffClients;
    }

    public DifferenceDTO getDiffInfo() {
        return diffInfo;
    }

    public void setDiffInfo(DifferenceDTO diffInfo) {
        this.diffInfo = diffInfo;
    }

}
