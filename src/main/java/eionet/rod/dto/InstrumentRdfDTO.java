package eionet.rod.dto;

import java.util.List;

/**
 *
 * @author altnyris
 *
 */
public class InstrumentRdfDTO implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    //Fields from T_SOURCE table
    private int sourceId;
    private String sourceCode;
    private String sourceCelexRef;
    private String sourceTitle;
    private String sourceLegalName;
    private String sourceAlias;
    private String sourceUrl;
    private String sourceAbstract;
    private String sourceValidFrom;
    private String sourceComment;
    private String sourceLastUpdate;
    private String sourceNextUpdate;
    private String sourceVerified;
    private String sourceVerifiedBy;
    private boolean sourceIsDraft;
    private String sourceEcAccession;
    private String sourceEcEntryIntoForce;
    private String sourceLastModified;
    private String sourceValidatedBy;
    private String sourceGeographicScope;
    private String sourceIssuedBy;

    //Fields from T_CLIENT table
    private int clientId;
    private String clientName;

    private List<InstrumentParentDTO> parents;
    private List<InstrumentDTO> relatedInstruments;
    private List<InstrumentObligationDTO> obligations;





    /**
     *
     */
    public InstrumentRdfDTO() {
    }


    public int getSourceId() {
        return sourceId;
    }


    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }


    public String getSourceCode() {
        return sourceCode;
    }


    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }


    public String getSourceCelexRef() {
        return sourceCelexRef;
    }


    public void setSourceCelexRef(String sourceCelexRef) {
        this.sourceCelexRef = sourceCelexRef;
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


    public String getSourceLegalName() {
        return sourceLegalName;
    }


    public void setSourceLegalName(String sourceLegalName) {
        this.sourceLegalName = sourceLegalName;
    }


    public String getSourceAbstract() {
        return sourceAbstract;
    }


    public void setSourceAbstract(String sourceAbstract) {
        this.sourceAbstract = sourceAbstract;
    }


    public String getSourceValidFrom() {
        return sourceValidFrom;
    }


    public void setSourceValidFrom(String sourceValidFrom) {
        this.sourceValidFrom = sourceValidFrom;
    }


    public String getSourceComment() {
        return sourceComment;
    }


    public void setSourceComment(String sourceComment) {
        this.sourceComment = sourceComment;
    }


    public int getClientId() {
        return clientId;
    }


    public void setClientId(int clientId) {
        this.clientId = clientId;
    }


    public String getClientName() {
        return clientName;
    }


    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public List<InstrumentParentDTO> getParents() {
        return parents;
    }


    public void setParents(List<InstrumentParentDTO> parents) {
        this.parents = parents;
    }


    public List<InstrumentDTO> getRelatedInstruments() {
        return relatedInstruments;
    }


    public void setRelatedInstruments(List<InstrumentDTO> relatedInstruments) {
        this.relatedInstruments = relatedInstruments;
    }


    public List<InstrumentObligationDTO> getObligations() {
        return obligations;
    }


    public void setObligations(List<InstrumentObligationDTO> obligations) {
        this.obligations = obligations;
    }


    public String getSourceLastUpdate() {
        return sourceLastUpdate;
    }


    public void setSourceLastUpdate(String sourceLastUpdate) {
        this.sourceLastUpdate = sourceLastUpdate;
    }


    public String getSourceNextUpdate() {
        return sourceNextUpdate;
    }


    public void setSourceNextUpdate(String sourceNextUpdate) {
        this.sourceNextUpdate = sourceNextUpdate;
    }


    public String getSourceVerified() {
        return sourceVerified;
    }


    public void setSourceVerified(String sourceVerified) {
        this.sourceVerified = sourceVerified;
    }


    public String getSourceVerifiedBy() {
        return sourceVerifiedBy;
    }


    public void setSourceVerifiedBy(String sourceVerifiedBy) {
        this.sourceVerifiedBy = sourceVerifiedBy;
    }


    public boolean isSourceIsDraft() {
        return sourceIsDraft;
    }


    public void setSourceIsDraft(boolean sourceIsDraft) {
        this.sourceIsDraft = sourceIsDraft;
    }


    public String getSourceEcAccession() {
        return sourceEcAccession;
    }


    public void setSourceEcAccession(String sourceEcAccession) {
        this.sourceEcAccession = sourceEcAccession;
    }


    public String getSourceEcEntryIntoForce() {
        return sourceEcEntryIntoForce;
    }


    public void setSourceEcEntryIntoForce(String sourceEcEntryIntoForce) {
        this.sourceEcEntryIntoForce = sourceEcEntryIntoForce;
    }

    public String getSourceValidatedBy() {
        return sourceValidatedBy;
    }


    public void setSourceValidatedBy(String sourceValidatedBy) {
        this.sourceValidatedBy = sourceValidatedBy;
    }


    public String getSourceLastModified() {
        return sourceLastModified;
    }


    public void setSourceLastModified(String sourceLastModified) {
        this.sourceLastModified = sourceLastModified;
    }


    public String getSourceGeographicScope() {
        return sourceGeographicScope;
    }


    public void setSourceGeographicScope(String sourceGeographicScope) {
        this.sourceGeographicScope = sourceGeographicScope;
    }


    public String getSourceIssuedBy() {
        return sourceIssuedBy;
    }


    public void setSourceIssuedBy(String sourceIssuedBy) {
        this.sourceIssuedBy = sourceIssuedBy;
    }


}
