package application;

import java.util.*;

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
    /** How many items can be displayed on page. */
    private final int contentPerPage;
    /** Maps staff roles to their database identifiers. */
    private static final HashMap<String, Integer> staffRoleToId = new HashMap<>();
    /** Maps job type names to their database identifiers. */
    private static final HashMap<String, Integer> jobNameToId = new HashMap<>();

    /**
     * Constructs a new Data object.
     */
    public Data() {
        if (Common.dbapi.isConnectionEstablished()) {
            if (!Common.username.equals("")) {
                Data.user = Common.username;
                this.setupParcelData();
                this.setupShipmentData();
            }
        }
        this.userIconIndex = new Random().nextInt(5);
        this.contentPerPage = 7;

        Data.staffRoleToId.put("Administrator", 1);
        Data.staffRoleToId.put("Warehouse manager", 2);
        Data.staffRoleToId.put("Warehouse agent", 3);
        Data.staffRoleToId.put("Delivery driver", 4);
        Data.staffRoleToId.put("International driver", 5);
        Data.staffRoleToId.put("Logistics agent", 6);
        Data.staffRoleToId.put("Order confirmation specialist", 7);

        Data.jobNameToId.put("Order processing", 1);
        Data.jobNameToId.put("Handover", 2);
        Data.jobNameToId.put("Check in", 3);
        Data.jobNameToId.put("Check out", 4);
        Data.jobNameToId.put("Cargo departing confirmation", 5);
        Data.jobNameToId.put("Cargo arrival confirmation", 6);
        Data.jobNameToId.put("Delivery cargo confirmation", 7);
        Data.jobNameToId.put("Parcel handover", 8);
    }

    /**
     * Sets up the parcel data.
     */
    void setupParcelData() {
        if (Data.userRole.equals("Warehouse manager")) {
            ArrayList<DataParcel> data = Common.dbapi.getWarehouseManagerParcelData(Data.user);
            for (DataParcel parcel : data) {
                String[] parcelString = {
                        parcel.parcelID,
                        parcel.parcelID,
                        Double.toString(parcel.weight),
                        parcel.dimensions.height + "x" + parcel.dimensions.width + "x" + parcel.dimensions.depth,
                        parcel.statusID,
                        ""
                };
                this.content.add(parcelString);
            }
        }
        else {
            ArrayList<DataJob> data = Common.dbapi.getJobsOfStaff(Data.user);
            for (DataJob job : data) {
                for (String parcelID : job.parcelIDs) {
                    Common.parcelToJob.put(parcelID, Integer.toString(job.jobID));
                    DataParcel parcel = Common.dbapi.getParcelData(parcelID);
                    String[] parcelString = {
                            Integer.toString(job.jobID),
                            parcel.parcelID,
                            Double.toString(parcel.weight),
                            parcel.dimensions.height + "x" + parcel.dimensions.width + "x" + parcel.dimensions.depth,
                            job.jobStatusID,
                            job.jobTypeID
                    };
                    this.content.add(parcelString);
                }
            }
        }
    }

    /**
     * Sets up the shipment data.
     */
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
                        job.jobStatusID
                };
                this.shipments.add(parcelString);
            }
        }
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

    /**
     * Creates a new parcel.
     * @param _sender String - The sender's username.
     * @param _senderStreetName String - The sender's street name.
     * @param _senderStreetNumber String - The sender's street number.
     * @param _senderCityCode String - The sender's city code.
     * @param _senderCityName String - The sender's city name.
     * @param _senderCountryCode String - The sender's country code.
     * @param _senderID String - The sender's ID.
     * @param _senderPhoneNumber String - The sender's phone number.
     * @param _rec String - The recipient's username.
     * @param _recStreetName String - The recipient's street name.
     * @param _recStreetNumber String - The recipient's street number.
     * @param _recCityCode String - The recipient's city code.
     * @param _recCityName String - The recipient's city name.
     * @param _recCountryCode String - The recipient's country code.
     * @param _recID String - The recipient's ID.
     * @param _recPhoneNumber String - The recipient's phone number.
     * @param _weight String - The parcel's weight.
     * @param _height String - The parcel's height.
     * @param _width String - The parcel's width.
     * @param _depth String - The parcel's depth.
     */
    public void createNewParcel(String _sender, String _senderStreetName, String _senderStreetNumber, String _senderCityCode, String _senderCityName, String _senderCountryCode, String _senderID, String _senderPhoneNumber,
                                String _rec, String _recPhoneNumber, String _recStreetName, String _recStreetNumber, String _recCityCode, String _recCityName, String _recCountryCode, String _recID,
                                String _weight, String _height, String _width, String _depth) {
        String senderFirstName = _sender.split(" ")[0];
        List<String> parts = Arrays.asList(_sender.split(" "));
        String senderLastName = String.join(" ", parts.subList(1, parts.size()));

        String recipientFirstName = _rec.split(" ")[0];
        parts = Arrays.asList(_rec.split(" "));
        String recipientLastName = String.join(" ", parts.subList(1, parts.size()));

        DataCustomer sender = new DataCustomer(
                0,
                _senderID.equals("") ? Utils.generateUsername(Common.dbapi, senderFirstName, senderLastName) : _senderID,
                senderFirstName,
                senderLastName,
                "",
                _senderPhoneNumber,
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
                _recID.equals("") ? Utils.generateUsername(Common.dbapi, recipientFirstName, recipientLastName) : _recID,
                recipientFirstName,
                recipientLastName,
                "",
                _recPhoneNumber,
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
            Common.dbapi.createCustomer(sender);
            Common.dbapi.createCustomer(recipient);
            String parcelID = Utils.generateParcelID(Common.dbapi, _senderCountryCode);
            Common.dbapi.createParcel(new DataParcel(
                    0,
                    parcelID,
                    "",
                    sender,
                    recipient,
                    Double.parseDouble(_weight),
                    dimensions
            ));

            Internals.createJobForDriver(parcelID, Data.user);

            // After adding parcel update view.
            this.setupParcelData();
            this.setupShipmentData();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void deliveryDriverAction(String action, String value, String driverType) {
        int jobID = Integer.parseInt(Common.parcelToJob.get(value));
        int newStatus = switch (action.split(" ")[1]) {
            case "handover" -> 2;
            case "cancelled" -> 3;
            default -> 0;
        };
        Common.dbapi.updateJobStatus(jobID, newStatus);

        if (newStatus == 2) {
            if (driverType.equals("International")) {
                int jobType = Common.dbapi.getJobType(jobID).value;

                if (jobType == 2) {
                    Internals.moveJob(Internals.MoveType.DELIVERYTOWAREHOUSE, jobID, value, Data.user, Data.userRole);
                }
                else if (jobType == 7) {
                    Internals.moveJob(Internals.MoveType.DELIVERYTODELIVERY, jobID, value, Data.user, Data.userRole);
                }
            }
            else {
                int jobType = Common.dbapi.getJobType(jobID).value;

                if (jobType == 5) {
                    Internals.moveJob(Internals.MoveType.INTERNATIONALTOINTERNATIONAL, jobID, value, Data.user, Data.userRole);
                }
                else if (jobType == 6) {
                    Internals.moveJob(Internals.MoveType.INTERNATIONALTOWAREHOUSE, jobID, value, Data.user, Data.userRole);
                }
            }
        }
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

    // WAREHOUSE AGENT METHODS

    /**
     * Changes the parcel status.
     *
     * @param parcelID String - The parcel ID.
     * @param status   String - The new status.
     */
    public void statusChanged(String parcelID, String status) {
        int newStatus = switch (status) {
            case "Completed" -> 2;
            case "Cancelled" -> 3;
            default -> 0;
        };

        int jobID = Integer.parseInt(Common.parcelToJob.get(parcelID));
        Common.dbapi.updateJobStatus(jobID, newStatus);

        if (newStatus == 2) {
            int branchID = Common.dbapi.getBranchIDFromUsername(Data.user).value;
            GeneralAddress branchAddress = Common.dbapi.getBranchAddress(branchID);
            DataParcel parcel = Common.dbapi.getParcelData(parcelID);

            SpecificAddress finalAddress = parcel.recipient.address;
            GeneralAddress finalAddressGeneral = new GeneralAddress(
                    0,
                    finalAddress.postCode,
                    finalAddress.cityName,
                    finalAddress.countryISO);

            ArrayList<DataParcelCenter> centers = Utils.shortestPath(Common.dbapi, branchAddress, finalAddressGeneral);
            int finalCenterID = Integer.parseInt(centers.get(centers.size() - 1).id);

            if (finalCenterID == branchID) {
                Internals.moveJob(Internals.MoveType.WAREHOUSETODELIVERY, jobID, parcelID, Data.user, Data.userRole);
            }
            else {
                Internals.moveJob(Internals.MoveType.WAREHOUSETOINTERNATIONAL, jobID, parcelID, Data.user, Data.userRole);
            }
        }
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
        String branchID = Common.dbapi.getBranchIDFromName(branch);
        DataCount[] data = Common.dbapi.getBranchStats(branchID);
        DataCount[] dataInbound = Common.dbapi.getCountOfAllInboundParcels();
        DataCount[] dataOutbound = Common.dbapi.getCountOfAllOutboundParcels();
        String[] branchData = {
                Integer.toString(data[0].value), // Inbound.
                Integer.toString(data[1].value), // Outbound.
                Double.toString((double)data[0].value / data[1].value), // Branch load.
                Double.toString((double)dataInbound[0].value / dataOutbound[0].value), // Average load.
                Integer.toString(data[2].value), // All jobs for drivers.
                Integer.toString(data[3].value), // No. of drivers.
                Double.toString((double) data[2].value / data[3].value) // Avg. no. of jobs per driver.
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
            Common.username = username;
            Data.user = username;
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

    /**
     * The internal implementation of the parcel pipeline.
     */
    private static class Internals {
        /** Current job ID. */
        private static int jobID;

        /**
         * The type of job move, which tells the system from which staff role to which staff role the parcel is moving.
         */
        public enum MoveType {
            DELIVERYTOWAREHOUSE,
            WAREHOUSETOINTERNATIONAL,
            INTERNATIONALTOINTERNATIONAL,
            INTERNATIONALTOWAREHOUSE,
            WAREHOUSETODELIVERY,
            DELIVERYTODELIVERY,
        }

        // STEP 2
        // On parcel create -> create job for random delivery driver at closest parcel center.

        /**
         * Creates a job for a random delivery driver at the closest parcel center.
         * @param parcelID String - The parcel ID.
         * @param username String - The username of the staff member who created the parcel.
         */
        private static void createJobForDriver(String parcelID, String username) {
            // From staff username get branch
            // From branch address get closest parcel center (coordinates etc.)
            // Create job for random driver at that parcel center

            int branchID = Common.dbapi.getBranchIDFromUsername(username).value;
            GeneralAddress branchAddress = Common.dbapi.getBranchAddress(branchID);
            SpecificAddress finalAddress = Common.dbapi.getParcelData(parcelID).recipient.address;
            GeneralAddress finalAddressGeneral = new GeneralAddress(0, finalAddress.postCode, finalAddress.cityName, finalAddress.countryISO);
            // SpecificAddress branchAddress = Common.dbapi.getBranchAddress(branchID);
            // GeneralAddress branchGeneralAddress = new GeneralAddress(branchAddress.items);
            // SpecificAddress finalAddress = Common.dbapi.getParcelData(parcelID).recipientAddress;
            // GeneralAddress finalGeneralAddress = new GeneralAddress(finalAddress.items);
            // ArrayList<DataParcelCenter> closestParcelCenter = Utils.shortestPath(Common.dbapi, branchGeneralAddress, finalGeneralAddress);
            // String parcelCenterID = closestParcelCenter.get(0).id;
            // ArrayList<DataStaff> drivers = Common.dbapi.getStaffDataFromParcelCenter(parcelCenterID, "Delivery driver");
            // Get random driver
            // Common.dbapi.createJob(//);

            ArrayList<DataParcelCenter> parcelCenters = Utils.shortestPath(Common.dbapi, branchAddress, finalAddressGeneral);
            String parcelCenterID = parcelCenters.get(0).id;
            ArrayList<DataStaff> drivers = Common.dbapi.getAllEmployeesWithRoleAtBranch(branchID, Data.staffRoleToId.get("Delivery driver"));

            DataStaff driver = drivers.get(new Random().nextInt(drivers.size()));
            Internals.jobID = Common.dbapi.createJob(Data.jobNameToId.get("Handover"), 1, driver.username).value;
            Common.dbapi.linkJobAndParcel(parcelID, Internals.jobID);
        }

        // STEPS 3, 4, 5, 6, 7
        // Moves job to another driver or warehouse agent.
        // Dijkstra on switch of roles.

        /**
         * Moves a job to another driver or warehouse agent.
         * @param type MoveType - The type of move.
         * @param jobID int - The job ID.
         * @param parcelID String - The parcel ID.
         * @param username String - The username of the staff member who moved the job.
         * @param userRole String - The user role.
         */
        private static void moveJob(MoveType type, int jobID, String parcelID, String username, String userRole) {
            switch (type) {
                case DELIVERYTOWAREHOUSE -> {
                    Internals.moveJobFromDeliveryToWarehouse(jobID, parcelID, username, userRole);
                }
                case WAREHOUSETOINTERNATIONAL -> {
                    Internals.moveJobFromWarehouseToInternational(jobID, parcelID, username, userRole);
                }
                case INTERNATIONALTOINTERNATIONAL -> {
                    Internals.moveJobFromInternationalToInternational(jobID, parcelID, username, userRole);
                }
                case INTERNATIONALTOWAREHOUSE -> {
                    Internals.moveJobFromInternationalToWarehouse(jobID, parcelID, username, userRole);
                }
                case WAREHOUSETODELIVERY -> {
                    Internals.moveJobFromWarehouseToDelivery(jobID, parcelID, username, userRole);
                }
                case DELIVERYTODELIVERY -> {
                    Internals.moveJobFromDeliveryToDelivery(jobID, parcelID, username, userRole);
                }
                default -> {
                    return;
                }
            }
            // If type is DELIVERTOWAREHOUSE
            // Get random warehouse agent at same parcel center.
            // Create job for that warehouse agent. (check-in)
            // If type is WAREHOUSETOINTERNATIONAL
            // Get random international driver at head office in same country as parcel center.
            // Create job for that international driver. (cargo departing info)
            // If type is INTERNATIONALTOINTERNATIONAL
            // Create cargo arrival info job for that international driver.
            // If type is INTERNATIONALTOWAREHOUSE
            // Get random warehouse agent at destination parcel center.
            // Create job for that warehouse agent. (check-in)
            // If type is WAREHOUSETODELIVERY
            // Get random delivery driver at current parcel center.
            // Create job for that delivery driver. (delivery cargo info)
            // If type is DELIVERYTODELIVERY
            // Create job for same delivery driver. (parcel handover)
        }

        /**
         * Moves a job from delivery driver to warehouse agent.
         * @param jobID int - The job ID.
         * @param parcelID String - The parcel ID.
         * @param username String - The username of the staff member who moved the job.
         * @param userRole String - The user role.
         */
        private static void moveJobFromDeliveryToWarehouse(int jobID, String parcelID, String username, String userRole) {
            int branchID = Common.dbapi.getBranchIDFromUsername(username).value;
            ArrayList<DataStaff> warehouseAgents = Common.dbapi.getAllEmployeesWithRoleAtBranch(branchID, Data.staffRoleToId.get("Warehouse agent"));
            DataStaff warehouseAgent = warehouseAgents.get(new Random().nextInt(warehouseAgents.size()));
            Internals.jobID = Common.dbapi.createJob(Data.jobNameToId.get("Check in"), 1, warehouseAgent.username).value;
            Common.dbapi.linkJobAndParcel(parcelID, jobID);

            // int branchID = Common.dbapi.getBranchIDFromStaff(username);
            // ArrayList<DataStaff> warehouseAgents = Common.dbapi.getStaffDataFromParcelCenter(branchID, "Warehouse Agent");
            // Get random agent
            // Common.dbapi.createJob(//);


            // Get random warehouse agent at same parcel center.
            // Create job for that warehouse agent. (check-in)
        }

        /**
         * Moves a job from warehouse agent to international driver.
         * @param jobID int - The job ID.
         * @param parcelID String - The parcel ID.
         * @param username String - The username of the staff member who moved the job.
         * @param userRole String - The user role.
         */
        private static void moveJobFromWarehouseToInternational(int jobID, String parcelID, String username, String userRole) {
            int branchID = Common.dbapi.getBranchIDFromUsername(username).value;
            GeneralAddress branchAddress = Common.dbapi.getBranchAddress(branchID);
            int headOfficeID = Common.dbapi.getBranchOffice(branchAddress.countryISO).value;
            ArrayList<DataStaff> internationalDrivers = Common.dbapi.getAllEmployeesWithRoleAtBranch(headOfficeID, Data.staffRoleToId.get("International driver"));
            DataStaff internationalDriver = internationalDrivers.get(new Random().nextInt(internationalDrivers.size()));
            Internals.jobID = Common.dbapi.createJob(Data.jobNameToId.get("Cargo departing confirmation"), 1, internationalDriver.username).value;
            Common.dbapi.linkJobAndParcel(parcelID, Internals.jobID);

            // int branchID = Common.dbapi.getBranchIDFromStaff(username);
            // SpecificAddress branchAddress = Common.dbapi.getBranchAddress(branchID);
            // int headOfficeID = Common.dbapi.getBranchOfficeIDFromCountry(branchAddress.country);
            // ArrayList<DataStaff> internationalDrivers = Common.dbapi.getStaffDataFromParcelCenter(headOfficeID, "International driver");
            // Get random driver
            // Common.dbapi.createJob(//);

            // Get random international driver at head office in same country as parcel center.
            // Create job for that international driver. (cargo departing info)
        }

        /**
         * Moves a job from international driver to international driver (same person).
         * @param jobID int - The job ID.
         * @param parcelID String - The parcel ID.
         * @param username String - The username of the staff member who moved the job.
         * @param userRole String - The user role.
         */
        private static void moveJobFromInternationalToInternational(int jobID, String parcelID, String username, String userRole) {
            ArrayList<DataJob> jobs = Common.dbapi.getJobsOfStaff(username);
            DataJob currentJob = null;
            for (DataJob job : jobs) {
                if (job.jobID == jobID) {
                    currentJob = job;
                    break;
                }
            }

            Internals.jobID = Common.dbapi.createJob(Data.jobNameToId.get("Cargo arrival confirmation"), 1, username).value;

            ArrayList<DataParcel> parcels = new ArrayList<>();
            for (String jobParcelID : currentJob.parcelIDs) {
                Common.dbapi.linkJobAndParcel(jobParcelID, Internals.jobID);
            }

            // Common.dbapi.createJob(//);

            // Create cargo arrival info job for that international driver.
        }

        /**
         * Moves a job from international driver to warehouse agent.
         * @param jobID int - The job ID.
         * @param parcelID String - The parcel ID.
         * @param username String - The username of the staff member who moved the job.
         * @param userRole String - The user role.
         */
        private static void moveJobFromInternationalToWarehouse(int jobID, String parcelID, String username, String userRole) {
            int branchID = Common.dbapi.getBranchIDFromUsername(username).value;

            GeneralAddress branchAddress = Common.dbapi.getBranchAddress(branchID);
            SpecificAddress finalAddress = Common.dbapi.getParcelData(parcelID).recipient.address;
            GeneralAddress finalAddressGeneral = new GeneralAddress(0, finalAddress.postCode, finalAddress.cityName, finalAddress.countryISO);


            ArrayList<DataParcelCenter> branches = Utils.shortestPath(Common.dbapi, branchAddress, finalAddressGeneral);
            DataParcelCenter branch = branches.get(0);

            ArrayList<DataStaff> warehouseAgents = Common.dbapi.getAllEmployeesWithRoleAtBranch(Integer.parseInt(branch.id), Data.staffRoleToId.get("Warehouse agent"));
            DataStaff warehouseAgent = warehouseAgents.get(new Random().nextInt(warehouseAgents.size()));

            Internals.jobID = Common.dbapi.createJob(Data.jobNameToId.get("Check in"), 1, warehouseAgent.username).value;
            Common.dbapi.linkJobAndParcel(parcelID, Internals.jobID);

            // int branchID = Common.dbapi.getBranchIDFromStaff(username);
            // int parcelID = Common.dbapi.getParcelIDFromJob(jobID);
            // SpecificAddress finalAddress = Common.dbapi.getParcelData(parcelID).recipientAddress;
            // ArrayList<DataStaff> warehouseAgents = Common.dbapi.getStaffDataFromParcelCenter(finalAddress, "Warehouse Agent");
            // Get random agent
            // Common.dbapi.createJob(//);

            // Get random warehouse agent at destination parcel center.
            // Create job for that warehouse agent. (check-in)
        }

        /**
         * Moves a job from warehouse agent to delivery driver.
         * @param jobID int - The job ID.
         * @param parcelID String - The parcel ID.
         * @param username String - The username of the staff member who moved the job.
         * @param userRole String - The user role.
         */
        private static void moveJobFromWarehouseToDelivery(int jobID, String parcelID, String username, String userRole) {
            int branchID = Common.dbapi.getBranchIDFromUsername(username).value;
            ArrayList<DataStaff> deliveryDrivers = Common.dbapi.getAllEmployeesWithRoleAtBranch(branchID, Data.staffRoleToId.get("Delivery driver"));
            DataStaff deliveryDriver = deliveryDrivers.get(new Random().nextInt(deliveryDrivers.size()));

            Internals.jobID = Common.dbapi.createJob(Data.jobNameToId.get("Delivery cargo confirmation"), 1, deliveryDriver.username).value;
            Common.dbapi.linkJobAndParcel(parcelID, Internals.jobID);

            // int branchID = Common.dbapi.getBranchIDFromStaff(username);
            // SpecificAddress branchAddress = Common.dbapi.getBranchAddress(branchID);
            // GeneralAddress branchGeneralAddress = new GeneralAddress(branchAddress.items);
            // ArrayList<DataStaff> deliveryDrivers = Common.dbapi.getStaffDataFromParcelCenter(branchGeneralAddress, "Delivery driver");
            // Get random driver
            // Common.dbapi.createJob(//);

            // Get random delivery driver at current parcel center.
            // Create job for that delivery driver. (delivery cargo info)
        }

        /**
         * Moves a job from delivery driver to delivery driver (same person).
         * @param jobID int - The job ID.
         * @param parcelID String - The parcel ID.
         * @param username String - The username of the staff member who moved the job.
         * @param userRole String - The user role.
         */
        private static void moveJobFromDeliveryToDelivery(int jobID, String parcelID, String username, String userRole) {
            ArrayList<DataJob> jobs = Common.dbapi.getJobsOfStaff(username);
            DataJob currentJob = null;
            for (DataJob job : jobs) {
                if (job.jobID == jobID) {
                    currentJob = job;
                    break;
                }
            }

            Internals.jobID = Common.dbapi.createJob(Data.jobNameToId.get("Parcel handover"), 1, username).value;

            ArrayList<DataParcel> parcels = new ArrayList<>();
            for (String jobParcelID : currentJob.parcelIDs) {
                Common.dbapi.linkJobAndParcel(jobParcelID, Internals.jobID);
            }
        }
    }

}
