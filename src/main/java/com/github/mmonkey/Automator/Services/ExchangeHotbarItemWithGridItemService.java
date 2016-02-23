package com.github.mmonkey.Automator.Services;

import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;

public class ExchangeHotbarItemWithGridItemService {

    private Slot hotbarSlot;
    private Slot gridSlot;
    private Inventory grid;

    public ExchangeHotbarItemWithGridItemService(Slot hotbarSlot, Slot gridSlot, Inventory grid) {
        this.hotbarSlot = hotbarSlot;
        this.gridSlot = gridSlot;
        this.grid = grid;
    }

    /**
     * Exchange ItemStack in Hand, with ItemStack in Grid
     */
    public void process() {

        ItemStack itemInHotbar = hotbarSlot.peek().isPresent() ? hotbarSlot.poll().get() : null;
        ItemStack itemInGrid = gridSlot.peek().isPresent() ? gridSlot.poll().get() : null;

        if (itemInGrid != null) {
            this.hotbarSlot.set(itemInGrid);
        }

        if (itemInHotbar != null) {

            // If itemInHand is stackable, combine with other like stacks if, then add remaining to first available slot
            if (this.grid != null && itemInHotbar.getMaxStackQuantity() > 1) {
                AddItemToInventoryService service = new AddItemToInventoryService(this.grid, itemInHotbar);
                service.process();

            // If itemInHand is not stackable, add item to gridSlot
            } else {
                this.gridSlot.set(itemInHotbar);
            }
        }
    }
}
