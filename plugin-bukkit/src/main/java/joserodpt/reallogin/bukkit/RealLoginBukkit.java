package joserodpt.reallogin.bukkit;

import java.io.File;
import joserodpt.reallogin.bukkit.config.impl.BukkitConfig;
import joserodpt.reallogin.common.config.ConfigBuilder;
import joserodpt.reallogin.common.config.ConfigFiles;
import joserodpt.reallogin.common.config.impl.DatabaseConfig;
import joserodpt.reallogin.common.config.impl.GlobalConfig;
import joserodpt.reallogin.common.config.impl.MessagesConfig;
import joserodpt.reallogin.common.config.impl.BlockadesConfig;
import joserodpt.reallogin.common.config.impl.RestrictionsConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class RealLoginBukkit extends JavaPlugin {

    private GlobalConfig globalConfig;
    private BukkitConfig bukkitConfig;
    private DatabaseConfig databaseConfig;
    private BlockadesConfig blockadesConfig;
    private RestrictionsConfig restrictionsConfig;
    private MessagesConfig messagesConfig;

    @Override
    public void onEnable() {
        final File dataFolder = this.getDataFolder();
        if (!dataFolder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dataFolder.mkdirs();
        }
        this.globalConfig = ConfigBuilder.<GlobalConfig>builder()
                .withConfigClass(GlobalConfig.class)
                .withConfigFile(new File(dataFolder, ConfigFiles.GLOBAL_CONFIG))
                .withDefaults(this.getResource(ConfigFiles.GLOBAL_CONFIG))
                .createConfig();
        this.bukkitConfig = ConfigBuilder.<BukkitConfig>builder()
                .withConfigClass(BukkitConfig.class)
                .withConfigFile(new File(dataFolder, ConfigFiles.BUKKIT_CONFIG))
                .withDefaults(this.getResource(ConfigFiles.BUKKIT_CONFIG))
                .createConfig();
        this.databaseConfig = ConfigBuilder.<DatabaseConfig>builder()
                .withConfigClass(DatabaseConfig.class)
                .withConfigFile(new File(dataFolder, ConfigFiles.DATABASE_CONFIG))
                .withDefaults(this.getResource(ConfigFiles.DATABASE_CONFIG))
                .createConfig();
        this.blockadesConfig = ConfigBuilder.<BlockadesConfig>builder()
                .withConfigClass(BlockadesConfig.class)
                .withConfigFile(new File(dataFolder, ConfigFiles.BLOCKADES_CONFIG))
                .withDefaults(this.getResource(ConfigFiles.BLOCKADES_CONFIG))
                .createConfig();
        this.restrictionsConfig = ConfigBuilder.<RestrictionsConfig>builder()
                .withConfigClass(RestrictionsConfig.class)
                .withConfigFile(new File(dataFolder, ConfigFiles.RESTRICTIONS_CONFIG))
                .withDefaults(this.getResource(ConfigFiles.RESTRICTIONS_CONFIG))
                .createConfig();
        this.messagesConfig = ConfigBuilder.<MessagesConfig>builder()
                .withConfigClass(MessagesConfig.class)
                .withConfigFile(new File(dataFolder, ConfigFiles.MESSAGES_CONFIG))
                .withDefaults(this.getResource(ConfigFiles.MESSAGES_CONFIG))
                .createConfig();
    }

    @Override
    public void onDisable() {

    }
}
