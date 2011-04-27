package eionet.rod.services.modules.db.dao;

import eionet.rod.services.modules.db.dao.mysql.MySqlDaoFactory;


public abstract class RODDaoFactory {
       public static final int MYSQL_DB = 1;

       private static RODDaoFactory mySqlDaoFactory = new MySqlDaoFactory();

       public static RODDaoFactory getDaoFactory(int whichFactory) {
          switch (whichFactory) {
             case MYSQL_DB:
                return mySqlDaoFactory;
             default:
                return null;
          }
       }

       public abstract IClientDao getClientDao();
       public abstract IDeliveryDao getDeliveryDao();
       public abstract IDifferencesDao getDifferencesDao();
       public abstract IGenericDao getGenericlDao();
       public abstract IHelpDao getHelpDao();
       public abstract IHistoricDeadlineDao getHistoricDeadlineDao();
       public abstract IHistoryDao getHistoryDao();
       public abstract IIssueDao getIssueDao();
       public abstract IObligationDao getObligationDao();
       public abstract IRoleDao getRoleDao();
       public abstract ISourceDao getSourceDao();
       public abstract ISpatialDao getSpatialDao();
       public abstract ISpatialHistoryDao getSpatialHistoryDao();
       public abstract IUndoDao getUndoDao();
       public abstract IAclDao getAclDao();
       public abstract IAnalysisDao getAnalysisDao();


}
