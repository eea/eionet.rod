package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class ObligationDTO implements java.io.Serializable {

    private Integer obligationId;
    private Integer sourceId;
    private String title;

    /**
     * Constructor.
     */
    public ObligationDTO() {
    }

    public Integer getObligationId() {
        return obligationId;
    }

    public void setObligationId(Integer obligationId) {
        this.obligationId = obligationId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
