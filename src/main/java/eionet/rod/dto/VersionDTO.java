package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class VersionDTO implements java.io.Serializable{

    private String undoTime;
    private String col;
    private String tab;
    private String operation;
    private String value;
    private String showObject;

    /**
     *
     */
    public VersionDTO() {
    }

    public String getUndoTime() {
        return undoTime;
    }

    public void setUndoTime(String undoTime) {
        this.undoTime = undoTime;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getShowObject() {
        return showObject;
    }

    public void setShowObject(String showObject) {
        this.showObject = showObject;
    }


}
