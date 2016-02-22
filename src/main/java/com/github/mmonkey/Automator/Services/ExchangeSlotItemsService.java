package com.github.mmonkey.Automator.Services;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;

public class ExchangeSlotItemsService {

    private Slot slotA;
    private Slot slotB;

    public ExchangeSlotItemsService(Slot slotA, Slot slotB) {
        this.slotA = slotA;
        this.slotB = slotB;
    }

    /**
     * Exchange ItemStack in Slot a, with ItemStack in Slot b
     */
    public void process() {

        ItemStack itemA = slotA.peek().isPresent() ? slotA.poll().get() : null;
        ItemStack itemB = slotB.peek().isPresent() ? slotB.poll().get() : null;

        if (itemA != null) {
            this.slotA.clear();
        }

        if (itemB != null) {
            this.slotB.clear();
            this.slotA.offer(itemB);
        }

        if (itemA != null) {
            this.slotB.offer(itemA);
        }
    }
}
