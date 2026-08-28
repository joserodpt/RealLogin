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
 * @author José Rodrigues © 2020-2026
 * @link https://github.com/joserodpt/RealLogin
 */

import joserodpt.reallogin.RealLogin;
import joserodpt.reallogin.config.RLConfig;
import joserodpt.reallogin.player.PlayerDataRow;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUIManager {

    private static final String DEFAULT_HEAD_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMGViZTdlNTIxNTE2OWE2OTlhY2M2Y2VmYTdiNzNmZGIxMDhkYjg3YmI2ZGFlMjg0OWZiZTI0NzE0YjI3In19fQ==";
    private static final Pattern TEXTURE_URL_PATTERN = Pattern.compile("\\\"url\\\":\\\"([^\\\"]+)\\\"");

    private final RealLogin rl;

    public GUIManager(RealLogin rl) {
        this.rl = rl;
    }

    public void openRegisterGUI(Player p) {
        GUIBuilder guiBuilder = new GUIBuilder(Text.color(RLConfig.file().getString("Strings.GUI.Register")), 4);

        guiBuilder.setItem(2, 8,
                Items.createItemLore(Material.EMERALD, RLConfig.file().getString("Strings.GUI.Items.Confirm-Pin.Name"), Collections.singletonList(RLConfig.file().getString("Strings.GUI.Items.Confirm-Pin.Description"))),
                event -> confirmAction(event.getClick(), p, this.rl.getPlayerManager().getPlayerPIN(p.getUniqueId()), guiBuilder));

        guiBuilder.setCloseAction(event -> {
            if (!this.rl.getPlayerManager().isPlayerAuthenticated(p.getUniqueId())) {
                openRegisterGUI(p);
            }
        });

        commonLoginRegister(p, guiBuilder);
    }

    public void openLoginGUI(Player p) {
        GUIBuilder guiBuilder = new GUIBuilder(Text.color(RLConfig.file().getString("Strings.GUI.Login")), 4);

        guiBuilder.setItem(2, 8, Items.createItemLore(Material.LAVA_BUCKET, RLConfig.file().getString("Strings.GUI.Items.Remove-Number.Name"), Collections.singletonList(RLConfig.file().getString("Strings.GUI.Items.Remove-Number.Description"))), event -> removeNumber(p, guiBuilder));
        guiBuilder.setCloseAction(event -> {
            if (!this.rl.getPlayerManager().isPlayerAuthenticated(p.getUniqueId())) {
                openLoginGUI(p);
            }
        });

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

        boolean useCustomHeads = false;

        try {
            createCustomHead(DEFAULT_HEAD_TEXTURE);
            useCustomHeads = RLConfig.file().getBoolean("Settings.Use-Custom-Heads");
        } catch (Exception ignored) {}

        gui.setItem(4, 5, useCustomHeads ? Items.renameItem(createCustomHead(DEFAULT_HEAD_TEXTURE), "&6&l0", Collections.singletonList("")) :
                Items.createItemLore(Material.BLACK_STAINED_GLASS_PANE, "&6&l0", Collections.emptyList()), event -> {
            if (event.getClick() == ClickType.DROP)
                removeNumber(p, gui);
            else
                addNumber(p, 0, gui);
        });

        // Helper method to create and set GuiItems
        boolean finalUseCustomHeads = useCustomHeads;
        BiConsumer<Integer, String> setGuiItem = (slot, base64) -> {
            ItemStack item = finalUseCustomHeads
                    ? Items.renameItem(createCustomHead(base64), "&6&l" + slot, Collections.emptyList())
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

    private ItemStack createCustomHead(String textureBase64) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        try {
            ItemMeta itemMeta = head.getItemMeta();
            if (!(itemMeta instanceof SkullMeta)) {
                return head;
            }

            SkullMeta skullMeta = (SkullMeta) itemMeta;
            Object profile = createPlayerProfile();
            URL textureUrl = extractTextureUrl(textureBase64);

            if (textureUrl == null) {
                return head;
            }

            Method getTextures = profile.getClass().getMethod("getTextures");
            Object textures = getTextures.invoke(profile);
            Method setSkin = textures.getClass().getMethod("setSkin", URL.class);
            setSkin.invoke(textures, textureUrl);

            Class<?> playerProfileClass = Class.forName("org.bukkit.profile.PlayerProfile");

            try {
                Method setPlayerProfile = SkullMeta.class.getMethod("setPlayerProfile", playerProfileClass);
                setPlayerProfile.setAccessible(true);
                setPlayerProfile.invoke(skullMeta, profile);
            } catch (NoSuchMethodException ignored) {
                try {
                    Method setOwnerProfile = SkullMeta.class.getMethod("setOwnerProfile", playerProfileClass);
                    setOwnerProfile.setAccessible(true);
                    setOwnerProfile.invoke(skullMeta, profile);
                } catch (NoSuchMethodException ignoredToo) {
                    Field profileField = skullMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(skullMeta, profile);
                }
            }

            head.setItemMeta(skullMeta);
        } catch (Exception e) {
            rl.getLogger().warning("Could not create custom head texture: " + e.getMessage());
        }

        return head;
    }

    private Object createPlayerProfile() throws ReflectiveOperationException {
        try {
            Method createPlayerProfile = Bukkit.class.getMethod("createPlayerProfile", UUID.class, String.class);
            return createPlayerProfile.invoke(null, UUID.randomUUID(), null);
        } catch (NoSuchMethodException ignored) {
            Method createPlayerProfile = Bukkit.class.getMethod("createPlayerProfile", UUID.class);
            return createPlayerProfile.invoke(null, UUID.randomUUID());
        }
    }

    private URL extractTextureUrl(String textureBase64) throws Exception {
        String decodedTexture = new String(Base64.getDecoder().decode(textureBase64), StandardCharsets.UTF_8);
        Matcher matcher = TEXTURE_URL_PATTERN.matcher(decodedTexture);

        if (!matcher.find()) {
            return null;
        }

        return new URL(matcher.group(1));
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

                p.sendTitle(Text.color(RLConfig.file().getString("Strings.Titles.Login.Up")), Text.color(RLConfig.file().getString("Strings.Titles.Login.Down")), 7, 50, 10);

                this.rl.getPlayerManager().loginGrantedForPlayer(p.getUniqueId());
                this.rl.getDatabaseManager().savePlayerData(pdo, true);
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

                    p.sendTitle(Text.color(RLConfig.file().getString("Strings.Titles.Registered.Up")), Text.color(RLConfig.file().getString("Strings.Titles.Registered.Down")), 7, 50, 10);

                    this.rl.getPlayerManager().loginGrantedForPlayer(p.getUniqueId());
                } catch (Exception ex) {
                    Bukkit.getLogger().severe("Error while hashing password: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }
}
