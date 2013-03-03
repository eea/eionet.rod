package eionet.rod.rdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.Properties;
import java.util.ResourceBundle;

import eionet.rdfexport.RDFExportService;
import eionet.rdfexport.RDFExportServiceImpl;
import eionet.rod.util.sql.ConnectionUtil;

/**
 * @author Risto Alt
 *
 */
public class RdfExporter {

    /**
     * main method.
     *
     * @param args
     *            - Command line arguments
     */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Missing argument what to import!");
            System.out.println("Usage: rdfExporter [table] [identifier]");
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

                System.out.println("Successfully exported to: " + dir + "/" + table + ".rdf");
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
}
