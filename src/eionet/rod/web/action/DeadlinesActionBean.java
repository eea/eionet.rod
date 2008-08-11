package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.dto.CountryDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/deliveries")
public class DeadlinesActionBean extends AbstractRODActionBean {
	
	private List<CountryDTO> memberCountries;
	private List<CountryDTO> nonMemberCountries;
	private int membersCount;
	private int nonMembersCount;
	
	/**
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		memberCountries = RODServices.getDbService().getSpatialDao().getMemberCountries();
		nonMemberCountries = RODServices.getDbService().getSpatialDao().getNonMemberCountries();
		
		membersCount = memberCountries.size() / 3;
		nonMembersCount = nonMemberCountries.size() / 3;
		
		return new ForwardResolution("/pages/deadlines.jsp");
	}

	public List<CountryDTO> getNonMemberCountries() {
		return nonMemberCountries;
	}

	public void setNonMemberCountries(List<CountryDTO> nonMemberCountries) {
		this.nonMemberCountries = nonMemberCountries;
	}

	public int getNonMembersCount() {
		return nonMembersCount;
	}

	public void setNonMembersCount(int nonMembersCount) {
		this.nonMembersCount = nonMembersCount;
	}

	public List<CountryDTO> getMemberCountries() {
		return memberCountries;
	}

	public void setMemberCountries(List<CountryDTO> memberCountries) {
		this.memberCountries = memberCountries;
	}

	public int getMembersCount() {
		return membersCount;
	}

	public void setMembersCount(int membersCount) {
		this.membersCount = membersCount;
	}

}
