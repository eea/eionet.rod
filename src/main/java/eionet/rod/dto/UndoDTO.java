package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class UndoDTO implements java.io.Serializable{

    private String undoTime;
    private String tabel;
    private String column;
    private String operation;
    private String value;
    private String currentValue;
    private String subTransNr;
    private boolean diff;


    /**
     *
     */
    public UndoDTO() {
    }


    public String getUndoTime() {
        return undoTime;
    }


    public void setUndoTime(String undoTime) {
        this.undoTime = undoTime;
    }


    public String getTabel() {
        return tabel;
    }


    public void setTabel(String tabel) {
        this.tabel = tabel;
    }


    public String getColumn() {
        return column;
    }


    public void setColumn(String column) {
        this.column = column;
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


    public String getCurrentValue() {
        return currentValue;
    }


    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }


    public String getSubTransNr() {
        return subTransNr;
    }


    public void setSubTransNr(String subTransNr) {
        this.subTransNr = subTransNr;
    }


    public boolean isDiff() {
        return diff;
    }


    public void setDiff(boolean diff) {
        this.diff = diff;
    }


}
