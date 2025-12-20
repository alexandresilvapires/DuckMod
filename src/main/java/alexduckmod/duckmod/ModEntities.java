package alexduckmod.duckmod;

import java.util.function.Supplier;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    public static final ResourceKey<EntityType<?>> DUCK_KEY =
        ResourceKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "duck_entity")
        );

    public static final EntityType<Duck> DUCK =
        Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            DUCK_KEY,
            EntityType.Builder.of(Duck::new, MobCategory.CREATURE)
                .sized(0.4f, 0.7f).build(DUCK_KEY)
        );

    public static final ResourceKey<EntityType<?>> DUCK_EGG_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "duck_egg"));
    public static final EntityType<DuckThrownEgg> DUCK_EGG_PROJECTILE =
        Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            DUCK_EGG_KEY,
            EntityType.Builder.<DuckThrownEgg>of(DuckThrownEgg::new, MobCategory.MISC)
                .sized(0.25f, 0.25f)
                .build(DUCK_EGG_KEY)
        );
    }
// public class ModEntities {
//     public static Supplier<EntityType<Duck>> DUCK;

//     public ModEntities()
//     {
//         DUCK = registerEntityType("duck_entity", EntityType.Builder.of(Duck::new, MobCategory.CREATURE));
//     }

//     public <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, EntityType.Builder<T> builder)
//     {
//         var entityType = Registry.register(
//             BuiltInRegistries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, name),
//             builder.build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, name)))
//         );
        
//         return () -> entityType;
//     }
// }
