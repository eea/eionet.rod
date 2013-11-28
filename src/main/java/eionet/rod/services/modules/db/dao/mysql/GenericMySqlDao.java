package eionet.rod.services.modules.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IGenericDao;

public class GenericMySqlDao extends MySqlBaseDao implements IGenericDao {

    public GenericMySqlDao() {
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IGenericlDao#getTable(java.lang.String)
     */
    @Override
    public Vector<Map<String,String>> getTable(String tablename) throws ServiceException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Vector<Map<String,String>> result = null;

        tablename = tablename.toUpperCase();
        if (allowedTable(tablename)) {
            String query = "SELECT * FROM " + tablename;
            try {
                connection = getConnection();
                preparedStatement = connection.prepareStatement(query);
                if (isDebugMode) logQuery(query);
                result = _getVectorOfHashes(preparedStatement);
            } catch (SQLException exception) {
                logger.error(exception);
                throw new ServiceException(exception.getMessage());
            } finally {
                closeAllResources(null, preparedStatement, connection);
            }

        }
        return result != null ? result : new Vector<Map<String,String>>();
    }

    /*
     * (non-Javadoc)
     *
     * @see eionet.rod.services.modules.db.dao.IGenericlDao#getTableDesc(java.lang.String)
     */
    @Override
    public Vector<Hashtable<String,String>> getTableDesc(String tablename) throws ServiceException {
        tablename = tablename.toUpperCase();
        if (allowedTable(tablename)) {
            Vector<Hashtable<String,String>> rvec = new Vector<Hashtable<String,String>>();

            Connection con = null;
            PreparedStatement stmt = null;
            ResultSet rset = null;

            Hashtable<String,String> h = null;

            String sql_stmt = "SHOW FULL COLUMNS FROM " + tablename;
            try {

                con = getConnection();
                stmt = con.prepareStatement(sql_stmt);
                if (isDebugMode) logQuery(sql_stmt);
                rset = stmt.executeQuery();
                ResultSetMetaData md = rset.getMetaData();

                int columnCnt = md.getColumnCount();

                while (rset.next()) {
                    h = new Hashtable<String,String>();
                    for (int i = 0; i < columnCnt; i++) {
                        String name = md.getColumnName(i + 1);
                        String value = rset.getString(i + 1);
                        if (value == null) value = "";
                        if (name.equals("Field") || name.equals("Comment")) h.put(name, value);
                        if (name.equals("Type")) {
                            int start = value.indexOf("(");
                            int end = value.indexOf(")");
                            String length = "";
                            String newVal = "";
                            if (start != -1 && end != -1) {
                                newVal = value.substring(0, start);
                                if (!newVal.equalsIgnoreCase("enum")) {
                                    length = value.substring(start + 1, end);
                                    value = newVal;
                                }
                            }
                            h.put("Type", value);
                            h.put("Length", length);
                        }
                    }
                    rvec.addElement(h);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Error occurred when processing result set: " + sql_stmt, e);
                throw new ServiceException("Error occurred when processing result set: " + sql_stmt);
            } catch (NullPointerException nue) {
                nue.printStackTrace();
                logger.error("getTableDesc() NullPointerException " + nue);
            } finally {
                closeAllResources(null, stmt, con);
            }

            return rvec;
        }
        return null;
    }

    private static final String q_is_obligation_id_available =
        "SELECT PK_RA_ID AS id " +
        "FROM T_OBLIGATION " +
        "WHERE PK_RA_ID=?";

    private static final String q_is_source_id_available =
        "SELECT PK_SOURCE_ID AS id " +
        "FROM T_SOURCE " +
        "WHERE PK_SOURCE_ID=?";

    @Override
    public boolean isIdAvailable(String id, String table) throws ServiceException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String[][] m = null;
        boolean result = false;
        String query = null;

        try {

            if (table.equalsIgnoreCase("T_OBLIGATION"))
                query = q_is_obligation_id_available;
            else if (table.equalsIgnoreCase("T_SOURCE")) query = q_is_source_id_available;
            if (query != null) {
                connection = getConnection();
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1,Integer.valueOf(id).intValue());
                if (isDebugMode) logQuery(query);
                m = _executeStringQuery(preparedStatement);
                if (m.length == 0) result = true;
            }

        } catch (SQLException exception) {
            logger.error(exception);
            throw new ServiceException(exception.getMessage());
        } finally {
            closeAllResources(null, preparedStatement, connection);
        }

        return result;
    }

    private boolean allowedTable(String tablename) throws ServiceException {

        if (tablename.equalsIgnoreCase("T_CLIENT") || tablename.equalsIgnoreCase("T_CLIENT_LNK") || tablename.equalsIgnoreCase("T_DELIVERY") || tablename.equalsIgnoreCase("T_INDICATOR") || tablename.equalsIgnoreCase("T_INFO_LNK") || tablename.equalsIgnoreCase("T_ISSUE") || tablename.equalsIgnoreCase("T_LOOKUP") || tablename.equalsIgnoreCase("T_OBLIGATION") || tablename.equalsIgnoreCase("T_RAISSUE_LNK") || tablename.equalsIgnoreCase("T_RASPATIAL_LNK") || tablename.equalsIgnoreCase("T_ROLE") || tablename.equalsIgnoreCase("T_SOURCE") || tablename.equalsIgnoreCase("T_SOURCE_CLASS") || tablename.equalsIgnoreCase("T_SOURCE_LNK") || tablename.equalsIgnoreCase("T_SOURCE_TYPE") || tablename.equalsIgnoreCase("T_SPATIAL")) {

            return true;
        }
        return false;
    }

    @Override
    public String getLastUpdate() throws ServiceException {

        String ret = null;
        Date o_date = new Date();
        Date s_date = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yy");

        String o_sql = "SELECT MAX(LAST_UPDATE) FROM T_OBLIGATION";
        String s_sql = "SELECT MAX(LAST_UPDATE) FROM T_SOURCE";

        String[][] oa = _executeStringQuery(o_sql);
        String[][] sa = _executeStringQuery(s_sql);
        if (oa.length > 0) {
            o_date.setYear(Integer.parseInt(oa[0][0].substring(0, 4)) - 1900);
            o_date.setMonth(Integer.parseInt(oa[0][0].substring(5, 7)) - 1);
            o_date.setDate(Integer.parseInt(oa[0][0].substring(8, 10)));
        }
        if (sa.length > 0) {
            s_date.setYear(Integer.parseInt(sa[0][0].substring(0, 4)) - 1900);
            s_date.setMonth(Integer.parseInt(sa[0][0].substring(5, 7)) - 1);
            s_date.setDate(Integer.parseInt(sa[0][0].substring(8, 10)));
        }

        if (s_date == null && o_date != null)
            return df.format(o_date);
        else if (s_date != null && o_date == null) return df.format(s_date);

        if (o_date != null && s_date != null) {
            if (o_date.after(s_date))
                ret = df.format(o_date);
            else
                ret = df.format(s_date);
        }

        return ret;

    }

}
