package joserodpt.reallogin.common.user;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import joserodpt.reallogin.common.config.impl.GlobalConfig;
import lombok.extern.java.Log;

@Log
public class UserManager {

    private final Map<UUID, User> users = new ConcurrentHashMap<>();
    private final GlobalConfig globalConfig;

    public UserManager(final GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    public void createUser(final UUID uniqueId) {
        try {
            this.users.computeIfAbsent(uniqueId, User::new);
        } catch (final Exception exception) {
            log.log(Level.SEVERE, "Failed to create user: " + uniqueId.toString(), exception);
        }
    }
}