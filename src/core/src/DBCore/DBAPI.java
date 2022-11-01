package DBCore;

/**
 * The public facing database access API.
 */
public class DBAPI {
    /** The database connection management core functionality. */
    private final DBCore core;

    /**
     * Constructs a new database API instance.
     */
    public DBAPI() {
        this.core = new DBCore();
    }

    /**
     * Logs into the database.
     * @param username String - The username.
     * @param password String - The password.
     */
    public void login(String username, String password) {
        this.core.login(username, password);
    }

    /**
     * Logs out of the database.
     */
    public void logout() {
        this.core.logout();
    }

    // PREPARED STATEMENTS

}
