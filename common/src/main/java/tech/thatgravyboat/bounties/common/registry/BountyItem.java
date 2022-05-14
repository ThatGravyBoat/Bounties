package tech.thatgravyboat.bounties.common.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.bounties.api.Bounty;
import tech.thatgravyboat.bounties.api.criteria.ICriteria;
import tech.thatgravyboat.bounties.common.registry.Registries;
import tech.thatgravyboat.bounties.common.storage.BountyStorage;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BountyItem extends Item {

    public BountyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        var tag = stack.getTag();
        if (stack.hasTag() && tag != null) {
            if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
                if (tag.contains("time")) {
                    var time = Instant.ofEpochSecond(tag.getLong("time"));
                    if (time.isBefore(Instant.now())) {
                        player.displayClientMessage(new TranslatableComponent("bounty.item.expired"), true);
                        return InteractionResultHolder.sidedSuccess(ItemStack.EMPTY, level.isClientSide());
                    }
                }
                Bounty bounty = BountyStorage.BOUNTIES.get(tag.getString("id"));
                if (bounty != null) {
                    CompoundTag criteriaTag = tag.getCompound("criteria");
                    for (ICriteria<?, ?> criterion : bounty.criteria()) {
                        var progress = criteriaTag.getCompound(criterion.id()).getInt("progress");
                        if (!criterion.finished(progress)) return InteractionResultHolder.sidedSuccess(stack, false);
                    }
                    bounty.rewards().forEach(reward -> reward.giveReward(serverPlayer));
                    return InteractionResultHolder.sidedSuccess(ItemStack.EMPTY, level.isClientSide());
                } else {
                    player.displayClientMessage(new TranslatableComponent("bounty.item.doesnt_exist"), true);
                }
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        return super.use(level, player, hand);
    }

    public static ItemStack getBounty(Bounty bounty) {
        ItemStack stack = new ItemStack(Registries.BOUNTY.get());
        var tag = stack.getOrCreateTag();
        tag.putString("id", bounty.id());
        tag.putString("title", bounty.title());
        tag.putString("description", bounty.description());
        if (bounty.seconds() > 0) tag.putLong("time", Instant.now().plusSeconds(bounty.seconds()).getEpochSecond());
        CompoundTag criteria = new CompoundTag();
        for (ICriteria<?, ?> criterion : bounty.criteria()) {
            CompoundTag criterionTag = new CompoundTag();
            criterionTag.putInt("progress", 0);
            criterionTag.putInt("max", criterion.maxProgress());
            criterionTag.putString("title", Component.Serializer.toJson(criterion.getMessage()));
            criteria.put(criterion.id(), criterionTag);
        }
        tag.put("criteria", criteria);
        stack.setTag(tag);
        return stack;
    }

    @Override
    public Component getName(ItemStack stack) {
        return stack.hasTag() ? new TextComponent(stack.getOrCreateTag().getString("title")).withStyle(ChatFormatting.BLUE) : super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        var tag = stack.getTag();
        if (tag == null || !stack.hasTag()) return;
        String desc = tag.getString("description");
        if (!desc.isBlank()) {
            for (String s : desc.split("\n")) {
                components.add(new TextComponent(s).withStyle(ChatFormatting.DARK_GRAY));
            }
        }
        Collection<String> criteriaIds = getCriteriaIds(stack);
        if (!criteriaIds.isEmpty()) {
            components.add(TextComponent.EMPTY);
            components.add(new TextComponent("Requirements:").withStyle(ChatFormatting.GREEN));
            CompoundTag criteria = tag.getCompound("criteria");
            criteriaIds.stream().map(criteria::getCompound).forEachOrdered(compound -> {
                MutableComponent component = Component.Serializer.fromJson(compound.getString("title"));
                if (component != null) component = component.withStyle(ChatFormatting.GRAY);
                int max = compound.getInt("max");
                float progress = (float) compound.getInt("progress") / max;
                String compId = progress >= 1f ? "bounty.item.tooltip.completed" : "bounty.item.tooltip.progressing";
                compId = max == 1 ? compId + ".boolean" : compId + ".percentage";
                components.add(new TranslatableComponent(compId, component, Math.min(compound.getInt("progress"), max) + "/" + max));
            });
        }
        if (tag.contains("time")) {
            components.add(TextComponent.EMPTY);
            components.add(getTimeComponent(Instant.ofEpochSecond(tag.getLong("time"))));
        }
    }

    private static Component getTimeComponent(Instant time) {
        long seconds = Instant.now().until(time, ChronoUnit.MILLIS);
        if ((seconds / 1000) > 0) {
            TextComponent text = new TextComponent(DurationFormatUtils.formatDuration(seconds, "HH:mm:ss", true));
            return new TranslatableComponent("bounty.item.tooltip.time",  text.withStyle(ChatFormatting.GREEN));
        }
        return new TranslatableComponent("bounty.item.tooltip.time_over");
    }

    private static Collection<String> getCriteriaIds(ItemStack stack) {
        if (!stack.hasTag()) return Collections.emptyList();
        return stack.getOrCreateTagElement("criteria").getAllKeys();
    }

    public static void increaseCriteriaOf(ItemStack stack, String id, int amount) {
        CompoundTag tag = stack.getTag();
        if (tag != null && stack.hasTag()) {
            var criteria1 = tag.getCompound("criteria");
            var criteria = criteria1.getCompound(id);
            criteria.putInt("progress", criteria.getInt("progress") + amount);
            criteria1.put(id, criteria);
            tag.put("criteria", criteria1);
            stack.setTag(tag);
        }
    }
}
