package com.fnordo.utils;

import com.fnordo.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Database {

    private static final String INSERT = "INSERT INTO derpybackpack (uuid,invstring) VALUES(?,?)";

    private static final String UPDATE = "UPDATE derpybackpack SET invstring=? WHERE uuid=?";

    private static final String ROWCOUNT = "SELECT COUNT(*) AS rowcount FROM derpybackpack WHERE uuid=?";

    public static void addDbItems(Player player, Inventory inventory) {

        Connection connection = null;
        PreparedStatement preparedStatement;
        try {
            connection = Main.instance.getHikari().getConnection();

            //Check to see if the player already has a backpack in the database.
            int bpExists = 0;
            bpExists = bpCheck(player);

            if(bpExists == 0) {
                preparedStatement = connection.prepareStatement(INSERT);
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.setString(2, InvSerializer.InventoryToString(inventory));
            }else {
                preparedStatement = connection.prepareStatement(UPDATE);
                preparedStatement.setString(1, InvSerializer.InventoryToString(inventory));
                preparedStatement.setString(2, player.getUniqueId().toString());
            }
            //Send the backpack inventory to the database.
            preparedStatement.execute();

            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.toString();
        }
    }

    private static final String SELECT = "SELECT invstring FROM derpybackpack WHERE uuid=?";

    public static void getDbItems(Player player, Inventory inventory) {

        Connection connection = null;
        ResultSet resultSet;

        try {
            connection = Main.instance.getHikari().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setString(1, player.getUniqueId().toString());

            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                String inv = resultSet.getString("invstring");

                player.sendMessage(inv);

                Inventory i = InvSerializer.StringToInventory(inv);

              /*for(ItemStack is : i) {
                  if (is == null) {
                     continue;
                  }
                    inventory.addItem(is);
              }*/

                ItemStack[] items = i.getContents();

                for (int j = 0; j < items.length; j++) {
                    inventory.setItem(j, items[j]);
                }

                player.updateInventory();
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     public static int bpCheck(Player player) {
        Connection connection = null;
        ResultSet resultSet;
        int itemRows = 0;

        try {
            connection = Main.instance.getHikari().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(ROWCOUNT);

            preparedStatement.setString(1, player.getUniqueId().toString());

            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();

            if(!resultSet.next()) {
                resultSet.close();
                preparedStatement.close();
                connection.close();
                return itemRows;
            } else {
                itemRows = resultSet.getInt("rowcount");
                resultSet.close();
                preparedStatement.close();
                connection.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemRows;
    }

    public static void populateDbSchema() {

    }
}
