package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends BaseContainerBlockEntity {
    @Unique
    private static Player playerTakingStacks;

    protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }


    @Inject(method = "awardUsedRecipesAndPopExperience", at = @At("HEAD"))
    private void savePlayerForLater(ServerPlayer player, CallbackInfo ci) {
        if (getType() == BlockEntityType.SMOKER) {
            playerTakingStacks = player;
        }
    }

    @Redirect(
        method = "method_17761",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/AbstractCookingRecipe;experience()F"
        )
    )
    private static float modifyExperienceGain(AbstractCookingRecipe abstractCookingRecipe) {
        float regularXp = abstractCookingRecipe.experience();

        if (playerTakingStacks != null) {
            if (ClassesPowerTypes.MORE_SMOKER_XP.isActive(playerTakingStacks)) {
                return regularXp * 2F;
            }
        }

        return regularXp;
    }
}
