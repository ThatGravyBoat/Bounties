package tech.thatgravyboat.bounties.api.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.bounties.api.criteria.criterion.ImpossibleCriteria;
import tech.thatgravyboat.bounties.api.criteria.criterion.KillEntityCriteria;
import tech.thatgravyboat.bounties.api.criteria.criterion.ObtainItemCriteria;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Criteria {

    private static final Map<ResourceLocation, ICriteriaSerializer<?>> SERIALIZERS = new HashMap<>();

    public static final Codec<ICriteriaSerializer<?>> TYPE_CODEC = ResourceLocation.CODEC.comapFlatMap(Criteria::decode, ICriteriaSerializer::getId);
    public static final Codec<ICriteria<?, ?>> CODEC = TYPE_CODEC.dispatch(ICriteria::serializer, ICriteriaSerializer::codec);


    private static DataResult<? extends ICriteriaSerializer<?>> decode(ResourceLocation id) {
        return Optional.ofNullable(SERIALIZERS.get(id)).map(DataResult::success).orElse(DataResult.error("No property type found."));
    }

    public static <C extends ICriteria<?, C>, T extends ICriteriaSerializer<C>> void register(T serializer) {
        if (SERIALIZERS.containsKey(serializer.getId()))
            throw new RuntimeException("Multiple criteria serializers registered with same id '" + serializer.getId() +"'");
        SERIALIZERS.put(serializer.getId(), serializer);
    }

    static {
        register(KillEntityCriteria.SERIALIZER);
        register(ObtainItemCriteria.SERIALIZER);
        register(ImpossibleCriteria.SERIALIZER);
    }
}
