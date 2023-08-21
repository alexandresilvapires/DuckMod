package duckmod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import net.minecraft.entity.Entity;

public class DuckEggEntity extends ThrownItemEntity {
    public DuckEggEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public DuckEggEntity(World world, LivingEntity owner) {
        super(DuckMod.DUCK_EGG_ENTITY, owner, world);
    }

    public DuckEggEntity(World world, double x, double y, double z) {
        super(DuckMod.DUCK_EGG_ENTITY, x, y, z, world);
    }

    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        if (status == 3) {

            for (int i = 0; i < 8; ++i) {
                this.getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, this.getStack()), this.getX(),
                        this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5D) * 0.08D,
                        ((double) this.random.nextFloat() - 0.5D) * 0.08D,
                        ((double) this.random.nextFloat() - 0.5D) * 0.08D);
            }
        }

    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.damage(this.getWorld().getDamageSources().thrown(this, this.getOwner()), 0.0F);
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            if (this.random.nextInt(8) == 0) {
                int i = 1;
                if (this.random.nextInt(32) == 0) {
                    i = 4;
                }

                for (int j = 0; j < i; ++j) {
                    DuckEntity duckEntity = (DuckEntity) DuckMod.DUCK.create(this.getWorld());
                    duckEntity.setBreedingAge(-24000);
                    duckEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(),this.getYaw(), 0.0F);
                    this.getWorld().spawnEntity(duckEntity);
                }
            }

            this.getWorld().sendEntityStatus(this, (byte) 3);
            this.discard();
        }

    }

    protected Item getDefaultItem() {
        return DuckMod.DUCK_EGG;
    }
}
