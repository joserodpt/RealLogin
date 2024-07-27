package joserodpt.reallogin.utils;

/*
 *    _____ _    _ _____ ____        _ _     _
 *   / ____| |  | |_   _|  _ \      (_) |   | |
 *  | |  __| |  | | | | | |_) |_   _ _| | __| | ___ _ __
 *  | | |_ | |  | | | | |  _ <| | | | | |/ _` |/ _ \ '__|
 *  | |__| | |__| |_| |_| |_) | |_| | | | (_| |  __/ |
 *   \_____|\____/|_____|____/ \__,_|_|_|\__,_|\___|_|
 *
 * History:
 * Modified and optimized version of AdvInventory Original author:
 * http://spigotmc.org/members/25376/ Homer04 Original utility version
 * http://www.spigotmc.org/threads/133942/ Modified by AnyOD Compatible
 * https://www.spigotmc.org/threads/gui-creator-v2-making-inventories-was-never-easier.296898/ Modified by Whoktor
 * This version is last modified by JoseGamer_PT on 27/07/2024
 * Licensed under the MIT License
 * @author José Rodrigues © 2024
 */

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GUIBuilder implements InventoryHolder {
    private Inventory inv;
    private String title;

    private final Map<Integer, ItemStack> items = new LinkedHashMap<>();
    private final Map<Integer, ClickRunnable> runnables = new HashMap<>();
    private CloseRunnable closeRunnable;

    private boolean refreshing = false;

    public GUIBuilder(final String title, final int rows) {
        this(title, rows, null);
    }

    public GUIBuilder(final String title, final int rows, final ItemStack placeholder) {
        this.title = title;
        if (rows <= 0 || rows > 6) {
            throw new IllegalArgumentException("Rows must be between 1 and 6.");
        }
        this.inv = Bukkit.createInventory(this, rows * 9, Text.color(this.title));
        if (placeholder != null) {
            for (int i = 0; i < this.inv.getSize(); ++i) {
                this.inv.setItem(i, placeholder);
            }
        }
    }

    public Inventory getInventory() {
        return this.inv;
    }

    public int getSize() {
        return this.inv.getSize();
    }

    public void removeItem(final int slot) {
        this.items.remove(slot);
        this.inv.setItem(slot, new ItemStack(Material.AIR));
    }

    public void setItem(final int row, final int column, final ItemStack i) {
        this.items.put((row-1) * 9 + (column-1), i);
    }

    public void setItem(final Integer slot, final ItemStack itemstack) {
        this.items.put(slot, itemstack);
    }

    public void setItem(int row, int column, ItemStack i, final ClickRunnable clickRunnable) {
        this.setItem((row-1) * 9 + (column-1), i, clickRunnable);
    }

    public void setItem(final int slot, final ItemStack i, final ClickRunnable clickRunnable) {
        final ItemMeta im = i.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        this.runnables.put(slot, clickRunnable);
        this.items.put(slot, i);
    }

    public void setDefaultClickAction(final ClickRunnable clickRunnable) {
        for (int i = 0; i < this.inv.getSize(); i++) {
            if (this.runnables.containsKey(i)) {
                continue;
            }
            this.runnables.put(i, clickRunnable);
        }
    }

    public void setCloseAction(final CloseRunnable closeRunnable) {
        this.closeRunnable = closeRunnable;
    }

    public void updateTitle(String title) {
        this.refreshing = true;
        List<HumanEntity> viewers = new ArrayList<>(this.inv.getViewers());
        this.inv = Bukkit.createInventory(this, this.inv.getSize(), title);
        viewers.forEach(this::open);
        this.title = title;
        this.refreshing = false;
    }

    public void open(@NotNull HumanEntity player) {
        if (!player.isSleeping()) {
            this.inv.clear();
            this.fillGUI();
            player.openInventory(this.inv);
        }
    }

    private void fillGUI() {
        for (Map.Entry<Integer, ItemStack> entry : this.items.entrySet()) {
            this.inv.setItem(entry.getKey(), entry.getValue());
        }
    }

    public void executeClick(int slot, InventoryClickEvent e) {
        if (this.runnables.containsKey(slot)) {
            this.runnables.get(slot).run(e);
        }
    }

    public void executeCloseRunnable(InventoryCloseEvent e) {
        if (this.closeRunnable != null) {
            this.closeRunnable.run(e);
        }
    }

    public boolean isRefreshing() {
        return this.refreshing;
    }

    @FunctionalInterface
    public interface ClickRunnable {
        void run(InventoryClickEvent event);
    }

    @FunctionalInterface
    public interface CloseRunnable {
        void run(InventoryCloseEvent event);
    }

    public static Listener getListener() {
        return new Listener() {
            @EventHandler
            public void onClick(final InventoryClickEvent e) {
                final InventoryView view = e.getView();
                if (view.getTopInventory().getHolder() instanceof GUIBuilder) {
                    e.setCancelled(true);
                    final GUIBuilder gui = (GUIBuilder) view.getTopInventory().getHolder();
                    final int slot = e.getRawSlot();
                    if (slot >= 0 && slot < gui.getSize() && !gui.isRefreshing()) {
                        gui.executeClick(slot, e);
                    }
                }
            }

            @EventHandler
            public void onClose(final InventoryCloseEvent e) {
                final InventoryView view = e.getView();
                if (view.getTopInventory().getHolder() instanceof GUIBuilder) {
                    final GUIBuilder gui = (GUIBuilder) view.getTopInventory().getHolder();
                    if (!gui.isRefreshing())
                        gui.executeCloseRunnable(e);
                }
            }
        };
    }
}