package com.github.mmonkey.Automator.Listeners;

import com.github.mmonkey.Automator.DefaultToolMapping;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class InteractWithBlock {

    @Listener
    public void onPrimaryClickBlock(InteractBlockEvent.Primary event) {

        Optional<Player> optionalPlayer = event.getCause().first(Player.class);

        if (!optionalPlayer.isPresent()) {
            return;
        }

        Player player = optionalPlayer.get();
        Optional<ItemStack> itemInHand = player.getItemInHand();
        BlockSnapshot blockSnapshot = event.getTargetBlock();
        ItemType[] defaultMappings = DefaultToolMapping.getBlockToolDefaults(blockSnapshot.getState().getType());

        player.sendMessage(Text.of("You clicked a " + blockSnapshot.getState().getType().getName() + "."));

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

        // Search the players grid inventory
        Inventory gridInventory = player.getInventory().query(GridInventory.class);
        if (gridInventory instanceof GridInventory) {
            GridInventory grid = (GridInventory) gridInventory;
            Iterable<Slot> gridSlots = grid.slots();
            for (Slot slot : gridSlots) {
                for (ItemType tool : defaultMappings) {
                    if (slot.contains(tool)) {
                        player.sendMessage(Text.of("Grid contains " + tool.getType().getName()));
                    }
                }
            }
        }

        // Search the players hotbar
        Inventory hotBarInventory = player.getInventory().query(Hotbar.class);
        if (hotBarInventory instanceof Hotbar) {
            Hotbar hotBar = (Hotbar) hotBarInventory;
            for (ItemType tool : defaultMappings) {
                if (hotBar.contains(tool)) {
                    player.sendMessage(Text.of("Hotbar contains " + tool.getType().getName()));
                }
            }
        }

        // swap tools
    }
}
