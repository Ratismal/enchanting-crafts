package me.stupidcat.enchantingcrafts.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.stupidcat.enchantingcrafts.recipes.RuneRecipe;

import java.util.List;

public class CraftingDisplay extends BasicDisplay {
    public CraftingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return null;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return null;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return null;
    }
}
