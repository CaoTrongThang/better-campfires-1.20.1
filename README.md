🔥Better Campfires🔥
====================

**Better Campfires** is a Minecraft mod that enhances the functionality of campfires, allowing players to cook a wider variety of food items and enjoy additional buffs from campfires. This mod aims to improve the campfire experience in your Minecraft world, making it more versatile and enjoyable.

Features
--------

*   **Rains Can Extinguish Campfires**: Rains now can extinguish the campfires
*   **Campfires Burn Out**: Campfires is burnt out after a certain time
*   **Cooking Around Campfires**: Cook various raw items into their cooked counterparts using campfires.
*   **Custom Buffs**: Receive buffs when near a campfire, adding a strategic element to survival gameplay.
*   **Configurable Options**: Easily customize which items can be cooked and the buffs that are applied via a configuration file.

**Giving Buffs:** ![so many buffs!](https://cdn.modrinth.com/data/cached_images/b7cfa027825e822d804c57a6d1d44ad21bdf8978.png)

**Cooking Near Items:**

![cookingthings](https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExeHRqaTM2MGdoZjBwbGU5ZmlxN3U1MzFndWR4bWZnYjA0ZzJya2J4ZiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/SUFaZLbzaZLWEseMdq/giphy-downsized-large.gif)

Configuration
-------------

*   **[All the buff Ids, Read This Before Adding Any Buff, You Need To Know The Ids First](https://minecraft.fandom.com/wiki/Effect)**
*   **You can press F3 + H in the game to show the tooltips for each item, so you can get the item's id**

_After changing the config, you need to restart the game to apply new changes._

The configuration file can be found at `config/better_campfires.json`. You can customize the following settings:

*   `campfires_can_buff`: Enable or disable campfire buffs.
*   `buff_radius`: The radius within which players receive buffs.
*   `buff_check_interval`: How often buffs are checked.
*   `campfires_can_cook`: Enable or disable cooking functionality.
*   `cook_radius`: The radius within which items can be cooked.
*   `cook_check_interval`: How often cooking checks are performed.
*   `require_lit_campfire`: Whether the campfire needs to be lit for cooking.

### Example Configuration (No Supporting For Cooking Mod Items, Hope Someone Can Make A Pull Request <3)

```json
{
  "campfires_can_burn_out": true,
  "campfires_burn_out_time": 200,
  "campfires_extinguish_by_rain": true,
  "campfires_can_buff": true,
  "buff_radius": 6,
  "buff_check_interval": 30,
  "campfires_can_cook": true,
  "cook_radius": 4,
  "cook_check_interval": 20,
  "require_lit_campfire": true,
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
  ]
}
