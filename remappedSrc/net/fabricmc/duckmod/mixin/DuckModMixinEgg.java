package net.fabricmc.duckmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.duckmod.DuckEggEntity;
import net.fabricmc.duckmod.DuckMod;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

@Mixin(ClientPlayNetworkHandler.class)
public class DuckModMixinEgg{
    @Shadow
	private ClientWorld world;

    @Environment(EnvType.CLIENT)
	@Inject(method = "onEntitySpawn", at = @At("TAIL"))
	private void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo info) {
        EntityType<?> entityType = packet.getEntityType();
        Entity entity = null;

        if (entityType == DuckMod.DUCK_EGG_ENTITY) {
            entity = new DuckEggEntity(world, packet.getX(), packet.getY(), packet.getZ());
        }

        if (entity != null) {
            entity.onSpawnPacket(packet);
            //double x = packet.getX();
            //double y = packet.getY();
            //double z = packet.getZ();
            //entity.updateTrackedPosition(x, y, z);
            //entity.setPitch = (float) (packet.getPitch() * 360) / 250F;
            //entity.setYaw = (float) (packet.getYaw() * 360) / 250F;
            //entity.setId(packet.getId());
            //entity.setUuid(packet.getUuid());
            world.addEntity(packet.getId(), entity);
        }
    }
}