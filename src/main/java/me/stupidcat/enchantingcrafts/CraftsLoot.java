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

    public static void register() {

        // 2 tries, 5% chance
        veryRareLootTables.add(new Identifier("minecraft", "chests/simple_dungeon"));
        veryRareLootTables.add(new Identifier("minecraft", "chests/abandoned_mineshaft"));
        veryRareLootTables.add(new Identifier("minecraft", "chests/pillager_outpost"));

        // 2 tries, 15% chance
        rareLootTables.add(new Identifier("minecraft", "chests/ruined_portal"));
        rareLootTables.add(new Identifier("minecraft", "chests/bastion_treasure"));

        // 3 tries, 34% chance
        uncommonLootTables.add(new Identifier("minecraft", "chests/desert_pyramid"));
        uncommonLootTables.add(new Identifier("minecraft", "chests/jungle_temple"));
        uncommonLootTables.add(new Identifier("minecraft", "chests/ancient_city"));

        // 3 tries, 70% chance
        commonLootTables.add(new Identifier("minecraft", "chests/stronghold_library"));
        commonLootTables.add(new Identifier("minecraft", "chests/end_city_treasure"));
        commonLootTables.add(new Identifier("minecraft", "chests/woodland_mansion"));
        LootTableEvents.MODIFY.register(((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin() && veryRareLootTables.contains(id)) {
                tableBuilder
                        .pool(LootPool.builder().rolls(BinomialLootNumberProvider.create(2, 0.05f))
                                .with(ItemEntry.builder(CraftsItems.BLANK_RUNE).weight(1))
                                .build()
                        );
            }

            if (source.isBuiltin() && rareLootTables.contains(id)) {
                tableBuilder
                        .pool(LootPool.builder().rolls(BinomialLootNumberProvider.create(2, 0.15f))
                                .with(ItemEntry.builder(CraftsItems.BLANK_RUNE).weight(1))
                                .build()
                        );
            }

            if (source.isBuiltin() && uncommonLootTables.contains(id)) {
                tableBuilder
                        .pool(LootPool.builder().rolls(BinomialLootNumberProvider.create(3, 0.34f))
                                .with(ItemEntry.builder(CraftsItems.BLANK_RUNE).weight(1))
                                .build()
                        );
            }

            if (source.isBuiltin() && commonLootTables.contains(id)) {
                tableBuilder
                        .pool(LootPool.builder().rolls(BinomialLootNumberProvider.create(3, 0.7f))
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
