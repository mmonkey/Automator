package com.github.mmonkey.Automator.Services;

import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;

public class AddItemToInventoryService {
    private Inventory inventory;
    private ItemStack item;

    public AddItemToInventoryService(Inventory inventory, ItemStack item) {
        this.inventory = inventory;
        this.item = item;
    }

    public void process() {
        if (this.inventory != null && this.item != null) {
            Iterable<Slot> inventorySlots = inventory.slots();

            for (Slot slot : inventorySlots) {
                if (slot.contains(this.item) && this.item.getQuantity() > 0) {
                    ItemStack slotItem = slot.peek().isPresent() ? slot.peek().get() : null;
                    if (slotItem != null) {
                        if (slotItem.getQuantity() != slotItem.getMaxStackQuantity()) {
                            int remaining = slotItem.getMaxStackQuantity() - slotItem.getQuantity();

                            if (remaining >= this.item.getQuantity()) {

                                slotItem.setQuantity(slotItem.getQuantity() + this.item.getQuantity());
                                this.item.setQuantity(0);
                                slot.set(slotItem);
                                return;

                            } else {

                                slotItem.setQuantity(slotItem.getMaxStackQuantity());
                                this.item.setQuantity(this.item.getQuantity() - remaining);
                                slot.set(slotItem);

                            }
                        }
                    }
                }
            }

            if (this.item.getQuantity() > 0) {
                for (Slot slot : inventorySlots) {
                    if (slot.getStackSize() == 0) {
                        slot.set(this.item);
                        return;
                    }
                }
            }
        }
    }

}
