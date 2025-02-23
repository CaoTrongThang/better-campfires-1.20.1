🔥Better Campfires🔥
====================

**Better Campfires** is a Minecraft mod designed to improve the campfire mechanics, making them more versatile and interactive for players. It introduces a variety of new features to enhance gameplay, including expanded cooking options, buffs, and more realistic campfires, which are fully configurable!

Features
--------

*   **Custom Buffs For Players**: Players receive buffs when near a campfire, adding a strategic element to survival gameplay.
*   **Campfires Can Start Unlit**: default is false in the config file.
*   **Custom Buffs For Hostile Mobs**: You can add debuffs or buffs for hostile mobs.
*   **Rains And Snow Can Extinguish Campfires**: Rains now can extinguish the campfires.
*   **Campfires Burn Out**: Campfires are burnt out after a certain time.
*   **Campfires Burn Hostile Mobs**: Campfires now can burn hostile mobs.
*   **Campfire Fuels**: Fuels to make your campfire burn longer. (works if Campfires Burn Out is enabled) 
*   **Configurable Options**: Easily customize which items can be cooked and the buffs that are applied via a configuration file.

**Giving Buffs:** ![so many buffs!](https://cdn.modrinth.com/data/cached_images/b7cfa027825e822d804c57a6d1d44ad21bdf8978.png)

Configuration
-------------

*   **[All the buff Ids, Read This Before Adding Any Buff, You Need To Know The Ids First](https://minecraft.fandom.com/wiki/Effect)**
*   **You can press F3 + H in the game to show the tooltips for each item, so you can get the item's id**

_After changing the config, you need to restart the game to apply new changes._

### Example Configuration (No Supporting For Cooking Mod Items, Hope Someone Can Make A Pull Request <3)

```json
{
  "campfires_start_unlit": false,
  "campfires_can_burn_out": true,
  "can_check_burn_out_time_left": true,
  "campfires_burn_out_time": 24000,
  "campfires_extinguish_by_rain": true,
  "rain_exinguish_time_multiply": 10,
  "campfires_extinguish_by_snow": true,
  "snow_extinguish_time_multiply": 3,
  "campfires_can_buff": true,
  "campfires_can_buff_for_non_hostile_mobs": true,
  "campfires_can_buff_for_hostile_mobs": true,
  "campfires_can_burn_hostile_mobs_based_on_buff_radius": true,
  "buff_radius": 7,
  "buff_check_interval": 30,
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
  "hostile_mob_buffs": [
    {
      "effect": "minecraft:weakness",
      "duration": 100,
      "amplifier": 0
    },
    {
      "effect": "minecraft:slowness",
      "duration": 100,
      "amplifier": 0
    }
  ],
  "campfire_fuels": [
    {
      "fuelId": "#minecraft:logs",
      "addBurnTime": 1600
    },
    {
      "fuelId": "#minecraft:planks",
      "addBurnTime": 400
    },
    {
      "fuelId": "minecraft:stick",
      "addBurnTime": 200
    },
    {
      "fuelId": "minecraft:coal",
      "addBurnTime": 2400
    },
    {
      "fuelId": "minecraft:charcoal",
      "addBurnTime": 2400
    },
    {
      "fuelId": "minecraft:coal_block",
      "addBurnTime": 19200
    }
  ]
}
```

**[Modrinth Download](https://modrinth.com/mod/better-campfires)**

**[CursedForge Download](https://www.curseforge.com/minecraft/mc-mods/better-campfires)**
