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
        api.printHey();

        /*ConfigLoader cfgldr = new ConfigLoader();
        cfgldr.load();
        Vector<DBData> data = cfgldr.fetchData();

        for (DBData d : data) {
            System.out.println(d);
        }*/

        configLoaderTests();
    }

    static void configLoaderTests() {
        ConfigLoader cfgldr = new ConfigLoader();
        cfgldr.load();
        Vector<DBData> data = cfgldr.fetchData();


        System.out.println("ConfigLoader tests:");

        if (data.size() != 1) {
            System.out.println("Test 1.1 not passed.");
            System.out.println("Expected: " + 1 + " || Result: " + data.size());
        }
        else {
            System.out.println("Test 1.1 passed.");
        }

        if (!data.get(0).toString().equals("[Name = test, ip = 127.0.0.2]")) {
            System.out.println("Test 1.2 not passed.");
            System.out.println("Expected: " + "[Name = test, ip = 127.0.0.2]" + " || Result: " + data.get(0));
        }
        else {
            System.out.println("Test 1.2 passed.");
        }
    }
}
