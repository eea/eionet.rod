package eionet.rod.services.modules.db.dao.mysql;

import eionet.rod.services.modules.db.dao.IAclDao;
import eionet.rod.services.modules.db.dao.IClientDao;
import eionet.rod.services.modules.db.dao.IDeliveryDao;
import eionet.rod.services.modules.db.dao.IDifferencesDao;
import eionet.rod.services.modules.db.dao.IGenericDao;
import eionet.rod.services.modules.db.dao.IHistoricDeadlineDao;
import eionet.rod.services.modules.db.dao.IHistoryDao;
import eionet.rod.services.modules.db.dao.IIssueDao;
import eionet.rod.services.modules.db.dao.IObligationDao;
import eionet.rod.services.modules.db.dao.IRoleDao;
import eionet.rod.services.modules.db.dao.ISourceDao;
import eionet.rod.services.modules.db.dao.ISpatialDao;
import eionet.rod.services.modules.db.dao.ISpatialHistoryDao;
import eionet.rod.services.modules.db.dao.IUndoDao;
import eionet.rod.services.modules.db.dao.IAnalysisDao;
import eionet.rod.services.modules.db.dao.RODDaoFactory;

public class MySqlDaoFactory extends RODDaoFactory {

	public MySqlDaoFactory() {
	}

	private IClientDao clientDao = new ClientMySqlDao();

	private IDeliveryDao deliveryDao = new DeliveryMySqlDao();

	private IDifferencesDao differencesDao = new DifferencesMySqlDao();

	private IGenericDao genericlDao = new GenericMySqlDao();

	private IHistoricDeadlineDao historicDeadlineDao = new HistoricDeadlineMySqlDao();

	private IHistoryDao historyDao = new HistoryMySqlDao();

	private IIssueDao issueDao = new IssueMySqlDao();

	private IObligationDao obligationDao = new ObligationMySqlDao();

	private IRoleDao roleDao = new RoleMySqlDao();

	private ISourceDao sourceDao = new SourceMySqlDao();

	private ISpatialDao spatialDao = new SpatialMySqlDao();

	private ISpatialHistoryDao spatialHistoryDao = new SpatialHistoryMySqlDao();

	private IUndoDao undoDao = new UndoMySqlDao();
	
	private IAclDao aclDao = new AclMySqlDao();
	
	private IAnalysisDao analysisDao = new AnalysisMySqlDao();

	public IClientDao getClientDao() {
		return clientDao;
	}

	public IDeliveryDao getDeliveryDao() {
		return deliveryDao;
	}

	public IDifferencesDao getDifferencesDao() {
		return differencesDao;
	}

	public IGenericDao getGenericlDao() {
		return genericlDao;
	}

	public IHistoricDeadlineDao getHistoricDeadlineDao() {
		return historicDeadlineDao;
	}

	public IHistoryDao getHistoryDao() {
		return historyDao;
	}

	public IIssueDao getIssueDao() {
		return issueDao;
	}

	public IObligationDao getObligationDao() {
		return obligationDao;
	}

	public IRoleDao getRoleDao() {
		return roleDao;
	}

	public ISourceDao getSourceDao() {
		return sourceDao;
	}

	public ISpatialDao getSpatialDao() {
		return spatialDao;
	}

	public ISpatialHistoryDao getSpatialHistoryDao() {
		return spatialHistoryDao;
	}

	public IUndoDao getUndoDao() {
		return undoDao;
	}

	public IAclDao getAclDao() {
		return aclDao;
	}
	
	public IAnalysisDao getAnalysisDao() {
		return analysisDao;
	}
}
