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

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.message.MessageKey;
import joserodpt.reallogin.config.RLConfig;
import joserodpt.reallogin.config.RLPlayerLegacyConfig;
import joserodpt.reallogin.config.RLSQLConfig;
import joserodpt.reallogin.managers.DatabaseManager;
import joserodpt.reallogin.managers.GUIManager;
import joserodpt.reallogin.managers.PlayerManager;
import joserodpt.reallogin.player.PlayerListener;
import joserodpt.reallogin.utils.GUIBuilder;
import joserodpt.reallogin.utils.Text;
import joserodpt.realpermissions.api.RealPermissionsAPI;
import joserodpt.realpermissions.api.pluginhook.ExternalPlugin;
import joserodpt.realpermissions.api.pluginhook.ExternalPluginPermission;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

public final class RealLogin extends JavaPlugin {

    public PlayerManager playerManager;
    public GUIManager guiManager;
    public DatabaseManager databaseManager;

    public PlayerManager getPlayerManager() { return this.playerManager; }

    public GUIManager getGUIManager() { return this.guiManager; }

    @Override
    public void onEnable() {
        printASCII();
        new Metrics(this, 12577);

        final long start = System.currentTimeMillis();

        RLConfig.setup(this);
        RLPlayerLegacyConfig.setup(this);
        RLSQLConfig.setup(this);

        try {
            getLogger().info("Loading database...");
            databaseManager = new DatabaseManager(this);
            getLogger().info("Database loaded successfully.");
        } catch (SQLException e) {
            getLogger().warning("Error while trying to connect to the database. Disabling plugin.");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (RLConfig.file().getBoolean("Settings.Bungeecord.Enabled")) {
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            getLogger().info("BungeeCord mode is enabled.");
        }

        playerManager = new PlayerManager(this);
        guiManager = new GUIManager(this);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getPluginManager().registerEvents(GUIBuilder.getListener(), this);

        BukkitCommandManager<CommandSender> commandManager = BukkitCommandManager.create(this);

        commandManager.registerMessage(MessageKey.UNKNOWN_COMMAND, (sender, context) -> Text.send(sender,"&cThe command you're trying to use doesn't exist!", true));
        commandManager.registerMessage(BukkitMessageKey.NO_PERMISSION, (sender, context) -> Text.send(sender,"&cYou don't have permission to execute this command!", true));
        commandManager.registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, (sender, context) -> Text.send(sender,"&cWrong usage for the command!", true));
        commandManager.registerCommand(new RealLoginCommand(this));

        if (getServer().getPluginManager().getPlugin("RealPermissions") != null) {
            //register RealMines permissions onto RealPermissions
            try {
                RealPermissionsAPI.getInstance().getHooksAPI().addHook(new ExternalPlugin(this.getDescription().getName(), "&fReal&7Login", this.getDescription().getDescription(), Material.TRIPWIRE_HOOK, Arrays.asList(
                        new ExternalPluginPermission("reallogin.admin", "Allow access to the main operator commands of RealMines.", Arrays.asList("rlog reload", "rlog deletepin", "rlog setpin")),
                        new ExternalPluginPermission("reallogin.resetpin", "Allow permission to reset the player's own pin.", Collections.singletonList("rlog resetpin"))
                ), this.getDescription().getVersion()));
            } catch (Exception e) {
                getLogger().warning("Error while trying to register RealMines permissions onto RealPermissions.");
                e.printStackTrace();
            }
        }

        getLogger().info("Finished loading in " + ((System.currentTimeMillis() - start) / 1000F) + " seconds.");
        getLogger().info("<------------------ RealLogin vPT ------------------>".replace("PT", this.getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling RealLogin...");
        if (RLConfig.file().getBoolean("Settings.Hide-Inventories")) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (playerManager.getPlayerInventory(player.getUniqueId()) == null) return;

                player.getInventory().setContents(playerManager.getPlayerInventory(player.getUniqueId()));
            });
        }
        playerManager.stopTickTask();
        getLogger().info("RealLogin disabled.");
    }

    private void printASCII() {
        logWithColor("  &b _____            _ _                 _");
        logWithColor("  &b|  __ \\          | | |               (_)");
        logWithColor("  &b| |__) |___  __ _| | |     ___   __ _ _ _ __");
        logWithColor("  &b|  _  // _ \\/ _` | | |    / _ \\ / _` | | '_ \\");
        logWithColor("  &b| | \\ \\  __/ (_| | | |___| (_) | (_| | | | | |");
        logWithColor("  &b|_|  \\_\\___|\\__,_|_|______\\___/ \\__, |_|_| |_|");
        logWithColor("  &b                                 __/ |");
        logWithColor("      Made by: &9JoseGamer_PT &b      |___/  &8v: &9" + this.getDescription().getVersion());
        logWithColor("");
    }

    public void logWithColor(String s) {
        getServer().getConsoleSender().sendMessage("[" + this.getDescription().getName() + "] " + Text.color(s));
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
