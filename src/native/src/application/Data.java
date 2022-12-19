package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Data.*;
import Utils.Utils;

/**
 * The data that is displayed in the application.
 */
public class Data {
	/** The page content. */
    List<String[]> content = new ArrayList<>();
	/** The list of employees. */
    List<String[]> employees = new ArrayList<>();
	/** The list of shipments. */
    List<String[]> shipments = new ArrayList<>();
	/** The current user. */
    public static String user;
    /** The current user role. */
    public static String userRole;
    /** The user icon index. */
    private final int userIconIndex;


    private final int contentPerPage;

    /**
     * Constructs a new Data object.
     */
    public Data() {
        this.userIconIndex = new Random().nextInt(5);
        this.contentPerPage = 7;
    }

    // *** test data ***
    void setupParcelData() {
        if (Data.userRole.equals("Warehouse manager")) {
            ArrayList<DataParcel> data = Common.dbapi.getWarehouseManagerParcelData(Data.user);
            for (DataParcel parcel : data) {
                String[] parcelString = {
                        parcel.parcelID,
                        parcel.parcelID,
                        Double.toString(parcel.weight),
                        parcel.dimensions.height + "x" + parcel.dimensions.width + "x" + parcel.dimensions.depth,
                        Integer.toString(parcel.statusID),
                        ""
                };
                this.content.add(parcelString);
            }
        }
        else {
            ArrayList<DataJob> data = Common.dbapi.getJobsOfStaff(Data.user);
            for (DataJob job : data) {
                for (String parcelID : job.parcelIDs) {
                    DataParcel parcel = Common.dbapi.getParcelData(parcelID);
                    String[] parcelString = {
                            parcel.parcelID,
                            parcel.parcelID,
                            Double.toString(parcel.weight),
                            parcel.dimensions.height + "x" + parcel.dimensions.width + "x" + parcel.dimensions.depth,
                            Integer.toString(parcel.statusID),
                            ""
                    };
                    this.content.add(parcelString);
                }
            }
        }

        /*if (Data.userRole.equals("Warehouse manager")) {
            
        }
        else {
            ArrayList<DataJob> data = Common.dbapi.getJobsOfStaff(Data.user);
            for (DataJob job : data) {
                //this.content.addAll(job.par)
            } 
        }*/
        
        
        /*String id, tracking_no, weight, dimensions, status, action;
        id = "0045";
        tracking_no = "YT72760621444007800";
        weight = "2.53";
        dimensions = "12.3 x 10.5 x 5.0";
        status = "Pending";
        action = "None"; // Delivery driver -> None, Handover, Cancelled
        String[][] temp_content = {{id + "1", tracking_no, weight, dimensions, status, action},
                {id + "2", tracking_no, weight, dimensions, status, action},
                {id + "3", tracking_no, weight, dimensions, status, action},
                {id + "4", tracking_no, weight, dimensions, status, action},
                {id + "5", tracking_no, weight, dimensions, status, action},
                {id + "5", tracking_no, weight, dimensions, status, action},
                {id + "6", tracking_no, weight, dimensions, status, action},
                {id + "7", tracking_no, weight, dimensions, status, action},
                {id + "8", tracking_no, weight, dimensions, status, action},
                {id + "9", tracking_no, weight, dimensions, status, action},
                {id + "10", tracking_no, weight, dimensions, status, action},
                {id + "11", tracking_no, weight, dimensions, status, action},
                {id + "12", tracking_no, weight, dimensions, status, action},
                {id + "13", tracking_no, weight, dimensions, status, action},
                {id + "14", tracking_no, weight, dimensions, status, action},
                {id + "15", tracking_no, weight, dimensions, status, action},
        };

        for (String[] parcel : temp_content) {
            content.add(parcel);
        }*/
    }

