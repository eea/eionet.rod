package eionet.rod.util.sql;

/**
 * 
 * @author heinljab
 * 
 */
public class DataSourceException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public DataSourceException() {
        super();
    }

    /**
     * 
     * @param message
     */
    public DataSourceException(String message) {
        super(message);
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
