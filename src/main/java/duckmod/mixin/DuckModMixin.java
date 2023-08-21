package duckmod.mixin;

import duckmod.DuckEntity;
import duckmod.DuckMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultBiomeFeatures.class)
public class DuckModMixin{
	private static final EntityType<DuckEntity> DUCK = DuckMod.DUCK;
			
	@Inject(method = "addFarmAnimals(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V", at = @At("HEAD"), cancellable = true)
	private static void addFarmAnimals(net.minecraft.world.biome.SpawnSettings.Builder builder, CallbackInfo info) {
		builder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(DUCK, 10, 2, 5));
	}
}