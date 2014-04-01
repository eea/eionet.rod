package eionet.rod.scheduled;

import eionet.rod.DeadlineCalc;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import static org.quartz.JobBuilder.*;


/**
 * Scheduled job for deadlinecalc.
 *
 * @author Kaido Laine
 */
public class DeadlineCalcJob implements ServletContextListener, StatefulJob {

    /** Class logger. */
    private static Log LOGGER = LogFactory.getLog(DeadlineCalcJob.class);
    /**
     * Job running interval in ms.
     */
    private static long interval;


    /**
     * Crontab entry for the job.
     */
    private static String cronTab;


    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        Class<DeadlineCalcJob> clazz = DeadlineCalcJob.class;
        JobDetail jobDetails = newJob(clazz).withIdentity(clazz.getSimpleName(), clazz.getName()).build();

        try {
            try {
                cronTab = RODServices.getFileService().getStringProperty(FileServiceIF.DEADLINECALC_JOB_CRONTAB);
            } catch (ServiceException se) {
                LOGGER.info("Crontab entry not defined in the props File");
            }

            if (StringUtils.isNotBlank(cronTab)) {
                JobScheduler.scheduleCronJob(cronTab, jobDetails);
                LOGGER.info("DeadlineCalc job started crontab " + cronTab);
            } else {
                interval = RODServices.getFileService().getTimePropertyMilliseconds(FileServiceIF.DEADLINECALC_JOB_INTERVAL,
                        4320000L);

                JobScheduler.scheduleIntervalJob(interval, jobDetails);
                LOGGER.info("DeadlineCalc job started with interval " + interval);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to initialize DeadlinesCalc job: " + e);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("Starting to update deadlines.");
        DeadlineCalc.main(null);
        LOGGER.info("Deadlines updated.");

    }
}
