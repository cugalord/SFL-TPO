package DBCore;

import Logger.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The public facing database access API.
 */
public class DBAPI {
    /** The database connection management core functionality. */
    private final DBCore core;
    /** The logger. */
    private final Logger logger;
    /** The current username. */
    private String currentUser;

    /**
     * Constructs a new database API instance.
     */
    public DBAPI() {
        this.core = new DBCore();
        this.logger = new Logger();
    }

    /**
     * Logs into the database.
     * @param username String - The username.
     * @param password String - The password.
     */
    public void login(String username, String password) {
        this.logger.log("DBAPI:login: The user " + username + " is trying to log in.", Logger.MessageType.LOG);
        this.currentUser = username;
        this.core.login(username, password);
    }

    /**
     * Logs out of the database.
     */
    public void logout() {
        this.logger.log("DBAPI:logout: The user " + this.currentUser + " is trying to log out.",
                Logger.MessageType.LOG);
        this.core.logout();
        this.currentUser = "";
    }

    /**
     * Getter for the flag that signifies if the connection is established.
     * @return Boolean - True if connection is established, false otherwise.
     */
    public boolean isConnectionEstablished() {
        return this.core.isConnectionEstablished();
    }

    // PREPARED STATEMENTS

    public void testPreparedStmtUpdate() {
        try {
            PreparedStatement stmt = this.core.getDbConnection().prepareStatement("insert into test values(?,?)");
            stmt.setInt(1, 3);
            stmt.setString(2, "klm");
            int i = stmt.executeUpdate();
            System.out.println(i + " records inserted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void testPreparedStmtSelect() {
        try {
            PreparedStatement stmt = this.core.getDbConnection().prepareStatement("select * from test");
            //stmt.setInt(1, 12); <- this breaks the query with no error!
            ResultSet rs = stmt.executeQuery();
            int i = 0;
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2));
                i++;
            }
            System.out.println(i + " records queried.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void testPreparedStmtUpdate2() {
        try {
            PreparedStatement stmt = this.core.getDbConnection().prepareStatement("insert into test2(name) values(?)");
            //stmt.setInt(1, 3);
            stmt.setString(1, "abc");
            int i = stmt.executeUpdate();
            System.out.println(i + " records inserted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void testPreparedStmtSelect2() {
        try {
            PreparedStatement stmt = this.core.getDbConnection().prepareStatement("select * from test2");
            ResultSet rs = stmt.executeQuery();
            int i = 0;
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2));
                i++;
            }
            System.out.println(i + " records queried.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
