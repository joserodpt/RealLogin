package joserodpt.reallogin.common.config;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import lombok.extern.java.Log;

@Log
public class ConfigBuilder<C extends AbstractConfig> {

    private Class<C> configClass;
    private File configFile;
    private InputStream defaultConfigInputStream;

    private ConfigBuilder() {
    }

    public static <C extends AbstractConfig> ConfigBuilder<C> builder() {
        return new ConfigBuilder<>();
    }

    public ConfigBuilder<C> withConfigClass(final Class<C> configClass) {
        this.configClass = configClass;
        return this;
    }

    public ConfigBuilder<C> withConfigFile(final File configFile) {
        this.configFile = configFile;
        return this;
    }

    public ConfigBuilder<C> withDefaults(final InputStream defaultConfigInputStream) {
        this.defaultConfigInputStream = defaultConfigInputStream;
        return this;
    }

    @SuppressWarnings("unchecked")
    public C createConfig() {
        try {
            final Constructor<?> constructor = this.configClass.getConstructor(File.class);
            final C configInstance = (C) constructor.newInstance(this.configFile);
            configInstance.loadConfig(this.defaultConfigInputStream);
            return configInstance;
        } catch (final Exception exception) {
            log.log(Level.SEVERE, "Could not construct config: " + this.configClass.getName(), exception);
        }
        return null;
    }
}