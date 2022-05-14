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

public record KillEntityCriteria(String id, String message, EntityTypePredicate type, int count) implements ICriteria<EntityType<?>, KillEntityCriteria> {

    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public int maxProgress() {
        return count;
    }

    @Override
    public Component getMessage() {
        return new TextComponent(message);
    }

    @Override
    public int test(EntityType<?> type) {
        return this.type.matches(type) ? 1 : 0;
    }

    @Override
    public ICriteriaSerializer<KillEntityCriteria> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements ICriteriaSerializer<KillEntityCriteria> {

        public static final Codec<KillEntityCriteria> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("id").forGetter(KillEntityCriteria::id),
                Codec.STRING.fieldOf("message").forGetter(KillEntityCriteria::message),
                CodecUtils.passthrough(EntityTypePredicate::serializeToJson, EntityTypePredicate::fromJson).fieldOf("entity").forGetter(KillEntityCriteria::type),
                Codec.INT.fieldOf("amount").orElse(1).forGetter(KillEntityCriteria::count)
        ).apply(instance, KillEntityCriteria::new));

        public static final ResourceLocation ID = new ResourceLocation(Bounties.MODID, "kill_entity");

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public Codec<KillEntityCriteria> codec() {
            return CODEC;
        }
    }
}
