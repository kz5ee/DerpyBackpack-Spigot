package com.fnordo.utils;

import com.fnordo.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

/* This code was originally posted/provided by Subtelny on the Spigot forums:
 * https://bukkit.org/threads/serialize-inventory-to-single-string-and-vice-versa.92094/page-4
 *
 * Changes have been made to use the 1.8 naming convention:  ID -> Name, i.e. ench. 16 -> SHARPNESS
 *
 * Renamed from ParkourInv to InvSerializer
 */

public class InvSerializer {

    public static String InventoryToString(Inventory invInventory) {
        String serialization = invInventory.getSize() + ";";
        for (int i = 0; i < invInventory.getSize(); i++) {
            ItemStack is = invInventory.getItem(i);

            if (is != null) {
                ItemMeta meta = is.getItemMeta();
                String serializedItemStack = new String();

                String isType = is.getType().toString();  //Change deprecated to 1.8 naming <KZ5EE>
                serializedItemStack += "t@" + isType;

                if (is.getDurability() != 0) {
                    String isDurability = String.valueOf(is.getDurability());
                    serializedItemStack += ":d@" + isDurability;
                }

                if (is.getAmount() != 1) {
                    String isAmount = String.valueOf(is.getAmount());
                    serializedItemStack += ":a@" + isAmount;
                }

                if (meta.hasDisplayName()) {
                    String isMeta = meta.getDisplayName();
                    serializedItemStack += ":m@" + isMeta;
                }

                if (meta.hasLore()) {
                    String isLore = meta.getLore().toString();
                    if (!(isLore == null)) {
                        serializedItemStack += ":l@" + isLore;
                    }
                }

                Map<Enchantment, Integer> isEnch = is.getEnchantments();
                if (isEnch.size() > 0) {
                    for (Entry<Enchantment, Integer> ench : isEnch.entrySet()) {
                        serializedItemStack += ":e@" + ench.getKey().getName()  //Change deprecated to 1.8 naming <KZ5EE>
                                + "@" + ench.getValue();
                    }
                }

                serialization += i + "#" + serializedItemStack + ";";
            }

        }
        return serialization;
    }

    public static void StringToInventory(String invString, Player player) {
        String[] serializedBlocks = invString.split(";");
        String invInfo = serializedBlocks[0];
        Inventory deserializedInventory = Bukkit.getServer().createInventory(null, Integer.valueOf(invInfo));

        for (int i = 1; i < serializedBlocks.length; i++) {
            String[] serializedBlock = serializedBlocks[i].split("#");
            int stackPosition = Integer.valueOf(serializedBlock[0]);

            if (stackPosition >= deserializedInventory.getSize()) {
                continue;
            }
            Main.instance.getLogger().info("Positions:  " + stackPosition);


            ItemStack is = null;
            Boolean createdItemStack = false;

            String[] serializedItemStack = serializedBlock[1].split(":");
            for (String itemInfo : serializedItemStack) {
                String[] itemAttribute = itemInfo.split("@");
                if (itemAttribute[0].equals("t")) {
                    is = new ItemStack(Material.getMaterial(itemAttribute[1]));
                    createdItemStack = true;
                } else if (itemAttribute[0].equals("d") && createdItemStack) {
                    is.setDurability(Short.valueOf(itemAttribute[1]));
                } else if (itemAttribute[0].equals("a") && createdItemStack) {
                    is.setAmount(Integer.valueOf(itemAttribute[1]));
                } else if (itemAttribute[0].equals("m") && createdItemStack) {
                    ItemMeta isM = is.getItemMeta();
                    isM.setDisplayName(itemAttribute[1]);
                    is.setItemMeta(isM);
                } else if (itemAttribute[0].equals("l") && createdItemStack) {
                    ItemMeta isM = is.getItemMeta();
                    String removeBuckle = itemAttribute[1].substring(1,
                            itemAttribute[1].length() - 1);
                    ArrayList<String> l = new ArrayList<String>();
                    for (String podpis : removeBuckle.split(", ")) {
                        l.add(podpis);
                    }
                    isM.setLore(l);
                    is.setItemMeta(isM);
                } else if (itemAttribute[0].equals("e") && createdItemStack) {

                    //<KZ5EE Add>
                    Enchantment enchLvlTest = Enchantment.getByName(itemAttribute[1]);
                    if (Integer.valueOf(itemAttribute[2]) > enchLvlTest.getMaxLevel()) {
                        is.addUnsafeEnchantment(Enchantment.getByName(itemAttribute[1]), Integer
                                .valueOf(itemAttribute[2]));
                    } else {
                        //This is original referenced code.  It has been modified for 1.8 naming
                        is.addEnchantment(Enchantment.getByName(itemAttribute[1]), Integer
                                .valueOf(itemAttribute[2]));
                    }
                    //</KZ5EE Add>
                }
                deserializedInventory.setItem(stackPosition, is);
            }
        }

        player.openInventory(deserializedInventory);
    }

}
