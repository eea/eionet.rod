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

import eionet.rod.rdf.RdfExporter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.servlet.ServletContextListener;

/**
 * RDF exporter automatic job.
 * @author Kaido Laine
 */
public class RdfExporterJob extends  AbstractScheduledJob implements ServletContextListener {

    /**
     * Class logger.
     */
    private static Log LOGGER = LogFactory.getLog(RdfExporterJob.class);

    @Override
    protected String getName() {
        return "rdfexporter";
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jData = jobExecutionContext.getJobDetail().getJobDataMap();
        String tableName = jData.getString("table");
        String identifier = jData.getString("identifier");

        //RDFExpoter supports null as identifier:
        if (StringUtils.isBlank(identifier)) {
            identifier = null;
        }

        if (StringUtils.isNotBlank(tableName)) {
            LOGGER.info("RDF exporter job starting for table: " + tableName + " identifier: " + identifier);
            RdfExporter.execute(tableName, identifier);
            LOGGER.info("RDF exporter job finished");
        } else {
            LOGGER.error("RDF exporter job cannot be started. Table name is not specified");
        }
    }
}
