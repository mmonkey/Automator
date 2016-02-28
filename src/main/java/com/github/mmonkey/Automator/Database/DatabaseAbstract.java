package com.github.mmonkey.Automator.Database;

import org.spongepowered.api.Game;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseAbstract {

    private Game game;
    private SqlService service;
    private String jdbcUrl = null;

    protected DatabaseAbstract(Game game) {
        this.game = game;
    }

    private String getJdbcUrl() {
        return (this.jdbcUrl != null) ? this.jdbcUrl : "";
    }

    protected void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public DataSource getDataSource(String jdbcUrl) {

        DataSource source = null;

        if (this.service == null) {
            this.service = this.game.getServiceManager().provide(SqlService.class).get();
        }

        try {
            source =  this.service.getDataSource(jdbcUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return source;
    }

    public DataSource getDataSource() {

        DataSource source = null;

        if (this.service == null) {
            this.service = this.game.getServiceManager().provide(SqlService.class).get();
        }

        try {
            source =  this.service.getDataSource(this.getJdbcUrl());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return source;

    }

    public Connection getConnection() {

        try {
            return this.getDataSource(this.getJdbcUrl()).getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
