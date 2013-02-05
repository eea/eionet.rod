package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import eionet.rod.RODUtil;
import eionet.rod.dto.HierarchyInstrumentDTO;
import eionet.rod.dto.InstrumentDTO;
import eionet.rod.dto.InstrumentFactsheetDTO;
import eionet.rod.dto.InstrumentObligationDTO;
import eionet.rod.dto.InstrumentParentDTO;
import eionet.rod.dto.InstrumentRdfDTO;
import eionet.rod.dto.InstrumentsDueDTO;
import eionet.rod.dto.InstrumentsListDTO;
import eionet.rod.dto.LookupDTO;
import eionet.rod.dto.SourceClassDTO;
import eionet.rod.dto.SourceLinksDTO;
import eionet.rod.dto.UrlDTO;
import eionet.rod.dto.readers.HierarchyInstrumentDTOReader;
import eionet.rod.dto.readers.InstrumentDTOReader;
import eionet.rod.dto.readers.InstrumentsDueDTOReader;
import eionet.rod.dto.readers.LookupDTOReader;
import eionet.rod.dto.readers.SourceClassDTOReader;
import eionet.rod.dto.readers.SourceLinksDTOReader;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.ISourceDao;
import eionet.rod.util.sql.SQLUtil;

public class SourceMySqlDao extends MySqlBaseDao implements ISourceDao {

    public SourceMySqlDao() {
    }

    private static final String q_instruments =
        "SELECT "
        + "s.PK_SOURCE_ID, "
        + "s.SOURCE_CODE, "
        + "s.FK_CLIENT_ID, "
        + "REPLACE(s.TITLE, '&', '&#038;') AS TITLE, "
        + "REPLACE(s.ALIAS, '&', '&#038;') AS ALIAS, "
        + "REPLACE(s.URL, '&', '&#038;') AS URL, "
        + "REPLACE(s.ABSTRACT, '&', '&#038;') AS ABSTRACT, "
        + "REPLACE(c.CLIENT_NAME, '&', '&#038;') AS ISSUED_BY, "
        + "REPLACE(s.LEGAL_NAME, '&', '&#038;') AS LEGAL_NAME, "
        + "REPLACE(s.CELEX_REF, '&', '&#038;') AS CELEX_REF, "
        + "REPLACE(s.TITLE, '&', '&#038;') AS TITLE, "
        + "REPLACE(s.TITLE, '&', '&#038;') AS TITLE, "
        + "CONCAT('" + rodDomain + "/instruments/', PK_SOURCE_ID) AS details_url, "
        + "DATE_FORMAT(s.LAST_UPDATE, '%Y-%m-%d') AS LAST_UPDATE "
        + "FROM T_SOURCE s LEFT OUTER JOIN T_CLIENT c ON s.FK_CLIENT_ID=c.PK_CLIENT_ID "
        + "ORDER BY TITLE ";

