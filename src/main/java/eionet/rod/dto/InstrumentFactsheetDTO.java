package eionet.rod.dto;

import java.util.List;

/**
 *
 * @author altnyris
 *
 */
public class InstrumentFactsheetDTO implements java.io.Serializable {

    //Fields from T_SOURCE table
    private int sourceId;
    private String sourceCode;
    private String sourceCelexRef;
    private String sourceTitle;
    private String sourceLegalName;
    private String sourceAlias;
    private String sourceUrl;
    private String sourceIssuedByUrl;
    private String sourceIssuedByUrlLabel;
    private String sourceSecretariat;
    private String sourceSecretariatUrl;
    private String sourceAbstract;
    private String sourceValidFrom;
    private String sourceEcAccession;
    private String sourceEcEntryIntoForce;
    private String sourceComment;
    private String sourceLastUpdate;
    private String sourceNextUpdate;
    private String sourceVerified;
    private String sourceVerifiedBy;
    private String sourceValidatedBy;
    private String sourceGeographicScope;
    private String sourceDgenvReview;
    private String sourceDraft;
    private String sourceFKClientId;

    //Fields from T_CLIENT_LNK table
    private int clientLnkFKClientId;
    private int clientLnkFKObjectId;
    private String clientLnkStatus;
    private String clientLnkType;

    //Fields from T_CLIENT table
    private int clientId;
    private String clientName;

    private List<InstrumentParentDTO> parents;
    private List<InstrumentDTO> relatedInstruments;
    private InstrumentDTO origin;
    private List<InstrumentObligationDTO> obligations;





    /**
     *
     */
    public InstrumentFactsheetDTO() {
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


    public String getSourceIssuedByUrl() {
        return sourceIssuedByUrl;
    }


    public void setSourceIssuedByUrl(String sourceIssuedByUrl) {
        this.sourceIssuedByUrl = sourceIssuedByUrl;
    }


    public String getSourceIssuedByUrlLabel() {
        return sourceIssuedByUrlLabel;
    }


    public void setSourceIssuedByUrlLabel(String sourceIssuedByUrlLabel) {
        this.sourceIssuedByUrlLabel = sourceIssuedByUrlLabel;
    }


    public String getSourceSecretariat() {
        return sourceSecretariat;
    }


    public void setSourceSecretariat(String sourceSecretariat) {
        this.sourceSecretariat = sourceSecretariat;
    }


    public String getSourceSecretariatUrl() {
        return sourceSecretariatUrl;
    }


    public void setSourceSecretariatUrl(String sourceSecretariatUrl) {
        this.sourceSecretariatUrl = sourceSecretariatUrl;
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


    public String getSourceComment() {
        return sourceComment;
    }


    public void setSourceComment(String sourceComment) {
        this.sourceComment = sourceComment;
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


    public String getSourceValidatedBy() {
        return sourceValidatedBy;
    }


    public void setSourceValidatedBy(String sourceValidatedBy) {
        this.sourceValidatedBy = sourceValidatedBy;
    }


    public String getSourceGeographicScope() {
        return sourceGeographicScope;
    }


    public void setSourceGeographicScope(String sourceGeographicScope) {
        this.sourceGeographicScope = sourceGeographicScope;
    }


    public String getSourceDgenvReview() {
        return sourceDgenvReview;
    }


    public void setSourceDgenvReview(String sourceDgenvReview) {
        this.sourceDgenvReview = sourceDgenvReview;
    }


    public int getClientLnkFKClientId() {
        return clientLnkFKClientId;
    }


    public void setClientLnkFKClientId(int clientLnkFKClientId) {
        this.clientLnkFKClientId = clientLnkFKClientId;
    }


    public int getClientLnkFKObjectId() {
        return clientLnkFKObjectId;
    }


    public void setClientLnkFKObjectId(int clientLnkFKObjectId) {
        this.clientLnkFKObjectId = clientLnkFKObjectId;
    }


    public String getClientLnkStatus() {
        return clientLnkStatus;
    }


    public void setClientLnkStatus(String clientLnkStatus) {
        this.clientLnkStatus = clientLnkStatus;
    }


    public String getClientLnkType() {
        return clientLnkType;
    }


    public void setClientLnkType(String clientLnkType) {
        this.clientLnkType = clientLnkType;
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


    public InstrumentDTO getOrigin() {
        return origin;
    }


    public void setOrigin(InstrumentDTO origin) {
        this.origin = origin;
    }


    public String getSourceLegalName() {
        return sourceLegalName;
    }


    public void setSourceLegalName(String sourceLegalName) {
        this.sourceLegalName = sourceLegalName;
    }


    public String getSourceDraft() {
        return sourceDraft;
    }


    public void setSourceDraft(String sourceDraft) {
        this.sourceDraft = sourceDraft;
    }


    public String getSourceFKClientId() {
        return sourceFKClientId;
    }


    public void setSourceFKClientId(String sourceFKClientId) {
        this.sourceFKClientId = sourceFKClientId;
    }


}
