package tech.thatgravyboat.bounties.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public class BountiesCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal("bounties")
                .requires(cs -> cs.hasPermission(2))
                .then(CompleteCriteriaCommand.register())
        );
    }
}
