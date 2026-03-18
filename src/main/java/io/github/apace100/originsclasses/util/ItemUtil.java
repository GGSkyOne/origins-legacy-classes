package io.github.apace100.originsclasses.util;

import com.google.common.collect.Sets;
import io.github.apace100.originsclasses.mixin.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.CombinedEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.TagEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.*;

public class ItemUtil {
    private static final Set<Item> OBTAINABLE = new HashSet<>();
    private static Item[] OBTAINABLE_ARRAY;
    private static boolean isObtainableSetBuilt = false;

    public static ItemStack createMerchantItemStack(Item item, Random random, World world) {
        ItemStack stack = new ItemStack(item);

        if (item.isEnchantable(stack) && random.nextFloat() < 0.5) {
            EnchantmentHelper.enchant(random, stack, 1 + random.nextInt(30), world.getRegistryManager(), Optional.empty());
        }

        return stack;
    }

    public static Item getRandomObtainableItem(MinecraftServer server, Random random, Set<Item> exclude) {
        buildObtainableSet(server);

        if (exclude == null || exclude.isEmpty()) {
            return OBTAINABLE_ARRAY[random.nextInt(OBTAINABLE_ARRAY.length)];
        } else {
            Set<Item> possibles = Sets.difference(OBTAINABLE, exclude);
            Item[] items = new Item[possibles.size()];
            items = possibles.toArray(items);

            return items[random.nextInt(items.length)];
        }
    }

    public static void buildObtainableSet(MinecraftServer server) {
        if (isObtainableSetBuilt) {
            return;
        }

        RegistryWrapper<LootTable> lootTableWrapper = (RegistryWrapper<LootTable>) server
            .getReloadableRegistries()
            .createRegistryLookup()
            .getOrThrow(RegistryKeys.LOOT_TABLE);

        lootTableWrapper.streamEntries().forEach(entry -> {
            LootTableAccessor table = (LootTableAccessor) entry.value();
            List<LootPool> pools = table.getPools();
            Queue<LootPoolEntry> entryQueue = new LinkedList<>();

            for (LootPool pool : pools) {
                entryQueue.addAll(pool.entries);
            }

            while (!entryQueue.isEmpty()) {
                LootPoolEntry lootEntry = entryQueue.remove();

                if(lootEntry instanceof ItemEntry) {
                    OBTAINABLE.add(((ItemEntryAccessor)lootEntry).getItem());
                } else if(lootEntry instanceof TagEntry) {
                    OBTAINABLE.addAll(TagUtil.getAllEntries(Registries.ITEM, ((TagEntryAccessor)lootEntry).getName()));
                } else if(lootEntry instanceof CombinedEntry) {
                    entryQueue.addAll(Arrays.asList(((CombinedEntryAccessor)lootEntry).getChildren()));
                }
            }
        });

        OBTAINABLE_ARRAY = new Item[OBTAINABLE.size()];
        OBTAINABLE_ARRAY = OBTAINABLE.toArray(OBTAINABLE_ARRAY);
        isObtainableSetBuilt = true;
    }
}
