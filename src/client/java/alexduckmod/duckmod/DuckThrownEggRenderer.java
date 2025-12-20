package alexduckmod.duckmod;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class DuckThrownEggRenderer extends ThrownItemRenderer<DuckThrownEgg> {
    public DuckThrownEggRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
}