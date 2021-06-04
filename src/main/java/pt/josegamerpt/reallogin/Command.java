package pt.josegamerpt.reallogin;

import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.annotations.WrongUsage;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pt.josegamerpt.reallogin.config.Config;
import pt.josegamerpt.reallogin.config.Players;
import pt.josegamerpt.reallogin.player.PlayerManager;
import pt.josegamerpt.reallogin.utils.Text;

import java.util.Arrays;

@me.mattstudios.mf.annotations.Command("reallogin")
public class Command extends CommandBase {
    @Default
    @Permission("reallogin.command")
    @WrongUsage("&c/reallogin <arguments>")
    public void defaultCommand(CommandSender commandSender) {
        Text.sendListCenteres((Player) commandSender, Arrays.asList("&6",
                "&fReal&7Login &a" + RealLogin.getVersion(),
                "&7by &fJoseGamer_PT",
                "&6",
                "/reallogin resetpin",
                "/reallogin reload",
                "&6"));
    }

    @SubCommand("resetPin")
    @Permission("reallogin.resetpin")
    public void resetPinCommand(CommandSender commandSender) {
        Players.file().set(commandSender.getName(), null);
        PlayerManager.openRegister(((Player) commandSender), Sound.ENTITY_VILLAGER_YES);

    }

    @SubCommand("reload")
    @Permission("reallogin.reload")
    public void reload(CommandSender commandSender) {
        Config.save();
        Config.reload();
        Players.save();
        Players.reload();
        Text.sendListCenteres((Player) commandSender, Arrays.asList("&6",
                "&fReal&7Login &aReloaded.",
                "&6"));
    }

    @SubCommand("deletepin")
    @Permission("reallogin.deletepin")
    public void adminreset(CommandSender commandSender, final String name) {
        if (Players.file().get(name) != null) {
            Players.file().set(name, null);
            Players.save();
            Text.sendListCenteres(commandSender, Arrays.asList("&6",
                    "&fPlayer pin &adeleted.",
                    "&6"));
        } else {
            Text.sendListCenteres(commandSender, Arrays.asList("&6",
                    "&fPlayer &cnot found.",
                    "&6"));
        }
    }

    @SubCommand("setpin")
    @Permission("reallogin.setpin")
    public void setpin(CommandSender commandSender, final String name, final Integer pin) {
        if (pin == null) {
            commandSender.sendMessage(Text.centeredString("&cInvalid input."));
            return;
        }
        Players.file().set(name, pin + "");
        Players.save();
        Text.sendListCenteres(commandSender, Arrays.asList("&6",
                "&fPlayer " + name + " &fpin set to &a" + pin,
                "&6"));
    }
}
