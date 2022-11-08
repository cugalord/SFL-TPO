package Utils;

import DBCore.DBAPI;

import java.util.List;
import java.util.Random;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.*;

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
     * Calculates the distance in km between two geographical locations using the Haversine formula.
     * Link to explanation: https://en.wikipedia.org/wiki/Haversine_formula
     * @param source Coordinates - The source coordinates.
     * @param destination Coordinates - The destination coordinates.
     * @return double - The distance in km.
     */
    private static double calculateDistanceFromCoordinates(Coordinates source, Coordinates destination) {
        final double earthRadiusKm = 6371;
        final double latitudeDifference = Math.toRadians(destination.latitude) - Math.toRadians(source.latitude);
        final double longitudeDifference = Math.toRadians(destination.longitude) - Math.toRadians(source.longitude);
        final double a = Math.pow(Math.sin(latitudeDifference / 2), 2)
                       + Math.cos(Math.toRadians(source.latitude)) * Math.cos(Math.toRadians(destination.latitude))
                       * Math.pow(Math.sin(longitudeDifference / 2), 2);
        final double c = 2 * Math.asin(Math.sqrt(a));
        return c * earthRadiusKm;
    }

    /**
     * Generates an 8 character parcel ID in format: ISOCODExxxxx where x means random alphanumeric character.
     * @param dbapi DBAPI - The database api.
     * @param country String - The country ISO code.
     * @return String - The 8 character parcel ID or null if timed out.
     * @throws Exception If country ISO code is in wrong format.
     * @throws NullPointerException If database API pointer is null.
     */
    public static String generateParcelID(DBAPI dbapi, String country) throws Exception {
        if (dbapi == null) {
            throw new NullPointerException("Database API is null.");
        }
        if (country.length() != 3) {
            throw new Exception("Country ISO code length is " + country.length() + " instead of 3 characters long.");
        }

        StringBuilder result;
        int identicalIDs = -1;
        int attemptCount = 0;

        do {
            result = new StringBuilder(country);
            result.append(Utils.generateRandomAlphanumericString(5));

            identicalIDs = dbapi.getCountOfIdenticalParcelIDs(result.toString()).value;

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
     * @param dbapi DBAPI - The database api.
     * @param name String - The user's name.
     * @param surname String - The user's surname.
     * @return String - The 10 character username or null if timed out.
     * @throws NullPointerException If database API pointer is null.
     */
    public static String generateUsername(DBAPI dbapi, String name, String surname) throws NullPointerException {
        if (dbapi == null) {
            throw new NullPointerException("Database API is null.");
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

            identicalIDs = dbapi.getCountOfIdenticalUsernames(result.toString()).value;

            attemptCount++;
            if (attemptCount > 20) {
                return null;
            }
        } while (identicalIDs != 0);

        return result.toString();
    }

    /**
     * Checks if the given post code exists in the database.
     * @param dbapi DBAPI - The database api.
     * @param postCode String - The post code.
     * @return boolean - True if post code exists in database, false otherwise.
     */
    public static boolean validatePostCode(DBAPI dbapi, String postCode) {
        int samePostCodes = dbapi.getCountOfIdenticalPostCodes(postCode).value;
        return samePostCodes > 0;
    }

    /**
     * Finds the shortest path (via parcel centers) between the sender and recipient address.
     * @param dbapi DBAPI - The database api.
     * @param senderAddress String - The sender address.
     * @param recipientAddress String - The recipient address.
     * @return String[] - The list of parcel centers.
     */
    public static String[] shortestPath(DBAPI dbapi, String senderAddress, String recipientAddress) {
        // TODO: Get list of parcel centers from database
        // TODO: Create graph of parcel centers
        // TODO: Find the closest parcel center to sender address (in same country)
        // TODO: Find the closest parcel center to recipient address (in same country)
        // TODO: Run the shortest path algorithm between them

        /*SimpleDirectedWeightedGraph<String, Integer> g = new SimpleDirectedWeightedGraph<String, Integer>(Integer.class);

        //DefaultDirectedWeightedGraph<String, Integer> g = new DefaultDirectedWeightedGraph<>(Integer.class);
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addVertex("D");
        g.addVertex("E");
        g.addVertex("F");
        g.addVertex("G");
        g.addEdge("A", "B", 10);
        g.addEdge("A", "C", 8);
        g.addEdge("A", "D", 7);
        g.addEdge("B", "C", 3);
        g.addEdge("B", "E", 10);
        g.addEdge("B", "F", 3);
        g.addEdge("B", "G", 10);
        g.addEdge("C", "D", 7);

        List<Integer> sp = DijkstraShortestPath.findPathBetween(g, "A", "G");
        System.out.println(sp);*/

        SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>  graph =
                new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>
                        (DefaultWeightedEdge.class);
        graph.addVertex("vertex1");
        graph.addVertex("vertex2");
        graph.addVertex("vertex3");
        graph.addVertex("vertex4");
        graph.addVertex("vertex5");


        DefaultWeightedEdge e1 = graph.addEdge("vertex1", "vertex2");
        graph.setEdgeWeight(e1, 5);

        DefaultWeightedEdge e2 = graph.addEdge("vertex2", "vertex3");
        graph.setEdgeWeight(e2, 3);

        DefaultWeightedEdge e3 = graph.addEdge("vertex4", "vertex5");
        graph.setEdgeWeight(e3, 6);

        DefaultWeightedEdge e4 = graph.addEdge("vertex2", "vertex4");
        graph.setEdgeWeight(e4, 2);

        DefaultWeightedEdge e5 = graph.addEdge("vertex5", "vertex4");
        graph.setEdgeWeight(e5, 4);


        DefaultWeightedEdge e6 = graph.addEdge("vertex2", "vertex5");
        graph.setEdgeWeight(e6, 9);

        DefaultWeightedEdge e7 = graph.addEdge("vertex4", "vertex1");
        graph.setEdgeWeight(e7, 7);

        DefaultWeightedEdge e8 = graph.addEdge("vertex3", "vertex2");
        graph.setEdgeWeight(e8, 2);

        DefaultWeightedEdge e9 = graph.addEdge("vertex1", "vertex3");
        graph.setEdgeWeight(e9, 10);

        DefaultWeightedEdge e10 = graph.addEdge("vertex3", "vertex5");
        graph.setEdgeWeight(e10, 1);


        System.out.println("Shortest path from vertex1 to vertex5:");
        List<DefaultWeightedEdge> shortest_path =
                DijkstraShortestPath.findPathBetween(graph, "vertex1", "vertex5");
        System.out.println(shortest_path);

        return null;
    }

    // Utility classes

    /**
     * The geographical coordinates.
     */
    private static class Coordinates {
        /** The latitude. */
        public final double latitude;
        /** The longitude. */
        public final double longitude;

        /**
         * Constructs a new Coordinates object.
         * @param latitude double - The latitude.
         * @param longitude dobule - The longitude.
         */
        Coordinates(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}


