package alexduckmod.duckmod;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModSounds {
	private ModSounds() {
		// private empty constructor to avoid accidental instantiation
	}

	public static final SoundEvent DUCK_SAY1 = registerSound("duck_say1");
	public static final SoundEvent DUCK_SAY2 = registerSound("duck_say2");
    public static final SoundEvent DUCK_SAY3 = registerSound("duck_say3");
	public static final SoundEvent DUCK_HURT1 = registerSound("duck_hurt1");
    public static final SoundEvent DUCK_HURT2 = registerSound("duck_hurt2");
	public static final SoundEvent DUCK_DEATH = registerSound("duck_death");

	// actual registration of all the custom SoundEvents
	private static SoundEvent registerSound(String id) {
		Identifier identifier = Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, id);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
	}
	public static void initialize() {
		Duckmod.LOGGER.info("Registering " + Duckmod.MOD_ID + " Sounds");
	}
}