package eionet.rod.dto;

/**
 * 
 * @author altnyris
 *
 */
public class HierarchyInstrumentDTO implements java.io.Serializable{
	
	//Fields from T_SOURCE table
	private int sourceId;
	private String sourceTitle;
	private String sourceAlias;
	private String sourceUrl;
	
	private int parentSourceId;
	private String parentSourceTitle;
	private String parentSourceAlias;
	private String parentSourceUrl;
		
	
	/**
	 * 
	 */
	public HierarchyInstrumentDTO(){
	}


	public int getSourceId() {
		return sourceId;
	}


	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}


	public String getSourceTitle() {
		return sourceTitle;
	}


	public void setSourceTitle(String sourceTitle) {
		this.sourceTitle = sourceTitle;
	}


	public String getSourceAlias() {
		return sourceAlias;
	}


	public void setSourceAlias(String sourceAlias) {
		this.sourceAlias = sourceAlias;
	}


	public String getSourceUrl() {
		return sourceUrl;
	}


	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}


	public int getParentSourceId() {
		return parentSourceId;
	}


	public void setParentSourceId(int parentSourceId) {
		this.parentSourceId = parentSourceId;
	}


	public String getParentSourceTitle() {
		return parentSourceTitle;
	}


	public void setParentSourceTitle(String parentSourceTitle) {
		this.parentSourceTitle = parentSourceTitle;
	}


	public String getParentSourceAlias() {
		return parentSourceAlias;
	}


	public void setParentSourceAlias(String parentSourceAlias) {
		this.parentSourceAlias = parentSourceAlias;
	}


	public String getParentSourceUrl() {
		return parentSourceUrl;
	}


	public void setParentSourceUrl(String parentSourceUrl) {
		this.parentSourceUrl = parentSourceUrl;
	}



		
}
