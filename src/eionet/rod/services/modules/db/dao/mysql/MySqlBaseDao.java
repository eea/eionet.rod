package eionet.rod.services.modules.db.dao.mysql;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import eionet.rod.Constants;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.LogServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * @author sasam
 * 
 */
public abstract class MySqlBaseDao {

	protected static String rodDomain; // TO DO INITIALISATION

	protected static String roNs; // TO DO INITIALISATION

	private static DataSource ds = null;

	protected static LogServiceIF logger = RODServices.getLogService();

	protected static boolean isDebugMode = true; // logger.enable(LogServiceIF.DEBUG);

	protected static Map properties = new HashMap();

	static {
		try {
			FileServiceIF fileService = RODServices.getFileService();
			properties.put(FileServiceIF.ISSUE_NAMESPACE, fileService.getStringProperty(FileServiceIF.ISSUE_NAMESPACE));
			properties.put(FileServiceIF.ORGANISATION_NAMESPACE, fileService.getStringProperty(FileServiceIF.ORGANISATION_NAMESPACE));
			properties.put(FileServiceIF.CONTREG_TITLE_PREDICATE, fileService.getStringProperty(FileServiceIF.CONTREG_TITLE_PREDICATE));
			properties.put(FileServiceIF.CONTREG_TYPE_PREDICATE, fileService.getStringProperty(FileServiceIF.CONTREG_TYPE_PREDICATE));
			properties.put(FileServiceIF.CONTREG_DATE_PREDICATE, fileService.getStringProperty(FileServiceIF.CONTREG_DATE_PREDICATE));
			properties.put(FileServiceIF.CONTREG_IDENTIFIER_PREDICATE, fileService.getStringProperty(FileServiceIF.CONTREG_IDENTIFIER_PREDICATE));
			properties.put(FileServiceIF.CONTREG_FORMAT_PREDICATE, fileService.getStringProperty(FileServiceIF.CONTREG_FORMAT_PREDICATE));
			properties.put(FileServiceIF.CONTREG_COVERAGE_PREDICATE, fileService.getStringProperty(FileServiceIF.CONTREG_COVERAGE_PREDICATE));
			properties.put(FileServiceIF.COUNTRY_NAMESPACE, fileService.getStringProperty(FileServiceIF.COUNTRY_NAMESPACE));
			properties.put(FileServiceIF.SPATIAL_NAMESPACE, fileService.getStringProperty(FileServiceIF.SPATIAL_NAMESPACE));
			properties.put(FileServiceIF.DB_DRV, fileService.getStringProperty(FileServiceIF.DB_DRV));
			properties.put(FileServiceIF.DB_URL, fileService.getStringProperty(FileServiceIF.DB_URL));
			properties.put(FileServiceIF.DB_USER_ID, fileService.getStringProperty(FileServiceIF.DB_USER_ID));
			properties.put(FileServiceIF.DB_USER_PWD, fileService.getStringProperty(FileServiceIF.DB_USER_PWD));
			properties.put(FileServiceIF.RA_NAMESPACE, fileService.getStringProperty(FileServiceIF.RA_NAMESPACE));
		    rodDomain  = RODServices.getFileService().getStringProperty( Constants.ROD_URL_DOMAIN );
		    roNs= RODServices.getFileService().getStringProperty( FileServiceIF.RO_NAMESPACE);

			
			try {
				InitialContext ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webrod");
			} catch (NamingException e) {
				logger.error("If you run a web application you need to specify webrod datasource ");
			}

		} catch (Exception e) {
			logger.error(e);
		}
	}

	public MySqlBaseDao() {
	}

	/**
	 * Returns a new database connection.
	 * 
	 * @throw ServiceException if no connections were available.
	 */
	public static synchronized Connection getConnection() throws SQLException {

		Connection conn = null;
		if (ds != null) {
			conn = ds.getConnection();
		} else {
			try {
				Class.forName(properties.get(FileServiceIF.DB_DRV).toString());
				conn = DriverManager.getConnection(properties.get(FileServiceIF.DB_URL).toString(), properties.get(FileServiceIF.DB_USER_ID).toString(), properties.get(FileServiceIF.DB_USER_PWD).toString());

			} catch (Throwable e) {
				System.err.println(e.getMessage());
			}

		}
		return conn;
	}

