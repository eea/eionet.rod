package eionet.rod.rdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.Properties;
import java.util.ResourceBundle;

import eionet.rdfexport.RDFExportService;
import eionet.rdfexport.RDFExportServiceImpl;
import eionet.rod.services.LogServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.util.sql.ConnectionUtil;

/**
 * @author Risto Alt
 *
 */
public class RdfExporter {
    protected static LogServiceIF logger = RODServices.getLogService();
    /**
     * main method.
     *
     * @param args
     *            - Command line arguments
     */
    public static void main(String... args) {
        if (args.length == 0) {
            //feedback to command line not debugging
            log("Missing argument what to import!", true);
            log("Usage: rdfExporter [table] [identifier]", true);

        } else {
            String table = null;
            String identifier = null;

            int i = 0;
            for (String arg : args) {
                if (i == 0) {
                    table = arg;
                } else if (i == 1) {
                    identifier = arg;
                }
                i++;
            }

            Connection conn = null;
            FileOutputStream fos = null;
            try {
                ResourceBundle props = ResourceBundle.getBundle("rdfexport");
                String dir = props.getString("files.dest.dir");

                File file = new File(dir, table + ".rdf");
                fos = new FileOutputStream(file);

                ConnectionUtil.setReturnSimpleConnection(true);
                conn = ConnectionUtil.getConnection();

                Properties properties = new Properties();
                properties.load(RdfExporter.class.getClassLoader().getResourceAsStream("rdfexport.properties"));
                RDFExportService rdfExportService = new RDFExportServiceImpl(new PrintStream(fos), conn, properties);
                rdfExportService.exportTable(table, identifier);

                //feedback to command line user
                log("Successfully exported to: " + dir + "/" + table + ".rdf", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally{
                try {
                    if (fos != null){
                        fos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ConnectionUtil.closeConnection(conn);
            }
        }
    }

    /**
     * Give feedback to command line user and add a row to the log file if called by cron.
     * @param str String to be logged.
     * @param error if true logger.error() is called otherwise logger.debug()
     */
    private static void log(String str, boolean error) {
        System.out.println(str);
        if (error) {
            logger.error(str);
        } else {
            logger.debug(str);
        }
    }
}
