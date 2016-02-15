package com.fnordo.listeners;

import com.fnordo.utils.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;


public class InventoryOpen implements Listener{

    @EventHandler
    //retrieve items from database
    private void onEvent(InventoryOpenEvent e){

        Player player = (Player) e.getPlayer();
        Inventory inv = e.getInventory();

        Database.getDbItems(player, inv);
    }
}
