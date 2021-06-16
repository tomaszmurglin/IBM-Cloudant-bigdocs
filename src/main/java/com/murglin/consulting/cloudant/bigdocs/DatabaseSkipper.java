package com.murglin.consulting.cloudant.bigdocs;

public class DatabaseSkipper {

    public static boolean shouldSkipDb(final String dbName) {
        return isInternalDb(dbName) || isEventsDb(dbName) || isMetricsDb(dbName) || isStatsDb(dbName) || isTransferDb(dbName);
    }

    private static boolean isInternalDb(final String dbName) {
        return dbName.startsWith("_");
    }

    private static boolean isEventsDb(final String dbName) {
        return dbName.startsWith("event");
    }

    private static boolean isMetricsDb(final String dbName) {
        return dbName.startsWith("metric");
    }

    private static boolean isStatsDb(final String dbName) {
        return dbName.startsWith("stat");
    }

    private static boolean isTransferDb(final String dbName) {
        return dbName.startsWith("transfer_database");
    }
}
