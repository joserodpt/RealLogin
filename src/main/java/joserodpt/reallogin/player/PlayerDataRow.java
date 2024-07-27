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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@DatabaseTable(tableName = "reallogin_player_data")
public class PlayerDataRow {

    @DatabaseField(columnName = "uuid", canBeNull = false, id = true)
    private @NotNull UUID uuid;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "hashed_pasword")
    private String hashed_pasword;

    @DatabaseField(columnName = "locale")
    private String locale;

    public PlayerDataRow(Player p, String hashed_pasword) {
        this.uuid = p.getUniqueId();
        this.name = p.getName();
        this.hashed_pasword = hashed_pasword;
        this.locale = p.getLocale();
    }

    public PlayerDataRow() {
        // ORMLite requires a no-arg constructor
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getHashedPassword() {
        return hashed_pasword;
    }

    public String getLocale() {
        return locale;
    }
}