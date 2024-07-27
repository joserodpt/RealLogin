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

import joserodpt.reallogin.config.RLConfig;
import joserodpt.reallogin.config.RLSQLConfig;
import joserodpt.reallogin.player.PlayerDataRow;
import joserodpt.reallogin.player.PlayerLoginRow;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import joserodpt.reallogin.utils.Text;

import java.util.List;
import java.util.stream.Collectors;

@me.mattstudios.mf.annotations.Command("reallogin")
@Alias({"rlogin", "rlog"})
public class RealLoginCommand extends CommandBase {

    private RealLogin rl;
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

    @SubCommand("info")
    @Alias("inf")
    @Permission("reallogin.admin")
    @Completion("#players")
    @SuppressWarnings("unused")
    public void infocmd(CommandSender commandSender, String name) {
        PlayerDataRow pdo = rl.getDatabaseManager().getPlayerData(name);
        if (pdo == null) {
            Text.send(commandSender, "&cPlayer not found.", true);
            return;
        }

        Text.send(commandSender, "&fInformation for &b" + name, true);
        Text.send(commandSender, " > &fLocale: &a" + pdo.getLocale(), false);

        List<PlayerLoginRow> logins = rl.getDatabaseManager().getPlayerLogins(name);

        Text.send(commandSender, " > &fLogin count: &a" + logins.size(), false);
        Text.send(commandSender, " > &fLast 10 logins:", false);
        for (int i = 0; i < 10 && i < logins.size(); ++i) {
            PlayerLoginRow plr = logins.get(i);
            Text.send(commandSender, "   &f" + (i + 1) + ". &a" + plr.getDate() + " &ffrom &a" + plr.getIP() + " &e(" + Text.diffTimeStampToNow(plr.getDateTimestamp()) + " ago)", false);
        }

        Text.send(commandSender, " > &fLast 10 Recorded IPs:", false);
        List<String> ips = logins.stream().map(PlayerLoginRow::getIP).distinct().collect(Collectors.toList());
        for (int i = 0; i < 10 && i < ips.size(); ++i) {
            Text.send(commandSender, "   &f" + (i+1) + ". &a" + ips.get(i), false);
        }
    }

    @SubCommand("reload")
    @Alias("rl")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void reloadcmd(CommandSender commandSender) {
        RLConfig.reload();
        RLSQLConfig.reload();

        Text.send(commandSender, "&aReloaded.", true);
    }

    @SubCommand("deletepin")
    @Alias("delpin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    @WrongUsage("&cUsage: /reallogin deletepin <player>")
    public void deletepincmd(CommandSender commandSender, final String name) {
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
    @WrongUsage("&cUsage: /reallogin setpin <player> <pin>")
    public void setpincmd(CommandSender commandSender, final String name, final Integer pin) {
        if (pin == null) {
            Text.send(commandSender, "&cInvalid PIN input.", true);
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
