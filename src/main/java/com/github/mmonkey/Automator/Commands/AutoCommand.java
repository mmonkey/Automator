package com.github.mmonkey.Automator.Commands;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Configs.DefaultConfig;
import com.github.mmonkey.Automator.Models.CommandSetting;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class AutoCommand extends CommandAbstract {

    public AutoCommand(Automator plugin) {
        super(plugin);
    }

    @Override
    protected String getCommandIdentifier() {
        return "";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Player player = null;
        if (src instanceof Player) {
            player = (Player) src;
        }

        Text.Builder commands = Text.builder();
        commands.append(Text.of(TextColors.YELLOW, "================= Automator Commands ================="));

        if (plugin.getDefaultConfig().get().getNode(DefaultConfig.COMMANDS, "refill").getBoolean()) {
            commands.append(Text.of(Text.NEW_LINE));
            commands.append(Text.of(getAutoSubcommand("refill"), TextColors.WHITE, " - Automatically refill items in hand when you run out."));
            if (player != null) {
                CommandSetting setting = this.getCommandSetting(player, "refill");
                boolean enabled = (setting != null && setting.isEnabled());
                commands.append(Text.of(" Currently: ", enabled ? Text.of(TextColors.GREEN, "enabled") : Text.of(TextColors.RED, "disabled")));
            }
        }

        if (plugin.getDefaultConfig().get().getNode(DefaultConfig.COMMANDS, "tool").getBoolean()) {
            commands.append(Text.of(Text.NEW_LINE));
            commands.append(Text.of(getAutoSubcommand("tool"), TextColors.WHITE, " - Automatically switch to a compatible tool when you hit a block."));
            if (player != null) {
                CommandSetting setting = this.getCommandSetting(player, "tool");
                boolean enabled = (setting != null && setting.isEnabled());
                commands.append(Text.of(" Currently: ", enabled ? Text.of(TextColors.GREEN, "enabled") : Text.of(TextColors.RED, "disabled")));
            }
        }

        if (plugin.getDefaultConfig().get().getNode(DefaultConfig.COMMANDS, "torch").getBoolean()) {
            commands.append(Text.of(Text.NEW_LINE));
            commands.append(Text.of(getAutoSubcommand("torch"), TextColors.WHITE, " - Automatically switch to torches when you right-click a compatible block and have a tool in hand."));
            if (player != null) {
                CommandSetting setting = this.getCommandSetting(player, "torch");
                boolean enabled = (setting != null && setting.isEnabled());
                commands.append(Text.of(" Currently: ", enabled ? Text.of(TextColors.GREEN, "enabled") : Text.of(TextColors.RED, "disabled")));
            }
        }

        commands.append(Text.NEW_LINE);
        commands.append(Text.of(TextColors.YELLOW, "====================================================="));
        src.sendMessage(commands.build());

        return CommandResult.empty();
    }

    private Text getAutoSubcommand(String subcommand) {
        return Text.builder("/auto " + subcommand)
                .onClick(TextActions.runCommand("/auto " + subcommand))
                .onHover(TextActions.showText(Text.of(TextColors.WHITE, "Run command ", TextColors.YELLOW, "/auto " + subcommand)))
                .color(TextColors.AQUA)
                .style(TextStyles.UNDERLINE)
                .build();
    }

}
