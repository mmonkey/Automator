package com.github.mmonkey.Automator.Migrations.DatabaseMigrations;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Configs.DefaultConfig;
import com.github.mmonkey.Automator.Database.Database;
import com.github.mmonkey.Automator.Migrations.Migration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class M01_AddInitialDatabaseTables extends Migration {

    private Database database;

    @Override
    public void up() {

        Connection connection = null;
        Statement statement = null;
        StringBuilder sql = new StringBuilder();

        // Create worlds table
        sql.append("CREATE TABLE IF NOT EXISTS worlds "
                + "(id INT PRIMARY KEY AUTO_INCREMENT NOT NULL, unique_id VARCHAR(255) NOT NULL);");

        // Create players table
        sql.append("CREATE TABLE IF NOT EXISTS players "
                + "(id INT PRIMARY KEY AUTO_INCREMENT NOT NULL, unique_id VARCHAR(255) NOT NULL);");

        // Create homes table
        sql.append("CREATE TABLE IF NOT EXISTS player_command_settings" +
                " (id INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
                " player_id INT UNSIGNED NOT NULL," +
                " word_id INT UNSIGNED NOT NULL," +
                " command_type_id TINYINT UNSIGNED NOT NULL," +
                " enabled TINYINT UNSIGNED NOT NULL);");

        try {

            connection = database.getConnection();
            statement = connection.createStatement();
            statement.execute(sql.toString());

            this.bumpVersion(DefaultConfig.DATABASE_VERSION, 1);

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }

        }

    }

    public M01_AddInitialDatabaseTables(Automator plugin) {
        super(plugin);
        this.database = this.plugin.getDatabase();
    }
}
