package eionet.rod.scheduled;

import eionet.rod.DeadlineCalc;
import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static org.quartz.JobBuilder.newJob;


/**
 * Scheduled job for deadlinecalc.
 *
 * @author Kaido Laine
 */
public class DeadlineCalcJob implements ServletContextListener, Job {

    /**
     * Class logger.
     */
    private static Log LOGGER = LogFactory.getLog(DeadlineCalcJob.class);
    /**
     * Job running interval in ms.
     */
    private static long interval;


    /**
     * Property value in props file.
     * can be cron expression or interval expression
     */
    private static String intervalPrpValue;


    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOGGER.debug(getClass().getName() + " context destroyed.");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        Class<DeadlineCalcJob> clazz = DeadlineCalcJob.class;
        JobDetail jobDetails = newJob(clazz).withIdentity(clazz.getSimpleName(), clazz.getName()).build();

        try {

            intervalPrpValue = RODServices.getFileService().getStringProperty(FileServiceIF.DEADLINECALC_JOB_INTERVAL);

            if (CronExpression.isValidExpression(intervalPrpValue)) {
                JobScheduler.scheduleCronJob(intervalPrpValue, jobDetails);
                LOGGER.info("DeadlineCalc job started crontab " + intervalPrpValue);
            } else {
                interval = RODServices.getFileService().getTimePropertyMilliseconds(FileServiceIF.DEADLINECALC_JOB_INTERVAL,
                        JobScheduler.DEFAULT_SCHEDULE_INTERVAL);
                JobScheduler.scheduleIntervalJob(interval, jobDetails);
                LOGGER.info("DeadlineCalc job started with interval " + interval + " ms");
            }

        } catch (Exception e) {
            LOGGER.error("Failed to initialize DeadlinesCalc job: " + e);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("Starting to update deadlines.");
        DeadlineCalc.execute();
        LOGGER.info("Deadlines updated.");

    }
}
