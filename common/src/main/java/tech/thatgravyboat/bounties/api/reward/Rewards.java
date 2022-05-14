package tech.thatgravyboat.bounties.api.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.bounties.api.reward.rewards.ExperienceReward;
import tech.thatgravyboat.bounties.api.reward.rewards.ItemReward;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Rewards {

    private static final Map<ResourceLocation, IRewardSerializer<?>> SERIALIZERS = new HashMap<>();

    public static final Codec<IRewardSerializer<?>> TYPE_CODEC = ResourceLocation.CODEC.comapFlatMap(Rewards::decode, IRewardSerializer::getId);
    public static final Codec<IReward<?>> CODEC = TYPE_CODEC.dispatch(IReward::serializer, IRewardSerializer::codec);


    private static DataResult<? extends IRewardSerializer<?>> decode(ResourceLocation id) {
        return Optional.ofNullable(SERIALIZERS.get(id)).map(DataResult::success).orElse(DataResult.error("No property type found."));
    }

    public static <C extends IReward<C>, T extends IRewardSerializer<C>> void register(T serializer) {
        if (SERIALIZERS.containsKey(serializer.getId()))
            throw new RuntimeException("Multiple reward serializers registered with same id '" + serializer.getId() +"'");
        SERIALIZERS.put(serializer.getId(), serializer);
    }

    static {
        register(ExperienceReward.SERIALIZER);
        register(ItemReward.SERIALIZER);
    }
}