    // *** test data ***
    void setupShipmentData() {
        if (!Data.userRole.equals("Warehouse manager")) {
            ArrayList<DataJob> data = Common.dbapi.getJobsOfStaff(Data.user);
            for (DataJob job : data) {
                double weight = 0;
                for (String parcelID : job.parcelIDs) {
                    DataParcel parcel = Common.dbapi.getParcelData(parcelID);
                    weight += parcel.weight;
                }
                String[] parcelString = {
                        Integer.toString(job.jobID),
                        Integer.toString(job.parcelIDs.size()),
                        Double.toString(weight),
                        Integer.toString(job.jobStatusID)
                };
                this.content.add(parcelString);
            }
        }
        /*String id, no_of_parcels, net_weight, status;
        id = "04";
        no_of_parcels = "23";
        net_weight = "22.5";
        status = "None";

        String[][] temp_content = {{id + "1", no_of_parcels, net_weight, status},
                {id + "2", no_of_parcels, net_weight, status},
                {id + "3", no_of_parcels, net_weight, status},
                {id + "4", no_of_parcels, net_weight, status},
                {id + "5", no_of_parcels, net_weight, status},
                {id + "5", no_of_parcels, net_weight, status},
                {id + "6", no_of_parcels, net_weight, status},
                {id + "7", no_of_parcels, net_weight, status},
                {id + "8", no_of_parcels, net_weight, status},
                {id + "9", no_of_parcels, net_weight, status},
                {id + "10", no_of_parcels, net_weight, status},
                {id + "11", no_of_parcels, net_weight, status},
                {id + "12", no_of_parcels, net_weight, status},
                {id + "13", no_of_parcels, net_weight, status},
                {id + "14", no_of_parcels, net_weight, status},
                {id + "15", no_of_parcels, net_weight, status},
        };

        for (String[] shipment : temp_content) {
            shipments.add(shipment);
        }*/
    }


    // ORDER CONFIRMATION SPECIALIST METHODS

    /**
     * Same as getParcelData().
     *
     * @param page int - The page number.
     * @return List<String[]> - The list of parcels on specific page.
     */
    public List<String[]> getParcelDataOrderConfirmationSpecialistByPage(int page) {
        int from = page * this.contentPerPage;
        int to;
        if (this.content.size() >= page * this.contentPerPage + this.contentPerPage) {
            to = page * this.contentPerPage + this.contentPerPage;
        }
        // If we're on the last page.
        else {
            to = this.content.size();
        }

        return this.content.subList(from, to);
    }

