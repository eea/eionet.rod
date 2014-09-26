package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class ObligationsListDTO implements java.io.Serializable {

    private Integer obligationId;
    private String obligationTitle;
    private Integer sourceId;
    private String sourceTitle;
    private Integer clientId;
    private String clientName;
    private String clientDescr;
    private String nextDeadline;
    private String nextReporting;
    private String fkDeliveryCountryIds;
    private String terminate;


    /**
     *
     */
    public ObligationsListDTO() {
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


    public Integer getClientId() {
        return clientId;
    }


    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }


    public void setObligationTitle(String obligationTitle) {
        this.obligationTitle = obligationTitle;
    }


    public Integer getSourceId() {
        return sourceId;
    }


    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }


    public String getSourceTitle() {
        return sourceTitle;
    }


    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }


    public String getClientName() {
        return clientName;
    }


    public String getClientDescr() {
        return clientDescr;
    }


    public void setClientDescr(String clientDescr) {
        this.clientDescr = clientDescr;
    }


    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public String getNextDeadline() {
        return nextDeadline;
    }


    public void setNextDeadline(String nextDeadline) {
        this.nextDeadline = nextDeadline;
    }


    public String getNextReporting() {
        return nextReporting;
    }


    public void setNextReporting(String nextReporting) {
        this.nextReporting = nextReporting;
    }


    public String getFkDeliveryCountryIds() {
        return fkDeliveryCountryIds;
    }


    public void setFkDeliveryCountryIds(String fkDeliveryCountryIds) {
        this.fkDeliveryCountryIds = fkDeliveryCountryIds;
    }


    public String getTerminate() {
        return terminate;
    }


    public void setTerminate(String terminate) {
        this.terminate = terminate;
    }


}
