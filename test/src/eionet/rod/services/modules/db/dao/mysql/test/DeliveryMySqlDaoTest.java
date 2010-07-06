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
		resultSet = statement.executeQuery("select count(*) from T_DELIVERY where STATUS=0 AND FK_RA_ID="+resultSet.getInt(1));
		resultSet.next();
		assertEquals(0, resultSet.getInt(1));

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
		deliveryMySqlDao.commitDeliveries(new HashMap());
		statement = connection.createStatement();
		resultSet = statement.executeQuery("select count(*) from T_DELIVERY where STATUS=0");
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
		resultSet = statement.executeQuery("select count(*) from T_DELIVERY where STATUS=1");
		resultSet.next();
		assertEquals(0, resultSet.getInt(1));
	}

}
