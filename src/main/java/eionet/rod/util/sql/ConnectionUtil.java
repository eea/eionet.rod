package eionet.rod.util.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

public class ConnectionUtil {

    /** */
    private static Log logger = LogFactory.getLog(ConnectionUtil.class);

    /** */
    private static DataSource dataSource = null;
    private static boolean returnSimpleConnection = false;

    /**
     * 
     * @throws NamingException
     */
    private static void initDataSource() throws NamingException {

        Context initContext = new InitialContext();
        Context context = (Context) initContext.lookup("java:comp/env");
        DataSource ds = (javax.sql.DataSource) context.lookup("jdbc/webrod");
        dataSource = ds;
    }

    /**
     * 
     * @return Connection
     * @throws DataSourceException
     * @throws SQLException
     * @throws ServiceException
     */
    public static Connection getConnection() throws DataSourceException, SQLException, ServiceException {
        if (ConnectionUtil.returnSimpleConnection)
            return getSimpleConnection();
        else
            return getJNDIConnection();
    }

    /**
     * 
     * @return Connection
     * @throws DataSourceException
     */
    private static synchronized Connection getJNDIConnection() throws DataSourceException {

        try {
            if (dataSource == null)
                initDataSource();
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new DataSourceException("Failed to get connection through JNDI: " + e.toString(), e);
        }
    }

    /**
     * 
     * @return Connection
     * @throws DataSourceException
     * @throws SQLException
     * @throws ServiceException
     */
    private static Connection getSimpleConnection() throws DataSourceException, SQLException, ServiceException {

        FileServiceIF fileService = RODServices.getFileService();

        String drv = fileService.getStringProperty(FileServiceIF.DB_DRV);
        if (drv == null || drv.trim().length() == 0)
            throw new SQLException("Failed to get connection, missing property: " + FileServiceIF.DB_DRV);

        String url = fileService.getStringProperty(FileServiceIF.DB_URL);
        if (url == null || url.trim().length() == 0)
            throw new SQLException("Failed to get connection, missing property: " + FileServiceIF.DB_URL);

        String usr = fileService.getStringProperty(FileServiceIF.DB_USER_ID);
        if (usr == null || usr.trim().length() == 0)
            throw new SQLException("Failed to get connection, missing property: " + FileServiceIF.DB_USER_ID);

        String pwd = fileService.getStringProperty(FileServiceIF.DB_USER_PWD);
        if (pwd == null || pwd.trim().length() == 0)
            throw new SQLException("Failed to get connection, missing property: " + FileServiceIF.DB_USER_PWD);

        try {
            Class.forName(drv);
            return DriverManager.getConnection(url, usr, pwd);
        } catch (ClassNotFoundException e) {
            throw new DataSourceException("Failed to get connection, driver class not found: " + drv, e);
        }
    }

    /**
     * 
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed())
                conn.close();
        } catch (SQLException e) {
            logger.error("Failed to close connection", e);
        }
    }

    /**
     * 
     * @return boolean
     */
    public static boolean isReturnSimpleConnection() {
        return returnSimpleConnection;
    }

    /**
     * 
     * @param b
     */
    public static void setReturnSimpleConnection(boolean b) {
        ConnectionUtil.returnSimpleConnection = b;
    }

}
