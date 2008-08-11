package eionet.rod.dto;

/**
 * 
 * @author altnyris
 *
 */
public class CountryDTO implements java.io.Serializable{
	
	private Integer countryId;
	private String name;
	private String twoLetter;
	private String isMemberCountry;
	private String type;
	
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

	public String getTwoLetter() {
		return twoLetter;
	}

	public void setTwoLetter(String twoLetter) {
		this.twoLetter = twoLetter;
	}

	public String getIsMemberCountry() {
		return isMemberCountry;
	}

	public void setIsMemberCountry(String isMemberCountry) {
		this.isMemberCountry = isMemberCountry;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}