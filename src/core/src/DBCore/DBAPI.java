package DBCore;

import Utils.Logger;

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
    /** The list of precompiled prepared statements. */
    private final PreparedStatement [] statements;

    /**
     * Constructs a new database API instance.
     */
    public DBAPI() {
        this.core = new DBCore();
        this.logger = new Logger();
        this.statements = new PreparedStatement[10];
    }

    /**
     * Pre-compiles the prepared statements to be used later. This is to increase performance, as the statements
     * need to be compiled only once.
     */
    private void precompileStatements() {
        try {
            this.statements[0] = this.core.getDbConnection()
                    .prepareStatement("SELECT COUNT(id) AS idcount FROM parcel WHERE id = ?");
            this.statements[1] = this.core.getDbConnection()
                    .prepareStatement("SELECT COUNT(username) AS uscount FROM customer WHERE username = ?");
            // ... more statements.
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
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
        this.precompileStatements();
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

    /**
     * Gets the number of identical parcel IDs from database.
     * @param parcelID String - The parcel ID to check.
     * @return int - The number of identical IDs.
     */
    public int getCountOfIdenticalParcelIDs(String parcelID) {
        int count = -1;
        try {
            this.statements[0].setString(0, parcelID);
            ResultSet rs = this.statements[0].executeQuery();
            count = rs.getInt("idcount");
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Gets the number of identical usernames from database.
     * @param userID String - The username to check.
     * @return int - The number of identical usernames.
     */
    public int getCountOfIdenticalUsernames(String userID) {
        int count = -1;
        try {
            this.statements[1].setString(0, userID);
            ResultSet rs = this.statements[1].executeQuery();
            count = rs.getInt("uscount");
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return count;
    }

    // TODO: Remove following methods.

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
