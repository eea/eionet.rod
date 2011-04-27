package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class InstrumentDTO implements java.io.Serializable{

    //Fields from T_SOURCE table
    private int sourceId;
    private String sourceTitle;
    private String sourceAlias;
    private String sourceLegalName;


    /**
     *
     */
    public InstrumentDTO() {
    }


    public int getSourceId() {
        return sourceId;
    }


    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }


    public String getSourceTitle() {
        return sourceTitle;
    }


    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }


    public String getSourceAlias() {
        return sourceAlias;
    }


    public void setSourceAlias(String sourceAlias) {
        this.sourceAlias = sourceAlias;
    }


    public String getSourceLegalName() {
        return sourceLegalName;
    }


    public void setSourceLegalName(String sourceLegalName) {
        this.sourceLegalName = sourceLegalName;
    }




}
