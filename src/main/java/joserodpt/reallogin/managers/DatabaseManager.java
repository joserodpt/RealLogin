package joserodpt.reallogin.managers;

/*
 *   _____            _ _                 _
 *  |  __ \          | | |               (_)
 *  | |__) |___  __ _| | |     ___   __ _ _ _ __
 *  |  _  // _ \/ _` | | |    / _ \ / _` | | '_ \
 *  | | \ \  __/ (_| | | |___| (_) | (_| | | | | |
 *  |_|  \_\___|\__,_|_|______\___/ \__, |_|_| |_|
 *                                   __/ |
 *                                  |___/
 *
 * Licensed under the MIT License
 * @author José Rodrigues © 2020-2024
 * @link https://github.com/joserodpt/RealLogin
 */

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.logger.NullLogBackend;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import joserodpt.reallogin.RealLogin;
import joserodpt.reallogin.config.RLSQLConfig;
import joserodpt.reallogin.player.PlayerDataRow;
import joserodpt.reallogin.player.PlayerLoginRow;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DatabaseManager {

    private final Dao<PlayerLoginRow, UUID> playerIPDao;

    private final Dao<PlayerDataRow, UUID> playerDataDao;
    private final Map<UUID, PlayerDataRow> playerDataCache = new HashMap<>();

    private final RealLogin rl;

    public DatabaseManager(RealLogin rl) throws SQLException {
        LoggerFactory.setLogBackendFactory(new NullLogBackend.NullLogBackendFactory());

        this.rl = rl;
        String databaseURL = getDatabaseURL();

        ConnectionSource connectionSource = new JdbcConnectionSource(
                databaseURL,
                RLSQLConfig.file().getString("username"),
                RLSQLConfig.file().getString("password"),
                DatabaseTypeUtils.createDatabaseType(databaseURL)
        );

        TableUtils.createTableIfNotExists(connectionSource, PlayerDataRow.class);
        this.playerDataDao = DaoManager.createDao(connectionSource, PlayerDataRow.class);
        getPlayerData();

        TableUtils.createTableIfNotExists(connectionSource, PlayerLoginRow.class);
        this.playerIPDao = DaoManager.createDao(connectionSource, PlayerLoginRow.class);
    }

    /**
     * Database connection String used for establishing a connection.
     *
     * @return The database URL String
     */
    @NotNull
    protected String getDatabaseURL() {
        final String driver = RLSQLConfig.file().getString("driver").toLowerCase();

        switch (driver) {
            case "mysql":
            case "mariadb":
            case "postgresql":
                return "jdbc:" + driver + "://" + RLSQLConfig.file().getString("host") + ":" + RLSQLConfig.file().getInt("port") + "/" + RLSQLConfig.file().getString("database");
            case "sqlserver":
                return "jdbc:sqlserver://" + RLSQLConfig.file().getString("host") + ":" + RLSQLConfig.file().getInt("port") + ";databaseName=" + RLSQLConfig.file().getString("database");
            default:
                return "jdbc:sqlite:" + new File(rl.getDataFolder(), RLSQLConfig.file().getString("database") + ".db");
        }
    }

    protected void getPlayerData() {
        try {
            playerDataDao.queryForAll().forEach(playerData -> playerDataCache.put(playerData.getUUID(), playerData));
        } catch (SQLException exception) {
            rl.getLogger().severe("Error while getting the player data:" + exception.getMessage());
        }
    }

    public PlayerDataRow getPlayerData(Player p) {
        return playerDataCache.computeIfAbsent(p.getUniqueId(), uuid -> {
            try {
                return playerDataDao.queryForId(uuid);
            } catch (SQLException exception) {
                return null;
            }
        });
    }

    public PlayerDataRow getPlayerData(String name) {
        try {
            List<PlayerDataRow> pdo = playerDataDao.queryForEq("name", name);
            return pdo.size() == 1 ? pdo.get(0) : null;
        } catch (SQLException exception) {
            return null;
        }
    }

    public void savePlayerData(PlayerDataRow playerData, boolean async) {
        playerDataCache.put(playerData.getUUID(), playerData);
        if (async) {
            rl.getServer().getScheduler().runTaskAsynchronously(rl, () -> savePlayerData(playerData, false));
        } else {
            try {
                playerDataDao.createOrUpdate(playerData);
            } catch (SQLException exception) {
                rl.getLogger().severe("Error while saving the player data for " + playerData.getName() + ":");
                exception.printStackTrace();
            }
        }
    }

    public boolean isPlayerRegistered(String name) {
        return getPlayerData(name) != null;
    }

    public boolean isPlayerRegistered(Player player) {
        return getPlayerData(player) != null;
    }

    public void deletePlayerData(Player player) {
        playerDataCache.remove(player.getUniqueId());
        try {
            playerDataDao.deleteById(player.getUniqueId());
        } catch (SQLException exception) {
            rl.getLogger().severe("Error while deleting the player data for " + player.getName() + ":");
            exception.printStackTrace();
        }
    }

    public void deletePlayerData(String name) {
        playerDataCache.remove(Bukkit.getPlayer(name).getUniqueId());
        try {
            playerDataDao.delete(playerDataDao.queryForEq("name", name));
        } catch (SQLException exception) {
            rl.getLogger().severe("Error while deleting the player data for " + name + ":");
            exception.printStackTrace();
        }
    }

    public List<PlayerLoginRow> getPlayerLogins(String name) {
        try {
            List<PlayerLoginRow> logins = playerIPDao.queryForEq("name", name);
            Collections.reverse(logins);
            return logins;
        } catch (SQLException exception) {
            return null;
        }
    }

    public void savePlayerLogin(Player p, boolean async) {
        if (async) {
            rl.getServer().getScheduler().runTaskAsynchronously(rl, () -> savePlayerLogin(p, false));
        } else {
            try {
                playerIPDao.createOrUpdate(new PlayerLoginRow(p));
            } catch (SQLException exception) {
                rl.getLogger().severe("Error while saving the player login for " + p.getName() + ":");
                exception.printStackTrace();
            }
        }
    }
}