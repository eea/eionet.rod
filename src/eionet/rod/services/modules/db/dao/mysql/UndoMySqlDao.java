package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import com.tee.uit.security.AccessController;
import com.tee.uit.security.SignOnException;


import eionet.rod.dto.ClientDTO;
import eionet.rod.dto.VersionDTO;
import eionet.rod.dto.readers.ClientDTOReader;
import eionet.rod.dto.readers.VersionDTOReader;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IHistoryDao;
import eionet.rod.services.modules.db.dao.IUndoDao;
import eionet.rod.util.sql.SQLUtil;

public class UndoMySqlDao extends MySqlBaseDao implements IUndoDao {

	public UndoMySqlDao() {
	}

	private final static String qCountSql = 
		"SELECT COUNT(*) AS count " + 
		"FROM T_UNDO " + 
		"WHERE (col='PK_RA_ID' OR col='PK_SOURCE_ID') " + 
		"AND (operation='U' OR operation='D' OR operation='UN' OR operation='UD' OR operation='UDD') " + 
		"AND show_object='y'";

	private final static String qUndoReportGeneral = 
		"select undo_time, col, tab, operation, value, show_object " + 
		"from T_UNDO " + 
		"WHERE (col='PK_RA_ID' OR col='PK_SOURCE_ID') " + 
			"AND (operation='U' OR operation='D' OR operation='UN' OR operation='UD' OR operation='UDD') " + 
		"ORDER BY undo_time DESC, tab LIMIT ?,?";

	private final static String qUndoReportSpecific = 
		"select a.undo_time, a.col, a.tab, a.operation, a.value, a.show_object " + 
		"from T_UNDO a, T_UNDO b " + "WHERE a.undo_time = b.undo_time " + 
			"AND b.col =? " + 
			"AND b.value =? " + 
			"AND a.tab =? " + 
			"AND (a.operation='U' OR a.operation='D' OR a.operation='UN' OR a.operation='UD' OR a.operation='UDD') " + 
		"ORDER BY a.undo_time DESC";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IUndoDao#getPreviousActions(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public Hashtable getPreviousActions(String id, String tab, String id_field) throws ServiceException {

		String sql = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rset = null;

		Hashtable ret = new Hashtable();

		try {

			if (id.equals("-1")) {
				FileServiceIF fileSrv = RODServices.getFileService();
				int step = fileSrv.getIntProperty(FileServiceIF.UNDO_STEP);

				con = getConnection();
				preparedStatement = con.prepareStatement(qCountSql);
				if (isDebugMode) logQuery(qCountSql);
				rset = preparedStatement.executeQuery();
				int count = 0;
				while (rset.next()) {
					count = rset.getInt("count");
				}

				int pages = 0;
				if (count % step == 0)
					pages = (count / step);
				else
					pages = (count / step) + 1;

				ret.put("pages", new Integer(pages));
				preparedStatement.close();
				int start = 0;
				for (int i = 1; i <= pages; i++) {
					preparedStatement = con.prepareStatement(qUndoReportGeneral);
					preparedStatement.setInt(1, start);
					preparedStatement.setInt(2, step);
					Vector vec = _getVectorOfHashes(preparedStatement);
					ret.put(new Integer(i).toString(), vec);
					start = start + step;
				}

				// sql = "select undo_time, col, tab, operation, value,
				// show_object from T_UNDO where operation='U' OR operation='D'
				// OR operation='UN' OR operation='UD' OR operation='UDD' ORDER
				// BY undo_time DESC, tab";
			} else {
				// sql = "select a.undo_time, a.col, a.tab, a.operation,
				// a.value, a.show_object from T_UNDO a, T_UNDO b WHERE
				// a.undo_time = b.undo_time AND b.col = '"+id_field+"' AND
				// b.value = "+id+" AND a.tab = '"+tab+"' AND (a.operation='U'
				// OR a.operation='D' OR a.operation='UN' OR a.operation='UD' OR
				// a.operation='UDD') ORDER BY a.undo_time DESC";
				// Vector vec = _getVectorOfHashes(sql);
				con = getConnection();
				preparedStatement = con.prepareStatement(qUndoReportSpecific);
				preparedStatement.setString(1, id_field);
				preparedStatement.setString(2, id);
				preparedStatement.setString(3, tab);
				Vector vec = _getVectorOfHashes(preparedStatement);
				ret.put("1", vec);
			}

		} catch (SQLException e) {

			logger.error("Error occurred when processing result set: " + sql, e);
			throw new ServiceException("Error occurred when processing result set: " + sql);
		} catch (NullPointerException nue) {
			nue.printStackTrace(System.out);
		} finally {
			closeAllResources(null, preparedStatement, con);
		}

