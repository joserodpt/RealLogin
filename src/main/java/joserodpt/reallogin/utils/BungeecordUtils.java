package joserodpt.reallogin.utils;

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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BungeecordUtils {
    public static void connect(String name, Player player, JavaPlugin jp) {
        if (player == null) {
            return;
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write("Connect".getBytes(StandardCharsets.UTF_8));
            out.write(name.getBytes(StandardCharsets.UTF_8));
            player.sendPluginMessage(jp, "BungeeCord", out.toByteArray());
        } catch (IOException e) {
            Bukkit.getLogger().warning("Error sending player to server:");
            e.printStackTrace();
        }
    }
}
