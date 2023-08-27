package joserodpt.reallogin.database.hikari;

import joserodpt.reallogin.database.Database;
import joserodpt.reallogin.database.DatabaseType;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public interface HikariDatabase extends Database<Connection> {

    @Override
    @NotNull Connection getConnection() throws SQLException;

    @Override
    @NotNull DatabaseType getType();
}