		return ret;
	}
	
	/*
     * (non-Javadoc)
     * 
     * @see eionet.rod.services.modules.db.dao.IUndoDao#getPreviousActionsGeneral()
     */
    public List<VersionDTO> getPreviousActionsGeneral() throws ServiceException {
    	
    	String query = "select undo_time, col, tab, operation, value, show_object " + 
    		"from T_UNDO " + 
    		"WHERE (col='PK_RA_ID' OR col='PK_SOURCE_ID') " + 
    			"AND (operation='U' OR operation='D' OR operation='UN' OR operation='UD' OR operation='UDD') AND show_object='y' " + 
    		"ORDER BY undo_time DESC";
    	
    	List<Object> values = new ArrayList<Object>();
				
		Connection conn = null;
		VersionDTOReader rsReader = new VersionDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(query, values, rsReader, conn);
			List<VersionDTO>  list = rsReader.getResultList();
			return list;
		}
		catch (Exception e){
			logger.error(e);
			throw new ServiceException(e.getMessage());
		}
		finally{
			try{
				if (conn!=null) conn.close();
			}
			catch (SQLException e){}
		}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see eionet.rod.services.modules.db.dao.IUndoDao#getPreviousActionsReportSpecific()
     */
    public List<VersionDTO> getPreviousActionsReportSpecific(String id, String tab, String id_field) throws ServiceException {
    	
    	String query = "select undo_time, col, tab, operation, value, show_object " + 
    		"from T_UNDO WHERE " + 
    			"col = '" + id_field + "' " + 
    			"AND value = '" + id + "' " + 
    			"AND tab = '" + tab + "' " +
    			"AND (operation='U' OR operation='D' OR operation='UN' OR operation='UD' OR operation='UDD') " + 
    		"ORDER BY undo_time DESC";
    	
    	List<Object> values = new ArrayList<Object>();
				
		Connection conn = null;
		VersionDTOReader rsReader = new VersionDTOReader();
		try{
			conn = getConnection();
			SQLUtil.executeQuery(query, values, rsReader, conn);
			List<VersionDTO>  list = rsReader.getResultList();
			return list;
		}
		catch (Exception e){
			logger.error(e);
			throw new ServiceException(e.getMessage());
		}
		finally{
			try{
				if (conn!=null) conn.close();
			}
			catch (SQLException e){}
		}
    }

	private static final String qDeletedFromUndo = 
		"select undo_time, col, tab, operation, value, show_object " + 
		"from T_UNDO " + 
		"where tab=? AND operation='D' " + 
		"ORDER BY undo_time DESC, tab";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IUndoDao#getDeletedFromUndo(java.lang.String)
	 */
	public Vector getDeletedFromUndo(String item_type) throws ServiceException {

		String tab = null;
		if (item_type.equals("O' OR ITEM_TYPE='A"))
			tab = "T_OBLIGATION";
		else if (item_type.equals("L")) tab = "T_SOURCE";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Vector result = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qDeletedFromUndo);
			preparedStatement = connection.prepareStatement(qDeletedFromUndo);
			preparedStatement.setString(1, tab);
			result = _getVectorOfHashes(preparedStatement);
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}
		return result != null ? result : new Vector();

	}

	private final static String qSourceObligations = 
		"SELECT PK_RA_ID AS id " + 
		"FROM T_OBLIGATION " + 
		"WHERE FK_SOURCE_ID=?";

	private final static String qAddObligationIdsIntoUndo = 
		"INSERT INTO T_UNDO VALUES (?,?,'OBLIGATIONS','O','y','n',?,0,'y')";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IUndoDao#addObligationIdsIntoUndo(java.lang.Integer,
	 *      long, java.lang.String)
	 */
	public void addObligationIdsIntoUndo(Integer id, long ts, String table) throws ServiceException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Vector ids = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qSourceObligations);
			preparedStatement = connection.prepareStatement(qSourceObligations);
			preparedStatement.setInt(1, id.intValue());
			ids = _getVectorOfHashes(preparedStatement);
			preparedStatement.close();
			StringBuffer obligation_ids = new StringBuffer();
			for (Enumeration en = ids.elements(); en.hasMoreElements();) {
				Hashtable hash = (Hashtable) en.nextElement();
				obligation_ids.append(hash.get("id"));
				if (en.hasMoreElements()) obligation_ids.append(",");
			}
			// String ids_sql = "INSERT INTO T_UNDO VALUES (" + ts + ",'" +
			// table + "','OBLIGATIONS','O','y','n','" +
			// obligation_ids.toString() + "',0,'y')";
			// _executeUpdate(ids_sql);
			if (isDebugMode) logQuery(qAddObligationIdsIntoUndo);
			preparedStatement = connection.prepareStatement(qAddObligationIdsIntoUndo);
			preparedStatement.setLong(1, ts);
			preparedStatement.setString(2, table);
			preparedStatement.setString(3, obligation_ids.toString());

		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

	}

	private final static String qInsertTransactionInfo = 
		"INSERT INTO T_UNDO " + 
		"VALUES (?,?,?,?,'y','n',?,0,'n')";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IUndoDao#insertTransactionInfo(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String, long,
	 *      java.lang.String)
	 */
	public void insertTransactionInfo(String id, String state, String table, String id_field, long ts, String extraSQL) throws ServiceException {

		String whereClause = id_field + " = " + id + " " + extraSQL;
		// String insert_stmt = "INSERT INTO T_UNDO VALUES (" + ts + ",'" +
		// table + "','" + id_field + "','" + state + "','y','n','" +
		// whereClause + "',0,'n')";
		// _executeUpdate(insert_stmt);
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qInsertTransactionInfo);
			preparedStatement = connection.prepareStatement(qInsertTransactionInfo);
			preparedStatement.setLong(1, ts);
			preparedStatement.setString(2, table);
			preparedStatement.setString(3, id_field);
			preparedStatement.setString(4, state);
			preparedStatement.setString(5, whereClause);
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}

	}


	private final static String qInsertIntoUndo = 
		"INSERT INTO T_UNDO " + 
		"VALUES (?,?,?,?,?,?,?,?,?)";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IUndoDao#insertIntoUndo(java.sql.Connection,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, long, java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	public boolean insertIntoUndo(Connection con, String id, String state, String table, String id_field, long ts, String extraSQL, String show, String whereClause) throws ServiceException {
		Statement stmt = null;
		ResultSet rset = null;
		String sql_stmt = null;
		boolean doCreateConnection = (con == null);
		// Process the result set

		try {

			if (doCreateConnection) con = getConnection();
			stmt = con.createStatement();
			if (whereClause != null)
				sql_stmt = "SELECT * FROM " + table + " WHERE " + whereClause;
			else
				sql_stmt = "SELECT * FROM " + table + " WHERE " + id_field + " = " + id + " " + extraSQL;
			rset = stmt.executeQuery(sql_stmt);
			ResultSetMetaData md = rset.getMetaData();

			if (state.equals("U") || state.equals("D") || state.equals("UN") || state.equals("UDD")) {
				int colCnt = md.getColumnCount();
				int rowCnt = 0;
				PreparedStatement preparedStatement = con.prepareStatement(qInsertIntoUndo);
				while (rset.next()) {
					for (int i = 1; i < (colCnt + 1); ++i) {
						String value = rset.getString(i);
						// if (value != null) value = value.replaceAll("'",
						// "''");
						String column = md.getColumnName(i);
						int type = md.getColumnType(i);
						String quotes = "y";
						if (type == Types.INTEGER) quotes = "n";
						String isPrimary = isPrimaryKey(table, column);

						//String a = "";
						// if (quotes.equalsIgnoreCase("y")) a = "'";

						preparedStatement.setLong(1, ts);
						preparedStatement.setString(2, table);
						preparedStatement.setString(3, column);

						preparedStatement.setString(4, state);
						preparedStatement.setString(5, quotes);
						preparedStatement.setString(6, isPrimary);
						//preparedStatement.setString(7, value != null ? value : "");
						preparedStatement.setString(7, value != null ? value : "null");
					    //preparedStatement.setString(7,value);
						preparedStatement.setInt(8, rowCnt);
						preparedStatement.setString(9, show);
						// String insert_stmt = "INSERT INTO T_UNDO VALUES (" +
						// ts + ",'" + table + "','" + column + "','" + state +
						// "','" + quotes + "','" + isPrimary + "'," + a + value
						// + a + "," + rowCnt + ",'" + show + "')";
						preparedStatement.executeUpdate();
					}
					rowCnt++;
				}
				preparedStatement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Error occurred when processing result set: " + sql_stmt, e);
			throw new ServiceException("Error occurred when processing result set: " + sql_stmt);
		} finally {
			if (doCreateConnection)
				closeAllResources(null, stmt, con);
			else
				closeAllResources(null, stmt, null);
		}
		return true;
	}

	private String isPrimaryKey(String table, String column) throws ServiceException {
		String sql_stmt = "SHOW KEYS FROM " + table;
		Vector result = _getVectorOfHashes(sql_stmt);

		for (Enumeration en = result.elements(); en.hasMoreElements();) {
			Hashtable hash = (Hashtable) en.nextElement();
			String column_name = (String) hash.get("Column_name");
			String key_name = (String) hash.get("Key_name");
			if (column_name != null && key_name != null) {
				if (column.equalsIgnoreCase(column_name) && key_name.equalsIgnoreCase("PRIMARY")) {
					return "y";
				}
			}
		}
		return "n";
	}

	private static final String tinfo_sql = 
		"SELECT undo_time, col, tab, operation, value, quotes, sub_trans_nr " + 
		"FROM T_UNDO " + 
		"WHERE undo_time=? AND operation = 'A' " + 
		"ORDER BY undo_time,tab,sub_trans_nr";

	private static final String opinfo_sql = 
		"SELECT undo_time, col, tab, operation, value, quotes, sub_trans_nr, p_key, show_object " + 
		"FROM T_UNDO " + 
		"WHERE undo_time=? " + 
			"AND (operation = 'A' OR operation = 'ACL' OR operation = 'K' OR operation = 'T' OR operation = 'O' OR operation = 'L' OR operation = ? ) " + 
			"ORDER BY undo_time,tab,sub_trans_nr";

	/**
	 * @param state
	 * @param ts
	 * @param del
	 * @throws ServiceException
	 */
	private void copyUndo(Connection connection, String state, long ts, boolean del) throws ServiceException {

		long ut = System.currentTimeMillis();
		PreparedStatement preparedStatement = null;
		Statement statement = null;

		try {

			if (!state.equals("UD")) {
				if (isDebugMode) logQuery(tinfo_sql);
				preparedStatement = connection.prepareStatement(tinfo_sql);
				preparedStatement.setLong(1, ts);
				Vector tinfo_vec = _getVectorOfHashes(preparedStatement);

				for (Enumeration en = tinfo_vec.elements(); en.hasMoreElements();) {
					Hashtable hash = (Hashtable) en.nextElement();
					String t = (String) hash.get("tab");
					String v = (String) hash.get("value");

					if (!state.equals("UNN") && !state.equals("UDD")) insertIntoUndo(connection, null, state, t, null, ut, null, "y", v);
					if (del) {
						String delete_stmt = "DELETE FROM " + hash.get("tab") + " WHERE " + hash.get("value");
						statement = connection.createStatement();
						if (isDebugMode) logQuery(delete_stmt);
						statement.executeUpdate(delete_stmt);
						statement.close();
					}
				}
			}
			if (!state.equals("UNN") && !state.equals("UDD")) {

				if (isDebugMode) logQuery(opinfo_sql);
				preparedStatement = connection.prepareStatement(opinfo_sql);
				preparedStatement.setLong(1, ts);
				preparedStatement.setString(2, state.equals("UD") ? "D" : "L");
				Vector opinfo_vec = _getVectorOfHashes(preparedStatement);
				preparedStatement.close();

				for (Enumeration en = opinfo_vec.elements(); en.hasMoreElements();) {
					Hashtable h = (Hashtable) en.nextElement();
					String t = (String) h.get("tab");
					String c = (String) h.get("col");
					String val = (String) h.get("value");
					String quotes = (String) h.get("quotes");
					String sub_trans_nr = (String) h.get("sub_trans_nr");
					String p_key = (String) h.get("p_key");
					String show = (String) h.get("show_object");
					String operation = (String) h.get("operation");

					if (state.equals("UD") && (operation.equals("D"))) operation = "UD";

					// if (val != null) val = val.replaceAll("'", "''");

					String a = "";
					// if (quotes.equalsIgnoreCase("y") || val.equals("")) a =
					// "'";
					preparedStatement = connection.prepareStatement(qInsertIntoUndo);
					preparedStatement.setLong(1, ut);
					preparedStatement.setString(2, t);
					preparedStatement.setString(3, c);
					preparedStatement.setString(4, operation);
					preparedStatement.setString(5, quotes);
					preparedStatement.setString(6, p_key);
					preparedStatement.setString(7, a + val + a);
					preparedStatement.setInt(8, Integer.valueOf(sub_trans_nr).intValue());
					preparedStatement.setString(9, show);
					preparedStatement.executeUpdate();
				}
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
				}
			}

		}
	}

	private static final String qSelectUndoByOperation = 
		"SELECT undo_time, col, tab, operation, value, quotes, sub_trans_nr " + 
		"FROM T_UNDO " + 
		"WHERE undo_time=? AND operation=? " + 
		"ORDER BY undo_time,tab,sub_trans_nr";

	private static final String qSelectUndoByTableAndOperation = 
		"SELECT value FROM T_UNDO " + 
		"WHERE undo_time=? AND operation=? AND tab=?";

	private static final String qGetObligationSql = 
		"SELECT undo_time " + 
		"FROM T_UNDO " + 
		"WHERE tab='T_OBLIGATION' " + 
		"AND col='PK_RA_ID' " + 
		"AND operation=? " + 
		"AND value=?";

	private static final String q_delete_from_undo_by_operation_and_table = 
		"DELETE FROM T_UNDO " + 
		"WHERE undo_time=?  AND tab=? AND operation =?";

	private static final String q_delete_undo_by_operations_in_A_K_O_T = 
		"DELETE FROM T_UNDO " + 
		"WHERE undo_time=? " + 
			"AND (operation = 'A' OR operation = 'ACL' OR operation = 'K' OR operation = 'O' OR operation = 'T')";

	private static final String q_delete_location = 
		"DELETE FROM T_UNDO " + 
		"WHERE undo_time =? AND operation = 'L'";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IUndoDao#undo(long,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public String undo(long ts, String tab, String op, String id) throws ServiceException {
		Connection con = null;
		ResultSet rset = null;
		String sql_stmt = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement ps = null;
		String location = "versions.jsp?id=-1";

		// Process the result set

		try {
			con = getConnection();

			ps = con.prepareStatement(qSelectUndoByOperation);
			ps.setLong(1, ts);
			ps.setString(2, op);
			if (isDebugMode) logQuery(qSelectUndoByOperation);
			rset = ps.executeQuery();
			// sql_stmt = "SELECT undo_time, col, tab, operation, value, quotes,
			// sub_trans_nr FROM T_UNDO " + "WHERE undo_time = " + ts + " AND
			// operation = '" + op + "' ORDER BY undo_time,tab,sub_trans_nr";
			// rset = stmt.executeQuery(sql_stmt);

			if (op.equals("U")) {
				copyUndo(con, "UN", ts, true);
			} else if (op.equals("UN")) {
				copyUndo(con, "UNN", ts, true);
			} else if (op.equals("D")) {
				if (tab.equals("T_SOURCE")) {
					preparedStatement = con.prepareStatement(qSelectUndoByTableAndOperation);
					preparedStatement.setLong(1, ts);
					preparedStatement.setString(2, "0");
					preparedStatement.setString(3, tab);
					if (isDebugMode) logQuery(qSelectUndoByTableAndOperation);
					String[][] array = _executeStringQuery(preparedStatement);
					preparedStatement.close();
					if (array.length > 0) {
						String ids = array[0][0];
						StringTokenizer st = new StringTokenizer(ids, ",");
						while (st.hasMoreTokens()) {
							String oid = st.nextToken();
							preparedStatement = con.prepareStatement(qGetObligationSql);
							preparedStatement.setString(1, op);
							preparedStatement.setString(2, oid);
							if (isDebugMode) logQuery(qGetObligationSql);
							String[][] oa = _executeStringQuery(preparedStatement);

							if (oa.length > 0) {
								String time = oa[0][0];
								if (RODServices.getDbService().getGenericlDao().isIdAvailable(oid, "T_OBLIGATION")) undo(Long.valueOf(time).longValue(), "T_OBLIGATION", op, oid);
							}
						}
					}
				}
				copyUndo(con, "UD", ts, false);
			} else if (op.equals("UD")) {
				if (tab.equals("T_SOURCE")) {
					preparedStatement = con.prepareStatement(qSelectUndoByTableAndOperation);
					preparedStatement.setLong(1, ts);
					preparedStatement.setString(2, "0");
					preparedStatement.setString(3, tab);
					if (isDebugMode) logQuery(qSelectUndoByTableAndOperation);
					String[][] array = _executeStringQuery(preparedStatement);
					preparedStatement.close();
					if (array.length > 0) {
						String ids = array[0][0];
						StringTokenizer st = new StringTokenizer(ids, ",");
						while (st.hasMoreTokens()) {
							String oid = st.nextToken();
							preparedStatement = con.prepareStatement(qGetObligationSql);
							preparedStatement.setString(1, op);
							preparedStatement.setString(2, oid);
							if (isDebugMode) logQuery(qGetObligationSql);
							String[][] oa = _executeStringQuery(preparedStatement);

							if (oa.length > 0) {
								String time = oa[0][0];
								undo(Long.valueOf(time).longValue(), "T_OBLIGATION", "UD", oid);
							}
						}
					}
				}
				copyUndo(con, "UDD", ts, true);
			}

			preparedStatement = con.prepareStatement(qSelectUndoByTableAndOperation);
			preparedStatement.setLong(1, ts);
			preparedStatement.setString(2, "K");
			preparedStatement.setString(3, tab);
			if (isDebugMode) logQuery(qSelectUndoByTableAndOperation);
			String[][] user_array = _executeStringQuery(preparedStatement);
			preparedStatement.close();

			String user = "";
			if (user_array.length > 0) user = user_array[0][0];

			preparedStatement = con.prepareStatement(qSelectUndoByTableAndOperation);
			preparedStatement.setLong(1, ts);
			preparedStatement.setString(2, "T");
			preparedStatement.setString(3, tab);
			if (isDebugMode) logQuery(qSelectUndoByTableAndOperation);
			String[][] type_array = _executeStringQuery(preparedStatement);
			preparedStatement.close();

			String type = "";
			if (type_array.length > 0) type = type_array[0][0];

                        String acl_stmt = "SELECT value FROM T_UNDO WHERE operation='ACL' AND undo_time="+ts+" AND tab='"+tab+"'";
                        String[][] acl_array = _executeStringQuery(acl_stmt);
                        String aclPath = "";
                        if(acl_array.length > 0)
                                aclPath = acl_array[0][0];


			IHistoryDao historyDao = RODServices.getDbService().getHistoryDao();
			if (op.equals("U"))
				historyDao.logHistory(type, id, user, "N", "Undo Update");
			else if (op.equals("D"))
				historyDao.logHistory(type, id, user, "N", "Undo Delete");
			else if (op.equals("UN"))
				historyDao.logHistory(type, id, user, "R", "Redo Update");
			else if (op.equals("UD")) historyDao.logHistory(type, id, user, "R", "Redo Delete");

			String prev_ut = null;
			String prev_table = null;
			String prev_subtransnr = null;
			String prev_column = null;
			String prev_value = null;
			String prev_quotes = null;

			Vector tvec = new Vector();
                        boolean aclAdded = false;			

			while (rset.next()) {
				String ut = rset.getString(1);
				String value = null;
				byte[] bvalue = rset.getBytes(5);
				if (bvalue != null) {
					value = new String(bvalue);
				}

				String column = rset.getString(2);
				String table = rset.getString(3);
				String quotes = rset.getString(6);
				String sub_trans_nr = rset.getString(7);

				/*
				 * if(column.equals("REDIRECT_URL")){ location = value; }
				 */

				if (value != null) value = value.replaceAll("'", "''");

				if (rset.isLast()) {
					String[] ta = new String[3];
					ta[0] = column;
					ta[1] = value;
					ta[2] = quotes;
					tvec.add(ta);
				}

				if (prev_column != null && prev_value != null && prev_quotes != null) {
					String[] array = new String[3];
					array[0] = prev_column;
					array[1] = prev_value;
					array[2] = prev_quotes;
					tvec.add(array);

					if ((!sub_trans_nr.equals(prev_subtransnr) || !ut.equals(prev_ut) || !table.equals(prev_table)) || rset.isLast()) {

						StringBuffer cols = new StringBuffer();
						StringBuffer values = new StringBuffer();
						for (Enumeration en = tvec.elements(); en.hasMoreElements();) {
							String[] ar = (String[]) en.nextElement();
							cols.append(ar[0]);
							String q = "";
							if (ar[2].equals("y")) q = "'";
							values.append(q + ar[1] + q);
							if (en.hasMoreElements()) {
								cols.append(",");
								values.append(",");
							}
						}
						String insert_sql = "INSERT INTO " + prev_table + " (" + cols.toString() + ") VALUES (" + values.toString() + ")";
						if (!op.equals("UD"))
						{
							logQuery(insert_sql);
							_executeUpdate(insert_sql);
						}
                         if(op.equals("D")  && !aclAdded){
                             try {
                                 HashMap acls = AccessController.getAcls();
                                 if(aclPath != null && !aclPath.equalsIgnoreCase("")){
                                         if (!acls.containsKey(aclPath)){
                                             AccessController.addAcl(aclPath, user, "");
                                             aclAdded = true;
                                         }
                                 } else {
                                     String path = "";
                                     if(tab.equals("T_OBLIGATION"))
                                         path = "/obligations/";
                                     else if(tab.equals("T_SOURCE"))
                                         path = "/instruments/";
                                     path = path + id;
                                     
                                     if (!acls.containsKey(path)){
                                         AccessController.addAcl(path, user, "");
                                         aclAdded = true;
                                     }
                                 }
                             } catch (SignOnException e){
                                 e.printStackTrace();
                             }
                         }							
						preparedStatement = con.prepareStatement(q_delete_from_undo_by_operation_and_table);
						preparedStatement.setLong(1, Long.valueOf(prev_ut).longValue());
						preparedStatement.setString(2, prev_table);
						preparedStatement.setString(3, op);
						if (isDebugMode) logQuery(q_delete_from_undo_by_operation_and_table);
						preparedStatement.executeUpdate();
						preparedStatement.close();
						preparedStatement = con.prepareStatement(q_delete_undo_by_operations_in_A_K_O_T);
						preparedStatement.setLong(1, Long.valueOf(prev_ut).longValue());
						if (isDebugMode) logQuery(q_delete_undo_by_operations_in_A_K_O_T);
						preparedStatement.executeUpdate();
						preparedStatement.close();
						tvec = new Vector();
					}
				}
				prev_ut = ut;
				prev_table = table;
				prev_subtransnr = sub_trans_nr;
				prev_column = column;
				prev_value = value;
				prev_quotes = quotes;
			}
			preparedStatement = con.prepareStatement(qSelectUndoByTableAndOperation);
			preparedStatement.setLong(1, ts);
			preparedStatement.setString(2, "L");
			preparedStatement.setString(3, tab);
			if (isDebugMode) logQuery(qSelectUndoByTableAndOperation);
			String[][] url_array = _executeStringQuery(preparedStatement);

			if (url_array.length > 0) {
				if (!op.equals("UD")) {
					location = url_array[0][0];
				}
				preparedStatement = con.prepareStatement(q_delete_location);
				preparedStatement.setLong(1, Long.valueOf(prev_ut).longValue());
				if (isDebugMode) logQuery(q_delete_location);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}

			// }
		} catch (SQLException e) {
			logger.error("Error occurred when processing result set: " + sql_stmt, e);
			throw new ServiceException("Error occurred when processing result set: " + sql_stmt);
		} finally {
			closeAllResources(null, ps, con);
		}
		return location;
	}

	private static final String qSelectAllUndoByTableAndOperation = 
		"SELECT * FROM T_UNDO " + 
		"WHERE undo_time=? AND operation=? AND tab=?";

	private static final String q_ut = 
		"SELECT undo_time " + 
		"FROM T_UNDO " + 
		"WHERE tab='T_SOURCE' " + 
		"AND col='PK_SOURCE_ID' AND value=?";

	private static final String q_ids_sql = 
		"SELECT value " + 
		"FROM T_UNDO WHERE " + 
		"tab='T_SOURCE' AND operation='O' AND undo_time=?";

	private static final String q_utime_sql = 
		"SELECT undo_time " + 
		"FROM T_UNDO " + 
		"WHERE tab = 'T_OBLIGATION' " + 
		"AND operation = 'D' AND col='PK_RA_ID' AND value=?";

	private static final String q_ob_sql = 
		"SELECT * " + 
		"FROM T_UNDO " + 
		"WHERE tab='T_OBLIGATION' AND operation='D' AND undo_time=?";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IUndoDao#getUndoInformation(long,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public Vector getUndoInformation(long ts, String op, String tab, String id) throws ServiceException {

		Vector vec = new Vector();
		Connection con = null;
		PreparedStatement preparedStatement = null;

		System.out.println("Operation is " + op);
		System.out.println("Table is " + tab);
		System.out.println("Id is " + id);

		try {
			con = getConnection();
			preparedStatement = con.prepareStatement(qSelectAllUndoByTableAndOperation + " ORDER BY undo_time,tab,sub_trans_nr");
			preparedStatement.setLong(1, ts);
			preparedStatement.setString(2, op);
			preparedStatement.setString(3, tab);
			if (isDebugMode) logQuery(qSelectAllUndoByTableAndOperation);
			vec = _getVectorOfHashes(preparedStatement);
			System.out.println("Vector is " + vec);
			preparedStatement.close();

			if (tab.equals("T_SOURCE") && op.equals("D")) {
				String ids = "";
				preparedStatement = con.prepareStatement(q_ut);
				preparedStatement.setString(1, id);
				if (isDebugMode) logQuery(q_ut);
				String[][] tsa = _executeStringQuery(preparedStatement);
				preparedStatement.close();

				if (tsa.length > 0) {
					String undo_time = tsa[0][0];
					preparedStatement = con.prepareStatement(q_ids_sql);
					preparedStatement.setLong(1, Long.valueOf(undo_time).longValue());
					if (isDebugMode) logQuery(q_ids_sql);
					String[][] idsa = _executeStringQuery(preparedStatement);
					preparedStatement.close();

					if (idsa.length > 0) ids = idsa[0][0];
				}
				Vector obligations_vec = new Vector();
				StringTokenizer st = new StringTokenizer(ids, ",");
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					preparedStatement = con.prepareStatement(q_utime_sql);
					preparedStatement.setString(1, token);
					if (isDebugMode) logQuery(q_utime_sql);
					String[][] utime = _executeStringQuery(preparedStatement);
					preparedStatement.close();

					if (utime.length > 0) {
						preparedStatement = con.prepareStatement(q_ob_sql);
						preparedStatement.setLong(1, Long.valueOf(utime[0][0]).longValue());
						if (isDebugMode) logQuery(q_ob_sql);
						obligations_vec = _getVectorOfHashes(preparedStatement);
						if (obligations_vec.size() > 0) vec.addAll(obligations_vec);
						preparedStatement.close();
					}
				}
			}
		} catch (SQLException e) {
			logger.error(e);
			throw new ServiceException(e.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, con);
		}

		return vec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IUndoDao#getUndoUser(long,
	 *      java.lang.String)
	 */
	public String getUndoUser(long ts, String tab) throws ServiceException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		String[][] ua = {};

		try {
			con = getConnection();
			preparedStatement = con.prepareStatement(qSelectUndoByTableAndOperation);
			preparedStatement.setLong(1, ts);
			preparedStatement.setString(2, "K");
			preparedStatement.setString(3, tab);
			if (isDebugMode) logQuery(qSelectUndoByTableAndOperation);
			ua = _executeStringQuery(preparedStatement);
		} catch (SQLException e) {
			logger.error(e);
			throw new ServiceException(e.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, con);
		}
		if (ua.length > 0) return ua[0][0];

		return null;
	}
	
	private static final String q_undo_object_id = 
		"SELECT value " + 
		"FROM T_UNDO " + 
		"WHERE undo_time=? AND tab=? AND col=?";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IUndoDao#getUndoObjetcId(long,
	 *      java.lang.String, java.lang.String)
	 */
	public String getUndoObjetcId(long ts, String tab, String col) throws ServiceException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		String[][] ua = {};

		try {
			con = getConnection();
			preparedStatement = con.prepareStatement(q_undo_object_id);
			preparedStatement.setLong(1, ts);
			preparedStatement.setString(2, tab);
			preparedStatement.setString(3, col);
			if (isDebugMode) logQuery(q_undo_object_id);
			ua = _executeStringQuery(preparedStatement);
		} catch (SQLException e) {
			logger.error(e);
			throw new ServiceException(e.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, con);
		}
		if (ua.length > 0) return ua[0][0];

		return null;
	}

	private static final String q_ts = 
		"SELECT undo_time " + 
		"FROM T_UNDO " + 
		"WHERE tab='T_SOURCE' AND col='PK_SOURCE_ID' AND value=?";

	private static final String q_ids_sql2 = 
		"SELECT value " + 
		"FROM T_UNDO " + 
		"WHERE tab='T_SOURCE' AND undo_time=? AND col='OBLIGATIONS'";

	private static final String q_oblligation_id = 
		"SELECT PK_RA_ID AS id " + 
		"FROM T_OBLIGATION " + 
		"WHERE PK_RA_ID=?";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eionet.rod.services.modules.db.dao.IUndoDao#areRelatedObligationsIdsAvailable(java.lang.String)
	 */
	public String areRelatedObligationsIdsAvailable(String id) throws ServiceException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		String result = "";
		try {
			con = getConnection();
			preparedStatement = con.prepareStatement(q_ts);
			preparedStatement.setString(1, id);
			if (isDebugMode) logQuery(q_ts);
			String[][] tsa = _executeStringQuery(preparedStatement);
			preparedStatement.close();
			String ids = "";
			if (tsa.length > 0) {
				String undo_time = tsa[0][0];
				preparedStatement = con.prepareStatement(q_ids_sql2);
				preparedStatement.setLong(1, Long.valueOf(undo_time).longValue());
				if (isDebugMode) logQuery(q_ids_sql2);
				String[][] idsa = _executeStringQuery(preparedStatement);
				preparedStatement.close();
				if (idsa.length > 0) ids = idsa[0][0];
			}

			Vector vec = new Vector();
			StringTokenizer st = new StringTokenizer(ids, ",");
			preparedStatement = con.prepareStatement(q_oblligation_id);
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				preparedStatement.setInt(1, Integer.valueOf(token).intValue());
				if (isDebugMode) logQuery(q_oblligation_id);
				String[][] sa = _executeStringQuery(preparedStatement);
				if (sa.length > 0) {
					vec.add(sa[0][0]);
				}
			}
			preparedStatement.close();
			StringBuffer sb = new StringBuffer();
			for (Enumeration en = vec.elements(); en.hasMoreElements();) {
				String match = (String) en.nextElement();
				sb.append(match);
				if (en.hasMoreElements()) sb.append(" ");
			}

			result = sb.toString();

		} catch (SQLException e) {
			logger.error(e);
			throw new ServiceException(e.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, con);
		}
		return result;

	}

	
	public void insertIntoUndo(long ts, String table, String column, String state, String quotes, String isPrimary, String value, int rowCnt , String show) throws ServiceException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			if (isDebugMode) logQuery(qInsertIntoUndo);
			preparedStatement = connection.prepareStatement(qInsertIntoUndo);
			preparedStatement.setLong(1, ts);
			preparedStatement.setString(2, table);
			preparedStatement.setString(3, column);
			preparedStatement.setString(4, state);
			preparedStatement.setString(5, quotes);
			preparedStatement.setString(6, isPrimary);
			//preparedStatement.setString(7, value != null ? value : "");
			preparedStatement.setString(7, value != null ? value : "null");
			//preparedStatement.setString(7, value);
			preparedStatement.setInt(8, rowCnt);
			preparedStatement.setString(9, show);

			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.error(exception);
			throw new ServiceException(exception.getMessage());
		} finally {
			closeAllResources(null, preparedStatement, connection);
		}		
	
	}
	
}
