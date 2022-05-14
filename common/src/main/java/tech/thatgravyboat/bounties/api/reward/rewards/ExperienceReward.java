package tech.thatgravyboat.bounties.api.reward.rewards;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import tech.thatgravyboat.bounties.Bounties;
import tech.thatgravyboat.bounties.api.reward.IReward;
import tech.thatgravyboat.bounties.api.reward.IRewardSerializer;

public record ExperienceReward(Type type, int xp) implements IReward<ExperienceReward> {

    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public void giveReward(ServerPlayer player) {
        switch (type) {
            case LEVELS -> player.giveExperienceLevels(xp);
            case POINTS -> player.giveExperiencePoints(xp);
        }
    }

    @Override
    public IRewardSerializer<ExperienceReward> serializer() {
        return SERIALIZER;
    }

    public enum Type {
        LEVELS,
        POINTS;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(Type::valueOf, Type::toString);
    }

    private static class Serializer implements IRewardSerializer<ExperienceReward> {

        private static final ResourceLocation ID = new ResourceLocation(Bounties.MODID, "experience");
        public static final Codec<ExperienceReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Type.CODEC.fieldOf("drops").orElse(Type.POINTS).forGetter(ExperienceReward::type),
                Codec.INT.fieldOf("amount").forGetter(ExperienceReward::xp)
        ).apply(instance, ExperienceReward::new));

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public Codec<ExperienceReward> codec() {
            return CODEC;
        }

        @Override
        public ExperienceReward fromJson(JsonObject json) {
            return null;
        }
    }

}
