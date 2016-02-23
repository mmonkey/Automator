package com.github.mmonkey.Automator.Commands;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Models.CommandSetting;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;

public abstract class CommandAbstract implements CommandExecutor {

    protected Automator plugin;

    public CommandAbstract(Automator plugin) {
        this.plugin = plugin;
    }

    protected abstract String getCommandIdentifier();

    protected CommandSetting getCommandSetting(Player player) {
        ArrayList<CommandSetting> settings = plugin.getPlayerSettings(player);
        for (CommandSetting setting : settings) {
            if (setting.getCommand().equals(this.getCommandIdentifier())
                    && setting.getWorldUniqueId().equals(player.getWorld().getUniqueId())) {
                return setting;
            }
        }

        return null;
    }

    protected void saveCommandSetting(Player player, CommandSetting setting) {

        ArrayList<CommandSetting> settings = plugin.getPlayerSettings(player);
        CommandSetting current = this.getCommandSetting(player);
        if (current != null) {
            settings.remove(current);
        }

        settings.add(setting);
        plugin.setPlayerSettings(player, settings);

    }

}
