package alexduckmod.duckmod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModLootTables
{
    public static final ResourceKey<LootTable> DUCK_LOOTTABLE =
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "entities/duck_entity"));

}