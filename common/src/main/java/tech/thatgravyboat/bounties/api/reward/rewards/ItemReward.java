package tech.thatgravyboat.bounties.api.reward.rewards;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import tech.thatgravyboat.bounties.Bounties;
import tech.thatgravyboat.bounties.api.reward.IReward;
import tech.thatgravyboat.bounties.api.reward.IRewardSerializer;

public record ItemReward(ItemStack stack) implements IReward<ItemReward> {

    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public void giveReward(ServerPlayer player) {
        player.addItem(stack.copy());
    }

    @Override
    public IRewardSerializer<ItemReward> serializer() {
        return null;
    }

    private static class Serializer implements IRewardSerializer<ItemReward> {

        private static final ResourceLocation ID = new ResourceLocation(Bounties.MODID, "item");
        public static final Codec<ItemReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ItemStack.CODEC.fieldOf("data").forGetter(ItemReward::stack)
        ).apply(instance, ItemReward::new));

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public Codec<ItemReward> codec() {
            return CODEC;
        }

        @Override
        public ItemReward fromJson(JsonObject json) {
            var decode = ItemStack.CODEC.parse(JsonOps.INSTANCE, json.getAsJsonObject("data"));
            return new ItemReward(decode.result().orElseThrow());
        }
    }
}
