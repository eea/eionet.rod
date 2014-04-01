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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronExpression;
import org.quartz.Job;
import org.quartz.JobDetail;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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
    private static Log LOGGER = LogFactory.getLog(AbstractScheduledJob.class);
    /**
     * Job running interval in ms.
     */
    private long interval;

    /**
     * Property value in props file.
     * can be cron expression or interval expression
     */
    private String intervalPrpValue;

    /**
     * Job Name.
     *
     * @return current job name
     */
    protected abstract String getName();


    @Override
    public void contextInitialized(ServletContextEvent sce) {

        String propName = getName() + ".job.interval";

        Class<? extends AbstractScheduledJob> clazz = this.getClass();

        JobDetail jobDetails = newJob(clazz).withIdentity(clazz.getSimpleName(), clazz.getName()).build();

        try {

            intervalPrpValue = RODServices.getFileService().getStringProperty(propName);

            if (CronExpression.isValidExpression(intervalPrpValue)) {
                JobScheduler.scheduleCronJob(intervalPrpValue, jobDetails);
                LOGGER.info(getName() + " job started crontab " + intervalPrpValue);
            } else {
                interval = RODServices.getFileService().getTimePropertyMilliseconds(propName,
                        JobScheduler.DEFAULT_SCHEDULE_INTERVAL);
                JobScheduler.scheduleIntervalJob(interval, jobDetails);
                LOGGER.info(getName() + " job started with interval " + interval + " ms");
            }

        } catch (Exception e) {
            LOGGER.error("Failed to initialize job: " + getName() + ": \n" + e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.debug(getName() + " context destroyed.");
    }
}
