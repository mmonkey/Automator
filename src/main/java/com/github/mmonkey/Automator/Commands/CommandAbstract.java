package com.github.mmonkey.Automator.Commands;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Dams.CommandSettingDam;
import com.github.mmonkey.Automator.Models.CommandSetting;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandAbstract implements CommandExecutor {

    protected Automator plugin;
    protected CommandSettingDam commandSettingDam;

    public CommandAbstract(Automator plugin) {
        this.plugin = plugin;
        this.commandSettingDam = new CommandSettingDam(plugin.getDatabase());
    }

    protected abstract String getCommandIdentifier();

    /**
     * Get this Player's CommandSettings List
     *
     * @param player Player
     * @return CommandSetting
     */
    protected List<CommandSetting> loadCommandSettings(Player player) {
        List<CommandSetting> settings = plugin.getPlayerSettings(player);
        if (settings.isEmpty()) {
            settings = commandSettingDam.getCommandSettings(player);
            plugin.setPlayerSettings(player, settings);
        }
        return settings;
    }

    /**
     * Get the Player's CommandSetting for this commandIdentifier
     *
     * @param player Player
     * @return CommandSetting
     */
    protected CommandSetting getCommandSetting(Player player) {
        List<CommandSetting> settings = this.loadCommandSettings(player);
        for (CommandSetting setting : settings) {
            if (setting.getCommand().equals(this.getCommandIdentifier())) {
                return setting;
            }
        }

        return null;
    }

    /**
     * Get the Player's CommandSetting for this command
     *
     * @param player Player
     * @param command String
     * @return CommandSetting
     */
    protected CommandSetting getCommandSetting(Player player, String command) {
        List<CommandSetting> settings = this.loadCommandSettings(player);
        String identifier = command.isEmpty() ? this.getCommandIdentifier() : command;
        for (CommandSetting setting : settings) {
            if (setting.getCommand().equals(identifier)) {
                return setting;
            }
        }

        return null;
    }

    /**
     * Save the Player's CommandSetting
     *
     * @param player Player
     * @param setting CommandSetting
     */
    protected void saveCommandSetting(Player player, CommandSetting setting) {

        CommandSetting current = this.getCommandSetting(player);
        List<CommandSetting> settings = plugin.getPlayerSettings(player);

        if (current != null) {
            settings.remove(current);
            this.commandSettingDam.updateCommandSetting(setting);
        } else {
            this.commandSettingDam.insertCommandSetting(setting);
        }

        settings.add(setting);
        plugin.setPlayerSettings(player, settings);
    }

}
