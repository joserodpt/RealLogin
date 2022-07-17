package pt.josegamerpt.reallogin.player;

import dev.dbassett.skullcreator.SkullCreator;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import pt.josegamerpt.reallogin.RealLogin;
import pt.josegamerpt.reallogin.config.Config;
import pt.josegamerpt.reallogin.config.Players;
import pt.josegamerpt.reallogin.utils.ItemBuilder;
import pt.josegamerpt.reallogin.utils.Text;

import java.util.Collections;

public class PlayerManager {

    public static void openPin(Player p, int i) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RealLogin.getPlugin(RealLogin.class), () -> {
            Gui gui = Gui.gui().rows(3).title(Component.text(Text.color(Config.file().getString("Strings.GUI.Login")))).create();
            p.setInvulnerable(true);

            //leaveServer
            GuiItem g = new GuiItem(new ItemBuilder(Material.OAK_DOOR).setDisplayName(Config.file().getString("Strings.GUI.Items.Leave-Server.Name")).setLore(Collections.singletonList(Config.file().getString("Strings.GUI.Items.Leave-Server.Description"))).get(), event -> p.kickPlayer(Text.color(RealLogin.getPrefix() + Config.file().getString("Strings.Kick-Message"))));
            gui.setItem(2, 2, g);

            pinItems(gui, p);

            GuiItem removeLetter = new GuiItem(new ItemBuilder(Material.LAVA_BUCKET).setDisplayName(Config.file().getString("Strings.GUI.Items.Remove-Number.Name")).setLore(Collections.singletonList(Config.file().getString("Strings.GUI.Items.Remove-Number.Description"))).get(), event -> removeNumber(p, gui));
            gui.setItem(2, 8, removeLetter);

            gui.open(p);
            gui.setDefaultClickAction(event -> event.setCancelled(true));
            gui.setCloseGuiAction(event -> openPin(p, 1));

            switch (i) {
                case 0:
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 20);
                    break;
                case 1:
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 20);
                    break;
            }
        }, 5L);
    }

    private static void pinItems(Gui gui, Player p) {
        if (Config.file().getBoolean("Use-Custom-Heads")) {
            GuiItem i1 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFiYzJiY2ZiMmJkMzc1OWU2YjFlODZmYzdhNzk1ODVlMTEyN2RkMzU3ZmMyMDI4OTNmOWRlMjQxYmM5ZTUzMCJ9fX0=")).setDisplayName("&6&l1").get(), event -> digitPin(p, 1, gui));
            gui.setItem(1, 4, i1);
            GuiItem i2 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNkOWVlZWU4ODM0Njg4ODFkODM4NDhhNDZiZjMwMTI0ODVjMjNmNzU3NTNiOGZiZTg0ODczNDE0MTk4NDcifX19")).setDisplayName("&6&l2").get(), event -> digitPin(p, 2, gui));
            gui.setItem(1, 5, i2);
            GuiItem i3 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWQ0ZWFlMTM5MzM4NjBhNmRmNWU4ZTk1NTY5M2I5NWE4YzNiMTVjMzZiOGI1ODc1MzJhYzA5OTZiYzM3ZTUifX19")).setDisplayName("&6&l3").get(), event -> digitPin(p, 3, gui));
            gui.setItem(1, 6, i3);
            GuiItem i4 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDJlNzhmYjIyNDI0MjMyZGMyN2I4MWZiY2I0N2ZkMjRjMWFjZjc2MDk4NzUzZjJkOWMyODU5ODI4N2RiNSJ9fX0=")).setDisplayName("&6&l4").get(), event -> digitPin(p, 4, gui));
            gui.setItem(2, 4, i4);
            GuiItem i5 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ1N2UzYmM4OGE2NTczMGUzMWExNGUzZjQxZTAzOGE1ZWNmMDg5MWE2YzI0MzY0M2I4ZTU0NzZhZTIifX19")).setDisplayName("&6&l5").get(), event -> digitPin(p, 5, gui));
            gui.setItem(2, 5, i5);
            GuiItem i6 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzM0YjM2ZGU3ZDY3OWI4YmJjNzI1NDk5YWRhZWYyNGRjNTE4ZjVhZTIzZTcxNjk4MWUxZGNjNmIyNzIwYWIifX19")).setDisplayName("&6&l6").get(), event -> digitPin(p, 6, gui));
            gui.setItem(2, 6, i6);
            GuiItem i7 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmRiNmViMjVkMWZhYWJlMzBjZjQ0NGRjNjMzYjU4MzI0NzVlMzgwOTZiN2UyNDAyYTNlYzQ3NmRkN2I5In19fQ==")).setDisplayName("&6&l7").get(), event -> digitPin(p, 7, gui));
            gui.setItem(3, 4, i7);
            GuiItem i8 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTkxOTQ5NzNhM2YxN2JkYTk5NzhlZDYyNzMzODM5OTcyMjI3NzRiNDU0Mzg2YzgzMTljMDRmMWY0Zjc0YzJiNSJ9fX0=")).setDisplayName("&6&l8").get(), event -> digitPin(p, 8, gui));
            gui.setItem(3, 5, i8);
            GuiItem i9 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTY3Y2FmNzU5MWIzOGUxMjVhODAxN2Q1OGNmYzY0MzNiZmFmODRjZDQ5OWQ3OTRmNDFkMTBiZmYyZTViODQwIn19fQ==")).setDisplayName("&6&l9").get(), event -> digitPin(p, 9, gui));
            gui.setItem(3, 6, i9);
        } else {
            GuiItem i1 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l1").get(), event -> digitPin(p, 1, gui));
            gui.setItem(1, 4, i1);
            GuiItem i2 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l2").setAmount(2).get(), event -> digitPin(p, 2, gui));
            gui.setItem(1, 5, i2);
            GuiItem i3 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l3").setAmount(3).get(), event -> digitPin(p, 3, gui));
            gui.setItem(1, 6, i3);
            GuiItem i4 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l4").setAmount(4).get(), event -> digitPin(p, 4, gui));
            gui.setItem(2, 4, i4);
            GuiItem i5 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l5").setAmount(5).get(), event -> digitPin(p, 5, gui));
            gui.setItem(2, 5, i5);
            GuiItem i6 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l6").setAmount(6).get(), event -> digitPin(p, 6, gui));
            gui.setItem(2, 6, i6);
            GuiItem i7 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l7").setAmount(7).get(), event -> digitPin(p, 7, gui));
            gui.setItem(3, 4, i7);
            GuiItem i8 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l8").setAmount(8).get(), event -> digitPin(p, 8, gui));
            gui.setItem(3, 5, i8);
            GuiItem i9 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l9").setAmount(9).get(), event -> digitPin(p, 9, gui));
            gui.setItem(3, 6, i9);
        }
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
        if (RealLogin.pin.get(p).length() < Config.file().getInt("Max-Pin-Length"))  {
            RealLogin.pin.put(p, RealLogin.pin.get(p) + i);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 50);
            g.updateTitle(Text.color("&bPIN: &8&l" + RealLogin.pin.get(p)));
            checkPin(p, g);
        }
    }

    private static void checkPin(Player p, Gui g) {
        if (RealLogin.pin.get(p).equalsIgnoreCase(Players.file().getString(p.getName()))) {
            g.setCloseGuiAction(event -> p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 20));
            g.close(p);
            p.setInvulnerable(false);
            RealLogin.frozen.remove(p);

            if (Config.file().getBoolean("Hide-Inventories")) {
                p.getInventory().setContents(RealLogin.inv.get(p));
                RealLogin.inv.remove(p);
            }
            RealLogin.pin.remove(p);
            p.sendTitle(Text.color(Config.file().getString("Strings.Titles.Login.Up")), Text.color(Config.file().getString("Strings.Titles.Login.Down")), 7, 50, 10);
        }
    }


    public static void openRegister(Player p, Sound s) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RealLogin.getPlugin(RealLogin.class), () -> {
            Gui gui = Gui.gui().rows(3).title(Component.text(Text.color(Config.file().getString("Strings.GUI.Register")))).create();

            p.setInvulnerable(true);
            RealLogin.pin.put(p, "");

            //leaveServer
            GuiItem g = new GuiItem(new ItemBuilder(Material.OAK_DOOR).setDisplayName(Config.file().getString("Strings.GUI.Items.Leave-Server.Name")).setLore(Collections.singletonList(Config.file().getString("Strings.GUI.Items.Leave-Server.Description"))).get(), event -> p.kickPlayer(Text.color(RealLogin.getPrefix() + Config.file().getString("Strings.Kick-Message"))));
            gui.setItem(2, 2, g);

            pinItems(gui, p);

            GuiItem removeLetter = new GuiItem(new ItemBuilder(Material.EMERALD).setDisplayName(Config.file().getString("Strings.GUI.Items.Confirm-Pin.Name")).setLore(Collections.singletonList(Config.file().getString("Strings.GUI.Items.Confirm-Pin.Description"))).get(), event -> confirmar(event.getClick(), p, RealLogin.pin.get(p), gui));
            gui.setItem(2, 8, removeLetter);

            gui.open(p);
            gui.setDefaultClickAction(event -> event.setCancelled(true));
            gui.setCloseGuiAction(event -> openRegister(p, Sound.ENTITY_VILLAGER_NO));

            p.playSound(p.getLocation(), s, 1, 20);
        }, 5L);
    }

    private static void confirmar(ClickType e, Player p, String s, Gui g) {
        if (e == ClickType.RIGHT) {
            removeNumber(p, g);
        } else {
            if (s.length() > 1) {
                Players.file().set(p.getName(), s);
                Players.save();

                g.setCloseGuiAction(event -> p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 20));
                g.close(p);
                p.setInvulnerable(false);

                RealLogin.frozen.remove(p);
                if (RealLogin.inv.containsKey(p)) {
                    p.getInventory().setContents(RealLogin.inv.get(p));
                    RealLogin.inv.remove(p);
                }
                RealLogin.pin.remove(p);

                p.sendTitle(Text.color(Config.file().getString("Strings.Titles.Registered.Up")), Text.color(Config.file().getString("Strings.Titles.Registered.Down")), 7, 50, 10);
            }
        }
    }
}
