package alexduckmod.duckmod;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;

public class ModSpawns {

    public static void register() {
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(
            Biomes.PLAINS,
            Biomes.SUNFLOWER_PLAINS,
            Biomes.SWAMP,
            Biomes.MANGROVE_SWAMP,
            Biomes.RIVER,
            Biomes.FOREST,
            Biomes.FLOWER_FOREST,
            Biomes.BIRCH_FOREST,
            Biomes.OLD_GROWTH_BIRCH_FOREST,
            Biomes.MEADOW,
            Biomes.CHERRY_GROVE,
            Biomes.TAIGA,
            Biomes.SNOWY_PLAINS,
            Biomes.JUNGLE,
            Biomes.SPARSE_JUNGLE,
            Biomes.BAMBOO_JUNGLE,
            Biomes.BEACH,
            Biomes.FROZEN_RIVER,
            Biomes.GROVE
            ),
            MobCategory.CREATURE,
            ModEntities.DUCK,
            5,  // weight
            2,   // min group
            5    // max group
        );
    }
}