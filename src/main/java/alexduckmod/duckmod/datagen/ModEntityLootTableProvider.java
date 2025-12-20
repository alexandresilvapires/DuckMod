package alexduckmod.duckmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import alexduckmod.duckmod.ModEntities;
import alexduckmod.duckmod.ModLootTables;

import java.util.concurrent.CompletableFuture;

public class ModEntityLootTableProvider extends FabricEntityLootTableProvider
{
    protected ModEntityLootTableProvider(FabricDataOutput output, @NotNull CompletableFuture<HolderLookup.Provider> registryLookup)
    {
        super(output, registryLookup);
    }

    @Override
    public void generate() {
this.add(ModEntities.DUCK, LootTable.lootTable()
    .withPool(LootPool.lootPool()
        .setRolls(UniformGenerator.between(0.0F, 1.0F))
        .add(LootItem.lootTableItem(Items.FEATHER)
            .apply(SetItemCountFunction.setCount(
                UniformGenerator.between(0.0F, 2.0F)
            ))
        )
    )
    .withPool(LootPool.lootPool()
        .setRolls(UniformGenerator.between(1.0F, 2.0F))
        .add(LootItem.lootTableItem(Items.CHICKEN)
            .apply(SmeltItemFunction.smelted().when(this.shouldSmeltLoot()))
            .apply(EnchantedCountIncreaseFunction.lootingMultiplier(
                this.registries,
                UniformGenerator.between(0.0F, 1.0F)
            ))
        )
    )
);
    }
}