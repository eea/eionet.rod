package eionet.rod.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author heinljab
 * 
 */
public class SQLUtil {

    /**
     * 
     * @param parameterizedSQL
     * @param values
     * @param conn
     * @return List<Map<String,SQLValue>>
     * @throws SQLException
     */
    public static List<Map<String, SQLValue>> executeQuery(String parameterizedSQL, List<Object> values, Connection conn)
            throws SQLException {

        SQLValueReader sqlValueReader = new SQLValueReader();
        executeQuery(parameterizedSQL, values, sqlValueReader, conn);
        return sqlValueReader.getResultList();
    }

    /**
     * 
     * @param parameterizedSQL
     * @param values
     * @param rsReader
     * @param conn
     * @throws SQLException
     */
    public static void executeQuery(String parameterizedSQL, List<Object> values, ResultSetBaseReader rsReader, Connection conn)
            throws SQLException {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = prepareStatement(parameterizedSQL, values, conn);
            rs = pstmt.executeQuery();
            if (rs != null) {
                ResultSetMetaData rsMd = rs.getMetaData();
                if (rsMd != null && rsMd.getColumnCount() > 0) {
                    rsReader.setResultSetMetaData(rsMd);
                    while (rs.next())
                        rsReader.readRow(rs);
                }
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
            }
        }

    }

    /**
     * 
     * @param sql
     * @param conn
     * @return List<Map<String,SQLValue>>
     * @throws SQLException
     */
    public static List<Map<String, SQLValue>> executeQuery(String sql, Connection conn) throws SQLException {

        SQLValueReader sqlValueReader = new SQLValueReader();
        executeQuery(sql, sqlValueReader, conn);
        return sqlValueReader.getResultList();
    }

    /**
     * 
     * @param sql
     * @param rsReader
     * @param conn
     * @throws SQLException
     */
    public static void executeQuery(String sql, ResultSetBaseReader rsReader, Connection conn) throws SQLException {

        ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs != null) {
                ResultSetMetaData rsMd = rs.getMetaData();
                if (rsMd != null && rsMd.getColumnCount() > 0) {
                    rsReader.setResultSetMetaData(rsMd);
                    while (rs.next())
                        rsReader.readRow(rs);
                }
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
            }
        }

    }

    /**
     * 
     * @param parameterizedSQL
     * @param values
     * @param conn
     * @return int
     * @throws SQLException
     */
    public static int executeUpdate(String parameterizedSQL, List<Object> values, Connection conn) throws SQLException {

        PreparedStatement pstmt = null;
        try {
            pstmt = prepareStatement(parameterizedSQL, values, conn);
            return pstmt.executeUpdate();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * 
     * @param parameterizedSQL
     * @param values
     * @param conn
     * @return PreparedStatement
     * @throws SQLException
     */
    public static PreparedStatement prepareStatement(String parameterizedSQL, List<Object> values, Connection conn)
            throws SQLException {

        PreparedStatement pstmt = conn.prepareStatement(parameterizedSQL);
        for (int i = 0; values != null && i < values.size(); i++) {
            try {
                String val = (String) values.get(i);
                if (val != null && val.equals("NULL"))
                    pstmt.setNull(i + 1, Types.NULL);
                else
                    pstmt.setObject(i + 1, values.get(i));
            } catch (ClassCastException e) {
                pstmt.setObject(i + 1, values.get(i));
            }
        }
        return pstmt;
    }

    /**
     * 
     * @param conn
     * @return Integer
     * @throws SQLException
     */
    public static Integer getLastInsertID(Connection conn) throws SQLException {

        ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select last_insert_id()");
            return (rs != null && rs.next()) ? new Integer(rs.getInt(1)) : null;
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
            }
        }
    }
}
