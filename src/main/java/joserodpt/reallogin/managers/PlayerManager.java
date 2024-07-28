package joserodpt.reallogin.managers;

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

import joserodpt.reallogin.RealLogin;
import joserodpt.reallogin.config.RLConfig;
import joserodpt.reallogin.utils.Text;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final RealLogin rl;

    public PlayerManager(RealLogin rl) {
        this.rl = rl;
        startTickTask();
    }

    private final Map<UUID, String> pin = new HashMap<>();
    private final Map<UUID, ItemStack[]> inv = new HashMap<>();
    private final Map<UUID, Long> sessionTime = new HashMap<>();

    private BukkitTask task;

    public void startTickTask() {
        if (RLConfig.file().getLong("Settings.Max-Session-Time") <= 0) {
            return;
        }
        if (task == null) {
            task = new BukkitRunnable() {
                @Override
                public void run() {
                    //fix concurrent modification exception
                    new HashMap<>(sessionTime).forEach((uuid, time) -> {
                        if (time <= 0) {
                            sessionTime.remove(uuid);
                        } else {
                            sessionTime.put(uuid, time - 1);
                        }
                    });
                }
            }.runTaskTimer(rl, 0, 20);
        }
    }

    public void stopTickTask() {
        if (task != null) {
            task.cancel();
        }
    }

    public void setupPlayerLogin(Player p) {
        if (rl.getPlayerManager().doesPlayerHaveSession(p.getUniqueId())) {
            return;
        }

        this.pin.put(p.getUniqueId(), "");
    }

    public boolean hasPlayerInventory(UUID uuid) {
        return this.inv.containsKey(uuid);
    }

    public void addPlayerInventory(UUID uuid, ItemStack[] inv) {
        this.inv.put(uuid, inv);
    }

    public ItemStack[] getPlayerInventory(UUID uuid) {
        return this.inv.remove(uuid);
    }

    public boolean isPlayerFronzen(UUID uuid) {
        return this.pin.containsKey(uuid);
    }

    public String getPlayerPIN(UUID uniqueId) {
        return this.pin.get(uniqueId);
    }

    public void setPlayerPin(UUID uniqueId, String currentPIN) {
        this.pin.put(uniqueId, currentPIN);
    }

    public void loginGrantedForPlayer(UUID uniqueId) {
        this.pin.remove(uniqueId);
        if (RLConfig.file().getLong("Settings.Max-Session-Time") > 0)
            this.sessionTime.put(uniqueId, RLConfig.file().getLong("Settings.Max-Session-Time"));
    }

    public boolean doesPlayerHaveSession(UUID uniqueId) {
        return this.sessionTime.containsKey(uniqueId);
    }

    public String getSessionTimeLeft(UUID uuid) {
        return Text.formatTimestampTime(this.sessionTime.get(uuid) * 1000);
    }
}
