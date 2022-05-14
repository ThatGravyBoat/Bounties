package tech.thatgravyboat.bounties.common.registry.fabric;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import tech.thatgravyboat.bounties.Bounties;

import java.util.function.Supplier;

public class RegistriesImpl {

    public static <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        T out = Registry.register(Registry.ITEM, new ResourceLocation(Bounties.MODID, id), item.get());
        return () -> out;
    }

    public static <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> item) {
        T out = Registry.register(Registry.BLOCK, new ResourceLocation(Bounties.MODID, id), item.get());
        return () -> out;
    }
}
