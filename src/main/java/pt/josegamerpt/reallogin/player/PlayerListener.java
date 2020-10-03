package pt.josegamerpt.reallogin.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import pt.josegamerpt.reallogin.RealLogin;
import pt.josegamerpt.reallogin.config.Players;

public class PlayerListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        RealLogin.prepPl(e.getPlayer());

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RealLogin.getPlugin(RealLogin.class), () -> {
            openGUI(e.getPlayer());
            RealLogin.inv.put(e.getPlayer(), e.getPlayer().getInventory().getContents());
            e.getPlayer().getInventory().clear();
        }, 10L);
    }

    public void openGUI(Player p) {
        if (Players.file().get(p.getName()) != null) {
            PlayerManager.openPin(p, 0);
        } else {
            PlayerManager.openRegister(p, 0);
        }
    }

    @EventHandler
    public void onWalk(PlayerMoveEvent e) {
        if (RealLogin.frozen.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (RealLogin.frozen.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (RealLogin.frozen.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}
