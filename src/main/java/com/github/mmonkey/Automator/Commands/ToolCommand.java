package com.github.mmonkey.Automator.Commands;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Dams.PlayerCommandSettingsDam;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class ToolCommand implements CommandExecutor {

    public static final int COMMAND_TYPE = 1;

    private PlayerCommandSettingsDam playerCommandSettingsDam;

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!(src instanceof Player)) {
            return CommandResult.success();
        }

        Player player = (Player) src;

        boolean isEnabled = this.playerCommandSettingsDam.isCommandEnabled(player, player.getWorld(), COMMAND_TYPE);
        String setting = isEnabled ? "off" : "on";

        if (this.playerCommandSettingsDam.setEnabled(player, player.getWorld(), COMMAND_TYPE, !isEnabled)) {
            player.sendMessage(Text.of("Tool automation has been turned " + setting + "."));
        } else {
            return CommandResult.empty();
        }

        return CommandResult.success();

    }

    public ToolCommand(Automator plugin) {
        this.playerCommandSettingsDam = new PlayerCommandSettingsDam(plugin);
    }

}
