package eionet.rod.dto;

/**
 * 
 * @author altnyris
 *
 */
public class DDParamDTO implements java.io.Serializable{
	
	private String elementName;
	private String elementUrl;
	private String tableName;
	private String datasetName;
	
	/**
	 * 
	 */
	public DDParamDTO(){
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getElementUrl() {
		return elementUrl;
	}

	public void setElementUrl(String elementUrl) {
		this.elementUrl = elementUrl;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	
}
