package duckmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.Registries;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;

public class DuckMod implements ModInitializer {
	// Creates instance of duck egg item
	public static final Item DUCK_EGG = new DuckEggItem(new Item.Settings().maxCount(16));

	//Thrown duck egg entity
	public static final EntityType<DuckEggEntity> DUCK_EGG_ENTITY = Registry.register(
		Registries.ENTITY_TYPE,
		new Identifier("duckmod","duck_egg"),FabricEntityTypeBuilder.<DuckEggEntity>create(SpawnGroup.MISC, DuckEggEntity::new).dimensions(
			EntityDimensions.fixed(0.25f, 0.25f)).build());
	//.maxTrackingRange(4).trackingTickInterval(10)
	// Instance of Duck Mob
	public static final EntityType<DuckEntity> DUCK = Registry.register(
		Registries.ENTITY_TYPE,
		new Identifier("duckmod", "duck"),FabricEntityTypeBuilder.<DuckEntity>create(SpawnGroup.CREATURE, DuckEntity::new).dimensions(
			EntityDimensions.fixed(0.4f, 0.7f)).build());

	//Instance of duck spawn egg
	public static final SpawnEggItem DUCK_SPAWN_EGG = new SpawnEggItem(DUCK, 0x65573e, 0xffad00,new Item.Settings());


	//Duck sounds
	public static final Identifier DUCK_SAY1_ID = new Identifier("duckmod:duck_say1");
	public static final Identifier DUCK_SAY2_ID = new Identifier("duckmod:duck_say2");
	public static final Identifier DUCK_SAY3_ID = new Identifier("duckmod:duck_say3");
	public static final Identifier DUCK_HURT1_ID = new Identifier("duckmod:duck_hurt1");
	public static final Identifier DUCK_HURT2_ID = new Identifier("duckmod:duck_hurt2");
	public static final Identifier DUCK_DEATH_ID = new Identifier("duckmod:duck_death");
	public static SoundEvent DUCK_SAY1 = SoundEvent.of(DUCK_SAY1_ID);
	public static SoundEvent DUCK_SAY2 = SoundEvent.of(DUCK_SAY2_ID);
	public static SoundEvent DUCK_SAY3 = SoundEvent.of(DUCK_SAY3_ID);
	public static SoundEvent DUCK_HURT1 = SoundEvent.of(DUCK_HURT1_ID);
	public static SoundEvent DUCK_HURT2 = SoundEvent.of(DUCK_HURT2_ID);
	public static SoundEvent DUCK_DEATH = SoundEvent.of(DUCK_DEATH_ID);


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//Item registers
		Registry.register(Registries.ITEM, new Identifier("duckmod", "duck_egg"), DUCK_EGG);
		Registry.register(Registries.ITEM, new Identifier("duckmod", "duck_spawn_egg"), DUCK_SPAWN_EGG);

		//Mob registers
		FabricDefaultAttributeRegistry.register(DUCK, DuckEntity.createDuckAttributes());

		//Sound register
		Registry.register(Registries.SOUND_EVENT, DuckMod.DUCK_SAY1_ID, DUCK_SAY1);
		Registry.register(Registries.SOUND_EVENT, DuckMod.DUCK_SAY2_ID, DUCK_SAY2);
		Registry.register(Registries.SOUND_EVENT, DuckMod.DUCK_SAY3_ID, DUCK_SAY3);
		Registry.register(Registries.SOUND_EVENT, DuckMod.DUCK_HURT1_ID, DUCK_HURT1);
		Registry.register(Registries.SOUND_EVENT, DuckMod.DUCK_HURT2_ID, DUCK_HURT2);
		Registry.register(Registries.SOUND_EVENT, DuckMod.DUCK_DEATH_ID, DUCK_DEATH);


		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(DUCK_EGG));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> entries.add(DUCK_SPAWN_EGG));


		// Set spawns
		BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_RIVER),
                SpawnGroup.CREATURE, DuckMod.DUCK, 5, 2, 5);
	}
}