    private static final String q_instruments_ids = "SELECT PK_SOURCE_ID FROM T_SOURCE";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISourceDao#getInstruments()
     */
    public Vector<Map<String, String>> getInstruments() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Vector<Map<String, String>> result = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_instruments);
            if (isDebugMode) logQuery(q_instruments);
            result = _getVectorOfHashes(preparedStatement);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result != null ? result : new Vector<Map<String, String>>();

    }

    private static final String q_rdf_instruments_factsheet =
        "SELECT SO.PK_SOURCE_ID, SO.SOURCE_CODE, SO.CELEX_REF, SO.TITLE, SO.LEGAL_NAME, SO.ALIAS, SO.URL, "
        + "SO.ABSTRACT, SO.VALID_FROM, SO.COMMENT, SO.LAST_UPDATE, SO.RM_NEXT_UPDATE, SO.RM_VERIFIED, SO.RM_VERIFIED_BY, "
        + "SO.EC_ACCESSION, SO.EC_ENTRY_INTO_FORCE, SO.LAST_MODIFIED, "
        + "SO.RM_VALIDATED_BY, SO.GEOGRAPHIC_SCOPE, SO.ISSUED_BY, IF(SO.DRAFT='Y','true','false') AS 'isDraft', "
        + "C.PK_CLIENT_ID, C.CLIENT_NAME "
        + "FROM T_SOURCE SO, T_CLIENT_LNK CL, T_CLIENT C "
        + "WHERE CL.TYPE='S' AND CL.STATUS='M' AND CL.FK_OBJECT_ID=SO.PK_SOURCE_ID AND C.PK_CLIENT_ID = CL.FK_CLIENT_ID";

    private static final String q_rdf_instrument_obligations =
        "SELECT OB.PK_RA_ID, OB.TITLE, OB.AUTHORITY, OB.TERMINATE "
        + "FROM T_OBLIGATION OB, T_SOURCE SO "
        + "WHERE SO.PK_SOURCE_ID=? AND SO.PK_SOURCE_ID=OB.FK_SOURCE_ID";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#getInstrumentsForRDF()
     */
    public List<InstrumentRdfDTO> getInstrumentsForRDF() throws ServiceException {

        List<InstrumentRdfDTO> ret = new ArrayList<InstrumentRdfDTO>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        ResultSet subRs = null;

        try {
            connection = getConnection();
            if (isDebugMode) logQuery(q_rdf_instruments_factsheet);
            preparedStatement = connection.prepareStatement(q_rdf_instruments_factsheet);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                InstrumentRdfDTO inst = new InstrumentRdfDTO();
                inst.setSourceId(new Integer(rs.getInt("SO.PK_SOURCE_ID")));
                inst.setSourceTitle(rs.getString("SO.TITLE"));
                inst.setSourceLegalName(rs.getString("SO.LEGAL_NAME"));
                inst.setSourceAlias(rs.getString("SO.ALIAS"));
                inst.setSourceCelexRef(rs.getString("SO.CELEX_REF"));
                inst.setSourceCode(rs.getString("SO.SOURCE_CODE"));
                inst.setSourceUrl(rs.getString("SO.URL"));
                inst.setSourceAbstract(rs.getString("SO.ABSTRACT"));
                inst.setSourceValidFrom(rs.getString("SO.VALID_FROM"));
                inst.setSourceComment(rs.getString("SO.COMMENT"));
                inst.setSourceEcAccession(rs.getString("SO.EC_ACCESSION"));
                inst.setSourceEcEntryIntoForce(rs.getString("SO.EC_ENTRY_INTO_FORCE"));
                inst.setSourceLastModified(rs.getString("SO.LAST_MODIFIED"));
                inst.setSourceValidatedBy(rs.getString("SO.RM_VALIDATED_BY"));
                inst.setSourceGeographicScope(rs.getString("SO.GEOGRAPHIC_SCOPE"));
                inst.setSourceIssuedBy(rs.getString("SO.ISSUED_BY"));
                inst.setSourceIsDraft(rs.getBoolean("isDraft"));

                inst.setClientId(new Integer(rs.getInt("C.PK_CLIENT_ID")));
                inst.setClientName(rs.getString("C.CLIENT_NAME"));

                List<InstrumentObligationDTO> obligations = new ArrayList<InstrumentObligationDTO>();
                preparedStatement = connection.prepareStatement(q_rdf_instrument_obligations);
                preparedStatement.setInt(1, rs.getInt("SO.PK_SOURCE_ID"));
                subRs = preparedStatement.executeQuery();
                while (subRs.next()) {
                    InstrumentObligationDTO obligation = new InstrumentObligationDTO();
                    obligation.setObligationId(new Integer(subRs.getInt("PK_RA_ID")));
                    obligation.setTitle(subRs.getString("TITLE"));
                    obligation.setAuthority(subRs.getString("AUTHORITY"));
                    obligation.setTerminate(subRs.getString("TERMINATE"));
                    obligations.add(obligation);
                }
                inst.setObligations(obligations);

                ret.add(inst);
            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return ret;
    }

    private static final String q_rdf_instrument_factsheet =
        "SELECT SO.PK_SOURCE_ID, SO.SOURCE_CODE, SO.CELEX_REF, SO.TITLE, SO.LEGAL_NAME, SO.ALIAS, SO.URL, "
        + "SO.ABSTRACT, SO.VALID_FROM, SO.COMMENT, SO.LAST_UPDATE, SO.RM_NEXT_UPDATE, SO.RM_VERIFIED, SO.RM_VERIFIED_BY, "
        + "SO.EC_ACCESSION, SO.EC_ENTRY_INTO_FORCE, SO.LAST_MODIFIED, "
        + "SO.RM_VALIDATED_BY, SO.GEOGRAPHIC_SCOPE, SO.ISSUED_BY, IF(SO.DRAFT='Y','true','false') AS 'isDraft', "
        + "C.PK_CLIENT_ID, C.CLIENT_NAME "
        + "FROM T_SOURCE SO, T_CLIENT_LNK CL, T_CLIENT C "
        + "WHERE SO.PK_SOURCE_ID=? AND CL.TYPE='S' AND CL.STATUS='M' AND CL.FK_OBJECT_ID=SO.PK_SOURCE_ID AND C.PK_CLIENT_ID = CL.FK_CLIENT_ID";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#getInstrumentForRDF(String id)
     */
    public InstrumentRdfDTO getInstrumentForRDF(String id) throws ServiceException {

        InstrumentRdfDTO ret = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        ResultSet subRs = null;

        List<InstrumentObligationDTO> obligations = new ArrayList<InstrumentObligationDTO>();

        try {
            connection = getConnection();
            if (isDebugMode) logQuery(q_rdf_instrument_factsheet);
            preparedStatement = connection.prepareStatement(q_rdf_instrument_factsheet);
            preparedStatement.setString(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ret = new InstrumentRdfDTO();
                ret.setSourceId(new Integer(rs.getInt("SO.PK_SOURCE_ID")));
                ret.setSourceTitle(rs.getString("SO.TITLE"));
                ret.setSourceLegalName(rs.getString("SO.LEGAL_NAME"));
                ret.setSourceAlias(rs.getString("SO.ALIAS"));
                ret.setSourceCelexRef(rs.getString("SO.CELEX_REF"));
                ret.setSourceCode(rs.getString("SO.SOURCE_CODE"));
                ret.setSourceUrl(rs.getString("SO.URL"));
                ret.setSourceAbstract(rs.getString("SO.ABSTRACT"));
                ret.setSourceValidFrom(rs.getString("SO.VALID_FROM"));
                ret.setSourceComment(rs.getString("SO.COMMENT"));
                ret.setSourceEcAccession(rs.getString("SO.EC_ACCESSION"));
                ret.setSourceEcEntryIntoForce(rs.getString("SO.EC_ENTRY_INTO_FORCE"));
                ret.setSourceLastModified(rs.getString("SO.LAST_MODIFIED"));
                ret.setSourceValidatedBy(rs.getString("SO.RM_VALIDATED_BY"));
                ret.setSourceGeographicScope(rs.getString("SO.GEOGRAPHIC_SCOPE"));
                ret.setSourceIssuedBy(rs.getString("SO.ISSUED_BY"));
                ret.setSourceIsDraft(rs.getBoolean("isDraft"));

                ret.setClientId(new Integer(rs.getInt("C.PK_CLIENT_ID")));
                ret.setClientName(rs.getString("C.CLIENT_NAME"));

                preparedStatement = connection.prepareStatement(q_rdf_instrument_obligations);
                preparedStatement.setInt(1, rs.getInt("SO.PK_SOURCE_ID"));
                subRs = preparedStatement.executeQuery();
                while (subRs.next()) {
                    InstrumentObligationDTO obligation = new InstrumentObligationDTO();
                    obligation.setObligationId(new Integer(subRs.getInt("PK_RA_ID")));
                    obligation.setTitle(subRs.getString("TITLE"));
                    obligation.setAuthority(subRs.getString("AUTHORITY"));
                    obligation.setTerminate(subRs.getString("TERMINATE"));
                    obligations.add(obligation);
                }
                ret.setObligations(obligations);

            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return ret;
    }

    private static final String q_subscribe_instruments =
        "SELECT REPLACE(TITLE, '&', '&#038;') AS TITLE "
        + "FROM T_SOURCE "
        + "ORDER BY TITLE ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISourceDao#getSubscribeInstruments()
     */
    public List<String> getSubscribeInstruments() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<String> result = new ArrayList<String>();

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_subscribe_instruments);
            if (isDebugMode) logQuery(q_subscribe_instruments);
            ResultSet rs = null;
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String title = rs.getString("TITLE");
                result.add(title);
            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result;

    }

    private static final String qInstrumentById =
        "SELECT "
        + "PK_SOURCE_ID AS instrumentID, "
        + "REPLACE(TITLE, '&', '&#038;') AS TITLE "
        + "FROM T_SOURCE WHERE PK_SOURCE_ID=? ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISourceDao#getInstrumentById(java.lang.Integer)
     */
    public Hashtable<String, String> getInstrumentById(Integer id) throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Hashtable<String, String> result = null;
        try {
            connection = getConnection();
            if (isDebugMode) logQuery(qInstrumentById);
            preparedStatement = connection.prepareStatement(qInstrumentById);
            preparedStatement.setInt(1, id.intValue());
            result = _getHashtable(preparedStatement);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result != null ? result : new Hashtable<String, String>();
    }

    private static final String q_instruments_rss =
        "SELECT "
        + "PK_SOURCE_ID, "
        + "REPLACE(TITLE, '&', '&#038;') AS TITLE, "
        + "CONCAT('" + rodDomain + "/instruments/', PK_SOURCE_ID) AS LINK, "
        + "REPLACE(COMMENT, '&', '&#038;') AS COMMENT "
        + "FROM T_SOURCE "
        + "ORDER BY PK_SOURCE_ID ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISourceDao#getInstrumentsRSS()
     */
    public String[][] getInstrumentsRSS() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String[][] result = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_instruments_rss);
            if (isDebugMode) logQuery(q_instruments_rss);
            result = _executeStringQuery(preparedStatement);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result != null ? result : new String[][] {};

    }

    private static final String q_delete_child_link =
        "DELETE FROM T_SOURCE_LNK "
        + "WHERE CHILD_TYPE='S' AND FK_SOURCE_CHILD_ID=?";

    public void deleteChildLink(Integer childId) throws ServiceException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            if (isDebugMode) logQuery(q_delete_child_link);
            preparedStatement = connection.prepareStatement(q_delete_child_link);
            preparedStatement.setInt(1, childId.intValue());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }
    }


    private static final String q_delete_parent_link =
        "DELETE FROM T_SOURCE_LNK "
        + "WHERE PARENT_TYPE='S' AND FK_SOURCE_PARENT_ID=?";


    public void deleteParentLink(Integer parentId) throws ServiceException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            if (isDebugMode) logQuery(q_delete_parent_link);
            preparedStatement = connection.prepareStatement(q_delete_parent_link);
            preparedStatement.setInt(1, parentId.intValue());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }
    }


    private static final String q_delete_source =
        "DELETE FROM T_SOURCE WHERE PK_SOURCE_ID=?";

    public void deleteSource(Integer sourceId) throws ServiceException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            if (isDebugMode) logQuery(q_delete_source);
            preparedStatement = connection.prepareStatement(q_delete_source);
            preparedStatement.setInt(1, sourceId.intValue());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

    }

    public String[][] getInstrumentIds() throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String[][] result = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_instruments_ids);
            if (isDebugMode) logQuery(q_instruments_ids);
            result = _executeStringQuery(preparedStatement);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result != null ? result : new String[][] {};
    }

    private static final String qDGEnv =
        "SELECT "
        + "C_TERM AS name "
        + "FROM T_LOOKUP WHERE C_VALUE=? AND CATEGORY = 'DGS' ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISourceDao#getDGEnvName(java.lang.String)
     */
    public String getDGEnvName(String value) throws ServiceException {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        String[][] result = null;
        String res = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(qDGEnv);
            preparedStatement.setString(1, value);
            if (isDebugMode) logQuery(qDGEnv);
            resultSet = preparedStatement.executeQuery();
            result = getResults(resultSet);
            resultSet.close();
            preparedStatement.close();
            if (result.length > 0) {
                res = result[0][0];
            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(resultSet, preparedStatement, connection);
        }

        return res != null ? res : "";
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#getInstrumentsDue()
     */
    public List<InstrumentsDueDTO> getInstrumentsDue() throws ServiceException {

        String query = "SELECT PK_SOURCE_ID, TITLE, RM_NEXT_UPDATE, RM_VERIFIED, RM_VERIFIED_BY "
        + "FROM T_SOURCE ORDER BY RM_NEXT_UPDATE";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        InstrumentsDueDTOReader rsReader = new InstrumentsDueDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<InstrumentsDueDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    private static final String q_instrument_factsheet =
        "SELECT SO.PK_SOURCE_ID, SO.SOURCE_CODE, SO.CELEX_REF, SO.TITLE, SO.LEGAL_NAME, SO.ALIAS, SO.URL, SO.FK_CLIENT_ID, "
        + "SO.ISSUED_BY_URL, CONCAT(LEFT(SO.ISSUED_BY_URL, 40), IF( LENGTH(SO.ISSUED_BY_URL) > 40 ,'...', '')) AS ISSUED_BY_URL_LABEL, "
        + "SO.SECRETARIAT, SO.SECRETARIAT_URL, SO.ABSTRACT, IF(SO.VALID_FROM, DATE_FORMAT(SO.VALID_FROM,'%d/%m/%Y'), '') AS VALID_FROM, "
        + "IF(SO.EC_ACCESSION, DATE_FORMAT(SO.EC_ACCESSION,'%d/%m/%Y'), '') AS EC_ACCESSION, "
        + "IF(SO.EC_ENTRY_INTO_FORCE, DATE_FORMAT(SO.EC_ENTRY_INTO_FORCE,'%d/%m/%Y'), '') AS EC_ENTRY_INTO_FORCE, SO.COMMENT, "
        + "DATE_FORMAT(SO.LAST_UPDATE, '%d/%m/%Y') AS LAST_UPDATE, DATE_FORMAT(SO.RM_NEXT_UPDATE, '%d/%m/%Y') AS RM_NEXT_UPDATE, "
        + "DATE_FORMAT(SO.RM_VERIFIED, '%d/%m/%Y') AS RM_VERIFIED, SO.RM_VERIFIED_BY, SO.RM_VALIDATED_BY, SO.RM_VALIDATED_BY, SO.GEOGRAPHIC_SCOPE, SO.DGENV_REVIEW, SO.DRAFT, "
        + "CL.FK_CLIENT_ID, CL.FK_OBJECT_ID, CL.STATUS, CL.TYPE, "
        + "C.PK_CLIENT_ID, C.CLIENT_NAME "
        + "FROM T_SOURCE SO, T_CLIENT_LNK CL, T_CLIENT C "
        + "WHERE SO.PK_SOURCE_ID=? AND CL.TYPE='S' AND CL.STATUS='M' AND CL.FK_OBJECT_ID=SO.PK_SOURCE_ID AND C.PK_CLIENT_ID = CL.FK_CLIENT_ID";

    private static final String q_parent_instruments =
        "SELECT SC.PK_CLASS_ID, SC.CLASSIFICATOR, SC.CLASS_NAME "
        + "FROM T_SOURCE_CLASS SC, T_SOURCE_LNK SL, T_SOURCE SO "
        + "WHERE SO.PK_SOURCE_ID=? AND SO.PK_SOURCE_ID = SL.FK_SOURCE_CHILD_ID AND SL.FK_SOURCE_PARENT_ID = SC.PK_CLASS_ID AND SL.CHILD_TYPE='S' AND SL.PARENT_TYPE='C'";

    private static final String q_related_instruments =
        "SELECT LSOURCE.PK_SOURCE_ID, LSOURCE.TITLE, LSOURCE.ALIAS "
        + "FROM T_SOURCE_LNK SL, T_SOURCE LSOURCE, T_SOURCE SO "
        + "WHERE SO.PK_SOURCE_ID=? AND SO.PK_SOURCE_ID=SL.FK_SOURCE_PARENT_ID AND SL.FK_SOURCE_CHILD_ID=LSOURCE.PK_SOURCE_ID AND SL.CHILD_TYPE='S' AND SL.PARENT_TYPE='S'";

    private static final String q_origin_instruments =
        "SELECT LSOURCE.PK_SOURCE_ID, LSOURCE.TITLE, LSOURCE.ALIAS "
        + "FROM T_SOURCE_LNK SL, T_SOURCE LSOURCE, T_SOURCE SO "
        + "WHERE SO.PK_SOURCE_ID=? AND SO.PK_SOURCE_ID=SL.FK_SOURCE_CHILD_ID AND SL.FK_SOURCE_PARENT_ID=LSOURCE.PK_SOURCE_ID AND SL.CHILD_TYPE='S' AND SL.PARENT_TYPE='S'";

    private static final String q_instrument_obligations =
        "SELECT OB.PK_RA_ID, OB.TITLE, OB.AUTHORITY, OB.TERMINATE "
        + "FROM T_OBLIGATION OB, T_SOURCE SO "
        + "WHERE SO.PK_SOURCE_ID=? AND SO.PK_SOURCE_ID=OB.FK_SOURCE_ID";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#getInstrumentFactsheet(String id)
     */
    public InstrumentFactsheetDTO getInstrumentFactsheet(String id) throws ServiceException {

        InstrumentFactsheetDTO ret = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        ResultSet subRs = null;

        List<InstrumentParentDTO> parents = new ArrayList<InstrumentParentDTO>();
        List<InstrumentDTO> relatedInstruments = new ArrayList<InstrumentDTO>();
        List<InstrumentObligationDTO> obligations = new ArrayList<InstrumentObligationDTO>();

        try {
            connection = getConnection();
            if (isDebugMode) logQuery(q_instrument_factsheet);
            preparedStatement = connection.prepareStatement(q_instrument_factsheet);
            preparedStatement.setString(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ret = new InstrumentFactsheetDTO();
                ret.setSourceId(new Integer(rs.getInt("PK_SOURCE_ID")));
                ret.setSourceTitle(rs.getString("TITLE"));
                ret.setSourceLegalName(rs.getString("LEGAL_NAME"));
                ret.setSourceAlias(rs.getString("ALIAS"));
                ret.setSourceCelexRef(rs.getString("CELEX_REF"));
                ret.setSourceCode(rs.getString("SOURCE_CODE"));
                ret.setSourceUrl(rs.getString("URL"));
                ret.setSourceIssuedByUrl(rs.getString("ISSUED_BY_URL"));
                ret.setSourceIssuedByUrlLabel(rs.getString("ISSUED_BY_URL_LABEL"));
                ret.setSourceSecretariat(rs.getString("SECRETARIAT"));
                ret.setSourceSecretariatUrl(rs.getString("SECRETARIAT_URL"));
                ret.setSourceAbstract(rs.getString("ABSTRACT"));
                ret.setSourceValidFrom(rs.getString("VALID_FROM"));
                ret.setSourceEcAccession(rs.getString("EC_ACCESSION"));
                ret.setSourceEcEntryIntoForce(rs.getString("EC_ENTRY_INTO_FORCE"));
                ret.setSourceComment(rs.getString("COMMENT"));
                ret.setSourceLastUpdate(rs.getString("LAST_UPDATE"));
                ret.setSourceNextUpdate(rs.getString("RM_NEXT_UPDATE"));
                ret.setSourceVerified(rs.getString("RM_VERIFIED"));
                ret.setSourceVerifiedBy(rs.getString("RM_VERIFIED_BY"));
                ret.setSourceValidatedBy(rs.getString("RM_VALIDATED_BY"));
                ret.setSourceGeographicScope(rs.getString("GEOGRAPHIC_SCOPE"));
                ret.setSourceDgenvReview(rs.getString("DGENV_REVIEW"));
                ret.setSourceDraft(rs.getString("DRAFT"));
                ret.setSourceFKClientId(rs.getString("FK_CLIENT_ID"));

                ret.setClientLnkFKClientId(new Integer(rs.getInt("FK_CLIENT_ID")));
                ret.setClientLnkFKObjectId(new Integer(rs.getInt("FK_OBJECT_ID")));
                ret.setClientLnkStatus(rs.getString("STATUS"));
                ret.setClientLnkType(rs.getString("TYPE"));

                ret.setClientId(new Integer(rs.getInt("PK_CLIENT_ID")));
                ret.setClientName(rs.getString("CLIENT_NAME"));

                preparedStatement = connection.prepareStatement(q_parent_instruments);
                preparedStatement.setString(1, id);
                subRs = preparedStatement.executeQuery();
                while (subRs.next()) {
                    InstrumentParentDTO parent = new InstrumentParentDTO();
                    parent.setClassId(new Integer(subRs.getInt("PK_CLASS_ID")));
                    parent.setClassificator(subRs.getString("CLASSIFICATOR"));
                    parent.setClassName(subRs.getString("CLASS_NAME"));
                    parents.add(parent);
                }
                ret.setParents(parents);

                preparedStatement = connection.prepareStatement(q_origin_instruments);
                preparedStatement.setString(1, id);
                subRs = preparedStatement.executeQuery();
                while (subRs.next()) {
                    InstrumentDTO origin = new InstrumentDTO();
                    origin.setSourceId(new Integer(subRs.getInt("PK_SOURCE_ID")));
                    origin.setSourceTitle(subRs.getString("TITLE"));
                    origin.setSourceAlias(subRs.getString("ALIAS"));
                    ret.setOrigin(origin);
                }

                preparedStatement = connection.prepareStatement(q_related_instruments);
                preparedStatement.setString(1, id);
                subRs = preparedStatement.executeQuery();
                while (subRs.next()) {
                    InstrumentDTO instrument = new InstrumentDTO();
                    instrument.setSourceId(new Integer(subRs.getInt("PK_SOURCE_ID")));
                    instrument.setSourceTitle(subRs.getString("TITLE"));
                    instrument.setSourceAlias(subRs.getString("ALIAS"));
                    relatedInstruments.add(instrument);
                }
                ret.setRelatedInstruments(relatedInstruments);

                preparedStatement = connection.prepareStatement(q_instrument_obligations);
                preparedStatement.setString(1, id);
                subRs = preparedStatement.executeQuery();
                while (subRs.next()) {
                    InstrumentObligationDTO obligation = new InstrumentObligationDTO();
                    obligation.setObligationId(new Integer(subRs.getInt("PK_RA_ID")));
                    obligation.setTitle(subRs.getString("TITLE"));
                    obligation.setAuthority(subRs.getString("AUTHORITY"));
                    obligation.setTerminate(subRs.getString("TERMINATE"));
                    obligations.add(obligation);
                }
                ret.setObligations(obligations);

            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return ret;
    }

    private static final String q_instrument_DGEnv =
        "SELECT "
        + "L.C_TERM AS name "
        + "FROM T_LOOKUP L, T_SOURCE S "
        + "WHERE S.PK_SOURCE_ID=? AND S.DGENV_REVIEW=L.C_VALUE AND L.CATEGORY='DGS' ";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISourceDao#getDGEnvNameByInstrumentId(java.lang.String)
     */
    public String getDGEnvNameByInstrumentId(String id) throws ServiceException {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        String[][] result = null;
        String res = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(q_instrument_DGEnv);
            preparedStatement.setString(1, id);
            if (isDebugMode) logQuery(q_instrument_DGEnv);
            resultSet = preparedStatement.executeQuery();
            result = getResults(resultSet);
            resultSet.close();
            preparedStatement.close();
            if (result.length > 0) {
                res = result[0][0];
            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(resultSet, preparedStatement, connection);
        }

        return res != null ? res : "";
    }

    private static final String q_hierarchy_instrument =
        "SELECT SC.PK_CLASS_ID, SC.CLASSIFICATOR, SC.CLASS_NAME, SL.FK_SOURCE_PARENT_ID "
        + "FROM T_SOURCE_CLASS SC, T_SOURCE_LNK SL "
        + "WHERE SC.PK_CLASS_ID=? AND SC.PK_CLASS_ID=SL.FK_SOURCE_CHILD_ID AND SL.CHILD_TYPE='C' AND SL.PARENT_TYPE='C' "
        + "ORDER BY SC.CLASSIFICATOR";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#getHierarchyInstrument(String id)
     */
    public InstrumentsListDTO getHierarchyInstrument(String id) throws ServiceException {

        InstrumentsListDTO ret = new InstrumentsListDTO();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            if (isDebugMode) logQuery(q_hierarchy_instrument);
            preparedStatement = connection.prepareStatement(q_hierarchy_instrument);
            preparedStatement.setString(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ret.setClassId(new Integer(rs.getInt("PK_CLASS_ID")));
                ret.setClassificator(rs.getString("CLASSIFICATOR"));
                ret.setClassName(rs.getString("CLASS_NAME"));
                ret.setParentId(rs.getString("FK_SOURCE_PARENT_ID"));

            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return ret;
    }

    private static final String q_hierarchy =
        "SELECT SC.PK_CLASS_ID, SC.CLASSIFICATOR, SC.CLASS_NAME, SL.FK_SOURCE_PARENT_ID "
        + "FROM T_SOURCE_CLASS SC_PARENT, T_SOURCE_CLASS SC, T_SOURCE_LNK SL "
        + "WHERE SC_PARENT.PK_CLASS_ID=? AND SC_PARENT.PK_CLASS_ID=SL.FK_SOURCE_PARENT_ID AND SL.FK_SOURCE_CHILD_ID=SC.PK_CLASS_ID AND SL.CHILD_TYPE='C' AND SL.PARENT_TYPE='C' "
        + "ORDER BY SC.CLASSIFICATOR";

    private static final String q_hierarchy_cnt =
        "SELECT COUNT(*) "
        + "FROM T_SOURCE_CLASS SC_PARENT, T_SOURCE_CLASS SC, T_SOURCE_LNK SL "
        + "WHERE SC_PARENT.PK_CLASS_ID=? AND SC_PARENT.PK_CLASS_ID=SL.FK_SOURCE_PARENT_ID AND SL.FK_SOURCE_CHILD_ID=SC.PK_CLASS_ID AND SL.CHILD_TYPE='C' AND SL.PARENT_TYPE='C' "
        + "ORDER BY SC.CLASSIFICATOR";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#getHierarchy(String id, boolean hasParent)
     */
    public String getHierarchy(String id, boolean hasParent, String mode) throws ServiceException {

        String newLine = "\n";

        StringBuilder ret = new StringBuilder();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = getConnection();

            preparedStatement = connection.prepareStatement(q_hierarchy_cnt);
            preparedStatement.setString(1, id);
            rs = preparedStatement.executeQuery();
            int cnt = 0;
            while (rs.next())
                cnt = rs.getInt(1);

            if (isDebugMode) logQuery(q_hierarchy);
            preparedStatement = connection.prepareStatement(q_hierarchy);
            preparedStatement.setString(1, id);
            rs = preparedStatement.executeQuery();
            String style = "category";
            if (!hasParent)
                style = "topcategory";

            if (cnt > 0) {
                ret.append("<ul class='").append(style).append("'>").append(newLine);
                while (rs.next()) {
                    String child_id = rs.getString("PK_CLASS_ID");
                    String classificator = rs.getString("CLASSIFICATOR");
                    String className = rs.getString("CLASS_NAME");
                    ret.append("<li>").append(newLine);
                    if (hasParent) {
                        if (!RODUtil.isNullOrEmpty(classificator))
                            ret.append(classificator).append("&#160;").append(newLine);
                    }
                    ret.append("<a href='instruments?id=").append(child_id);
                    if (mode != null && mode.equals("X"))
                        ret.append("&amp;mode=X");
                    ret.append("'>").append(className).append("</a>").append(newLine);
                    ret.append(getHierarchy(child_id, true, mode));
                    ret.append("</li>").append(newLine);
                }
                ret.append("</ul>").append(newLine);
            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return ret.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#getHierarchyInstruments(String id)
     */
    public List<HierarchyInstrumentDTO> getHierarchyInstruments(String id) throws ServiceException {

        String query =
            "SELECT S.PK_SOURCE_ID, S.TITLE, S.ALIAS, S.URL, "
            + "PS.PK_SOURCE_ID AS PARENT_ID, PS.TITLE AS PARENT_TITLE, PS.ALIAS AS PARENT_ALIAS, PS.URL AS PARENT_URL "
            + "FROM T_SOURCE_CLASS SC JOIN T_SOURCE_LNK SL ON SC.PK_CLASS_ID=SL.FK_SOURCE_PARENT_ID "
            + "JOIN T_SOURCE S ON SL.FK_SOURCE_CHILD_ID=S.PK_SOURCE_ID "
            + "LEFT OUTER JOIN T_SOURCE_LNK PSL ON PSL.FK_SOURCE_CHILD_ID=S.PK_SOURCE_ID AND PSL.CHILD_TYPE='S' AND PSL.PARENT_TYPE='S' "
            + "LEFT OUTER JOIN T_SOURCE PS ON PS.PK_SOURCE_ID=PSL.FK_SOURCE_PARENT_ID "
            + "WHERE SL.CHILD_TYPE='S' AND SL.PARENT_TYPE='C' AND SC.PK_CLASS_ID=" +id+ " ORDER BY S.TITLE";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        HierarchyInstrumentDTOReader rsReader = new HierarchyInstrumentDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<HierarchyInstrumentDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#getLookupList(String category)
     */
    public List<LookupDTO> getLookupList(String category) throws ServiceException {

        String query = "SELECT C_TERM, C_VALUE "
        + "FROM T_LOOKUP "
        + "WHERE CATEGORY='" + category + "' ORDER BY C_TERM";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        LookupDTOReader rsReader = new LookupDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<LookupDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#getParentInstrumentsList(String id)
     */
    public List<InstrumentDTO> getParentInstrumentsList(String id) throws ServiceException {

        String query = "SELECT PK_SOURCE_ID, CONCAT(IF(ALIAS != '', CONCAT(ALIAS, ' - '), ''),  TITLE) AS TITLE "
        + "FROM T_SOURCE WHERE PK_SOURCE_ID != " + id + " ORDER BY TITLE";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        InstrumentDTOReader rsReader = new InstrumentDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<InstrumentDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    private static final String qParentInstrumentId =
        "SELECT FK_SOURCE_PARENT_ID "
        + "FROM T_SOURCE_LNK WHERE FK_SOURCE_CHILD_ID=? AND CHILD_TYPE = 'S' AND PARENT_TYPE = 'S'";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.ISourceDao#getParentInstrumentId(String childId)
     */
    public String getParentInstrumentId(String childId) throws ServiceException {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        String[][] result = null;
        String res = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(qParentInstrumentId);
            preparedStatement.setString(1, childId);
            if (isDebugMode) logQuery(qParentInstrumentId);
            resultSet = preparedStatement.executeQuery();
            result = getResults(resultSet);
            resultSet.close();
            preparedStatement.close();
            if (result.length > 0) {
                res = result[0][0];
            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(resultSet, preparedStatement, connection);
        }

        return res != null ? res : "";
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#getAllSourceClasses()
     */
    public List<SourceClassDTO> getAllSourceClasses() throws ServiceException {

        String query = "SELECT PK_CLASS_ID, CLASSIFICATOR, CLASS_NAME "
        + "FROM T_SOURCE_CLASS WHERE CLASS_NAME != '' ORDER BY CLASSIFICATOR";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        SourceClassDTOReader rsReader = new SourceClassDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<SourceClassDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#getSourceClassesByInstrumentId(String id)
     */
    public List<SourceClassDTO> getSourceClassesByInstrumentId(String id) throws ServiceException {

        String query = "SELECT T_SOURCE_CLASS.PK_CLASS_ID, T_SOURCE_CLASS.CLASSIFICATOR, T_SOURCE_CLASS.CLASS_NAME, "
        + "T_SOURCE_LNK.FK_SOURCE_PARENT_ID "
        + "FROM T_SOURCE_LNK, T_SOURCE_CLASS WHERE T_SOURCE_LNK.FK_SOURCE_CHILD_ID = " +id+ " "
        + "AND T_SOURCE_LNK.FK_SOURCE_PARENT_ID=T_SOURCE_CLASS.PK_CLASS_ID "
        + "AND T_SOURCE_LNK.PARENT_TYPE='C' AND T_SOURCE_LNK.CHILD_TYPE='S' "
        + "ORDER BY CLASSIFICATOR";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        SourceClassDTOReader rsReader = new SourceClassDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<SourceClassDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#getInstrumentSourceClassesList(List<String> scIds)
     */
    public List<SourceClassDTO> getInstrumentSourceClassesList(List<String> scIds) throws ServiceException {

        StringBuilder ids = new StringBuilder();
        if (scIds != null) {
            for (Iterator<String> it = scIds.iterator(); it.hasNext(); ) {
                String id = it.next();
                ids.append(id);
                if (it.hasNext())
                    ids.append(",");
            }
        } else {
            return null;
        }

        List<Object> values = new ArrayList<Object>();

        String qObligationIssuesList =
            "SELECT PK_CLASS_ID, CLASSIFICATOR, CLASS_NAME "
            + "FROM T_SOURCE_CLASS "
            + "WHERE PK_CLASS_ID IN (" + ids.toString() + ") "
            + "ORDER BY CLASSIFICATOR ";

        Connection conn = null;
        SourceClassDTOReader rsReader = new SourceClassDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(qObligationIssuesList, values, rsReader, conn);
            List<SourceClassDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }

    }

    /** */
    private static final String editSourceSQL = "UPDATE T_SOURCE SET "
    + "TITLE=?, ALIAS=?, SOURCE_CODE=?, DRAFT=?, URL=?, CELEX_REF=?, "
    + "FK_CLIENT_ID=?, ISSUED_BY_URL=?, DGENV_REVIEW=?, VALID_FROM=?, GEOGRAPHIC_SCOPE=?, ABSTRACT=?, "
    + "COMMENT=?, EC_ENTRY_INTO_FORCE=?, EC_ACCESSION=?, SECRETARIAT=?, SECRETARIAT_URL=?, RM_VERIFIED=?, "
    + "RM_VERIFIED_BY=?, RM_NEXT_UPDATE=?, RM_VALIDATED_BY=?, LAST_UPDATE=CURDATE() "
    + "WHERE PK_SOURCE_ID=?";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#editInstrument(InstrumentFactsheetDTO instrument)
     */
    public void editInstrument(InstrumentFactsheetDTO instrument) throws ServiceException {

        List<Object> values = new ArrayList<Object>();
        values.add(instrument.getSourceTitle());
        values.add(instrument.getSourceAlias());
        values.add(instrument.getSourceCode());
        values.add(instrument.getSourceDraft());
        values.add(instrument.getSourceUrl());
        values.add(instrument.getSourceCelexRef());
        values.add(instrument.getSourceFKClientId());
        values.add(instrument.getSourceIssuedByUrl());
        values.add(instrument.getSourceDgenvReview());
        values.add(RODUtil.str2Date(instrument.getSourceValidFrom()));
        values.add(instrument.getSourceGeographicScope());
        values.add(instrument.getSourceAbstract());
        values.add(instrument.getSourceComment());
        values.add(RODUtil.str2Date(instrument.getSourceEcEntryIntoForce()));
        values.add(RODUtil.str2Date(instrument.getSourceEcAccession()));
        values.add(instrument.getSourceSecretariat());
        values.add(instrument.getSourceSecretariatUrl());
        values.add(RODUtil.str2Date(instrument.getSourceVerified()));
        values.add(instrument.getSourceVerifiedBy());
        values.add(RODUtil.str2Date(instrument.getSourceNextUpdate()));
        values.add(instrument.getSourceValidatedBy());

        values.add(instrument.getSourceId());

        Connection conn = null;
        try {
            conn = getConnection();
            SQLUtil.executeUpdate(editSourceSQL, values, conn);

        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    private static final String q_insert_parent_instrument =
        "INSERT INTO T_SOURCE_LNK (FK_SOURCE_PARENT_ID, FK_SOURCE_CHILD_ID, CHILD_TYPE, PARENT_TYPE) VALUES (?,?,'S','S')";

    public void addParentInstrument(String instId, String parentInstrumentId) throws ServiceException {
        List<Object> values = null;
        Connection conn = null;
        try {
            conn = getConnection();
            values = new ArrayList<Object>();
            values.add(parentInstrumentId);
            values.add(instId);
            SQLUtil.executeUpdate(q_insert_parent_instrument, values, conn);
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    private static final String q_insert_linked_sources =
        "INSERT INTO T_SOURCE_LNK (FK_SOURCE_PARENT_ID, FK_SOURCE_CHILD_ID, CHILD_TYPE, PARENT_TYPE) VALUES (?,?,'S','C')";

    public void addLinkedSources(String instId, List<String> selectedSourceClasses) throws ServiceException {
        List<Object> values = null;
        Connection conn = null;
        try {
            conn = getConnection();
            for (Iterator<String> it = selectedSourceClasses.iterator(); it.hasNext();) {
                String sourceId = it.next();
                values = new ArrayList<Object>();
                values.add(sourceId);
                values.add(instId);
                SQLUtil.executeUpdate(q_insert_linked_sources, values, conn);
            }
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    /** */
    private static final String addSourceSQL = "INSERT INTO T_SOURCE SET "
    + "TITLE=?, ALIAS=?, SOURCE_CODE=?, DRAFT=?, URL=?, CELEX_REF=?, "
    + "FK_CLIENT_ID=?, ISSUED_BY_URL=?, DGENV_REVIEW=?, VALID_FROM=?, GEOGRAPHIC_SCOPE=?, ABSTRACT=?, "
    + "COMMENT=?, EC_ENTRY_INTO_FORCE=?, EC_ACCESSION=?, SECRETARIAT=?, SECRETARIAT_URL=?, RM_VERIFIED=?, "
    + "RM_VERIFIED_BY=?, RM_NEXT_UPDATE=?, RM_VALIDATED_BY=?, LAST_UPDATE=CURDATE()";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#addInstrument(InstrumentFactsheetDTO instrument)
     */
    public Integer addInstrument(InstrumentFactsheetDTO instrument) throws ServiceException {

        List<Object> values = new ArrayList<Object>();
        values.add(instrument.getSourceTitle());
        values.add(instrument.getSourceAlias());
        values.add(instrument.getSourceCode());
        values.add(instrument.getSourceDraft());
        values.add(instrument.getSourceUrl());
        values.add(instrument.getSourceCelexRef());
        values.add(instrument.getSourceFKClientId());
        values.add(instrument.getSourceIssuedByUrl());
        values.add(instrument.getSourceDgenvReview());
        values.add(RODUtil.str2Date(instrument.getSourceValidFrom()));
        values.add(instrument.getSourceGeographicScope());
        values.add(instrument.getSourceAbstract());
        values.add(instrument.getSourceComment());
        values.add(RODUtil.str2Date(instrument.getSourceEcEntryIntoForce()));
        values.add(RODUtil.str2Date(instrument.getSourceEcAccession()));
        values.add(instrument.getSourceSecretariat());
        values.add(instrument.getSourceSecretariatUrl());
        values.add(RODUtil.str2Date(instrument.getSourceVerified()));
        values.add(instrument.getSourceVerifiedBy());
        values.add(RODUtil.str2Date(instrument.getSourceNextUpdate()));
        values.add(instrument.getSourceValidatedBy());

        Integer instrumentId = null;

        Connection conn = null;
        try {
            conn = getConnection();
            SQLUtil.executeUpdate(addSourceSQL, values, conn);
            instrumentId = SQLUtil.getLastInsertID(conn);

        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
        return instrumentId;
    }

    private static final String q_instrument_urls =
        "SELECT URL, SECRETARIAT, SECRETARIAT_URL, ALIAS, ISSUED_BY_URL "
        + "FROM T_SOURCE";

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao##getInstrumentsUrls()
     */
    public List<UrlDTO> getInstrumentsUrls() throws ServiceException {

        List<UrlDTO> ret = new ArrayList<UrlDTO>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            if (isDebugMode) logQuery(q_instrument_urls);
            preparedStatement = connection.prepareStatement(q_instrument_urls);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                UrlDTO url = new UrlDTO();

                url.setTitle(null);
                url.setUrl(rs.getString("URL"));
                ret.add(url);

                url = new UrlDTO();
                url.setTitle(rs.getString("SECRETARIAT"));
                url.setUrl(rs.getString("SECRETARIAT_URL"));
                ret.add(url);

                url = new UrlDTO();
                url.setTitle(rs.getString("ALIAS"));
                url.setUrl(rs.getString("ISSUED_BY_URL"));
                ret.add(url);
            }
        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return ret;
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.dao.ISourceDao#getSourceLinks()
     */
    public List<SourceLinksDTO> getSourceLinks() throws ServiceException {

        String query = "SELECT FK_SOURCE_CHILD_ID, FK_SOURCE_PARENT_ID "
        + "FROM T_SOURCE_LNK WHERE CHILD_TYPE = 'S' AND PARENT_TYPE = 'S'";

        List<Object> values = new ArrayList<Object>();

        Connection conn = null;
        SourceLinksDTOReader rsReader = new SourceLinksDTOReader();
        try {
            conn = getConnection();
            SQLUtil.executeQuery(query, values, rsReader, conn);
            List<SourceLinksDTO>  list = rsReader.getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }
}
