package alexduckmod.duckmod;

import java.util.Set;

import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.client.model.BabyModelTransform;

public class DuckModel  extends EntityModel<DuckRenderState> {
    public static final String RED_THING = "red_thing";
    public static final float Y_OFFSET = 16.0F;
    public static final MeshTransformer BABY_TRANSFORMER = new BabyModelTransform(false, 5.0F, 2.0F, 2.0F, 1.99F, 24.0F, Set.of("head", "beak", "red_thing"));
    private final ModelPart head;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    private final ModelPart body;
    private final ModelPart beak;
    private final ModelPart redThing;

    private float head_original;
    private float body_original;
    private float rightWing_original;
    private float leftWing_original;
    private float beak_original;
    private float redThing_original;

    public DuckModel(ModelPart modelPart) {
        super(modelPart);
        this.head = modelPart.getChild("head");
        this.body = modelPart.getChild("body");
        this.beak = this.head.getChild("beak");       // beak is under head
        this.redThing = this.head.getChild("red_thing"); // red_thing is under head
        this.rightLeg = modelPart.getChild("right_leg");
        this.leftLeg = modelPart.getChild("left_leg");
        this.rightWing = modelPart.getChild("right_wing");
        this.leftWing = modelPart.getChild("left_wing");

        head_original = this.head.y;
        body_original = this.body.y;
        beak_original = this.beak.y;
        redThing_original = this.redThing.y;
        rightWing_original = this.rightWing.y;
        leftWing_original = this.leftWing.y;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = createBaseChickenModel();
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    protected static MeshDefinition createBaseChickenModel() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 3.0F), PartPose.offset(0.0F, 15.0F, -4.0F));
        partDefinition2.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(14, 0).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 2.0F), PartPose.ZERO);
        partDefinition2.addOrReplaceChild("red_thing", CubeListBuilder.create().texOffs(14, 4).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
        partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 9).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 1.5707964F, 0.0F, 0.0F));
        CubeListBuilder cubeListBuilder = CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F);
        partDefinition.addOrReplaceChild("right_leg", cubeListBuilder, PartPose.offset(-2.0F, 19.0F, 1.0F));
        partDefinition.addOrReplaceChild("left_leg", cubeListBuilder, PartPose.offset(1.0F, 19.0F, 1.0F));
        partDefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(24, 13).addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), PartPose.offset(-4.0F, 13.0F, 0.0F));
        partDefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(24, 13).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), PartPose.offset(4.0F, 13.0F, 0.0F));
        return meshDefinition;
    }

    public void setupAnim(DuckRenderState duckRenderState) {
        super.setupAnim(duckRenderState);

        // Usual animation
        float f = (Mth.sin(duckRenderState.flap) + 1.0F) * duckRenderState.flapSpeed;
        this.head.xRot = duckRenderState.xRot * ((float)Math.PI / 180F);
        this.head.yRot = duckRenderState.yRot * ((float)Math.PI / 180F);

        float g = duckRenderState.walkAnimationSpeed;
        float h = duckRenderState.walkAnimationPos;
        this.rightLeg.xRot = Mth.cos(h * 0.6662F) * 1.4F * g;
        this.leftLeg.xRot = Mth.cos(h * 0.6662F + (float)Math.PI) * 1.4F * g;
        this.rightWing.zRot = f;
        this.leftWing.zRot = -f;

        // --- SITTING / PARKING ---
        if (duckRenderState.duck != null && duckRenderState.parking) {
            this.body.y = body_original + 4.0F;
            this.head.y = head_original + 4.0F;
            // this.beak.y = beak_original + 4.0F;
            // this.redThing.y = redThing_original + 4.0F;
            this.rightWing.y = rightWing_original + 4.0F;
            this.leftWing.y = leftWing_original + 4.0F;

            this.rightWing.zRot = 0.2F;
            this.leftWing.zRot = -0.2F;
        } else {
            this.body.y = body_original;
            this.head.y = head_original;
            this.beak.y = beak_original;
            this.redThing.y = redThing_original;
            this.rightWing.y = rightWing_original;
            this.leftWing.y = leftWing_original;
        }
    }

    // public void setupAnim(DuckRenderState duckRenderState) {
    //     super.setupAnim(duckRenderState);
    //     float f = (Mth.sin((double)duckRenderState.flap) + 1.0F) * duckRenderState.flapSpeed;
    //     this.head.xRot = duckRenderState.xRot * 0.017453292F;
    //     this.head.yRot = duckRenderState.yRot * 0.017453292F;
    //     float g = duckRenderState.walkAnimationSpeed;
    //     float h = duckRenderState.walkAnimationPos;
    //     this.rightLeg.xRot = Mth.cos((double)(h * 0.6662F)) * 1.4F * g;
    //     this.leftLeg.xRot = Mth.cos((double)(h * 0.6662F + 3.1415927F)) * 1.4F * g;
    //     this.rightWing.zRot = f;
    //     this.leftWing.zRot = -f;
    // }
}
