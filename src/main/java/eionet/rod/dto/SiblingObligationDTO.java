package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class SiblingObligationDTO implements java.io.Serializable{

    private String obligationId;
    private String fkSourceId;
    private String title;
    private String authority;
    private String terminate;

    /**
     *
     */
    public SiblingObligationDTO() {
    }

    public String getObligationId() {
        return obligationId;
    }

    public void setObligationId(String obligationId) {
        this.obligationId = obligationId;
    }

    public String getFkSourceId() {
        return fkSourceId;
    }

    public void setFkSourceId(String fkSourceId) {
        this.fkSourceId = fkSourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getTerminate() {
        return terminate;
    }

    public void setTerminate(String terminate) {
        this.terminate = terminate;
    }




}
