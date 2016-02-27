package com.github.mmonkey.Automator;

import com.github.mmonkey.Automator.Commands.AutoCommand;
import com.github.mmonkey.Automator.Commands.RefillCommand;
import com.github.mmonkey.Automator.Commands.ToolCommand;
import com.github.mmonkey.Automator.Commands.TorchCommand;
import com.github.mmonkey.Automator.Configs.DefaultConfig;
import com.github.mmonkey.Automator.Configs.MappingsConfig;
import com.github.mmonkey.Automator.Listeners.InteractBlockListener;
import com.github.mmonkey.Automator.Migrations.ConfigMigrationRunner;
import com.github.mmonkey.Automator.Models.CommandSetting;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.util.*;

@Plugin(id = Automator.ID, name = Automator.NAME, version = Automator.VERSION)
public class Automator {

    public static final String NAME = "Automator";
    public static final String ID = "Automator";
    public static final String VERSION = "0.0.1-3.0.0";
    public static final int CONFIG_VERSION = 0;

    private static Automator instance;

    @Inject
    private Game game;

    @Inject
    private static Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;

    private DefaultConfig defaultConfig;
    private MappingsConfig mappingsConfig;
    private HashMap<UUID, ArrayList<CommandSetting>> settings = new HashMap<>();

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
     * @return MappingsConfig
     */
    public MappingsConfig getMappingsConfig() {
        return this.mappingsConfig;
    }

    /**
     * Get all CommandSettings for a Player as an ArrayList
     *
     * @param player Player
     * @return ArrayList<CommandSetting>
     */
    public ArrayList<CommandSetting> getPlayerSettings(Player player) {

        if (this.settings.containsKey(player.getUniqueId())) {
            return settings.get(player.getUniqueId());
        }
        this.settings.put(player.getUniqueId(), new ArrayList<CommandSetting>());
        return new ArrayList<CommandSetting>();

    }

    /**
     * Set all CommandSettings for a Player
     *
     * @param player   Player
     * @param settings ArrayList<CommandSetting>
     */
    public void setPlayerSettings(Player player, ArrayList<CommandSetting> settings) {
        this.settings.put(player.getUniqueId(), settings);
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

        // Load mappings config
        this.mappingsConfig = new MappingsConfig(this, this.configDir);
        this.mappingsConfig.load();

        // Run config migrations
        int configVersion = this.defaultConfig.get().getNode(DefaultConfig.CONFIG_VERSION).getInt(0);
        ConfigMigrationRunner configMigrationRunner = new ConfigMigrationRunner(this, configVersion);
        configMigrationRunner.run();
    }

    @Listener
    public void onInit(GameInitializationEvent event) {

        game.getEventManager().registerListeners(this, new InteractBlockListener(this));
        HashMap<List<String>, CommandSpec> subcommands = new HashMap<>();

        /**
         * /auto refill
         */
        if (this.getDefaultConfig().get().getNode(DefaultConfig.COMMANDS, "refill").getBoolean()) {
            subcommands.put(Collections.singletonList("refill"), CommandSpec.builder()
                    .permission("auto.refill")
                    .description(Text.of("Enable/disable auto refill"))
                    .extendedDescription(Text.of("Enable or disable automatic item refilling."))
                    .executor(new RefillCommand(this))
                    .build());
        }

        /**
         * /auto tool
         */
        if (this.getDefaultConfig().get().getNode(DefaultConfig.COMMANDS, "tool").getBoolean()) {
            subcommands.put(Collections.singletonList("tool"), CommandSpec.builder()
                    .permission("auto.tool")
                    .description(Text.of("Enable/disable tool swapping"))
                    .extendedDescription(Text.of("Enable or disable automatic tool swapping."))
                    .executor(new ToolCommand(this))
                    .build());
        }

        /**
         * /auto torch
         */
        if (this.getDefaultConfig().get().getNode(DefaultConfig.COMMANDS, "torch").getBoolean()) {
            subcommands.put(Collections.singletonList("torch"), CommandSpec.builder()
                    .permission("auto.torch")
                    .description(Text.of("Enable/disable torch swapping"))
                    .extendedDescription(Text.of("Enable or disable automatic torch swapping."))
                    .executor(new TorchCommand(this))
                    .build());
        }

        /**
         * /auto
         */
        if (!subcommands.isEmpty()) {
            CommandSpec autoCommand = CommandSpec.builder()
                    .description(Text.of("/auto commands list"))
                    .extendedDescription(Text.of("Shows which commands are available for Automator."))
                    .executor(new AutoCommand(this))
                    .children(subcommands)
                    .build();
            game.getCommandManager().register(this, autoCommand, "auto");
        }

    }

}
