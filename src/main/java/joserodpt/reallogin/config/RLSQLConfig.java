package joserodpt.reallogin.config;

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

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class RLSQLConfig implements Listener {

    private static String name = "sql.yml";

    private static YamlDocument document;

    public static void setup(final JavaPlugin rm) {
        try {
            document = YamlDocument.create(new File(rm.getDataFolder(), name), rm.getResource(name));
        } catch (final IOException e) {
            Bukkit.getLogger().severe( "Couldn't setup " + name + "!");
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    public static YamlDocument file() {
        return document;
    }

    public static void reload() {
        try {
            document.reload();
        } catch (final IOException e) {
            Bukkit.getLogger().severe( "Couldn't reload " + name + "!");
        }
    }
}