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

import eionet.rod.countrysrv.Extractor;
import eionet.rod.countrysrv.ExtractorConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Extractor scheduled job.
 *
 * @author Kaido Laine
 */
public class ExtractorJob extends AbstractScheduledJob implements ServletContextListener {

    /**
     * Extractor takes numeric arguments.
     */
    private static final Map<String, Integer> JOBARGUMENTS = new HashMap<String, Integer>(5);

    /**
     * init hash constant.
     */
    static {
        JOBARGUMENTS.put("all", 0);
        JOBARGUMENTS.put("deliveries", 1);
        JOBARGUMENTS.put("roles", 2);
        JOBARGUMENTS.put("parameters", 3);
    }

    /**
     * Class logger.
     */
    private static final Log LOGGER = LogFactory.getLog(ExtractorJob.class);

    @Override
    protected String getName() {
        return "extractor";
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobDataMap jData = jobExecutionContext.getJobDetail().getJobDataMap();
        String mode = "all";
        if (jData.containsKey("mode")) {
            mode = String.valueOf(jobExecutionContext.getJobDetail().getJobDataMap().get("mode"));
        }

        if (JOBARGUMENTS.containsKey(mode)) {
            LOGGER.info("Extractor starts, mode=" + mode);
            Extractor.execute(JOBARGUMENTS.get(mode), ExtractorConstants.SYSTEM_USER);
            LOGGER.info("Extractor finished, mode=" + mode);
        } else {
            LOGGER.error("Unknown mode for extractor: " + mode);
        }


    }
}
