package eionet.rod.dto;

/**
 * 
 * @author altnyris
 *
 */
public class RoleOccupantDTO implements java.io.Serializable{
	
	private String person;
	private String institute;
	
	/**
	 * 
	 */
	public RoleOccupantDTO(){
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}
	
}
