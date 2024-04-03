package me.stupidcat.enchantingcrafts.client.renderer;

import me.stupidcat.enchantingcrafts.CraftsItems;
import me.stupidcat.enchantingcrafts.EnchantingCrafts;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataEntries;
import me.stupidcat.enchantingcrafts.item.AttunedRuneItem;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.SpriteTexturedVertexConsumer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.List;
import java.util.Set;

public class AttunedRuneItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    private ModelPart sigilPart;

    private ModelPart getSigilPart() {
        if (sigilPart == null) {
            var modelData = new ModelData();
            var root = modelData.getRoot();
            var builder = ModelPartBuilder.create();
            for (var x = 0; x < 4; x++) {
                for (var y = 0; y < 4; y++) {
                    builder.mirrored().uv(12 - x * 4 - 8, 12 - y * 4)
                            .cuboid(x * 4, 1.1f, y * 4, 4f, 0f, 4f, Set.of(Direction.UP));
                }
            }

            var child = root.addChild("sigil", builder,
                    ModelTransform.of(16f, 0f, 16f, 0f, (float)Math.toRadians(180), 0f));

            sigilPart = child.createPart(16, 16);
            sigilPart.rotate(new Vector3f(0, 0, 0));
        }

        return sigilPart;
    }

    private void renderSigil(Sprite sprite, MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int i, int level, Enchantment enchant) {
        matrices.push();
        var spriteConsumer = new SpriteTexturedVertexConsumer(vertexConsumer, sprite);

        var part = getSigilPart();
        // part.translate(new Vector3f(0, 4f, 0f));
        var r = 1f;
        var g = 1f;
        var b = 1f;
        switch (level) {
            case 1 -> {
                r = 0.8f;
                g = 0.1f;
                b = 0.2f;
            }
            case 2 -> {
                r = 0.1f;
                g = 0.82f;
                b = 0.12f;
            }
            case 3 -> {
                r = 0.8f;
                g = 0.3f;
                b = 0.66f;
            }
            case 4 -> {
                r = 0.1f;
                g = 0.86f;
                b = 0.74f;
            }
            case 5 -> {
                r = 0.85f;
                g = 0.56f;
                b = 0.84f;
            }
            default -> {
                var random = Random.create();
                random.setSeed(level);
                r = random.nextBetween(1, 255) / 256f;
                g = random.nextBetween(1, 255) / 256f;
                b = random.nextBetween(1, 255) / 256f;
            }
        }

        if (level == enchant.getMaxLevel()) {
            r = 0.82f;
            g = 0.71f;
            b = 0.06f;
        }

        if (enchant.isCursed()) {
            r = 0.15f;
            g = 0.15f;
            b = 0.15f;
        }

        part.render(matrices, spriteConsumer, light, overlay, r, g, b, 0.5f);
        matrices.pop();
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        var layer = RenderLayer.getCutout();
        var consumer = vertexConsumers.getBuffer(layer);

        matrices.push();
        var renderer = MinecraftClient.getInstance().getItemRenderer();
        Random random = Random.create();
        long l = 42L;
        random.setSeed(42L);
        var entity = stack.getHolder();
        LivingEntity livingEntity = null;
        World world = null;
        if (entity instanceof LivingEntity e) {
            world = entity.getWorld();
            livingEntity = e;
        }
        var model = renderer.getModel(CraftsItems.BLANK_RUNE.getDefaultStack(), world, livingEntity, random.nextInt());

        var glintConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, layer, true, stack.hasGlint());

        for (Direction direction : Direction.values()) {
            random.setSeed(42L);
            this.renderBakedItemQuads(matrices, glintConsumer, model.getQuads(null, direction, random), stack, light, overlay);
        }
        random.setSeed(42L);
        var quads = model.getQuads(null, null, random);
        this.renderBakedItemQuads(matrices, glintConsumer, quads, stack, light, overlay);
        matrices.pop();

        var enchants = AttunedRuneItem.getEnchantments(stack);
        int i = 0;
        for (var entry : enchants.entrySet()) {
            var enchant = entry.getKey();
            var level = entry.getValue();
            var data = RuneDataEntries.runeMap.get(enchant);
            if (data != null && data.sigilId != null) {
                var sprite = getSprite(data.sigilId);
                renderSigil(sprite, matrices, consumer, light, overlay, i++, level, enchant);
            }
        }


        /*
        BakedQuad sigilQuad = null;
        for (var bakedQuad : quads) {
            var spr = bakedQuad.getSprite();
            if (bakedQuad.getFace() == Direction.UP && spr.getContents().getId().equals(EnchantingCrafts.Id("item/sigil"))) {
                MatrixStack.Entry entry = matrices.peek();

                spriteConsumer.quad(entry, bakedQuad, 1, 1, 1, light, overlay);
            }
        }
         */
        // consumer.

        // renderer.renderItem(stack, mode, light, overlay, matrices, vertexConsumers, null, 0);

        // var quads = model.

        matrices.pop();
    }

    private void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
        boolean bl = !stack.isEmpty();
        MatrixStack.Entry entry = matrices.peek();
        for (BakedQuad bakedQuad : quads) {
            int i = -1;

            if (bl && bakedQuad.hasColor()) {
                // i = this.colors.getColor(stack, bakedQuad.getColorIndex());
            }
            float f = (float)(i >> 16 & 0xFF) / 255.0f;
            float g = (float)(i >> 8 & 0xFF) / 255.0f;
            float h = (float)(i & 0xFF) / 255.0f;
            vertices.quad(entry, bakedQuad, f, g, h, light, overlay);
        }
    }

    public Sprite getSprite(Identifier id) {
        return MinecraftClient.getInstance().getBakedModelManager().getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).getSprite(id);
    }
}
