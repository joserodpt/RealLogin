package pt.josegamerpt.reallogin.managers;

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
import pt.josegamerpt.reallogin.config.RLConfig;
import pt.josegamerpt.reallogin.config.RLPlayerConfig;
import pt.josegamerpt.reallogin.utils.ItemBuilder;
import pt.josegamerpt.reallogin.utils.BungeecordUtils;
import pt.josegamerpt.reallogin.utils.Text;

import java.util.Collections;

public class GUIManager {

    private RealLogin rl;
    public GUIManager(RealLogin rl) {
        this.rl = rl;
    }

    public void openRegisterGUI(Player p, Sound s) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RealLogin.getPlugin(RealLogin.class), () -> {
            Gui gui = Gui.gui().rows(3).title(Component.text(Text.color(RLConfig.file().getString("Strings.GUI.Register")))).create();

            p.setInvulnerable(true);
            this.rl.getPlayerManager().getPlayerPIN().put(p, "");

            //leaveServer
            GuiItem g = new GuiItem(new ItemBuilder(Material.OAK_DOOR).setDisplayName(RLConfig.file().getString("Strings.GUI.Items.Leave-Server.Name")).setLore(Collections.singletonList(RLConfig.file().getString("Strings.GUI.Items.Leave-Server.Description"))).get(), event -> p.kickPlayer(Text.color(Text.getPrefix() + RLConfig.file().getString("Strings.Kick-Message"))));
            gui.setItem(2, 2, g);

            setPinItems(gui, p);

            GuiItem removeLetter = new GuiItem(new ItemBuilder(Material.EMERALD).setDisplayName(RLConfig.file().getString("Strings.GUI.Items.Confirm-Pin.Name")).setLore(Collections.singletonList(RLConfig.file().getString("Strings.GUI.Items.Confirm-Pin.Description"))).get(), event -> confirmAction(event.getClick(), p, this.rl.getPlayerManager().getPlayerPIN().get(p), gui));
            gui.setItem(2, 8, removeLetter);

            gui.open(p);
            gui.setDefaultClickAction(event -> event.setCancelled(true));
            gui.setCloseGuiAction(event -> openRegisterGUI(p, Sound.ENTITY_VILLAGER_NO));

            p.playSound(p.getLocation(), s, 1, 20);
        }, 5L);
    }

    public void openPinGUI(Player p, int i) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RealLogin.getPlugin(RealLogin.class), () -> {
            Gui gui = Gui.gui().rows(3).title(Component.text(Text.color(RLConfig.file().getString("Strings.GUI.Login")))).create();
            p.setInvulnerable(true);

            //leaveServer
            GuiItem g = new GuiItem(new ItemBuilder(Material.OAK_DOOR).setDisplayName(RLConfig.file().getString("Strings.GUI.Items.Leave-Server.Name")).setLore(Collections.singletonList(RLConfig.file().getString("Strings.GUI.Items.Leave-Server.Description"))).get(), event -> p.kickPlayer(Text.color(Text.getPrefix() + RLConfig.file().getString("Strings.Kick-Message"))));
            gui.setItem(2, 2, g);

            setPinItems(gui, p);

            GuiItem removeLetter = new GuiItem(new ItemBuilder(Material.LAVA_BUCKET).setDisplayName(RLConfig.file().getString("Strings.GUI.Items.Remove-Number.Name")).setLore(Collections.singletonList(RLConfig.file().getString("Strings.GUI.Items.Remove-Number.Description"))).get(), event -> removeNumber(p, gui));
            gui.setItem(2, 8, removeLetter);

            gui.open(p);
            gui.setDefaultClickAction(event -> event.setCancelled(true));
            gui.setCloseGuiAction(event -> openPinGUI(p, 1));

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

    public void setPinItems(Gui gui, Player p) {
        if (RLConfig.file().getBoolean("Settings.Use-Custom-Heads")) {
            GuiItem i1 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFiYzJiY2ZiMmJkMzc1OWU2YjFlODZmYzdhNzk1ODVlMTEyN2RkMzU3ZmMyMDI4OTNmOWRlMjQxYmM5ZTUzMCJ9fX0=")).setDisplayName("&6&l1").get(), event -> addNumber(p, 1, gui));
            gui.setItem(1, 4, i1);
            GuiItem i2 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNkOWVlZWU4ODM0Njg4ODFkODM4NDhhNDZiZjMwMTI0ODVjMjNmNzU3NTNiOGZiZTg0ODczNDE0MTk4NDcifX19")).setDisplayName("&6&l2").get(), event -> addNumber(p, 2, gui));
            gui.setItem(1, 5, i2);
            GuiItem i3 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWQ0ZWFlMTM5MzM4NjBhNmRmNWU4ZTk1NTY5M2I5NWE4YzNiMTVjMzZiOGI1ODc1MzJhYzA5OTZiYzM3ZTUifX19")).setDisplayName("&6&l3").get(), event -> addNumber(p, 3, gui));
            gui.setItem(1, 6, i3);
            GuiItem i4 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDJlNzhmYjIyNDI0MjMyZGMyN2I4MWZiY2I0N2ZkMjRjMWFjZjc2MDk4NzUzZjJkOWMyODU5ODI4N2RiNSJ9fX0=")).setDisplayName("&6&l4").get(), event -> addNumber(p, 4, gui));
            gui.setItem(2, 4, i4);
            GuiItem i5 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ1N2UzYmM4OGE2NTczMGUzMWExNGUzZjQxZTAzOGE1ZWNmMDg5MWE2YzI0MzY0M2I4ZTU0NzZhZTIifX19")).setDisplayName("&6&l5").get(), event -> addNumber(p, 5, gui));
            gui.setItem(2, 5, i5);
            GuiItem i6 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzM0YjM2ZGU3ZDY3OWI4YmJjNzI1NDk5YWRhZWYyNGRjNTE4ZjVhZTIzZTcxNjk4MWUxZGNjNmIyNzIwYWIifX19")).setDisplayName("&6&l6").get(), event -> addNumber(p, 6, gui));
            gui.setItem(2, 6, i6);
            GuiItem i7 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmRiNmViMjVkMWZhYWJlMzBjZjQ0NGRjNjMzYjU4MzI0NzVlMzgwOTZiN2UyNDAyYTNlYzQ3NmRkN2I5In19fQ==")).setDisplayName("&6&l7").get(), event -> addNumber(p, 7, gui));
            gui.setItem(3, 4, i7);
            GuiItem i8 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTkxOTQ5NzNhM2YxN2JkYTk5NzhlZDYyNzMzODM5OTcyMjI3NzRiNDU0Mzg2YzgzMTljMDRmMWY0Zjc0YzJiNSJ9fX0=")).setDisplayName("&6&l8").get(), event -> addNumber(p, 8, gui));
            gui.setItem(3, 5, i8);
            GuiItem i9 = new GuiItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTY3Y2FmNzU5MWIzOGUxMjVhODAxN2Q1OGNmYzY0MzNiZmFmODRjZDQ5OWQ3OTRmNDFkMTBiZmYyZTViODQwIn19fQ==")).setDisplayName("&6&l9").get(), event -> addNumber(p, 9, gui));
            gui.setItem(3, 6, i9);
        } else {
            GuiItem i1 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l1").get(), event -> addNumber(p, 1, gui));
            gui.setItem(1, 4, i1);
            GuiItem i2 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l2").setAmount(2).get(), event -> addNumber(p, 2, gui));
            gui.setItem(1, 5, i2);
            GuiItem i3 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l3").setAmount(3).get(), event -> addNumber(p, 3, gui));
            gui.setItem(1, 6, i3);
            GuiItem i4 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l4").setAmount(4).get(), event -> addNumber(p, 4, gui));
            gui.setItem(2, 4, i4);
            GuiItem i5 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l5").setAmount(5).get(), event -> addNumber(p, 5, gui));
            gui.setItem(2, 5, i5);
            GuiItem i6 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l6").setAmount(6).get(), event -> addNumber(p, 6, gui));
            gui.setItem(2, 6, i6);
            GuiItem i7 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l7").setAmount(7).get(), event -> addNumber(p, 7, gui));
            gui.setItem(3, 4, i7);
            GuiItem i8 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l8").setAmount(8).get(), event -> addNumber(p, 8, gui));
            gui.setItem(3, 5, i8);
            GuiItem i9 = new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l9").setAmount(9).get(), event -> addNumber(p, 9, gui));
            gui.setItem(3, 6, i9);
        }
    }

    public void removeNumber(Player p, Gui g) {
        if (!this.rl.getPlayerManager().getPlayerPIN().get(p).isEmpty()) {
            this.rl.getPlayerManager().getPlayerPIN().put(p, this.rl.getPlayerManager().getPlayerPIN().get(p).substring(0, this.rl.getPlayerManager().getPlayerPIN().get(p).length() - 1));
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 20);
            g.updateTitle(Text.color("&bPIN: &8&l" + this.rl.getPlayerManager().getPlayerPIN().get(p)));
            checkPIN(p, g);
        }
    }

    public void addNumber(Player p, int i, Gui g) {
        if (this.rl.getPlayerManager().getPlayerPIN().get(p).length() < RLConfig.file().getInt("Settings.Max-Pin-Length"))  {
            this.rl.getPlayerManager().getPlayerPIN().put(p, this.rl.getPlayerManager().getPlayerPIN().get(p) + i);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 50);
            g.updateTitle(Text.color("&bPIN: &8&l" + this.rl.getPlayerManager().getPlayerPIN().get(p)));
            checkPIN(p, g);
        }
    }

    public void checkPIN(Player p, Gui g) {
        if (this.rl.getPlayerManager().getPlayerPIN().get(p).equalsIgnoreCase(RLPlayerConfig.file().getString(p.getName()))) {
            g.setCloseGuiAction(event -> p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 20));
            g.close(p);
            p.setInvulnerable(false);

            this.rl.getPlayerManager().getFrozenPlayers().remove(p);

            if (RLConfig.file().getBoolean("Settings.Hide-Inventories")) {
                p.getInventory().setContents(this.rl.getPlayerManager().getPlayerInventories().get(p));
                this.rl.getPlayerManager().getPlayerInventories().remove(p);
            }
            this.rl.getPlayerManager().getPlayerPIN().remove(p);
            p.sendTitle(Text.color(RLConfig.file().getString("Strings.Titles.Login.Up")), Text.color(RLConfig.file().getString("Strings.Titles.Login.Down")), 7, 50, 10);
            if (RLConfig.file().getBoolean("Settings.BungeeCord.Connect-Lobby")) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RealLogin.getPlugin(RealLogin.class), () -> {
                    p.sendTitle(Text.color(RLConfig.file().getString("Strings.Titles.Sending.Up")), Text.color(RLConfig.file().getString("Strings.Titles.Sending.Down")), 7, 50, 10);
                }, 5L);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RealLogin.getPlugin(RealLogin.class), () -> {
                    BungeecordUtils.connect(RLConfig.file().getString("Settings.BungeeCord.Lobby-Server"), p, this.rl);
                }, 10L);
            }
        }
    }

    public void confirmAction(ClickType e, Player p, String s, Gui g) {
        if (e == ClickType.RIGHT) {
            removeNumber(p, g);
        } else {
            if (s.length() > 1) {
                RLPlayerConfig.file().set(p.getName(), s);
                RLPlayerConfig.save();

                g.setCloseGuiAction(event -> p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 20));
                g.close(p);
                p.setInvulnerable(false);

                this.rl.getPlayerManager().getFrozenPlayers().remove(p);
                if (this.rl.getPlayerManager().getPlayerInventories().containsKey(p)) {
                    p.getInventory().setContents(this.rl.getPlayerManager().getPlayerInventories().get(p));
                    this.rl.getPlayerManager().getPlayerInventories().remove(p);
                }
                this.rl.getPlayerManager().getPlayerPIN().remove(p);

                p.sendTitle(Text.color(RLConfig.file().getString("Strings.Titles.Registered.Up")), Text.color(RLConfig.file().getString("Strings.Titles.Registered.Down")), 7, 50, 10);
                if (RLConfig.file().getBoolean("Settings.BungeeCord.Connect-Lobby")) {
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RealLogin.getPlugin(RealLogin.class), () -> {
                        p.sendTitle(Text.color(RLConfig.file().getString("Strings.Titles.Sending.Up")), Text.color(RLConfig.file().getString("Strings.Titles.Sending.Down")), 7, 50, 10);
                    }, 5L);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RealLogin.getPlugin(RealLogin.class), () -> {
                        BungeecordUtils.connect(RLConfig.file().getString("Settings.BungeeCord.Lobby-Server"), p, this.rl);
                    }, 10L);
                }
            }
        }
    }

}
