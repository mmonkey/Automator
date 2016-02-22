package com.github.mmonkey.Automator.Dams;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Database.Database;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.sql.*;

public class PlayerCommandSettingsDam {

    public static final String tblName = "player_command_settings";

    private Database database;
    private PlayerDam playerDam;
    private WorldDam worldDam;

    public boolean isCommandEnabled(Player player, World world, int commandType) {

        boolean enabled = false;

        int commandSettingId = 0;
        int playerId = this.playerDam.getPlayerId(player.getUniqueId());
        int worldId = this.worldDam.getWorldId(world.getUniqueId());

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        String sql = "SELECT" +
                " id" +
                " enabled" +
                " FROM " + tblName +
                " WHERE player_id = ?" +
                " AND world_id = ?" +
                " AND command_type_id = ?" +
                " LIMIT 1";

        try {

            connection = database.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, playerId);
            statement.setInt(2, worldId);
            statement.setInt(3, commandType);
            result = statement.executeQuery();

            while (result.next()) {

                commandSettingId = result.getInt("id");
                enabled = (result.getInt("enabled") > 0);
            }

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            try { if (result != null) result.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }

        }

        if (commandSettingId == 0) {
            this.insertCommandSetting(player, world, commandType, false);
        }

        return enabled;
    }

    public boolean setEnabled(Player player, World world, int commandType, boolean isEnabled) {

        // Return true if player's setting == isEnabled
        // Return true if player's setting != isEnabled, and it successfully set the new isEnabled.
        return (this.isCommandEnabled(player, world, commandType) == isEnabled)
                || (this.updatePlayerCommandSetting(player, world, commandType, isEnabled) > 0);

    }

    private int insertCommandSetting(Player player, World world, int commandType, boolean isEnabled) {

        int id = 0;

        int enabled = isEnabled ? 1 : 0;
        int playerId = this.playerDam.getPlayerId(player.getUniqueId());
        int worldId = this.worldDam.getWorldId(world.getUniqueId());

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        String sql = "INSERT INTO " + tblName + " (player_id, world_id, command_type_id, enabled) VALUES (?)";

        try {

            connection = database.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, playerId);
            statement.setInt(2, worldId);
            statement.setInt(3, commandType);
            statement.setInt(4, enabled);
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

        return id;
    }

    private int updatePlayerCommandSetting(Player player, World world, int commandType, boolean isEnabled) {

        int updated = 0;
        int playerId = this.playerDam.getPlayerId(player.getUniqueId());
        int worldId = this.worldDam.getWorldId(world.getUniqueId());

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "INSERT INTO " + tblName + " (enabled) VALUES (?)"
                + " WHERE player_id = ? AND world_id = ? AND command_type_id = ?";

        try {

            connection = database.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, playerId);
            statement.setInt(2, worldId);
            statement.setInt(3, commandType);
            updated = statement.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }

        }

        return updated;
    }

    public PlayerCommandSettingsDam(Automator plugin) {
        this.database = plugin.getDatabase();
        this.playerDam = new PlayerDam(plugin.getDatabase());
        this.worldDam = new WorldDam(plugin.getDatabase());
    }

}