	public static void closeAllResources(ResultSet rs, Statement pstmt, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if ((conn != null) && (!conn.isClosed())) {
				conn.close();
				conn = null;
			}
		} catch (SQLException sqle) {
		}
	}

	public static void closeConnection(Connection conn) {
		try {
			if ((conn != null) && (!conn.isClosed())) {
				conn.close();
				conn = null;
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	public static void commit(Connection conn) {
		try {
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void rollback(Connection conn) {
		try {
			conn.rollback();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void logQuery(String query) {
		logger.debug("Query is " + query);
	}

	protected java.sql.Date sqlDate(Date date) {
		if(date == null)
			return null;
		return new java.sql.Date(date.getTime());

	}

	protected String[][] _executeStringQuery(String sql_stmt) throws ServiceException {
		Vector rvec = new Vector(); // Return value as Vector
		String rval[][] = {}; // Return value
		Connection con = null;
		Statement stmt = null;
		ResultSet rset = null;

		if (isDebugMode) logQuery(sql_stmt);

		// Process the result set

		try {
			con = getConnection();
			stmt = con.createStatement();
			rset = stmt.executeQuery(sql_stmt);
			ResultSetMetaData md = rset.getMetaData();

			// number of columns in the result set
			int colCnt = md.getColumnCount();

			while (rset.next()) {
				String row[] = new String[colCnt]; // Row of the result set

				// Retrieve the columns of the result set
				for (int i = 0; i < colCnt; ++i) {
					byte[] bvalue = rset.getBytes(i + 1);
					if (bvalue != null)
						row[i] = new String(bvalue, "UTF8");
					else
						row[i] = null;
				}

				rvec.addElement(row); // Store the row into the vector
			}
		} catch (UnsupportedEncodingException e) {
			logger.fatal(e);
			throw new ServiceException(e.getMessage());
		} catch (SQLException ex) {
			logger.error(ex);
			throw new ServiceException(ex.getMessage());
		} finally {
			// Close connection

			closeAllResources(null, stmt, con);
		}

		// Build return value
		if (rvec.size() > 0) {
			rval = new String[rvec.size()][];

			for (int i = 0; i < rvec.size(); ++i)
				rval[i] = (String[]) rvec.elementAt(i);
		}

		// Success
		return rval;
	}

	protected String[][] _executeStringQuery(PreparedStatement preparedStatement) throws SQLException {
		Vector rvec = new Vector(); // Return value as Vector
		String rval[][] = {}; // Return value
		ResultSet rset = preparedStatement.executeQuery();
		ResultSetMetaData md = rset.getMetaData();

		// number of columns in the result set
		int colCnt = md.getColumnCount();

		try {
			while (rset.next()) {
				String row[] = new String[colCnt]; // Row of the result set

				// Retrieve the columns of the result set
				for (int i = 0; i < colCnt; ++i) {
					byte[] bvalue = rset.getBytes(i + 1);
					if (bvalue != null)
						row[i] = new String(bvalue, "UTF8");
					else
						row[i] = null;
				}

				rvec.addElement(row); // Store the row into the vector
			}
		} catch (UnsupportedEncodingException e) {
			logger.fatal(e);
			throw new SQLException(e.getMessage());
		}
		rset.close();
		// Build return value
		if (rvec.size() > 0) {
			rval = new String[rvec.size()][];

			for (int i = 0; i < rvec.size(); ++i)
				rval[i] = (String[]) rvec.elementAt(i);
		}

		// Success
		return rval;
	}

	public static String[][] getResults(ResultSet rset) throws SQLException {
		Vector rvec = new Vector(); // Return value as Vector
		String rval[][] = {}; // Return value

		// if (logger.enable(logger.DEBUG)) logger.debug(sql);

		// Process the result set

		try {

			ResultSetMetaData md = rset.getMetaData();

			// number of columns in the result set
			int colCnt = md.getColumnCount();

			while (rset.next()) {
				String row[] = new String[colCnt]; // Row of the result set

				// Retrieve the columns of the result set
				for (int i = 0; i < colCnt; ++i)
					row[i] = rset.getString(i + 1);

				rvec.addElement(row); // Store the row into the vector
			}
		} catch (SQLException e) {
			// logger.error("Error occurred when processing result set: " +
			// sql,e);
			logger.error(e);
			throw new SQLException("Error occurred when processing result set: " + "");
		}

		// Build return value
		if (rvec.size() > 0) {
			rval = new String[rvec.size()][];

			for (int i = 0; i < rvec.size(); ++i)
				rval[i] = (String[]) rvec.elementAt(i);
		}

		// Success
		return rval;
	}

	protected Vector _getVectorOfHashes(String sql_stmt) throws ServiceException {

		Vector rvec = new Vector(); // Return value as Vector

		Connection con = null;
		Statement stmt = null;
		ResultSet rset = null;

		Hashtable h = null;

		if (isDebugMode) logQuery(sql_stmt);

		// _log("CONN OK");
		try {
			con = getConnection();
			stmt = con.createStatement();
			rset = stmt.executeQuery(sql_stmt);
			// _log("RESULT OK");
			ResultSetMetaData md = rset.getMetaData();

			// number of columns in the result set
			int colCnt = md.getColumnCount();
			// _log("COLS OK" + colCnt);

			while (rset.next()) {
				h = new Hashtable();

				// Retrieve the columns of the result set
				for (int i = 0; i < colCnt; ++i) {
					String name = md.getColumnName(i + 1);
					String value = rset.getString(i + 1);
					if (value == null) value = "";

					h.put(name, value);
				}

				rvec.addElement(h); // Store the row into the vector
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Error occurred when processing result set: " + sql_stmt, e);
			throw new ServiceException("Error occurred when processing result set: " + sql_stmt);
		} catch (NullPointerException nue) {
			nue.printStackTrace(System.out);
		} finally {
			closeAllResources(rset, stmt, con);
		}
		// _log(" return vec");
		return rvec;

	}

	// a new method
	protected Vector _getVectorOfHashes(PreparedStatement pstatement) throws SQLException {

		Vector rvec = new Vector(); // Return value as Vector
		Map record;
		ResultSet rs = null;

		rs = pstatement.executeQuery();
		ResultSetMetaData md = rs.getMetaData();
		// number of columns in the result set
		int colCnt = md.getColumnCount();
		// _log("COLS OK" + colCnt);
		String value = null;
		try {
			while (rs.next()) {
				record = new Hashtable();
				// Retrieve the columns of the result set
				for (int i = 0; i < colCnt; ++i) {
					String name = md.getColumnName(i + 1);
					byte[] bvalue = rs.getBytes(i + 1);
					if (bvalue != null)
						value = new String(bvalue, "UTF8");
					else
						value = "";
					record.put(name, value);
				}
				rvec.addElement(record); // Store the row into the vector
			}
		} catch (UnsupportedEncodingException e) {
			logger.fatal(e);
			throw new SQLException(e.getMessage());
		}
		if (rs != null) {
			rs.close();
		}

		return rvec;

	}

	protected String changeDateDelimeter(String date) {
		if (date != null && !date.equals("") && !date.equals("present") && !date.equals("Prior to start of ROD (2003)")) {
			StringTokenizer st = new StringTokenizer(date, "/");
			String[] dmy = new String[3];
			int cnt = 0;
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				dmy[cnt] = token;
				cnt++;
			}
			String sDate = "'" + dmy[2] + "-" + dmy[1] + "-" + dmy[0] + "'";

			return sDate;
		}
		return "NULL";
	}

	protected int _executeUpdate(String sql_stmt) throws ServiceException {

		if (isDebugMode) logQuery(sql_stmt);

		Connection con = null; // Connection object
		Statement stmt = null; // Statement object
		int rval = 0; // Return value
		// Get connection object

		// Create statement object
		try {
			con = getConnection();
			stmt = con.createStatement();
		} catch (SQLException e) {
			// Error handling
			// logger.error( "UpdateStatement failed: " + e.toString());
			// Free resources
			try {
				closeAllResources(null, stmt, con);
			} catch (Throwable exc) {
				throw new ServiceException("_close() failed: " + sql_stmt);
			}
			logger.error("Connection.createStatement() failed: " + sql_stmt, e);
			throw new ServiceException("Update failed: " + sql_stmt);
		}

		// Execute update
		try {
			rval = stmt.executeUpdate(sql_stmt);
		} catch (Exception e) {
			// Error handling
			throw new ServiceException("Statement.executeUpdate(" + sql_stmt + ") failed" + e.getMessage());
		} finally {
			// Free resources
			closeAllResources(null, stmt, con);
		}
		// Success
		return rval;
	}

	protected void addStringsToSet(Set set, String[][] str) {
		for (int i = 0; i < str.length; i++)
			set.add(str[i][0]);
	}

	protected String strLit(String s) {
		int i = 0;
		while (i < s.length()) {
			if (s.charAt(i) == '\'') {
				s = s.substring(0, i) + "'" + s.substring(i);
				i++;
			}
			i++;
		}
		return s;

	}

	protected String cnvVector(Vector v, String separator) {

		// quick fix
		if (v == null){
			return "";
		}

		StringBuffer s = new StringBuffer();
		for (int i = 0; i < v.size(); i++) {
			
			if (v.elementAt(i)!=null) {
				s.append(v.elementAt(i));
				if (i < v.size() - 1){
					s.append(separator);
				}
			}
		}

		return s.toString();
	}

	protected String currentDate() throws ServiceException {
		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		java.util.Date dt = new java.util.Date();
		Date dt2 = new Date(dt.getTime());
		String curDate = "";// = dt2.toString(); //objToStr(dt2,
		// ANSI_DATETIME_FORMAT);

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			curDate = sdf.format(dt2);
		} catch (Exception e) {
			throw new ServiceException("Getting today's date failed");
		}
		return curDate;
	}

	protected Hashtable _getHashtable(String sql_stmt) throws ServiceException {

		Connection con = null;
		Statement stmt = null;
		ResultSet rset = null;

		Hashtable h = null;

		if (isDebugMode) logQuery(sql_stmt);

		// _log("CONN OK");
		try {
			con = getConnection();
			stmt = con.createStatement();
			rset = stmt.executeQuery(sql_stmt);
			// _log("RESULT OK");
			ResultSetMetaData md = rset.getMetaData();

			// number of columns in the result set
			int colCnt = md.getColumnCount();
			// _log("COLS OK" + colCnt);

			while (rset.next()) {
				h = new Hashtable();
				// Retrieve the columns of the result set
				for (int i = 0; i < colCnt; ++i) {
					String name = md.getColumnName(i + 1);
					String value = rset.getString(i + 1);
					if (value == null) value = "";
					h.put(name, value);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Error occurred when processing result set: " + sql_stmt, e);
			throw new ServiceException("Error occurred when processing result set: " + sql_stmt);
		} catch (NullPointerException nue) {
			nue.printStackTrace(System.out);
		} finally {
			closeAllResources(null, stmt, con);
		}
		// _log(" return vec");
		return h;

	}

	protected Hashtable _getHashtable(PreparedStatement preparedStatement) throws ServiceException {

		Connection con = null;
		Statement stmt = null;
		ResultSet rset = null;

		Hashtable h = null;
		try {
			rset = preparedStatement.executeQuery();
			// _log("RESULT OK");
			ResultSetMetaData md = rset.getMetaData();

			// number of columns in the result set
			int colCnt = md.getColumnCount();
			// _log("COLS OK" + colCnt);

			while (rset.next()) {
				h = new Hashtable();
				// Retrieve the columns of the result set
				for (int i = 0; i < colCnt; ++i) {
					String name = md.getColumnName(i + 1);
					String value = rset.getString(i + 1);
					if (value == null) value = "";
					h.put(name, value);
				}
			}
		} catch (SQLException e) {

			logger.error(e);
			throw new ServiceException(e.getMessage());
		} catch (NullPointerException nue) {
			nue.printStackTrace(System.out);
		} finally {
			closeAllResources(null, stmt, con);
		}
		// _log(" return vec");
		return h;

	}

	protected static String rplAmp(String fld, String alias) {
	    return "REPLACE(REPLACE(" + fld +", '&', '&#038;'),'<','&lt;') AS " + alias;
	}

	/**
	 * builds an addition to where clause, OR condition of field IDs, given in
	 * the param
	 * 
	 * example ids=[1,4,5], fldName=ITEM_ID return ( ITEM_ID=1 OR ITEM_ID=4 OR
	 * ITEM_ID=5 )
	 */
	protected String getWhereClause(String fldName, StringTokenizer ids) {
		StringBuffer s = new StringBuffer();
		s.append(" (");
		while (ids.hasMoreTokens()) {
			s.append(fldName).append("=").append(ids.nextToken()).append(" OR ");
		}
		s.delete(s.length() - 4, s.length());
		s.append(" ) ");
		return s.toString();
	}

	public String[][] getCountryIdPairs() throws ServiceException {
		String sql = "SELECT PK_SPATIAL_ID, SPATIAL_NAME FROM T_SPATIAL WHERE SPATIAL_TYPE ='C' " + " ORDER BY SPATIAL_NAME";
		String[][] s = _executeStringQuery(sql);

		return s;
	}

	private static final String qCountryId = "" + "SELECT PK_SPATIAL_ID " + "FROM T_SPATIAL " + "WHERE SPATIAL_TYPE='C' AND SPATIAL_NAME=?";

	protected Integer getCountryId(String countryName, HashMap countryMap, Connection connection) throws Exception {
		Integer countryId = null;
		if (!countryMap.containsKey(countryName)) {

			PreparedStatement preparedStatement = connection.prepareStatement(qCountryId);
			preparedStatement.setString(1, countryName);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				countryId = new Integer(resultSet.getInt(1));
			}
			if (countryId != null) {
				countryMap.put(countryName, countryId);
			}
		}
		return (Integer) (countryMap.get(countryName));
	}

	protected Vector _getVectorOfNames(PreparedStatement preparedStatement, long ts, String tab, String col, String vol, String sql, String status) throws ServiceException {

		Vector rvec = new Vector(); // Return value as Vector

		Connection con = null;
		Statement stmt = null;
		ResultSet rset = null;

		try {
			con = getConnection();
			// stmt = con.createStatement();
			// rset = stmt.executeQuery(sql_stmt);

			rset = preparedStatement.executeQuery();
			while (rset.next()) {
				String value = "";
				byte[] bvalue = rset.getBytes(1);
				if (bvalue != null) value = new String(bvalue, "UTF8");
				String sub_trans_nr = null;
				String val = null;
				if (vol != null) {
					sub_trans_nr = rset.getString(2);
					String s = "SELECT value FROM T_UNDO where undo_time = '"+ts+"' AND tab='" + tab + "' " + " AND col = '" + col + "' AND sub_trans_nr = " + sub_trans_nr;
					String[][] sa = _executeStringQuery(s);
					if (sa.length > 0) val = sa[0][0];
					if (val.equals(vol)) {
						String name_sql = sql + value;
						String[][] na = _executeStringQuery(name_sql);
						if (na.length > 0) {
							if (status != null) {
								String s2 = "SELECT value FROM T_UNDO WHERE undo_time = '"+ts+"' AND tab = '" + tab + "' AND col = 'STATUS' AND sub_trans_nr = " + sub_trans_nr;
								String[][] sa2 = _executeStringQuery(s2);
								if (sa2.length > 0) if (sa2[0][0].equals(status)) rvec.addElement(na[0][0]);
							} else {
								rvec.addElement(na[0][0]);
							}
						}
					}
				} else {
					String name_sql = sql + "'" + value + "'";
					String[][] na = _executeStringQuery(name_sql);
					if (na.length > 0) rvec.addElement(na[0][0]);
				}
			}
		} catch (NullPointerException nue) {
			nue.printStackTrace(System.out);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			throw new ServiceException(e.getMessage());
		} finally {
			closeAllResources(null, stmt, con);
		}
		return rvec;
	}

}
