package duckmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class DuckModClient implements ClientModInitializer {
    public static final EntityModelLayer DUCK_LAYER = new EntityModelLayer(new Identifier("duck", "duck"), "main");


    @Override
    public void onInitializeClient() {
        //Renderer register
        EntityRendererRegistry.register(DuckMod.DUCK, DuckEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(DUCK_LAYER, DuckEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(DuckMod.DUCK_EGG_ENTITY, (dispatcher) -> new FlyingItemEntityRenderer<DuckEggEntity>(dispatcher, 1.0f,false));
    }
}
