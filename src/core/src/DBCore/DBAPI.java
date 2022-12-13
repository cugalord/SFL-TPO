package DBCore;

import Data.*;
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
            this.callables[1] = this.core.getDbConnection().prepareCall("CALL user_info(?, ?, ?, ?)");
            this.callables[2] = this.core.getDbConnection().prepareCall("CALL get_jobs(?)");
            this.callables[3] = this.core.getDbConnection().prepareCall("CALL get_no_jobs(?, ?)");
            this.callables[4] = this.core.getDbConnection().prepareCall("CALL get_warehouse_parcel_info(?, ?, ? ,?)");
            this.callables[5] = this.core.getDbConnection().prepareCall("CALL get_jobs_filter_type(?, ?)");
            this.callables[6] = this.core.getDbConnection().prepareCall("CALL update_parcel_status(?, ?)");
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
                        new Coordinates(
                                0,
                                Double.parseDouble(rs.getString("latitude")),
                                Double.parseDouble(rs.getString("longitude"))
                        ),
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

    /**
     * Gets staff data from staff username.
     * @param username String - The staff username.
     * @return DataStaff - The staff data (name, surname, role name).
     */
    public DataStaff getStaffDataFromUsername(String username) {
        DataStaff data = null;
        try {
            this.callables[1].setString("username", username);
            this.callables[1].registerOutParameter("u_name", Types.VARCHAR);
            this.callables[1].registerOutParameter("u_surname", Types.VARCHAR);
            this.callables[1].registerOutParameter("u_role", Types.VARCHAR);
            this.callables[1].executeQuery();
            data = new DataStaff(
                    0,
                    username,
                    this.callables[1].getString("u_name"),
                    this.callables[1].getString("u_surname"),
                    this.callables[1].getString("u_role")
            );
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets all jobs assigned to a given employee.
     * @param username String - The staff username.
     * @return ArrayList<DataJob> - The jobs of employee.
     */
    public ArrayList<DataJob> getJobsOfStaff(String username) {
        ArrayList<DataJob> data = new ArrayList<>();
        try {
            this.callables[2].setString("username", username);
            ResultSet rs = this.callables[2].executeQuery();
            int i = 0;
            while (rs.next()) {
                int currentJobID = rs.getInt("job_id");
                // Check if this job already exists.
                DataJob job = data.stream().filter(jobd -> jobd.jobID == currentJobID).findAny().orElse(null);
                // If the job doesn't exist, create it.
                if (job == null) {
                    data.add(new DataJob(
                            i,
                            currentJobID,
                            null,
                            null,
                            -1,
                            -1,
                            username
                    ));
                }
                // If the job already exists, add parcel to its list.
                else {
                    String currentParcelID = rs.getString("parcel_id");
                    if (job.parcelIDs.stream().filter(id -> id.equals(currentParcelID)).findAny().orElse(null) == null) {
                        job.parcelIDs.add(currentParcelID);
                    }
                }
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the number of uncompleted jobs assigned to a given employee.
     * @param username String - The staff username.
     * @return DataCount - The number of uncompleted jobs.
     */
    public DataCount getNumberOfUncompletedJobOfStaff(String username) {
        DataCount data = null;
        try {
            this.callables[3].setString("username", username);
            this.callables[3].registerOutParameter("no_of_jobs", Types.INTEGER);
            this.callables[3].executeQuery();
            data = new DataCount(0, this.callables[3].getInt("no_of_jobs"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the numbers of parcels that belong to the warehouse of a given warehouse manager.
     * @param username String - The warehouse manager username.
     * @return DataCount[] - The numbers of parcels (all, pending, processed).
     */
    public DataCount[] getWarehouseManagerWarehouseParcelInfo(String username) {
        DataCount[] data = {null, null, null};
        try {
            this.callables[4].setString("username", username);
            this.callables[4].registerOutParameter("no_of_all_parcels", Types.INTEGER);
            this.callables[4].registerOutParameter("no_of_pending_parcels", Types.INTEGER);
            this.callables[4].registerOutParameter("no_of_processed_parcels", Types.INTEGER);
            this.callables[4].executeQuery();
            data[0] = new DataCount(0, this.callables[4].getInt("no_of_all_parcels"));
            data[1] = new DataCount(1, this.callables[4].getInt("no_of_pending_parcels"));
            data[2] = new DataCount(2, this.callables[4].getInt("no_of_processed_parcels"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets all jobs assigned to a given employee with given type.
     * @param username String - The staff username.
     * @param type String - The job type.
     * @return ArrayList<DataJob> - The jobs of employee.
     */
    public ArrayList<DataJob> getJobsOfStaffByType(String username, String type) {
        ArrayList<DataJob> data = new ArrayList<>();
        try {
            this.callables[5].setString("username", username);
            this.callables[5].setString("type", type);
            ResultSet rs = this.callables[5].executeQuery();
            int i = 0;
            while (rs.next()) {
                int currentJobID = rs.getInt("job_id");
                // Check if this job already exists.
                DataJob job = data.stream().filter(jobd -> jobd.jobID == currentJobID).findAny().orElse(null);
                // If the job doesn't exist, create it.
                if (job == null) {
                    data.add(new DataJob(
                            i,
                            currentJobID,
                            null,
                            null,
                            -1,
                            -1,
                            username
                    ));
                }
                // If the job already exists, add parcel to its list.
                else {
                    String currentParcelID = rs.getString("parcel_id");
                    if (job.parcelIDs.stream().filter(id -> id.equals(currentParcelID)).findAny().orElse(null) == null) {
                        job.parcelIDs.add(currentParcelID);
                    }
                }
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Updates the parcel status for the given parcel.
     * @param parcelID String - The parcel ID.
     * @param newStatus String - The new parcel status.
     */
    public void updateParcelStatus(String parcelID, String newStatus) {
        try {
            this.callables[6].setString("parcel_id", parcelID);
            this.callables[6].setString("new_status", newStatus);
            this.callables[6].executeQuery();
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
    }

}
