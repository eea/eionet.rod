package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class CountryDeliveryDataDTO implements java.io.Serializable {

    //Fields from T_OBLIGATION table
    private Integer obligationId;
    private String obligationTitle;
    private String obligationReportFreqMonths;
    private String obligationTerminate;
    private String obligationNextDeadline;
    private String obligationFormatName;
    private String obligationLastHarvested;
    private String obligationReportFormatUrl;

    //Fields from T_CLIENT_LNK table
    private Integer clientLnkFKClientId;
    private Integer clientLnkFKObjectId;
    private String clientLnkStatus;
    private String clientLnkType;

    //Fields from T_CLIENT table
    private Integer clientId;
    private String clientName;

    public Integer getObligationId() {
        return obligationId;
    }
    public void setObligationId(Integer obligationId) {
        this.obligationId = obligationId;
    }
    public String getObligationTitle() {
        return obligationTitle;
    }
    public void setObligationTitle(String obligationTitle) {
        this.obligationTitle = obligationTitle;
    }
    public String getObligationReportFreqMonths() {
        return obligationReportFreqMonths;
    }
    public void setObligationReportFreqMonths(String obligationReportFreqMonths) {
        this.obligationReportFreqMonths = obligationReportFreqMonths;
    }
    public String getObligationTerminate() {
        return obligationTerminate;
    }
    public void setObligationTerminate(String obligationTerminate) {
        this.obligationTerminate = obligationTerminate;
    }
    public String getObligationNextDeadline() {
        return obligationNextDeadline;
    }
    public void setObligationNextDeadline(String obligationNextDeadline) {
        this.obligationNextDeadline = obligationNextDeadline;
    }
    public String getObligationFormatName() {
        return obligationFormatName;
    }
    public void setObligationFormatName(String obligationFormatName) {
        this.obligationFormatName = obligationFormatName;
    }
    public String getObligationLastHarvested() {
        return obligationLastHarvested;
    }
    public void setObligationLastHarvested(String obligationLastHarvested) {
        this.obligationLastHarvested = obligationLastHarvested;
    }
    public Integer getClientLnkFKClientId() {
        return clientLnkFKClientId;
    }
    public void setClientLnkFKClientId(Integer clientLnkFKClientId) {
        this.clientLnkFKClientId = clientLnkFKClientId;
    }
    public Integer getClientLnkFKObjectId() {
        return clientLnkFKObjectId;
    }
    public void setClientLnkFKObjectId(Integer clientLnkFKObjectId) {
        this.clientLnkFKObjectId = clientLnkFKObjectId;
    }
    public String getClientLnkStatus() {
        return clientLnkStatus;
    }
    public void setClientLnkStatus(String clientLnkStatus) {
        this.clientLnkStatus = clientLnkStatus;
    }
    public String getClientLnkType() {
        return clientLnkType;
    }
    public void setClientLnkType(String clientLnkType) {
        this.clientLnkType = clientLnkType;
    }
    public Integer getClientId() {
        return clientId;
    }
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public String getObligationReportFormatUrl() {
        return obligationReportFormatUrl;
    }
    public void setObligationReportFormatUrl(String obligationReportFormatUrl) {
        this.obligationReportFormatUrl = obligationReportFormatUrl;
    }



}
