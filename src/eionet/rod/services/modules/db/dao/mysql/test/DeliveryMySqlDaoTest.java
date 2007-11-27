package eionet.rod.services.modules.db.dao.mysql.test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import eionet.rod.services.modules.db.dao.mysql.DeliveryMySqlDao;

public class DeliveryMySqlDaoTest extends BaseMySqlDaoTest {

	private DeliveryMySqlDao deliveryMySqlDao;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(DeliveryMySqlDaoTest.class);
	}

	public DeliveryMySqlDaoTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		deliveryMySqlDao = new DeliveryMySqlDao();

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for
	 * 'eionet.rod.services.modules.db.dao.mysql.DeliveryMySqlDao.saveDeliveries(Integer,
	 * Vector, HashMap)'
	 */
	public void testSaveDeliveries() throws Exception {
		Map delivery = new Hashtable();

		Vector tmpVector = new Vector();
		tmpVector.add("Yearly report to the Basel Convention");
		delivery.put("http://rod.eionet.eu.int/schema.rdf#obligation", tmpVector);
		tmpVector = new Vector();
		tmpVector.add("Reportnet Delivery");
		delivery.put("http://www.w3.org/1999/02/22-rdf-syntax-ns#type", tmpVector);

		tmpVector = new Vector();
		tmpVector.add("http://cdr.eionet.europa.eu/at/un/colqluv2q/envqluwjg");
		delivery.put("http://purl.org/dc/elements/1.1/identifier", tmpVector);

		tmpVector = new Vector();
		tmpVector.add("2006-01-16T13:46:40Z");
		delivery.put("http://purl.org/dc/elements/1.1/date", tmpVector);

		tmpVector = new Vector();
		tmpVector.add("Austria");
		delivery.put("http://rod.eionet.eu.int/schema.rdf#locality", tmpVector);

		tmpVector = new Vector();
		tmpVector.add("Yearly Report to the Basel Convention 2003");
		delivery.put("http://purl.org/dc/elements/1.1/title", tmpVector);

		tmpVector = new Vector();
		tmpVector.add("1992");
		delivery.put("http://purl.org/dc/elements/1.1/coverage", tmpVector);

		tmpVector = new Vector();
		tmpVector.add("en");
		delivery.put("http://purl.org/dc/elements/1.1/language", tmpVector);

		Map deliveriesMap = new Hashtable();
		deliveriesMap.put("http://cdr.eionet.europa.eu/at/un/colqluv2q/envqluwjg", delivery);
		Vector deliveries = new Vector(1);
		deliveries.add(deliveriesMap);

		statement = connection.createStatement();
		resultSet = statement.executeQuery("select PK_RA_ID from T_OBLIGATION where TITLE='PARCOM RECOMMENDATION 96/3 Concerning Best Available Techniques for the Manufacture of Suspension PVC from Vinyl Chloride Monomer'");
                // FIXME: Need some assert statements here
		resultSet.next();
		deliveryMySqlDao.saveDeliveries(new Integer(resultSet.getInt(1)), deliveries, new HashMap());

		resultSet = statement.executeQuery("select max(PK_DELIVERY_ID) from T_DELIVERY");
		resultSet.next();
                // FIXME: Need some assert statements here
		statement.executeUpdate("delete from T_DELIVERY where PK_DELIVERY_ID = " + resultSet.getInt(1));
                // FIXME: Need some assert statements here

	}

	/*
	 * Test method for
	 * 'eionet.rod.services.modules.db.dao.mysql.DeliveryMySqlDao.rollBackDeliveries(Integer)'
	 */
	public void testRollBackDeliveries() throws Exception {
		statement = connection.createStatement();
		resultSet = statement.executeQuery("select PK_RA_ID from T_OBLIGATION where TITLE='PARCOM RECOMMENDATION 96/3 Concerning Best Available Techniques for the Manufacture of Suspension PVC from Vinyl Chloride Monomer'");
		resultSet.next();
		deliveryMySqlDao.rollBackDeliveries(new Integer(resultSet.getInt(1)));
                // FIXME: Need some assert statements here

	}

	/*
	 * Test method for
	 * 'eionet.rod.services.modules.db.dao.mysql.DeliveryMySqlDao.commitDeliveries()'
	 */
	public void testCommitDeliveries() throws Exception {
		deliveryMySqlDao.commitDeliveries();
                // FIXME: Need some assert statements here

	}

	/*
	 * Test method for
	 * 'eionet.rod.services.modules.db.dao.mysql.DeliveryMySqlDao.backUpDeliveries()'
	 */
	public void testBackUpDeliveries() throws Exception {
		deliveryMySqlDao.backUpDeliveries();
                // FIXME: Need some assert statements here
	}

}
