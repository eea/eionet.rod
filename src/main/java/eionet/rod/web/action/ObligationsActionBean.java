package eionet.rod.web.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import com.tee.uit.client.ServiceClientIF;
import com.tee.uit.client.ServiceClients;
import com.tee.uit.security.AccessControlListIF;
import com.tee.uit.security.AccessController;
import com.tee.uit.security.SignOnException;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import eionet.rod.Attrs;
import eionet.rod.Constants;
import eionet.rod.RODUtil;
import eionet.rod.UNSEventSender;
import eionet.rod.countrysrv.Extractor;
import eionet.rod.dto.ClientDTO;
import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.CountryDeliveryDTO;
import eionet.rod.dto.DDParamDTO;
import eionet.rod.dto.DifferenceDTO;
import eionet.rod.dto.InstrumentFactsheetDTO;
import eionet.rod.dto.IssueDTO;
import eionet.rod.dto.LookupDTO;
import eionet.rod.dto.ObligationCountryDTO;
import eionet.rod.dto.ObligationFactsheetDTO;
import eionet.rod.dto.ObligationRdfDTO;
import eionet.rod.dto.ObligationsListDTO;
import eionet.rod.dto.SiblingObligationDTO;
import eionet.rod.dto.VersionDTO;
import eionet.rod.rdf.Activities;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IObligationDao;
import eionet.rod.services.modules.db.dao.IUndoDao;

