package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class UrlDTO implements java.io.Serializable {

    private String title;
    private String url;

    /**
     *
     */
    public UrlDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
