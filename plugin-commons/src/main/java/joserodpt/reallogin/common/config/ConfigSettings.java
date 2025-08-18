package joserodpt.reallogin.common.config;

import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;

final class ConfigSettings {

    static final GeneralSettings GENERAL_SETTINGS = GeneralSettings.DEFAULT;
    static final LoaderSettings LOADER_SETTINGS = LoaderSettings.builder()
            .setAutoUpdate(true)
            .setDetailedErrors(true)
            .setMaxCollectionAliases(256)
            .build();
    static final DumperSettings DUMPER_SETTINGS = DumperSettings.builder()
            .setEncoding(DumperSettings.Encoding.UNICODE)
            .setIndentation(2)
            .build();
    static final UpdaterSettings UPDATER_SETTINGS = UpdaterSettings.builder()
            .setVersioning(new BasicVersioning("Config-Version"))
            .build();

    private ConfigSettings() {
    }
}