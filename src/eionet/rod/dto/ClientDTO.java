package eionet.rod.dto;

/**
 * 
 * @author altnyris
 *
 */
public class ClientDTO implements java.io.Serializable{
	
	private Integer clientId;
	private String name;
	private String acronym;
	
	/**
	 * 
	 */
	public ClientDTO(){
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	
}
