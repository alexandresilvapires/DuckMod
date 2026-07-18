package alexduckmod.duckmod;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.chicken.Chicken;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;

public class Duck extends Animal {

    // Duck textures
    public static Identifier duck_texture = Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "textures/entity/duck/duck.png");
    public static Identifier duck_texture_melted = Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "textures/entity/duck/duck_melted.png");
    public static Identifier duck_texture_swimming = Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "textures/entity/duck/duck_s.png");
    public static Identifier duck_texture_melted_swimming = Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "textures/entity/duck/duck_melted_s.png");

    public static Identifier duck_female_texture = Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "textures/entity/duck/duck_f.png");
    public static Identifier duck_female_texture_melted = Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "textures/entity/duck/duck_f_melted.png");
    public static Identifier duck_female_texture_swimming = Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "textures/entity/duck/duck_f_s.png");
    public static Identifier duck_female_texture_melted_swimming = Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "textures/entity/duck/duck_f_melted_s.png");

    public static Identifier duck_baby_texture = Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "textures/entity/duck/duck_b.png");
    public static Identifier duck_baby_texture_melted = Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "textures/entity/duck/duck_b_melted.png");
    public static Identifier duck_baby_texture_swimming = Identifier.fromNamespaceAndPath(Duckmod.MOD_ID, "textures/entity/duck/duck_b_s.png");
    public static Identifier duck_baby_texture_melted_swimming = Identifier.fromNamespaceAndPath(Duckmod.MOD_ID,"textures/entity/duck/duck_b_melted_s.png");

    // private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0F;
    private float nextFlap = 1.0F;
    public int eggTime;
    public boolean jockey;
    public int meltTicks = 0;

    private static final EntityDimensions BABY_DIMENSIONS;

    private static final EntityDataAccessor<Boolean> FEMALE = SynchedEntityData.defineId(Duck.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PARKING = SynchedEntityData.defineId(Duck.class, EntityDataSerializers.BOOLEAN);
    //private static final boolean DEFAULT_DUCK_JOCKEY = false;

    public Duck(EntityType<? extends Duck> entityType, Level level) {
        super(entityType, level);
        this.eggTime = this.random.nextInt(6000) + 6000;

        this.setPathfindingMalus(PathType.WATER, 0.0F);

        if (!level.isClientSide()) {
            this.entityData.set(FEMALE, this.random.nextBoolean());
        }
        this.entityData.set(PARKING, false);
    }

    public boolean getFemale() {
        // return dataTracker.get(gender) == 1;
        return this.entityData.get(FEMALE);
    }

    public boolean getParking() {
        // return dataTracker.get(parking) == 1;
        return this.entityData.get(PARKING);
    }

    public Identifier getText() {
        if (this.isInWater() || this.getParking()) {
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

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4D));
        this.goalSelector.addGoal(2, new DuckBreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, (itemStack) -> {
            return itemStack.is(ItemTags.CHICKEN_FOOD);
        }, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new ParkGoal(this));
    }

    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        int randomSound = this.random.nextInt(3);

        switch (randomSound) {
            case 0:
                return ModSounds.DUCK_SAY1;
            case 1:
                return ModSounds.DUCK_SAY2;
            case 2:
                return ModSounds.DUCK_SAY3;
            default:
                return ModSounds.DUCK_SAY1;
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.DUCK_DEATH;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return (AgeableMob) BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.fromNamespaceAndPath(Duckmod.MOD_ID,"duck_entity")).create(serverLevel, EntitySpawnReason.BREEDING);
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        int randomSound = this.random.nextInt(2);

        switch (randomSound) {
            case 0:
                return ModSounds.DUCK_HURT1;
            case 1:
                return ModSounds.DUCK_HURT2;
            default:
                return ModSounds.DUCK_HURT1;
        }
    }

    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.CHICKEN_FOOD);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        // Case for melting the duck :3
        if (!this.isFood(itemStack)) {
            meltTicks = 100;
            return InteractionResult.SUCCESS;
        }
        else {
            return super.mobInteract(player, interactionHand);
        }
    }

    public void aiStep() {
        super.aiStep();
        if (meltTicks > 0)
            meltTicks--;

        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        
        if (this.isInShallowWater()) {
            this.flapSpeed *= 0.5F;
            this.flapping *= 0.5F;
        } else {
            this.flapSpeed += (this.onGround() ? -1.0F : 4.0F) * 0.3F;

            if (!this.onGround() && this.flapping < 1.0F) {
                this.flapping = 1.0F;
            }
        }

        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);

        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0 && !isInShallowWater()) {
            this.setDeltaMovement(vec3.multiply(1.0, 0.6, 1.0));
        }

        updateParkingMovement();

        // if (this.getParking()) {
        //     this.getNavigation().stop();
        // }

        this.flap += this.flapping * 2.0F;
        Level var3 = this.level();
        if (var3 instanceof ServerLevel serverLevel) {
            if (this.isAlive() && !this.isBaby() && --this.eggTime <= 0 && this.getFemale()) {
                this.spawnAtLocation(serverLevel, ModItems.DUCK_EGG);
                this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.gameEvent(GameEvent.ENTITY_PLACE);
                this.eggTime = this.random.nextInt(6000) + 6000;
            }
        }

    }

    private void updateParkingMovement() {
        var attr = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr == null) return;

        if (this.getParking()) {
            attr.setBaseValue(0.0D);
        } else {
            attr.setBaseValue(0.25D); // your normal speed
        }
    }

    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    protected void onFlap() {
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    @Override
    public void travel(Vec3 movementInput) {
        // if (this.isInWater()) {
        //     // Gentle buoyancy: stay near surface, no hopping
        //     Vec3 velocity = this.getDeltaMovement();

        //     // Kill vertical impulses
        //     double y = velocity.y;
        //     if (y > 0.02) y = 0.02;
        //     if (y < -0.02) y = -0.02;

        //     // Smooth horizontal glide
        //     this.setDeltaMovement(
        //         velocity.x * 0.9,
        //         y,
        //         velocity.z * 0.9
        //     );

        //     this.moveRelative(0.02F, movementInput);
        //     this.move(MoverType.SELF, this.getDeltaMovement());

        //     return;
        // }

        super.travel(movementInput);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FEMALE, false);
        builder.define(PARKING, false);
    }

    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.entityData.set(FEMALE, valueInput.getBooleanOr("IsFemale",this.getFemale()));
        valueInput.getInt("EggLayTime").ifPresent((integer) -> {
            this.eggTime = integer;
        });
    }

    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        // valueOutput.putInt("IsFemale", this.female);
        valueOutput.putInt("EggLayTime", this.eggTime);
        valueOutput.putBoolean("IsFemale", this.getFemale());
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.CHICKEN_STEP.value(), 0.15F, 1.0F);
    }

    @Override
    public double getFluidJumpThreshold() {
        return (double)this.getEyeHeight() < 0.4 ? 0.35 : 0.4;
    }



    public boolean removeWhenFarAway(double distanceSquared) {
        return this.hasJockey();
    }

    @Override
    protected void positionRider(Entity entity, Entity.MoveFunction moveFunction) {
        super.positionRider(entity, moveFunction);
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).yBodyRot = this.yBodyRot;
        }
    }

    @Override
    protected int calculateFallDamage(double d, float f) {
        return 0;
    }

    public boolean hasJockey() {
        return this.jockey;
    }

    public void setHasJockey(boolean hasJockey) {
        this.jockey = hasJockey;
    }

    static class ParkGoal extends Goal {
        private final Duck duck;
        private int parkDuration;

        public ParkGoal(Duck duck) {
            this.duck = duck;
            this.parkDuration = 0;
        }

        public boolean canStart() {
            boolean toStart = !this.duck.getParking() && !this.duck.isInWater()
                    && this.duck.onGround() && !this.duck.isInLava() && !this.duck.isOnFire() && !this.duck.isPanicking()
                    && this.duck.random.nextInt(600) == 1 && this.parkDuration == 0;
            return toStart;
        }

        public boolean shouldContinue() {
            if (this.parkDuration > 0 && !this.duck.isInWater()
                    && this.duck.onGround() && !this.duck.isInLava() && !this.duck.isOnFire() && !this.duck.isPanicking()) {
                this.parkDuration--;
                return true;
            } else {
                return false;
            }
        }

        public void start() {
            // this.duck.setParkingValue(1);
            this.duck.entityData.set(PARKING, true);
            this.parkDuration = 200 + this.duck.random.nextInt(400);
        }

        public void stop() {
            // this.duck.setParkingValue(0);
            this.duck.entityData.set(PARKING, false);
            this.parkDuration = 0;
        }

        @Override
        public boolean canUse() {
            return !duck.getParking() && !duck.isInWater() && duck.onGround()
                && !duck.isInLava() && !duck.isOnFire() && !duck.isPanicking()
                && duck.random.nextInt(600) == 1 && this.parkDuration == 0;
        }

        @Override
        public boolean canContinueToUse() {
            // Continue while parkDuration > 0 and still on ground & safe
            this.parkDuration--;
            return parkDuration > 0 && duck.onGround() && !duck.isInWater()
                    && !duck.isInLava() && !duck.isOnFire() && !duck.isPanicking();
        }

    }
    
    public class DuckBreedGoal extends Goal {
        private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0).ignoreLineOfSight();
        protected final Duck animal;
        protected final ServerLevel level;
        protected @Nullable Animal partner;
        private final Class<? extends Animal> partnerClass;
        private int loveTime;
        private final double speedModifier;

        public DuckBreedGoal(Duck animal, double d) {
            this(animal, d, animal.getClass());
        }

        public DuckBreedGoal(Duck animal, double d, Class<? extends Animal> class_) {
            this.animal = animal;
            this.level = getServerLevel(animal);
            this.partnerClass = class_;
            this.speedModifier = d;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            if (!this.animal.isInLove() || this.partnerClass != alexduckmod.duckmod.Duck.class) {
                return false;
            } else {
                this.partner = this.getFreePartner();
                return this.partner != null && ((Duck) this.partner).getFemale() != this.animal.getFemale();
            }
        }

        public boolean canContinueToUse() {
            return this.partner.isAlive() && this.partner.isInLove() && this.loveTime < 60 && !this.partner.isPanicking();
        }

        public void stop() {
            this.partner = null;
            this.loveTime = 0;
        }

        public void tick() {
            this.animal.getLookControl().setLookAt(this.partner, 10.0F, (float)this.animal.getMaxHeadXRot());
            this.animal.getNavigation().moveTo(this.partner, this.speedModifier);
            ++this.loveTime;
            if (this.loveTime >= this.adjustedTickDelay(60) && this.animal.distanceToSqr(this.partner) < 9.0) {
                this.breed();
            }

        }

        private @Nullable Animal getFreePartner() {
            List<? extends Animal> list = this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING, this.animal, this.animal.getBoundingBox().inflate(8.0));
            double d = Double.MAX_VALUE;
            Animal animal = null;
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
                Animal animal2 = (Animal)var5.next();
                if (this.animal.canMate(animal2) && !animal2.isPanicking() && this.animal.distanceToSqr(animal2) < d) {
                    animal = animal2;
                    d = this.animal.distanceToSqr(animal2);
                }
            }

            return animal;
        }

        protected void breed() {
            this.animal.spawnChildFromBreeding(this.level, this.partner);
        }
    }
    static {
        BABY_DIMENSIONS = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.withDefaultNamespace("chicken")).getDimensions().scale(0.5F).withEyeHeight(0.2975F);

        //BABY_DIMENSIONS = EntityType.CHICKEN.getDimensions().scale(0.5F).withEyeHeight(0.2975F);
    }
}
