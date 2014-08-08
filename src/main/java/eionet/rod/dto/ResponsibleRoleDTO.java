package eionet.rod.dto;

import java.util.List;

/**
 *
 * @author altnyris
 *
 */
public class ResponsibleRoleDTO implements java.io.Serializable {

    private String name;
    private String email;
    private String roleUrl;
    private String membersUrl;
    private String lastHarvested;
    private List<RoleOccupantDTO> occupants;
    private List<ObligationDTO> obligations;


    /**
     *
     */
    public ResponsibleRoleDTO() {
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getRoleUrl() {
        return roleUrl;
    }


    public void setRoleUrl(String roleUrl) {
        this.roleUrl = roleUrl;
    }


    public String getMembersUrl() {
        return membersUrl;
    }


    public void setMembersUrl(String membersUrl) {
        this.membersUrl = membersUrl;
    }


    public String getLastHarvested() {
        return lastHarvested;
    }


    public void setLastHarvested(String lastHarvested) {
        this.lastHarvested = lastHarvested;
    }


    public List<RoleOccupantDTO> getOccupants() {
        return occupants;
    }


    public void setOccupants(List<RoleOccupantDTO> occupants) {
        this.occupants = occupants;
    }


    public List<ObligationDTO> getObligations() {
        return obligations;
    }


    public void setObligations(List<ObligationDTO> obligations) {
        this.obligations = obligations;
    }


}
