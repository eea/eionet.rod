package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eionet.rod.dto.HistDeadlineDTO;
import eionet.rod.dto.readers.HistDeadlineDTOReader;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IHistoricDeadlineDao;
import eionet.rod.util.sql.SQLUtil;

public class HistoricDeadlineMySqlDao extends MySqlBaseDao implements IHistoricDeadlineDao {

	private static final String qHistoricDeadlines = 
		"SELECT h.FK_RA_ID AS id, h.DEADLINE AS deadline, o.TITLE AS title, o.FK_SOURCE_ID AS source " + 
		"FROM T_HISTORIC_DEADLINES h, T_OBLIGATION o " + 
		"WHERE h.DEADLINE >= ? AND h.DEADLINE <= ? AND h.FK_RA_ID = o.PK_RA_ID " + 
		"ORDER BY h.DEADLINE DESC";

	private static SimpleDateFormat simpFormater = new SimpleDateFormat("dd/MM/yyyy");

	public HistoricDeadlineMySqlDao() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IHistoricDeadlineDao#getHistoricDeadlines(java.lang.String,
	 *      java.lang.String)
	 */
	public List<HistDeadlineDTO> getHistoricDeadlines(String start_date, String end_date) throws ServiceException {
		String d = simpFormater.format(new Date());
		if (start_date == null || start_date.equals("")) start_date = "01/01/0001";
		if (end_date == null || end_date.equals("")) end_date = d;
		Connection connection = null;
		List<HistDeadlineDTO> result = new ArrayList<HistDeadlineDTO>();

		try {
			connection = getConnection();
			
			List<Object> values = new ArrayList<Object>();
			values.add(new java.sql.Date(simpFormater.parse(start_date).getTime()));
			values.add(new java.sql.Date(simpFormater.parse(end_date).getTime()));
			HistDeadlineDTOReader rsReader = new HistDeadlineDTOReader();
			SQLUtil.executeQuery(qHistoricDeadlines, values, rsReader, connection);
			result = rsReader.getResultList();
			
		} catch (Exception exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, null, connection);
		}

		return result;

	}

	
	private static final String q_delete_by_obligation_id =
		"DELETE FROM T_HISTORIC_DEADLINES " +
		"WHERE FK_RA_ID=?";
	
	public void  deleteByObligationId(Integer raId) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(q_delete_by_obligation_id);
			preparedStatement = connection.prepareStatement(q_delete_by_obligation_id);
			preparedStatement.setInt(1, raId.intValue());
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
		
	}
}
