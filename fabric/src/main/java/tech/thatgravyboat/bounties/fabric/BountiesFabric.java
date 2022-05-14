package tech.thatgravyboat.bounties.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.bounties.Bounties;
import tech.thatgravyboat.bounties.EventManager;
import tech.thatgravyboat.bounties.common.commands.BountiesCommand;
import tech.thatgravyboat.bounties.common.storage.BountyStorage;

public class BountiesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Bounties.init();

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation("bounties", "bounties");
            }

            @Override
            public void onResourceManagerReload(@NotNull ResourceManager manager) {
                BountyStorage.onReload(manager);
            }
        });

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (!world.isClientSide && entity instanceof Player player) {
                EventManager.onEntityKilled(player, killedEntity);
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> BountiesCommand.register(dispatcher));
    }
}
