package com.fnordo.listeners;

import com.fnordo.utils.Database;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;


public class InventoryClose implements Listener {

    public static ItemStack[] prev;

    @EventHandler
    public void onEvent(InventoryCloseEvent e) {


        //Save items to database
        if (ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase(e.getPlayer().getName() + "'s backpack")) {
            try {
                    Database.addDbItems((Player) e.getPlayer(), e.getInventory());
                //}
                } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }
}