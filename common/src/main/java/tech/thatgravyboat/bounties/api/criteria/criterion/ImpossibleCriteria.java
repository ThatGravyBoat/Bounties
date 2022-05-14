package tech.thatgravyboat.bounties.api.criteria.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import tech.thatgravyboat.bounties.Bounties;
import tech.thatgravyboat.bounties.api.CodecUtils;
import tech.thatgravyboat.bounties.api.criteria.ICriteria;
import tech.thatgravyboat.bounties.api.criteria.ICriteriaSerializer;

public record ImpossibleCriteria(String id, String message) implements ICriteria<EntityType<?>, ImpossibleCriteria> {

    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public int maxProgress() {
        return 1;
    }

    @Override
    public Component getMessage() {
        return new TextComponent(message);
    }

    @Override
    public int test(EntityType<?> type) {
        return 0;
    }

    @Override
    public ICriteriaSerializer<ImpossibleCriteria> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements ICriteriaSerializer<ImpossibleCriteria> {

        public static final Codec<ImpossibleCriteria> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("id").forGetter(ImpossibleCriteria::id),
                Codec.STRING.fieldOf("message").forGetter(ImpossibleCriteria::message)
        ).apply(instance, ImpossibleCriteria::new));

        public static final ResourceLocation ID = new ResourceLocation(Bounties.MODID, "impossible");

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public Codec<ImpossibleCriteria> codec() {
            return CODEC;
        }
    }
}
