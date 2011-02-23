package eionet.rod.services.modules.db.dao.mysql.test;

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

	private FlatXmlDataSet loadedDataSet;

	protected Connection connection;

	protected Statement statement;

	protected ResultSet resultSet;
	
	protected PreparedStatement preparedStatement;

	protected static Map<String,String> properties = new HashMap<String,String>();
	static {
		try {
			FileServiceIF fileService = RODServices.getFileService();
			properties.put(FileServiceIF.DB_TEST_DRV, fileService.getStringProperty(FileServiceIF.DB_TEST_DRV));
			properties.put(FileServiceIF.DB_TEST_URL, fileService.getStringProperty(FileServiceIF.DB_TEST_URL));
			properties.put(FileServiceIF.DB_TEST_USER_ID, fileService.getStringProperty(FileServiceIF.DB_TEST_USER_ID));
			properties.put(FileServiceIF.DB_TEST_USER_PWD, fileService.getStringProperty(FileServiceIF.DB_TEST_USER_PWD));			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BaseMySqlDaoTest(String arg0) {
		super(arg0);
		System.setProperty("contreg.maven.phase","test");
		
		try {
			Class.forName(properties.get(FileServiceIF.DB_TEST_DRV).toString());
			connection = DriverManager.getConnection(properties.get(FileServiceIF.DB_TEST_URL).toString(), properties.get(FileServiceIF.DB_TEST_USER_ID).toString(), properties.get(FileServiceIF.DB_TEST_USER_PWD).toString());
		} catch (Throwable e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

	}

	protected IDatabaseConnection getConnection() throws Exception {
		
		Properties properties = new Properties();
		properties.setProperty("http://www.dbunit.org/properties/datatypeFactory", "org.dbunit.ext.mysql.MySqlDataTypeFactory");
		DatabaseConnection dbConn = new DatabaseConnection(connection);
		dbConn.getConfig().setPropertiesByString(properties);
	    return dbConn;
	    
	}

	/*
	 * Load the data which will be inserted for the test - Required by DatabaseTestCase
	 * The table must already exist
	 */
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
