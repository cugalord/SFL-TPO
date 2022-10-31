package ConfigLoader;

/**
 * A database configuration object.
 */
public class DBData {
    /** The database name. */
    private String name;
    /** The database location. */
    private String ip;

    /**
     * Constructs a new DBData instance with default values.
     */
    protected DBData() {
        this.name = "default";
        this.ip = "127.0.0.1";
    }

    /**
     * Constructs a new DBData instance with given values.
     * @param name The database name.
     * @param ip The database ip.
     */
    protected DBData(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    /**
     * Converts object to String.
     * @return the String representation of the object.
     */
    public String toString() {
        return "[Name = " + this.name + ", ip = " + this.ip + "]";
    }

    /**
     * The database name getter.
     * @return The database name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * The database ip getter.
     * @return The database ip.
     */
    public String getIP () {
        return this.ip;
    }
}
