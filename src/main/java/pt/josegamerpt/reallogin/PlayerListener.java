package pt.josegamerpt.reallogin;

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
 * @author José Rodrigues
 * @link https://github.com/joserodpt/RealLogin
 */

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import pt.josegamerpt.reallogin.config.RLConfig;
import pt.josegamerpt.reallogin.config.RLPlayerConfig;

public class PlayerListener implements Listener {

    private RealLogin rl;
    public PlayerListener(RealLogin rl) {
        this.rl = rl;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        rl.getPlayerManager().setupPlayerLogin(e.getPlayer());

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RealLogin.getPlugin(RealLogin.class), () -> {
            openGUI(e.getPlayer());
            if (RLConfig.file().getBoolean("Settings.Hide-Inventories")) {
                rl.getPlayerManager().getPlayerInventories().put(e.getPlayer(), e.getPlayer().getInventory().getContents());
                e.getPlayer().getInventory().clear();
            }
        }, 10L);
    }

    public void openGUI(Player p) {
        if (RLPlayerConfig.file().get(p.getName()) != null) {
            rl.getGUIManager().openPinGUI(p, 0);
        } else {
            rl.getGUIManager().openRegisterGUI(p, Sound.ENTITY_VILLAGER_YES);
        }
    }

    @EventHandler
    public void onWalk(PlayerMoveEvent e) {
        if (rl.getPlayerManager().getFrozenPlayers().contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (rl.getPlayerManager().getFrozenPlayers().contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (rl.getPlayerManager().getFrozenPlayers().contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        if (rl.getPlayerManager().getFrozenPlayers().contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e)
    {
        if (rl.getPlayerManager().getFrozenPlayers().contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}
