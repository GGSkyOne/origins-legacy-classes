package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import io.github.apace100.originsclasses.util.EnchantmentContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuMixin {
    @Unique
    private Player enchanter;

    @Inject(
        method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
        at = @At("TAIL")
    )
    private void saveEnchanterInHandler(int syncId, Inventory playerInventory, ContainerLevelAccess context, CallbackInfo ci) {
        this.enchanter = playerInventory.player;
    }

    @Inject(
        method = "method_17411",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/EnchantmentMenu;getEnchantmentList(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/item/ItemStack;II)Ljava/util/List;"
        )
    )
    private void saveEnchanterForPreview(ItemStack stack, Level world, BlockPos pos, CallbackInfo ci) {
        EnchantmentContext.setClericEnchanting(ClassesPowerTypes.BETTER_ENCHANTING.isActive(enchanter));
    }

    @Inject(
        method = "method_17410",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/EnchantmentMenu;getEnchantmentList(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/item/ItemStack;II)Ljava/util/List;"
        )
    )
    private void saveEnchanter(ItemStack itemStack, int id, Player playerEntity, int level, ItemStack stack2, Level world, BlockPos pos, CallbackInfo ci) {
        EnchantmentContext.setClericEnchanting(ClassesPowerTypes.BETTER_ENCHANTING.isActive(playerEntity));
    }
}
