package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class HistDeadlineDTO implements java.io.Serializable {

    private Integer obligationId;
    private Integer sourceId;
    private String obligationTitle;
    private String deadline;

    /**
     *
     */
    public HistDeadlineDTO() {
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

    public String getObligationTitle() {
        return obligationTitle;
    }

    public void setObligationTitle(String obligationTitle) {
        this.obligationTitle = obligationTitle;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }



}
