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
	private int membersCount1;
	private int membersCount2;
	private int nonMembersCount1;
	private int nonMembersCount2;
	
	/**
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		memberCountries = RODServices.getDbService().getSpatialDao().getMemberCountries();
		nonMemberCountries = RODServices.getDbService().getSpatialDao().getNonMemberCountries();
		
		int memberSize = memberCountries.size();
		int cnt = memberSize / 3;
		if((memberSize - (cnt * 3)) == 0){
			membersCount1 = cnt;
			membersCount2 = cnt * 2; 
		} else if ((memberSize - (cnt * 3)) == 1){
			membersCount1 = cnt + 1;
			membersCount2 = (cnt * 2) + 1;
		} else if ((memberSize - (cnt * 3)) == 2){
			membersCount1 = cnt + 1;
			membersCount2 = (cnt + 1) * 2;
		}		
		
		int nonMemberSize = nonMemberCountries.size();
		int cnt2 = nonMemberSize / 3;
		if((nonMemberSize - (cnt2 * 3)) == 0){
			nonMembersCount1 = cnt2;
			nonMembersCount2 = cnt2 * 2; 
		} else if ((nonMemberSize - (cnt2 * 3)) == 1){
			nonMembersCount1 = cnt2 + 1;
			nonMembersCount2 = (cnt2 * 2) + 1;
		} else if ((nonMemberSize - (cnt2 * 3)) == 2){
			nonMembersCount1 = cnt2 + 1;
			nonMembersCount2 = (cnt2 + 1) * 2;
		}		
				
		return new ForwardResolution("/pages/deadlines.jsp");
	}

	public List<CountryDTO> getNonMemberCountries() {
		return nonMemberCountries;
	}

	public void setNonMemberCountries(List<CountryDTO> nonMemberCountries) {
		this.nonMemberCountries = nonMemberCountries;
	}

	public List<CountryDTO> getMemberCountries() {
		return memberCountries;
	}

	public void setMemberCountries(List<CountryDTO> memberCountries) {
		this.memberCountries = memberCountries;
	}

	public int getMembersCount1() {
		return membersCount1;
	}

	public void setMembersCount1(int membersCount1) {
		this.membersCount1 = membersCount1;
	}

	public int getMembersCount2() {
		return membersCount2;
	}

	public void setMembersCount2(int membersCount2) {
		this.membersCount2 = membersCount2;
	}

	public int getNonMembersCount1() {
		return nonMembersCount1;
	}

	public void setNonMembersCount1(int nonMembersCount1) {
		this.nonMembersCount1 = nonMembersCount1;
	}

	public int getNonMembersCount2() {
		return nonMembersCount2;
	}

	public void setNonMembersCount2(int nonMembersCount2) {
		this.nonMembersCount2 = nonMembersCount2;
	}

}
