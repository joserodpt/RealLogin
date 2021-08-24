package pt.josegamerpt.reallogin;

import me.mattstudios.mf.base.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pt.josegamerpt.reallogin.config.Config;
import pt.josegamerpt.reallogin.config.Players;
import pt.josegamerpt.reallogin.player.PlayerListener;
import pt.josegamerpt.reallogin.utils.Text;

import java.util.ArrayList;
import java.util.HashMap;

public final class RealLogin extends JavaPlugin {

    public static HashMap<Player, String> pin = new HashMap<>();
    public static HashMap<Player, ItemStack[]> inv = new HashMap<>();
    public static ArrayList<Player> frozen = new ArrayList<>();
    private static String prefixColor = "&fReal&7Login ";
    private static String version;
    PluginManager pm = Bukkit.getPluginManager();

    public static String getPrefix() {
        return prefixColor;
    }

    public static void prepPl(Player p) {
        pin.put(p, "");
        frozen.add(p);
    }

    public static String getVersion() {
        return version;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        version = getDescription().getVersion();

        //config
        saveDefaultConfig();
        Config.setup(this);
        Players.setup(this);

        prefixColor = Config.file().getString("Strings.Prefix");

        //commands
        CommandManager commandManager = new CommandManager(this);
        commandManager.getMessageHandler().register("cmd.no.console", sender -> sender.sendMessage(Text.color(prefixColor + "&f| &cThis command can't be used in the console!")));
        commandManager.getMessageHandler().register("cmd.no.exists", sender -> sender.sendMessage(Text.color(prefixColor + "&f| &cThe command you're trying to use doesn't exist!")));
        commandManager.getMessageHandler().register("cmd.no.permission", sender -> sender.sendMessage(Text.color(prefixColor + "&f| &cYou don't have permission to execute this command!")));
        commandManager.getMessageHandler().register("cmd.wrong.usage", sender -> sender.sendMessage(Text.color(prefixColor + "&f| &c&cWrong usage for the command!")));
        commandManager.register(new Command());
        pm.registerEvents(new PlayerListener(), this);

        new Metrics(this, 12577);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
