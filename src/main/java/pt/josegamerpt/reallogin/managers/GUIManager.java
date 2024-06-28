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
import java.util.Map;
import java.util.function.BiConsumer;

public class GUIManager {

    private RealLogin rl;
    public GUIManager(RealLogin rl) {
        this.rl = rl;
    }

    public void openRegisterGUI(Player p, Sound s) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(rl, () -> {
            Gui gui = Gui.gui().rows(3).title(Component.text(Text.color(RLConfig.file().getString("Strings.GUI.Register")))).create();

            p.setInvulnerable(true);
            this.rl.getPlayerManager().getPlayerPIN().put(p, "");

            setPinItems(gui, p);

            gui.setItem(2, 8,
                    new GuiItem(new ItemBuilder(Material.EMERALD).setDisplayName(RLConfig.file().getString("Strings.GUI.Items.Confirm-Pin.Name")).setLore(Collections.singletonList(RLConfig.file().getString("Strings.GUI.Items.Confirm-Pin.Description"))).get(), event -> confirmAction(event.getClick(), p, this.rl.getPlayerManager().getPlayerPIN().get(p), gui)));

            gui.setDefaultClickAction(event -> event.setCancelled(true));
            gui.setCloseGuiAction(event -> openRegisterGUI(p, Sound.ENTITY_VILLAGER_NO));

            gui.open(p);
            p.playSound(p.getLocation(), s, 1, 20);
        }, 5L);
    }
    
    public void openLoginGUI(Player p, Sound sound) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(rl, () -> {
            Gui gui = Gui.gui().rows(3).title(Component.text(Text.color(RLConfig.file().getString("Strings.GUI.Login")))).create();
            p.setInvulnerable(true);

            setPinItems(gui, p);

            gui.setItem(2, 8, new GuiItem(new ItemBuilder(Material.LAVA_BUCKET).setDisplayName(RLConfig.file().getString("Strings.GUI.Items.Remove-Number.Name")).setLore(Collections.singletonList(RLConfig.file().getString("Strings.GUI.Items.Remove-Number.Description"))).get(), event -> removeNumber(p, gui)));

            gui.setDefaultClickAction(event -> event.setCancelled(true));
            gui.setCloseGuiAction(event -> openLoginGUI(p, Sound.ENTITY_VILLAGER_NO));

            gui.open(p);
            p.playSound(p.getLocation(), sound, 1, 20);
        }, 5L);
    }

    public void setPinItems(Gui gui, Player p) {
        //leave server button
        gui.setItem(2, 2, new GuiItem(new ItemBuilder(Material.OAK_DOOR).setDisplayName(RLConfig.file().getString("Strings.GUI.Items.Leave-Server.Name")).setLore(Collections.singletonList(RLConfig.file().getString("Strings.GUI.Items.Leave-Server.Description"))).get(), event -> p.kickPlayer(Text.color(Text.getPrefix() + RLConfig.file().getString("Strings.Kick-Message")))));

        boolean useCustomHeads = RLConfig.file().getBoolean("Settings.Use-Custom-Heads");

        // Helper method to create and set GuiItems
        BiConsumer<Integer, String> setGuiItem = (slot, base64) -> {
            GuiItem item = new GuiItem(
                    useCustomHeads
                            ? new ItemBuilder(SkullCreator.itemFromBase64(base64)).setDisplayName("&6&l" + slot).get()
                            : new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&6&l" + slot).setAmount(slot).get(),
                    event -> addNumber(p, slot, gui)
            );
            gui.setItem((slot - 1) / 3 + 1, (slot - 1) % 3 + 4, item);
        };

        // Mapping of slot numbers to custom head base64 strings
        Map<Integer, String> customHeads = Map.of(
                1, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFiYzJiY2ZiMmJkMzc1OWU2YjFlODZmYzdhNzk1ODVlMTEyN2RkMzU3ZmMyMDI4OTNmOWRlMjQxYmM5ZTUzMCJ9fX0=",
                2, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNkOWVlZWU4ODM0Njg4ODFkODM4NDhhNDZiZjMwMTI0ODVjMjNmNzU3NTNiOGZiZTg0ODczNDE0MTk4NDcifX19",
                3, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWQ0ZWFlMTM5MzM4NjBhNmRmNWU4ZTk1NTY5M2I5NWE4YzNiMTVjMzZiOGI1ODc1MzJhYzA5OTZiYzM3ZTUifX19",
                4, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDJlNzhmYjIyNDI0MjMyZGMyN2I4MWZiY2I0N2ZkMjRjMWFjZjc2MDk4NzUzZjJkOWMyODU5ODI4N2RiNSJ9fX0=",
                5, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ1N2UzYmM4OGE2NTczMGUzMWExNGUzZjQxZTAzOGE1ZWNmMDg5MWE2YzI0MzY0M2I4ZTU0NzZhZTIifX19",
                6, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzM0YjM2ZGU3ZDY3OWI4YmJjNzI1NDk5YWRhZWYyNGRjNTE4ZjVhZTIzZTcxNjk4MWUxZGNjNmIyNzIwYWIifX19",
                7, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmRiNmViMjVkMWZhYWJlMzBjZjQ0NGRjNjMzYjU4MzI0NzVlMzgwOTZiN2UyNDAyYTNlYzQ3NmRkN2I5In19fQ==",
                8, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTkxOTQ5NzNhM2YxN2JkYTk5NzhlZDYyNzMzODM5OTcyMjI3NzRiNDU0Mzg2YzgzMTljMDRmMWY0Zjc0YzJiNSJ9fX0=",
                9, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTY3Y2FmNzU5MWIzOGUxMjVhODAxN2Q1OGNmYzY0MzNiZmFmODRjZDQ5OWQ3OTRmNDFkMTBiZmYyZTViODQwIn19fQ=="
        );

        // Set the GUI items
        for (int i = 1; i <= 9; ++i) {
            setGuiItem.accept(i, customHeads.get(i));
        }
    }

    public void removeNumber(Player p, Gui g) {
        updatePIN(p, g, -1, Sound.BLOCK_ANVIL_BREAK, 1, 20);
    }

    public void addNumber(Player p, int i, Gui g) {
        if (this.rl.getPlayerManager().getPlayerPIN().get(p).length() < RLConfig.file().getInt("Settings.Max-Pin-Length")) {
            updatePIN(p, g, i, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 50);
        }
    }

    private void updatePIN(Player p, Gui g, int i, Sound sound, float volume, float pitch) {
        PlayerManager playerManager = this.rl.getPlayerManager();
        String currentPIN = playerManager.getPlayerPIN().get(p);

        if (i == -1 && !currentPIN.isEmpty()) {
            currentPIN = currentPIN.substring(0, currentPIN.length() - 1);
        } else if (i != -1) {
            currentPIN += i;
        }

        playerManager.getPlayerPIN().put(p, currentPIN);
        p.playSound(p.getLocation(), sound, volume, pitch);
        g.updateTitle(Text.color(RLConfig.file().getString("Strings.GUI.PIN") + currentPIN));
        checkPIN(p, g);
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
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(rl, () -> {
                    p.sendTitle(Text.color(RLConfig.file().getString("Strings.Titles.Sending.Up")), Text.color(RLConfig.file().getString("Strings.Titles.Sending.Down")), 7, 50, 10);
                }, 5L);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(rl, () -> {
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
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(rl, () -> {
                        p.sendTitle(Text.color(RLConfig.file().getString("Strings.Titles.Sending.Up")), Text.color(RLConfig.file().getString("Strings.Titles.Sending.Down")), 7, 50, 10);
                    }, 5L);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(rl, () -> {
                        BungeecordUtils.connect(RLConfig.file().getString("Settings.BungeeCord.Lobby-Server"), p, this.rl);
                    }, 10L);
                }
            }
        }
    }

}
