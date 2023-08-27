package joserodpt.reallogin.database.hikari.mysql;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.dejvokep.boostedyaml.YamlDocument;
import joserodpt.reallogin.database.DatabaseType;
import joserodpt.reallogin.database.hikari.HikariDatabase;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MySQLDatabase implements HikariDatabase {

    private static final String JDBC_URL = "jdbc:mysql://%s:%d/%s";
    private final HikariDataSource dataSource;
    private final ExecutorService executorService;

    public MySQLDatabase(final YamlDocument config) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(String.format(JDBC_URL,
                config.getString("database-settings.host"),
                config.getInt("database-settings.port"),
                config.getString("database-settings.database-name")
                ));
        hikariConfig.setDriverClassName(this.getType().getDriverClass());
        hikariConfig.setPoolName("RealLogin-Database");
        hikariConfig.setUsername(config.getString("database-settings.username"));
        hikariConfig.setPassword(config.getString("database-settings.password"));

        this.dataSource = new HikariDataSource(hikariConfig);
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2,
                new ThreadFactoryBuilder().setNameFormat("RealLogin-Hikari-%d").build());
    }

    @Override
    public @NotNull Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    @Override
    public @NotNull DatabaseType getType() {
        return DatabaseType.MYSQL;
    }

    @Override
    public @NotNull ExecutorService getExecutorService() {
        return this.executorService;
    }
}