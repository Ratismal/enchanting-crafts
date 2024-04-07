package me.stupidcat.enchantingcrafts.data.runes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.stupidcat.enchantingcrafts.EnchantingCrafts;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RuneDataResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    public static final String DATA_ID = "enchantment_runes";

    @Override
    public Identifier getFabricId() {
        return EnchantingCrafts.Id(DATA_ID);
    }

    @Override
    public void reload(ResourceManager manager) {
        RuneDataEntries.reset();
        manager.findResources(DATA_ID, id -> id.getPath().endsWith(".json")).forEach((resourceId, resource) -> {
            try {
                var length = DATA_ID.length();
                var id = new Identifier(resourceId.getNamespace(), resourceId.getPath().substring(length + 1, resourceId.getPath().length() - 5));

                var data = new RuneData(id);

                JsonObject runeJson = JsonParser.parseReader(new InputStreamReader(resource.getInputStream())).getAsJsonObject();
                data.parseJson(runeJson);

                RuneDataEntries.register(data);
            } catch (Exception e) {
                EnchantingCrafts.LOGGER.info("Error occurred while loading resource json " + resourceId, e);
            }
        });
    }
}
