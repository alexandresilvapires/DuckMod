package alexduckmod.duckmod;

// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class DuckThrownEgg extends ThrowableItemProjectile {
   private static final EntityDimensions ZERO_SIZED_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);

   // public DuckThrownEgg(EntityType<? extends ThrownEgg> entityType, Level level) {
   //    super(entityType, level);
   // }

   public DuckThrownEgg(EntityType<DuckThrownEgg> type, Level level){
      super(type, level);
   }

   public DuckThrownEgg(Level level, LivingEntity livingEntity, ItemStack itemStack) {
      super(ModEntities.DUCK_EGG_PROJECTILE, livingEntity, level, itemStack);
   }

   public DuckThrownEgg(Level level, double d, double e, double f, ItemStack itemStack) {
      super(ModEntities.DUCK_EGG_PROJECTILE, d, e, f, level, itemStack);
   }

   public void handleEntityEvent(byte b) {
      if (b == 3) {
         double d = 0.08;

         for(int i = 0; i < 8; ++i) {
            this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), ((double)this.random.nextFloat() - 0.5) * 0.08, ((double)this.random.nextFloat() - 0.5) * 0.08, ((double)this.random.nextFloat() - 0.5) * 0.08);
         }
      }

   }

   protected void onHitEntity(EntityHitResult entityHitResult) {
      super.onHitEntity(entityHitResult);
      entityHitResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
   }

   protected void onHit(HitResult hitResult) {
      super.onHit(hitResult);
      if (!this.level().isClientSide()) {
         if (this.random.nextInt(8) == 0) {
            int i = 1;
            if (this.random.nextInt(32) == 0) {
               i = 4;
            }

            for(int j = 0; j < i; ++j) {
               Duck duck = (Duck) ModEntities.DUCK.create(this.level(), EntitySpawnReason.TRIGGERED);
               if (duck != null) {
                  duck.setAge(-24000);
                  duck.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                  // Optional var10000 = Optional.ofNullable((EitherHolder)this.getItem().get(DataComponents.duck_VARIANT)).flatMap((eitherHolder) -> {
                  //    return eitherHolder.unwrap(this.registryAccess());
                  // });
                  Objects.requireNonNull(duck);
                  // var10000.ifPresent(duck::setVariant);
                  if (!duck.fudgePositionAfterSizeChange(ZERO_SIZED_DIMENSIONS)) {
                     break;
                  }

                  this.level().addFreshEntity(duck);
               }
            }
         }

         this.level().broadcastEntityEvent(this, (byte)3);
         this.discard();
      }

   }

   protected Item getDefaultItem() {
      return ModItems.DUCK_EGG;
   }
}
