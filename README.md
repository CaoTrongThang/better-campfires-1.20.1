🔥Better Campfires🔥
====================

**Better Campfires** is a Minecraft mod designed to improve the campfire mechanics, making them more versatile and interactive for players. It introduces a variety of new features to enhance gameplay, including expanded cooking options, buffs, and more realistic campfires, which are fully configurable!

Features
--------

*   **Cooking Around Campfires**: Cook various raw items into their cooked counterparts using campfires.
*   **Custom Buffs**: Receive buffs when near a campfire, adding a strategic element to survival gameplay.
*   **Rains Can Extinguish Campfires**: Rains now can extinguish the campfires.
*   **Campfires Burn Out**: Campfires are burnt out after a certain time.
*   **Campfires Burn Hostile Mobs**: Campfires now can burn hostile mobs.
*   **Campfire Fuels**: Fuels to make your campfire burn longer. (works if Campfires Burn Out is enabled)
*   **Configurable Options**: Easily customize which items can be cooked and the buffs that are applied via a configuration file.

**Giving Buffs:** ![so many buffs!](https://cdn.modrinth.com/data/cached_images/b7cfa027825e822d804c57a6d1d44ad21bdf8978.png)

**Cooking Items:**

![cookingthings](https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExeHRqaTM2MGdoZjBwbGU5ZmlxN3U1MzFndWR4bWZnYjA0ZzJya2J4ZiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/SUFaZLbzaZLWEseMdq/giphy-downsized-large.gif)

Configuration
-------------

*   **[All the buff Ids, Read This Before Adding Any Buff, You Need To Know The Ids First](https://minecraft.fandom.com/wiki/Effect)**
*   **You can press F3 + H in the game to show the tooltips for each item, so you can get the item's id**

_After changing the config, you need to restart the game to apply new changes._

### Example Configuration (No Supporting For Cooking Mod Items, Hope Someone Can Make A Pull Request <3)

```json
{
  "campfires_can_burn_out": true,
  "campfires_burn_out_time": 3600,
  "campfires_extinguish_by_rain": true,
  "campfires_can_buff": true,
  "campfires_can_buff_for_non_hostile_mobs": true,
  "campfires_can_buff_for_hostile_mobs": true,
  "campfires_can_burn_hostile_mobs_based_on_buff_radius": false,
  "buff_radius": 7,
  "buff_check_interval": 30,
  "campfires_can_cook": true,
  "cook_radius": 4,
  "cook_check_interval": 20,
  "buffs": [
    {
      "effect": "minecraft:regeneration",
      "duration": 200,
      "amplifier": 0
    },
    {
      "effect": "minecraft:resistance",
      "duration": 200,
      "amplifier": 0
    }
  ],
  "cookable_items": [
    {
      "rawItem": "minecraft:cod",
      "cookTime": 200,
      "cookedItem": "minecraft:cooked_cod"
    },
    {
      "rawItem": "minecraft:salmon",
      "cookTime": 150,
      "cookedItem": "minecraft:cooked_salmon"
    },
    {
      "rawItem": "minecraft:beef",
      "cookTime": 300,
      "cookedItem": "minecraft:cooked_beef"
    },
    {
      "rawItem": "minecraft:chicken",
      "cookTime": 200,
      "cookedItem": "minecraft:cooked_chicken"
    },
    {
      "rawItem": "minecraft:mutton",
      "cookTime": 200,
      "cookedItem": "minecraft:cooked_mutton"
    },
    {
      "rawItem": "minecraft:porkchop",
      "cookTime": 250,
      "cookedItem": "minecraft:cooked_porkchop"
    },
    {
      "rawItem": "minecraft:rabbit",
      "cookTime": 200,
      "cookedItem": "minecraft:cooked_rabbit"
    },
    {
      "rawItem": "minecraft:potato",
      "cookTime": 100,
      "cookedItem": "minecraft:baked_potato"
    },
    {
      "rawItem": "minecraft:grass_block",
      "cookTime": 200,
      "cookedItem": "minecraft:dirt"
    }
  ],
  "campfire_fuels": [
    {
      "fuelId": "minecraft:stick",
      "addBurnTime": 100
    },
    {
      "fuelId": "minecraft:oak_log",
      "addBurnTime": 800
    },
    {
      "fuelId": "minecraft:birch_log",
      "addBurnTime": 800
    },
    {
      "fuelId": "minecraft:spruce_log",
      "addBurnTime": 800
    },
    {
      "fuelId": "minecraft:jungle_log",
      "addBurnTime": 800
    },
    {
      "fuelId": "minecraft:acacia_log",
      "addBurnTime": 800
    },
    {
      "fuelId": "minecraft:dark_oak_log",
      "addBurnTime": 800
    },
    {
      "fuelId": "minecraft:oak_planks",
      "addBurnTime": 200
    },
    {
      "fuelId": "minecraft:birch_planks",
      "addBurnTime": 200
    },
    {
      "fuelId": "minecraft:spruce_planks",
      "addBurnTime": 200
    },
    {
      "fuelId": "minecraft:jungle_planks",
      "addBurnTime": 200
    },
    {
      "fuelId": "minecraft:acacia_planks",
      "addBurnTime": 200
    },
    {
      "fuelId": "minecraft:dark_oak_planks",
      "addBurnTime": 200
    },
    {
      "fuelId": "minecraft:coal",
      "addBurnTime": 1200
    },
    {
      "fuelId": "minecraft:charcoal",
      "addBurnTime": 1200
    },
    {
      "fuelId": "minecraft:coal_block",
      "addBurnTime": 9600
    }
  ]
}
```
**[Modrinth Download](https://modrinth.com/mod/better-campfires)**

**[CursedForge Download](https://www.curseforge.com/minecraft/mc-mods/better-campfires)**
