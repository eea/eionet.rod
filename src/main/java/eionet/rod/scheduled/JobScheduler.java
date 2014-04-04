package eionet.rod.scheduled;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.text.ParseException;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Common scheduler for scheduled jobs.
 * @author  Kaido Laine
 */
public class JobScheduler implements ServletContextListener {

    /**
     * default interval for jobs if not defined in the props file.
     * 24h.
     */
    static final long DEFAULT_SCHEDULE_INTERVAL = 24 * 60 * 60 * 1000;

    /** local logger. */
    private static final Log LOGGER = LogFactory.getLog(JobScheduler.class);

    /** Quarz scheduler instance. */
    private static Scheduler quartzScheduler = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            quartzScheduler = StdSchedulerFactory.getDefaultScheduler();
            quartzScheduler.start();
        } catch (SchedulerException e) {
            LOGGER.fatal("Error initializing scheduler " + e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (quartzScheduler != null) {
            try {
                quartzScheduler.shutdown();
            } catch (SchedulerException e) {
                LOGGER.warn("Error shutting down scheduler " + e);
            }
        }
    }

    /**
     * Schedules an interval job.
     * @param repeatInterval repeat interval in millis
     * @param jobDetails job details
     * @throws SchedulerException if scheduling fails
     */
    public static synchronized void scheduleIntervalJob(long repeatInterval, JobDetail jobDetails) throws  SchedulerException {

        SimpleTrigger trigger = newTrigger()
                .withIdentity(jobDetails.getKey().getName(), jobDetails.getKey().getGroup())
                .withSchedule(simpleSchedule()
                        .withIntervalInMilliseconds(repeatInterval)
                        .repeatForever())
                .build();

        quartzScheduler.scheduleJob(jobDetails, trigger);
    }

    /**
     * Schedules a job based on crontab format.
     * @param cronExpression crontab time expression
     * @param jobDetails job details
     * @throws SchedulerException if scheduling fails
     * @throws ParseException if crontab format is incorrect
     */
    public static synchronized void scheduleCronJob(String cronExpression, JobDetail jobDetails) throws SchedulerException,
            ParseException {

        CronTrigger trigger = newTrigger().withIdentity(jobDetails.getKey().getName(), jobDetails.getKey().getGroup())
                .withSchedule(cronSchedule(cronExpression)).forJob(jobDetails).build();

        quartzScheduler.scheduleJob(jobDetails, trigger);
    }

}
