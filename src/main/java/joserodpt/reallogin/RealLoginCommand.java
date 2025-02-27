package joserodpt.reallogin;

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

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import joserodpt.reallogin.config.RLConfig;
import joserodpt.reallogin.config.RLSQLConfig;
import joserodpt.reallogin.player.PlayerDataRow;
import joserodpt.reallogin.player.PlayerLoginRow;
import joserodpt.reallogin.utils.LocationUtils;
import joserodpt.reallogin.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@Command(value = "reallogin", alias = "rl")
public class RealLoginCommand extends BaseCommand {

    private final RealLogin rl;
    public RealLoginCommand(RealLogin rl) {
        this.rl = rl;
    }

    @Default
    @SuppressWarnings("unused")
    public void defaultcmd(CommandSender commandSender) {
        Text.send(commandSender,
                "&fReal&7Login &6v" + this.rl.getDescription().getVersion(), false);
    }

    @SubCommand("resetpin")
    @Permission("reallogin.resetpin")
    @SuppressWarnings("unused")
    public void resetpincmd(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            Text.send(commandSender, "&cOnly players can use this command.", true);
            return;
        }

        this.rl.getDatabaseManager().deletePlayerData(((Player) commandSender));
        this.rl.getGUIManager().openRegisterGUI(((Player) commandSender));
    }

    @SubCommand("settplogin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void settplogin(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            Text.send(commandSender, "&cOnly players can use this command.", true);
            return;
        }

        Player p = (Player) commandSender;
        RLConfig.file().set("Locations.TPLogin", LocationUtils.serialize(p.getLocation()));
        RLConfig.save();
        Text.send(p, "&aLogin location set.", true);
    }

    @SubCommand("settpafterlogin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void settpafterlogin(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            Text.send(commandSender, "&cOnly players can use this command.", true);
            return;
        }

        Player p = (Player) commandSender;
        RLConfig.file().set("Locations.TPAfterLogin", LocationUtils.serialize(p.getLocation()));
        RLConfig.save();
        Text.send(p, "&aAfter Login location set.", true);
    }

    @SubCommand("tplogin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void gotptplogin(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            Text.send(commandSender, "&cOnly players can use this command.", true);
            return;
        }
        Player p = (Player) commandSender;
        Location l = LocationUtils.deserializeSection(RLConfig.file().getSection("Locations.TPLogin"));

        p.teleport(l);
        Text.send(p, "&aTeleported to the login location.", true);
    }

    @SubCommand("tpafterlogin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void gotptpafterlogin(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            Text.send(commandSender, "&cOnly players can use this command.", true);
            return;
        }
        Player p = (Player) commandSender;
        Location l = LocationUtils.deserializeSection(RLConfig.file().getSection("Locations.TPAfterLogin"));

        p.teleport(l);
        Text.send(p, "&aTeleported to the after login location.", true);
    }

    @SubCommand("deltplogin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void deltplogin(CommandSender commandSender) {
        RLConfig.file().remove("Locations.TPLogin");
        RLConfig.save();
        Text.send(commandSender, "&aDeleted the login location.", true);
    }

    @SubCommand("deltpafterlogin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void deltpafterlogin(CommandSender commandSender) {
        RLConfig.file().remove("Locations.TPAfterLogin");
        RLConfig.save();
        Text.send(commandSender, "&aDeleted the after login location.", true);
    }

    @SubCommand(value = "info", alias = "inf")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void infocmd(CommandSender commandSender, String name) {
        if (name == null) {
            Text.send(commandSender, "&cInvalid usage: /rl info <name>", true);
            return;
        }

        PlayerDataRow pdo = rl.getDatabaseManager().getPlayerData(name);
        if (pdo == null) {
            Text.send(commandSender, "&cPlayer not found.", true);
            return;
        }

        Text.send(commandSender, "&fInformation for &b" + name, true);
        Text.send(commandSender, " > &fLocale: &a" + pdo.getLocale(), false);

        boolean hasTimeSession = rl.getPlayerManager().doesPlayerHaveSession(pdo.getUUID());

        Text.send(commandSender, " > &fHas Time Session: " + (hasTimeSession ? "&aTrue" : "&cFalse"), false);
        if (hasTimeSession) {
            Text.send(commandSender, "   &fTime Left: &a" + rl.getPlayerManager().getSessionTimeLeft(pdo.getUUID()), false);
        }

        List<PlayerLoginRow> logins = rl.getDatabaseManager().getPlayerLogins(name);

        Text.send(commandSender, " > &fLogin count: &a" + logins.size(), false);
        Text.send(commandSender, " > &fLast 10 logins:", false);
        for (int i = 0; i <= 10 && i < logins.size(); ++i) {
            PlayerLoginRow plr = logins.get(i);
            Text.send(commandSender, "   &f" + (i + 1) + ". &a" + plr.getDate() + " &ffrom &a" + plr.getIP() + " &e(" + Text.diffTimeStampToNow(plr.getDateTimestamp()) + " ago)", false);
        }

        Text.send(commandSender, " > &fLast 10 Recorded IPs:", false);
        List<String> ips = logins.stream().map(PlayerLoginRow::getIP).distinct().collect(Collectors.toList());
        for (int i = 0; i < 10 && i < ips.size(); ++i) {
            Text.send(commandSender, "   &f" + (i+1) + ". &a" + ips.get(i), false);
        }
    }

    @SubCommand(value = "reload", alias = "rl")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void reloadcmd(CommandSender commandSender) {
        RLConfig.reload();
        rl.getPlayerManager().startTickTask();
        RLSQLConfig.reload();

        Text.send(commandSender, "&aReloaded.", true);
    }

    @SubCommand(value = "deletepin", alias = "delpin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void deletepincmd(CommandSender commandSender, final String name) {
        if (name == null) {
            Text.send(commandSender, "&cInvalid usage: /rl deletepin <name>", true);
            return;
        }

        if (rl.getDatabaseManager().isPlayerRegistered(name)) {
            rl.getDatabaseManager().deletePlayerData(name);
            Text.send(commandSender, "&fPlayer pin has been &cdeleted.", true);
        } else {
            Text.send(commandSender, "&fPlayer &cnot found.", true);
        }
    }

    @SubCommand("setpin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void setpincmd(CommandSender commandSender, final String name, final Integer pin) {
        if (name == null || pin == null) {
            Text.send(commandSender, "&cInvalid usage: /rl setpin <name> <pin>", true);
            return;
        }

        if (rl.getDatabaseManager().isPlayerRegistered(name)) {
            rl.getDatabaseManager().savePlayerData(new PlayerDataRow(Bukkit.getPlayer(name), pin.toString()), true);
            Text.send(commandSender, "&fPlayer PIN is now: &a" + pin, true);
        } else {
            Text.send(commandSender, "&fPlayer &cnot found.", true);
        }
        Text.send(commandSender, "&fPlayer PIN is now: &a" + pin, true);
    }
}
