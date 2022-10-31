package ConfigLoader;

/**
 * A database configuration object.
 */
public class DBData {
    /** The database name. */
    private String name;
    /** The database location. */
    private String ip;
    /** The database port. */
    private String port;

    /**
     * Constructs a new DBData instance with default values.
     */
    protected DBData() {
        this.name = "default";
        this.ip = "127.0.0.1";
        this.port = "3331";
    }

    /**
     * Constructs a new DBData instance with given values.
     * @param name String - The database name.
     * @param ip String - The database ip.
     * @param port String - The database port.
     */
    protected DBData(String name, String ip, String port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    public String toString() {
        return "[Name = " + this.name + ", ip = " + this.ip + "]";
    }

    /**
     * The database name getter.
     * @return String - The database name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * The database ip getter.
     * @return String - The database ip.
     */
    public String getIP () {
        return this.ip;
    }

    /**
     * The database port getter.
     * @return String - The database port.
     */
    public String getPort() {
        return this.port;
    }

}
