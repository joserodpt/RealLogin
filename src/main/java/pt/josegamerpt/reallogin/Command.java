package pt.josegamerpt.reallogin;

import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pt.josegamerpt.reallogin.config.Config;
import pt.josegamerpt.reallogin.config.Players;
import pt.josegamerpt.reallogin.player.PlayerListener;
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
        Players.file().set(((Player) commandSender).getName(), null);
        PlayerManager.openRegister(((Player) commandSender), 0);

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
}
