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
 * @author José Rodrigues © 2020-2026
 * @link https://github.com/joserodpt/RealLogin
 */

import dev.dejvokep.boostedyaml.YamlDocument;
import joserodpt.realutils.config.YamlConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class RLSQLConfig {

    private static YamlConfig config;

    public static void setup(final JavaPlugin rm) {
        config = YamlConfig.of(rm, "sql.yml").load();
    }

    public static YamlDocument file() {
        return config.file();
    }

    public static void reload() {
        config.reload();
    }
}