/**
 *
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/obligations")
public class ObligationsActionBean extends AbstractRODActionBean implements ValidationErrorHandler {

    @ValidateNestedProperties({
        @Validate(field = "title", on ={"edit","add"}, required = true),
        @Validate(field = "description", on ={"edit","add"}, required = true),
        @Validate(field = "fkClientId", on ={"edit","add"}, expression="this ne '0'"),
        @Validate(field = "firstReporting", on ={"edit","add"}, mask = "([0-9]{2})/([0-9]{2})/([0-9]{4})"),
        @Validate(field = "validTo", on ={"edit","add"}, mask = "([0-9]{2})/([0-9]{2})/([0-9]{4})"),
        @Validate(field = "validSince", on ={"edit","add"}, mask = "([0-9]{2})/([0-9]{2})/([0-9]{4})"),
        @Validate(field = "reportFormatUrl", on ={"edit","add"}, mask = "^((ht|f)tps?://).*"),
        @Validate(field = "locationPtr", on ={"edit","add"}, mask = "^((ht|f)tps?://).*"),
        @Validate(field = "dataUsedForUrl", on ={"edit","add"}, mask = "^((ht|f)tps?://).*"),
        @Validate(field = "coordinatorUrl", on ={"edit","add"}, mask = "^((ht|f)tps?://).*"),
        @Validate(field = "overlapUrl", on ={"edit","add"}, mask = "^((ht|f)tps?://).*"),
        @Validate(field = "rmVerified", on ={"edit","add"}, mask = "([0-9]{2})/([0-9]{2})/([0-9]{4})"),
        @Validate(field = "rmNextUpdate", on ={"edit","add"}, mask = "([0-9]{2})/([0-9]{2})/([0-9]{4})")})
    private ObligationFactsheetDTO obligation;
    private String id;
    private List<ClientDTO> clients;
    private List<LookupDTO> infoTypeList;
    private String tab;
    private List<CountryDeliveryDTO> deliveries;
    private String spatialId;
    private List<VersionDTO> versions;
    private List<SiblingObligationDTO> siblingObligations;
    private List<ObligationCountryDTO> countries;
    private List<IssueDTO> issues;
    private List<DDParamDTO> ddparameters;

    private String anmode;
    private List<ObligationsListDTO> obligations;
    private List<ObligationsListDTO> indirectObligations;
    private List<CountryDTO> formCountries;
    private List<IssueDTO> formIssues;
    private List<ClientDTO> formClients;
    private String country;
    private String issue;
    private String client;
    private String terminated;
    private String countryName;
    private String issueName;
    private String clientName;

    //variables for EDIT, ADD and DELETE obligation
    private String aid;

    private List<String> selectedClients;
    private List<ClientDTO> obligationClients;
    private List<String> selClients;

    private List<CountryDTO> allcountries;

    private List<CountryDTO> formalCountries;
    private List<String> selectedFormalCountries;
    private List<String> forCountries;

    private List<CountryDTO> voluntaryCountries;
    private List<String> selectedVoluntaryCountries;
    private List<String> volCountries;

    private List<LookupDTO> legalMoral;
    private List<String> selectedInfoTypes;

    private List<IssueDTO> obligationIssues;
    private List<String> selectedIssues;
    private List<String> selIssues;

    private IUndoDao undoDao;
    private IObligationDao obligationDao;
    private long ts;

    private boolean hasErrors = false;

    //fields for initializing the instrument after delete
    private InstrumentFactsheetDTO instrument;
    private String instId;
    private String dgenv;
    /////

    /**
     *
     * @return
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {

        String forwardPage = "/pages/obligation.jsp";
        String pathInfo = getContext().getRequest().getPathInfo();
        if (!RODUtil.isNullOrEmpty(pathInfo)) {
            StringTokenizer st = new StringTokenizer(pathInfo,"/");
            if (st.hasMoreElements())
                id = st.nextToken();
            if (st.hasMoreElements())
                tab = st.nextToken();
        }

        String acceptHeader = getContext().getRequest().getHeader("accept");
        String[] accept = null;
        if (acceptHeader != null && acceptHeader.length() > 0)
            accept = acceptHeader.split(",");

        if (!RODUtil.isNullOrEmpty(id)) {
            if (id.equals("new")) {
                obligation = new ObligationFactsheetDTO();
                if (tab == null || !RODUtil.isNumber(tab)) {
                    handleRodException("Legislative instrument ID is missing!", Constants.SEVERITY_WARNING);
                    return new ForwardResolution("/pages/eobligation.jsp");
                } else {
                    aid = tab;
                }
            } else{
                obligation = RODServices.getDbService().getObligationDao().getObligationFactsheet(id);
            }

            if (!id.equals("new") && (obligation == null || !RODUtil.isNumber(id))) {
                return new ErrorResolution(HttpServletResponse.SC_NOT_FOUND);
            }

            if (!id.equals("new") && obligation != null && accept != null && accept.length > 0 && accept[0].equals("application/rdf+xml")) {
                return new StreamingResolution("application/rdf+xml;charset=UTF-8") {
                    public void stream(HttpServletResponse response) throws Exception {
                        ObligationRdfDTO obligationForRdf = RODServices.getDbService().getObligationDao().getObligationForRDF(id);
                        Activities act = new Activities();
                        String rdf = act.getRdf(getContext().getRequest(), obligationForRdf);
                        response.getWriter().write(rdf);
                    }
                };
            }

            if (RODUtil.isNullOrEmpty(tab) || tab.equals("overview")) {
                clients = RODServices.getDbService().getClientDao().getClients(id);
                infoTypeList = RODServices.getDbService().getObligationDao().getLookupList(id);
            } else if (tab.equals("legislation")) {
                siblingObligations = RODServices.getDbService().getObligationDao().getSiblingObligations(id);
                countries = RODServices.getDbService().getSpatialDao().getObligationCountriesList(id);
                issues = RODServices.getDbService().getIssueDao().getObligationIssuesList(id);
            } else if (tab.equals("deliveries")) {
                deliveries = RODServices.getDbService().getDeliveryDao().getCountyDeliveriesList(id, spatialId);
            } else if (tab.equals("history")) {
                versions = RODServices.getDbService().getUndoDao().getPreviousActionsReportSpecific(id, "T_OBLIGATION", "PK_RA_ID");
            } else if (tab.equals("parameters")) {
                ddparameters = getDDParams();
            } else if (id.equals("new") || tab.equals("edit")) {
                forwardPage = "/pages/eobligation.jsp";

                handleCheckboxes();

                clients = RODServices.getDbService().getClientDao().getClientsList();
                allcountries = RODServices.getDbService().getSpatialDao().getCountriesList();
                legalMoral = RODServices.getDbService().getObligationDao().getLookupListByCategory("2");
                infoTypeList = RODServices.getDbService().getObligationDao().getLookupListByCategory("I");
                issues = RODServices.getDbService().getIssueDao().getIssuesList();

                if (tab != null && tab.equals("edit")) {
                    obligationClients = RODServices.getDbService().getClientDao().getClients(id);
                    formalCountries = RODServices.getDbService().getSpatialDao().getEditObligationCountriesList(id, "N");
                    voluntaryCountries = RODServices.getDbService().getSpatialDao().getEditObligationCountriesList(id, "Y");
                    obligationIssues = RODServices.getDbService().getIssueDao().getObligationIssuesList(id);
                    List<LookupDTO> lookups = RODServices.getDbService().getObligationDao().getLookupList(id);
                    List<String> infoTypes = new ArrayList<String>();
                    for(Iterator<LookupDTO> it=lookups.iterator(); it.hasNext();) {
                        LookupDTO ld = it.next();
                        infoTypes.add(ld.getCvalue());
                    }
                    selectedInfoTypes = infoTypes;
                }
            }
        } else if (RODUtil.isNullOrEmpty(id)) {

            if (accept != null && accept.length > 0 && accept[0].equals("application/rdf+xml")) {

                return new StreamingResolution("application/rdf+xml;charset=UTF-8") {
                    public void stream(HttpServletResponse response) throws Exception {
                        Activities act = new Activities();
                        String rdf = act.getRdf(getContext().getRequest());
                        response.getWriter().write(rdf);
                    }
                }.setFilename("obligations.rdf");

            }

            formCountries = RODServices.getDbService().getSpatialDao().getCountriesList();
            formIssues = RODServices.getDbService().getIssueDao().getIssuesList();
            formClients = RODServices.getDbService().getClientDao().getClientsList();

            obligations = RODServices.getDbService().getObligationDao().getObligationsList(anmode, null, null, null, null, false);
            if (anmode != null && anmode.equals("NI"))
                issue = "NI";
            forwardPage = "/pages/obligations.jsp";
        } else {
            handleRodException("Obligation ID has to be a number!", Constants.SEVERITY_WARNING);
        }

        return new ForwardResolution(forwardPage);
    }

    /**
     *
     * @return
     */
    public Resolution filter() throws ServiceException {

        formCountries = RODServices.getDbService().getSpatialDao().getCountriesList();
        formIssues = RODServices.getDbService().getIssueDao().getIssuesList();
        formClients = RODServices.getDbService().getClientDao().getClientsList();

        if (!RODUtil.isNullOrEmpty(country) && !country.equals("-1"))
            countryName = RODServices.getDbService().getSpatialDao().getCountryById(new Integer(country).intValue());
        if (!RODUtil.isNullOrEmpty(client) && !client.equals("-1"))
            clientName = RODServices.getDbService().getClientDao().getOrganisationNameByID(client);
        if (!RODUtil.isNullOrEmpty(issue) && !issue.equals("-1") && !issue.equals("NI"))
            issueName = RODServices.getDbService().getIssueDao().getIssueNameById(issue);

        obligations = RODServices.getDbService().getObligationDao().getObligationsList(anmode, country, issue, client, terminated, false);
        if (!RODUtil.isNullOrEmpty(client) && !client.equals("-1"))
            indirectObligations = RODServices.getDbService().getObligationDao().getObligationsList(anmode, country, issue, client, terminated, true);

        return new ForwardResolution("/pages/obligations.jsp");
    }

    private List<DDParamDTO> getDDParams() {

        List<DDParamDTO> retList = new ArrayList<DDParamDTO>();

        try {
            String serviceName  = "DataDictService";
            String rpcRouterUrl = RODServices.getFileService().getStringProperty("dd.service.url");
            String methodName   = "getParametersByActivityID";

            if (rpcRouterUrl != null) {
                ServiceClientIF client = ServiceClients.getServiceClient(serviceName, rpcRouterUrl);

                Vector<String> params = new Vector<String>();
                params.add(id);

                Vector result = (Vector)client.getValue(methodName, params);
                for(int i = 0; result != null && i < result.size(); i++) {
                    Hashtable hash = (Hashtable)result.get(i);

                    DDParamDTO elem = new DDParamDTO();
                    elem.setElementName((String)hash.get("elm-name")); // element name
                    elem.setElementUrl((String)hash.get("elm-url"));   // details url
                    elem.setTableName((String)hash.get("tbl-name"));   // table name
                    elem.setDatasetName((String)hash.get("dst-name")); // dataset name

                    retList.add(elem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retList;
    }

    //methods for EDIT, ADD and DELETE obligation starts

    private void handleCheckboxes() {
        if (RODUtil.isNullOrEmpty(obligation.getCoordinatorRoleSuf()))
            obligation.setCoordinatorRoleSuf("0");

        if (RODUtil.isNullOrEmpty(obligation.getResponsibleRoleSuf()))
            obligation.setResponsibleRoleSuf("0");

        if (RODUtil.isNullOrEmpty(obligation.getDpsirD()))
            obligation.setDpsirD("no");

        if (RODUtil.isNullOrEmpty(obligation.getDpsirP()))
            obligation.setDpsirP("no");

        if (RODUtil.isNullOrEmpty(obligation.getDpsirS()))
            obligation.setDpsirS("no");

        if (RODUtil.isNullOrEmpty(obligation.getDpsirI()))
            obligation.setDpsirI("no");

        if (RODUtil.isNullOrEmpty(obligation.getDpsirR()))
            obligation.setDpsirR("no");

        if (RODUtil.isNullOrEmpty(obligation.getContinousReporting()))
            obligation.setContinousReporting("no");

    }

    public Resolution add() throws ServiceException {

        if (hasErrors)
            return new ForwardResolution("/pages/eobligation.jsp");

        Resolution resolution = new ForwardResolution("/pages/obligation.jsp");

        String userName = getUserName();
        if (RODUtil.isNullOrEmpty(userName)) {
            handleRodException("You are not logged in!", Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/eobligation.jsp");
        }

        if (id != null && id.equals("new") && RODUtil.isNullOrEmpty(aid)) {
            handleRodException("Legislative instrument ID is missing!", Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/eobligation.jsp");
        }

        String acl_p = (RODUtil.isNullOrEmpty(id) || id.equals("new"))? Constants.ACL_RA_NAME : (Constants.ACL_RA_NAME + "/" +id);
        boolean ins = false;
        try {
            AccessControlListIF acl = AccessController.getAcl(acl_p);
            ins = acl.checkPermission(userName, Constants.ACL_INSERT_PERMISSION);
        } catch (Exception e ) {
            handleRodException(e.getMessage(), Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/eobligation.jsp");
        }

        if (!ins) {
            handleRodException("Isufficient permissions", Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/eobligation.jsp");
        }

        obligationDao = RODServices.getDbService().getObligationDao();

        handleCheckboxes();
        obligation.setFkSourceId(aid);
        Integer obligationId = obligationDao.insertObligation(obligation);
        id = obligationId.toString();

        try {
            String aclPath = "/obligations/"+id;
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

        if (hasErrors)
            return new ForwardResolution("/pages/eobligation.jsp");

        Resolution resolution = new ForwardResolution("/pages/obligation.jsp");

        String userName = getUserName();
        if (RODUtil.isNullOrEmpty(userName)) {
            handleRodException("You are not logged in!", Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/eobligation.jsp");
        }

        String acl_p = (RODUtil.isNullOrEmpty(id))? Constants.ACL_RA_NAME : (Constants.ACL_RA_NAME + "/" +id);
        boolean upd = false;
        try {
            AccessControlListIF acl = AccessController.getAcl(acl_p);
            upd = acl.checkPermission(userName, Constants.ACL_UPDATE_PERMISSION);
        } catch (Exception e ) {
            handleRodException(e.getMessage(), Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/eobligation.jsp");
        }

        if (!upd) {
            handleRodException("Isufficient permissions", Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/eobligation.jsp");
        }

        processEditDelete("U", userName);

        if (RODUtil.isNullOrEmpty(obligation.getValidTo())) {
            obligation.setTerminate("N");
            obligation.setValidTo("31/12/9999");
        }

        handleCheckboxes();
        obligationDao.editObligation(obligation);

        processLinkedTables();

        sendEvent(true);

        initAfterAddEdit();

        return resolution;
    }

    public Resolution delete() throws ServiceException {

        Resolution resolution = new ForwardResolution("/pages/instrument.jsp");

        if (RODUtil.isNullOrEmpty(id)) {
            handleRodException("Obligation ID is missing!", Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/obligation.jsp");
        }

        obligation = RODServices.getDbService().getObligationDao().getObligationFactsheet(id);

        String userName = getUserName();
        if (RODUtil.isNullOrEmpty(userName)) {
            handleRodException("You are not logged in!", Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/obligation.jsp");
        }

        String acl_p = (RODUtil.isNullOrEmpty(id))? Constants.ACL_RA_NAME : (Constants.ACL_RA_NAME + "/" +id);
        boolean del = false;
        try {
            AccessControlListIF acl = AccessController.getAcl(acl_p);
            del = acl.checkPermission(userName, Constants.ACL_DELETE_PERMISSION);
        } catch (Exception e ) {
            handleRodException(e.getMessage(), Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/eobligation.jsp");
        }

        if (!del) {
            handleRodException("You do not have permissions for that operation!", Constants.SEVERITY_WARNING);
            return new ForwardResolution("/pages/obligation.jsp");
        }

        processEditDelete("D", userName);

        instId = obligation.getFkSourceId();
        instrument = RODServices.getDbService().getSourceDao().getInstrumentFactsheet(instId);
        dgenv = RODServices.getDbService().getSourceDao().getDGEnvNameByInstrumentId(instId);

        return resolution;
    }

    private void processLinkedTables() throws ServiceException {
        if (!RODUtil.isNullOrEmpty(obligation.getFkClientId()) && !obligation.getFkClientId().equals("0"))
            RODServices.getDbService().getClientDao().insertClientLink(Integer.valueOf(obligation.getFkClientId()),Integer.valueOf(id),"M","A");

        if (selectedIssues != null && selectedIssues.size() > 0)
            RODServices.getDbService().getIssueDao().insertObligationIssues(id, selectedIssues);

        if (selectedFormalCountries != null && selectedFormalCountries.size() > 0)
            RODServices.getDbService().getSpatialDao().insertObligationCountries(id, selectedFormalCountries, "N");

        if (selectedVoluntaryCountries != null && selectedVoluntaryCountries.size() > 0)
            RODServices.getDbService().getSpatialDao().insertObligationCountries(id, selectedVoluntaryCountries, "Y");

        if (selectedClients != null && selectedClients.size() > 0)
            RODServices.getDbService().getClientDao().insertObligationClients(id, selectedClients);

        if (selectedInfoTypes != null && selectedInfoTypes.size() > 0) {
            for(Iterator<String> it = selectedInfoTypes.iterator(); it.hasNext();) {
                String infoId = it.next();
                if (!RODUtil.isNullOrEmpty(infoId)) {
                    obligationDao.insertInfoLink(Integer.valueOf(id),infoId);
                }
            }
        }
    }

    private void processEditDelete(String state, String userName) throws ServiceException {

        undoDao = RODServices.getDbService().getUndoDao();
        obligationDao = RODServices.getDbService().getObligationDao();
        Extractor ext = new Extractor(); //used for role handling
        ts = System.currentTimeMillis();

        if (state != null && state.equals("U"))
            undoDao.insertIntoUndo(null, id, "U", "T_OBLIGATION", "PK_RA_ID", ts, "", "y", null);

        String url = "obligations/"+id;
        undoDao.insertIntoUndo(ts,"T_OBLIGATION","REDIRECT_URL","L","y","n",url,0,"n");
        undoDao.insertIntoUndo(ts,"T_OBLIGATION","A_USER","K","y","n",userName,0,"n");
        undoDao.insertIntoUndo(ts,"T_OBLIGATION","TYPE","T","y","n","A",0,"n");

        if (state != null && state.equals("D")) {
            String acl_path = "/obligations/"+id;
            undoDao.insertIntoUndo(ts,"T_OBLIGATION","ACL","ACL","y","n",acl_path,0,"n");
        }

        //Get role info from Directory and save in T_ROLE
        String roleId = obligation.getResponsibleRole();
        try {
            ext.saveRole(roleId);
            roleId = obligation.getCoordinatorRole();
            ext.saveRole(roleId);
        } catch (Exception e ) {
            //don't worry about the role saving if something wrong
        }

        delActivity(state, "y");
    }

    public void delActivity(String op, String show) throws ServiceException {

        Integer obligationID = new Integer(id);

        undoDao.insertTransactionInfo(id, "A", "T_RAISSUE_LNK", "FK_RA_ID", ts, "");
        undoDao.insertTransactionInfo(id, "A", "T_RASPATIAL_LNK", "FK_RA_ID", ts, "");
        undoDao.insertTransactionInfo(id, "A", "T_INFO_LNK", "FK_RA_ID", ts, "");
        // !!!!!!!!!! in old version single quotes was escaped in where clause
        undoDao.insertTransactionInfo(id, "A", "T_CLIENT_LNK", "FK_OBJECT_ID", ts, "AND TYPE='A'");
        undoDao.insertTransactionInfo(id, "A", "T_OBLIGATION", "PK_RA_ID", ts, "");
        undoDao.insertTransactionInfo(id, "A", "T_HISTORIC_DEADLINES", "FK_RA_ID", ts, "");

        undoDao.insertIntoUndo(null, id, op, "T_RAISSUE_LNK", "FK_RA_ID", ts, "", show, null);
        // delete linked environmental issues
        obligationDao.deleteIssueLink(obligationID);
        undoDao.insertIntoUndo(null, id, op, "T_RASPATIAL_LNK", "FK_RA_ID", ts, "", show, null);
        // delete linked countries
        obligationDao.deleteSpatialLink(obligationID);
        undoDao.insertIntoUndo(null, id, op, "T_INFO_LNK", "FK_RA_ID", ts, "", show, null);
        // delete linked info
        obligationDao.deleteInfoLink(obligationID);
        undoDao.insertIntoUndo(null, id, op, "T_HISTORIC_DEADLINES", "FK_RA_ID", ts, "", show, null);
        // delete linked historical deadlines
        RODServices.getDbService().getHistoricDeadlineDao().deleteByObligationId(obligationID);

        if (op != null && op.equals("D")) {
            String acl_id = RODServices.getDbService().getAclDao().getAclId(id, "/obligations");
            undoDao.insertIntoUndo(null,acl_id, op, "ACLS", "ACL_ID",ts,"","n",null);
            undoDao.insertIntoUndo(null,acl_id, op, "ACL_ROWS", "ACL_ID",ts,"","n",null);
            try {
                String aclPath = "/obligations/"+id;
                AccessController.removeAcl(aclPath);
            } catch (SignOnException e) {
                e.printStackTrace();
            }
        }

        undoDao.insertIntoUndo(null, id, op, "T_CLIENT_LNK", "FK_OBJECT_ID", ts, "AND TYPE='A'", show, null);
        RODServices.getDbService().getClientDao().deleteObligationLink(obligationID);

        RODServices.getDbService().getSpatialHistoryDao().updateEndDateForObligation(obligationID);

        if (op != null && op.equals("D")) {
            undoDao.insertIntoUndo(null, id, "D", "T_OBLIGATION", "PK_RA_ID", ts, "", show, null);
            obligationDao.deleteObligation(obligationID);
        }
    }

    private void initAfterAddEdit() throws ServiceException {
        obligation = RODServices.getDbService().getObligationDao().getObligationFactsheet(id);
        clients = RODServices.getDbService().getClientDao().getClients(id);
        infoTypeList = RODServices.getDbService().getObligationDao().getLookupList(id);
        tab = "overview";
    }

    @ValidationMethod(on={"edit","add"})
    public void validateDates(ValidationErrors errors) throws Exception {
        if (RODUtil.isNullOrEmpty(obligation.getNextReporting()) && (RODUtil.isNullOrEmpty(obligation.getFirstReporting()) ||
                RODUtil.isNullOrEmpty(obligation.getValidTo()) || RODUtil.isNullOrEmpty(obligation.getReportFreqMonths()))) {
            getContext().getMessages().add(new SimpleError(getBundle().getString("both.dates.empty")));
            getContext().setSeverity(Constants.SEVERITY_VALIDATION);
            hasErrors = true;
        }
        else if (!RODUtil.isNullOrEmpty(obligation.getNextReporting()) && !RODUtil.isNullOrEmpty(obligation.getNextDeadline())) {
            getContext().getMessages().add(new SimpleError(getBundle().getString("both.dates.used")));
            getContext().setSeverity(Constants.SEVERITY_VALIDATION);
            hasErrors = true;
        }
        else if (RODUtil.isNullOrEmpty(obligation.getNextDeadline()) && RODUtil.isNullOrEmpty(obligation.getFirstReporting()) &&
                RODUtil.isNullOrEmpty(obligation.getValidTo()) && RODUtil.isNullOrEmpty(obligation.getReportFreqMonths())) {
            getContext().getMessages().add(new SimpleError(getBundle().getString("unable.calculate.due.date")));
            getContext().setSeverity(Constants.SEVERITY_VALIDATION);
            hasErrors = true;
        }
        if (hasErrors)
            initOnValidationErrors();
    }

    public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
        validateDates(errors);
        if (errors.hasFieldErrors())
            initOnValidationErrors();
        getContext().setSeverity(Constants.SEVERITY_VALIDATION);
        return new ForwardResolution("/pages/eobligation.jsp");
    }

    private void initOnValidationErrors() throws ServiceException {

        clients = RODServices.getDbService().getClientDao().getClientsList();
        obligationClients = RODServices.getDbService().getClientDao().getClients(selectedClients);
        allcountries = RODServices.getDbService().getSpatialDao().getCountriesList();
        formalCountries = RODServices.getDbService().getSpatialDao().getObligationCountriesList(selectedFormalCountries);
        voluntaryCountries = RODServices.getDbService().getSpatialDao().getObligationCountriesList(selectedVoluntaryCountries);
        legalMoral = RODServices.getDbService().getObligationDao().getLookupListByCategory("2");
        infoTypeList = RODServices.getDbService().getObligationDao().getLookupListByCategory("I");
        issues = RODServices.getDbService().getIssueDao().getIssuesList();
        obligationIssues = RODServices.getDbService().getIssueDao().getObligationIssuesList(selectedIssues);

        handleCheckboxes();
    }

    private void sendEvent(boolean isUpdate) throws ServiceException {
        String userName = getUserName();
        FileServiceIF fileService = RODServices.getFileService();

        try {

            Vector<Vector<String>> lists = new Vector<Vector<String>>();
            Vector<String> list = new Vector<String>();
            long timestamp = System.currentTimeMillis();
            String events = "http://rod.eionet.europa.eu/events/" + timestamp;

            int obligation_id = Integer.valueOf(id).intValue();

            if (isUpdate) {

                list.add(events);
                list.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                list.add(Attrs.SCHEMA_RDF + "ObligationChange");
                lists.add(list);

                list = new Vector<String>();
                list.add(events);
                String et_schema = fileService.getStringProperty(FileServiceIF.UNS_EVENTTYPE_PREDICATE);
                list.add(et_schema);
                list.add("Obligation change");
                lists.add(list);

                list = new Vector<String>();
                list.add(events);
                list.add("http://purl.org/dc/elements/1.1/title");
                list.add("Obligation change");
                lists.add(list);

            } else {

                list.add(events);
                list.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                list.add(Attrs.SCHEMA_RDF + "NewObligation");
                lists.add(list);

                list = new Vector<String>();
                list.add(events);
                String et_schema = fileService.getStringProperty(FileServiceIF.UNS_EVENTTYPE_PREDICATE);
                list.add(et_schema);
                list.add("New Obligation");
                lists.add(list);

                list = new Vector<String>();
                list.add(events);
                list.add("http://purl.org/dc/elements/1.1/title");
                list.add("New Obligation");
                lists.add(list);

            }

            list = new Vector<String>();
            list.add(events);
            String obl_schema = fileService.getStringProperty(FileServiceIF.UNS_OBLIGATION_PREDICATE);
            list.add(obl_schema);
            list.add(obligation.getTitle());
            lists.add(list);


            Vector<Map<String,String>> countries = RODServices.getDbService().getSpatialDao().getObligationCountries(obligation_id);

            for (Enumeration<Map<String,String>> en = countries.elements(); en.hasMoreElements(); ) {
                Map<String,String> hash = en.nextElement();
                list = new Vector<String>();
                list.add(events);
                String loc_schema = fileService.getStringProperty(FileServiceIF.UNS_COUNTRY_PREDICATE);
                list.add(loc_schema);
                list.add((String)hash.get("name"));
                lists.add(list);
            }

            list = new Vector<String>();
            list.add(events);
            list.add(Attrs.SCHEMA_RDF + "responsiblerole");
            list.add(obligation.getResponsibleRole());
            lists.add(list);

            list = new Vector<String>();
            list.add(events);
            list.add(Attrs.SCHEMA_RDF + "actor");
            list.add(userName);
            lists.add(list);

            if (isUpdate) {
                Vector<String> changes = getChanges(id);
                for(Enumeration<String> en = changes.elements(); en.hasMoreElements(); ) {
                    String label = (String) en.nextElement();
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
            String url = "http://rod.eionet.europa.eu/obligations/"+id;
            list.add(url);

            lists.add(list);

            if (lists.size() > 0) UNSEventSender.makeCall(lists);
        } catch (Exception e) {

        }
    }

    private Vector<String> getChanges(String obligationID) throws ServiceException {

        Vector<String> res_vec = new Vector<String>();
        Vector<Map<String,String>> undo_vec = RODServices.getDbService().getUndoDao().getUndoInformation(ts,"U","T_OBLIGATION",obligationID);
        for (int i=0; i<undo_vec.size(); i++) {
            Map<String,String> hash = undo_vec.elementAt(i);

            String label = "";
            String ut = (String) hash.get("undo_time");
            String tabel = (String) hash.get("tab");
            String col = (String) hash.get("col");
            String value = (String) hash.get("value");
            String currentValue = RODServices.getDbService().getDifferencesDao().getDifferences(Long.valueOf(ut).longValue(),tabel,col);
            if ((value != null && value.trim().equals("")) || (value != null && value.trim().equals("null"))) value = null;
            if ((currentValue != null && currentValue.trim().equals("")) || (currentValue != null && currentValue.trim().equals("null"))) currentValue = null;
            boolean diff = (value != null && currentValue != null && value.equals(currentValue)) || (value == null && currentValue == null)  ;

            if (!diff) {
                label = getLabel(col, value, currentValue);
                res_vec.add(label);
            }
        }
        int intId = new Integer(obligationID).intValue();
        DifferenceDTO countries_formally = RODServices.getDbService().getDifferencesDao().getDifferencesInCountries(ts,intId,"N","U");
        if (countries_formally != null) {
            String added = countries_formally.getAdded();
            String removed = countries_formally.getRemoved();
            if (added.length() > 0) {
                res_vec.add("'Countries reporting formally' added: "+added);
            }
            if (removed.length() > 0) {
                res_vec.add("'Countries reporting formally' removed: "+removed);
            }
        }

        DifferenceDTO countries_voluntarily = RODServices.getDbService().getDifferencesDao().getDifferencesInCountries(ts,intId,"Y","U");
        if (countries_voluntarily != null) {
            String added = countries_voluntarily.getAdded();
            String removed = countries_voluntarily.getRemoved();
            if (added.length() > 0) {
                res_vec.add("'Countries reporting voluntarily' added: "+added);
            }
            if (removed.length() > 0) {
                res_vec.add("'Countries reporting voluntarily' removed: "+removed);
            }
        }

        DifferenceDTO issues = RODServices.getDbService().getDifferencesDao().getDifferencesInIssues(ts,intId,"U");
        if (issues != null) {
            String added = issues.getAdded();
            String removed = issues.getRemoved();
            if (added.length() > 0) {
                res_vec.add("'Environmental issues' added: "+added);
            }
            if (removed.length() > 0) {
                res_vec.add("'Environmental issues' removed: "+removed);
            }
        }

        DifferenceDTO clients = RODServices.getDbService().getDifferencesDao().getDifferencesInClients(ts,intId,"C","U","A");
        if (clients != null) {
            String added = clients.getAdded();
            String removed = clients.getRemoved();
            if (added.length() > 0) {
                res_vec.add("'Other clients using this reporting' added: "+added);
            }
            if (removed.length() > 0) {
                res_vec.add("'Other clients using this reporting' removed: "+removed);
            }
        }

        DifferenceDTO info = RODServices.getDbService().getDifferencesDao().getDifferencesInInfo(ts,intId,"U","I");
        if (info != null) {
            String added = info.getAdded();
            String removed = info.getRemoved();
            if (added.length() > 0) {
                res_vec.add("'Type of info reported' added: "+added);
            }
            if (removed.length() > 0) {
                res_vec.add("'Type of info reported' removed: "+removed);
            }
        }

        return res_vec;
    }

    private String getLabel(String col, String value, String currentValue) throws ServiceException {

        String label = "";

        if (col != null && col.equalsIgnoreCase("TITLE")) {
            label = "'Title' changed ";
        } else if (col != null && col.equalsIgnoreCase("DESCRIPTION")) {
            label = "'Description' changed ";
        } else if (col != null && col.equalsIgnoreCase("COORDINATOR_ROLE")) {
            label = "'National reporting coordinators role' changed ";
        } else if (col != null && col.equalsIgnoreCase("COORDINATOR_ROLE_SUF")) {
            label = "'National reporting coordinators suffix' changed ";
            value = getSuffixValue(value);
            currentValue = getSuffixValue(currentValue);
        } else if (col != null && col.equalsIgnoreCase("COORDINATOR")) {
            label = "'National reporting coordinators name' changed ";
        } else if (col != null && col.equalsIgnoreCase("COORDINATOR_URL")) {
            label = "'National reporting coordinators URL' changed ";
        } else if (col != null && col.equalsIgnoreCase("RESPONSIBLE_ROLE")) {
            label = "'National reporting contacts role' changed ";
        } else if (col != null && col.equalsIgnoreCase("RESPONSIBLE_ROLE_SUF")) {
            label = "'National reporting contacts suffix' changed ";
            value = getSuffixValue(value);
            currentValue = getSuffixValue(currentValue);
        } else if (col != null && col.equalsIgnoreCase("NATIONAL_CONTACT")) {
            label = "'National reporting contacts name' changed ";
        } else if (col != null && col.equalsIgnoreCase("REPORT_FREQ_MONTHS")) {
            label = "'Reporting frequency in months' changed ";
        } else if (col != null && col.equalsIgnoreCase("FIRST_REPORTING")) {
            label = "'Baseline reporting date' changed ";
        } else if (col != null && col.equalsIgnoreCase("VALID_TO")) {
            label = "'Valid to' changed ";
        } else if (col != null && col.equalsIgnoreCase("NEXT_DEADLINE")) {
            label = "'Next due date' changed ";
        } else if (col != null && col.equalsIgnoreCase("NEXT_REPORTING")) {
            label = "'Reporting date' changed ";
        } else if (col != null && col.equalsIgnoreCase("DATE_COMMENTS")) {
            label = "'Date comments' changed ";
        } else if (col != null && col.equalsIgnoreCase("FORMAT_NAME")) {
            label = "'Name of reporting guidelines' changed ";
        } else if (col != null && col.equalsIgnoreCase("REPORT_FORMAT_URL")) {
            label = "'URL to reporting guidelines' changed ";
        } else if (col != null && col.equalsIgnoreCase("VALID_SINCE")) {
            label = "'Format valid since' changed ";
        } else if (col != null && col.equalsIgnoreCase("REPORTING_FORMAT")) {
            label = "'Reporting guidelines -Extra info' changed ";
        } else if (col != null && col.equalsIgnoreCase("LOCATION_INFO")) {
            label = "'Name of repository' changed ";
        } else if (col != null && col.equalsIgnoreCase("LOCATION_PTR")) {
            label = "'URL to repository' changed ";
        } else if (col != null && col.equalsIgnoreCase("DATA_USED_FOR")) {
            label = "'Data used for' changed ";
        } else if (col != null && col.equalsIgnoreCase("LEGAL_MORAL")) {
            label = "'Obligation type' changed ";
        } else if (col != null && col.equalsIgnoreCase("PARAMETERS")) {
            label = "'Parameters' changed ";
        } else if (col != null && col.equalsIgnoreCase("EEA_PRIMARY")) {
            label = "'This obligation is EIONET Priority Data flow' changed ";
            value = getChkValue(value);
            currentValue = getChkValue(currentValue);
        } else if (col != null && col.equalsIgnoreCase("EEA_CORE")) {
            label = "'This obligation is used for EEA Core set of indicators' changed ";
            value = getChkValue(value);
            currentValue = getChkValue(currentValue);
        } else if (col != null && col.equalsIgnoreCase("FLAGGED")) {
            label = "'This obligation is flagged' changed ";
            value = getChkValue(value);
            currentValue = getChkValue(currentValue);
        } else if (col != null && col.equalsIgnoreCase("DPSIR_D")) {
            label = "'DPSIR D' changed ";
            value = getDpsirValue(value);
            currentValue = getDpsirValue(currentValue);
        } else if (col != null && col.equalsIgnoreCase("DPSIR_P")) {
            label = "'DPSIR P' changed ";
            value = getDpsirValue(value);
            currentValue = getDpsirValue(currentValue);
        } else if (col != null && col.equalsIgnoreCase("DPSIR_S")) {
            label = "'DPSIR S' changed ";
            value = getDpsirValue(value);
            currentValue = getDpsirValue(currentValue);
        } else if (col != null && col.equalsIgnoreCase("DPSIR_I")) {
            label = "'DPSIR I' changed ";
            value = getDpsirValue(value);
            currentValue = getDpsirValue(currentValue);
        } else if (col != null && col.equalsIgnoreCase("DPSIR_R")) {
            label = "'DPSIR R' changed ";
            value = getDpsirValue(value);
            currentValue = getDpsirValue(currentValue);
        } else if (col != null && col.equalsIgnoreCase("OVERLAP_URL")) {
            label = "'URL of overlapping obligation' changed ";
        } else if (col != null && col.equalsIgnoreCase("OVERLAP_URL")) {
            //Indicators
        } else if (col != null && col.equalsIgnoreCase("COMMENT")) {
            label = "'General comments' changed ";
        } else if (col != null && col.equalsIgnoreCase("AUTHORITY")) {
            label = "'Authority giving rise to the obligation' changed ";
        } else if (col != null && col.equalsIgnoreCase("RM_VERIFIED")) {
            label = "'Verified' changed ";
        } else if (col != null && col.equalsIgnoreCase("RM_VERIFIED_BY")) {
            label = "'Verified by' changed ";
        } else if (col != null && col.equalsIgnoreCase("RM_NEXT_UPDATE")) {
            label = "'Next update due' changed ";
        } else if (col != null && col.equalsIgnoreCase("VALIDATED_BY")) {
            label = "'Validated by' changed ";
        } else if (col != null && col.equalsIgnoreCase("FK_CLIENT_ID")) {
            label = "'Report to' changed ";
            value = RODServices.getDbService().getClientDao().getOrganisationNameByID(value);
            currentValue = RODServices.getDbService().getClientDao().getOrganisationNameByID(currentValue);
        } else if (col != null && col.equalsIgnoreCase("LAST_UPDATE")) {
            label = "'Last update' changed ";
        }

        label = label + " from '" + value + "' to '" + currentValue + "'";

        return label;
    }

    private String getDpsirValue(String value) throws ServiceException {

         String ret = null;
         if (value.equalsIgnoreCase("null") || value.equalsIgnoreCase("no")) {
             ret="unchecked";
         }
         if (value.equalsIgnoreCase("yes")) {
             ret="checked";
         }
         return ret;
    }

    private String getChkValue(String value) throws ServiceException {

        String ret = null;
        int b = new Integer(value).intValue();
        if (b == 0) {
            ret="unchecked";
        }
        if (b == 1) {
            ret="checked";
        }
        return ret;
   }

    private String getSuffixValue(String value) throws ServiceException {

        String ret = null;
        int b = new Integer(value).intValue();
        if (b == 0) {
            ret="checked";
        }
        if (b == 1) {
            ret="unchecked";
        }
        return ret;
    }


    //methods for EDIT, ADD and DELETE obligation ends

    public ObligationFactsheetDTO getObligation() {
        return obligation;
    }

    public void setObligation(ObligationFactsheetDTO obligation) {
        this.obligation = obligation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ClientDTO> getClients() {
        return clients;
    }

    public void setClients(List<ClientDTO> clients) {
        this.clients = clients;
    }

    public List<LookupDTO> getInfoTypeList() {
        return infoTypeList;
    }

    public void setInfoTypeList(List<LookupDTO> infoTypeList) {
        this.infoTypeList = infoTypeList;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }


    public List<CountryDeliveryDTO> getDeliveries() {
        return deliveries;
    }


    public void setDeliveries(List<CountryDeliveryDTO> deliveries) {
        this.deliveries = deliveries;
    }


    public String getSpatialId() {
        return spatialId;
    }


    public void setSpatialId(String spatialId) {
        this.spatialId = spatialId;
    }


    public List<VersionDTO> getVersions() {
        return versions;
    }


    public void setVersions(List<VersionDTO> versions) {
        this.versions = versions;
    }


    public List<SiblingObligationDTO> getSiblingObligations() {
        return siblingObligations;
    }


    public void setSiblingObligations(List<SiblingObligationDTO> siblingObligations) {
        this.siblingObligations = siblingObligations;
    }


    public List<ObligationCountryDTO> getCountries() {
        return countries;
    }


    public void setCountries(List<ObligationCountryDTO> countries) {
        this.countries = countries;
    }


    public List<IssueDTO> getIssues() {
        return issues;
    }


    public void setIssues(List<IssueDTO> issues) {
        this.issues = issues;
    }

    public List<DDParamDTO> getDdparameters() {
        return ddparameters;
    }

    public void setDdparameters(List<DDParamDTO> ddparameters) {
        this.ddparameters = ddparameters;
    }

    public List<ObligationsListDTO> getObligations() {
        return obligations;
    }

    public void setObligations(List<ObligationsListDTO> obligations) {
        this.obligations = obligations;
    }

    public String getAnmode() {
        return anmode;
    }

    public void setAnmode(String anmode) {
        this.anmode = anmode;
    }

    public List<CountryDTO> getFormCountries() {
        return formCountries;
    }

    public void setFormCountries(List<CountryDTO> formCountries) {
        this.formCountries = formCountries;
    }

    public List<IssueDTO> getFormIssues() {
        return formIssues;
    }

    public void setFormIssues(List<IssueDTO> formIssues) {
        this.formIssues = formIssues;
    }

    public List<ClientDTO> getFormClients() {
        return formClients;
    }

    public void setFormClients(List<ClientDTO> formClients) {
        this.formClients = formClients;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getTerminated() {
        return terminated;
    }

    public void setTerminated(String terminated) {
        this.terminated = terminated;
    }

    public List<ObligationsListDTO> getIndirectObligations() {
        return indirectObligations;
    }

    public void setIndirectObligations(List<ObligationsListDTO> indirectObligations) {
        this.indirectObligations = indirectObligations;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<CountryDTO> getAllcountries() {
        return allcountries;
    }

    public void setAllcountries(List<CountryDTO> allcountries) {
        this.allcountries = allcountries;
    }

    public List<LookupDTO> getLegalMoral() {
        return legalMoral;
    }

    public void setLegalMoral(List<LookupDTO> legalMoral) {
        this.legalMoral = legalMoral;
    }

    public List<ClientDTO> getObligationClients() {
        return obligationClients;
    }

    public void setObligationClients(List<ClientDTO> obligationClients) {
        this.obligationClients = obligationClients;
    }

    public List<CountryDTO> getFormalCountries() {
        return formalCountries;
    }

    public void setFormalCountries(List<CountryDTO> formalCountries) {
        this.formalCountries = formalCountries;
    }

    public List<CountryDTO> getVoluntaryCountries() {
        return voluntaryCountries;
    }

    public void setVoluntaryCountries(List<CountryDTO> voluntaryCountries) {
        this.voluntaryCountries = voluntaryCountries;
    }

    public List<IssueDTO> getObligationIssues() {
        return obligationIssues;
    }

    public void setObligationIssues(List<IssueDTO> obligationIssues) {
        this.obligationIssues = obligationIssues;
    }

    public List<String> getSelectedInfoTypes() {
        return selectedInfoTypes;
    }

    public void setSelectedInfoTypes(List<String> selectedInfoTypes) {
        this.selectedInfoTypes = selectedInfoTypes;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public InstrumentFactsheetDTO getInstrument() {
        return instrument;
    }

    public void setInstrument(InstrumentFactsheetDTO instrument) {
        this.instrument = instrument;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getDgenv() {
        return dgenv;
    }

    public void setDgenv(String dgenv) {
        this.dgenv = dgenv;
    }

    public List<String> getSelectedClients() {
        return selectedClients;
    }

    public void setSelectedClients(List<String> selectedClients) {
        this.selectedClients = selectedClients;
    }

    public List<String> getSelClients() {
        return selClients;
    }

    public void setSelClients(List<String> selClients) {
        this.selClients = selClients;
    }

    public List<String> getSelectedFormalCountries() {
        return selectedFormalCountries;
    }

    public void setSelectedFormalCountries(List<String> selectedFormalCountries) {
        this.selectedFormalCountries = selectedFormalCountries;
    }

    public List<String> getForCountries() {
        return forCountries;
    }

    public void setForCountries(List<String> forCountries) {
        this.forCountries = forCountries;
    }

    public List<String> getSelectedVoluntaryCountries() {
        return selectedVoluntaryCountries;
    }

    public void setSelectedVoluntaryCountries(
            List<String> selectedVoluntaryCountries) {
        this.selectedVoluntaryCountries = selectedVoluntaryCountries;
    }

    public List<String> getVolCountries() {
        return volCountries;
    }

    public void setVolCountries(List<String> volCountries) {
        this.volCountries = volCountries;
    }

    public List<String> getSelectedIssues() {
        return selectedIssues;
    }

    public void setSelectedIssues(List<String> selectedIssues) {
        this.selectedIssues = selectedIssues;
    }

    public List<String> getSelIssues() {
        return selIssues;
    }

    public void setSelIssues(List<String> selIssues) {
        this.selIssues = selIssues;
    }

    public IUndoDao getUndoDao() {
        return undoDao;
    }

    public void setUndoDao(IUndoDao undoDao) {
        this.undoDao = undoDao;
    }

    public IObligationDao getObligationDao() {
        return obligationDao;
    }

    public void setObligationDao(IObligationDao obligationDao) {
        this.obligationDao = obligationDao;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}
