package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class DocumentationDTO implements java.io.Serializable{

    private String areaId;
    private String screenId;
    private String description;
    private String html;


    /**
     *
     */
    public DocumentationDTO() {
    }


    public String getAreaId() {
        return areaId;
    }


    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }


    public String getScreenId() {
        return screenId;
    }


    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getHtml() {
        return html;
    }


    public void setHtml(String html) {
        this.html = html;
    }



}
