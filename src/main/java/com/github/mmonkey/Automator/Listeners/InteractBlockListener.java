package com.github.mmonkey.Automator.Listeners;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.DefaultToolMapping;
import com.github.mmonkey.Automator.Models.CommandSetting;
import com.github.mmonkey.Automator.Services.ExchangeHotbarItemWithGridItemService;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.property.BooleanProperty;
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
        CommandSetting setting = this.getCommandSettings(player, "torch");

        if (setting == null || !setting.isEnabled()) {
            return;
        }

        Optional<GameMode> gameMode = player.getGameModeData().get(Keys.GAME_MODE);

        if (gameMode.isPresent() && (gameMode.get()).equals(GameModes.CREATIVE)) {
            return;
        }

        Optional<ItemStack> itemInHand = player.getItemInHand();

        // Torch is already in hand
        if (itemInHand.isPresent() && itemInHand.get().getItem() == ItemTypes.TORCH) {
            return;
        }

        BlockSnapshot blockSnapshot = event.getTargetBlock();
        Location<World> block = blockSnapshot.getLocation().isPresent() ? blockSnapshot.getLocation().get() : null;

        if (block == null) {
            return;
        }

        Optional<SolidCubeProperty> solidProperty = block.getProperty(SolidCubeProperty.class);

        // No solid property, definitely not solid block
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
            ItemType[] torch = {ItemTypes.TORCH};
            int compatibleIndex = getCompatibleToolHotbarIndex(hotbar, torch);
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
     * Returns the index of the first tool matching the tools array in the Hotbar;
     *
     * @param hotbar Hotbar
     * @param tools  ItemType[]
     * @return int
     */
    private int getCompatibleToolHotbarIndex(Hotbar hotbar, ItemType[] tools) {

        int index = 0;
        Iterable<Slot> hotbarSlots = hotbar.slots();
        for (Slot slot : hotbarSlots) {
            for (ItemType tool : tools) {
                if (slot.contains(tool)) {
                    return index;
                }
            }
            index++;
        }

        return -1;

    }

}
