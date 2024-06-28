package pt.josegamerpt.reallogin.utils;

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
 * @author José Rodrigues
 * @link https://github.com/joserodpt/RealLogin
 */

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pt.josegamerpt.reallogin.config.RLConfig;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Text {
    public static String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void send(CommandSender p, String s, boolean prefix) {
        p.sendMessage(color((prefix ? getPrefix() : "") + s));
    }

    public static List<String> color(Collection<String> list) {
        return list.stream()
                .map(Text::color)
                .collect(Collectors.toList());
    }

    public static String getPrefix() {
        return RLConfig.file().getString("Strings.Prefix");
    }
}