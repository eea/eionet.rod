package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class InstrumentParentDTO implements java.io.Serializable {

    //Fields from T_SOURCE_CLASS table
    private int classId;
    private String classificator;
    private String className;


    /**
     *
     */
    public InstrumentParentDTO() {
    }


    public int getClassId() {
        return classId;
    }


    public void setClassId(int classId) {
        this.classId = classId;
    }


    public String getClassificator() {
        return classificator;
    }


    public void setClassificator(String classificator) {
        this.classificator = classificator;
    }


    public String getClassName() {
        return className;
    }


    public void setClassName(String className) {
        this.className = className;
    }


}
