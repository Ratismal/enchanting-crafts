package me.stupidcat.enchantingcrafts.compat.rei.category;

import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.stupidcat.enchantingcrafts.compat.rei.display.CraftingDisplay;
import net.minecraft.text.Text;

public class CraftingCategory implements DisplayCategory<CraftingDisplay> {
    @Override
    public CategoryIdentifier<? extends CraftingDisplay> getCategoryIdentifier() {
        return null;
    }

    @Override
    public Text getTitle() {
        return null;
    }

    @Override
    public Renderer getIcon() {
        return null;
    }
}
