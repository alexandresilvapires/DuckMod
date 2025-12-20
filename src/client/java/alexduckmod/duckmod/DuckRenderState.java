package alexduckmod.duckmod;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.jspecify.annotations.Nullable;

public class DuckRenderState extends LivingEntityRenderState {
   public float flap;
   public float flapSpeed;
   public @Nullable Duck duck;
   public @Nullable boolean parking;
   public @Nullable boolean isBaby;

   public DuckRenderState() {
      super();
   }
}
