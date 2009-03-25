package eionet.rod.dto;

/**
 * 
 * @author altnyris
 *
 */
public class DeliveryDTO implements java.io.Serializable{
	
	private String deliveryId;
	private String title;
	private String url;
	
	
	/**
	 * 
	 */
	public DeliveryDTO(){
	}


	public String getDeliveryId() {
		return deliveryId;
	}


	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


}
