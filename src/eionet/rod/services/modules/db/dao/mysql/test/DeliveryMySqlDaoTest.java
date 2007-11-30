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

		// Check how many deliveries there are in the table before we do anything
		resultSet = statement.executeQuery("SELECT COUNT(*) FROM T_DELIVERY");
		resultSet.next();
		assertEquals(4, resultSet.getInt(1));

		// Get the PK_RA_ID
		resultSet = statement.executeQuery("select PK_RA_ID from T_OBLIGATION where TITLE='PARCOM RECOMMENDATION 96/3 Concerning Best Available Techniques for the Manufacture of Suspension PVC from Vinyl Chloride Monomer'");
		resultSet.next();
		assertEquals(514, resultSet.getInt("PK_RA_ID"));
		
		deliveryMySqlDao.saveDeliveries(new Integer(resultSet.getInt(1)), deliveries, new HashMap());

		// Check how many deliveries there are in the table after the save
		resultSet = statement.executeQuery("SELECT COUNT(*) FROM T_DELIVERY");
		resultSet.next();
		assertEquals(5, resultSet.getInt(1));

		// Delete the last delivery - not really necessary
		resultSet = statement.executeQuery("select max(PK_DELIVERY_ID) from T_DELIVERY");
		resultSet.next();
<<<<<<< .mine
		assertEquals(13499168, resultSet.getInt(1));
		
		int ret = statement.executeUpdate("delete from T_DELIVERY where PK_DELIVERY_ID = " + resultSet.getInt(1));
		assertEquals(1, ret);

=======
		statement.executeUpdate("delete from T_DELIVERY where PK_DELIVERY_ID = " + resultSet.getInt(1));
>>>>>>> .r4302
	}

	/*
	 * Test method for
	 * 'eionet.rod.services.modules.db.dao.mysql.DeliveryMySqlDao.rollBackDeliveries(Integer)'
	 */
	/*
	 * FIXME: We don't do any inserts into the T_DELIVERY file, so there is nothing to roll back
	 */
	public void testRollBackDeliveries() throws Exception {
		statement = connection.createStatement();
		resultSet = statement.executeQuery("select PK_RA_ID from T_OBLIGATION where TITLE='PARCOM RECOMMENDATION 96/3 Concerning Best Available Techniques for the Manufacture of Suspension PVC from Vinyl Chloride Monomer'");
		resultSet.next();
		assertEquals(514, resultSet.getInt(1));
		deliveryMySqlDao.rollBackDeliveries(new Integer(resultSet.getInt(1)));
<<<<<<< .mine
		resultSet = statement.executeQuery("select count(PK_DELIVERY_ID) from T_DELIVERY where STATUS=0 AND FK_RA_ID="+resultSet.getInt(1));
		resultSet.next();
		assertEquals(0, resultSet.getInt(1));
=======
>>>>>>> .r4302

		// Check how many deliveries there are in the table after the roll back
		resultSet = statement.executeQuery("SELECT COUNT(*) FROM T_DELIVERY");
		resultSet.next();
		assertEquals(4, resultSet.getInt(1));
	}

	/*
	 * Test method for
	 * 'eionet.rod.services.modules.db.dao.mysql.DeliveryMySqlDao.commitDeliveries()'
	 */
	public void testCommitDeliveries() throws Exception {
		deliveryMySqlDao.commitDeliveries();
		statement = connection.createStatement();
		resultSet = statement.executeQuery("select count(PK_DELIVERY_ID) from T_DELIVERY where STATUS=0");
		resultSet.next();
		assertEquals(0, resultSet.getInt(1));	

	}

	/*
	 * Test method for
	 * 'eionet.rod.services.modules.db.dao.mysql.DeliveryMySqlDao.backUpDeliveries()'
	 */
	public void testBackUpDeliveries() throws Exception {
		deliveryMySqlDao.backUpDeliveries();
		statement = connection.createStatement();
		resultSet = statement.executeQuery("select count(PK_DELIVERY_ID) from T_DELIVERY where STATUS=1");
		resultSet.next();
		assertEquals(0, resultSet.getInt(1));
	}

}
