package com.github.mmonkey.Automator.Models;

import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class CommandSetting {

    private UUID playerUniqueId;
    private UUID worldUniqueId;
    private String command;
    private boolean enabled;

    public CommandSetting(Player player, String command, boolean enabled) {
        this.playerUniqueId = player.getUniqueId();
        this.worldUniqueId = player.getWorld().getUniqueId();
        this.command = command;
        this.enabled = enabled;
    }

    public UUID getPlayerUniqueId() {
        return playerUniqueId;
    }

    public void setPlayerUniqueId(UUID playerUniqueId) {
        this.playerUniqueId = playerUniqueId;
    }

    public UUID getWorldUniqueId() {
        return worldUniqueId;
    }

    public void setWorldUniqueId(UUID worldUniqueId) {
        this.worldUniqueId = worldUniqueId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
