package eionet.rod.dto;

/**
 * 
 * @author altnyris
 *
 */
public class ObligationRdfDTO implements java.io.Serializable{
	
	private int obligationId;
	private int sourceId;
	private String title;
	private String sourceTitle;
	private String detailsUrl;
	private String uri;
	private String terminated;
	private String validSince;
	private String eeaPrimary;
	private String responsibleRole;
	private String description;
	private String nextDeadline;
	private String nextDeadline2;
	private String comment;
	private String reportingFormat;
	private String formatName;
	private String reportFormatUrl;
	private int clientId;
	private String dataUsedForUrl;
	
	/**
	 * 
	 */
	public ObligationRdfDTO(){
	}

	public int getObligationId() {
		return obligationId;
	}

	public void setObligationId(int obligationId) {
		this.obligationId = obligationId;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSourceTitle() {
		return sourceTitle;
	}

	public void setSourceTitle(String sourceTitle) {
		this.sourceTitle = sourceTitle;
	}

	public String getDetailsUrl() {
		return detailsUrl;
	}

	public void setDetailsUrl(String detailsUrl) {
		this.detailsUrl = detailsUrl;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getTerminated() {
		return terminated;
	}

	public void setTerminated(String terminated) {
		this.terminated = terminated;
	}

	public String getValidSince() {
		return validSince;
	}

	public void setValidSince(String validSince) {
		this.validSince = validSince;
	}

	public String getResponsibleRole() {
		return responsibleRole;
	}

	public void setResponsibleRole(String responsibleRole) {
		this.responsibleRole = responsibleRole;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNextDeadline() {
		return nextDeadline;
	}

	public void setNextDeadline(String nextDeadline) {
		this.nextDeadline = nextDeadline;
	}

	public String getNextDeadline2() {
		return nextDeadline2;
	}

	public void setNextDeadline2(String nextDeadline2) {
		this.nextDeadline2 = nextDeadline2;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getReportingFormat() {
		return reportingFormat;
	}

	public void setReportingFormat(String reportingFormat) {
		this.reportingFormat = reportingFormat;
	}

	public String getFormatName() {
		return formatName;
	}

	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}

	public String getReportFormatUrl() {
		return reportFormatUrl;
	}

	public void setReportFormatUrl(String reportFormatUrl) {
		this.reportFormatUrl = reportFormatUrl;
	}

	public String getEeaPrimary() {
		return eeaPrimary;
	}

	public void setEeaPrimary(String eeaPrimary) {
		this.eeaPrimary = eeaPrimary;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getDataUsedForUrl() {
		return dataUsedForUrl;
	}

	public void setDataUsedForUrl(String dataUsedForUrl) {
		this.dataUsedForUrl = dataUsedForUrl;
	}

	
}
