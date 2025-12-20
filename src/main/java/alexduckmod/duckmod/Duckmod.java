package alexduckmod.duckmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Duckmod implements ModInitializer {
	public static final String MOD_ID = "duckmod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModItems.initialize();
		ModSounds.initialize();
		//new ModEntities();
		FabricDefaultAttributeRegistry.register(
			ModEntities.DUCK,
			Duck.createAttributes()
		);

		ModSpawns.register();

		LOGGER.info("Duckmod on!");

	}
}