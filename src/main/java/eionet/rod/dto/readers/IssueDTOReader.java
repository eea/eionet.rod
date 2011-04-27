package eionet.rod.dto.readers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eionet.rod.dto.IssueDTO;
import eionet.rod.util.sql.ResultSetBaseReader;

public class IssueDTOReader extends ResultSetBaseReader {

    /** */
    List<IssueDTO> resultList = new ArrayList<IssueDTO>();

    /*
     * (non-Javadoc)
     * @see eionet.rod.util.sql.ResultSetBaseReader#readRow(java.sql.ResultSet)
     */
    public void readRow(ResultSet rs) throws SQLException {

        IssueDTO issueDTO = new IssueDTO();
        issueDTO.setIssueId(new Integer(rs.getInt("PK_ISSUE_ID")));
        issueDTO.setName(rs.getString("ISSUE_NAME"));

        resultList.add(issueDTO);
    }

    /**
     * @return the resultList
     */
    public List<IssueDTO> getResultList() {
        return resultList;
    }

}