    // funkcija se izvede ko Order conf. specialist ustvari novo pošiljko
    public void createNewParcel(String _sender, String _senderStreetName, String _senderStreetNumber, String _senderCityCode, String _senderCityName, String _senderCountryCode, String _senderID, String _rec, String _recStreetName, String _recStreetNumber, String _recCityCode, String _recCityName, String _recCountryCode, String _recID, String _weight, String _height, String _width, String _depth) {
        DataCustomer sender = new DataCustomer(
                0,
                _sender.equals("") ? Utils.generateUsername(Common.dbapi, "", "") : _sender,
                "",
                "",
                "",
                "",
                new SpecificAddress(
                        0,
                        _senderStreetName,
                        Integer.parseInt(_senderStreetNumber),
                        _senderCityCode,
                        _senderCityName,
                        _senderCountryCode)
        );
        DataCustomer recipient = new DataCustomer(
                0,
                _rec.equals("") ? Utils.generateUsername(Common.dbapi, "", "") : _rec,
                "",
                "",
                "",
                "",
                new SpecificAddress(
                        0,
                        _recStreetName,
                        Integer.parseInt(_recStreetNumber),
                        _recCityCode,
                        _recCityName,
                        _recCountryCode)
        );
        Dimensions dimensions = new Dimensions(
                0,
                Integer.parseInt(_height),
                Integer.parseInt(_width),
                Integer.parseInt(_depth)
        );
        try {
            Common.dbapi.createParcel(new DataParcel(
                    0,
                    Utils.generateParcelID(Common.dbapi, _senderCountryCode),
                    0,
                    sender,
                    recipient,
                    Double.parseDouble(_weight),
                    dimensions
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("created new parcel! ");
        System.out.println(_sender + _senderStreetName+ _rec+ _recStreetName+ _weight+ _height+ _depth);
    }


    // DELIVERY DRIVER METHODS

    /**
     * Gets the shipment data based on page. It gets only 7 shipments,
     * as only 7 fit on one page. Which shipments are shown is dependent on the page parameter.
     *
     * @param page int - The page number.
     * @return List<String []> - The list of shipments on specific page.
     */
    public List<String[]> getShipmentDataByPage(int page) {
        int from = page * this.contentPerPage;
        int to;
        if (this.shipments.size() >= page * this.contentPerPage + this.contentPerPage) {
            to = page * this.contentPerPage + this.contentPerPage;
        }
        // If we're on the last page.
        else {
            to = this.shipments.size();
        }
        return this.shipments.subList(from, to);
    }

    /**
     * Executes the action of delivery driver.
     *
     * @param action String - The action.
     * @param value  String - The action value.
     */
    public void deliveryDriverAction(String action, String value) {
        // TODO:
        System.out.println(action + " " + value);
    }


    // INTERNATIONAL DRIVER METHODS

    /**
     * Gets the shipment data by type and page. It gets only 7 shipments,
     * as only 7 fit on one page. Which shipments are shown is dependent on the type and page parameters.
     *
     * @param type String - The shipment type. Either "Cargo Departing Info" or "Cargo Arrival Info".
     * @param page int - The page number.
     * @return List<String [ ]> - The list of shipments on specific page.
     */
    public List<String[]> getShipmentDataByTypeByPage(String type, int page) {
        int from = page * this.contentPerPage;
        int to;
        if (this.shipments.size() >= page * this.contentPerPage + this.contentPerPage) {
            to = page * this.contentPerPage + this.contentPerPage;
        }
        // If we're on the last page.
        else {
            to = this.shipments.size();
        }
        return this.shipments.subList(from, to);
    }

    /**
     * Changes the parcel status.
     *
     * @param parcelID String - The parcel ID.
     * @param status   String - The new status.
     */
    public void statusChanged(String parcelID, String status) {
        int statusID = switch (status) {
            case "In Transit" -> 1;
            case "Arrived" -> 2;
            case "Delivered" -> 3;
            default -> 0;
        };
        Common.dbapi.updateParcelStatus(parcelID, statusID);
        System.out.println(parcelID + " " + status);
        // primer: 00451 Processed
    }

    /**
     * Gets parcel data based on specific employee, or all employees.
     *
     * @param employee String - The employee ID. Either ID or "All".
     * @param page     int - The page number.
     * @return List<String [ ]> - The list of parcels on specific page.
     */
    public List<String[]> getParcelDataByPageByEmployee(String employee, int page) {
        int from = page * this.contentPerPage;
        int to;
        if (this.content.size() >= page * this.contentPerPage + this.contentPerPage) {
            to = page * this.contentPerPage + this.contentPerPage;
        }
        // If we're on the last page.
        else {
            to = this.content.size();
        }
        return this.content.subList(from, to);
    }

    /**
     * Gets the employees as a String array.
     *
     * @return String[] - The employees.
     */
    public String[] getEmployeesArray() {
        ArrayList<DataStaff> data = Common.dbapi.getWarehouseManagerEmployeesInfo(user);
        return data.stream().map(DataStaff::getFullName).toArray(String[]::new);
    }

    /**
     * Gets the employees at branch as a String array.
     *
     * @param branch String - The branch ID.
     * @return String[] - The employees.
     */
    public String[] getEmployeesByBranchArray(String branch) {
        ArrayList<DataStaff> data = Common.dbapi.getEmployeesAtBranch(Integer.parseInt(branch));
        return data.stream().map(DataStaff::getFullName).toArray(String[]::new);
    }

    /**
     * Gets the number of jobs assigned to a specific employee.
     *
     * @param employee String - The employee ID.
     * @param branch   String - The branch ID.
     * @return String - The number of jobs.
     */
    public String getNoOfJobsOfEmployee(String employee, String branch) {
        return Integer.toString(Common.dbapi.getCountOfUncompletedJobOfStaff(employee).value);
    }


    // LOGISTICS AGENT METHODS

    /**
     * Gets the drivers as a String array.
     *
     * @return String[] - The employees.
     */
    public String[] getEmployeesDriversArray() {
        String[] employees = {"Joe", "Jack", "Bob"};
        return employees;
    }

    /**
     * Gets the branches as a String array.
     *
     * @return String[] - The branches.
     */
    public String[] getBranchesArray() {
        ArrayList<DataBranch> branches = Common.dbapi.getBranches();
        return branches.stream().map(b -> b.name).toArray(String[]::new);
    }

    /**
     * Gets the data of specific branch. This data includes inbound and outbound parcels, branch load, average load,
     * all jobs for drivers, number of drivers, number of jobs per driver.
     *
     * @param branch String - The branch name.
     * @return String[] - The branch data.
     */
    public String[] getBranchData(String branch) {
        // inbound, outbound, branch load, average load, all jobs for drivers,
        // no. of drivers, avg. no. of jobs per driver
        String branchID = Common.dbapi.getBranchIDFromName(branch);
        DataCount[] data = Common.dbapi.getBranchStats(branchID);
        //String[] branchData = {"4.512", "1.332", "24", "3", "242", "32", "2.4"};
        String[] branchData = {
                Integer.toString(data[0].value),
                Integer.toString(data[1].value),
                "0", // TODO:
                "0",
                Integer.toString(data[2].value),
                Integer.toString(data[3].value),
                Double.toString((double) data[2].value / data[3].value)
        };
        return branchData;
    }

    /**
     * Checks if enough drivers are assigned to a specific branch.
     *
     * @param branch String - The branch name.
     * @return Boolean - True if enough branches, false otherwise.
     */
    public boolean isEnoughDriversInThisBranch(String branch) {
        String branchID = Common.dbapi.getBranchIDFromName(branch);
        DataCount[] data = Common.dbapi.getBranchStats(branchID);
        return data[0].value <= data[1].value;
    }


    // WAREHOUSE MANAGER METHODS

    /**
     * Gets number of all parcels of warehouse manager's warehouse.
     *
     * @return String - The number of packages.
     */
    public String getNumberOfAllPackages() {
        return Integer.toString(Common.dbapi.getWarehouseManagerWarehouseParcelInfo(user)[0].value);
    }

    /**
     * Gets number of pending parcels of warehouse manager's warehouse.
     *
     * @return String - The number of packages.
     */
    public String getNumberOfPendingPackages() {
        return Integer.toString(Common.dbapi.getWarehouseManagerWarehouseParcelInfo(user)[1].value);
    }

    /**
     * Gets number of processed parcels of warehouse manager's warehouse.
     *
     * @return String - The number of packages.
     */
    public String getNumberOfProcessedPackages() {
        return Integer.toString(Common.dbapi.getWarehouseManagerWarehouseParcelInfo(user)[2].value);
    }

    /**
     * Gets all employees of warehouse manager's warehouse.
     *
     * @return List<String[]> - The employee data.
     */
    public List<String[]> getEmployees() {
        ArrayList<DataStaff> data = Common.dbapi.getWarehouseManagerEmployeesInfo(Data.user);
        for (DataStaff staff : data) {
            this.employees.add(new String[] {staff.username, staff.getFullName(), "", staff.role, ""});
        }
        return this.employees;
    }


    // MISCELLANEOUS METHODS

    /**
     * Gets the parcel data based on page, in format {id, trackingNo, Weight, Dimensions, Status}. It gets only 7 parcels,
     * as only 7 fit on one page. Which parcels are shown is dependent on the page parameter.
     *
     * @param page int - The page number.
     * @return List<String [ ]> - The list of parcels on specific page.
     */
    public List<String[]> getParcelDataByPage(int page) {
        int from = page * this.contentPerPage;
        int to;
        if (this.content.size() >= page * this.contentPerPage + this.contentPerPage) {
            to = page * this.contentPerPage + this.contentPerPage;
        }
        // If we're on the last page.
        else {
            to = this.content.size();
        }
        return this.content.subList(from, to);
    }

    /**
     * Verifies if the username and password are correct.
     *
     * @param username String - The username.
     * @param password String - The password.
     * @return String - UserID if correct, "false" if incorrect.
     */
    public String verifyUsernameAndPassword(String username, String password) {
        Common.dbapi.login(username, password);
        if (Common.dbapi.isConnectionEstablished()) {
            Data.userRole = this.getUserRole(username);
            this.setupParcelData();
            this.setupShipmentData();

            return username;
        }
        return "false";
    }

    /**
     * Gets the user profile picture of the current user.
     *
     * @return String - The user icon.
     */
    public String getUserProfilePicture() {
        String[] usericons = {"User icon.png", "User icon2.png", "User icon3.png", "User icon4.png", "User icon5.png", "User icon6.png"};
        return usericons[this.userIconIndex];
	}

    /**
     * Gets the current user's full name.
     *
     * @return String - The full name.
     */
    public String getUserFullName() {
        return Common.dbapi.getStaffDataFromUsername(user).getFirstNameAndInitial();
	}

    /**
     * Gets the current user's role.
     *
     * @param username String - The username.
     * @return String - The user role.
     */
    public String getUserRole(String username) {
        return Common.dbapi.getStaffDataFromUsername(username).role;
    }

}
