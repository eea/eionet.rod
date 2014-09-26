package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class HelpDTO implements java.io.Serializable {

    private String helpId;
    private String helpTitle;
    private String helpText;
    /**
     *
     */
    public HelpDTO() {
    }
    public String getHelpId() {
        return helpId;
    }
    public void setHelpId(String helpId) {
        this.helpId = helpId;
    }
    public String getHelpTitle() {
        return helpTitle;
    }
    public void setHelpTitle(String helpTitle) {
        this.helpTitle = helpTitle;
    }
    public String getHelpText() {
        return helpText;
    }
    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }
}
