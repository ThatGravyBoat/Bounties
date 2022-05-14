package tech.thatgravyboat.bounties.api.criteria;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

public interface ICriteriaSerializer<T extends ICriteria<?, T>> {

    ResourceLocation getId();

    Codec<T> codec();
}
