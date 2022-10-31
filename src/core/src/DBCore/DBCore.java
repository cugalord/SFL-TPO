package DBCore;

import ConfigLoader.ConfigLoader;
import ConfigLoader.DBData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The internal database connection implementation.
 */
class DBCore {
    /** The database config loader. */
    private final ConfigLoader cfgLoader;
    /** The database connection. */
    private Connection dbConnection;
    /** The MySQL connection string. */
    private String url = "jdbc:mysql://<ip>:<port>/<dbname>";
    /** The username to connect with. */
    private String username = "";
    /** The password to connect with. */
    private String password = "";
    /** The flag that signifies if the connection is currently established. */
    private boolean connectionEstablished;

    /**
     * Constructs a new database core instance.
     */
    DBCore() {
        this.cfgLoader = new ConfigLoader();
        this.connectionEstablished = true;
    }

    /**
     * Sets the database url parameters.
     * @throws Exception If database info could not be retrieved.
     */
    private void setUrlParameters() throws Exception {
        cfgLoader.load();
        DBData data = cfgLoader.fetchData();

        if (data == null) {
            throw new Exception("DBCore:setUrlParameters: Database data is null.");
        }

        this.url = url.replace("<ip>", data.getIP());
        this.url = url.replace("<port>", data.getPort());
        this.url = url.replace("<dbname>", data.getName());
    }

    /**
     * Sets the database login parameters.
     * @param username String - The username.
     * @param password String - The password.
     */
    private void setLoginParameters(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Initializes the database connection.
     * @throws java.sql.SQLException
     */
    private void init() {
        try {
            this.dbConnection = DriverManager.getConnection(this.url, this.username, this.password);
            this.connectionEstablished = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs into the database with the login parameters and url. This method encapsulates setting of parameters
     * and initialization.
     * @throws Exception If setUrlParameters fails.
     */
    void login(String username, String password) {
        try {
            this.setUrlParameters();
            this.setLoginParameters(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.init();
    }

    /**
     * Closes the current database connection.
     * @throws java.sql.SQLException
     */
    void logout() {
        try {
            this.url = "jdbc:mysql://<ip>:<port>/<dbname>";
            this.username = "";
            this.password = "";
            this.connectionEstablished = false;

            this.dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Database connection getter.
     * @return Connection - The database connection.
     */
    Connection getDbConnection() {
        return this.dbConnection;
    }

    /**
     * Getter for the flag that signifies if the connection is established.
     * @return Boolean - True if connection is established, false otherwise.
     */
    boolean isConnectionEstablished() {
        return this.connectionEstablished;
    }

}
