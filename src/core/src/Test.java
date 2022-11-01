import DBCore.DBAPI;
import ConfigLoader.*;
import Logger.Logger;

/**
 * Class containing test cases.
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        DBAPI api = new DBAPI();

        Logger log = new Logger("system.log");

        log.log("Test message!", Logger.MessageType.LOG);
        log.log("Test warning!", Logger.MessageType.WARNING);
        log.log("Test error!", Logger.MessageType.ERROR);

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

        if (!data.toString().equals("[Name = test_tpo, ip = 127.0.0.1, port = 3306]")) {
            System.out.println("Test 1.2 not passed.");
            System.out.println("Expected: " + "[Name = test_tpo, ip = 127.0.0.1, port = 3306]" + " || Result: " + data);
        }
        else {
            System.out.println("Test 1.2 passed.");
        }
    }
}
