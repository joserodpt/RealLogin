package pt.josegamerpt.reallogin.player;

import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import pt.josegamerpt.reallogin.RealLogin;
import pt.josegamerpt.reallogin.config.Config;
import pt.josegamerpt.reallogin.config.Players;
import pt.josegamerpt.reallogin.utils.Itens;
import pt.josegamerpt.reallogin.utils.Text;

import java.util.Arrays;
import java.util.Collections;

public class PlayerManager {

    public static void openPin(Player p, int i) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RealLogin.getPlugin(RealLogin.class), () -> {
            Gui gui = new Gui(3, Text.color(Config.file().getString("Strings.GUI.Login")));
            p.setInvulnerable(true);

            //leaveServer
            GuiItem g = new GuiItem(Itens.createItem(Material.OAK_DOOR, 1, Config.file().getString("Strings.GUI.Items.Leave-Server.Name"), Collections.singletonList(Config.file().getString("Strings.GUI.Items.Leave-Server.Description"))), event -> p.kickPlayer(Text.color(RealLogin.getPrefix() + "\n&cSais-te do servidor.")));
            gui.setItem(2, 2, g);

            GuiItem i1 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, "&6&l1"), event -> digitPin(p, 1, gui));
            gui.setItem(1, 4, i1);
            GuiItem i2 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 2, "&6&l2"), event -> digitPin(p, 2, gui));
            gui.setItem(1, 5, i2);
            GuiItem i3 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 3, "&6&l3"), event -> digitPin(p, 3, gui));
            gui.setItem(1, 6, i3);
            GuiItem i4 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 4, "&6&l4"), event -> digitPin(p, 4, gui));
            gui.setItem(2, 4, i4);
            GuiItem i5 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 5, "&6&l5"), event -> digitPin(p, 5, gui));
            gui.setItem(2, 5, i5);
            GuiItem i6 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 6, "&6&l6"), event -> digitPin(p, 6, gui));
            gui.setItem(2, 6, i6);
            GuiItem i7 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 7, "&6&l7"), event -> digitPin(p, 7, gui));
            gui.setItem(3, 4, i7);
            GuiItem i8 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 8, "&6&l8"), event -> digitPin(p, 8, gui));
            gui.setItem(3, 5, i8);
            GuiItem i9 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 9, "&6&l9"), event -> digitPin(p, 9, gui));
            gui.setItem(3, 6, i9);

            GuiItem removeLetter = new GuiItem(Itens.createItem(Material.BARRIER, 1, Config.file().getString("Strings.GUI.Items.Remove-Number.Name"), Collections.singletonList(Config.file().getString("Strings.GUI.Items.Remove-Number.Description"))), event -> removeNumber(p,  gui));
            gui.setItem(2, 8, removeLetter);

            gui.open(p);
            gui.setDefaultClickAction(event -> event.setCancelled(true));
            gui.setCloseGuiAction(event -> openPin(p, 1));

            switch (i)
            {
                case 0:
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 20);
                    break;
                case 1:
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 20);
                    break;
            }
        }, 5L);
    }

    private static void removeNumber(Player p, Gui g) {
        if (RealLogin.pin.get(p).length() > 0) {
            RealLogin.pin.put(p, RealLogin.pin.get(p).substring(0, RealLogin.pin.get(p).length() - 1));
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 20);
            g.updateTitle(Text.color("&bPIN: &8&l" + RealLogin.pin.get(p)));
            checkPin(p, g);
        }
    }

    public static void digitPin(Player p, int i, Gui g) {
        RealLogin.pin.put(p, RealLogin.pin.get(p) + i);
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 50);
        g.updateTitle(Text.color("&bPIN: &8&l" + RealLogin.pin.get(p)));

        checkPin(p, g);
    }

    private static void checkPin(Player p, Gui g) {
        if (RealLogin.pin.get(p).equalsIgnoreCase(Players.file().getString(p.getName())))
        {
            g.setCloseGuiAction(event -> p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 20));
            g.close(p);
            p.setInvulnerable(false);
            RealLogin.frozen.remove(p);

            p.sendTitle(Text.color(Config.file().getString("Strings.Titles.Login.Up")), Text.color(Config.file().getString("Strings.Titles.Login.Down")),7, 50, 10);
        }
    }


    public static void openRegister(Player p, int i) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RealLogin.getPlugin(RealLogin.class), () -> {

            Gui gui = new Gui(3, Text.color(Config.file().getString("Strings.GUI.Register")));
            p.setInvulnerable(true);
            RealLogin.pin.put(p, "");

            //leaveServer
            GuiItem g = new GuiItem(Itens.createItem(Material.OAK_DOOR, 1, Config.file().getString("Strings.GUI.Items.Leave-Server.Name"), Collections.singletonList(Config.file().getString("Strings.GUI.Items.Leave-Server.Description"))), event -> p.kickPlayer(Text.color(RealLogin.getPrefix() + "\n&cSais-te do servidor.")));
            gui.setItem(2, 2, g);

            GuiItem i1 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, "&6&l1"), event -> digitPin(p, 1, gui));
            gui.setItem(1, 4, i1);
            GuiItem i2 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 2, "&6&l2"), event -> digitPin(p, 2, gui));
            gui.setItem(1, 5, i2);
            GuiItem i3 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 3, "&6&l3"), event -> digitPin(p, 3, gui));
            gui.setItem(1, 6, i3);
            GuiItem i4 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 4, "&6&l4"), event -> digitPin(p, 4, gui));
            gui.setItem(2, 4, i4);
            GuiItem i5 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 5, "&6&l5"), event -> digitPin(p, 5, gui));
            gui.setItem(2, 5, i5);
            GuiItem i6 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 6, "&6&l6"), event -> digitPin(p, 6, gui));
            gui.setItem(2, 6, i6);
            GuiItem i7 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 7, "&6&l7"), event -> digitPin(p, 7, gui));
            gui.setItem(3, 4, i7);
            GuiItem i8 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 8, "&6&l8"), event -> digitPin(p, 8, gui));
            gui.setItem(3, 5, i8);
            GuiItem i9 = new GuiItem(Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 9, "&6&l9"), event -> digitPin(p, 9, gui));
            gui.setItem(3, 6, i9);

            GuiItem removeLetter = new GuiItem(Itens.createItem(Material.EMERALD, 1, Config.file().getString("Strings.GUI.Items.Confirm-Pin.Name"), Arrays.asList(Config.file().getString("Strings.GUI.Items.Confirm-Pin.Description"))), event -> confirmar(event.getClick(), p, RealLogin.pin.get(p), gui));
            gui.setItem(2, 8, removeLetter);

            gui.open(p);
            gui.setDefaultClickAction(event -> event.setCancelled(true));
            gui.setCloseGuiAction(event -> openRegister(p, 1));

            switch (i)
            {
                case 0:
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 20);
                    break;
                case 1:
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 20);
                    break;
            }
        }, 5L);
    }

    private static void confirmar(ClickType e, Player p, String s, Gui g) {
        if (e == ClickType.RIGHT) {
            removeNumber(p, g);
        } else {
            Players.file().set(p.getName(), s);
            Players.save();

            g.setCloseGuiAction(event -> p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 20));
            g.close(p);
            p.setInvulnerable(false);

            RealLogin.frozen.remove(p);

            p.sendTitle(Text.color(Config.file().getString("Strings.Titles.Registered.Up")), Text.color(Config.file().getString("Strings.Titles.Registered.Down")), 7, 50, 10);

        }
    }
}
