package io.github.apace100.originsclasses.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.function.Predicate;

public class MultiMinePower extends Power {
    @FunctionalInterface
    public interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
    }

    private final TriFunction<LivingEntity, BlockState, BlockPos, List<BlockPos>> affectedBlocksFunction;
    private final Predicate<BlockState> isBlockStateAffected;

    public MultiMinePower(PowerType<?> type, LivingEntity entity, TriFunction<LivingEntity, BlockState, BlockPos, List<BlockPos>> affectedBlocksFunction, Predicate<BlockState> isBlockStateAffected) {
        super(type, entity);

        this.affectedBlocksFunction = affectedBlocksFunction;
        this.isBlockStateAffected = isBlockStateAffected;
    }

    public boolean isBlockStateAffected(BlockState state) {
        return isBlockStateAffected.test(state);
    }

    public List<BlockPos> getAffectedBlocks(BlockState state, BlockPos position) {
        return affectedBlocksFunction.apply(entity, state, position);
    }
}
