package com.github.mmonkey.Automator.Database;

import com.github.mmonkey.Automator.Automator;
import org.h2.tools.Server;
import org.spongepowered.api.Game;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class H2EmbeddedDatabase extends DatabaseAbstract {

    private static final String defaultPath = "." + File.separator + "data";

    private String dbName;
    private String username;
    private String password;
    private Server webServer;
    private Server tcpServer;
    private File directory = null;

    public H2EmbeddedDatabase(Game game, String dbName, String username, String password) {
        super(game);
        this.dbName = dbName;
        this.username = username;
        this.password = password;
    }

    public boolean setDirectory(File directory) {

        if(directory.exists() && directory.isDirectory()) {
            this.directory = directory;
            return true;
        }

        return false;
    }

    public Connection getConnection() {

        String path = (this.directory != null && this.directory.exists()) ? this.directory.getPath() : defaultPath;
        this.setJdbcUrl("jdbc:h2:file:" + path + File.separator + this.dbName + ";user=" + this.username + ";password=" + this.password);
        return super.getConnection();

    }

    public boolean startWebServer() {

        try {

            this.webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            this.tcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "true", "-tcpPort", "9092", "-tcpPassword", "test123").start();
            Automator.getLogger().info("H2 console started on port 8082.");
            return true;

        } catch (SQLException e) {

            Automator.getLogger().info("H2 console was unable to start.");
            return false;

        }
    }

    public void stopWebServer() {

        this.webServer.stop();
        this.tcpServer.stop();

    }

}
