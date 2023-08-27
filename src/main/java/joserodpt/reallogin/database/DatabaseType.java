package joserodpt.reallogin.database;

import org.jetbrains.annotations.Nullable;

public enum DatabaseType {

    MYSQL("com.mysql.cj.jdbc.Driver"),
    SQLITE("org.sqlite.JDBC"),
    MONGODB("com.mongodb.client.MongoClient"),
    JSON(null);

    private final String driverClass;

    DatabaseType(final String driverClass) {
        this.driverClass = driverClass;
    }

    @Nullable
    public String getDriverClass() {
        return this.driverClass;
    }
}