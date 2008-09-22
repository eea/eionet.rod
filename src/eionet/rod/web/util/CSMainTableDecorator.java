package eionet.rod.web.util;

import org.displaytag.decorator.TableDecorator;
import eionet.rod.RODUtil;
import eionet.rod.dto.CSSearchDTO;

/**
 * 
 * @author altnyris
 *
 */
public class CSMainTableDecorator extends TableDecorator{
	
	/**
	 * 
	 * @return
	 */
	public String getTitle(){
		
		StringBuilder ret = new StringBuilder();
		CSSearchDTO search = (CSSearchDTO) getCurrentRowObject();
		ret.append("<a href='show.jsv?id=").append(search.getObligationId()).append("&amp;mode=A'>");
		ret.append(RODUtil.threeDots(search.getObligationTitle(), 40));
		ret.append("</a>");
		if(!RODUtil.isNullOrEmpty(search.getSourceCode())){
			ret.append("<br/><a href='show.jsv?id=").append(search.getSourceId()).append("&amp;mode=S'>");
			ret.append(search.getSourceCode());
			ret.append("</a>");
		}
		
		return ret.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getClient(){
		
		StringBuilder ret = new StringBuilder();
		CSSearchDTO search = (CSSearchDTO) getCurrentRowObject();
		ret.append("<a href='client.jsv?id=").append(search.getClientId()).append("' title='");
		ret.append(search.getClientName()).append("'>");
		ret.append(RODUtil.threeDots(search.getClientDescr(), 20));
		ret.append("</a>");
		
		return ret.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDeadline(){
		
		StringBuilder ret = new StringBuilder();
		CSSearchDTO search = (CSSearchDTO) getCurrentRowObject();
		String nextDeadline = search.getObligationNextDeadline();
		String nextReporting = search.getObligationNextReporting();
		if(RODUtil.isNullOrEmpty(nextDeadline)){
			ret.append("<div title='").append(nextReporting).append("' style='color:#006666'>");
			ret.append(RODUtil.threeDots(nextReporting, 10));
			ret.append("</div>");
		} else {
			ret.append("<div title='").append(nextDeadline).append("' style='color:#000000'>");
			ret.append(RODUtil.threeDots(nextDeadline, 10));
			ret.append("</div>");
		}
	
		return ret.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRole(){
		
		StringBuilder ret = new StringBuilder();
		ret.append("");
		CSSearchDTO search = (CSSearchDTO) getCurrentRowObject();
		if(!RODUtil.isNullOrEmpty(search.getObligationRespRole())){
			if(RODUtil.isNullOrEmpty(search.getRoleDescr())){
				if(search.getSpatialIsMember().equals("Y")){
					String rolemc = search.getObligationRespRole() + "-mc-" + search.getSpatialTwoLetter();
					ret.append("<div title='").append(rolemc).append("'>");
					ret.append(RODUtil.threeDots(rolemc, 35));
					ret.append("</div>");
				} else {
					String rolecc = search.getObligationRespRole() + "-cc-" + search.getSpatialTwoLetter();
					ret.append("<div title='").append(rolecc).append("'>");
					ret.append(RODUtil.threeDots(rolecc, 35));
					ret.append("</div>");
				}
			} else {
				ret.append("<a href='responsible.jsp?role=").append(search.getObligationRespRole()).append("&amp;spatial=");
				ret.append(search.getSpatialTwoLetter()).append("&amp;member=").append(search.getSpatialIsMember()).append("'>");
				ret.append(RODUtil.threeDots(search.getRoleDescr(), 15));
				ret.append("</a>");
			}
		}
		
		return ret.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getHasDelivery(){
		
		StringBuilder ret = new StringBuilder();
		CSSearchDTO search = (CSSearchDTO) getCurrentRowObject();
		if(search.getObligationHasDelivery() == 1){
			ret.append("<a href='csdeliveries?actDetailsId=").append(search.getObligationId());
			ret.append("&amp;spatialId=").append(search.getSpatialId()).append("'>Show list</a>");
		} else {
			ret.append("None");
		}
		
		return ret.toString();
	}

}
