package alexduckmod.duckmod;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class DuckRenderer extends MobRenderer<Duck, DuckRenderState, DuckModel>  {
    private final DuckModel model_adult;
    private final DuckModel model_baby;

    public DuckRenderer(EntityRendererProvider.Context context) {
        super(context, new DuckModel(context.bakeLayer(DuckmodClient.DUCK_LAYER)), 0.3F);
        this.model_adult = new DuckModel(context.bakeLayer(DuckmodClient.DUCK_LAYER));
        this.model_baby = new DuckModel(context.bakeLayer(DuckmodClient.DUCK_BABY_LAYER));
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

        if (duck.isBaby()) {
            // this.model = model_baby;
            DuckRenderState.isBaby = true;
        } else {
            // this.model = model_adult;
            DuckRenderState.isBaby = false;
        }
    }

    @Override
    public void submit(DuckRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        // Swap the model instance based on the state for this specific frame
        this.model = state.isBaby ? this.model_baby : this.model_adult;
        
        super.submit(state, poseStack, submitNodeCollector, cameraRenderState);
    }
}
