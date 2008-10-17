package eionet.rod.dto;

/**
 * 
 * @author altnyris
 *
 */
public class CountryDTO implements java.io.Serializable{
	
	private Integer countryId;
	private String name;
	
	
	/**
	 * 
	 */
	public CountryDTO(){
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

}
