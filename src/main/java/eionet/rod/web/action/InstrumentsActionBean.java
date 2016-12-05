package eionet.rod.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

import eionet.acl.AccessControlListIF;
import eionet.acl.AccessController;
import eionet.acl.SignOnException;

import eionet.rod.Attrs;
import eionet.rod.Constants;
import eionet.rod.RODUtil;
import eionet.rod.UNSEventSender;
import eionet.rod.dto.ClientDTO;
import eionet.rod.dto.DifferenceDTO;
import eionet.rod.dto.HierarchyInstrumentDTO;
import eionet.rod.dto.InstrumentDTO;
import eionet.rod.dto.InstrumentFactsheetDTO;
import eionet.rod.dto.InstrumentsListDTO;
import eionet.rod.dto.LookupDTO;
import eionet.rod.dto.SourceClassDTO;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IObligationDao;
import eionet.rod.services.modules.db.dao.ISourceDao;
import eionet.rod.services.modules.db.dao.IUndoDao;

/**
 *
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/instruments/{instId}/{action}")
public class InstrumentsActionBean extends AbstractRODActionBean implements ValidationErrorHandler {

    @ValidateNestedProperties({
        @Validate(field = "sourceTitle", on = {"edit", "add"}, required = true),
        @Validate(field = "sourceValidFrom", on = {"edit", "add"}, mask = "([0-9]{2})/([0-9]{2})/([0-9]{4})"),
        @Validate(field = "sourceEcEntryIntoForce", on = {"edit", "add"}, mask = "([0-9]{2})/([0-9]{2})/([0-9]{4})"),
        @Validate(field = "sourceEcAccession", on = {"edit", "add"}, mask = "([0-9]{2})/([0-9]{2})/([0-9]{4})"),
        @Validate(field = "sourceUrl", on = {"edit", "add"}, mask = "^((ht|f)tps?://).*"),
        @Validate(field = "sourceIssuedByUrl", on = {"edit", "add"}, mask = "^((ht|f)tps?://).*"),
        @Validate(field = "sourceFKClientId", on = {"edit", "add"}, required = true),
        @Validate(field = "sourceSecretariatUrl", on = {"edit", "add"}, mask = "^((ht|f)tps?://).*"),
        @Validate(field = "sourceVerified", on = {"edit", "add"}, mask = "([0-9]{2})/([0-9]{2})/([0-9]{4})"),
        @Validate(field = "sourceNextUpdate", on = {"edit", "add"}, mask = "([0-9]{2})/([0-9]{2})/([0-9]{4})")})
    private InstrumentFactsheetDTO instrument;
    private InstrumentsListDTO hierarchyInstrument;
    private List<HierarchyInstrumentDTO> hierarchyInstruments;
    private String instId;
    private String id;
    private String dgenv;
    private String hierarchyTree;
    private String mode;
    private String action;

    private List<LookupDTO> dgenvlist;
    private List<ClientDTO> clients;
    private List<InstrumentDTO> parentInstrumentsList;
    private String parentInstrumentId;

    private List<SourceClassDTO> allSourceClasses;
    private List<SourceClassDTO> sourceClasses;
    private List<SourceClassDTO> instrumentSourceClasses;
    private List<String> selSourceClasses;
    private List<String> selectedSourceClasses;

    private IUndoDao undoDao;
    private ISourceDao sourceDao;
    private long ts;

    /**
     *
     * @return Resolution
     * @throws ServiceException
     * @throws IOException
     * @throws ServletException
     */
    @DefaultHandler
    public Resolution init() throws ServiceException, IOException, ServletException {

        String forwardPage = "/pages/instrument.jsp";
        if (!RODUtil.isNullOrEmpty(instId)) {
            if (instId.equals("new")) {
                instrument = new InstrumentFactsheetDTO();
            } else {
                instrument = RODServices.getDbService().getSourceDao().getInstrumentFactsheet(instId);
            }

            if (!instId.equals("new") && (instrument == null || !RODUtil.isNumber(instId))) {
                return new ErrorResolution(HttpServletResponse.SC_NOT_FOUND);
            }

            dgenv = RODServices.getDbService().getSourceDao().getDGEnvNameByInstrumentId(instId);

            if (instId.equals("new") || (!RODUtil.isNullOrEmpty(action) && action.equals("edit"))) {
                forwardPage = "/pages/einstrument.jsp";

                dgenvlist = RODServices.getDbService().getSourceDao().getLookupList("DGS");
                clients = RODServices.getDbService().getClientDao().getAllClients();
                allSourceClasses = RODServices.getDbService().getSourceDao().getAllSourceClasses();

                if (!RODUtil.isNullOrEmpty(action) && action.equals("edit")) {
                    parentInstrumentsList = RODServices.getDbService().getSourceDao().getParentInstrumentsList(instId);
                    parentInstrumentId = RODServices.getDbService().getSourceDao().getParentInstrumentId(instId);
                    instrumentSourceClasses = RODServices.getDbService().getSourceDao().getSourceClassesByInstrumentId(instId);
                    sourceClasses = initSourceClasses();
                } else if (instId.equals("new")) {
                    parentInstrumentsList = RODServices.getDbService().getSourceDao().getParentInstrumentsList("-1");
                    sourceClasses = allSourceClasses;
                }
            }
        } else {
            if (RODUtil.isNullOrEmpty(id)) {
                id = "1";
            }
            hierarchyInstrument = RODServices.getDbService().getSourceDao().getHierarchyInstrument(id);
            String parentId = hierarchyInstrument.getParentId();
            boolean hasParent = true;
            if (RODUtil.isNullOrEmpty(parentId)) {
                hasParent = false;
            }

            hierarchyTree = RODServices.getDbService().getSourceDao().getHierarchy(id, hasParent, mode);

            hierarchyInstruments = RODServices.getDbService().getSourceDao().getHierarchyInstruments(id);

            if (mode != null && mode.equals("X")) {
                forwardPage = "/pages/instrumentsx.jsp";
            } else {
                forwardPage = "/pages/instruments.jsp";
            }
        }

        return new ForwardResolution(forwardPage);
    }

    private List<SourceClassDTO> initSourceClasses() {
        List<SourceClassDTO> ret = new ArrayList<SourceClassDTO>();

        for (Iterator<SourceClassDTO> it = allSourceClasses.iterator(); it.hasNext();) {
            boolean hasSc = false;
            SourceClassDTO sc = it.next();
         if(instrumentSourceClasses!=null){
            for (Iterator<SourceClassDTO> it2 = instrumentSourceClasses.iterator(); it2.hasNext();) {
                SourceClassDTO sc2 = it2.next();
                if (sc.getClassId() == sc2.getClassId()) {
                    hasSc = true;
                }
            }
        }
            if (!hasSc) {
                ret.add(sc);
            }
        }
        return ret;
    }

    public Resolution add() throws ServiceException {

        Resolution resolution = new ForwardResolution("/pages/instrument.jsp");

        String userName = getUserName();
        if (RODUtil.isNullOrEmpty(userName)) {
            handleRodException("You are not logged in!", Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/einstrument.jsp");
        }

        boolean ins = false;
        try {
            AccessControlListIF acl = AccessController.getAcl(Constants.ACL_LI_NAME);
            ins = acl.checkPermission(userName, Constants.ACL_INSERT_PERMISSION);
        } catch (Exception e) {
            handleRodException(e.getMessage(), Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/einstrument.jsp");
        }

        if (!ins) {
            handleRodException("Isufficient permissions", Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/einstrument.jsp");
        }

        if (instrument.getSourceCode() == null) {
            instrument.setSourceCode("");
        }

        sourceDao = RODServices.getDbService().getSourceDao();
        Integer instrumentId = sourceDao.addInstrument(instrument);
        instId = instrumentId.toString();

        try {
            String aclPath = "/instruments/" + instId;
            HashMap acls = AccessController.getAcls();
            if (!acls.containsKey(aclPath)) {
                AccessController.addAcl(aclPath, userName, "");
            }
        } catch (SignOnException e) {
            e.printStackTrace();
        }

        processLinkedTables();

        sendEvent(false);

        initAfterAddEdit();

        return resolution;
    }

    public Resolution edit() throws ServiceException {

        Resolution resolution = new ForwardResolution("/pages/instrument.jsp");

        String userName = getUserName();
        if (RODUtil.isNullOrEmpty(userName)) {
            handleRodException("You are not logged in!", Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/einstrument.jsp");
        }

        String acl_p = (instId == null || instId == "") ? Constants.ACL_LI_NAME : (Constants.ACL_LI_NAME + "/" + instId);
        boolean upd = false;
        try {
            AccessControlListIF acl = AccessController.getAcl(acl_p);
            upd = acl.checkPermission(userName, Constants.ACL_UPDATE_PERMISSION);
        } catch (Exception e) {
            handleRodException(e.getMessage(), Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/einstrument.jsp");
        }

        if (!upd) {
            handleRodException("Isufficient permissions", Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/einstrument.jsp");
        }

        instrument.setSourceId(new Integer(instId).intValue());

        processEditDelete("U", userName);

        if (instrument.getSourceCode() == null) {
            instrument.setSourceCode("");
        }

        sourceDao.editInstrument(instrument);

        processLinkedTables();

        sendEvent(true);

        initAfterAddEdit();

        return resolution;
    }

    public Resolution delete() throws ServiceException {

        Resolution resolution = new ForwardResolution("/pages/instruments.jsp");

        String userName = getUserName();
        if (RODUtil.isNullOrEmpty(userName)) {
            handleRodException("You are not logged in!", Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/einstrument.jsp");
        }

        String acl_p = (instId == null || instId == "") ? Constants.ACL_LI_NAME : (Constants.ACL_LI_NAME + "/" + instId);
        boolean del = false;
        try {
            AccessControlListIF acl = AccessController.getAcl(acl_p);
            del = acl.checkPermission(userName, Constants.ACL_DELETE_PERMISSION);
        } catch (Exception e) {
            handleRodException(e.getMessage(), Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/einstrument.jsp");
        }

        if (!del) {
            handleRodException("Isufficient permissions", Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/einstrument.jsp");
        }

        processEditDelete("D", userName);

        hierarchyTree = RODServices.getDbService().getSourceDao().getHierarchy("1", false, null);
        hierarchyInstruments = RODServices.getDbService().getSourceDao().getHierarchyInstruments("1");

        return resolution;
    }

    private void processEditDelete(String state, String userName) throws ServiceException {

        undoDao = RODServices.getDbService().getUndoDao();
        sourceDao = RODServices.getDbService().getSourceDao();
        ts = System.currentTimeMillis();

        if (state != null && state.equals("U")) {
            undoDao.insertIntoUndo(null, instId, "U", "T_SOURCE", "PK_SOURCE_ID", ts, "", "y", null);
            undoDao.insertIntoUndo(null, instId, "U", "T_CLIENT_LNK", "FK_OBJECT_ID", ts, "AND TYPE='S'", "y", null);
        }

        String url = "instruments/" + instId;
        undoDao.insertIntoUndo(ts, "T_SOURCE", "REDIRECT_URL", "L", "y", "n", url, 0, "n");
        undoDao.insertIntoUndo(ts, "T_SOURCE", "A_USER", "K", "y", "n", userName, 0, "n");
        undoDao.insertIntoUndo(ts, "T_SOURCE", "TYPE", "T", "y", "n", "L", 0, "n");

        if (state != null && state.equals("D")) {
            String acl_path = "/instruments/" + instId;
            undoDao.insertIntoUndo(ts, "T_SOURCE", "ACL", "ACL", "y", "n", acl_path, 0, "n");
        }

        // delete all linked parameter and medium records and in delete mode also the self record
        if (state != null && state.equals("U")) {
            RODServices.getDbService().getClientDao().deleteParameterLink(Integer.valueOf(instId));
        }

        delActivity(state);
    }

    private void delActivity(String op) throws ServiceException {

        undoDao.insertTransactionInfo(instId, "A", "T_CLIENT_LNK", "FK_OBJECT_ID", ts, "AND TYPE='S'");
        undoDao.insertTransactionInfo(instId, "A", "T_SOURCE_LNK", "FK_SOURCE_CHILD_ID", ts, "AND CHILD_TYPE='S'");
        undoDao.insertTransactionInfo(instId, "A", "T_SOURCE_LNK", "FK_SOURCE_PARENT_ID", ts, "AND PARENT_TYPE='S'");
        undoDao.insertTransactionInfo(instId, "A", "T_SOURCE", "PK_SOURCE_ID", ts, "");

        if (op != null && op.equals("D")) {
            undoDao.insertIntoUndo(null, instId, op, "T_CLIENT_LNK", "FK_OBJECT_ID", ts, "AND TYPE='S'", "y", null);
            RODServices.getDbService().getClientDao().deleteSourceLink(Integer.valueOf(instId));
        }

        undoDao.insertIntoUndo(null, instId, op, "T_SOURCE_LNK", "FK_SOURCE_CHILD_ID", ts, "AND CHILD_TYPE='S'", "y", null);
        sourceDao.deleteChildLink(Integer.valueOf(instId));

        if (op != null && op.equals("D")) {
            undoDao.insertIntoUndo(null, instId, op, "T_SOURCE_LNK", "FK_SOURCE_PARENT_ID", ts, "AND PARENT_TYPE='S'", "y", null);
            sourceDao.deleteParentLink(Integer.valueOf(instId));

            try {
                String aclPath = "/instruments/" + instId;
                AccessController.removeAcl(aclPath);
            } catch (SignOnException e) {
                e.printStackTrace();
            }

            undoDao.addObligationIdsIntoUndo(Integer.valueOf(instId), ts, "T_SOURCE");

            // cascade delete related reporting obligations
            List<String> sourceObligations = RODServices.getDbService().getObligationDao()
                    .getObligationsBySource(Integer.valueOf(instId));
            int cnt = 1;
            Iterator<String> soIterator = sourceObligations.iterator();
            while (soIterator.hasNext()) {
                String obligationId = soIterator.next();
                IObligationDao obligationDao = RODServices.getDbService().getObligationDao();
                ObligationsActionBean obligationBean = new ObligationsActionBean();
                obligationBean.setId(obligationId);
                obligationBean.setTs(ts + cnt);
                obligationBean.setUndoDao(undoDao);
                obligationBean.setObligationDao(obligationDao);
                obligationBean.delActivity("D", "n");
                cnt++;
            }
            undoDao.insertIntoUndo(null, instId, "D", "T_SOURCE", "PK_SOURCE_ID", ts, "", "y", null);
            // delete legislative act itself
            sourceDao.deleteSource(Integer.valueOf(instId));
        }

    }

    private void processLinkedTables() throws ServiceException {
        if (instrument.getSourceFKClientId() != null && !instrument.getSourceFKClientId().trim().equals("")
                && !instrument.getSourceFKClientId().trim().equals("NULL")) {
            RODServices.getDbService().getClientDao()
                    .insertClientLink(Integer.valueOf(instrument.getSourceFKClientId()), Integer.valueOf(instId), "M", "S");
        }

        if (!RODUtil.isNullOrEmpty(parentInstrumentId) && !parentInstrumentId.equals("NULL")) {
            sourceDao.addParentInstrument(instId, parentInstrumentId);
        }
        if (selectedSourceClasses != null && selectedSourceClasses.size() > 0) {
            sourceDao.addLinkedSources(instId, selectedSourceClasses);
        }
    }

    private void initAfterAddEdit() throws ServiceException {
        instrument = RODServices.getDbService().getSourceDao().getInstrumentFactsheet(instId);
        dgenv = RODServices.getDbService().getSourceDao().getDGEnvNameByInstrumentId(instId);
    }

    public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
        if (errors.hasFieldErrors()) {
            initOnValidationErrors();
        }
        getContext().setSeverity(Constants.SEVERITY_VALIDATION);
        return new ForwardResolution("/pages/einstrument.jsp");
    }

    private void initOnValidationErrors() throws ServiceException {
        dgenvlist = RODServices.getDbService().getSourceDao().getLookupList("DGS");
        clients = RODServices.getDbService().getClientDao().getAllClients();
        allSourceClasses = RODServices.getDbService().getSourceDao().getAllSourceClasses();

        instrumentSourceClasses = RODServices.getDbService().getSourceDao().getInstrumentSourceClassesList(selectedSourceClasses);
        sourceClasses = initSourceClasses();

        if (instId != null && instId.equals("new")) {
            parentInstrumentsList = RODServices.getDbService().getSourceDao().getParentInstrumentsList("-1");
        } else {
            parentInstrumentsList = RODServices.getDbService().getSourceDao().getParentInstrumentsList(instId);
        }

    }

    private void sendEvent(boolean isUpdate) throws ServiceException {
        String userName = getUserName();
        FileServiceIF fileService = RODServices.getFileService();

        try {

            Vector<Vector<String>> lists = new Vector<Vector<String>>();
            Vector<String> list = new Vector<String>();
            long timestamp = System.currentTimeMillis();
            String events = "http://rod.eionet.europa.eu/events/" + timestamp;

            if (isUpdate) {

                list.add(events);
                list.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                list.add(Attrs.SCHEMA_RDF + "InstrumentChange");
                lists.add(list);

                list = new Vector<String>();
                list.add(events);
                String et_schema = fileService.getStringProperty(FileServiceIF.UNS_EVENTTYPE_PREDICATE);
                list.add(et_schema);
                list.add("Instrument change");
                lists.add(list);

                list = new Vector<String>();
                list.add(events);
                list.add("http://purl.org/dc/elements/1.1/title");
                list.add("Instrument change");
                lists.add(list);

            } else {

                list.add(events);
                list.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                list.add(Attrs.SCHEMA_RDF + "NewInstrument");
                lists.add(list);

                list = new Vector<String>();
                list.add(events);
                String et_schema = fileService.getStringProperty(FileServiceIF.UNS_EVENTTYPE_PREDICATE);
                list.add(et_schema);
                list.add("New instrument");
                lists.add(list);

                list = new Vector<String>();
                list.add(events);
                list.add("http://purl.org/dc/elements/1.1/title");
                list.add("New instrument");
                lists.add(list);

            }

            list = new Vector<String>();
            list.add(events);
            String inst_schema = fileService.getStringProperty(FileServiceIF.UNS_INSTRUMENT_PREDICATE);
            list.add(inst_schema);
            list.add(instrument.getSourceTitle());
            lists.add(list);

            list = new Vector<String>();
            list.add(events);
            list.add(Attrs.SCHEMA_RDF + "actor");
            list.add(userName);
            lists.add(list);

            if (isUpdate) {
                Vector<String> changes = getChanges(instId);
                for (Enumeration<String> en = changes.elements(); en.hasMoreElements();) {
                    String label = en.nextElement();
                    list = new Vector<String>();
                    list.add(events);
                    list.add(Attrs.SCHEMA_RDF + "change");
                    list.add(label);
                    lists.add(list);
                }
            }

            list = new Vector<String>();
            list.add(events);
            list.add("http://purl.org/dc/elements/1.1/identifier");
            String url = "http://rod.eionet.europa.eu/instruments/" + instId;
            list.add(url);

            lists.add(list);

            if (lists.size() > 0) {
                UNSEventSender.makeCall(lists);
            }

        } catch (Exception e) {

        }
    }

    private Vector<String> getChanges(String instrumentID) throws ServiceException {

        Vector<String> res_vec = new Vector<String>();
        Vector<Map<String, String>> undo_vec = RODServices.getDbService().getUndoDao()
                .getUndoInformation(ts, "U", "T_SOURCE", instrumentID);
        for (int i = 0; i < undo_vec.size(); i++) {
            Map<String, String> hash = undo_vec.elementAt(i);

            String label = "";
            String ut = hash.get("undo_time");
            String tabel = hash.get("tab");
            String col = hash.get("col");
            String value = hash.get("value");
            if (tabel != null && !tabel.equals("") && tabel.equals("T_SOURCE")) {
                String currentValue = RODServices.getDbService().getDifferencesDao()
                        .getDifferences(Long.valueOf(ut).longValue(), tabel, col);
                if ((value != null && value.trim().equals("")) || (value != null && value.trim().equals("null"))) {
                    value = null;
                }
                if ((currentValue != null && currentValue.trim().equals(""))
                        || (currentValue != null && currentValue.trim().equals("null"))) {
                    currentValue = null;
                }
                boolean diff = (value != null && currentValue != null && value.equals(currentValue))
                        || (value == null && currentValue == null);

                if (!diff) {
                    label = getLabel(col, value, currentValue);
                    res_vec.add(label);
                }
            }
        }

        DifferenceDTO eurlex = RODServices.getDbService().getDifferencesDao()
                .getDifferencesInEurlexCategories(ts, new Integer(instrumentID).intValue(), "U");
        if (eurlex != null) {
            String added = eurlex.getAdded();
            String removed = eurlex.getRemoved();
            if (added.length() > 0) {
                res_vec.add("'Eur-lex categories' added: " + added);
            }
            if (removed.length() > 0) {
                res_vec.add("'Eur-lex categories' removed: " + removed);
            }
        }

        return res_vec;
    }

    private String getLabel(String col, String value, String currentValue) throws ServiceException {

        String label = "";

        if (col != null && col.equalsIgnoreCase("TITLE")) {
            label = "'Title' changed ";
        } else if (col != null && col.equalsIgnoreCase("ALIAS")) {
            label = "'Short name' changed ";
        } else if (col != null && col.equalsIgnoreCase("SOURCE_CODE")) {
            label = "'Identification number' changed ";
        } else if (col != null && col.equalsIgnoreCase("DRAFT")) {
            label = "'Draft' changed ";
        } else if (col != null && col.equalsIgnoreCase("URL")) {
            label = "'URL to official text' changed ";
        } else if (col != null && col.equalsIgnoreCase("CELEX_REF")) {
            label = "'CELEX reference' changed ";
        } else if (col != null && col.equalsIgnoreCase("FK_CLIENT_ID")) {
            label = "'Issued by' changed ";
            value = RODServices.getDbService().getClientDao().getOrganisationNameByID(value);
            currentValue = RODServices.getDbService().getClientDao().getOrganisationNameByID(currentValue);
        } else if (col != null && col.equalsIgnoreCase("ISSUED_BY")) {
            label = "'Issuer' changed ";
        } else if (col != null && col.equalsIgnoreCase("ISSUED_BY_URL")) {
            label = "'URL to issuer' changed ";
        } else if (col != null && col.equalsIgnoreCase("DGENV_REVIEW")) {
            label = "'DG Env review of reporting theme' changed ";
            value = RODServices.getDbService().getSourceDao().getDGEnvName(value);
            currentValue = RODServices.getDbService().getSourceDao().getDGEnvName(currentValue);
        } else if (col != null && col.equalsIgnoreCase("VALID_FROM")) {
            label = "'Valid from' changed ";
        } else if (col != null && col.equalsIgnoreCase("GEOGRAPHIC_SCOPE")) {
            label = "'Geographic scope' changed ";
        } else if (col != null && col.equalsIgnoreCase("ABSTRACT")) {
            label = "'Abstract' changed ";
        } else if (col != null && col.equalsIgnoreCase("COMMENT")) {
            label = "'Comments' changed ";
        } else if (col != null && col.equalsIgnoreCase("EC_ENTRY_INTO_FORCE")) {
            label = "'EC entry into force' changed ";
        } else if (col != null && col.equalsIgnoreCase("EC_ACCESSION")) {
            label = "'EC accession' changed ";
        } else if (col != null && col.equalsIgnoreCase("SECRETARIAT")) {
            label = "'Secretariat' changed ";
        } else if (col != null && col.equalsIgnoreCase("SECRETARIAT_URL")) {
            label = "'URL to Secretariat homepage' changed ";
        } else if (col != null && col.equalsIgnoreCase("RM_VERIFIED")) {
            label = "'Verified' changed ";
        } else if (col != null && col.equalsIgnoreCase("RM_VERIFIED_BY")) {
            label = "'Verified by' changed ";
        } else if (col != null && col.equalsIgnoreCase("RM_NEXT_UPDATE")) {
            label = "'Next update due' changed ";
        } else if (col != null && col.equalsIgnoreCase("RM_VALIDATED_BY")) {
            label = "'Validated by' changed ";
        } else if (col != null && col.equalsIgnoreCase("LAST_UPDATE")) {
            label = "'Last update' changed ";
        } else if (col != null && col.equalsIgnoreCase("LAST_MODIFIED")) {
            label = "'Last modification' changed ";
        } else if (col != null && col.equalsIgnoreCase("LEGAL_NAME")) {
            label = "'Legal name' changed ";
        }

        label = label + " from '" + value + "' to '" + currentValue + "'";

        return label;
    }


    public InstrumentFactsheetDTO getInstrument() {
        return instrument;
    }

    public void setInstrument(InstrumentFactsheetDTO instrument) {
        this.instrument = instrument;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDgenv() {
        return dgenv;
    }

    public void setDgenv(String dgenv) {
        this.dgenv = dgenv;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public InstrumentsListDTO getHierarchyInstrument() {
        return hierarchyInstrument;
    }

    public void setHierarchyInstrument(InstrumentsListDTO hierarchyInstrument) {
        this.hierarchyInstrument = hierarchyInstrument;
    }

    public String getHierarchyTree() {
        return hierarchyTree;
    }

    public void setHierarchyTree(String hierarchyTree) {
        this.hierarchyTree = hierarchyTree;
    }

    public List<HierarchyInstrumentDTO> getHierarchyInstruments() {
        return hierarchyInstruments;
    }

    public void setHierarchyInstruments(List<HierarchyInstrumentDTO> hierarchyInstruments) {
        this.hierarchyInstruments = hierarchyInstruments;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<LookupDTO> getDgenvlist() {
        return dgenvlist;
    }

    public void setDgenvlist(List<LookupDTO> dgenvlist) {
        this.dgenvlist = dgenvlist;
    }

    public List<ClientDTO> getClients() {
        return clients;
    }

    public void setClients(List<ClientDTO> clients) {
        this.clients = clients;
    }

    public List<InstrumentDTO> getParentInstrumentsList() {
        return parentInstrumentsList;
    }

    public void setParentInstrumentsList(List<InstrumentDTO> parentInstrumentsList) {
        this.parentInstrumentsList = parentInstrumentsList;
    }

    public String getParentInstrumentId() {
        return parentInstrumentId;
    }

    public void setParentInstrumentId(String parentInstrumentId) {
        this.parentInstrumentId = parentInstrumentId;
    }

    public List<SourceClassDTO> getSourceClasses() {
        return sourceClasses;
    }

    public void setSourceClasses(List<SourceClassDTO> sourceClasses) {
        this.sourceClasses = sourceClasses;
    }

    public List<String> getSelSourceClasses() {
        return selSourceClasses;
    }

    public void setSelSourceClasses(List<String> selSourceClasses) {
        this.selSourceClasses = selSourceClasses;
    }

    public List<String> getSelectedSourceClasses() {
        return selectedSourceClasses;
    }

    public void setSelectedSourceClasses(List<String> selectedSourceClasses) {
        this.selectedSourceClasses = selectedSourceClasses;
    }

    public List<SourceClassDTO> getInstrumentSourceClasses() {
        return instrumentSourceClasses;
    }

    public void setInstrumentSourceClasses(List<SourceClassDTO> instrumentSourceClasses) {
        this.instrumentSourceClasses = instrumentSourceClasses;
    }

    public List<SourceClassDTO> getAllSourceClasses() {
        return allSourceClasses;
    }

    public void setAllSourceClasses(List<SourceClassDTO> allSourceClasses) {
        this.allSourceClasses = allSourceClasses;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
