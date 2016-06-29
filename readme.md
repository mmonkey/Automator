# Automator

Automator is a [Sponge](https://www.spongepowered.org/) Minecraft plugin to automate repetitive tasks.

* Automatic tool switching
* Automatic block refilling
* Automatic torch switching when you are mining

## Commands

* `/auto` - List available commands
* `/auto tool` - swap tools from your inventory into your hand when you click on a block that has an associated block-item-mapping.
* `/auto torch` - when a compatible tool is in hand, if you right click a solid block, swap the in-hand tool with torches from your inventory.
* `/auto refill` - when placing stack-able blocks, if you have more in your inventory, and you place the last block of that type, move the blocks from your inventory into your hand.

## Permissions

* "auto.refill"
* "auto.tool" 
* "auto.torch"

## Default Item-Block Mappings

**Minecraft Version 1.8.9**  
All vanilla minecraft items are setup by default.

## Custom Item-Block Mappings

You can update mappings however you wish to. You may also add item mappings from other forge mods.  
For example, you can add [Pixelmon](http://pixelmonmod.com/) item-block mappings:  

mappings.conf
```
block-item-mappings {
    some-group-name {
        blocks=[
            "pixelmon:Pixelmon_Anvil"
        ]
        items=[
            "pixelmon:AmethystHammer"
            "pixelmon:CrystalHammer"
            "pixelmon:DawnstoneHammer"
            "pixelmon:DuskstoneHammer"
            "pixelmon:FirestoneHammer"
            "pixelmon:item.Aluminium_Hammer"
            "pixelmon:item.Diamond_Hammer"
            "pixelmon:item.Gold_Hammer"
            "pixelmon:item.Iron_Hammer"
            "pixelmon:item.Stone_Hammer"
            "pixelmon:item.Wood_Hammer"
            "pixelmon:LeafstoneHammer"
            "pixelmon:MoonstoneHammer"
            "pixelmon:RubyHammer"
            "pixelmon:SapphireHammer"
            "pixelmon:SunstoneHammer"
            "pixelmon:ThunderstoneHammer"
            "pixelmon:WaterstoneHammer"
        ]
    }
}

```

**Things to note:**
* [Pixelmon 4.2.7 Item Ids](https://gist.github.com/mmonkey/20ac5229017375667df2f146793524db)
* [Pixelmon 4.2.7 Block Ids](https://gist.github.com/mmonkey/5f7aa1c9ec096794d3874e51ba8bab20)
* You can name the mapping group however you would like
* Items can be used in multiple groups, however, a block can only belong to one group.

**You can mix minecraft items/blocks with forge mod items/blocks**  

mappings.conf  
```
pickaxes-iron {
        blocks=[
            "minecraft:diamond_block",
            "minecraft:diamond_ore",
            "minecraft:emerald_block",
            "minecraft:emerald_ore",
            "minecraft:gold_block",
            "minecraft:gold_ore",
            "minecraft:lit_redstone_ore",
            "minecraft:redstone_ore",
            "pixelmon:Bauxite"
        ]
        items=[
            "minecraft:iron_pickaxe",
            "minecraft:golden_pickaxe",
            "minecraft:diamond_pickaxe"
        ]
    }
```
Pixelmon's bauxite ore is now in the pickaxes-iron group.
