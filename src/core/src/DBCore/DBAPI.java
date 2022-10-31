package DBCore;

/**
 * The public facing database access API.
 */
public class DBAPI {
    private DBCore core;

    public DBAPI() {
        core = new DBCore();
    }

    public void printHey() {
        System.out.println("Hey!");
        core.printHey();
    }
}
