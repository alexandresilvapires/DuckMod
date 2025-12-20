package alexduckmod.duckmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class DuckmodClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EntityRenderers.register(
            ModEntities.DUCK,
            DuckRenderer::new
        );
		EntityRenderers.register(ModEntities.DUCK_EGG_PROJECTILE, DuckThrownEggRenderer::new);
	}
}