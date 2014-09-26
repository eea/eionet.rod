package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class IssueDTO implements java.io.Serializable {

    private Integer issueId;
    private String name;

    /**
     *
     */
    public IssueDTO() {
    }

    public Integer getIssueId() {
        return issueId;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
