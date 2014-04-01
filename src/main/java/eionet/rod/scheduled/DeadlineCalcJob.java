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

import eionet.rod.DeadlineCalc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.servlet.ServletContextListener;


/**
 * Scheduled job for deadlinecalc.
 *
 * @author Kaido Laine
 */
public class DeadlineCalcJob extends AbstractScheduledJob implements ServletContextListener {

    /**
     * Class logger.
     */
    private static Log LOGGER = LogFactory.getLog(DeadlineCalcJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("Starting to update deadlines.");
        DeadlineCalc.execute();
        LOGGER.info("Deadlines updated.");
    }

    @Override
    protected String getName() {
        return "deadlinecalc";
    }
}
