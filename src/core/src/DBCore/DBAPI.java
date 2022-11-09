package DBCore;

import Data.Coordinates;
import Data.DataCount;
import Data.DataParcelCenter;
import Data.GeneralAddress;
import Utils.Logger;

import java.sql.*;

import java.util.ArrayList;

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
    /** The list of precompiled callable statements. */
    private final CallableStatement [] callables;

    /**
     * Constructs a new database API instance.
     */
    public DBAPI() {
        this.core = new DBCore();
        this.logger = new Logger();
        this.statements = new PreparedStatement[10];
        this.callables = new CallableStatement[10];
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
            this.statements[2] = this.core.getDbConnection()
                    .prepareStatement("SELECT COUNT(code) AS postCode FROM city WHERE code = ?");
            this.statements[3] = this.core.getDbConnection()
                    .prepareStatement("SELECT branch_id, latitude, longitude, country FROM parcel_center_locations");
            // ... more statements.
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
    }

    private void precompileCallables() {
        try {
            this.callables[0] = this.core.getDbConnection().prepareCall("CALL resolve_address(?, ?, ?, ?, ?)");
            // ... more callables.
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
        this.logger.log("The user " + username + " is trying to log in.", Logger.MessageType.LOG);
        this.currentUser = username;
        this.core.login(username, password);
        if (this.core.isConnectionEstablished()) {
            this.precompileStatements();
            this.precompileCallables();
        }
    }

    /**
     * Logs out of the database.
     */
    public void logout() {
        this.logger.log("The user " + this.currentUser + " is trying to log out.",
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
     * @return DataCount - Object representing the number of identical IDs.
     */
    public DataCount getCountOfIdenticalParcelIDs(String parcelID) {
        DataCount count = new DataCount(0, -1);
        try {
            this.statements[0].setString(1, parcelID);
            ResultSet rs = this.statements[0].executeQuery();
            rs.next();
            count = new DataCount(0, rs.getInt("idcount"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Gets the number of identical usernames from database.
     * @param username String - The username to check.
     * @return DataCount - Object representing the number of identical usernames.
     */
    public DataCount getCountOfIdenticalUsernames(String username) {
        DataCount count = new DataCount(0, -1);
        try {
            this.statements[1].setString(1, username);
            ResultSet rs = this.statements[1].executeQuery();
            rs.next();
            count = new DataCount(0,  rs.getInt("uscount"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Gets the number of identical post codes from database.
     * @param postCode String - The post code to check.
     * @return DataCount - Object representing the number of identical post codes.
     */
    public DataCount getCountOfIdenticalPostCodes(String postCode) {
        DataCount count = new DataCount(0, -1);
        try {
            this.statements[2].setString(1, postCode);
            ResultSet rs = this.statements[1].executeQuery();
            rs.next();
            count = new DataCount(0,  rs.getInt("uscount"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Gets the data of all parcel centers from database.
     * @return ArrayList<DataParcelCenter> - List of parcel center data objects.
     */
    public ArrayList<DataParcelCenter> getAllParcelCenterData() {
        ArrayList<DataParcelCenter> data = new ArrayList<>();
        try {
            ResultSet rs = this.statements[3].executeQuery();
            int i = 0;
            while (rs.next()) {
                data.add(new DataParcelCenter(
                        i,
                        rs.getString("branch_id"),
                        Double.parseDouble(rs.getString("latitude")),
                        Double.parseDouble(rs.getString("longitude")),
                        rs.getString("country")
                    ));
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    // CALLABLE STATEMENTS

    /**
     * Gets coordinates from a general address data object.
     * @param address GeneralAddress - The general address (post code, city name, country ISO code).
     * @return Coordinates - The coordinates of the address.
     */
    public Coordinates getCoordinatesFromAddress(GeneralAddress address) {
        Coordinates data = null;
        try {
            this.callables[0].setString("postcode", address.postCode);
            this.callables[0].setString("cityname", address.cityName);
            this.callables[0].setString("country", address.countryISO);
            this.callables[0].registerOutParameter("latitude", Types.VARCHAR);
            this.callables[0].registerOutParameter("longitude", Types.VARCHAR);
            this.callables[0].executeQuery();
            data = new Coordinates(
                    0,
                    Double.parseDouble(this.callables[0].getString("latitude")),
                    Double.parseDouble(this.callables[0].getString("longitude"))
                    );
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }
}
