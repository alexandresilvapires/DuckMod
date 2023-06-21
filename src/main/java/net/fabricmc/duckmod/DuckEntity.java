package net.fabricmc.duckmod;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DuckEntity extends AnimalEntity {

    // Duck textures
    public static Identifier duck_texture = new Identifier("duckmod", "textures/entity/duck/duck.png");
    public static Identifier duck_texture_melted = new Identifier("duckmod", "textures/entity/duck/duck_melted.png");
    public static Identifier duck_texture_swimming = new Identifier("duckmod", "textures/entity/duck/duck_s.png");
    public static Identifier duck_texture_melted_swimming = new Identifier("duckmod",
            "textures/entity/duck/duck_melted_s.png");

    public static Identifier duck_female_texture = new Identifier("duckmod", "textures/entity/duck/duck_f.png");
    public static Identifier duck_female_texture_melted = new Identifier("duckmod",
            "textures/entity/duck/duck_f_melted.png");
    public static Identifier duck_female_texture_swimming = new Identifier("duckmod",
            "textures/entity/duck/duck_f_s.png");
    public static Identifier duck_female_texture_melted_swimming = new Identifier("duckmod",
            "textures/entity/duck/duck_f_melted_s.png");

    public static Identifier duck_baby_texture = new Identifier("duckmod", "textures/entity/duck/duck_b.png");
    public static Identifier duck_baby_texture_melted = new Identifier("duckmod", "textures/entity/duck/duck_b_melted.png");
    public static Identifier duck_baby_texture_swimming = new Identifier("duckmod", "textures/entity/duck/duck_b_s.png");
    public static Identifier duck_baby_texture_melted_swimming = new Identifier("duckmod",
            "textures/entity/duck/duck_b_melted_s.png");

    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1.0F;
    public int eggLayTime;
    public boolean jockey;
    public int meltTicks = 0;
    private float field_28639 = 1.0f;

    public int female = 2;
    public int sitting = 0;

    private static final TrackedData<Integer> gender = DataTracker.registerData(DuckEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> parking = DataTracker.registerData(DuckEntity.class, TrackedDataHandlerRegistry.INTEGER);


    public DuckEntity(EntityType<? extends DuckEntity> entityType, World world) {
        super(entityType, world);

        /*
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);

        this.eggLayTime = this.random.nextInt(6000) + 6000;

        if (!this.getWorld().isClient) {
            this.female = this.random.nextInt(2);
            setGenderValue(this.female);
        } else {
            this.female = 2;
        }
        this.sitting = 0;
        */
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(gender, 2);
        this.dataTracker.startTracking(parking, 0);
    }

    protected void setGenderValue(int randomValue) {
        dataTracker.set(gender, randomValue);
    }

    protected void setParkingValue(int randomValue) {
        dataTracker.set(parking, randomValue);
    }

    public boolean getFemale() {
        return dataTracker.get(gender) == 1;
    }

    public boolean getParking() {
        return dataTracker.get(parking) == 1;
    }


    public Identifier getText() {
        if (this.isTouchingWater() || this.getParking()) {
            if (this.meltTicks == 0) {
                if(this.isBaby()){
                    return duck_baby_texture_swimming;
                } else if (this.getFemale()) {
                    return duck_female_texture_swimming;
                } else {
                    return duck_texture_swimming;
                }
            } else {
                if(this.isBaby()){
                    return duck_baby_texture_melted_swimming;
                } else if (this.getFemale()) {
                    return duck_female_texture_melted_swimming;
                } else {
                    return duck_texture_melted_swimming;
                }
            }
        } else {
            if (this.meltTicks == 0) {
                if(this.isBaby()){
                    return duck_baby_texture;
                } else if (this.getFemale()) {
                    return duck_female_texture;
                } else {
                    return duck_texture;
                }
            } else {
                if(this.isBaby()){
                    return duck_baby_texture_melted;
                } else if (this.getFemale()) {
                    return duck_female_texture_melted;
                } else {
                    return duck_texture_melted;
                }
            }
        }
    }

    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4D));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(3, new TemptGoal(this, 1.0, BREEDING_INGREDIENT, false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new ParkGoal(this));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    public void setMoveSpeed() {
        if (this.getParking()) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0D);
            Vec3d vec3d = this.getVelocity();
            this.setVelocity(vec3d.multiply(0.0D, 1.0D, 0.0D));
        }
        else{
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        int randomSound = this.random.nextInt(3);

        switch (randomSound) {
            case 0:
                return DuckMod.DUCK_SAY1;
            case 1:
                return DuckMod.DUCK_SAY2;
            case 2:
                return DuckMod.DUCK_SAY3;
            default:
                return DuckMod.DUCK_SAY1;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        int randomSound = this.random.nextInt(2);

        switch (randomSound) {
            case 0:
                return DuckMod.DUCK_HURT1;
            case 1:
                return DuckMod.DUCK_HURT2;
            default:
                return DuckMod.DUCK_HURT1;
        }
    }

    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
            EntityData entityData, NbtCompound entityTag) {

        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);

        this.eggLayTime = this.random.nextInt(6000) + 6000;

        this.female = this.random.nextInt(2);
        setGenderValue(this.female);

        this.sitting = 0;
        setParkingValue(this.sitting);
        
        //if (entityData == null) {
        //    entityData = new PassiveEntity.PassiveData(0.2F);
        //}

        return super.initialize(world, difficulty, spawnReason, (EntityData) entityData, entityTag);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return DuckMod.DUCK_DEATH;
    }

    @Override
    public DuckEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return (DuckEntity) DuckMod.DUCK.create(serverWorld);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        // Case for melting the duck :3
        if (!this.isBreedingItem(itemStack)) {
            meltTicks = 100;
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        // reduces melt ticks
        if (meltTicks > 0)
            meltTicks--;

        this.prevFlapProgress = this.flapProgress;
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation = (float) ((double) this.maxWingDeviation + (double) (this.isOnGround() ? -1 : 4) * 0.3D);
        this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0F, 1.0F);

        // Swim stuff
        if (this.isTouchingWater()) {
            this.flapSpeed = 0;
            this.maxWingDeviation = 0;
        } else if (!this.isOnGround() && this.flapSpeed < 1.0F) {
            this.flapSpeed = 1.0F;
        }

        this.flapSpeed = (float) ((double) this.flapSpeed * 0.9D);
        Vec3d vec3d = this.getVelocity();
        if (!this.isOnGround() && vec3d.y < 0.0D) {
            this.setVelocity(vec3d.multiply(1.0D, 0.6D, 1.0D));
        }

        if(!getParking()){
            this.flapProgress += this.flapSpeed * 2.0F;
        }
        else{
            this.flapProgress = 0;
        }
        if (!this.getWorld().isClient && getFemale() && this.isAlive() && !this.isBaby() && !this.hasJockey() && --this.eggLayTime <= 0) {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            this.dropItem(DuckMod.DUCK_EGG);
            this.eggLayTime = this.random.nextInt(6000) + 6000;
        }

        setMoveSpeed();
    }
    @Override
    protected boolean isFlappingWings() {
        return this.speed > this.field_28639;
    }

    @Override
    protected void addFlapEffects() {
        this.field_28639 = this.speed + this.maxWingDeviation / 2.0f;
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        if (other == this) {
            return false;
        } else if (other.getClass() != this.getClass()) {
            return false;
        } else {
            if (other instanceof DuckEntity && ((DuckEntity) other).getFemale() != this.getFemale()) {
                return this.isInLove() && other.isInLove();
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean hurtByWater() {
        return false;
    }

    public static DefaultAttributeContainer.Builder createDuckAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return this.isBaby() ? dimensions.height * 0.85F : dimensions.height * 0.92F;
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }

    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    //protected int getCurrentExperience(PlayerEntity player) {
    //    return this.hasJockey() ? 10 : super.getCurrentExperience(player);
    //}

    public void readCustomDataFromNbt(NbtCompound nbt)  {
        super.readCustomDataFromNbt(nbt);
        this.jockey = nbt.getBoolean("IsChickenJockey");
        this.eggLayTime = nbt.getInt("EggLayTime");
        this.female = nbt.getInt("Female");
        this.sitting = nbt.getInt("Parking");
        this.setGenderValue(this.female);
        this.setParkingValue(this.sitting);
    }

    public void writeCustomDataToNbt(NbtCompound nbt)  {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsChickenJockey", this.jockey);
        nbt.putInt("EggLayTime", this.eggLayTime);
        nbt.putInt("Female", this.female);
        nbt.putInt("Parking", this.sitting);
    }

    public boolean canImmediatelyDespawn(double distanceSquared) {
        return this.hasJockey();
    }

    /*@Override
    protected int getXpToDrop(PlayerEntity player) {
        if (this.hasJockey()) {
            return 10;
        }
        return super.getXpToDrop(player);
    }*/

    @Override
    protected void updatePassengerPosition(Entity passenger,Entity.PositionUpdater positionUpdater) {
        super.updatePassengerPosition(passenger, positionUpdater);
        float f = MathHelper.sin(this.bodyYaw * 0.017453292F);
        float g = MathHelper.cos(this.bodyYaw * 0.017453292F);
        passenger.updatePosition(this.getX() + (double) (0.1F * f),
                this.getBodyY(0.5D) + passenger.getHeightOffset() + 0.0D, this.getZ() - (double) (0.1F * g));
        if (passenger instanceof LivingEntity) {
            ((LivingEntity) passenger).bodyYaw = this.bodyYaw;
        }

    }

    public boolean hasJockey() {
        return this.jockey;
    }

    public void setHasJockey(boolean hasJockey) {
        this.jockey = hasJockey;
    }


    static class ParkGoal extends Goal {
        private final DuckEntity duck;
        private int nextParkAge;

        public ParkGoal(DuckEntity duck) {
            this.duck = duck;
        }

        public boolean canStart() {
            boolean toStart = this.nextParkAge < this.duck.age && !this.duck.getParking() && !this.duck.isTouchingWater()
                    && !this.duck.isFallFlying() && !this.duck.isInLava() && !this.duck.isOnFire()
                    && this.duck.random.nextInt(200) == 1;

            return toStart;
        }

        public boolean shouldContinue() {
            if (this.nextParkAge < this.duck.age && !this.duck.isTouchingWater()
                    && !this.duck.isFallFlying() && !this.duck.isInLava() && !this.duck.isOnFire()
                    && this.duck.random.nextInt(300) != 1) {
                return true;
            } else {
                return false;
            }
        }

        public void start() {
            this.duck.setParkingValue(1);
            this.duck.sitting = 1;
            this.nextParkAge = 0;
        }

        public void stop() {
            this.duck.setParkingValue(0);
            this.duck.sitting = 0;
            this.nextParkAge = this.duck.age + 200;
        }
    }
}
