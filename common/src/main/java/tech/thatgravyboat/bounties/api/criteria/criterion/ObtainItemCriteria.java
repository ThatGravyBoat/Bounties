package tech.thatgravyboat.bounties.api.criteria.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import tech.thatgravyboat.bounties.Bounties;
import tech.thatgravyboat.bounties.api.CodecUtils;
import tech.thatgravyboat.bounties.api.criteria.ICriteria;
import tech.thatgravyboat.bounties.api.criteria.ICriteriaSerializer;

public record ObtainItemCriteria(String id, String message, ItemPredicate stack) implements ICriteria<ItemStack, ObtainItemCriteria> {

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
    public int test(ItemStack testingStack) {
        if (!stack.matches(testingStack)) return 0;
        return 1;
    }

    @Override
    public ICriteriaSerializer<ObtainItemCriteria> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements ICriteriaSerializer<ObtainItemCriteria> {

        public static final ResourceLocation ID = new ResourceLocation(Bounties.MODID, "pickup_item");
        public static final Codec<ObtainItemCriteria> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("id").forGetter(ObtainItemCriteria::id),
                Codec.STRING.fieldOf("message").forGetter(ObtainItemCriteria::message),
                CodecUtils.passthrough(ItemPredicate::serializeToJson, ItemPredicate::fromJson).fieldOf("data").forGetter(ObtainItemCriteria::stack)
        ).apply(instance, ObtainItemCriteria::new));

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public Codec<ObtainItemCriteria> codec() {
            return CODEC;
        }
    }
}
