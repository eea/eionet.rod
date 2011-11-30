/**
 *
 */
package eionet.rod.rdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.ResourceBundle;

import eionet.rod.util.sql.ConnectionUtil;

/**
 * @author Risto Alt
 *
 */
public class RdfExporter {

    /**
     * main method.
     *
     * @param args - Command line arguments
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

            try {
                ResourceBundle props = ResourceBundle.getBundle("rdfexport");
                String dir = props.getString("files.dest.dir");

                File file = new File(dir, table + ".rdf");
                FileOutputStream fos = new FileOutputStream(file);

                ConnectionUtil.setReturnSimpleConnection(true);
                Connection con = ConnectionUtil.getConnection();
                GenerateRDF genRdf = new GenerateRDF(new PrintStream(fos), con);
                genRdf.exportTable(table, identifier);
                genRdf.close();
                fos.close();

                System.out.println("Successfully exported to: " + dir + "/" + table + ".rdf");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
