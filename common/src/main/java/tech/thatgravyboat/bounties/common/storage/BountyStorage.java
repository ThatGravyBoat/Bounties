package tech.thatgravyboat.bounties.common.storage;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import tech.thatgravyboat.bounties.Bounties;
import tech.thatgravyboat.bounties.api.Bounty;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BountyStorage {

    public static final Map<String, Bounty> BOUNTIES = new HashMap<>();
    public static final RandomCollection<String> RANDOM_BOUNTIES = new RandomCollection<>();

    public static void onReload(ResourceManager manager) {
        BOUNTIES.clear();
        RANDOM_BOUNTIES.clear();
        for (ResourceLocation item : manager.listResources("bounties", path -> path.endsWith(".json"))) {
            try (InputStream stream = manager.getResource(item).getInputStream()) {
                JsonObject jsonObject = Bounties.GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class);
                var bounty = Bounty.codec(getId(item)).parse(JsonOps.INSTANCE, jsonObject).getOrThrow(false, System.out::println);
                BOUNTIES.put(bounty.id(), bounty);
                RANDOM_BOUNTIES.add(bounty.weight(), bounty.id());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Bounty getRandom() {
        return BOUNTIES.get(RANDOM_BOUNTIES.next());
    }

    private static String getId(ResourceLocation location) {
        var path = location.getPath();
        return path.substring(path.lastIndexOf('/')+1, path.length()-5);
    }


}
