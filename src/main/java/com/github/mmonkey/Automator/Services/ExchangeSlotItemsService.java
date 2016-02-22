package com.github.mmonkey.Automator.Services;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;

public class ExchangeSlotItemsService {

    private Slot slotA;

    private Slot slotB;

    public void setSlotA(Slot slotA) {
        this.slotA = slotA;
    }

    public void setSlotB(Slot slotB) {
        this.slotB = slotB;
    }

    /**
     * Exchange ItemStack in Slot a, with ItemStack in Slot b
     */
    public void process() {

        ItemStack itemA = this.slotA.peek().isPresent() ? this.slotA.poll().get() : null;
        ItemStack itemB = this.slotB.peek().isPresent() ? this.slotB.poll().get() : null;

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

    public ExchangeSlotItemsService() {
    }

    public ExchangeSlotItemsService(Slot slotA, Slot slotB) {
        this.slotA = slotA;
        this.slotB = slotB;
    }
}
