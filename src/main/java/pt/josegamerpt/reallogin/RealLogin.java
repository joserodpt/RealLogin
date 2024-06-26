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

import me.mattstudios.mf.base.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pt.josegamerpt.reallogin.config.RLConfig;
import pt.josegamerpt.reallogin.config.RLPlayerConfig;
import pt.josegamerpt.reallogin.managers.GUIManager;
import pt.josegamerpt.reallogin.managers.PlayerManager;
import pt.josegamerpt.reallogin.utils.Text;

public final class RealLogin extends JavaPlugin {

    public PlayerManager playerManager;
    public GUIManager guiManager;

    @Override
    public void onEnable() {
        //config
        saveDefaultConfig();
        RLConfig.setup(this);
        RLPlayerConfig.setup(this);

        //managers
        playerManager = new PlayerManager(this);
        guiManager = new GUIManager(this);

        if (RLConfig.file().getBoolean("Settings.Bungeecord.Enabled")) {
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            getLogger().info("BungeeCord mode is enabled.");
        }

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        //commands
        CommandManager commandManager = new CommandManager(this);
        commandManager.getMessageHandler().register("cmd.no.console", sender -> Text.send(sender,"&cThis command can't be used in the console!", true));
        commandManager.getMessageHandler().register("cmd.no.exists", sender -> Text.send(sender,"&cThe command you're trying to use doesn't exist!", true));
        commandManager.getMessageHandler().register("cmd.no.permission", sender -> Text.send(sender,"&cYou don't have permission to execute this command!", true));
        commandManager.getMessageHandler().register("cmd.wrong.usage", sender -> Text.send(sender,"&cWrong usage for the command!", true));
        commandManager.register(new RealLoginCommand(this));

        new Metrics(this, 12577);

        getLogger().info("RealLogin v" + getDescription().getVersion() + " enabled.");
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public GUIManager getGUIManager() { return this.guiManager; }
}
