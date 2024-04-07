package me.stupidcat.enchantingcrafts;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CraftsLoot {
    private static final List<Identifier> veryRareLootTables = new ArrayList<>();
    private static final List<Identifier> rareLootTables = new ArrayList<>();
    private static final List<Identifier> uncommonLootTables = new ArrayList<>();
    private static final List<Identifier> commonLootTables = new ArrayList<>();

    private static void registerDungeonsArise() {
        // Dungeons Arise
        commonLootTables.add(new Identifier("dungeons_arise", "aviary/aviary_treasure"));
        veryRareLootTables.add(new Identifier("dungeons_arise", "bandit_towers/bandit_towers_treasure"));
        veryRareLootTables.add(new Identifier("dungeons_arise", "ceryneian_hind/ceryneian_hind_treasure"));
        veryRareLootTables.add(new Identifier("dungeons_arise", "foundry/foundry_passage_normal"));
        rareLootTables.add(new Identifier("dungeons_arise", "foundry/foundry_treasure"));;
        uncommonLootTables.add(new Identifier("dungeons_arise", "heavenly_challenger/heavenly_challenger_treasure"));
        uncommonLootTables.add(new Identifier("dungeons_arise", "heavenly_conqueror/heavenly_conqueror_treasure"));
        uncommonLootTables.add(new Identifier("dungeons_arise", "heavenly_rider/heavenly_rider_treasure"));
        rareLootTables.add(new Identifier("dungeons_arise", "illager_corsair/illager_corsair_treasure"));
        rareLootTables.add(new Identifier("dungeons_arise", "illager_fort/illager_fort_treasure"));
        rareLootTables.add(new Identifier("dungeons_arise", "illager_galley/illager_galley_treasure"));
        rareLootTables.add(new Identifier("dungeons_arise", "illager_windmill/illager_windmill_treasure"));
        uncommonLootTables.add(new Identifier("dungeons_arise", "infested_temple/infested_temple_top_treasure"));
        uncommonLootTables.add(new Identifier("dungeons_arise", "jungle_tree_house/jungle_tree_house_treasure"));
        veryRareLootTables.add(new Identifier("dungeons_arise", "keep_kayra/keep_kayra_garden_treasure"));
        uncommonLootTables.add(new Identifier("dungeons_arise", "keep_kayra/keep_kayra_library_treasure"));
        rareLootTables.add(new Identifier("dungeons_arise", "keep_kayra/keep_kayra_treasure"));
        veryRareLootTables.add(new Identifier("dungeons_arise", "lighthouse/lighthouse_top"));
        veryRareLootTables.add(new Identifier("dungeons_arise", "mines_treasure_big"));
        veryRareLootTables.add(new Identifier("dungeons_arise", "mines_treasure_medium"));
        veryRareLootTables.add(new Identifier("dungeons_arise", "mines_treasure_small"));
        rareLootTables.add(new Identifier("dungeons_arise", "mining_system/mining_system_treasure"));
        rareLootTables.add(new Identifier("dungeons_arise", "monastery/monastery_map"));
        rareLootTables.add(new Identifier("dungeons_arise", "mushroom_house/mushroom_house_treasure"));
        rareLootTables.add(new Identifier("dungeons_arise", "mushroom_mines/mushroom_mines_treasure"));
        veryRareLootTables.add(new Identifier("dungeons_arise", "mushroom_village/mushroom_village_treasure"));
        uncommonLootTables.add(new Identifier("dungeons_arise", "plague_asylum/plague_asylum_treasure"));
        uncommonLootTables.add(new Identifier("dungeons_arise", "scorched_mines/scorched_mines_treasure"));
        uncommonLootTables.add(new Identifier("dungeons_arise", "shiraz_palace/shiraz_palace_library"));
        uncommonLootTables.add(new Identifier("dungeons_arise", "shiraz_palace/shiraz_palace_treasure"));
        rareLootTables.add(new Identifier("dungeons_arise", "small_blimp/small_blimp_treasure"));
        uncommonLootTables.add(new Identifier("dungeons_arise", "thornborn_towers/thornborn_towers_top_treasure"));
        uncommonLootTables.add(new Identifier("dungeons_arise", "typhon/typhon_treasure"));
        uncommonLootTables.add(new Identifier("dungeons_arise", "undead_pirate_ship/undead_pirate_ship_enchants"));
        uncommonLootTables.add(new Identifier("dungeons_arise", "undead_pirate_ship/undead_pirate_ship_treasure"));
    }

    public static void register() {

        // 2 tries, 24% chance
        veryRareLootTables.add(new Identifier("minecraft", "chests/simple_dungeon"));
        veryRareLootTables.add(new Identifier("minecraft", "chests/abandoned_mineshaft"));
        veryRareLootTables.add(new Identifier("minecraft", "chests/pillager_outpost"));

        // 3 tries, 34% chance
        rareLootTables.add(new Identifier("minecraft", "chests/ruined_portal"));
        rareLootTables.add(new Identifier("minecraft", "chests/bastion_treasure"));

        // 3 tries, 60% chance
        uncommonLootTables.add(new Identifier("minecraft", "chests/desert_pyramid"));
        uncommonLootTables.add(new Identifier("minecraft", "chests/jungle_temple"));
        uncommonLootTables.add(new Identifier("minecraft", "chests/ancient_city"));

        // 4 tries, 70% chance
        commonLootTables.add(new Identifier("minecraft", "chests/stronghold_library"));
        commonLootTables.add(new Identifier("minecraft", "chests/end_city_treasure"));
        commonLootTables.add(new Identifier("minecraft", "chests/woodland_mansion"));
        LootTableEvents.MODIFY.register(((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin() && veryRareLootTables.contains(id)) {
                tableBuilder
                        .pool(LootPool.builder().rolls(BinomialLootNumberProvider.create(2, 0.24f))
                                .with(ItemEntry.builder(CraftsItems.BLANK_RUNE).weight(1))
                                .build()
                        );
            }

            if (source.isBuiltin() && rareLootTables.contains(id)) {
                tableBuilder
                        .pool(LootPool.builder().rolls(BinomialLootNumberProvider.create(3, 0.34f))
                                .with(ItemEntry.builder(CraftsItems.BLANK_RUNE).weight(1))
                                .build()
                        );
            }

            if (source.isBuiltin() && uncommonLootTables.contains(id)) {
                tableBuilder
                        .pool(LootPool.builder().rolls(BinomialLootNumberProvider.create(3, 0.60f))
                                .with(ItemEntry.builder(CraftsItems.BLANK_RUNE).weight(1))
                                .build()
                        );
            }

            if (source.isBuiltin() && commonLootTables.contains(id)) {
                tableBuilder
                        .pool(LootPool.builder().rolls(BinomialLootNumberProvider.create(4, 0.7f))
                                .with(ItemEntry.builder(CraftsItems.BLANK_RUNE).weight(1))
                                .build()
                        );
            }

            // idk how to do this :(
            /*
            if (source.isBuiltin() && (id.toString().equals("minecraft:chests/end_city_treasure")))
            {
                tableBuilder
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(CraftsItems.ATTUNED_RUNE))
                                .conditionally(RandomChanceLootCondition.builder(0.25f))
                                .build()
                        );
            }
             */
        }));
    }
}
