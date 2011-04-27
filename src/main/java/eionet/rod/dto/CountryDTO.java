package eionet.rod.dto;

/**
 *
 * @author altnyris
 *
 */
public class CountryDTO implements java.io.Serializable{

    private Integer countryId;
    private String name;
    private String type;
    private String twoletter;
    private String isMember;


    /**
     *
     */
    public CountryDTO() {
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTwoletter() {
        return twoletter;
    }

    public void setTwoletter(String twoletter) {
        this.twoletter = twoletter;
    }

    public String getIsMember() {
        return isMember;
    }

    public void setIsMember(String isMember) {
        this.isMember = isMember;
    }

}
