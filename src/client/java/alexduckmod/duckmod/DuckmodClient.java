package alexduckmod.duckmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.Identifier;

public class DuckmodClient implements ClientModInitializer {

	public static final ModelLayerLocation DUCK_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "main"), null);

	public static final ModelLayerLocation DUCK_BABY_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "baby"), null);

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EntityRenderers.register(
            ModEntities.DUCK,
            DuckRenderer::new
        );
		EntityRenderers.register(ModEntities.DUCK_EGG_PROJECTILE, DuckThrownEggRenderer::new);

        ModelLayerRegistry.registerModelLayer(DUCK_LAYER, DuckModel::createBodyLayer);
		ModelLayerRegistry.registerModelLayer(DUCK_BABY_LAYER, DuckModel::createBabyLayer);

	}
}