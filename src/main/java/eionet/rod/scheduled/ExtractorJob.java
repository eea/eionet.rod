

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
