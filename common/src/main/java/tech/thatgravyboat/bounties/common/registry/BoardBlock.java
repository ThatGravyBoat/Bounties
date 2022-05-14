package tech.thatgravyboat.bounties.common.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.bounties.common.storage.BountyStorage;

import java.util.List;

public class BoardBlock extends Block {
    public BoardBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        if (!level.isClientSide() && hand.equals(InteractionHand.MAIN_HAND)) {
            if (BountyStorage.BOUNTIES.isEmpty()) {
                player.displayClientMessage(new TranslatableComponent("bounty.block.no_bounties"), true);
            } else {
                List<String> ids = player.getInventory().items.stream()
                        .filter(item -> item.is(Registries.BOUNTY.get()))
                        .filter(ItemStack::hasTag)
                        .map(ItemStack::getOrCreateTag)
                        .map(tag -> tag.getString("id")).toList();
                int count = 5;
                do {
                    var random = BountyStorage.getRandom();
                    if (ids.contains(random.id())) count--;
                    else player.addItem(BountyItem.getBounty(random));
                } while (count != 0 && count != 5);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.use(state, level, pos, player, hand, result);
    }
}
