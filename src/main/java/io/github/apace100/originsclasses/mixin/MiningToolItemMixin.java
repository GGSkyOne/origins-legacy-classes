package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.component.ClassesComponents;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MiningToolItemMixin {
    @Inject(
        method = "getMiningSpeed",
        at = @At("RETURN"),
        cancellable = true
    )
    private void applyMiningSpeedMultiplier(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        if (stack != null) {
            Float multiplier = stack.get(ClassesComponents.MINING_SPEED_MULTIPLIER);

            if (multiplier != null) {
                float base = cir.getReturnValueF();

                if (base > 1.0F) {
                    cir.setReturnValue(base * multiplier);
                }
            }
        }
    }
}

