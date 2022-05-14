package tech.thatgravyboat.bounties.api.reward;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

public interface IRewardSerializer<T extends IReward<T>> {

    ResourceLocation getId();

    Codec<T> codec();

    T fromJson(JsonObject json);
}
