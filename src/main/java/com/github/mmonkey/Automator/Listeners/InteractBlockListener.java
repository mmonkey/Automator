package com.github.mmonkey.Automator.Listeners;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Models.BlockItemMapping;
import com.github.mmonkey.Automator.Models.CommandSetting;
import com.github.mmonkey.Automator.Services.ExchangeHotbarItemWithGridItemService;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.property.block.SolidCubeProperty;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InteractBlockListener extends ListenerAbstract {

    public InteractBlockListener(Automator plugin) {
        super(plugin);
    }

    @Listener
    public void onPrimaryClickBlock(InteractBlockEvent.Primary event) {

        Optional<Player> optionalPlayer = event.getCause().first(Player.class);

        if (!optionalPlayer.isPresent()) {
            return;
        }

        Player player = optionalPlayer.get();
        CommandSetting setting = this.getCommandSettings(player, "tool");

        if (setting == null || !setting.isEnabled()) {
            return;
        }

        Optional<GameMode> gameMode = player.getGameModeData().get(Keys.GAME_MODE);

        if (gameMode.isPresent() && (gameMode.get()).equals(GameModes.CREATIVE)) {
            return;
        }

        Optional<ItemStack> itemInHand = player.getItemInHand();
        BlockSnapshot blockSnapshot = event.getTargetBlock();
        BlockItemMapping mapping = getBlockItemMapping(blockSnapshot.getState().getType());

        // No mapping found for this BlockType
        if (mapping == null) {
            return;
        }

        // Compatible tool is already in hand
        if (itemInHand.isPresent()) {
            for (ItemType item : mapping.getItems()) {
                if (itemInHand.get().getItem() == item) {
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
            int compatibleIndex = getCompatibleToolHotbarIndex(hotbar, mapping.getItems());
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
                for (ItemType item : mapping.getItems()) {
                    if (slot.contains(item)) {

                        new ExchangeHotbarItemWithGridItemService(selectedHotbarSlot, slot, grid).process();
                        return;

                    }
                }
            }
        }

    }

    @Listener
    public void onSecondaryClickBlock(InteractBlockEvent.Secondary event) {

        Optional<Player> optionalPlayer = event.getCause().first(Player.class);

        if (!optionalPlayer.isPresent()) {
            return;
        }

        Player player = optionalPlayer.get();
        CommandSetting torchSetting = this.getCommandSettings(player, "torch");
        CommandSetting refillSetting = this.getCommandSettings(player, "refill");
        if ((torchSetting == null || !torchSetting.isEnabled()) && (refillSetting == null || !refillSetting.isEnabled())) {
            return;
        }

        Optional<GameMode> gameMode = player.getGameModeData().get(Keys.GAME_MODE);
        if (gameMode.isPresent() && (gameMode.get()).equals(GameModes.CREATIVE)) {
            return;
        }

        Optional<ItemStack> itemInHand = player.getItemInHand();

        // Load block
        BlockSnapshot blockSnapshot = event.getTargetBlock();
        Location<World> block = blockSnapshot.getLocation().isPresent() ? blockSnapshot.getLocation().get() : null;
        if (block == null) {
            return;
        }

        // Auto torch logic
        if (torchSetting != null && torchSetting.isEnabled()) {

            // Check if there isn't already a torch in hand
            if (!itemInHand.isPresent() || (itemInHand.isPresent() && itemInHand.get().getItem() != ItemTypes.TORCH)) {

                // Only switch if there is a configured item in hand or hand is empty
                List<ItemType> torchItems = plugin.getMappingsConfig().getTorchItems();
                if (!itemInHand.isPresent() || (itemInHand.isPresent() && torchItems.contains(itemInHand.get().getItem()))) {

                    // Only switch to torch if the block that was clicked is solid (cant place torches otherwise)
                    Optional<SolidCubeProperty> solidProperty = block.getProperty(SolidCubeProperty.class);
                    if (!solidProperty.isPresent()) {
                        return;
                    }

                    // Confirmed not solid
                    SolidCubeProperty isSolid = solidProperty.get();
                    if (isSolid.getValue() == null || !isSolid.getValue()) {
                        return;
                    }

                    Slot selectedHotbarSlot = null;
                    Inventory hotbarInventory = player.getInventory().query(Hotbar.class);
                    if (hotbarInventory instanceof Hotbar) {
                        Hotbar hotbar = (Hotbar) hotbarInventory;
                        selectedHotbarSlot = getSelectedHotbarSlot(hotbar);

                        // If there are torches already in the hotbar, set selected index to the torches slot index
                        int compatibleIndex = getCompatibleToolHotbarIndex(hotbar, Collections.singletonList(ItemTypes.TORCH));
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
                            if (slot.contains(ItemTypes.TORCH)) {

                                new ExchangeHotbarItemWithGridItemService(selectedHotbarSlot, slot, grid).process();
                                return;

                            }
                        }
                    }

                }

            }
        }

        // Auto refill logic
        if (refillSetting != null && refillSetting.isEnabled()) {

            // Nothing to refill if there is nothing in hand
            if (!itemInHand.isPresent()) {
                return;
            }

            ItemStack item = itemInHand.get();
            ItemType itemType = item.getItem().getType();

            // If item is stackable and there is only one left add more items to inventory (after item is placed)
            if (item.getMaxStackQuantity() > 1 && item.getQuantity() == 1) {

                // Check the hotbar for a matching itemstack
                Slot selectedHotbarSlot = null;
                Inventory hotbarInventory = player.getInventory().query(Hotbar.class);
                if (hotbarInventory instanceof Hotbar) {
                    Hotbar hotbar = (Hotbar) hotbarInventory;
                    selectedHotbarSlot = getSelectedHotbarSlot(hotbar);

                    // If there is a matching item in the hotbar (not including the item in hand)
                    int compatibleIndex = getCompatibleNotSelectedHotbarIndex(hotbar, itemType);
                    if (compatibleIndex >= 0 && compatibleIndex <= 8) {

                        plugin.getGame().getScheduler().createTaskBuilder().delayTicks(1).execute(() -> {
                            hotbar.setSelectedSlotIndex(compatibleIndex);
                        }).submit(plugin);
                        return;

                    }
                }

                // Search the players grid inventory
                Inventory grid = player.getInventory().query(GridInventory.class);
                if (grid instanceof GridInventory) {
                    Iterable<Slot> gridSlots = grid.slots();
                    for (Slot slot : gridSlots) {
                        if (slot.contains(itemType)) {

                            final Slot selectedSlot = selectedHotbarSlot;
                            plugin.getGame().getScheduler().createTaskBuilder().delayTicks(1).execute(() -> {
                                new ExchangeHotbarItemWithGridItemService(selectedSlot, slot, grid).process();
                            }).submit(plugin);
                            return;

                        }
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
    private int getCompatibleToolHotbarIndex(Hotbar hotbar, List<ItemType> items) {

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
    private int getCompatibleNotSelectedHotbarIndex(Hotbar hotbar, ItemType itemType) {

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

    /**
     * Get the first mapping for this block
     *
     * @param block BlockType
     * @return BlockItemMapping|null
     */
    private BlockItemMapping getBlockItemMapping(BlockType block) {

        List<BlockItemMapping> blockItemMappings = plugin.getMappingsConfig().getBlockItemMappings();
        for (BlockItemMapping mapping : blockItemMappings) {
            if (mapping.getBlocks().contains(block)) {
                return mapping;
            }
        }

        return null;
    }

}
