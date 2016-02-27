package com.github.mmonkey.Automator.Listeners;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Models.CommandSetting;
import com.github.mmonkey.Automator.Models.EntityItemMapping;
import com.github.mmonkey.Automator.Services.ExchangeHotbarItemWithGridItemService;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.type.GridInventory;

import java.util.List;
import java.util.Optional;

public class InteractEntityListener extends ListenerAbstract {

    public InteractEntityListener(Automator plugin) {
        super(plugin);
    }

    @Listener
    public void onInteractEntity(InteractEntityEvent event) {

        // Only works for players
        Optional<Player> optionalPlayer = event.getCause().first(Player.class);
        if (!optionalPlayer.isPresent()) {
            return;
        }

        Player player = optionalPlayer.get();

        // Only works if player has the setting enabled
        CommandSetting setting = this.getCommandSettings(player, "tool");
        if (setting == null || !setting.isEnabled()) {
            return;
        }

        // Does nothing in creative mode
        Optional<GameMode> gameMode = player.getGameModeData().get(Keys.GAME_MODE);
        if (gameMode.isPresent() && (gameMode.get()).equals(GameModes.CREATIVE)) {
            return;
        }

        Entity target = event.getTargetEntity();
        String interactionType = (event instanceof InteractEntityEvent.Primary) ? EntityItemMapping.INTERACTION_TYPE_PRIMARY : EntityItemMapping.INTERACTION_TYPE_SECONDARY;
        EntityItemMapping mapping = this.getEntityItemMapping(target.getType(), interactionType);

        // No mapping set up for this entity and interaction type
        if (mapping == null) {
            return;
        }

        // Compatible tool is already in hand
        Optional<ItemStack> itemInHand = player.getItemInHand();
        if (itemInHand.isPresent()) {
            for (ItemType item : mapping.getItems()) {
                if (itemInHand.get().getItem() == item) {
                    return;
                }
            }
        }

        // Search the player's hotbar inventory
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

        // Search the player's grid inventory
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

    /**
     * Get the first mapping for this entity
     *
     * @param entity          EntityType
     * @param interactionType String
     * @return EntityItemMapping|null
     */
    private EntityItemMapping getEntityItemMapping(EntityType entity, String interactionType) {

        List<EntityItemMapping> entityItemMappings = plugin.getMappingsConfig().getEntityItemMappings();
        for (EntityItemMapping mapping : entityItemMappings) {
            if (mapping.getEntities().contains(entity) && mapping.getInteractionType().equals(interactionType)) {
                return mapping;
            }
        }

        return null;
    }
}
