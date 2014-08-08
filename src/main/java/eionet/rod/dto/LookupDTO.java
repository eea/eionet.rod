package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class LookupDTO implements java.io.Serializable {

    private String cterm;
    private String cvalue;

    /**
     *
     */
    public LookupDTO() {
    }

    public String getCterm() {
        return cterm;
    }

    public void setCterm(String cterm) {
        this.cterm = cterm;
    }

    public String getCvalue() {
        return cvalue;
    }

    public void setCvalue(String cvalue) {
        this.cvalue = cvalue;
    }



}
