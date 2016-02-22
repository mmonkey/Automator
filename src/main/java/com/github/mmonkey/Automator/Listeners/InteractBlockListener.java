package com.github.mmonkey.Automator.Listeners;

import com.github.mmonkey.Automator.DefaultToolMapping;
import com.github.mmonkey.Automator.Services.ExchangeSlotItemsService;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.type.GridInventory;

import java.util.Optional;

public class InteractBlockListener {

    @Listener
    public void onPrimaryClickBlock(InteractBlockEvent.Primary event) {

        Optional<Player> optionalPlayer = event.getCause().first(Player.class);

        if (!optionalPlayer.isPresent()) {
            return;
        }

        Player player = optionalPlayer.get();
        Optional<GameMode> gameMode = player.getGameModeData().get(Keys.GAME_MODE);

        if (gameMode.isPresent() && (gameMode.get()).equals(GameModes.CREATIVE)) {
            return;
        }

        Optional<ItemStack> itemInHand = player.getItemInHand();
        BlockSnapshot blockSnapshot = event.getTargetBlock();
        ItemType[] defaultMappings = DefaultToolMapping.getBlockToolDefaults(blockSnapshot.getState().getType());

        // No tool found for this block
        if (defaultMappings == null) {
            return;
        }

        // Compatible tool is already in hand
        if (itemInHand.isPresent()) {
            for (ItemType tool : defaultMappings) {
                if (itemInHand.get().getItem() == tool) {
                    return;
                }
            }
        }

        Slot selectedHotbarSlot = null;
        Inventory hotbarInventory = player.getInventory().query(Hotbar.class);
        if (hotbarInventory instanceof Hotbar) {
            Hotbar hotbar = (Hotbar) hotbarInventory;
            selectedHotbarSlot = getSelectedHotbarSlot(hotbar);

            // If there is a compatible tool already in the hotbar, set selected index to that item's slot index
            int compatibleIndex = getCompatibleToolHotbarIndex(hotbar, defaultMappings);
            if (compatibleIndex >= 0 && compatibleIndex <= 8) {
                hotbar.setSelectedSlotIndex(compatibleIndex);
                return;
            }

        }

        // Search the players grid inventory
        Inventory grid = player.getInventory().query(GridInventory.class);
        if (grid instanceof GridInventory) {
            Iterable<Slot> gridSlots = grid.slots();
            for (Slot slot : gridSlots) {
                for (ItemType tool : defaultMappings) {
                    if (slot.contains(tool)) {

                        new ExchangeSlotItemsService(selectedHotbarSlot, slot).process();
                        return;

                    }
                }
            }
        }

    }

    /**
     * Return's the Slot of the selected slot index
     *
     * @param hotbar Hotbar
     * @return Slot
     */
    private Slot getSelectedHotbarSlot(Hotbar hotbar) {

        int activeIndex = hotbar.getSelectedSlotIndex();
        Optional<Slot> slot = hotbar.getSlot(new SlotIndex(activeIndex));
        return slot.isPresent() ? slot.get() : null;

    }

    /**
     * Returns the index of the first tool matching the tools array in the Hotbar;
     *
     * @param hotbar Hotbar
     * @param tools ItemType[]
     * @return int
     */
    private int getCompatibleToolHotbarIndex(Hotbar hotbar, ItemType[] tools) {

        for (int index = 0; index < hotbar.size(); index ++) {
            Optional<Slot> slot = hotbar.getSlot(new SlotIndex(index));
            if (slot.isPresent()) {
                for (ItemType tool : tools) {
                    if (slot.get().contains(tool)) {
                        return index;
                    }
                }
            }
        }

        return -1;

    }

    private void swapItemInHand(Player player, Slot activeSlot, Slot toolSlot) {

        ItemStack itemInHand = player.getItemInHand().isPresent() ? player.getItemInHand().get() : null;
        ItemStack itemInSlot = toolSlot.peek().isPresent() ? toolSlot.poll().get() : null;

        if (activeSlot != null) {
            activeSlot.clear();
        }

        if (itemInSlot != null) {
            toolSlot.clear();
            player.setItemInHand(itemInSlot);
        }

        if (itemInHand != null) {
            toolSlot.offer(itemInHand);
        }
    }

    private void swapSlotItems(Slot slotA, Slot slotB) {

        ItemStack itemA = slotA.peek().isPresent() ? slotA.poll().get() : null;
        ItemStack itemB = slotB.peek().isPresent() ? slotB.poll().get() : null;

        if (itemA != null) {
            slotA.clear();
        }

        if (itemB != null) {
            slotB.clear();
            slotA.offer(itemB);
        }

        if (itemA != null) {
            slotB.offer(itemA);
        }

    }

}
