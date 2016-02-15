package com.fnordo.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


public class BackPack implements CommandExecutor {

    private Inventory inv;  //This configures basic info for backpack

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender.isOp()) {   //This is for permissions plugins.

            inv = Bukkit.createInventory(null, 54, ChatColor.BLUE + ((Player) sender).getPlayer().getName()
                    + "'s Backpack");
            ((Player) sender).openInventory(inv);  //Gets the player, then opens the backpack!
        } else if (sender.hasPermission("dbp.use")) {
            if (sender.hasPermission("dbp.slots.18")) {
                inv = Bukkit.createInventory(null, 18, ChatColor.BLUE + ((Player) sender).getPlayer().getName()
                        + "'s Backpack");
                ((Player) sender).openInventory(inv);
            } else if (sender.hasPermission("dbp.slots.27")) {
                inv = Bukkit.createInventory(null, 27, ChatColor.BLUE + ((Player) sender).getPlayer().getName()
                        + "'s Backpack");
                ((Player) sender).openInventory(inv);

            } else if (sender.hasPermission("dbp.slots.36")) {
                inv = Bukkit.createInventory(null, 36, ChatColor.BLUE + ((Player) sender).getPlayer().getName()
                        + "'s Backpack");
                ((Player) sender).openInventory(inv);

            } else if (sender.hasPermission("dbp.slots.45")) {
                inv = Bukkit.createInventory(null, 45, ChatColor.BLUE + ((Player) sender).getPlayer().getName()
                        + "'s Backpack");
                ((Player) sender).openInventory(inv);

            } else if (sender.hasPermission("dbp.slots.54")) {
                inv = Bukkit.createInventory(null, 54, ChatColor.BLUE + ((Player) sender).getPlayer().getName()
                        + "'s Backpack");
                ((Player) sender).openInventory(inv);

            } else {
                sender.sendMessage("No backpack size set.  Please contact your administrator");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use the Backpack.");
        }

        return false;
    }
}
