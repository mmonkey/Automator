package com.github.mmonkey.Automator.Commands;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Models.CommandSetting;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class TorchCommand extends CommandAbstract {

    public TorchCommand(Automator plugin) {
        super(plugin);
    }

    @Override
    protected String getCommandIdentifier() {
        return "torch";
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!(src instanceof Player)) {
            return CommandResult.success();
        }

        Player player = (Player) src;

        CommandSetting setting = this.getCommandSetting(player);
        if (setting == null) {
            setting = new CommandSetting(player, this.getCommandIdentifier(), false);
        }

        if (setting.isEnabled()) {

            setting.setEnabled(false);
            this.saveCommandSetting(player, setting);
            player.sendMessage(Text.of(TextColors.GOLD, "Automatic torch switching has been disabled."));

        } else {

            setting.setEnabled(true);
            this.saveCommandSetting(player, setting);
            player.sendMessage(Text.of(TextColors.GREEN, "Automatic torch switching has been enabled."));

        }

        return CommandResult.success();

    }

}
