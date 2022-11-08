package Data;

/**
 * Data representation of the parcel center location.
 */
public class DataParcelCenter extends Data {
    /** The parcel center address. */
    public final String address;
    /** The parcel center latitude. */
    public final double latitude;
    /** The parcel center longitude. */
    public final double longitude;

    /**
     * Constructs a new DataParcelCenter object.
     * @param index int - The sequential record number.
     * @param address String - The parcel center address.
     * @param latitude double - The parcel center latitude.
     * @param longitude double - The parcel center longitude.
     */
    DataParcelCenter(int index, String address, double latitude, double longitude) {
        super(index);
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "DataParcelCenter{" +
                "recordNumber=" + recordNumber +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
