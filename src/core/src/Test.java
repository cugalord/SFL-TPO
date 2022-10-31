import DBCore.DBAPI;
import ConfigLoader.*;

import java.util.Vector;

/**
 * Class containing test cases.
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        DBAPI api = new DBAPI();

        configLoaderTests();
    }

    static void configLoaderTests() {
        ConfigLoader cfgldr = new ConfigLoader();
        cfgldr.load();
        DBData data = cfgldr.fetchData();


        System.out.println("ConfigLoader tests:");

        if (data == null) {
            System.out.println("Test 1.1 not passed.");
            System.out.println("Data is null.");
        }
        else {
            System.out.println("Test 1.1 passed.");
        }

        if (!data.toString().equals("[Name = test, ip = 127.0.0.2]")) {
            System.out.println("Test 1.2 not passed.");
            System.out.println("Expected: " + "[Name = test, ip = 127.0.0.2]" + " || Result: " + data);
        }
        else {
            System.out.println("Test 1.2 passed.");
        }
    }
}
