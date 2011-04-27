package eionet.rod.dto;

import java.util.List;

/**
 *
 * @author altnyris
 *
 */
public class InstrumentRdfDTO implements java.io.Serializable{

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


}
