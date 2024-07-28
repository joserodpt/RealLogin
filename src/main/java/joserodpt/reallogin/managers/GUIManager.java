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

import dev.dbassett.skullcreator.SkullCreator;
import joserodpt.reallogin.RealLogin;
import joserodpt.reallogin.config.RLConfig;
import joserodpt.reallogin.player.PlayerDataRow;
import joserodpt.reallogin.utils.BungeecordUtils;
import joserodpt.reallogin.utils.GUIBuilder;
import joserodpt.reallogin.utils.Items;
import joserodpt.reallogin.utils.PBKDF2;
import joserodpt.reallogin.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;

public class GUIManager {

    private final RealLogin rl;

    public GUIManager(RealLogin rl) {
        this.rl = rl;
    }

    public void openRegisterGUI(Player p) {
        GUIBuilder guiBuilder = new GUIBuilder(Text.color(RLConfig.file().getString("Strings.GUI.Register")), 4);

        guiBuilder.setItem(2, 8,
                Items.createItemLore(Material.EMERALD, RLConfig.file().getString("Strings.GUI.Items.Confirm-Pin.Name"), Collections.singletonList(RLConfig.file().getString("Strings.GUI.Items.Confirm-Pin.Description"))),
                event -> confirmAction(event.getClick(), p, this.rl.getPlayerManager().getPlayerPIN(p.getUniqueId()), guiBuilder));

        guiBuilder.setCloseAction(event -> openRegisterGUI(p));

        commonLoginRegister(p, guiBuilder);
    }

    public void openLoginGUI(Player p) {
        GUIBuilder guiBuilder = new GUIBuilder(Text.color(RLConfig.file().getString("Strings.GUI.Login")), 4);

        guiBuilder.setItem(2, 8, Items.createItemLore(Material.LAVA_BUCKET, RLConfig.file().getString("Strings.GUI.Items.Remove-Number.Name"), Collections.singletonList(RLConfig.file().getString("Strings.GUI.Items.Remove-Number.Description"))), event -> removeNumber(p, guiBuilder));
        guiBuilder.setCloseAction(event -> openLoginGUI(p));

        commonLoginRegister(p, guiBuilder);
    }

