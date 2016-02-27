package com.github.mmonkey.Automator.Models;

import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.item.ItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityItemMapping {

    public static final String INTERACTION_TYPE_PRIMARY = "primary";
    public static final String INTERACTION_TYPE_SECONDARY = "secondary";

    private String name;
    private String interactionType;
    private List<EntityType> entities;
    private List<ItemType> items;

    public EntityItemMapping(String name) {
        this.name = name;
        this.interactionType = "";
        this.items = new ArrayList<>();
        this.entities = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    public List<EntityType> getEntities() {
        return this.entities;
    }

    public void setEntities(List<EntityType> entities) {
        this.entities = entities;
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
    public List<String> getEntityNameList() {
        return this.entities.stream().map(EntityType::getName).collect(Collectors.toList());
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
