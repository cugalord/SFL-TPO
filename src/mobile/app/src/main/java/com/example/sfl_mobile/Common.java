package com.example.sfl_mobile;

import DBCore.DBAPI;

public class Common {
    /** The database connectivity api. Logging is disabled due to Read-only file system. */
    public static DBAPI dbapi = new DBAPI(false);
}
