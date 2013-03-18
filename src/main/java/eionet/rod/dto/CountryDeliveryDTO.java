package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class CountryDeliveryDTO implements java.io.Serializable{

    //Fields from T_DELIVERY table
    private Integer deliveryFKObligationId;
    private Integer deliveryFKSpatialId;
    private String deliveryTitle;
    private String deliveryUrl;
    private String deliveryUploadDate;
    private String deliveryType;
    private String deliveryFormat;
    private String deliveryCoverage;
    private String deliveryCoverageNote;

    //Fields from T_OBLIGATION table
    private Integer obligationId;
    private Integer obligationFKSourceId;
    private String obligationTitle;
    private String obligationReportFreqMonths;
    private String obligationTerminate;
    private String obligationNextDeadline;
    private String obligationReportFormatUrl;
    private String obligationRespRole;
    private String obligationFormatName;
    private String obligationFKDeliveryCountryIds;
    private String obligationParameters;

    //Fields from T_SPATIAL table
    private Integer spatialId;
    private String spatialName;
    private String spatialTwoLetter;
    private String spatialIsMember;

    //Fields from T_ROLE table
    private String roleName;
    private String roleUrl;
    private String roleMembersUrl;

    //Fields from T_CLIENT_LNK table
    private Integer clientLnkFKClientId;
    private Integer clientLnkFKObjectId;
    private String clientLnkStatus;
    private String clientLnkType;

    //Fields from T_CLIENT table
    private Integer clientId;
    private String clientName;

    public Integer getDeliveryFKObligationId() {
        return deliveryFKObligationId;
    }
    public void setDeliveryFKObligationId(Integer deliveryFKObligationId) {
        this.deliveryFKObligationId = deliveryFKObligationId;
    }
    public Integer getDeliveryFKSpatialId() {
        return deliveryFKSpatialId;
    }
    public void setDeliveryFKSpatialId(Integer deliveryFKSpatialId) {
        this.deliveryFKSpatialId = deliveryFKSpatialId;
    }
    public String getDeliveryTitle() {
        return deliveryTitle;
    }
    public void setDeliveryTitle(String deliveryTitle) {
        this.deliveryTitle = deliveryTitle;
    }
    public String getDeliveryUrl() {
        return deliveryUrl;
    }
    public void setDeliveryUrl(String deliveryUrl) {
        this.deliveryUrl = deliveryUrl;
    }
    public String getDeliveryUploadDate() {
        return deliveryUploadDate;
    }
    public void setDeliveryUploadDate(String deliveryUploadDate) {
        this.deliveryUploadDate = deliveryUploadDate;
    }
    public String getDeliveryType() {
        return deliveryType;
    }
    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
    public String getDeliveryFormat() {
        return deliveryFormat;
    }
    public void setDeliveryFormat(String deliveryFormat) {
        this.deliveryFormat = deliveryFormat;
    }
    public String getDeliveryCoverage() {
        return deliveryCoverage;
    }
    public void setDeliveryCoverage(String deliveryCoverage) {
        this.deliveryCoverage = deliveryCoverage;
    }
    public Integer getObligationId() {
        return obligationId;
    }
    public void setObligationId(Integer obligationId) {
        this.obligationId = obligationId;
    }
    public Integer getObligationFKSourceId() {
        return obligationFKSourceId;
    }
    public void setObligationFKSourceId(Integer obligationFKSourceId) {
        this.obligationFKSourceId = obligationFKSourceId;
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
    public String getObligationReportFormatUrl() {
        return obligationReportFormatUrl;
    }
    public void setObligationReportFormatUrl(String obligationReportFormatUrl) {
        this.obligationReportFormatUrl = obligationReportFormatUrl;
    }
    public String getObligationRespRole() {
        return obligationRespRole;
    }
    public void setObligationRespRole(String obligationRespRole) {
        this.obligationRespRole = obligationRespRole;
    }
    public String getObligationFormatName() {
        return obligationFormatName;
    }
    public void setObligationFormatName(String obligationFormatName) {
        this.obligationFormatName = obligationFormatName;
    }
    public String getObligationFKDeliveryCountryIds() {
        return obligationFKDeliveryCountryIds;
    }
    public void setObligationFKDeliveryCountryIds(
            String obligationFKDeliveryCountryIds) {
        this.obligationFKDeliveryCountryIds = obligationFKDeliveryCountryIds;
    }
    public String getObligationParameters() {
        return obligationParameters;
    }
    public void setObligationParameters(String obligationParameters) {
        this.obligationParameters = obligationParameters;
    }
    public Integer getSpatialId() {
        return spatialId;
    }
    public void setSpatialId(Integer spatialId) {
        this.spatialId = spatialId;
    }
    public String getSpatialName() {
        return spatialName;
    }
    public void setSpatialName(String spatialName) {
        this.spatialName = spatialName;
    }
    public String getSpatialTwoLetter() {
        return spatialTwoLetter;
    }
    public void setSpatialTwoLetter(String spatialTwoLetter) {
        this.spatialTwoLetter = spatialTwoLetter;
    }
    public String getSpatialIsMember() {
        return spatialIsMember;
    }
    public void setSpatialIsMember(String spatialIsMember) {
        this.spatialIsMember = spatialIsMember;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public String getRoleUrl() {
        return roleUrl;
    }
    public void setRoleUrl(String roleUrl) {
        this.roleUrl = roleUrl;
    }
    public String getRoleMembersUrl() {
        return roleMembersUrl;
    }
    public void setRoleMembersUrl(String roleMembersUrl) {
        this.roleMembersUrl = roleMembersUrl;
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
    /**
     * @return the deliveryCoverageNote
     */
    public String getDeliveryCoverageNote() {
        return deliveryCoverageNote;
    }
    /**
     * @param deliveryCoverageNote the deliveryCoverageNote to set
     */
    public void setDeliveryCoverageNote(String deliveryCoverageNote) {
        this.deliveryCoverageNote = deliveryCoverageNote;
    }
}
