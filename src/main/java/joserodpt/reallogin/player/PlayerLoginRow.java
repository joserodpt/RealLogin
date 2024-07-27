package joserodpt.reallogin.player;

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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import joserodpt.reallogin.utils.Text;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@DatabaseTable(tableName = "reallogin_logins")
public class PlayerLoginRow {

    @DatabaseField(columnName = "date", canBeNull = false, unique = true, id = true)
    private @NotNull long date;

    @DatabaseField(columnName = "uuid", canBeNull = false)
    private @NotNull UUID uuid;

    @DatabaseField(columnName = "name", canBeNull = false)
    private @NotNull String name;

    @DatabaseField(columnName = "ip", canBeNull = false)
    private @NotNull String ip;

    public PlayerLoginRow(Player p) {
        this.date = System.currentTimeMillis();
        this.uuid = p.getUniqueId();
        this.name = p.getName();
        this.ip = p.getAddress().getAddress().getHostAddress();
    }

    public PlayerLoginRow() {
        // ORMLite requires a no-arg constructor
    }

    @NotNull
    public UUID getPlayerUUID() {
        return uuid;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getIP() {
        return ip;
    }

    @NotNull
    public String getDate() {
        return Text.formatTimestamp(date);
    }

    public long getDateTimestamp() {
        return date;
    }
}