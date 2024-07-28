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

import joserodpt.reallogin.config.RLConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
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

    public static String formatTimestamp(long l) {
        if (l == 0) return "Never";

        SimpleDateFormat sdf = new SimpleDateFormat(RLConfig.file().getString("Settings.Date-Format"));
        return sdf.format(new Date(l));
    }

    public static String formatTimestampTime(long diff) {
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        long diffYears = diffDays / 365;
        diffDays %= 365;
        long diffMonths = diffDays / 30;
        diffDays %= 30;

        StringBuilder timeDiff = new StringBuilder();

        if (diffYears > 0) {
            timeDiff.append(diffYears).append("year ");
        }
        if (diffMonths > 0) {
            timeDiff.append(diffMonths).append("month ");
        }
        if (diffDays > 0) {
            timeDiff.append(diffDays).append("day ");
        }
        if (diffHours > 0) {
            timeDiff.append(diffHours).append("h ");
        }
        if (diffMinutes > 0) {
            timeDiff.append(diffMinutes).append("m ");
        }
        if (diffSeconds > 0 || timeDiff.length() == 0) {
            timeDiff.append(diffSeconds).append("s");
        }

        return timeDiff.toString().trim();
    }

    public static String diffTimeStampToNow(long dateTimestamp) {
        long diff = System.currentTimeMillis() - dateTimestamp;
        return formatTimestampTime(diff);
    }
}