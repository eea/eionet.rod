package eionet.rod.web.util;

import org.displaytag.decorator.TableDecorator;
import eionet.rod.RODUtil;
import eionet.rod.dto.CSDeliveryDTO;
import eionet.rod.dto.CSSearchDTO;

/**
 * 
 * @author altnyris
 *
 */
public class CSDeliveriesTableDecorator extends TableDecorator{
	
	/**
	 * 
	 * @return
	 */
	public String getContact(){
		
		StringBuilder ret = new StringBuilder();
		ret.append("");
		CSDeliveryDTO delivery = (CSDeliveryDTO) getCurrentRowObject();
		if(!RODUtil.isNullOrEmpty(delivery.getObligationRespRole())){
			if(RODUtil.isNullOrEmpty(delivery.getRoleName())){
				if(delivery.getSpatialIsMember().equals("Y")){
					String rolemc = delivery.getObligationRespRole() + "-mc-" + delivery.getSpatialTwoLetter();
					ret.append("<div title='").append(rolemc).append("'>");
					ret.append(RODUtil.threeDots(rolemc, 40));
					ret.append("</div>");
				} else {
					String rolecc = delivery.getObligationRespRole() + "-cc-" + delivery.getSpatialTwoLetter();
					ret.append("<div title='").append(rolecc).append("'>");
					ret.append(RODUtil.threeDots(rolecc, 40));
					ret.append("</div>");
				}
			} else {
				ret.append("<a href='responsible.jsp?role=").append(delivery.getObligationRespRole()).append("&amp;spatial=");
				ret.append(delivery.getSpatialTwoLetter()).append("&amp;member=").append(delivery.getSpatialIsMember()).append("'>");
				ret.append(RODUtil.threeDots(delivery.getRoleName(), 15));
				ret.append("</a>");
			}
		}
		
		return ret.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTitle(){
		
		StringBuilder ret = new StringBuilder();
		CSDeliveryDTO delivery = (CSDeliveryDTO) getCurrentRowObject();
		ret.append("<a href='").append(delivery.getDeliveryUrl()).append("'>");
		ret.append(delivery.getDeliveryTitle());
		ret.append("</a>");
		
		return ret.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDate(){
		
		StringBuilder ret = new StringBuilder();
		CSDeliveryDTO delivery = (CSDeliveryDTO) getCurrentRowObject();
		if(!delivery.getDeliveryUploadDate().equals("0000-00-00"))
			ret.append(delivery.getDeliveryUploadDate());
		else
			ret.append("&lt;No date&gt;");
		
		return ret.toString();
	}

}
