package Utils;

import DBCore.DBAPI;

import java.util.Random;

/**
 * The core utilities.
 */
public class Utils {
    /**
     * Generates a random alphanumeric string of given length.
     * @param targetLength int - The target length of string.
     * @return String - The random alphanumeric string.
     */
    private static String generateRandomAlphanumericString(int targetLength) {
        final int lowerLimit = 48; // Character '0'.
        final int upperLimit = 122; // Character 'z'.

        return new Random()
                .ints(lowerLimit, upperLimit)
                // Leave out non-alphanumeric characters.
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * Generates an 8 character parcel ID in format: ISOCODExxxxx where x means random alphanumeric character.
     * @param country String - The country ISO code.
     * @return String - The 8 character parcel ID or null if timed out.
     * @throws Exception If country ISO code is in wrong format.
     * @throws NullPointerException If database API pointer is null.
     */
    public static String generateParcelID(DBAPI dbapi, String country) throws Exception {
        if (dbapi == null) {
            throw new NullPointerException("Utils:generateParcelIDs: Database API is null.");
        }
        if (country.length() != 3) {
            throw new Exception("Utils:generateParcelIDs: " +
                    "Country ISO code length is " + country.length() + " instead of 3 characters long.");
        }

        StringBuilder result;
        int identicalIDs = -1;
        int attemptCount = 0;

        do {
            result = new StringBuilder(country);
            result.append(Utils.generateRandomAlphanumericString(5));

            identicalIDs = dbapi.getCountOfIdenticalParcelIDs(result.toString());

            attemptCount++;
            if (attemptCount > 20) {
                return null;
            }
        } while (identicalIDs != 0);

        return result.toString();
    }

    /**
     * Generates a 10 character username based on name and surname. If name and surname are blank,
     * generate only reference number.
     * @param name String - The user's name.
     * @param surname String - The user's surname.
     * @return String - The 10 character username or null if timed out.
     * @throws NullPointerException If database API pointer is null.
     */
    public static String generateUsername(DBAPI dbapi, String name, String surname) throws NullPointerException {
        if (dbapi == null) {
            throw new NullPointerException("Utils:generateUserID: Database API is null.");
        }

        StringBuilder result;
        int identicalIDs = -1;
        int attemptCount = 0;

        do {
            if (name.equals("") || surname.equals("")) {
                result = new StringBuilder();
            }
            else {
                if (name.length() < 2 || surname.length() < 2) {
                    result = new StringBuilder();
                }
                else {
                    result = new StringBuilder(name.substring(0, 2));
                    result.append(surname, 0, 2);
                }
            }

            result.append(Utils.generateRandomAlphanumericString(10 - result.length()));

            identicalIDs = dbapi.getCountOfIdenticalUsernames(result.toString());

            attemptCount++;
            if (attemptCount > 20) {
                return null;
            }
        } while (identicalIDs != 0);

        return result.toString();
    }

    /**
     * Finds the shortest path from the source parcel center to the destination parcel center.
     */
    public static void shortestPath() {

    }
}
