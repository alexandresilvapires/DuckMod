package alexduckmod.duckmod;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.TypedEntityData;

import java.util.function.Function;


public class ModItems {

	
    public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
		// Create the item key.
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, name));

		// Create the item instance.
		Item item = itemFactory.apply(settings.setId(itemKey));

		// Register the item.
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);

		return item;
	}
	public static final Item DUCK_EGG = register("duck_egg", DuckEggItem::new, new Item.Properties().stacksTo(16));
	public static final Item DUCK_SPAWN_EGG = register("duck_spawn_egg", props -> new SpawnEggItem(
            props.component(
                DataComponents.ENTITY_DATA,
                TypedEntityData.of(ModEntities.DUCK, new CompoundTag())
            )
        ), new Item.Properties());

	public static void initialize(){
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((itemGroup) -> itemGroup.accept(ModItems.DUCK_EGG));
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register((itemGroup) -> itemGroup.accept(ModItems.DUCK_SPAWN_EGG));
	}
}
