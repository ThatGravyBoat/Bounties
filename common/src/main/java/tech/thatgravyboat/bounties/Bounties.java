package tech.thatgravyboat.bounties;

import com.google.gson.Gson;
import tech.thatgravyboat.bounties.common.registry.Registries;

public class Bounties {

    public static final String MODID = "bounties";
    public static final Gson GSON = new Gson();

    public static void init() {
        Registries.register();
    }
}
