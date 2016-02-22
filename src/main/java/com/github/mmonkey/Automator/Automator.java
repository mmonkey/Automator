package com.github.mmonkey.Automator;

import com.github.mmonkey.Automator.Configs.DefaultConfig;
import com.github.mmonkey.Automator.Dams.TestConnectionDam;
import com.github.mmonkey.Automator.Database.Database;
import com.github.mmonkey.Automator.Database.H2EmbeddedDatabase;
import com.github.mmonkey.Automator.Listeners.InteractBlockListener;
import com.github.mmonkey.Automator.Migrations.ConfigMigrationRunner;
import com.github.mmonkey.Automator.Migrations.DatabaseMigrationRunner;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;

@Plugin(id = Automator.ID, name = Automator.NAME, version = Automator.VERSION)
public class Automator {

    public static final String NAME = "Automator";
    public static final String ID = "Automator";
    public static final String VERSION = "0.0.1-3.0.0";
    public static final int CONFIG_VERSION = 0;
    public static final int DATABASE_VERSION = 1;

    private static Automator instance;

    @Inject
    private Game game;

    @Inject
    private static Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;

    private DefaultConfig defaultConfig;
    private Database database;
    private boolean webServerRunning = false;

    /**
     * @return Automator
     */
    public static Automator getInstance() {
        return instance;
    }

    /**
     * @return Game
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * @return Logger
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * @return DefaultConfig
     */
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    /**
     * @return Database
     */
    public Database getDatabase() {
        return this.database;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {

        Automator.instance = this;
        Automator.logger = LoggerFactory.getLogger(Automator.NAME);
        getLogger().info(String.format("Starting up %s v%s.", Automator.NAME, Automator.VERSION));

        if (!this.configDir.isDirectory()) {
            if (this.configDir.mkdir()) {
                getLogger().info("Automator config directory successfully created!");
            }
        }

        // Load default config
        this.defaultConfig = new DefaultConfig(this.configDir);
        this.defaultConfig.load();

        // Run config migrations
        int configVersion = this.defaultConfig.get().getNode(DefaultConfig.CONFIG_VERSION).getInt(0);
        ConfigMigrationRunner configMigrationRunner = new ConfigMigrationRunner(this, configVersion);
        configMigrationRunner.run();

        // Setup database
        this.setupDatabase();

        // Run database migrations
        int databaseVersion = this.defaultConfig.get().getNode(DefaultConfig.DATABASE_VERSION).getInt(0);
        DatabaseMigrationRunner databaseMigrationRunner = new DatabaseMigrationRunner(this, databaseVersion);
        databaseMigrationRunner.run();
    }

    @Listener
    public void onInit(GameInitializationEvent event) {

        // Register Events
        DefaultToolMapping.initialize();

        game.getEventManager().registerListeners(this, new InteractBlockListener());

    }

    @Listener
    public void onServerStart(GameAboutToStartServerEvent event) {
        this.startWebServer();
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event) {
        if (this.database instanceof H2EmbeddedDatabase && this.webServerRunning) {
            ((H2EmbeddedDatabase) this.database).stopWebServer();
            this.webServerRunning = false;
        }
    }

    /**
     * Create database "automator", if it doesn't exist, and test connection
     */
    private void setupDatabase() {

        this.database = new H2EmbeddedDatabase(this.getGame(), "automator", "admin", "");
        this.startWebServer();

        TestConnectionDam testConnectionDam = new TestConnectionDam(this.database);
        if (testConnectionDam.testConnection()) {
            getLogger().info("Database connected successfully.");
        } else {
            getLogger().info("Unable to connect to database.");
        }

    }

    /**
     * If webserver is enabled, start the H2 webserver
     */
    private void startWebServer() {

        CommentedConfigurationNode dbConfig = this.defaultConfig.get().getNode(DefaultConfig.DATABASE);

        if (dbConfig.getNode("webserver").getBoolean()) {
            if (this.database instanceof H2EmbeddedDatabase && !this.webServerRunning) {
                if (((H2EmbeddedDatabase) this.database).startWebServer()) {
                    this.webServerRunning = true;
                }
            }
        }

    }

}
