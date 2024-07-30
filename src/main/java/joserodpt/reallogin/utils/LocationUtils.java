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

import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Map;

public class LocationUtils {
    public static Map<String, Object> serialize(Location l) {
        return Map.of(
                "world", l.getWorld().getName(),
                "x", l.getX(),
                "y", l.getY(),
                "z", l.getZ(),
                "yaw", l.getYaw(),
                "pitch", l.getPitch()
        );
    }

    public static Location deserialize(Map<String, Object> map) {
        return new Location(
                Bukkit.getWorld((String) map.get("world")),
                (double) map.get("x"),
                (double) map.get("y"),
                (double) map.get("z"),
                ((Double) map.get("yaw")).floatValue(),
                ((Double) map.get("pitch")).floatValue()
        );
    }

    public static Location deserializeSection(Section s) {
        double x = 0, y = 0, z = 0;
        float yaw = 0, pitch = 0;
        World w = null;
        for (String routesAsString : s.getRoutesAsStrings(false)) {
            switch (routesAsString) {
                case "world":
                    w = Bukkit.getWorld(s.getString(routesAsString));
                    break;
                case "x":
                    x = s.getDouble(routesAsString);
                    break;
                case "y":
                    y = s.getDouble(routesAsString);
                    break;
                case "z":
                    z = s.getDouble(routesAsString);
                    break;
                case "yaw":
                    yaw = s.getFloat(routesAsString);
                    break;
                case "pitch":
                    pitch = s.getFloat(routesAsString);
                    break;
            }
        }
        if (w == null) {
            return null;
        }
        return new Location(w, x, y, z, yaw, pitch);
    }
}
