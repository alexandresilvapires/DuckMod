package duckmod;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.ChickenEntity;

public class SitGoal extends Goal {
    private final ChickenEntity chicken;
    private int nextSitAge;

    public SitGoal(ChickenEntity chicken) {
        this.chicken = chicken;
    }

    @Override
    public boolean canStart() {
        return this.nextSitAge < this.chicken.age
                && !((ISittable) this.chicken).isSitting()
                && !this.chicken.isTouchingWater()
                && !this.chicken.isFallFlying()
                && !this.chicken.isInLava()
                && !this.chicken.isOnFire()
                && this.chicken.getRandom().nextInt(200) == 1;  // <- changed
    }

    @Override
    public boolean shouldContinue() {
        return ((ISittable) this.chicken).isSitting()
                && !this.chicken.isTouchingWater()
                && !this.chicken.isFallFlying()
                && !this.chicken.isInLava()
                && !this.chicken.isOnFire()
                && this.chicken.getRandom().nextInt(300) != 1;  // <- changed
    }

    @Override
    public void start() {
        ((ISittable) this.chicken).setSitting(true);
        this.chicken.getAttributeInstance(
                net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED
        ).setBaseValue(0D);
        this.nextSitAge = 0;
    }

    @Override
    public void stop() {
        ((ISittable) this.chicken).setSitting(false);
        this.chicken.getAttributeInstance(
                net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED
        ).setBaseValue(0.25D);
        this.nextSitAge = this.chicken.age + 200;
    }
}