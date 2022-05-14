package tech.thatgravyboat.bounties;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import tech.thatgravyboat.bounties.api.Bounty;
import tech.thatgravyboat.bounties.api.criteria.ICriteria;
import tech.thatgravyboat.bounties.api.criteria.criterion.KillEntityCriteria;
import tech.thatgravyboat.bounties.api.criteria.criterion.ObtainItemCriteria;
import tech.thatgravyboat.bounties.common.registry.BountyItem;
import tech.thatgravyboat.bounties.common.registry.Registries;
import tech.thatgravyboat.bounties.common.storage.BountyStorage;

public class EventManager {


    public static void onEntityKilled(Player player, Entity entity) {
        checkAndUpdateCriteria(player, entity.getType(), KillEntityCriteria.class);
    }

    public static void onItemObtained(ItemStack stack, Player player) {
        checkAndUpdateCriteria(player, stack, ObtainItemCriteria.class);
    }

    private static <T> void checkAndUpdateCriteria(Player player, T type, Class<? extends ICriteria<T, ?>> criteriaClass) {
        for (ItemStack item : player.getInventory().items) {
            if (item.is(Registries.BOUNTY.get()) && item.hasTag()) {
                Bounty bounty = BountyStorage.BOUNTIES.get(item.getOrCreateTag().getString("id"));
                if (bounty != null) {
                    for (ICriteria<?, ?> criterion : bounty.criteria()) {
                        if (criteriaClass.isInstance(criterion)) {
                            int test = criteriaClass.cast(criterion).test(type);
                            if (test > 0) {
                                BountyItem.increaseCriteriaOf(item, criterion.id(), test);
                            }
                        }
                    }
                }
            }
        }
    }

}
