package com.github.mmonkey.Automator.Configs;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Helpers.Mappings;
import com.github.mmonkey.Automator.Models.BlockItemMapping;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.ItemType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MappingsConfig extends Config {

    public static final String BLOCK_ITEM_MAPPINGS = "block-item-mappings";
    public static final String TORCH_ITEMS = "torch-items";
    public static final String BLOCKS = "blocks";
    public static final String ITEMS = "items";

    private Automator plugin;
    private List<BlockItemMapping> blockItemMappings;
    private List<ItemType> torchItems;

    public MappingsConfig(Automator plugin, File configDir) {
        super(configDir);

        this.plugin = plugin;
        this.blockItemMappings = new ArrayList<>();
        this.torchItems = new ArrayList<>();
        setConfigFile(new File(configDir, "mappings.conf"));
    }

    @Override
    public void load() {

        setConfigLoader(HoconConfigurationLoader.builder().setFile(getConfigFile()).build());

        try {

            if (!getConfigFile().isFile()) {
                getConfigFile().createNewFile();
                saveDefaults();
            }

            setConfig(getConfigLoader().load());

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private void saveDefaults() {

        try {

            setConfig(getConfigLoader().load());

        } catch (IOException e) {

            e.printStackTrace();

        }

        // Save default block-item-mappings
        List<BlockItemMapping> blockItemMappings = Mappings.getDefaultItemBlockMappings();
        blockItemMappings.forEach((mapping) -> {
            if (!mapping.getName().isEmpty()) {
                CommentedConfigurationNode node = get().getNode(BLOCK_ITEM_MAPPINGS, mapping.getName());
                node.getNode(ITEMS).setValue(mapping.getItemNamesList());
                node.getNode(BLOCKS).setValue(mapping.getBlockNamesList());
            }
        });

        // Save default torch-items
        List<String> torchItems = Mappings.getDefaultTorchItems();
        get().getNode(TORCH_ITEMS).setComment("Switch to torches, if available, when a player right-clicks on a solid block, and one of these items are currently in hand.");
        get().getNode(TORCH_ITEMS).setValue(torchItems);

        save();

    }

    /**
     * Get a list of BlockItemMapping from the mappings config for block-item-mappings.
     *
     * @return List<BlockItemMapping>
     */
    public List<BlockItemMapping> getBlockItemMappings() {

        if (!this.blockItemMappings.isEmpty()) {
            return this.blockItemMappings;
        }

        List<BlockItemMapping> mappings = new ArrayList<>();
        Collection<ItemType> itemTypes = plugin.getGame().getRegistry().getAllOf(ItemType.class);
        Collection<BlockType> blockTypes = plugin.getGame().getRegistry().getAllOf(BlockType.class);

        get().getNode(BLOCK_ITEM_MAPPINGS).getChildrenMap().keySet().forEach((entry) -> {
            String key = entry.toString();
            CommentedConfigurationNode node = get().getNode(BLOCK_ITEM_MAPPINGS, key);
            BlockItemMapping mapping = new BlockItemMapping(key);

            // Get Blocks
            List<BlockType> blocks = new ArrayList<>();
            List<String> blockNames = node.getNode(BLOCKS).getChildrenList().stream()
                    .map(ConfigurationNode::getString).collect(Collectors.toList());
            blockNames.forEach((name) -> {
                blockTypes.forEach((type) -> {
                    if (type.getName().equals(name)) {
                        blocks.add(type);
                    }
                });
            });

            // Get Items
            List<ItemType> items = new ArrayList<>();
            List<String> itemNames = node.getNode(ITEMS).getChildrenList().stream()
                    .map(ConfigurationNode::getString).collect(Collectors.toList());
            itemNames.forEach((name) -> {
                itemTypes.forEach((type) -> {
                    if (type.getName().equals(name)) {
                        items.add(type);
                    }
                });
            });

            mapping.setBlocks(blocks);
            mapping.setItems(items);
            mappings.add(mapping);
        });

        this.blockItemMappings = mappings;
        return mappings;
    }

    /**
     * Get a list of ItemType from the mappings config for torch-items.
     *
     * @return List<ItemType>
     */
    public List<ItemType> getTorchItems() {

        if (!this.torchItems.isEmpty()) {
            return this.torchItems;
        }

        Collection<ItemType> itemTypes = plugin.getGame().getRegistry().getAllOf(ItemType.class);

        List<ItemType> torchItems = new ArrayList<>();
        List<String> itemNames = get().getNode(TORCH_ITEMS).getChildrenList().stream()
                .map(ConfigurationNode::getString).collect(Collectors.toList());
        itemNames.forEach((name) -> {
            itemTypes.forEach((type) -> {
                if (type.getName().equals(name)) {
                    torchItems.add(type);
                }
            });
        });

        this.torchItems = torchItems;
        return torchItems;
    }

}
