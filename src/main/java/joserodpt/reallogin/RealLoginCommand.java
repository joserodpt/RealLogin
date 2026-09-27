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
 * @author José Rodrigues © 2020-2026
 * @link https://github.com/joserodpt/RealLogin
 */

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import joserodpt.reallogin.config.RLConfig;
import joserodpt.reallogin.config.RLSQLConfig;
import joserodpt.reallogin.player.PlayerDataRow;
import joserodpt.reallogin.player.PlayerLoginRow;
import joserodpt.reallogin.utils.Format;
import joserodpt.reallogin.utils.LocationUtils;
import joserodpt.realutils.dialog.Dialogs;
import joserodpt.realutils.BuildInfo;
import joserodpt.realutils.text.Text;
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
        BuildInfo.sendAbout(commandSender, this.rl, "&fReal&7Login");
    }

    @SubCommand("settings")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void settingscmd(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            Text.send(commandSender, "&cOnly players can use this command.");
            return;
        }
        ConfigEditor.open((Player) commandSender, this.rl);
    }

    @SubCommand("resetpin")
    @Permission("reallogin.resetpin")
    @SuppressWarnings("unused")
    public void resetpincmd(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            Text.send(commandSender, "&cOnly players can use this command.");
            return;
        }

        final Player p = (Player) commandSender;
        final Runnable reset = () -> {
            this.rl.getDatabaseManager().deletePlayerData(p);
            this.rl.getGUIManager().openRegisterGUI(p);
        };
        //asked first where the server has dialogs; elsewhere the command resets straight away
        if (!Dialogs.confirm(p, RLConfig.file().getString("Strings.Dialogs.Reset-Pin.Title"),
                RLConfig.file().getString("Strings.Dialogs.Reset-Pin.Question"),
                RLConfig.file().getString("Strings.Dialogs.Reset-Pin.Button"),
                RLConfig.file().getString("Strings.Dialogs.Cancel"), reset, null)) {
            reset.run();
        }
    }

    @SubCommand("logout")
    @SuppressWarnings("unused")
    public void logoutcmd(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            Text.send(commandSender, "&cOnly players can use this command.");
            return;
        }

        final Player p = (Player) commandSender;
        this.rl.getPlayerManager().logout(p);
        Text.send(p, RLConfig.file().getString("Strings.Messages.Logged-Out", "&fYou logged out. Enter your PIN to log back in."));
    }

    @SubCommand("bypass")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void adminbypasscmd(CommandSender commandSender, final String name) {
        if (name == null) {
            Text.send(commandSender, "&cInvalid usage: /rl admin bypass <name>");
            return;
        }

        Player target = Bukkit.getPlayerExact(name);
        if (target == null) {
            Text.send(commandSender, "&cPlayer must be online to bypass login.");
            return;
        }

        if (!rl.getPlayerManager().isPlayerFronzen(target.getUniqueId())) {
            Text.send(commandSender, "&ePlayer is not currently waiting for login.");
            return;
        }

        rl.getPlayerManager().loginGrantedForPlayer(target.getUniqueId());
        target.closeInventory();
        target.setInvulnerable(false);

        String bypassGranted = RLConfig.file().getString("Strings.Messages.Admin.Bypass-Granted");
        String bypassTarget = RLConfig.file().getString("Strings.Messages.Admin.Bypass-Target");

        Text.send(commandSender, bypassGranted.replace("%player%", target.getName()));
        Text.send(target, bypassTarget);
    }

    @SubCommand("settplogin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void settplogin(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            Text.send(commandSender, "&cOnly players can use this command.");
            return;
        }

        Player p = (Player) commandSender;
        RLConfig.file().set("Locations.TPLogin", LocationUtils.serialize(p.getLocation()));
        RLConfig.save();
        Text.send(p, "&aLogin location set.");
    }

    @SubCommand("settpafterlogin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void settpafterlogin(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            Text.send(commandSender, "&cOnly players can use this command.");
            return;
        }

        Player p = (Player) commandSender;
        RLConfig.file().set("Locations.TPAfterLogin", LocationUtils.serialize(p.getLocation()));
        RLConfig.save();
        Text.send(p, "&aAfter Login location set.");
    }

    @SubCommand("tplogin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void gotptplogin(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            Text.send(commandSender, "&cOnly players can use this command.");
            return;
        }
        Player p = (Player) commandSender;
        Location l = LocationUtils.deserializeSection(RLConfig.file().getSection("Locations.TPLogin"));

        p.teleport(l);
        Text.send(p, "&aTeleported to the login location.");
    }

    @SubCommand("tpafterlogin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void gotptpafterlogin(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            Text.send(commandSender, "&cOnly players can use this command.");
            return;
        }
        Player p = (Player) commandSender;
        Location l = LocationUtils.deserializeSection(RLConfig.file().getSection("Locations.TPAfterLogin"));

        p.teleport(l);
        Text.send(p, "&aTeleported to the after login location.");
    }

    @SubCommand("deltplogin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void deltplogin(CommandSender commandSender) {
        final Runnable delete = () -> {
            RLConfig.file().remove("Locations.TPLogin");
            RLConfig.save();
            Text.send(commandSender, "&aDeleted the login location.");
        };
        if (!confirm(commandSender, "&fDelete the login location? Players stay where they join instead.", delete)) {
            delete.run();
        }
    }

    @SubCommand("deltpafterlogin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void deltpafterlogin(CommandSender commandSender) {
        final Runnable delete = () -> {
            RLConfig.file().remove("Locations.TPAfterLogin");
            RLConfig.save();
            Text.send(commandSender, "&aDeleted the after login location.");
        };
        if (!confirm(commandSender, "&fDelete the after login location? Players stay where they log in instead.", delete)) {
            delete.run();
        }
    }

    /**
     * An admin's yes-or-no dialog before something that cannot be undone.
     *
     * @return false if nothing was asked - the console, or a server without dialogs - so the caller goes ahead
     */
    private static boolean confirm(final CommandSender sender, final String question, final Runnable confirmed) {
        return sender instanceof Player && Dialogs.confirm((Player) sender, "&fReal&7Login &8| &fConfirm", question,
                "&cDelete", RLConfig.file().getString("Strings.Dialogs.Cancel"), confirmed, null);
    }

    @SubCommand(value = "info", alias = "inf")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void infocmd(CommandSender commandSender, @Suggestion("#players") String name) {
        if (name == null) {
            Text.send(commandSender, "&cInvalid usage: /rl info <name>");
            return;
        }

        PlayerDataRow pdo = rl.getDatabaseManager().getPlayerData(name);
        if (pdo == null) {
            Text.send(commandSender, "&cPlayer not found.");
            return;
        }

        Text.send(commandSender, "&fInformation for &b" + name);
        Text.sendRaw(commandSender, " > &fLocale: &a" + pdo.getLocale());

        boolean hasTimeSession = rl.getPlayerManager().doesPlayerHaveSession(pdo.getUUID());

        Text.sendRaw(commandSender, " > &fHas Time Session: " + (hasTimeSession ? "&aTrue" : "&cFalse"));
        if (hasTimeSession) {
            Text.sendRaw(commandSender, "   &fTime Left: &a" + rl.getPlayerManager().getSessionTimeLeft(pdo.getUUID()));
        }

        List<PlayerLoginRow> logins = rl.getDatabaseManager().getPlayerLogins(name);

        Text.sendRaw(commandSender, " > &fLogin count: &a" + logins.size());
        Text.sendRaw(commandSender, " > &fLast 10 logins:");
        for (int i = 0; i <= 10 && i < logins.size(); ++i) {
            PlayerLoginRow plr = logins.get(i);
            Text.sendRaw(commandSender, "   &f" + (i + 1) + ". &a" + plr.getDate() + " &ffrom &a" + plr.getIP() + " &e(" + Format.diffTimeStampToNow(plr.getDateTimestamp()) + " ago)");
        }

        Text.sendRaw(commandSender, " > &fLast 10 Recorded IPs:");
        List<String> ips = logins.stream().map(PlayerLoginRow::getIP).distinct().collect(Collectors.toList());
        for (int i = 0; i < 10 && i < ips.size(); ++i) {
            Text.sendRaw(commandSender, "   &f" + (i+1) + ". &a" + ips.get(i));
        }
    }

    @SubCommand(value = "reload", alias = "rl")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void reloadcmd(CommandSender commandSender) {
        RLConfig.reload();
        rl.getPlayerManager().startTickTask();
        RLSQLConfig.reload();

        Text.send(commandSender, "&aReloaded.");
    }

    @SubCommand(value = "deletepin", alias = "delpin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void deletepincmd(CommandSender commandSender, final String name) {
        if (name == null) {
            Text.send(commandSender, "&cInvalid usage: /rl deletepin <name>");
            return;
        }

        if (rl.getDatabaseManager().isPlayerRegistered(name)) {
            final Runnable delete = () -> {
                rl.getDatabaseManager().deletePlayerData(name);
                Text.send(commandSender, "&fPlayer pin has been &cdeleted.");
            };
            if (!confirm(commandSender, "&fDelete &b" + name + "&f's PIN? They will register a new one when they next join.", delete)) {
                delete.run();
            }
        } else {
            Text.send(commandSender, "&fPlayer &cnot found.");
        }
    }

    @SubCommand("setpin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void setpincmd(CommandSender commandSender, final String name, final Integer pin) {
        if (name == null || pin == null) {
            Text.send(commandSender, "&cInvalid usage: /rl setpin <name> <pin>");
            return;
        }

        if (rl.getDatabaseManager().isPlayerRegistered(name)) {
            rl.getDatabaseManager().savePlayerData(new PlayerDataRow(Bukkit.getPlayer(name), pin.toString()), true);
            Text.send(commandSender, "&fPlayer PIN is now: &a" + pin);
        } else {
            Text.send(commandSender, "&fPlayer &cnot found.");
        }
        Text.send(commandSender, "&fPlayer PIN is now: &a" + pin);
    }
}
