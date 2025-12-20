package alexduckmod.duckmod;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.animal.chicken.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class DuckRenderer extends MobRenderer<Duck, DuckRenderState, DuckModel>  {
    private final DuckModel model_adult;
    private final DuckModel model_baby;

    public DuckRenderer(EntityRendererProvider.Context context) {
        super(context, new DuckModel(context.bakeLayer(ModelLayers.CHICKEN)), 0.3F);
        this.model_adult = new DuckModel(context.bakeLayer(ModelLayers.CHICKEN));
        this.model_baby = new DuckModel(context.bakeLayer(ModelLayers.CHICKEN_BABY));
    }

    public void submit(DuckRenderState duckRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (duckRenderState.isBaby == true)
            this.model = model_baby;
        else
            this.model = model_adult; 
        super.submit(duckRenderState, poseStack, submitNodeCollector, cameraRenderState);
    }

    public Identifier getTextureLocation(DuckRenderState DuckRenderState) {
        return DuckRenderState.duck.getText();
    }

    public DuckRenderState createRenderState() {
        return new DuckRenderState();
    }

    public void extractRenderState(Duck duck, DuckRenderState DuckRenderState, float f) {
        super.extractRenderState(duck, DuckRenderState, f);
        DuckRenderState.flap = Mth.lerp(f, duck.oFlap, duck.flap);
        DuckRenderState.flapSpeed = Mth.lerp(f, duck.oFlapSpeed, duck.flapSpeed);
        DuckRenderState.duck = duck;
        DuckRenderState.parking = duck.getParking();
        DuckRenderState.isBaby = duck.isBaby();
    }
}
