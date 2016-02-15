package com.fnordo.main;

import com.fnordo.commands.BackPack;
import com.fnordo.listeners.InventoryClose;
import com.fnordo.listeners.InventoryOpen;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    public static Main instance;
    private static HikariDataSource hikari;

    public void onEnable() {
        instance = this;

        createConfig();
        setupHikari();

        registerListeners();
        registerCommands();
    }

    public void onDisable() {
        instance = null;

        if (hikari == null) {
            return;
        } else {
            hikari.close();
        }
    }

    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {  //Does the Plugin dir exist?
                getDataFolder().mkdirs();  //Create Plugin dir if it doesn't exist.
            }
            /*
                Now that we know the status of the Plugin dir, we need to check to make sure that
                    we have a config file to use.  If we have one, use it and load the config.  If we don't,
                    create one and populate default values.
             */
            File file = new File(getDataFolder(), "config.yml");
            //Does the file exist?
            if (!file.exists()) {               //No
                saveDefaultConfig();       //Save default config.yml.
                //Notify through console that a new config file was created.
                getLogger().log(Level.SEVERE, ChatColor.RED + "No Config found! Creating config with default settings!");
            } else {    //Yes
                getLogger().log(Level.INFO, ChatColor.GREEN + "Config found!  Loading settings.");//Load config and notify.
            }
        } catch (Exception e) { //There's been an error, send notification.
            getLogger().info("" + e);
        }
    }


    private void registerCommands() {
        getCommand("bp").setExecutor(new BackPack());
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new InventoryOpen(), this);
        pm.registerEvents(new InventoryClose(), this);
    }

    private void setupHikari() {
        FileConfiguration config = getConfig();

        String address = config.getString("mysql.host");
        String name = config.getString("mysql.database");
        String username = config.getString("mysql.username");
        String password = getConfig().getString("mysql.password");
        String port = getConfig().getString("mysql.port");

        hikari = new HikariDataSource();

        hikari.setMaximumPoolSize(10);
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", address);
        hikari.addDataSourceProperty("port", port);  //Integer.parseInt(port));
        hikari.addDataSourceProperty("databaseName", name);
        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);
    }



    public HikariDataSource getHikari() {
        return hikari;
    }
}
