package io.github.apace100.originsclasses.power;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.block.LeavesBlock;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class TreeFellingPower extends MultiMinePower {
    private static final int BLOCK_LIMIT = 255;

    public static final PowerFactory<TreeFellingPower> FACTORY = new PowerFactory<>(
        Identifier.of(OriginsClasses.MODID, "tree_felling"),
        new SerializableData(),
        data -> (type, entity) -> {
            TreeFellingPower power = new TreeFellingPower(type, entity);
            power.addCondition(e -> e instanceof LivingEntity l && l.getMainHandStack().getItem() instanceof AxeItem);

            return power;
        }
    );

    public TreeFellingPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity, (pl, bs, bp) -> {
            Set<BlockPos> affected = new HashSet<>();
            Deque<BlockPos> queue = new ArrayDeque<>();
            queue.add(bp);

            boolean foundOneWithLeaves = false;

            BlockPos.Mutable pos = bp.mutableCopy();
            BlockPos.Mutable newPos = bp.mutableCopy();

            while (!queue.isEmpty()) {
                pos.set(queue.remove());

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = 0; dy <= 1; dy++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            if (dx == 0 && dy == 0 && dz == 0) {
                                continue;
                            }

                            newPos.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                            BlockState state = pl.getEntityWorld().getBlockState(newPos);

                            if (state.isOf(bs.getBlock()) && !affected.contains(newPos)) {
                                BlockPos savedNewPos = newPos.toImmutable();
                                affected.add(savedNewPos);
                                queue.add(savedNewPos);

                                if (affected.size() > BLOCK_LIMIT) {
                                    if (!foundOneWithLeaves) {
                                        return new ArrayList<>();
                                    }

                                    return new ArrayList<>(affected);
                                }
                            } else if (state.isIn(BlockTags.LEAVES) && !state.get(LeavesBlock.PERSISTENT)) {
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
        state -> state.isIn(BlockTags.LOGS));
    }
}
