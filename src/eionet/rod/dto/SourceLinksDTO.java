package eionet.rod.dto;

/**
 * 
 * @author altnyris
 *
 */
public class SourceLinksDTO implements java.io.Serializable{
	
	private int childId;
	private int parentId;
	
	/**
	 * 
	 */
	public SourceLinksDTO(){
	}

	public int getChildId() {
		return childId;
	}

	public void setChildId(int childId) {
		this.childId = childId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

		
}
