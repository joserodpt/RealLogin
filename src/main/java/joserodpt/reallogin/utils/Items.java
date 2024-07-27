package joserodpt.reallogin.utils;

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

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Items {

    public static ItemStack createItem(final Material material, final String nome) {
        return createItem(material, 1, nome);
    }

    public static ItemStack createItem(final Material material, final int quantidade, final String nome) {
        final ItemStack item = new ItemStack(material, quantidade);
        final ItemMeta meta = item.getItemMeta();
        if (nome != null) {
            meta.setDisplayName(Text.color(nome));
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItemLore(final Material material, final String nome, final List<String> desc) {
        return createItemLore(material, 1, nome, desc);
    }

    public static ItemStack createItemLore(final Material material, final int quantidade, final String nome, final List<String> desc) {
        final ItemStack item = new ItemStack(material, quantidade);
        if (item.getItemMeta() != null) {
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Text.color(nome));
            meta.setLore(Text.color(desc));
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createItemLoreEnchanted(final Material m, final int i, final String name, final List<String> desc) {
        final ItemStack item = new ItemStack(m, i);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Text.color(name));
        meta.setLore(Text.color(desc));
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack changeItemStack(final String name, final List<String> list, final ItemStack item) {
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Text.color(name));
        meta.setLore(Text.color(list));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack renameItem(ItemStack itemStack, String s, List<String> strings) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Text.color(s));
        itemMeta.setLore(Text.color(strings));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}