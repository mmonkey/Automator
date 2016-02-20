package com.github.mmonkey.Automator;

import com.github.mmonkey.Automator.Listeners.InteractWithBlock;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;

@Plugin(id = Automator.ID, name = Automator.NAME, version = Automator.VERSION)
public class Automator {

    public static final String NAME = "Automator";
    public static final String ID = "Automator";
    public static final String VERSION = "0.0.1-3.0.0";
    public static final int CONFIG_VERSION = 1;

    private static Automator instance;

    @Inject
    private Game game;

    @Inject
    private static Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;

    private DefaultConfig defaultConfig;

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

        // TODO load default config & run config migrations
    }

    @Listener
    public void onInit(GameInitializationEvent event) {

        // Register Events
        DefaultToolMapping.initialize();

        game.getEventManager().registerListeners(this, new InteractWithBlock());

    }
}
