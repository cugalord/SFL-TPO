package application;

import DBCore.DBAPI;

import java.util.HashMap;

/**
 * Common items between all views.
 */
public class Common {
    /** The database API. */
    public static DBAPI dbapi = new DBAPI();
    /** The current staff username. */
    public static String username = "";
    /** Maps parcels to their jobs. */
    public static final HashMap<String, String> parcelToJob = new HashMap<>();
}
