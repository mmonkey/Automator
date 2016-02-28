package com.github.mmonkey.Automator.Migrations.DatabaseMigrations;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Configs.DefaultConfig;
import com.github.mmonkey.Automator.Dams.CommandSettingDam;
import com.github.mmonkey.Automator.Database.DatabaseAbstract;
import com.github.mmonkey.Automator.Migrations.Migration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class M01_AddInitialDatabaseTables extends Migration {

    private DatabaseAbstract database;

    public M01_AddInitialDatabaseTables(Automator plugin) {
        super(plugin);
        this.database = this.plugin.getDatabase();
    }

    @Override
    public void up() {

        Connection connection = null;
        Statement statement = null;
        StringBuilder sql = new StringBuilder();

        // Create destinations table
        sql.append("CREATE TABLE IF NOT EXISTS " + CommandSettingDam.tblName +
                " (id INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
                " player_unique_id UUID NOT NULL," +
                " world_unique_id UUID NOT NULL," +
                " command VARCHAR NOT NULL," +
                " enabled BOOLEAN NOT NULL);");

        try {

            connection = database.getConnection();
            statement = connection.createStatement();
            statement.execute(sql.toString());

            this.bumpDatabaseVersion(1);

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }

        }
    }

}
