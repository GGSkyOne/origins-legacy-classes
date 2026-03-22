package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import io.github.apace100.originsclasses.util.ClassesTags;
import io.github.apace100.originsclasses.util.ItemUtil;
import io.github.apace100.originsclasses.util.TagUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.RandomSource;
import java.util.Set;

@Mixin(AbstractVillager.class)
public abstract class AbstractVillagerMixin extends AgeableMob {
    @Shadow
    protected MerchantOffers offers;
    @Shadow
    private Player tradingPlayer;

    @Unique
    private int offerCountWithoutAdditional;
    @Unique
    private MerchantOffers additionalOffers;

    protected AbstractVillagerMixin(EntityType<? extends AgeableMob> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "notifyTrade",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/trading/MerchantOffer;increaseUses()V"
        )
    )
    private void dontUseUpTrades(MerchantOffer tradeOffer) {
        if (((Object)this instanceof WanderingTrader) || !ClassesPowerTypes.TRADE_AVAILABILITY.isActive(this.tradingPlayer)) {
            tradeOffer.increaseUses();
        }
    }

    @Inject(method = "setTradingPlayer", at = @At("HEAD"))
    private void addAdditionalOffers(Player customer, CallbackInfo ci) {
        if ((Object)this instanceof WanderingTrader) {
            if (ClassesPowerTypes.RARE_WANDERING_LOOT.isActive(customer)) {
                if (additionalOffers == null) {
                    offerCountWithoutAdditional = offers.size();
                    additionalOffers = buildAdditionalOffers();
                }

                this.offers.addAll(additionalOffers);
            } else if (additionalOffers != null) {
                while (this.offers.size() > offerCountWithoutAdditional) {
                    this.offers.removeLast();
                }
            }
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void addAdditionalOffers(ValueOutput view, CallbackInfo ci) {
        if (additionalOffers != null) {
            view.store("AdditionalOffers", MerchantOffers.CODEC, additionalOffers);
            view.putInt("OfferCountNoAdditional", offerCountWithoutAdditional);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readAdditionalOffers(ValueInput view, CallbackInfo ci) {
        view.read("AdditionalOffers", MerchantOffers.CODEC).ifPresent(list -> additionalOffers = list);
        offerCountWithoutAdditional = view.getInt("OfferCountNoAdditional").orElse(0);
    }

    @Unique
    private MerchantOffers buildAdditionalOffers() {
        MerchantOffers list = new MerchantOffers();
        RandomSource random = getRandom();
        Set<Item> excludedItems = TagUtil.getAllEntries(BuiltInRegistries.ITEM, ClassesTags.MERCHANT_BLACKLIST);

        list.add(
            new MerchantOffer(
                new ItemCost(
                    Items.EMERALD,
                    random.nextInt(12) + 6
                ),
                ItemUtil.createMerchantItemStack(
                    ItemUtil.getRandomObtainableItem(
                        this.level().getServer(),
                        random,
                        excludedItems
                    ),
                    random,
                    this.level()
                ),
                1,
                5,
                0.05F)
        );

        Item desiredItem = ItemUtil.getRandomObtainableItem(
            this.level().getServer(),
            random,
            excludedItems
        );

        list.add(
            new MerchantOffer(
                new ItemCost(
                    desiredItem,
                    1 + random.nextInt(Math.min(16, desiredItem.getDefaultMaxStackSize()))
                ),
                ItemUtil.createMerchantItemStack(
                    ItemUtil.getRandomObtainableItem(
                        this.level().getServer(),
                        random,
                        excludedItems
                    ),
                    random,
                    this.level()
                ),
                1,
                5,
                0.05F
            )
        );

        return list;
    }
}
