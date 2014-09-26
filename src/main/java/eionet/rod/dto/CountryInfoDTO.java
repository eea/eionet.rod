package eionet.rod.dto;

import java.util.List;

/**
 *
 * @author altnyris
 *
 */
public class CountryInfoDTO implements java.io.Serializable {

    private String obligationTitle;
    private String role;
    private String country;
    private String twoLetter;
    private String start;
    private String end;
    private List<DeliveryDTO> deliveries;


    /**
     *
     */
    public CountryInfoDTO() {
    }


    public String getObligationTitle() {
        return obligationTitle;
    }


    public void setObligationTitle(String obligationTitle) {
        this.obligationTitle = obligationTitle;
    }


    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }


    public String getCountry() {
        return country;
    }


    public void setCountry(String country) {
        this.country = country;
    }


    public String getTwoLetter() {
        return twoLetter;
    }


    public void setTwoLetter(String twoLetter) {
        this.twoLetter = twoLetter;
    }


    public String getStart() {
        return start;
    }


    public void setStart(String start) {
        this.start = start;
    }


    public String getEnd() {
        return end;
    }


    public void setEnd(String end) {
        this.end = end;
    }


    public List<DeliveryDTO> getDeliveries() {
        return deliveries;
    }


    public void setDeliveries(List<DeliveryDTO> deliveries) {
        this.deliveries = deliveries;
    }

}
