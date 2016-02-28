package com.github.mmonkey.Automator.Dams;

import com.github.mmonkey.Automator.Database.DatabaseAbstract;
import com.github.mmonkey.Automator.Models.CommandSetting;
import org.spongepowered.api.entity.living.player.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandSettingDam {

    public static final String tblName = "command_settings";

    private DatabaseAbstract database;

    public CommandSettingDam(DatabaseAbstract database) {
        this.database = database;
    }

    /**
     * Get a Player's CommandSettings for this World
     *
     * @param player Player
     * @return List<CommandSetting>
     */
    public List<CommandSetting> getCommandSettings(Player player) {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        List<CommandSetting> results = new ArrayList<>();

        String sql = "SELECT * FROM " + tblName +
                " WHERE player_unique_id = ? AND world_unique_id = ?";

        try {

            connection = database.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setObject(1, player.getUniqueId());
            statement.setObject(2, player.getWorld().getUniqueId());
            result = statement.executeQuery();

            while (result.next()) {

                CommandSetting setting = new CommandSetting(
                        player,
                        result.getString("command"),
                        result.getBoolean("enabled")
                );

                results.add(setting);
            }

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            try { if (result != null) result.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }

        }

        return results;
    }

    /**
     * Add new CommandSetting
     *
     * @param setting CommandSetting
     * @return boolean
     */
    public boolean insertCommandSetting(CommandSetting setting) {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        int id = 0;

        String sql = "INSERT INTO " + tblName +
                " (player_unique_id, world_unique_id, command, enabled)" +
                " VALUES (?, ?, ?, ?)";
        try {

            connection = database.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, setting.getPlayerUniqueId());
            statement.setObject(2, setting.getWorldUniqueId());
            statement.setString(3, setting.getCommand());
            statement.setBoolean(4, setting.isEnabled());
            statement.executeUpdate();
            result = statement.getGeneratedKeys();

            if (result.next()) {
                id = result.getInt(1);
            }

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            try { if (result != null) result.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }

        }

        return (id > 0);
    }

    /**
     * Update a CommandSetting
     *
     * @param setting CommandSetting
     * @return boolean
     */
    public boolean updateCommandSetting(CommandSetting setting) {

        Connection connection = null;
        PreparedStatement statement = null;
        int rows = 0;

        String sql = "UPDATE " + tblName +
                " SET enabled = ?" +
                " WHERE player_unique_id = ?" +
                " AND world_unique_id =?" +
                " AND command = ?";

        try {

            connection = database.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setBoolean(1, setting.isEnabled());
            statement.setObject(2, setting.getPlayerUniqueId());
            statement.setObject(3, setting.getWorldUniqueId());
            statement.setString(4, setting.getCommand());
            rows = statement.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }

        }

        return (rows > 0);
    }
}
