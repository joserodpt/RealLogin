package joserodpt.reallogin.player;

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
import joserodpt.reallogin.config.RLPlayerLegacyConfig;
import joserodpt.reallogin.utils.PBKDF2;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    private final RealLogin rl;

    public PlayerListener(RealLogin rl) {
        this.rl = rl;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        rl.getPlayerManager().setupPlayerLogin(e.getPlayer());

        // check if player has legacy pin data
        String name = e.getPlayer().getName();
        String pass = RLPlayerLegacyConfig.file().getString(name);
        if (pass != null && !pass.isEmpty()) {
            rl.getLogger().info("Migrating player data for " + name + "...");
            String password;
            try {
                password = PBKDF2.hash(RLPlayerLegacyConfig.file().getString(name));
                rl.getDatabaseManager().savePlayerData(new PlayerDataRow(Bukkit.getPlayer(name), password), false);
            } catch (Exception ex) {
                rl.getLogger().severe("Error while migrating the player data for " + name + ":");
                ex.printStackTrace();
                return;
            }

            RLPlayerLegacyConfig.file().set(name, null);
            RLPlayerLegacyConfig.save();
            rl.getLogger().info("Player data for " + name + " migrated successfully.");
        }

        this.rl.getDatabaseManager().savePlayerLogin(e.getPlayer(), true);

        if (rl.getPlayerManager().doesPlayerHaveSession(e.getPlayer().getUniqueId())) {
            return;
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(rl, () -> {
            if (rl.getDatabaseManager().isPlayerRegistered(e.getPlayer())) {
                rl.getGUIManager().openLoginGUI(e.getPlayer());
            } else {
                rl.getGUIManager().openRegisterGUI(e.getPlayer());
            }

            if (RLConfig.file().getBoolean("Settings.Hide-Inventories")) {
                rl.getPlayerManager().addPlayerInventory(e.getPlayer().getUniqueId(), e.getPlayer().getInventory().getContents());
                e.getPlayer().getInventory().clear();
            }
        }, 5L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (rl.getPlayerManager().hasPlayerInventory(e.getPlayer().getUniqueId())) {
            e.getPlayer().getInventory().setContents(rl.getPlayerManager().getPlayerInventory(e.getPlayer().getUniqueId()));
        }
    }

    @EventHandler
    public void onWalk(PlayerMoveEvent e) {
        if (rl.getPlayerManager().isPlayerFronzen(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (rl.getPlayerManager().isPlayerFronzen(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (rl.getPlayerManager().isPlayerFronzen(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (rl.getPlayerManager().isPlayerFronzen(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        if (rl.getPlayerManager().isPlayerFronzen(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }
}
