package tech.thatgravyboat.bounties.common.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import tech.thatgravyboat.bounties.common.registry.BountyItem;

public class CompleteCriteriaCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("complete")
                .then(Commands.argument("criteria", StringArgumentType.word())
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                .executes(context -> {
                                    String criteria = StringArgumentType.getString(context, "criteria");
                                    int amount = IntegerArgumentType.getInteger(context, "amount");
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    for (ItemStack item : player.getInventory().items) {
                                        BountyItem.increaseCriteriaOf(item, criteria, amount);
                                    }
                                    return 1;
                                })
                        )
                );
    }
}
