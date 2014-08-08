package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class DifferenceDTO implements java.io.Serializable {

    private String undo;
    private String current;
    private String added;
    private String removed;

    /**
     *
     */
    public DifferenceDTO() {
    }

    public String getUndo() {
        return undo;
    }

    public void setUndo(String undo) {
        this.undo = undo;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getRemoved() {
        return removed;
    }

    public void setRemoved(String removed) {
        this.removed = removed;
    }
}
