package pt.josegamerpt.reallogin.player;

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
        if (Players.file().get(e.getPlayer().getName()) != null)
        {
            PlayerManager.openPin(e.getPlayer(), 0);
        } else {
            PlayerManager.openRegister(e.getPlayer(), 0);
        }
    }

    @EventHandler
    public void onWalk(PlayerMoveEvent e)
    {
        if (RealLogin.frozen.contains(e.getPlayer()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        if (RealLogin.frozen.contains(e.getPlayer()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e)
    {
        if (RealLogin.frozen.contains(e.getPlayer()))
        {
            e.setCancelled(true);
        }
    }
}
