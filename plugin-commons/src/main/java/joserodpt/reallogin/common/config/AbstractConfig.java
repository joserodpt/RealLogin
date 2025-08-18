package joserodpt.reallogin.common.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Level;
import lombok.extern.java.Log;

@Log
public abstract class AbstractConfig {

    private final File configFile;
    private YamlDocument yamlDocument;

    public AbstractConfig(final File configFile) {
        this.configFile = configFile;
    }

    public final void loadConfig(final InputStream defaultInputStream) {
        try {
            this.yamlDocument = YamlDocument.create(
                    this.configFile,
                    defaultInputStream,
                    ConfigSettings.GENERAL_SETTINGS,
                    ConfigSettings.LOADER_SETTINGS,
                    ConfigSettings.DUMPER_SETTINGS,
                    ConfigSettings.UPDATER_SETTINGS
            );
        } catch (final Exception exception) {
            log.log(Level.SEVERE, "Failed to load config file: " + this.configFile.getAbsolutePath(), exception);
        }
    }

    public final boolean reloadConfig() {
        try {
            this.yamlDocument.reload();
        } catch (final Exception exception) {
            log.log(Level.SEVERE, "Failed to reload config: " + this.configFile.getName(), exception);
        }
        return false;
    }

    public final <T> T get(final String key, final Class<T> type) {
        return this.yamlDocument.getAs(key, type);
    }
}