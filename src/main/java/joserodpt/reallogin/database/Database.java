package joserodpt.reallogin.database;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public interface Database<T> {

    @NotNull
    T getConnection() throws SQLException;

    @NotNull
    DatabaseType getType();

    @NotNull
    ExecutorService getExecutorService();
}