package tech.thatgravyboat.bounties.api.criteria;

import net.minecraft.network.chat.Component;

public interface ICriteria<T, C extends ICriteria<T, C>> {

    String id();

    int maxProgress();

    Component getMessage();

    default boolean finished(int progress) {
        return progress >= maxProgress();
    }

    int test(T stack);

    ICriteriaSerializer<C> serializer();
}