    private void commonLoginRegister(Player p, GUIBuilder guiBuilder) {
        p.setInvulnerable(true);
        //this.rl.getPlayerManager().getPlayerPIN().put(p.getUniqueId(), "");

        setPinItems(guiBuilder, p);

        guiBuilder.setDefaultClickAction(event -> event.setCancelled(true));

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(rl, () -> {
            guiBuilder.open(p);
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 20);
        }, 5L);
    }

    public void setPinItems(GUIBuilder gui, Player p) {
        //leave server button
        gui.setItem(2, 2,
                Items.createItemLore(Material.OAK_DOOR, RLConfig.file().getString("Strings.GUI.Items.Leave-Server.Name"), Collections.singletonList(RLConfig.file().getString("Strings.GUI.Items.Leave-Server.Description"))),
                event -> p.kickPlayer(Text.color(Text.getPrefix() + RLConfig.file().getString("Strings.Kick-Message"))));

        boolean useCustomHeads = RLConfig.file().getBoolean("Settings.Use-Custom-Heads");

        gui.setItem(4, 5, useCustomHeads ? Items.renameItem(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMGViZTdlNTIxNTE2OWE2OTlhY2M2Y2VmYTdiNzNmZGIxMDhkYjg3YmI2ZGFlMjg0OWZiZTI0NzE0YjI3In19fQ=="), "&6&l0", Collections.singletonList("")) :
                Items.createItemLore(Material.BLACK_STAINED_GLASS_PANE, "&6&l0", Collections.emptyList()), event -> {
            if (event.getClick() == ClickType.DROP)
                removeNumber(p, gui);
            else
                addNumber(p, 0, gui);
        });

        // Helper method to create and set GuiItems
        BiConsumer<Integer, String> setGuiItem = (slot, base64) -> {
            ItemStack item = useCustomHeads
                    ? Items.renameItem(SkullCreator.itemFromBase64(base64), "&6&l" + slot, Collections.emptyList())
                    : Items.createItem(Material.BLACK_STAINED_GLASS_PANE, "&6&l" + slot);

            gui.setItem((slot - 1) / 3 + 1, (slot - 1) % 3 + 4, item, event -> {
                if (event.getClick() == ClickType.DROP)
                    removeNumber(p, gui);
                else
                    addNumber(p, slot, gui);
            });
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

    public void removeNumber(Player p, GUIBuilder g) {
        updatePIN(p, g, -1, Sound.BLOCK_ANVIL_BREAK, 20);
    }

    public void addNumber(Player p, int i, GUIBuilder g) {
        if (this.rl.getPlayerManager().getPlayerPIN(p.getUniqueId()).length() < RLConfig.file().getInt("Settings.Max-Pin-Length")) {
            updatePIN(p, g, i, Sound.BLOCK_NOTE_BLOCK_CHIME, 50);
        }
    }

    private void updatePIN(Player p, GUIBuilder g, int i, Sound sound, float pitch) {
        PlayerManager playerManager = this.rl.getPlayerManager();
        String currentPIN = playerManager.getPlayerPIN(p.getUniqueId());

        if (i == -1 && !currentPIN.isEmpty()) {
            currentPIN = currentPIN.substring(0, currentPIN.length() - 1);
        } else if (i != -1) {
            currentPIN += i;
        }

        playerManager.setPlayerPin(p.getUniqueId(), currentPIN);
        p.playSound(p.getLocation(), sound, (float) 1, pitch);
        g.updateTitle(Text.color(RLConfig.file().getString("Strings.GUI.PIN") + currentPIN));
        checkPIN(p, g);
    }

    public void checkPIN(Player p, GUIBuilder g) {
        PlayerDataRow pdo = this.rl.getDatabaseManager().getPlayerData(p);
        if (pdo == null) {
            return;
        }
        String hashedPassword = pdo.getHashedPassword();
        if (hashedPassword == null) {
            return;
        }
        try {
            if (PBKDF2.equalHash(this.rl.getPlayerManager().getPlayerPIN(p.getUniqueId()), hashedPassword)) {
                g.setCloseAction(event -> p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 20));
                p.closeInventory();
                p.setInvulnerable(false);

                this.rl.getPlayerManager().loginGrantedForPlayer(p.getUniqueId());
                if (RLConfig.file().getBoolean("Settings.Hide-Inventories")) {
                    p.getInventory().setContents(this.rl.getPlayerManager().getPlayerInventory(p.getUniqueId()));
                }

                p.sendTitle(Text.color(RLConfig.file().getString("Strings.Titles.Login.Up")), Text.color(RLConfig.file().getString("Strings.Titles.Login.Down")), 7, 50, 10);

                this.rl.getDatabaseManager().savePlayerData(pdo, true);

                if (RLConfig.file().getBoolean("Settings.BungeeCord.Connect-Lobby")) {
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(rl, () -> p.sendTitle(Text.color(RLConfig.file().getString("Strings.Titles.Sending.Up")), Text.color(RLConfig.file().getString("Strings.Titles.Sending.Down")), 7, 50, 10), 5L);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(rl, () -> BungeecordUtils.connect(RLConfig.file().getString("Settings.BungeeCord.Lobby-Server"), p, this.rl), 10L);
                }
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            rl.getLogger().severe("Error while comparing hashed passwords: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void confirmAction(ClickType e, Player p, String s, GUIBuilder g) {
        if (e == ClickType.RIGHT) {
            removeNumber(p, g);
        } else {
            if (s.length() > 1) {
                try {
                    String hashedPassword = PBKDF2.hash(s);
                    rl.getDatabaseManager().savePlayerData(new PlayerDataRow(p, hashedPassword), true);

                    g.setCloseAction(event -> p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 20));
                    p.closeInventory();
                    p.setInvulnerable(false);

                    this.rl.getPlayerManager().loginGrantedForPlayer(p.getUniqueId());
                    if (this.rl.getPlayerManager().hasPlayerInventory(p.getUniqueId())) {
                        p.getInventory().setContents(this.rl.getPlayerManager().getPlayerInventory(p.getUniqueId()));
                    }

                    p.sendTitle(Text.color(RLConfig.file().getString("Strings.Titles.Registered.Up")), Text.color(RLConfig.file().getString("Strings.Titles.Registered.Down")), 7, 50, 10);
                    if (RLConfig.file().getBoolean("Settings.BungeeCord.Connect-Lobby")) {
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(rl, () -> {
                            p.sendTitle(Text.color(RLConfig.file().getString("Strings.Titles.Sending.Up")), Text.color(RLConfig.file().getString("Strings.Titles.Sending.Down")), 7, 50, 10);
                        }, 5L);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(rl, () -> {
                            BungeecordUtils.connect(RLConfig.file().getString("Settings.BungeeCord.Lobby-Server"), p, this.rl);
                        }, 10L);
                    }

                } catch (Exception ex) {
                    Bukkit.getLogger().severe("Error while hashing password: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }
}
