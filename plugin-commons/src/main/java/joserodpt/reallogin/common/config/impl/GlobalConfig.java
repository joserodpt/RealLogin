package joserodpt.reallogin.common.config.impl;

import java.io.File;
import joserodpt.reallogin.common.config.AbstractConfig;

public class GlobalConfig extends AbstractConfig {

    public GlobalConfig(final File configFile) {
        super(configFile);
    }

    public int getLoginCountdown() {
        return this.get("login-countdown-seconds", int.class);
    }
}
