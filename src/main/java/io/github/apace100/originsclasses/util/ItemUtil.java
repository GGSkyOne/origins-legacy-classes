package io.github.apace100.originsclasses.util;

import com.google.common.collect.Sets;
import io.github.apace100.originsclasses.mixin.*;
import io.github.apace100.originsclasses.mixin.CompositeEntryBaseAccessor;
import io.github.apace100.originsclasses.mixin.LootItemAccessor;
import io.github.apace100.originsclasses.mixin.LootTableAccessor;
import io.github.apace100.originsclasses.mixin.TagEntryAccessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import java.util.*;

public class ItemUtil {
    private static final Set<Item> OBTAINABLE = new HashSet<>();
    private static Item[] OBTAINABLE_ARRAY;
    private static boolean isObtainableSetBuilt = false;

    public static ItemStack createMerchantItemStack(Item item, RandomSource random, Level world) {
        ItemStack stack = new ItemStack(item);

        if (stack.has(DataComponents.ENCHANTABLE) && random.nextFloat() < 0.5) {
            EnchantmentHelper.enchantItem(random, stack, 1 + random.nextInt(30), world.registryAccess(), Optional.empty());
        }

        return stack;
    }

    public static Item getRandomObtainableItem(MinecraftServer server, RandomSource random, Set<Item> exclude) {
        buildObtainableSet(server);

        if (exclude == null || exclude.isEmpty()) {
            return OBTAINABLE_ARRAY[random.nextInt(OBTAINABLE_ARRAY.length)];
        }

        Set<Item> possibles = Sets.difference(OBTAINABLE, exclude);

        if (possibles.isEmpty()) {
            return OBTAINABLE_ARRAY[random.nextInt(OBTAINABLE_ARRAY.length)];
        }

        return possibles.toArray(new Item[0])[random.nextInt(possibles.size())];
    }

    public static void buildObtainableSet(MinecraftServer server) {
        if (isObtainableSetBuilt) {
            return;
        }

        HolderLookup<LootTable> lootTableWrapper = server
            .reloadableRegistries()
            .lookup()
            .lookupOrThrow(Registries.LOOT_TABLE);

        lootTableWrapper.listElements().forEach(entry -> {
            LootTableAccessor table = (LootTableAccessor) entry.value();
            List<LootPool> pools = table.getPools();
            Queue<LootPoolEntryContainer> entryQueue = new LinkedList<>();

            for (LootPool pool : pools) {
                entryQueue.addAll(pool.entries);
            }

            while (!entryQueue.isEmpty()) {
                LootPoolEntryContainer lootEntry = entryQueue.remove();

                if (lootEntry instanceof LootItem) {
                    OBTAINABLE.add(((LootItemAccessor)lootEntry).getItem().value());
                } else if(lootEntry instanceof TagEntry) {
                    OBTAINABLE.addAll(TagUtil.getAllEntries(BuiltInRegistries.ITEM, ((TagEntryAccessor)lootEntry).getTag()));
                } else if(lootEntry instanceof CompositeEntryBase) {
                    entryQueue.addAll(((CompositeEntryBaseAccessor)lootEntry).getChildren());
                }
            }
        });

        OBTAINABLE_ARRAY = OBTAINABLE.toArray(new Item[0]);
        isObtainableSetBuilt = true;
    }
}
