package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class SearchDTO implements java.io.Serializable{

    //Fields from T_OBLIGATION table
    private Integer obligationId;
    private String obligationTitle;
    private String obligationRespRole;
    private String obligationNextReporting;
    private String obligationNextDeadline;
    private Integer obligationFKSourceId;
    private String obligationTerminate;
    private Integer obligationFKClientId;
    private String obligationFKDeliveryCountryIds;
    private String obligationDeadline;
    private String obligationDeadline2;
    private int obligationHasDelivery;

    //Fields from T_ROLE table
    private String roleDescr;
    private String roleUrl;
    private String roleMembersUrl;

    //Fields from T_CLIENT_LNK table
    private Integer clientLnkFKClientId;
    private Integer clientLnkFKObjectId;
    private String clientLnkType;
    private String clientLnkStatus;

    //Fields from T_CLIENT table
    private Integer clientId;
    private String clientName;
    private String clientDescr;

    //Fields from T_RASPATIAL_LNK table
    private Integer spatialLnkFKObligationId;
    private Integer spatialLnkFKSpatialId;

    //Fields from T_SPATIAL table
    private Integer spatialId;
    private String spatialName;
    private String spatialTwoLetter;
    private String spatialIsMember;

    //Fields from T_SOURCE table
    private Integer sourceId;
    private String sourceCode;

    //Fields from T_RAISSUE_LNK table
    private Integer issueLnkFKObligationId;
    private Integer issueLnkFKIssueId;


    /**
     *
     */
    public SearchDTO() {
    }


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


    public String getObligationRespRole() {
        return obligationRespRole;
    }


    public void setObligationRespRole(String obligationRespRole) {
        this.obligationRespRole = obligationRespRole;
    }


    public String getObligationNextReporting() {
        return obligationNextReporting;
    }


    public void setObligationNextReporting(String obligationNextReporting) {
        this.obligationNextReporting = obligationNextReporting;
    }


    public String getObligationNextDeadline() {
        return obligationNextDeadline;
    }


    public void setObligationNextDeadline(String obligationNextDeadline) {
        this.obligationNextDeadline = obligationNextDeadline;
    }


    public Integer getObligationFKSourceId() {
        return obligationFKSourceId;
    }


    public void setObligationFKSourceId(Integer obligationFKSourceId) {
        this.obligationFKSourceId = obligationFKSourceId;
    }


    public String getObligationTerminate() {
        return obligationTerminate;
    }


    public void setObligationTerminate(String obligationTerminate) {
        this.obligationTerminate = obligationTerminate;
    }


    public Integer getObligationFKClientId() {
        return obligationFKClientId;
    }


    public void setObligationFKClientId(Integer obligationFKClientId) {
        this.obligationFKClientId = obligationFKClientId;
    }


    public String getObligationFKDeliveryCountryIds() {
        return obligationFKDeliveryCountryIds;
    }


    public void setObligationFKDeliveryCountryIds(
            String obligationFKDeliveryCountryIds) {
        this.obligationFKDeliveryCountryIds = obligationFKDeliveryCountryIds;
    }


    public String getObligationDeadline() {
        return obligationDeadline;
    }


    public void setObligationDeadline(String obligationDeadline) {
        this.obligationDeadline = obligationDeadline;
    }


    public String getObligationDeadline2() {
        return obligationDeadline2;
    }


    public void setObligationDeadline2(String obligationDeadline2) {
        this.obligationDeadline2 = obligationDeadline2;
    }


    public int getObligationHasDelivery() {
        return obligationHasDelivery;
    }


    public void setObligationHasDelivery(int obligationHasDelivery) {
        this.obligationHasDelivery = obligationHasDelivery;
    }


    public String getRoleDescr() {
        return roleDescr;
    }


    public void setRoleDescr(String roleDescr) {
        this.roleDescr = roleDescr;
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


    public String getClientLnkType() {
        return clientLnkType;
    }


    public void setClientLnkType(String clientLnkType) {
        this.clientLnkType = clientLnkType;
    }


    public String getClientLnkStatus() {
        return clientLnkStatus;
    }


    public void setClientLnkStatus(String clientLnkStatus) {
        this.clientLnkStatus = clientLnkStatus;
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


    public String getClientDescr() {
        return clientDescr;
    }


    public void setClientDescr(String clientDescr) {
        this.clientDescr = clientDescr;
    }


    public Integer getSpatialLnkFKObligationId() {
        return spatialLnkFKObligationId;
    }


    public void setSpatialLnkFKObligationId(Integer spatialLnkFKObligationId) {
        this.spatialLnkFKObligationId = spatialLnkFKObligationId;
    }


    public Integer getSpatialLnkFKSpatialId() {
        return spatialLnkFKSpatialId;
    }


    public void setSpatialLnkFKSpatialId(Integer spatialLnkFKSpatialId) {
        this.spatialLnkFKSpatialId = spatialLnkFKSpatialId;
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


    public Integer getSourceId() {
        return sourceId;
    }


    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }


    public String getSourceCode() {
        return sourceCode;
    }


    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }


    public Integer getIssueLnkFKObligationId() {
        return issueLnkFKObligationId;
    }


    public void setIssueLnkFKObligationId(Integer issueLnkFKObligationId) {
        this.issueLnkFKObligationId = issueLnkFKObligationId;
    }


    public Integer getIssueLnkFKIssueId() {
        return issueLnkFKIssueId;
    }


    public void setIssueLnkFKIssueId(Integer issueLnkFKIssueId) {
        this.issueLnkFKIssueId = issueLnkFKIssueId;
    }

}
