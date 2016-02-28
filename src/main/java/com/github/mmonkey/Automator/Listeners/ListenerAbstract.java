package com.github.mmonkey.Automator.Listeners;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Dams.CommandSettingDam;
import com.github.mmonkey.Automator.Models.CommandSetting;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.Hotbar;

import java.util.ArrayList;
import java.util.List;

public abstract class ListenerAbstract {

    protected Automator plugin;
    protected CommandSettingDam commandSettingDam;

    public ListenerAbstract(Automator plugin) {
        this.plugin = plugin;
        this.commandSettingDam = new CommandSettingDam(plugin.getDatabase());
    }

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
     * Get the Player's CommandSetting for this command
     *
     * @param player Player
     * @param command String
     * @return CommandSetting
     */
    protected CommandSetting getCommandSettings(Player player, String command) {
        List<CommandSetting> settings = this.loadCommandSettings(player);
        for (CommandSetting setting : settings) {
            if (setting.getCommand().equals(command)) {
                return setting;
            }
        }

        return null;
    }

    /**
     * Return's the Slot of the selected slot index
     *
     * @param hotbar Hotbar
     * @return Slot
     */
    protected Slot getSelectedHotbarSlot(Hotbar hotbar) {

        int index = 0;
        int activeIndex = hotbar.getSelectedSlotIndex();
        Iterable<Slot> hotbarSlots = hotbar.slots();
        for (Slot slot : hotbarSlots) {
            if (index == activeIndex) {
                return slot;
            }
            index++;
        }

        return null;
    }

    /**
     * Returns the index of the first tool matching the tools array in the Hotbar.
     * Returns -1 if no matching item was found.
     *
     * @param hotbar Hotbar
     * @param items  List<ItemType>
     * @return int
     */
    protected int getCompatibleToolHotbarIndex(Hotbar hotbar, List<ItemType> items) {

        int index = 0;
        Iterable<Slot> hotbarSlots = hotbar.slots();
        for (Slot slot : hotbarSlots) {
            for (ItemType tool : items) {
                if (slot.contains(tool)) {
                    return index;
                }
            }
            index++;
        }

        return -1;
    }

    /**
     * Returns the first index of the matching item in the Hotbar (not including the active item in hand).
     * Returns -1 if no matching item was found.
     *
     * @param hotbar   Hotbar
     * @param itemType ItemStack
     * @return int
     */
    protected int getCompatibleNotSelectedHotbarIndex(Hotbar hotbar, ItemType itemType) {

        int index = 0;
        int activeSlot = hotbar.getSelectedSlotIndex();
        Iterable<Slot> hotbarSlots = hotbar.slots();
        for (Slot slot : hotbarSlots) {
            if (index != activeSlot && slot.contains(itemType)) {
                return index;
            }
            index++;
        }

        return -1;
    }

}
