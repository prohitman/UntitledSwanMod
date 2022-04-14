package com.prohitman.untitledswanmod.client.renderer;

import com.prohitman.untitledswanmod.entity.SwanEggEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class SwanEggEntityRenderer extends ThrownItemRenderer<SwanEggEntity> {
    public SwanEggEntityRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, Minecraft.getInstance().getItemRenderer());
    }

}
