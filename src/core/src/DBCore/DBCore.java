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
    /** The MySQL connection string. */
    private String connectionString = "jdbc:mysql://<ip>:<port>/<dbname>";
    /** The username to connect with. */
    private String username = "";
    /** The password to connect with. */
    private String password = "";

    /**
     * Constructs a new database core instance.
     */
    DBCore() {
        this.cfgLoader = new ConfigLoader();
    }

    void setLoginParameters(String username, String password) {
        this.username = username;
        this.password = password;
    }

    void printHey() {
        System.out.println("Hey from core!");
    }
}
