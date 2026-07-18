package duckmod.mixin;

import duckmod.ISittable;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

@Mixin(ChickenEntity.class)
public abstract class MixinChickenEntity implements ISittable {

    @Unique
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(
            ChickenEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerSittingTracker(EntityType<?> type, World world, CallbackInfo ci) {
        ((ChickenEntity)(Object)this).getDataTracker().startTracking(SITTING, false);
    }

    @Override
    public boolean isSitting() {
        return ((ChickenEntity)(Object)this).getDataTracker().get(SITTING);
    }

    @Override
    public void setSitting(boolean value) {
        ((ChickenEntity)(Object)this).getDataTracker().set(SITTING, value);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void addSitGoal(CallbackInfo ci) {
        ChickenEntity self = (ChickenEntity)(Object)this;

        ((MobEntityAccessor)(Object)this).getGoalSelector().add(7, new Goal() {
            private int nextSitAge = 0;

            @Override
            public boolean canStart() {
                return nextSitAge <= self.age
                        && !isSitting()
                        && !self.isTouchingWater()
                        && !self.isFallFlying()
                        && !self.isInLava()
                        && !self.isOnFire()
                        && self.getRandom().nextInt(200) == 1;
            }

            @Override
            public boolean shouldContinue() {
                return isSitting()
                        && !self.isTouchingWater()
                        && !self.isFallFlying()
                        && !self.isInLava()
                        && !self.isOnFire()
                        && self.getRandom().nextInt(300) != 1;
            }

            @Override
            public void start() {
                setSitting(true);
                self.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
                        .setBaseValue(0D);
                nextSitAge = 0;
            }

            @Override
            public void stop() {
                setSitting(false);
                self.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
                        .setBaseValue(0.25D);
                nextSitAge = self.age + 200;
            }
        });
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readSitting(NbtCompound nbt, CallbackInfo ci) {
        setSitting(nbt.getBoolean("Sitting"));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeSitting(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("Sitting", isSitting());
    }
}