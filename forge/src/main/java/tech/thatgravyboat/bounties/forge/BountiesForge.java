package tech.thatgravyboat.bounties.forge;

import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tech.thatgravyboat.bounties.Bounties;
import tech.thatgravyboat.bounties.common.commands.BountiesCommand;
import tech.thatgravyboat.bounties.common.registry.forge.RegistriesImpl;
import tech.thatgravyboat.bounties.common.storage.BountyStorage;

@Mod(Bounties.MODID)
public class BountiesForge {

    public BountiesForge() {
        Bounties.init();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        RegistriesImpl.BLOCKS.register(bus);
        RegistriesImpl.ITEMS.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener((ResourceManagerReloadListener) BountyStorage::onReload);
    }

    @SubscribeEvent
    public void addCommands(RegisterCommandsEvent event) {
        BountiesCommand.register(event.getDispatcher());
    }
}
