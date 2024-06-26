package pt.josegamerpt.reallogin.utils;

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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {

    private final ItemStack stack;

    public ItemBuilder(Material mat) {
        stack = new ItemStack(mat, 1);
    }

    public ItemBuilder(ItemStack i) {
        stack = i;
    }

    public ItemMeta getItemMeta() {
        return stack.getItemMeta();
    }

    public ItemBuilder setItemMeta(ItemMeta meta) {
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemBuilder setDisplayName(String displayname) {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(Text.color(displayname));
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(Text.color(lore));
        setItemMeta(meta);
        return this;
    }

    public ItemStack get() {
        return stack;
    }

}