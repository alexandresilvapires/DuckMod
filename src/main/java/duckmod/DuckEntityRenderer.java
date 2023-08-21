package duckmod;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;


@Environment(value=EnvType.CLIENT)
public class DuckEntityRenderer
extends MobEntityRenderer<DuckEntity, DuckEntityModel<DuckEntity>> {

    public DuckEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DuckEntityModel<duckmod.DuckEntity>(context.getPart(DuckMod.DUCK_LAYER)), 0.3f);
    }

    @Override
    public Identifier getTexture(DuckEntity entity) {
        return entity.getText();
    }

    @Override
    protected float getAnimationProgress(DuckEntity duckEntity, float f) {
        float g = MathHelper.lerp(f, duckEntity.prevFlapProgress, duckEntity.flapProgress);
        float h = MathHelper.lerp(f, duckEntity.prevMaxWingDeviation, duckEntity.maxWingDeviation);
        return (MathHelper.sin(g) + 1.0F) * h;
    }

}
