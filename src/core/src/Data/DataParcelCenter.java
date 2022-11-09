package Data;

/**
 * Data representation of the parcel center location.
 */
public class DataParcelCenter extends Data {
    /** The parcel center id. */
    public final String id;
    /** The parcel center latitude. */
    public final double latitude;
    /** The parcel center longitude. */
    public final double longitude;
    /** The country ISO3 code. */
    public final String countryISO;

    /**
     * Constructs a new DataParcelCenter object.
     * @param index int - The sequential record number.
     * @param id String - The parcel center id.
     * @param latitude double - The parcel center latitude.
     * @param longitude double - The parcel center longitude.
     * @param countryISO String - The country ISO3 code.
     */
    public DataParcelCenter(int index, String id, double latitude, double longitude, String countryISO) {
        super(index);
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryISO = countryISO;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "DataParcelCenter{" +
                "recordNumber=" + recordNumber +
                ", id='" + id + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", country=" + countryISO +
                '}';
    }
}
