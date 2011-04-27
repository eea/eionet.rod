package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class InstrumentObligationDTO implements java.io.Serializable{

    //Fields from T_SOURCE_CLASS table
    private int obligationId;
    private String title;
    private String authority;
    private String terminate;


    /**
     *
     */
    public InstrumentObligationDTO() {
    }


    public int getObligationId() {
        return obligationId;
    }


    public void setObligationId(int obligationId) {
        this.obligationId = obligationId;
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
