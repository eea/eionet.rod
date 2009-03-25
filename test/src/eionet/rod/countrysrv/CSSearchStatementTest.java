package eionet.rod.countrysrv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import eionet.rod.countrysrv.CSSearchStatement;
import com.tee.xmlserver.Parameters;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

//import static org.easymock.EasyMock.*;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;


public class CSSearchStatementTest extends DatabaseTestCase {

	private FlatXmlDataSet loadedDataSet;

	protected Statement statement;

	protected ResultSet resultSet;
	
	protected PreparedStatement preparedStatement;

	protected static Map properties = new HashMap();
	static {
		try {
			FileServiceIF fileService = RODServices.getFileService();
			properties.put(FileServiceIF.DB_DRV, fileService.getStringProperty(FileServiceIF.DB_DRV));
			properties.put(FileServiceIF.DB_URL, fileService.getStringProperty(FileServiceIF.DB_URL));
			properties.put(FileServiceIF.DB_USER_ID, fileService.getStringProperty(FileServiceIF.DB_USER_ID));
			properties.put(FileServiceIF.DB_USER_PWD, fileService.getStringProperty(FileServiceIF.DB_USER_PWD));			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	protected IDatabaseConnection getConnection() throws Exception
        {
                Class.forName(properties.get(FileServiceIF.DB_DRV).toString());
                Connection connection = DriverManager.getConnection(
                        properties.get(FileServiceIF.DB_URL).toString(),
                        properties.get(FileServiceIF.DB_USER_ID).toString(),
                        properties.get(FileServiceIF.DB_USER_PWD).toString());

                return new DatabaseConnection(connection);
	}

	/*
	 * Load the data which will be inserted for the test - Required by DatabaseTestCase
	 * The table must already exist
	 */
	protected IDataSet getDataSet() throws Exception
	{
		loadedDataSet = new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("seed-rod.xml"));
		return loadedDataSet;
	}

        //  check for Unknown column 'T_SPATIAL.SPATIAL_ISMEMBERCOUNTRY' in 'on clause'
        /*public void test_query() throws Exception
        {
                // Create the mock objects
                HttpServletRequest request = createMock(HttpServletRequest.class);
                String reqString = "COUNTRY_ID=3&ORD=NEXT_REPORTING,%20DEADLINE";
                Parameters params = new Parameters(request, reqString);
                CSSearchStatement stmt = new CSSearchStatement(params, false, false);
                Connection conn = getConnection().getConnection();
                PreparedStatement ps = stmt.prepareStatement(conn);
                stmt.executeQuery();
        }*/

}
