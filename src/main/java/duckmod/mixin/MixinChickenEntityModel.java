package duckmod.mixin;

import duckmod.ISittable;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.entity.passive.ChickenEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;

@Mixin(ChickenEntityModel.class)
public class MixinChickenEntityModel {

    @Shadow @Final private ModelPart head;
    @Shadow @Final private ModelPart beak;
    @Shadow @Final private ModelPart wattle;    // <- was redThing
    @Shadow @Final private ModelPart body;
    @Shadow @Final private ModelPart rightLeg;
    @Shadow @Final private ModelPart leftLeg;
    @Shadow @Final private ModelPart rightWing;
    @Shadow @Final private ModelPart leftWing;

    private float orig_body;
    private float orig_head;
    private float orig_beak;
    private float orig_wattle;
    private float orig_rightWing;
    private float orig_leftWing;
    private boolean originsSaved = false;

    @Inject(method = "setAngles", at = @At("TAIL"))
    private void applySitPose(Entity entity, float limbAngle, float limbDistance,
                            float animationProgress, float headYaw, float headPitch,
                            CallbackInfo ci) {

        if (!originsSaved) {
            orig_body      = body.pivotY;
            orig_head      = head.pivotY;
            orig_beak      = beak.pivotY;
            orig_wattle    = wattle.pivotY;
            orig_rightWing = rightWing.pivotY;
            orig_leftWing  = leftWing.pivotY;
            originsSaved = true;
        }

        // Cast to ChickenEntity safely before checking ISittable
        if (entity instanceof ChickenEntity && ((ISittable) entity).isSitting()) {
            body.pivotY      = 20f;
            head.pivotY      = 19f;
            beak.pivotY      = 19f;
            wattle.pivotY    = 19f;
            rightWing.pivotY = 18f;
            leftWing.pivotY  = 18f;
            rightWing.roll   =  0.2f;
            leftWing.roll    = -0.2f;
        } else {
            body.pivotY      = orig_body;
            head.pivotY      = orig_head;
            beak.pivotY      = orig_beak;
            wattle.pivotY    = orig_wattle;
            rightWing.pivotY = orig_rightWing;
            leftWing.pivotY  = orig_leftWing;
        }
    }
}