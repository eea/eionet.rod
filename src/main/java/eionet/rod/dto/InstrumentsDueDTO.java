package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class InstrumentsDueDTO implements java.io.Serializable{

    private Integer instrumentId;
    private String title;
    private String nextUpdate;
    private String verified;
    private String verifiedBy;

    /**
     *
     */
    public InstrumentsDueDTO() {
    }

    public Integer getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Integer instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
