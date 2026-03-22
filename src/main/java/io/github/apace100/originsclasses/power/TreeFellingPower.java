package io.github.apace100.originsclasses.power;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;

import java.util.*;

public class TreeFellingPower extends MultiMinePower {
    private static final int BLOCK_LIMIT = 255;

    public static final PowerFactory<TreeFellingPower> FACTORY = new PowerFactory<>(
        Identifier.fromNamespaceAndPath(OriginsClasses.MODID, "tree_felling"),
        new SerializableData(),
        data -> (type, entity) -> {
            TreeFellingPower power = new TreeFellingPower(type, entity);
            power.addCondition(e -> e instanceof LivingEntity l && l.getMainHandItem().getItem() instanceof AxeItem);

            return power;
        }
    );

    public TreeFellingPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity, (pl, bs, bp) -> {
            Set<BlockPos> affected = new HashSet<>();
            Deque<BlockPos> queue = new ArrayDeque<>();
            queue.add(bp);

            boolean foundOneWithLeaves = false;

            BlockPos.MutableBlockPos pos = bp.mutable();
            BlockPos.MutableBlockPos newPos = bp.mutable();

            while (!queue.isEmpty()) {
                pos.set(queue.remove());

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = 0; dy <= 1; dy++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            if (dx == 0 && dy == 0 && dz == 0) {
                                continue;
                            }

                            newPos.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                            BlockState state = pl.level().getBlockState(newPos);

                            if (state.is(bs.getBlock()) && !affected.contains(newPos)) {
                                BlockPos savedNewPos = newPos.immutable();
                                affected.add(savedNewPos);
                                queue.add(savedNewPos);

                                if (affected.size() > BLOCK_LIMIT) {
                                    if (!foundOneWithLeaves) {
                                        return new ArrayList<>();
                                    }

                                    return new ArrayList<>(affected);
                                }
                            } else if (state.is(BlockTags.LEAVES) && !state.getValue(LeavesBlock.PERSISTENT)) {
                                foundOneWithLeaves = true;
                            }
                        }
                    }
                }
            }

            if (!foundOneWithLeaves) {
                affected.clear();
            }

            return new ArrayList<>(affected);
        },
        state -> state.is(BlockTags.LOGS));
    }
}
