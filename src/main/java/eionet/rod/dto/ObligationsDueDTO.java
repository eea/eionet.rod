package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class ObligationsDueDTO implements java.io.Serializable{

    private Integer obligationId;
    private String obligationTitle;
    private String lastUpdate;
    private String validatedBy;
    private String nextUpdate;
    private String verified;
    private String verifiedBy;


    /**
     *
     */
    public ObligationsDueDTO() {
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


    public String getLastUpdate() {
        return lastUpdate;
    }


    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    public String getValidatedBy() {
        return validatedBy;
    }


    public void setValidatedBy(String validatedBy) {
        this.validatedBy = validatedBy;
    }


    public String getNextUpdate() {
        return nextUpdate;
    }


    public void setNextUpdate(String nextUpdate) {
        this.nextUpdate = nextUpdate;
    }


    public String getVerified() {
        return verified;
    }


    public void setVerified(String verified) {
        this.verified = verified;
    }


    public String getVerifiedBy() {
        return verifiedBy;
    }


    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }



}
