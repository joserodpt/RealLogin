package pt.josegamerpt.reallogin;

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

import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pt.josegamerpt.reallogin.config.RLConfig;
import pt.josegamerpt.reallogin.config.RLPlayerConfig;
import pt.josegamerpt.reallogin.utils.Text;

@me.mattstudios.mf.annotations.Command("reallogin")
@Alias({"rlogin", "rlog"})
public class RealLoginCommand extends CommandBase {

    private RealLogin rl;
    public RealLoginCommand(RealLogin rl) {
        this.rl = rl;
    }

    @Default
    @SuppressWarnings("unused")
    public void defaultCommand(CommandSender commandSender) {
        Text.send(commandSender,
                "&fReal&7Login &6v" + this.rl.getDescription().getVersion(), false);
    }

    @SubCommand("resetpin")
    @Permission("reallogin.resetpin")
    @SuppressWarnings("unused")
    public void resetPinCommand(CommandSender commandSender) {
        RLPlayerConfig.file().set(commandSender.getName(), null);
        RLPlayerConfig.save();
        this.rl.getGUIManager().openRegisterGUI(((Player) commandSender), Sound.ENTITY_VILLAGER_YES);
    }

    @SubCommand("reload")
    @Alias("rl")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void reload(CommandSender commandSender) {
        RLConfig.reload();
        RLPlayerConfig.reload();

        Text.send(commandSender, "&aReloaded.", true);
    }

    @SubCommand("deletepin")
    @Alias("delpin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void deletepincmd(CommandSender commandSender, final String name) {
        if (RLPlayerConfig.file().get(name) != null) {
            RLPlayerConfig.file().set(name, null);
            RLPlayerConfig.save();
            Text.send(commandSender, "&fPlayer pin has been &cdeleted.", true);
        } else {
            Text.send(commandSender, "&fPlayer &cnot found.", true);
        }
    }

    @SubCommand("setpin")
    @Permission("reallogin.admin")
    @SuppressWarnings("unused")
    public void setpincmd(CommandSender commandSender, final String name, final Integer pin) {
        if (pin == null) {
            Text.send(commandSender, "&cInvalid PIN input.", true);
            return;
        }

        RLPlayerConfig.file().set(name, pin + "");
        RLPlayerConfig.save();
        Text.send(commandSender, "&fPlayer PIN is now: &a" + pin, true);
    }
}
