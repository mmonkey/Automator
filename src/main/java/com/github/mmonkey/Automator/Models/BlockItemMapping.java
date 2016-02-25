package com.github.mmonkey.Automator.Models;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.ItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockItemMapping {

    private String name;
    private List<BlockType> blocks;
    private List<ItemType> items;

    public BlockItemMapping(String name) {
        this.name = name;
        this.items = new ArrayList<>();
        this.blocks = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BlockType> getBlocks() {
        return this.blocks;
    }

    public void setBlocks(List<BlockType> blocks) {
        this.blocks = blocks;
    }

    public List<ItemType> getItems() {
        return this.items;
    }

    public void setItems(List<ItemType> items) {
        this.items = items;
    }

    /**
     * Get a List<String> containing this mappings block names
     *
     * @return List<String>
     */
    public List<String> getBlockNamesList() {
        return this.blocks.stream().map(BlockType::getName).collect(Collectors.toList());
    }

    /**
     * Get a List<String> containing this mappings item names
     *
     * @return List<String>
     */
    public List<String> getItemNamesList() {
        return this.items.stream().map(ItemType::getName).collect(Collectors.toList());
    }
}
