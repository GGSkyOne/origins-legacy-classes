package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(
        method = "getAndUpdateRenderState",
        at = @At("RETURN")
    )
    @Environment(EnvType.CLIENT)
    private void hideNameForSneaky(Entity entity, float tickProgress, CallbackInfoReturnable<EntityRenderState> cir) {
        if (ClassesPowerTypes.SNEAKY.isActive(entity)) {
            cir.getReturnValue().displayName = null;
        }
    }
}
