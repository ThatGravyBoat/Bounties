package tech.thatgravyboat.bounties.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import tech.thatgravyboat.bounties.api.criteria.Criteria;
import tech.thatgravyboat.bounties.api.criteria.ICriteria;
import tech.thatgravyboat.bounties.api.reward.IReward;
import tech.thatgravyboat.bounties.api.reward.Rewards;

import java.util.ArrayList;
import java.util.List;

public record Bounty(
        String id,
        String title,
        String description,
        int seconds,
        double weight,
        List<IReward<?>> rewards,
        List<ICriteria<?, ?>> criteria
) {

    public static Codec<Bounty> codec(String id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                MapCodec.of(Encoder.empty(), Decoder.unit(() -> id)).forGetter(Bounty::id),
                Codec.STRING.fieldOf("title").forGetter(Bounty::title),
                Codec.STRING.fieldOf("description").forGetter(Bounty::description),
                Codec.INT.fieldOf("time").orElse(60).forGetter(Bounty::seconds),
                Codec.DOUBLE.fieldOf("weight").orElse(1d).forGetter(Bounty::weight),
                Rewards.CODEC.listOf().fieldOf("rewards").orElse(new ArrayList<>()).forGetter(Bounty::rewards),
                Criteria.CODEC.listOf().fieldOf("criteria").orElse(new ArrayList<>()).forGetter(Bounty::criteria)
        ).apply(instance, Bounty::new));
    }
}
