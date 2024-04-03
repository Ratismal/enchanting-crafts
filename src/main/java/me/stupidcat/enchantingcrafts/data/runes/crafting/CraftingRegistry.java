package me.stupidcat.enchantingcrafts.data.runes.crafting;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class CraftingRegistry {
    private static final Map<Identifier, CraftingMethod> methods = new HashMap<>();

    public static CraftingMethod register(Identifier idenfitier, CraftingMethod method) {
        methods.put(idenfitier, method);

        return method;
    }

    public static CraftingMethod getMethod(Identifier identifier) {
        return methods.get(identifier);
    }
}
