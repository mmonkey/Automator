package com.github.mmonkey.Automator.Listeners;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Models.CommandSetting;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.Hotbar;

import java.util.ArrayList;
import java.util.List;

public abstract class ListenerAbstract {

    protected Automator plugin;

    public ListenerAbstract(Automator plugin) {
        this.plugin = plugin;
    }

    protected CommandSetting getCommandSettings(Player player, String command) {
        ArrayList<CommandSetting> settings = plugin.getPlayerSettings(player);
        for (CommandSetting setting : settings) {
            if (setting.getCommand().equals(command)
                    && setting.getWorldUniqueId().equals(player.getWorld().getUniqueId())) {
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
