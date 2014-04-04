/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is WebROD
 *
 * The Initial Owner of the Original Code is European Environment
 * Agency. Portions created by Zero Technologies are Copyright
 * (C) European Environment Agency.  All Rights Reserved.
 *
 * Contributor(s): Kaido Laine
 */

package eionet.rod.scheduled;

import eionet.rod.services.RODServices;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronExpression;
import org.quartz.Job;
import org.quartz.JobDetail;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static org.quartz.JobBuilder.newJob;

/**
 * Abstract class for scheduled job.
 *
 * @author Kaido Laine
 */
public abstract class AbstractScheduledJob implements ServletContextListener, Job {

    /**
     * local logger.
     */
    private static final Log LOGGER = LogFactory.getLog(AbstractScheduledJob.class);

    /**
     * Job Name.
     *
     * @return current job name
     */
    protected abstract String getName();

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        String propName = getName() + ".job.interval";
        List<JobDetail> jobs = new ArrayList<JobDetail>(10);

        Class<? extends AbstractScheduledJob> clazz = this.getClass();

        try {

            String jobData = RODServices.getFileService().getStringProperty(getName() + ".job.data", "");
            if (StringUtils.isNotBlank(jobData)) {
                parseJobData(clazz, jobs, jobData);
            } else {
                JobDetail job = newJob(clazz).withIdentity(clazz.getSimpleName(), clazz.getName()).build();
                jobs.add(job);
            }

            String intervalPrpValue = RODServices.getFileService().getStringProperty(propName);

            for (JobDetail jobDetails : jobs) {

                if (CronExpression.isValidExpression(intervalPrpValue)) {
                    JobScheduler.scheduleCronJob(intervalPrpValue, jobDetails);
                    LOGGER.info(getName() + " job started crontab " + intervalPrpValue);
                } else {
                    long interval = RODServices.getFileService().getTimePropertyMilliseconds(propName,
                            JobScheduler.DEFAULT_SCHEDULE_INTERVAL);
                    JobScheduler.scheduleIntervalJob(interval, jobDetails);
                    LOGGER.info(getName() + " job started with interval " + interval + " ms");
                }
            }

        } catch (Exception e) {
            LOGGER.error("Failed to initialize job: " + getName() + ": \n" + e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.debug(getName() + " context destroyed.");
    }

    /**
     * Parses job params value from props file and adds to job data map.
     *
     * @param clazz   class which is used for building the job
     * @param jobs    list of jobs
     * @param jobData full String for several jobs in format key1=valueA;key2=valueB|key1=valueC;key2=valueD|...|key1=valueN
     */
    private void parseJobData(Class clazz, List<JobDetail> jobs, String jobData) {
        StringTokenizer differentJobs = new StringTokenizer(jobData, "|");
        while (differentJobs.hasMoreTokens()) {
            String jobParams = differentJobs.nextToken();
            JobDetail jobDetails = newJob(clazz).withIdentity(clazz.getSimpleName() + ":" + jobParams, clazz.getName()).build();
            StringTokenizer args = new StringTokenizer(jobParams, ";");
            while (args.hasMoreTokens()) {
                String arg = args.nextToken();
                String key = StringUtils.substringBefore(arg, "=");
                String value = StringUtils.substringAfter(arg, "=");
                jobDetails.getJobDataMap().put(key, value);
            }
            jobs.add(jobDetails);
        }

    }
}
