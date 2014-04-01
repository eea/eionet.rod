package eionet.rod.scheduled;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * Listener class for ROD jobs.
 * @author  Kaido Laine
 */
public class RODJobListener implements JobListener {

    private  String name;

    /**
     * initializes the listener.
     *
     * @param jobName given name for the job
     */
    RODJobListener(String jobName) {
        this.name = jobName;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {

    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {

    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {

    }
}
