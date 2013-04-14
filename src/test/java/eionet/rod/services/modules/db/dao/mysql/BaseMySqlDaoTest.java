package eionet.rod.services.modules.db.dao.mysql;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;

public abstract class BaseMySqlDaoTest extends DatabaseTestCase {


    /** Only used in getDataSet(). */
    private FlatXmlDataSet loadedDataSet;

    protected Connection connection;

    /** Not used here. */
    protected Statement statement;

    /** Not used here. */
    protected ResultSet resultSet;

    /** Not used here. */
    protected PreparedStatement preparedStatement;


    protected static Map<String,String> properties = new HashMap<String,String>();
    static {
        try {
            FileServiceIF fileService = RODServices.getFileService();
            properties.put(FileServiceIF.DB_DRV, fileService.getStringProperty(FileServiceIF.DB_DRV));
            properties.put(FileServiceIF.DB_URL, fileService.getStringProperty(FileServiceIF.DB_URL));
            properties.put(FileServiceIF.DB_USER_ID, fileService.getStringProperty(FileServiceIF.DB_USER_ID));
            properties.put(FileServiceIF.DB_USER_PWD, fileService.getStringProperty(FileServiceIF.DB_USER_PWD));

            //FIXME: Not needed
            properties.put(FileServiceIF.DB_TEST_DRV, fileService.getStringProperty(FileServiceIF.DB_TEST_DRV));
            properties.put(FileServiceIF.DB_TEST_URL, fileService.getStringProperty(FileServiceIF.DB_TEST_URL));
            properties.put(FileServiceIF.DB_TEST_USER_ID, fileService.getStringProperty(FileServiceIF.DB_TEST_USER_ID));
            properties.put(FileServiceIF.DB_TEST_USER_PWD, fileService.getStringProperty(FileServiceIF.DB_TEST_USER_PWD));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor.
     */
    public BaseMySqlDaoTest(String arg0) throws Exception {
        super(arg0);

        //FIXME: Signal to tell the app we are unit testing is not needed now.
        System.setProperty("contreg.maven.phase","test");
        getJDBCConnection();
        //createTables();

    }

    private void getJDBCConnection() {
        try {
            Class.forName(properties.get(FileServiceIF.DB_DRV).toString());
            connection = DriverManager.getConnection(
                    properties.get(FileServiceIF.DB_URL).toString(),
                    properties.get(FileServiceIF.DB_USER_ID).toString(),
                    properties.get(FileServiceIF.DB_USER_PWD).toString());

        } catch (Throwable e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
    /**
     * Create tables from rod.ddl.
     */
    /*
    private void createTables() throws Exception {
        String sqlQuery;
        StringBuffer sqlBatch = new StringBuffer();
        String[] queries;

        // Create the tables from a file.
        InputStreamReader inStream = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("rod.ddl"));
        BufferedReader infile = new BufferedReader(inStream);

        Statement localStatement = connection.createStatement();

        while((sqlQuery = infile.readLine()) != null) {
            sqlQuery = sqlQuery.trim();
            if (sqlQuery.length() == 0) {
                continue;
            }
            sqlBatch.append(sqlQuery);
        }
        sqlQuery = sqlBatch.toString();
        queries = sqlQuery.split(";");
        for (String query : queries) {
            //System.out.println(query);
            localStatement.execute(query);
        }

        localStatement.close();
    }
    */

    /**
     * Creates a connection for the test method - Required by DatabaseTestCase.
     * Note that it assumed it is a MYSQL database.
     */
    @Override
    protected IDatabaseConnection getConnection() throws Exception {

        Properties properties = new Properties();
        properties.setProperty("http://www.dbunit.org/properties/datatypeFactory", "org.dbunit.ext.mysql.MySqlDataTypeFactory");
        getJDBCConnection();
        DatabaseConnection dbConn = new DatabaseConnection(connection);
        dbConn.getConfig().setPropertiesByString(properties);
        return dbConn;

    }

    // Shuts down the server for EACH test method.
    // Requires that we know where the database is located.
    /*
    @Override
    protected void closeConnection(IDatabaseConnection connection) throws Exception {
        ServerLauncherSocketFactory.shutdown(databaseDir, null);
        connection = null;
    }
    */

    /**
     * Load the data which will be inserted for the test - Required by DatabaseTestCase.
     * The tables must already exist.
     * If a test class needs another seed, then it should provide this method.
     */
    @Override
    protected IDataSet getDataSet() throws Exception {

        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        loadedDataSet = builder.build(this.getClass().getClassLoader().getResourceAsStream("seed-rod.xml"));
        return loadedDataSet;

    }

    protected static void printVectorResult(Vector<Map<String,String>> vector) {
        System.out.println("\n\n\nResult size is " + vector.size());
        System.out.println("\nResult is\n " + vector.toString().replaceAll(", \\{", "\n{") + "\n");
    }

    protected static void printMatrixResult(String[][] matrix) {
        String result = "";
        for (int i = 0; i < matrix.length; i++) {
            String[] strings = matrix[i];
            result = "";
            for (int j = 0; j < strings.length; j++) {
                result += strings[j] + " ";
            }
            System.out.println(result + "\n");
        }
    }

    protected void closeAllResources() {
        try {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            if (statement != null) {
                statement.close();
                statement = null;
            }
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
            if ((connection != null) && (!connection.isClosed())) {
                connection.close();
                connection = null;
            }
        } catch (SQLException sqle) {
        }
    }

}
