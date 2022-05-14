package tech.thatgravyboat.bounties.api.reward;

import net.minecraft.server.level.ServerPlayer;

public interface IReward<T extends IReward<T>> {

    void giveReward(ServerPlayer player);

    IRewardSerializer<T> serializer();
}
