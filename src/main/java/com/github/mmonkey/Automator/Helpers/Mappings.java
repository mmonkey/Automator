package com.github.mmonkey.Automator.Helpers;

import com.github.mmonkey.Automator.Models.BlockItemMapping;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Mappings {

    /**
     * Get Minecraft default block to item mappings
     *
     * @return List<BlockItemMapping>
     */
    public static List<BlockItemMapping> getDefaultItemBlockMappings() {

        List<BlockItemMapping> defaultMappings = new ArrayList<>();

        BlockItemMapping axes = new BlockItemMapping("axes");
        axes.setItems(
                Arrays.asList(
                        ItemTypes.WOODEN_AXE,
                        ItemTypes.STONE_AXE,
                        ItemTypes.IRON_AXE,
                        ItemTypes.GOLDEN_AXE,
                        ItemTypes.DIAMOND_AXE
                )
        );
        axes.setBlocks(
                Arrays.asList(
                        BlockTypes.ACACIA_DOOR,
                        BlockTypes.ACACIA_FENCE,
                        BlockTypes.ACACIA_FENCE_GATE,
                        BlockTypes.ACACIA_STAIRS,
                        BlockTypes.BIRCH_DOOR,
                        BlockTypes.BIRCH_FENCE,
                        BlockTypes.BIRCH_FENCE_GATE,
                        BlockTypes.BIRCH_STAIRS,
                        BlockTypes.BOOKSHELF,
                        BlockTypes.BROWN_MUSHROOM_BLOCK,
                        BlockTypes.CHEST,
                        BlockTypes.COCOA,
                        BlockTypes.COMMAND_BLOCK,
                        BlockTypes.CRAFTING_TABLE,
                        BlockTypes.DARK_OAK_DOOR,
                        BlockTypes.DARK_OAK_FENCE,
                        BlockTypes.DARK_OAK_FENCE_GATE,
                        BlockTypes.DARK_OAK_STAIRS,
                        BlockTypes.DAYLIGHT_DETECTOR,
                        BlockTypes.DAYLIGHT_DETECTOR_INVERTED,
                        BlockTypes.DOUBLE_WOODEN_SLAB,
                        BlockTypes.FENCE,
                        BlockTypes.FENCE_GATE,
                        BlockTypes.JUKEBOX,
                        BlockTypes.JUNGLE_DOOR,
                        BlockTypes.JUNGLE_FENCE,
                        BlockTypes.JUNGLE_FENCE_GATE,
                        BlockTypes.JUNGLE_STAIRS,
                        BlockTypes.LADDER,
                        BlockTypes.LIT_PUMPKIN,
                        BlockTypes.LOG,
                        BlockTypes.LOG2,
                        BlockTypes.NOTEBLOCK,
                        BlockTypes.OAK_STAIRS,
                        BlockTypes.PLANKS,
                        BlockTypes.PUMPKIN,
                        BlockTypes.RED_MUSHROOM_BLOCK,
                        BlockTypes.SPRUCE_DOOR,
                        BlockTypes.SPRUCE_FENCE,
                        BlockTypes.SPRUCE_FENCE_GATE,
                        BlockTypes.SPRUCE_STAIRS,
                        BlockTypes.STANDING_BANNER,
                        BlockTypes.STANDING_SIGN,
                        BlockTypes.TRAPDOOR,
                        BlockTypes.TRAPPED_CHEST,
                        BlockTypes.WALL_BANNER,
                        BlockTypes.WALL_SIGN,
                        BlockTypes.WOODEN_BUTTON,
                        BlockTypes.WOODEN_DOOR,
                        BlockTypes.WOODEN_PRESSURE_PLATE,
                        BlockTypes.WOODEN_SLAB
                )
        );

        BlockItemMapping pickaxes = new BlockItemMapping("pickaxes");
        pickaxes.setItems(
                Arrays.asList(
                        ItemTypes.WOODEN_PICKAXE,
                        ItemTypes.STONE_PICKAXE,
                        ItemTypes.IRON_PICKAXE,
                        ItemTypes.GOLDEN_PICKAXE,
                        ItemTypes.DIAMOND_PICKAXE
                )
        );
        pickaxes.setBlocks(
                Arrays.asList(
                        BlockTypes.ACTIVATOR_RAIL,
                        BlockTypes.ANVIL,
                        BlockTypes.BREWING_STAND,
                        BlockTypes.BRICK_BLOCK,
                        BlockTypes.BRICK_STAIRS,
                        BlockTypes.CAULDRON,
                        BlockTypes.COAL_BLOCK,
                        BlockTypes.COAL_ORE,
                        BlockTypes.COBBLESTONE,
                        BlockTypes.COBBLESTONE_WALL,
                        BlockTypes.DETECTOR_RAIL,
                        BlockTypes.DISPENSER,
                        BlockTypes.DOUBLE_STONE_SLAB,
                        BlockTypes.DOUBLE_STONE_SLAB2,
                        BlockTypes.DROPPER,
                        BlockTypes.ENCHANTING_TABLE,
                        BlockTypes.END_STONE,
                        BlockTypes.ENDER_CHEST,
                        BlockTypes.FURNACE,
                        BlockTypes.GOLDEN_RAIL,
                        BlockTypes.HARDENED_CLAY,
                        BlockTypes.HEAVY_WEIGHTED_PRESSURE_PLATE,
                        BlockTypes.HOPPER,
                        BlockTypes.IRON_BARS,
                        BlockTypes.IRON_DOOR,
                        BlockTypes.IRON_TRAPDOOR,
                        BlockTypes.LIGHT_WEIGHTED_PRESSURE_PLATE,
                        BlockTypes.LIT_FURNACE,
                        BlockTypes.MOB_SPAWNER,
                        BlockTypes.MONSTER_EGG,
                        BlockTypes.MOSSY_COBBLESTONE,
                        BlockTypes.NETHER_BRICK,
                        BlockTypes.NETHER_BRICK_FENCE,
                        BlockTypes.NETHER_BRICK_STAIRS,
                        BlockTypes.NETHERRACK,
                        BlockTypes.PRISMARINE,
                        BlockTypes.QUARTZ_BLOCK,
                        BlockTypes.QUARTZ_ORE,
                        BlockTypes.QUARTZ_STAIRS,
                        BlockTypes.RAIL,
                        BlockTypes.RED_SANDSTONE,
                        BlockTypes.RED_SANDSTONE_STAIRS,
                        BlockTypes.REDSTONE_BLOCK,
                        BlockTypes.SANDSTONE,
                        BlockTypes.SANDSTONE_STAIRS,
                        BlockTypes.STAINED_HARDENED_CLAY,
                        BlockTypes.STONE,
                        BlockTypes.STONE_BRICK_STAIRS,
                        BlockTypes.STONE_BUTTON,
                        BlockTypes.STONE_PRESSURE_PLATE,
                        BlockTypes.STONE_SLAB,
                        BlockTypes.STONE_SLAB2,
                        BlockTypes.STONE_STAIRS,
                        BlockTypes.STONEBRICK
                )
        );

        BlockItemMapping stonePickaxes = new BlockItemMapping("pickaxes-stone");
        stonePickaxes.setItems(
                Arrays.asList(
                        ItemTypes.STONE_PICKAXE,
                        ItemTypes.IRON_PICKAXE,
                        ItemTypes.GOLDEN_PICKAXE,
                        ItemTypes.DIAMOND_PICKAXE
                )
        );
        stonePickaxes.setBlocks(
                Arrays.asList(
                        BlockTypes.IRON_BLOCK,
                        BlockTypes.IRON_ORE,
                        BlockTypes.LAPIS_BLOCK,
                        BlockTypes.LAPIS_ORE
                )
        );

        BlockItemMapping ironPickaxes = new BlockItemMapping("pickaxes-iron");
        ironPickaxes.setItems(
                Arrays.asList(
                        ItemTypes.IRON_PICKAXE,
                        ItemTypes.GOLDEN_PICKAXE,
                        ItemTypes.DIAMOND_PICKAXE
                )
        );
        ironPickaxes.setBlocks(
                Arrays.asList(
                        BlockTypes.DIAMOND_BLOCK,
                        BlockTypes.DIAMOND_ORE,
                        BlockTypes.EMERALD_BLOCK,
                        BlockTypes.EMERALD_ORE,
                        BlockTypes.GOLD_BLOCK,
                        BlockTypes.GOLD_ORE,
                        BlockTypes.LIT_REDSTONE_ORE,
                        BlockTypes.REDSTONE_ORE
                )
        );

        BlockItemMapping diamondPickaxes = new BlockItemMapping("pickaxes-diamond");
        diamondPickaxes.setItems(
                Collections.singletonList(ItemTypes.DIAMOND_PICKAXE)
        );
        diamondPickaxes.setBlocks(
                Collections.singletonList(BlockTypes.OBSIDIAN)
        );

        BlockItemMapping shears = new BlockItemMapping("shears");
        shears.setItems(
                Collections.singletonList(ItemTypes.SHEARS)
        );
        shears.setBlocks(
                Arrays.asList(
                        BlockTypes.LEAVES,
                        BlockTypes.LEAVES2,
                        BlockTypes.VINE,
                        BlockTypes.WEB,
                        BlockTypes.WOOL
                )
        );

        BlockItemMapping shovels = new BlockItemMapping("shovels");
        shovels.setItems(
                Arrays.asList(
                        ItemTypes.WOODEN_SHOVEL,
                        ItemTypes.STONE_SHOVEL,
                        ItemTypes.IRON_SHOVEL,
                        ItemTypes.GOLDEN_SHOVEL,
                        ItemTypes.DIAMOND_SHOVEL
                )
        );
        shovels.setBlocks(
                Arrays.asList(
                        BlockTypes.CLAY,
                        BlockTypes.DIRT,
                        BlockTypes.FARMLAND,
                        BlockTypes.GRASS,
                        BlockTypes.GRAVEL,
                        BlockTypes.MYCELIUM,
                        BlockTypes.SAND,
                        BlockTypes.SNOW,
                        BlockTypes.SNOW_LAYER,
                        BlockTypes.SOUL_SAND
                )
        );

        defaultMappings.add(axes);
        defaultMappings.add(pickaxes);
        defaultMappings.add(stonePickaxes);
        defaultMappings.add(ironPickaxes);
        defaultMappings.add(diamondPickaxes);
        defaultMappings.add(shears);
        defaultMappings.add(shovels);

        return defaultMappings;
    }

    /**
     * Get the default items that trigger a torch switch
     *
     * @return List<String>
     */
    public static List<String> getDefaultTorchItems() {

        List<ItemType> torchItems = Arrays.asList(
                ItemTypes.WOODEN_AXE,
                ItemTypes.STONE_AXE,
                ItemTypes.IRON_AXE,
                ItemTypes.GOLDEN_AXE,
                ItemTypes.DIAMOND_AXE,
                ItemTypes.WOODEN_PICKAXE,
                ItemTypes.STONE_PICKAXE,
                ItemTypes.IRON_PICKAXE,
                ItemTypes.GOLDEN_PICKAXE,
                ItemTypes.DIAMOND_PICKAXE,
                ItemTypes.SHEARS,
                ItemTypes.WOODEN_SHOVEL,
                ItemTypes.STONE_SHOVEL,
                ItemTypes.IRON_SHOVEL,
                ItemTypes.GOLDEN_SHOVEL,
                ItemTypes.DIAMOND_SHOVEL
        );

        return torchItems.stream().map(ItemType::getName).collect(Collectors.toList());
    }

}
