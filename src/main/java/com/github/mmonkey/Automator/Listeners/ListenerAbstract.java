package com.github.mmonkey.Automator.Listeners;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Models.CommandSetting;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;

public abstract class ListenerAbstract {

    protected Automator plugin;

    public ListenerAbstract(Automator plugin) {
        this.plugin = plugin;
    }

    protected CommandSetting getCommandSettings(Player player, String command) {
        ArrayList<CommandSetting> settings = plugin.getPlayerSettings(player);
        for (CommandSetting setting : settings) {
            if (setting.getCommand().equals(command)) {
                return setting;
            }
        }

        return null;
    }

}
