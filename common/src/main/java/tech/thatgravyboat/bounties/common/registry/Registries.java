package tech.thatgravyboat.bounties.common.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class Registries {

    public static final Supplier<BountyItem> BOUNTY = registerItem("bounty", () -> new BountyItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<BoardBlock> BOUNTY_BOARD = registerBlock("bounty_board",
            () -> new BoardBlock(BlockBehaviour.Properties.of(Material.WOOD)));
    public static final Supplier<BlockItem> BOUNTY_BOARD_ITEM = registerItem("bounty_board", () -> new BlockItem(BOUNTY_BOARD.get(), new Item.Properties()));

    public static void register() {
        //Init class
    }

    @ExpectPlatform
    public static <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> item) {
        throw new AssertionError();
    }
}
