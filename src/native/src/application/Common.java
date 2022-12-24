package application;

import Data.*;
import DBCore.DBAPI;

import java.util.HashMap;

public class Common {
    public static DBAPI dbapi = new DBAPI();
    public static String username = "";
    public static final HashMap<String, String> parcelToJob = new HashMap<>();
}
